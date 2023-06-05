package com.alrex.parcool.common.event;

import com.alrex.parcool.common.capability.impl.Parkourability;
import net.minecraft.entity.player.PlayerEntity;

;

public class EventPlayerClone {

	public static void onClone(PlayerEntity from, PlayerEntity to, boolean isOriginalDead) {
		if (isOriginalDead) {
			Parkourability pFrom = Parkourability.get(from);
			Parkourability pTo = Parkourability.get(to);
			if (pFrom != null && pTo != null) {
				pTo.CopyFrom(pFrom);
			}
		}
	}
}
