package com.alrex.parcool.utilities.ticker;

import com.alrex.parcool.common.event.EventPlayerFall;
import io.github.fabricators_of_create.porting_lib.event.common.LivingEntityEvents;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;

public class PlayerFallEventHandler implements LivingEntityEvents.Fall {
    @Override
    public void onFall(FallEvent event) {
        EventPlayerFall.onDamage(event);
    }
}
