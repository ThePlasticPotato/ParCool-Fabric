package com.alrex.parcool.common.action.impl;

import com.alrex.parcool.client.animation.impl.BackwardWallJumpAnimator;
import com.alrex.parcool.client.animation.impl.WallJumpAnimator;
import com.alrex.parcool.client.input.KeyRecorder;
import com.alrex.parcool.common.action.Action;
import com.alrex.parcool.common.action.StaminaConsumeTiming;
import com.alrex.parcool.common.capability.IStamina;
import com.alrex.parcool.common.capability.impl.Animation;
import com.alrex.parcool.common.capability.impl.Parkourability;
import com.alrex.parcool.utilities.WorldUtil;
import net.fabricmc.api.Environment;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;

import java.nio.ByteBuffer;

import static net.fabricmc.api.EnvType.CLIENT;

public class WallJump extends Action {

	private boolean jump = false;

	public boolean justJumped() {
		return jump;
	}

	@Override
	public void onTick(PlayerEntity player, Parkourability parkourability, IStamina stamina) {
		jump = false;
	}

	@Override
	public StaminaConsumeTiming getStaminaConsumeTiming() {
		return StaminaConsumeTiming.OnStart;
	}


	@Environment(CLIENT)
	@Nullable
	private Vec3d getJumpDirection(PlayerEntity player, Vec3d wall) {
		if (wall == null) return null;
		wall = wall.normalize();

		Vec3d lookVec = player.getRotationVector();
		Vec3d vec = new Vec3d(lookVec.x, 0, lookVec.z).normalize();

		if (wall.dotProduct(vec) > 0.5) {//To Wall
			return null;
		} else {/*back on Wall*/}

		return vec.normalize().add(wall.multiply(-0.7));
	}

	@Override
	public boolean canStart(PlayerEntity player, Parkourability parkourability, IStamina stamina, ByteBuffer startInfo) {
		Vec3d wallDirection = WorldUtil.getWall(player);
		Vec3d jumpDirection = getJumpDirection(player, wallDirection);
		if (jumpDirection == null) return false;
		ClingToCliff cling = parkourability.get(ClingToCliff.class);

		boolean value = (!stamina.isExhausted()
				&& parkourability.getActionInfo().can(WallJump.class)
				&& !player.isOnGround()
				&& !player.isInsideWaterOrBubbleColumn()
				&& !player.isFallFlying()
				&& !player.getAbilities().flying
				&& parkourability.getAdditionalProperties().getNotCreativeFlyingTick() > 10
				&& ((!cling.isDoing() && cling.getNotDoingTick() > 3)
				|| (cling.isDoing() && cling.getFacingDirection() != ClingToCliff.FacingDirection.ToWall))
				&& KeyRecorder.keyWallJump.isPressed()
				&& !parkourability.get(Crawl.class).isDoing()
				&& parkourability.getAdditionalProperties().getNotLandingTick() > 5
				&& WorldUtil.getWall(player) != null
		);
		if (!value) return false;

		//doing "wallDirection/jumpDirection" as complex number(x + z i) to calculate difference of player's direction to wall
		Vec3d dividedVec =
				new Vec3d(
						wallDirection.x * jumpDirection.x + wallDirection.z * jumpDirection.z, 0,
						-wallDirection.x * jumpDirection.z + wallDirection.z * jumpDirection.x
				).normalize();
		Vec3d lookVec = player.getRotationVector().multiply(1, 0, 1).normalize();
		Vec3d lookDividedVec =
				new Vec3d(
						lookVec.x * wallDirection.x + lookVec.z * wallDirection.z, 0,
						-lookVec.x * wallDirection.z + lookVec.z * wallDirection.x
				).normalize();

		WallJumpAnimationType type;
		if (lookDividedVec.x > 0.5) {
			type = WallJumpAnimationType.Back;
		} else if (dividedVec.z > 0) {
			type = WallJumpAnimationType.SwingRightArm;
		} else {
			type = WallJumpAnimationType.SwingLeftArm;
		}
		startInfo
				.putDouble(jumpDirection.x)
				.putDouble(jumpDirection.z)
				.putDouble(wallDirection.x)
				.putDouble(wallDirection.z)
				.put(type.getCode());
		return true;
	}

	@Override
	public boolean canContinue(PlayerEntity player, Parkourability parkourability, IStamina stamina) {
		return false;
	}

	@Override
	public void onStart(PlayerEntity player, Parkourability parkourability) {
		jump = true;
		player.fallDistance = 0;
	}

	@Override
	public void onStartInLocalClient(PlayerEntity player, Parkourability parkourability, IStamina stamina, ByteBuffer startData) {
		double speedScale = player.getWidth() / 0.6;
		Vec3d jumpDirection = new Vec3d(startData.getDouble(), 0, startData.getDouble()).multiply(speedScale);
		Vec3d direction = new Vec3d(jumpDirection.x, 1.512, jumpDirection.z).multiply(0.3);
		Vec3d wallDirection = new Vec3d(startData.getDouble(), 0, startData.getDouble());
		Vec3d motion = player.getVelocity();

		BlockPos leanedBlock = new BlockPos(
				player.getX() + wallDirection.x,
				player.getBoundingBox().minY + player.getHeight() * 0.25,
				player.getZ() + wallDirection.z
		);
		float slipperiness = player.world.isChunkLoaded(leanedBlock) ?
				player.world.getBlockState(leanedBlock).getBlock().getSlipperiness()
				: 0.6f;

		double ySpeed;
		if (slipperiness > 0.9) {// icy blocks
			ySpeed = motion.y;
		} else {
			ySpeed = motion.y > direction.y ? motion.y + direction.y : direction.y;
		}
		player.setVelocity(
				motion.x + direction.x,
				ySpeed,
				motion.z + direction.z
		);

		WallJumpAnimationType type = WallJumpAnimationType.fromCode(startData.get());
		Animation animation = Animation.get(player);
		if (animation != null) {
			switch (type) {
				case Back:
					animation.addAnimator(new BackwardWallJumpAnimator());
					break;
				case SwingLeftArm:
					animation.addAnimator(new WallJumpAnimator(false));
					break;
				case SwingRightArm:
					animation.addAnimator(new WallJumpAnimator(true));
			}
		}
	}

	@Override
	public void onStartInOtherClient(PlayerEntity player, Parkourability parkourability, ByteBuffer startData) {
		startData.position(32);
		WallJumpAnimationType type = WallJumpAnimationType.fromCode(startData.get());
		Animation animation = Animation.get(player);
		if (animation != null) {
			switch (type) {
				case Back:
					animation.addAnimator(new BackwardWallJumpAnimator());
					break;
				case SwingLeftArm:
					animation.addAnimator(new WallJumpAnimator(false));
					break;
				case SwingRightArm:
					animation.addAnimator(new WallJumpAnimator(true));
			}
		}
	}

	private enum WallJumpAnimationType {
		Back((byte) 0), SwingRightArm((byte) 1), SwingLeftArm((byte) 2);
		private byte code;

		WallJumpAnimationType(byte code) {
			this.code = code;
		}

		public byte getCode() {
			return code;
		}

		public static WallJumpAnimationType fromCode(byte code) {
			switch (code) {
				case 1:
					return SwingRightArm;
				case 2:
					return SwingLeftArm;
				default:
					return Back;
			}
		}
	}
}
