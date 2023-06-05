package com.alrex.parcool.common.event;

import com.alrex.parcool.common.action.impl.Dive;
import com.alrex.parcool.common.capability.IStamina;
import com.alrex.parcool.common.capability.impl.Parkourability;
import net.minecraft.entity.player.PlayerEntity;

;

public class EventPlayerJump {

	public static void onJump(PlayerEntity player) {
		if (!player.isMainPlayer()) return;
		Parkourability parkourability = Parkourability.get(player);
		if (parkourability == null) return;
		IStamina stamina = IStamina.get(player);
		if (stamina == null) return;
		parkourability.get(Dive.class).onJump(player, parkourability, stamina);
	}
}
