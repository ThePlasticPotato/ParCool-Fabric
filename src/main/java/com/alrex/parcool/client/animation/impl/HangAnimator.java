package com.alrex.parcool.client.animation.impl;

import com.alrex.parcool.ParCool;
import com.alrex.parcool.ParCoolConfig;
import com.alrex.parcool.client.animation.Animator;
import com.alrex.parcool.client.animation.PlayerModelRotator;
import com.alrex.parcool.client.animation.PlayerModelTransformer;
import com.alrex.parcool.common.action.impl.HangDown;
import com.alrex.parcool.common.capability.impl.Parkourability;
import com.alrex.parcool.utilities.EasingFunctions;
import com.alrex.parcool.utilities.MathUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.entity.player.PlayerEntity;

public class HangAnimator extends Animator {
	@Override
	public boolean shouldRemoved(PlayerEntity player, Parkourability parkourability) {
		return !parkourability.get(HangDown.class).isDoing();
	}

	@Override
	public void animatePost(PlayerEntity player, Parkourability parkourability, PlayerModelTransformer transformer) {
		HangDown hangDown = parkourability.get(HangDown.class);
		HangDown.BarAxis axis = hangDown.getHangingBarAxis();
		if (axis == null) return;
		boolean orthogonal = hangDown.isOrthogonalToBar();
		if (orthogonal) {
			float zAngle = (float) Math.toRadians(10 + 20 * Math.sin(24 * hangDown.getArmSwingAmount()));
			transformer
					.rotateRightArm(
							(float) Math.PI,
							0,
							-zAngle
					)
					.rotateLeftArm(
							(float) Math.PI,
							0,
							zAngle
					);
		} else {
			float xAngle = (float) Math.toRadians(180 + 25 * Math.sin(24 * hangDown.getArmSwingAmount()));
			transformer
					.rotateRightArm(
							xAngle,
							0,
							(float) Math.toRadians(15)
					)
					.rotateLeftArm(
							-xAngle,
							0,
							-(float) Math.toRadians(15)
					);
		}
		transformer
				.rotateRightLeg(0, 0, 0)
				.rotateLeftLeg(0, 0, 0)
				.makeLegsLittleMoving()
				.end();
	}

	private float getRotateAngle(HangDown hangDown, double partialTick) {
		return (float) (-hangDown.getBodySwingAngleFactor() * 40 * Math.sin((hangDown.getDoingTick() + partialTick) / 10 * Math.PI));
	}

	@Override
	public void rotate(PlayerEntity player, Parkourability parkourability, PlayerModelRotator rotator) {
		HangDown hangDown = parkourability.get(HangDown.class);
		rotator.startBasedTop()
				.rotateFrontward(getRotateAngle(hangDown, rotator.getPartialTick()))
				.end();
	}

	@Override
	public void onCameraSetUp(PlayerEntity clientPlayer, Parkourability parkourability) {
		if (!clientPlayer.isMainPlayer() ||
				!MinecraftClient.getInstance().options.getPerspective().isFirstPerson() ||
				ParCoolConfig.CONFIG_CLIENT.disableCameraHang.get()
		) return;

		Camera camera = MinecraftClient.getInstance().gameRenderer.getCamera();
		if (camera == null) return;

		HangDown hangDown = parkourability.get(HangDown.class);
		camera.setAnglesInternal(camera.getYaw(), (float) (clientPlayer.getPitch(ParCool.PARTIALTICK) + getRotateAngle(hangDown, ParCool.PARTIALTICK)));
	}
}
