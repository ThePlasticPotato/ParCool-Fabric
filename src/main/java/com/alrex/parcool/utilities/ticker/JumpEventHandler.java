package com.alrex.parcool.utilities.ticker;

import com.alrex.parcool.common.event.EventPlayerJump;
import io.github.fabricators_of_create.porting_lib.event.common.LivingEntityEvents;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;

public class JumpEventHandler implements LivingEntityEvents.Jump {
    @Override
    public void onLivingEntityJump(LivingEntity entity) {
        if (entity instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) entity;
            EventPlayerJump.onJump(player);
        }
    }
}
