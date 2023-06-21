package com.alrex.parcool.common.action.impl;

import com.alrex.parcool.ParCoolConfig;
import com.alrex.parcool.client.animation.impl.RollAnimator;
import com.alrex.parcool.client.input.KeyBindings;
import com.alrex.parcool.common.action.Action;
import com.alrex.parcool.common.action.StaminaConsumeTiming;
import com.alrex.parcool.common.capability.IStamina;
import com.alrex.parcool.common.capability.impl.Animation;
import com.alrex.parcool.common.capability.impl.Parkourability;
import com.alrex.parcool.utilities.BufferUtil;
import com.alrex.parcool.utilities.VectorUtil;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;

import java.nio.ByteBuffer;

;

public class Roll extends Action {
	private int creativeCoolTime = 0;
	private boolean startRequired = false;

	public enum Direction {Front, Back}

	@Environment(EnvType.CLIENT)
	@Override
	public void onClientTick(PlayerEntity player, Parkourability parkourability, IStamina stamina) {
		if (player.isMainPlayer()) {
			if (KeyBindings.getKeyBreakfall().isPressed()
					&& KeyBindings.getKeyForward().isPressed()
					&& !parkourability.get(Dodge.class).isDoing()
					&& ParCoolConfig.CONFIG_CLIENT.enableRollWhenCreative.get()
					&& player.isCreative()
					&& parkourability.getAdditionalProperties().getLandingTick() <= 1
					&& player.isOnGround()
					&& !isDoing()
					&& creativeCoolTime == 0
			) {
				startRequired = true;
				creativeCoolTime = 20;
			}
			if (creativeCoolTime > 0) creativeCoolTime--;
		}
	}

	@Override
	public StaminaConsumeTiming getStaminaConsumeTiming() {
		return StaminaConsumeTiming.OnStart;
	}

	@Override
	public boolean canStart(PlayerEntity player, Parkourability parkourability, IStamina stamina, ByteBuffer startInfo) {
		BufferUtil.wrap(startInfo).putBoolean(KeyBindings.getKeyBack().isPressed());
		return startRequired;
	}

	@Override
	public boolean canContinue(PlayerEntity player, Parkourability parkourability, IStamina stamina) {
		return getDoingTick() < getRollMaxTick();
	}

	@Override
	public void onStartInOtherClient(PlayerEntity player, Parkourability parkourability, ByteBuffer startData) {
		startRequired = false;
		Direction direction = BufferUtil.getBoolean(startData) ? Direction.Back : Direction.Front;
		Animation animation = Animation.get(player);
		if (animation != null) animation.addAnimator(new RollAnimator(direction));
	}

	@Override
	public void onStartInLocalClient(PlayerEntity player, Parkourability parkourability, IStamina stamina, ByteBuffer startData) {
		startRequired = false;
		Direction direction = BufferUtil.getBoolean(startData) ? Direction.Back : Direction.Front;
		double modifier = Math.sqrt(player.getWidth());
		Vec3d vec = VectorUtil.fromYawDegree(player.bodyYaw).multiply(modifier);
		if (direction == Direction.Back) {
			vec = vec.negate();
		}
		player.setVelocity(vec.x, 0, vec.z);
		Animation animation = Animation.get(player);
		if (animation != null) animation.addAnimator(new RollAnimator(direction));
	}

	public void startRoll(PlayerEntity player) {
		startRequired = true;
	}

	public int getRollMaxTick() {
		return 9;
	}
}
