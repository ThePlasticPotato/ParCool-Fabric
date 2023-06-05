package com.alrex.parcool.common.capability.capabilities;

import com.alrex.parcool.ParCool;
import com.alrex.parcool.common.capability.IStamina;
import com.alrex.parcool.common.capability.impl.Animation;
import com.alrex.parcool.common.capability.impl.Parkourability;
import com.alrex.parcool.common.capability.impl.Stamina;
import com.alrex.parcool.server.command.CommandRegistry;
import dev.onyxstudios.cca.api.v3.component.Component;
import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistryV3;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import dev.onyxstudios.cca.api.v3.entity.RespawnCopyStrategy;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Identifier;

;

public class Capabilities implements EntityComponentInitializer {
    public static final Identifier STAMINA_LOCATION = new Identifier(ParCool.MOD_ID, "stamina");

    public static final ComponentKey<IStamina> STAMINA_CAPABILITY = ComponentRegistryV3.INSTANCE.getOrCreate(STAMINA_LOCATION, IStamina.class);

    public static final Identifier PARKOURABILITY_LOCATION = new Identifier(ParCool.MOD_ID, "parkourability");

    public static final ComponentKey<Parkourability> PARKOURABILITY_CAPABILITY = ComponentRegistryV3.INSTANCE.getOrCreate(PARKOURABILITY_LOCATION, Parkourability.class);

    public static final Identifier ANIMATION_LOCATION = new Identifier(ParCool.MOD_ID, "animation");

    public static final ComponentKey<Animation> ANIMATION_CAPABILITY = ComponentRegistryV3.INSTANCE.getOrCreate(ANIMATION_LOCATION, Animation.class);

    public static void register() {
        ComponentRegistry.getOrCreate(PARKOURABILITY_LOCATION, Parkourability.class);
        ComponentRegistry.getOrCreate(STAMINA_LOCATION, IStamina.class);
    }

    @Environment(EnvType.CLIENT)
    public static void registerClient() {
        ComponentRegistry.getOrCreate(ANIMATION_LOCATION, Animation.class);
    }

    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
        //todo check if player has tag before registering

        registry.registerForPlayers(PARKOURABILITY_CAPABILITY, player-> new Parkourability(), RespawnCopyStrategy.ALWAYS_COPY);
        registry.registerForPlayers(STAMINA_CAPABILITY, player-> new Stamina(), RespawnCopyStrategy.INVENTORY);
        registry.registerForPlayers(ANIMATION_CAPABILITY, player-> new Animation(), RespawnCopyStrategy.ALWAYS_COPY);
    }
}
