package com.alrex.parcool.client.animation.impl;

import com.alrex.parcool.ParCool;
import com.alrex.parcool.ParCoolConfig;
import com.alrex.parcool.client.animation.Animator;
import com.alrex.parcool.client.animation.PlayerModelRotator;
import com.alrex.parcool.client.animation.PlayerModelTransformer;
import com.alrex.parcool.common.action.impl.Roll;
import com.alrex.parcool.common.capability.impl.Parkourability;
import com.alrex.parcool.utilities.MathUtil;
import com.alrex.parcool.utilities.EasingFunctions;
import com.alrex.parcool.utilities.MathUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.entity.player.PlayerEntity;

;

public class RollAnimator extends Animator {
	private final Roll.Direction direction;

	public RollAnimator(Roll.Direction direction) {
		this.direction = direction;
	}

	public static float calculateMovementFactor(float progress) {
		return -MathUtil.squaring(progress - 1) + 1;
	}

	@Override
	public boolean shouldRemoved(PlayerEntity player, Parkourability parkourability) {
		return !parkourability.get(Roll.class).isDoing();
	}

	@Override
	public void animatePost(PlayerEntity player, Parkourability parkourability, PlayerModelTransformer transformer) {
		Roll roll = parkourability.get(Roll.class);
		float phase = (roll.getDoingTick() + transformer.getPartialTick()) / (float) roll.getRollMaxTick();
		float factor = 1 - 4 * (0.5f - phase) * (0.5f - phase);
		transformer
				.addRotateLeftLeg(
						(float) Math.toRadians(-70 * factor), 0, 0
				)
				.addRotateRightLeg(
						(float) Math.toRadians(-70 * factor), 0, 0
				)
				.addRotateRightArm(
						(float) Math.toRadians(-80 * factor), 0, 0
				)
				.addRotateLeftArm(
						(float) Math.toRadians(-80 * factor), 0, 0
				)
				.end();
	}

	@Override
	public void rotate(PlayerEntity player, Parkourability parkourability, PlayerModelRotator rotator) {
		Roll roll = parkourability.get(Roll.class);
		float phase = (roll.getDoingTick() + rotator.getPartialTick()) / (float) roll.getRollMaxTick();
		float factor = calculateMovementFactor(phase);
		float sign = direction == Roll.Direction.Front ? 1 : -1;
		rotator
				.startBasedCenter()
				.rotateFrontward(sign * MathUtil.lerp(0, 360, factor))
				.end();
	}

	@Override
	public void onCameraSetUp(PlayerEntity clientPlayer, Parkourability parkourability) {
		Roll roll = parkourability.get(Roll.class);
		float sign = direction == Roll.Direction.Front ? 1 : -1;
		if (roll.isDoing() && clientPlayer.isMainPlayer() && MinecraftClient.getInstance().options.getPerspective().isFirstPerson() && !ParCoolConfig.CONFIG_CLIENT.disableCameraRolling.get()) {
			float factor = calculateMovementFactor((float) ((roll.getDoingTick() + ParCool.PARTIALTICK) / (float) roll.getRollMaxTick()));
			Camera camera = MinecraftClient.getInstance().gameRenderer.getCamera();
			if (camera == null) return;

			camera.setAnglesInternal(camera.getYaw(), sign * (factor > 0.5 ? factor - 1 : factor) * 360f + clientPlayer.getPitch((float) ParCool.PARTIALTICK));
		}
	}
}
