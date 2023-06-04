package com.alrex.parcool.client.animation;

import com.alrex.parcool.utilities.MathUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.CrossbowPosing;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;

/**
 * Using Radians
 */
public class PlayerModelTransformer {
	private final PlayerEntity player;
	private final PlayerEntityModel<PlayerEntity> model;
	private final boolean slim;
	private final float partial;
	private final float ageInTicks;
	private final float limbSwing;
	private final float limbSwingAmount;
	private final float netHeadYaw;
	private final float headPitch;

	public float getPartialTick() {
		return partial;
	}

	public float getHeadPitch() {
		return headPitch;
	}

	public float getNetHeadYaw() {
		return netHeadYaw;
	}

	public float getLimbSwing() {
		return limbSwing;
	}

	public float getLimbSwingAmount() {
		return limbSwingAmount;
	}

	public PlayerEntityModel<PlayerEntity> getRawModel() {
		return model;
	}

	public PlayerModelTransformer(
			PlayerEntity player,
			PlayerEntityModel<PlayerEntity> model,
			boolean slim,
			float ageInTicks,
			float limbSwing,
			float limbSwingAmount,
			float netHeadYaw,
			float headPitch
	) {
		this.player = player;
		this.model = model;
		this.slim = slim;
		this.partial = MinecraftClient.getInstance().getTickDelta();
		this.ageInTicks = ageInTicks;
		this.limbSwing = limbSwing;
		this.limbSwingAmount = limbSwingAmount;
		this.netHeadYaw = netHeadYaw;
		this.headPitch = headPitch;
	}

	/**
	 * @param angleX swing arm frontward or backward
	 * @param angleY rotate arm around
	 * @param angleZ swing arm upward or downward
	 */
	public PlayerModelTransformer rotateRightArm(float angleX, float angleY, float angleZ) {
		ModelPart rightArm = model.rightArm;
		if (rightArm.visible) {
			setRotations(rightArm, angleX, angleY, angleZ);
		}
		return this;
	}

	public PlayerModelTransformer rotateRightArm(float angleX, float angleY, float angleZ, float factor) {
		ModelPart rightArm = model.rightArm;
		if (rightArm.visible) {
			setRotations(rightArm,
					MathUtil.lerp(rightArm.pitch, angleX, factor),
					MathUtil.lerp(rightArm.yaw, angleY, factor),
					MathUtil.lerp(rightArm.roll, angleZ, factor)
			);
		}
		return this;
	}

	/**
	 * @param angleX swing arm frontward or backward
	 * @param angleY rotate arm around
	 * @param angleZ swing arm upward or downward
	 */
	public PlayerModelTransformer rotateLeftArm(float angleX, float angleY, float angleZ) {
		ModelPart leftArm = model.leftArm;
		if (leftArm.visible) {
			setRotations(leftArm, angleX, angleY, angleZ);
		}
		return this;
	}

	public PlayerModelTransformer rotateLeftArm(float angleX, float angleY, float angleZ, float factor) {
		ModelPart leftArm = model.leftArm;
		if (leftArm.visible) {
			setRotations(leftArm,
					MathUtil.lerp(leftArm.pitch, angleX, factor),
					MathUtil.lerp(leftArm.yaw, angleY, factor),
					MathUtil.lerp(leftArm.roll, angleZ, factor)
			);
		}
		return this;
	}

	/**
	 * @param angleX swing leg frontward or backward
	 * @param angleY rotate leg around
	 * @param angleZ swing leg upward or downward
	 */
	public PlayerModelTransformer rotateRightLeg(float angleX, float angleY, float angleZ) {
		ModelPart rightLeg = model.rightLeg;
		if (rightLeg.visible) {
			setRotations(rightLeg, angleX, angleY, angleZ);
		}
		return this;
	}

	public PlayerModelTransformer rotateRightLeg(float angleX, float angleY, float angleZ, float factor) {
		ModelPart rightLeg = model.rightLeg;
		if (rightLeg.visible) {
			setRotations(rightLeg,
					MathUtil.lerp(rightLeg.pitch, angleX, factor),
					MathUtil.lerp(rightLeg.yaw, angleY, factor),
					MathUtil.lerp(rightLeg.roll, angleZ, factor)
			);
		}
		return this;
	}

	/**
	 * @param angleX swing leg frontward or backward
	 * @param angleY rotate leg around
	 * @param angleZ swing leg upward or downward
	 */
	public PlayerModelTransformer rotateLeftLeg(float angleX, float angleY, float angleZ) {
		ModelPart leftLeg = model.leftLeg;
		if (leftLeg.visible) {
			setRotations(leftLeg, angleX, angleY, angleZ);
		}
		return this;
	}

	public PlayerModelTransformer rotateLeftLeg(float angleX, float angleY, float angleZ, float factor) {
		ModelPart leftLeg = model.leftLeg;
		if (leftLeg.visible) {
			setRotations(leftLeg,
					MathUtil.lerp(leftLeg.pitch, angleX, factor),
					MathUtil.lerp(leftLeg.yaw, angleY, factor),
					MathUtil.lerp(leftLeg.roll, angleZ, factor)
			);
		}
		return this;
	}

	public PlayerModelTransformer addRotateRightArm(float angleX, float angleY, float angleZ) {
		ModelPart arm = model.rightArm;
		if (arm.visible) {
			setRotations(arm, arm.pitch + angleX, arm.yaw + angleY, arm.roll + angleZ);
		}
		return this;
	}

	public PlayerModelTransformer addRotateLeftArm(float angleX, float angleY, float angleZ) {
		ModelPart arm = model.leftArm;
		if (arm.visible) {
			setRotations(arm, arm.pitch + angleX, arm.yaw + angleY, arm.roll + angleZ);
		}
		return this;
	}

	public PlayerModelTransformer addRotateRightLeg(float angleX, float angleY, float angleZ) {
		ModelPart leg = model.rightLeg;
		if (leg.visible) {
			setRotations(leg, leg.pitch + angleX, leg.yaw + angleY, leg.roll + angleZ);
		}
		return this;
	}

	public PlayerModelTransformer addRotateLeftLeg(float angleX, float angleY, float angleZ) {
		ModelPart leg = model.leftLeg;
		if (leg.visible) {
			setRotations(leg, leg.pitch + angleX, leg.yaw + angleY, leg.roll + angleZ);
		}
		return this;
	}

	public PlayerModelTransformer makeArmsNatural() {
		CrossbowPosing.swingArms(model.rightArm, model.leftArm, ageInTicks);
		return this;
	}

	public PlayerModelTransformer makeArmsMovingDynamically(float factor) {
		model.rightArm.roll += MathHelper.cos(ageInTicks * 0.56F) * 0.8F * factor + 0.05F;
		model.leftArm.roll -= MathHelper.cos(ageInTicks * 0.56F) * 0.8F * factor + 0.05F;
		model.rightArm.pitch += MathHelper.sin(ageInTicks * 0.56F) * 0.8F * factor;
		model.leftArm.pitch -= MathHelper.sin(ageInTicks * 0.56F) * 0.8F * factor;
		return this;
	}

	public PlayerModelTransformer makeLegsLittleMoving() {
		CrossbowPosing.swingArms(model.rightLeg, model.leftLeg, ageInTicks);
		return this;
	}

	public PlayerModelTransformer makeLegsShakingDynamically(float factor) {
		model.rightLeg.roll += MathHelper.cos(ageInTicks * 0.56F) * 0.8F * factor + 0.05F;
		model.leftLeg.roll += MathHelper.cos(ageInTicks * 0.56F) * 0.8F * factor + 0.05F;
		model.rightLeg.pitch += MathHelper.sin(ageInTicks * 0.56F) * 0.2F * factor;
		model.leftLeg.pitch -= MathHelper.sin(ageInTicks * 0.56F) * 0.2F * factor;
		return this;
	}

	public PlayerModelTransformer rotateAdditionallyHeadPitch(float pitchDegree) {
		model.head.pitch = (float) Math.toRadians(pitchDegree + headPitch);
		return this;
	}

	public PlayerModelTransformer rotateHeadPitch(float pitchDegree) {
		model.head.pitch = (float) Math.toRadians(pitchDegree);
		return this;
	}

	public void end() {
	}

	public PlayerModelTransformer rotateAdditionallyHeadYaw(float yawDegree) {
		model.head.yaw = (float) Math.toRadians(yawDegree + netHeadYaw);
		return this;
	}

	public void copyFromBodyToWear() {
		model.rightSleeve.copyTransform(model.rightArm);
		model.leftSleeve.copyTransform(model.leftArm);
		model.rightPants.copyTransform(model.rightLeg);
		model.leftPants.copyTransform(model.leftLeg);
		model.jacket.copyTransform(model.body);
		model.hat.copyTransform(model.head);
	}

	private void setRotations(ModelPart renderer, float angleX, float angleY, float angleZ) {
		renderer.pitch = angleX;
		renderer.yaw = angleY;
		renderer.roll = angleZ;
	}

	public void reset() {
		resetModel(model.head);
		resetModel(model.hat);
		resetModel(model.jacket);
		resetModel(model.body);
		{
			resetModel(model.rightArm);
			model.rightArm.pivotX = 5.2F;
			model.rightArm.pivotY = this.slim ? 2.5F : 2.0F;
			model.rightArm.pivotZ = 0.0F;
			model.rightSleeve.copyTransform(model.rightArm);
		}
		{
			resetModel(model.leftArm);
			model.leftArm.pivotX = 2.5F;
			model.leftArm.pivotY = this.slim ? 2.5F : 2.0F;
			model.leftArm.pivotZ = 0.0F;

			model.leftSleeve.copyTransform(model.leftArm);
		}
		{
			resetModel(model.leftLeg);
			model.leftLeg.pivotX = 1.9F;
			model.leftLeg.pivotY = 12.0F;
			model.leftLeg.pivotZ = 0.0F;

			model.leftPants.copyTransform(model.leftLeg);
		}
		{
			resetModel(model.rightLeg);
			model.rightLeg.pivotX = -1.9F;
			model.rightLeg.pivotY = 12.0F;
			model.rightLeg.pivotZ = 0.0F;

			model.rightPants.copyTransform(model.rightLeg);
		}
	}

	public void resetModel(ModelPart model) {
		model.pitch = 0;
		model.yaw = 0;
		model.roll = 0;
		model.pivotX = 0;
		model.pivotY = 0;
		model.pivotZ = 0;
	}
}
