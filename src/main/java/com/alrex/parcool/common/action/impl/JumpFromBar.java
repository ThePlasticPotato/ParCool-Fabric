package com.alrex.parcool.common.action.impl;

import com.alrex.parcool.client.animation.impl.JumpFromBarAnimator;
import com.alrex.parcool.client.input.KeyRecorder;
import com.alrex.parcool.common.action.Action;
import com.alrex.parcool.common.action.StaminaConsumeTiming;
import com.alrex.parcool.common.capability.IStamina;
import com.alrex.parcool.common.capability.impl.Animation;
import com.alrex.parcool.common.capability.impl.Parkourability;
import com.alrex.parcool.utilities.EntityUtil;
import net.minecraft.entity.player.PlayerEntity;


import java.nio.ByteBuffer;

;

public class JumpFromBar extends Action {
	@Override
	public boolean canStart(PlayerEntity player, Parkourability parkourability, IStamina stamina, ByteBuffer startInfo) {
		HangDown hangDown = parkourability.get(HangDown.class);
		return hangDown.isDoing()
				&& hangDown.getDoingTick() > 2
				&& parkourability.getActionInfo().can(JumpFromBar.class)
				&& KeyRecorder.keyJumpState.isPressed();
	}

	@Override
	public boolean canContinue(PlayerEntity player, Parkourability parkourability, IStamina stamina) {
		return getDoingTick() < 2;
	}

	@Override
	public void onStartInLocalClient(PlayerEntity player, Parkourability parkourability, IStamina stamina, ByteBuffer startData) {
		EntityUtil.addVelocity(player, player.getRotationVector().multiply(1, 0, 1).normalize().multiply(player.getWidth() * 0.75));
		Animation animation = Animation.get(player);
		if (animation != null) animation.addAnimator(new JumpFromBarAnimator());
	}

	@Override
	public void onStartInOtherClient(PlayerEntity player, Parkourability parkourability, ByteBuffer startData) {
		Animation animation = Animation.get(player);
		if (animation != null) animation.addAnimator(new JumpFromBarAnimator());
	}

	@Override
	public StaminaConsumeTiming getStaminaConsumeTiming() {
		return StaminaConsumeTiming.OnStart;
	}
}
