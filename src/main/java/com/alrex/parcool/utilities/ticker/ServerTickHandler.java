package com.alrex.parcool.utilities.ticker;

import com.alrex.parcool.ParCool;
import com.alrex.parcool.common.capability.impl.Parkourability;
import com.alrex.parcool.common.event.EventSendPermissions;
import com.alrex.parcool.common.info.ActionInfo;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.List;

public class ServerTickHandler implements ServerTickEvents.EndTick {
    @Override
    public void onEndTick(MinecraftServer server) {
        List<ServerPlayerEntity> players = server.getPlayerManager().getPlayerList();

        for (ServerPlayerEntity player : players) {
            if (Parkourability.get(player) != null) {
                Parkourability pa = Parkourability.get(player);
                ActionInfo info = pa.getActionInfo();

                if (!info.getServerLimitation().isReceived()) {
                    EventSendPermissions.JoinEvent(player);
                }
            }
        }
    }

}
