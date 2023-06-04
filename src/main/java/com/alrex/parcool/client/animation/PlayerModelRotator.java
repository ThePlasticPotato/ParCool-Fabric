package com.alrex.parcool.client.animation;

import com.alrex.parcool.utilities.VectorUtil;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3f;

public class PlayerModelRotator {
	private final MatrixStack stack;
	private final PlayerEntity player;
	private final float partial;
	private double playerHeight = 1.8;

	public float getPartialTick() {
		return partial;
	}

	private boolean basedCenter = false;
	private boolean basedTop = false;
	private boolean legGrounding = false;

	private float angleFront = 0;

	public PlayerModelRotator(MatrixStack stack, PlayerEntity player, float partial) {
		this.stack = stack;
		this.player = player;
		this.partial = partial;
		switch (player.getPose()) {
			case SWIMMING:
			case CROUCHING:
			case SLEEPING:
				playerHeight = 0.6;
		}
	}

	public PlayerModelRotator start() {
		return this;
	}

	public PlayerModelRotator startBasedCenter() {
		basedCenter = true;
		stack.translate(0, playerHeight / 2, 0);
		return this;
	}

	public PlayerModelRotator startBasedTop() {
		basedTop = true;
		stack.translate(0, playerHeight, 0);
		return this;
	}

	public PlayerModelRotator rotateFrontward(float angleDegree) {
		Vec3d lookVec = VectorUtil.fromYawDegree(player.bodyYaw).rotateY((float) Math.PI / 2);
		Vec3f vec = new Vec3f((float) lookVec.x, 0, (float) lookVec.z);
		angleFront += angleDegree;
		stack.multiply(vec.getDegreesQuaternion(angleDegree));
		return this;
	}

	public PlayerModelRotator rotateRightward(float angleDegree) {
		Vec3d lookVec = VectorUtil.fromYawDegree(player.bodyYaw);
		Vec3f vec = new Vec3f((float) lookVec.x, 0, (float) lookVec.z);
		stack.multiply(vec.getDegreesQuaternion(angleDegree));
		return this;
	}

	public void end() {
		if (basedCenter) {
			stack.translate(0, -playerHeight / 2, 0);
		}
		if (basedTop) {
			stack.translate(0, -playerHeight, 0);
		}
	}

	public void endEnabledLegGrounding() {
		end();
	}
}
