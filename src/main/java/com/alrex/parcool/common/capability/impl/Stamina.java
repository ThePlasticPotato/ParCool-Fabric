package com.alrex.parcool.common.capability.impl;

import com.alrex.parcool.ParCoolConfig;
import com.alrex.parcool.common.capability.IStamina;
import com.alrex.parcool.common.potion.Effects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import org.jetbrains.annotations.Nullable;

public class Stamina implements IStamina {
	public Stamina(@Nullable PlayerEntity player) {
		this.player = player;
		if (player != null && player.isMainPlayer()) {
			maxStamina = ParCoolConfig.CONFIG_CLIENT.staminaMax.get();
			set(maxStamina);
		}
	}

	public Stamina() {
		this.player = null;
	}

	@Nullable
	private final PlayerEntity player;

	private int stamina = 0;
	private int staminaOld = 0;
	private int maxStamina = 1;
	private boolean exhausted = false;

	@Override
	public int getMaxStamina() {
		return maxStamina;
	}

	@Override
	public int getActualMaxStamina() {
		if (player == null) return maxStamina;
		Parkourability parkourability = Parkourability.get(player);
		if (parkourability == null) return maxStamina;
		return Math.min(maxStamina, parkourability.getActionInfo().getMaxStaminaLimitation());
	}

	@Override
	public void setMaxStamina(int value) {
		maxStamina = stamina;
	}

	@Override
	public int get() {
		return stamina;
	}

	@Override
	public int getOldValue() {
		return staminaOld;
	}

	@Override
	public void consume(int value) {
		if (player == null) return;
		Parkourability parkourability = Parkourability.get(player);
		if (parkourability == null) return;
		if (exhausted
				|| (ParCoolConfig.CONFIG_CLIENT.infiniteStamina.get() && parkourability.getActionInfo().isInfiniteStaminaPermitted())
				|| player.hasStatusEffect(Effects.INEXHAUSTIBLE)
		) return;
		if (ParCoolConfig.CONFIG_CLIENT.useHungerBarInsteadOfStamina.get()) {
			player.addExhaustion(value / 1000f);
			return;
		}
		recoverCoolTime = 30;
		set(stamina - value);
		if (stamina == 0) {
			exhausted = true;
		}
	}

	@Override
	public void recover(int value) {
		set(stamina + value);
		if (stamina == getActualMaxStamina()) {
			exhausted = false;
		}
	}

	@Override
	public boolean isExhausted() {
		return exhausted;
	}

	@Override
	public void setExhaustion(boolean value) {
		exhausted = value;
	}

	private int recoverCoolTime = 0;

	@Override
	public void tick() {
		staminaOld = stamina;
		if (recoverCoolTime > 0) recoverCoolTime--;
		if (recoverCoolTime <= 0) {
			if (player == null) return;
			Parkourability parkourability = Parkourability.get(player);
			if (parkourability == null) return;
			recover(Math.min(
					ParCoolConfig.CONFIG_CLIENT.staminaRecovery.get(),
					parkourability.getActionInfo().getMaxStaminaRecoveryLimitation()
			));
		}
	}

	@Override
	public void set(int value) {
		stamina = Math.min(value, getActualMaxStamina());
		if (stamina <= 0) {
			stamina = 0;
		}
	}

	@Override
	public void readFromNbt(NbtCompound tag) {

	}

	@Override
	public void writeToNbt(NbtCompound tag) {

	}
}
