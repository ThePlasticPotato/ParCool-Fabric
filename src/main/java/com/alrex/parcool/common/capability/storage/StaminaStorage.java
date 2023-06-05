package com.alrex.parcool.common.capability.storage;

import com.alrex.parcool.common.action.impl.Roll;
import com.alrex.parcool.common.capability.IStamina;
import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.Nullable;

public class StaminaStorage {
	@Nullable
	public NbtElement writeTag(ComponentKey<IStamina> capability, IStamina instance, Direction side) {
		NbtCompound nbt = new NbtCompound();
		nbt.putBoolean("exhausted", instance.isExhausted());
		nbt.putInt("value", instance.get());
		return nbt;
	}

	public void readTag(ComponentKey<IStamina> capability, IStamina instance, Direction side, NbtElement nbt) {
		if (nbt instanceof NbtCompound compound) {
			instance.set(compound.getInt("value"));
			instance.setExhaustion(compound.getBoolean("exhausted"));
		} else {
			throw new IllegalArgumentException("Tag for StaminaStorage, is not CompoundTag");
		}
	}
}
