package com.alrex.parcool.common.action.impl;

import com.alrex.parcool.client.input.KeyBindings;
import com.alrex.parcool.common.action.Action;
import com.alrex.parcool.common.action.StaminaConsumeTiming;
import com.alrex.parcool.common.capability.IStamina;
import com.alrex.parcool.common.capability.impl.Parkourability;
import net.minecraft.entity.player.PlayerEntity;

import java.nio.ByteBuffer;

;

public class BreakfallReady extends Action {
	public void startBreakfall(PlayerEntity player, Parkourability parkourability, IStamina stamina) {
		setDoing(false);
		if ((KeyBindings.getKeyForward().isPressed() || KeyBindings.getKeyBack().isPressed())
				&& (parkourability.getActionInfo().can(Roll.class) || !parkourability.getActionInfo().can(Tap.class))
		) {
			parkourability.get(Roll.class).startRoll(player);
		} else {
			parkourability.get(Tap.class).startTap(player);
		}
	}

	@Override
	public boolean canStart(PlayerEntity player, Parkourability parkourability, IStamina stamina, ByteBuffer startInfo) {
		return canContinue(player, parkourability, stamina);
	}

	@Override
	public boolean canContinue(PlayerEntity player, Parkourability parkourability, IStamina stamina) {
		return (parkourability.getActionInfo().can(BreakfallReady.class)
				&& KeyBindings.getKeyBreakfall().isPressed()
				&& !stamina.isExhausted()
				&& !parkourability.get(Crawl.class).isDoing()
				&& !player.isInsideWaterOrBubbleColumn()
				&& (!player.isOnGround() || parkourability.getAdditionalProperties().getLandingTick() < 3)
		);
	}

	@Override
	public StaminaConsumeTiming getStaminaConsumeTiming() {
		return StaminaConsumeTiming.None;
	}
}
