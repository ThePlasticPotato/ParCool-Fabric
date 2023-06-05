package com.alrex.parcool.common.action.impl;

import com.alrex.parcool.ParCoolConfig;
import com.alrex.parcool.client.animation.impl.HorizontalWallRunAnimator;
import com.alrex.parcool.client.input.KeyBindings;
import com.alrex.parcool.common.action.Action;
import com.alrex.parcool.common.action.StaminaConsumeTiming;
import com.alrex.parcool.common.capability.IStamina;
import com.alrex.parcool.common.capability.impl.Animation;
import com.alrex.parcool.common.capability.impl.Parkourability;
import com.alrex.parcool.utilities.BufferUtil;
import com.alrex.parcool.utilities.VectorUtil;
import com.alrex.parcool.utilities.WorldUtil;
import net.fabricmc.api.Environment;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.nio.ByteBuffer;

;import static net.fabricmc.api.EnvType.CLIENT;

public class HorizontalWallRun extends Action {
	private int coolTime = 0;
	private float bodyYaw = 0;
	private static final int Max_Running_Tick = ParCoolConfig.CONFIG_CLIENT.wallRunContinuableTick.get();
	private boolean wallIsRightward = false;
	private Vec3d runningWallDirection = null;

	@Override
	public void onClientTick(PlayerEntity player, Parkourability parkourability, IStamina stamina) {
		if (coolTime > 0) coolTime--;
	}

	@Environment(CLIENT)
	@Override
	public void onWorkingTickInLocalClient(PlayerEntity player, Parkourability parkourability, IStamina stamina) {
		Vec3d wallDirection = WorldUtil.getRunnableWall(player, player.getWidth());
		if (wallDirection == null) return;
		Vec3d targetVec = wallDirection.rotateY((wallIsRightward ? 1 : -1) * (float) Math.PI / 2);
		Vec3d lookVec = player.getRotationVector();
		double differenceAngle = Math.asin(
				new Vec3d(
						lookVec.x * targetVec.x + lookVec.z * targetVec.z, 0,
						-lookVec.x * targetVec.z + lookVec.z * targetVec.x
				).normalize().z
		);
		bodyYaw = (float) VectorUtil.toYawDegree(targetVec.rotateY((float) (differenceAngle / 10)));
		if (runningWallDirection == null) return;
		Vec3d movement = player.getVelocity();
		BlockPos leanedBlock = new BlockPos(
				player.getX() + runningWallDirection.x,
				player.getBoundingBox().minY + player.getHeight() * 0.5,
				player.getZ() + runningWallDirection.z
		);
		if (!player.world.isChunkLoaded(leanedBlock)) return;
		float slipperiness = player.world.getBlockState(leanedBlock).getBlock().getSlipperiness();
		if (slipperiness <= 0.8) {
			player.setVelocity(
					movement.x,
					movement.y * (slipperiness - 0.1) * ((double) getDoingTick()) / Max_Running_Tick,
					movement.z
			);
		}
	}

	@Environment(CLIENT)
	@Override
	public boolean canStart(PlayerEntity player, Parkourability parkourability, IStamina stamina, ByteBuffer startInfo) {
		Vec3d wallDirection = WorldUtil.getRunnableWall(player, player.getWidth());
		if (wallDirection == null) return false;
		Vec3d wallVec = wallDirection.normalize();
		Vec3d direction = VectorUtil.fromYawDegree(player.bodyYaw);
		direction = new Vec3d(direction.x, 0, direction.z).normalize();
		//doing "wallDirection/direction" as complex number(x + z i) to calculate difference of player's direction to steps
		Vec3d dividedVec =
				new Vec3d(
						wallVec.x * direction.x + wallVec.z * direction.z, 0,
						-wallVec.x * direction.z + wallVec.z * direction.x
				).normalize();
		if (Math.abs(dividedVec.z) < 0.9) {
			return false;
		}
		BufferUtil.wrap(startInfo).putBoolean(dividedVec.z > 0/*if true, wall is in right side*/);
		startInfo.putDouble(wallDirection.x)
				.putDouble(wallDirection.z);

		return (parkourability.getActionInfo().can(HorizontalWallRun.class)
				&& !parkourability.get(WallJump.class).justJumped()
				&& !parkourability.get(Crawl.class).isDoing()
				&& KeyBindings.getKeyHorizontalWallRun().isPressed()
				&& Math.abs(player.getVelocity().y) < 0.3
				&& coolTime == 0
				&& !player.isOnGround()
				&& parkourability.getAdditionalProperties().getNotLandingTick() > 5
				&& (parkourability.get(FastRun.class).canActWithRunning(player)
				|| parkourability.get(FastRun.class).getNotDashTick(parkourability.getAdditionalProperties()) < 10
		)
				&& !stamina.isExhausted()
		);
	}

	@Environment(CLIENT)
	@Override
	public boolean canContinue(PlayerEntity player, Parkourability parkourability, IStamina stamina) {
		Vec3d wallDirection = WorldUtil.getRunnableWall(player, player.getWidth());
		if (wallDirection == null) return false;
		return (getDoingTick() < Max_Running_Tick
				&& !stamina.isExhausted()
				&& parkourability.getActionInfo().can(HorizontalWallRun.class)
				&& !parkourability.get(WallJump.class).justJumped()
				&& !parkourability.get(Crawl.class).isDoing()
				&& KeyBindings.getKeyHorizontalWallRun().isPressed()
				&& !player.isOnGround()
		);
	}

	@Override
	public void onStop(PlayerEntity player) {
		coolTime = 10;
	}

	@Override
	public void onStartInLocalClient(PlayerEntity player, Parkourability parkourability, IStamina stamina, ByteBuffer startData) {
		wallIsRightward = BufferUtil.getBoolean(startData);
		runningWallDirection = new Vec3d(startData.getDouble(), 0, startData.getDouble());
		Animation animation = Animation.get(player);
		if (animation != null) {
			animation.setAnimator(new HorizontalWallRunAnimator(wallIsRightward));
		}
	}

	@Override
	public void onStartInOtherClient(PlayerEntity player, Parkourability parkourability, ByteBuffer startData) {
		wallIsRightward = BufferUtil.getBoolean(startData);
		runningWallDirection = new Vec3d(startData.getDouble(), 0, startData.getDouble());
		Animation animation = Animation.get(player);
		if (animation != null) {
			animation.setAnimator(new HorizontalWallRunAnimator(wallIsRightward));
		}
	}

	@Environment(CLIENT)
	@Override
	public void onRenderTick(PlayerEntity player, Parkourability parkourability) {
		if (isDoing()) {
			player.setYaw(bodyYaw);
		}
	}

	@Override
	public void saveSynchronizedState(ByteBuffer buffer) {
		buffer.putFloat(bodyYaw);
	}

	@Override
	public void restoreSynchronizedState(ByteBuffer buffer) {
		bodyYaw = buffer.getFloat();
	}

	@Override
	public StaminaConsumeTiming getStaminaConsumeTiming() {
		return StaminaConsumeTiming.OnWorking;
	}
}
