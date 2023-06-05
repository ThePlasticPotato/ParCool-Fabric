package com.alrex.parcool.common.event;


import net.minecraft.server.network.ServerPlayerEntity;

public class EventSendPermissions {

	public static void JoinEvent(ServerPlayerEntity player) {
		LimitationByServerMessage.send(player);
		LimitationByServerMessage.sendIndividualLimitation(player);
	}
}
