package com.alrex.parcool.common.action.impl;

import com.alrex.parcool.client.animation.impl.DiveAnimator;
import com.alrex.parcool.common.action.Action;
import com.alrex.parcool.common.action.StaminaConsumeTiming;
import com.alrex.parcool.common.capability.IStamina;
import com.alrex.parcool.common.capability.impl.Animation;
import com.alrex.parcool.common.capability.impl.Parkourability;
import com.alrex.parcool.utilities.WorldUtil;
import net.fabricmc.api.Environment;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;

import java.nio.ByteBuffer;

import static net.fabricmc.api.EnvType.CLIENT;

public class Dive extends Action {
	private boolean justJumped = false;
	private double playerYSpeedOld = 0;
	private double playerYSpeed = 0;

	public double getPlayerYSpeed(float partialTick) {
		return MathHelper.lerp(partialTick, playerYSpeedOld, playerYSpeed);
	}

	@Override
	public void onWorkingTickInLocalClient(PlayerEntity player, Parkourability parkourability, IStamina stamina) {
		playerYSpeedOld = playerYSpeed;
		playerYSpeed = player.getVelocity().y;
	}

	@Environment(CLIENT)
	@Override
	public boolean canStart(PlayerEntity player, Parkourability parkourability, IStamina stamina, ByteBuffer startInfo) {
		boolean can = (justJumped
				&& !stamina.isExhausted()
				&& !parkourability.get(Crawl.class).isDoing()
				&& !player.isInSwimmingPose()
				&& parkourability.get(FastRun.class).canActWithRunning(player)
				&& parkourability.getActionInfo().can(Dive.class)
				&& WorldUtil.existsDivableSpace(player)
		);
		startInfo.putDouble(player.getVelocity().y);
		justJumped = false;
		return can;
	}

	@Environment(CLIENT)
	@Override
	public boolean canContinue(PlayerEntity player, Parkourability parkourability, IStamina stamina) {
		return !(player.isFallFlying()
				|| player.getAbilities().flying
				|| player.isInsideWaterOrBubbleColumn()
				|| player.isInLava()
				|| player.isSwimming()
				|| player.isOnGround()
				|| stamina.isExhausted()
		);
	}

	public void onJump(PlayerEntity player, Parkourability parkourability, IStamina stamina) {
		if (!player.isMainPlayer()) return;
		justJumped = true;
	}

	@Environment(CLIENT)
	@Override
	public void onStartInLocalClient(PlayerEntity player, Parkourability parkourability, IStamina stamina, ByteBuffer startData) {
		double ySpeed = startData.getDouble();
		playerYSpeedOld = playerYSpeed = ySpeed;
		Animation animation = Animation.get(player);
		if (animation != null) {
			animation.setAnimator(new DiveAnimator(ySpeed));
		}
	}

	@Override
	public StaminaConsumeTiming getStaminaConsumeTiming() {
		return StaminaConsumeTiming.None;
	}

	@Override
	public void saveSynchronizedState(ByteBuffer buffer) {
		buffer.putDouble(playerYSpeed)
				.putDouble(playerYSpeedOld);
	}

	@Override
	public void restoreSynchronizedState(ByteBuffer buffer) {
		playerYSpeed = buffer.getDouble();
		playerYSpeedOld = buffer.getDouble();
	}

	@Environment(CLIENT)
	@Override
	public void onStartInOtherClient(PlayerEntity player, Parkourability parkourability, ByteBuffer startData) {
		double ySpeed = startData.getDouble();
		playerYSpeedOld = playerYSpeed = ySpeed;
		Animation animation = Animation.get(player);
		if (animation != null) {
			animation.setAnimator(new DiveAnimator(ySpeed));
		}
	}
}
