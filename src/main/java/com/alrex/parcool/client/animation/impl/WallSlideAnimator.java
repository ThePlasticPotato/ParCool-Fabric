package com.alrex.parcool.client.animation.impl;

import com.alrex.parcool.client.animation.Animator;
import com.alrex.parcool.client.animation.PlayerModelTransformer;
import com.alrex.parcool.common.action.impl.WallSlide;
import com.alrex.parcool.common.capability.impl.Parkourability;
import com.alrex.parcool.utilities.VectorUtil;
import com.alrex.parcool.utilities.EasingFunctions;
import com.alrex.parcool.utilities.MathUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.entity.player.PlayerEntity;

;

public class WallSlideAnimator extends Animator {
	@Override
	public boolean shouldRemoved(PlayerEntity player, Parkourability parkourability) {
		return !parkourability.get(WallSlide.class).isDoing();
	}

	@Override
	public void animatePost(PlayerEntity player, Parkourability parkourability, PlayerModelTransformer transformer) {
		Vec3 wall = parkourability.get(WallSlide.class).getLeanedWallDirection();
		if (wall == null) return;
		Vec3 bodyVec = VectorUtil.fromYawDegree(player.yBodyRot);
		Vec3 vec = new Vec3(bodyVec.x, 0, bodyVec.z).normalize();

		Vec3 dividedVec =
				new Vec3(
						vec.x * wall.x + vec.z * wall.z, 0,
						-vec.x * wall.z + vec.z * wall.x
				).normalize();
		if (dividedVec.z < 0) {
			transformer
					.rotateRightArm(
							(float) Math.toRadians(-160),
							(float) -Math.toRadians(VectorUtil.toYawDegree(dividedVec) + 90),
							0
					)
					.end();
		} else {
			transformer
					.rotateLeftArm(
							(float) Math.toRadians(-160),
							(float) -Math.toRadians(VectorUtil.toYawDegree(dividedVec) + 90),
							0
					)
					.end();
		}
	}
}
