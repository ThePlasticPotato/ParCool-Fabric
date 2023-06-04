package com.alrex.parcool.client.animation.impl;

import com.alrex.parcool.ParCool;
import com.alrex.parcool.ParCoolConfig;
import com.alrex.parcool.client.animation.Animator;
import com.alrex.parcool.client.animation.PlayerModelRotator;
import com.alrex.parcool.client.animation.PlayerModelTransformer;
import com.alrex.parcool.common.action.impl.HorizontalWallRun;
import com.alrex.parcool.common.capability.impl.Parkourability;
import com.alrex.parcool.utilities.IParCoolCamera;
import com.alrex.parcool.utilities.MathUtil;
import com.alrex.parcool.utilities.EasingFunctions;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.entity.player.PlayerEntity;

;

public class HorizontalWallRunAnimator extends Animator {
	final boolean wallIsRightSide;

	public HorizontalWallRunAnimator(boolean wallIsRightSide) {
		this.wallIsRightSide = wallIsRightSide;
	}

	@Override
	public boolean shouldRemoved(PlayerEntity player, Parkourability parkourability) {
		return !parkourability.get(HorizontalWallRun.class).isDoing();
	}

	@Override
	public void animatePost(PlayerEntity player, Parkourability parkourability, PlayerModelTransformer transformer) {
		if (wallIsRightSide) {
			transformer
					.addRotateLeftArm(0, 0, (float) Math.toRadians(-30))
					.makeArmsNatural()
					.rotateRightArm(0, 0, (float) Math.toRadians(60))
					.addRotateRightLeg(0, 0, (float) Math.toRadians(10))
					.addRotateLeftLeg(0, 0, (float) Math.toRadians(15))
					.end();
		} else {
			transformer
					.addRotateRightArm(0, 0, (float) Math.toRadians(30))
					.makeArmsNatural()
					.rotateLeftArm(0, 0, (float) Math.toRadians(-60))
					.addRotateRightLeg(0, 0, (float) Math.toRadians(-15))
					.addRotateLeftLeg(0, 0, (float) Math.toRadians(-10))
					.end();
		}
	}

	private float getFactor(float tick) {
		return tick < 5 ? 1 - MathUtil.squaring((5 - tick) / 5) : 1;
	}

	@Override
	public void rotate(PlayerEntity player, Parkourability parkourability, PlayerModelRotator rotator) {
		float factor = getFactor(getTick() + rotator.getPartialTick());
		float angle = factor * 30 * (wallIsRightSide ? -1 : 1);
		rotator
				.startBasedCenter()
				.rotateRightward(angle)
				.end();
	}

	@Override
	public void onCameraSetUp(PlayerEntity clientPlayer, Parkourability parkourability) {
		if (!MinecraftClient.getInstance().options.getPerspective().isFirstPerson() || ParCoolConfig.CONFIG_CLIENT.disableCameraHorizontalWallRun.get())
			return;
		Camera camera = MinecraftClient.getInstance().gameRenderer.getCamera();
		if (camera == null)
			return;

		float factor = getFactor((float) (getTick() + ParCool.PARTIALTICK));
		float angle = factor * 20 * (wallIsRightSide ? -1 : 1);

		IParCoolCamera parcoolCamera = (IParCoolCamera) camera;
		parcoolCamera.setRoll(angle);
	}
}
