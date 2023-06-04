package com.alrex.parcool.common.capability.storage;

import com.alrex.parcool.common.capability.IStamina;
import org.jetbrains.annotations.Nullable;

public class StaminaStorage {
	@Nullable
	public Tag writeTag(Capability<IStamina> capability, IStamina instance, Direction side) {
		CompoundTag nbt = new CompoundTag();
		nbt.putBoolean("exhausted", instance.isExhausted());
		nbt.putInt("value", instance.get());
		return nbt;
	}

	public void readTag(Capability<IStamina> capability, IStamina instance, Direction side, Tag nbt) {
		if (nbt instanceof CompoundTag compound) {
			instance.set(compound.getInt("value"));
			instance.setExhaustion(compound.getBoolean("exhausted"));
		} else {
			throw new IllegalArgumentException("Tag for StaminaStorage, is not CompoundTag");
		}
	}
}
