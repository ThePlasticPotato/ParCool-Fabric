package com.alrex.parcool.common.capability.storage;

import com.alrex.parcool.common.capability.impl.Parkourability;
import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import net.minecraft.nbt.NbtElement;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.Nullable;


;

public class ParkourabilityStorage {
	@Nullable
	public NbtElement writeTag(ComponentKey<Parkourability> capability, Parkourability instance, Direction side) {
		return instance.getActionInfo().writeTag();
	}

	public void readTag(ComponentKey<Parkourability> capability, Parkourability instance, Direction side, NbtElement nbt) {
		instance.getActionInfo().readTag(nbt);
	}
}
