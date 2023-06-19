package com.alrex.parcool.utilities.ticker;

import com.alrex.parcool.common.event.EventSendPermissions;
import io.github.fabricators_of_create.porting_lib.event.common.EntityEvents;
import io.github.fabricators_of_create.porting_lib.event.common.PlayerEvents;
import net.minecraft.entity.Entity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;

public class PermissionSendEventHandler implements EntityEvents.JoinWorld {
    @Override
    public boolean onJoinWorld(Entity entity, World world, boolean loadedFromDisk) {
        if (entity instanceof ServerPlayerEntity) {
            ServerPlayerEntity player = (ServerPlayerEntity) entity;
            EventSendPermissions.JoinEvent(player);
            return true;
        }
        return true;
    }
}
