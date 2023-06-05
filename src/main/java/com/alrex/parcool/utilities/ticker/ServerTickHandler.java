package com.alrex.parcool.utilities.ticker;

import com.alrex.parcool.ParCool;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;

public class ServerTickHandler implements ServerTickEvents.EndTick {
    @Override
    public void onEndTick(MinecraftServer server) {
    }
}
