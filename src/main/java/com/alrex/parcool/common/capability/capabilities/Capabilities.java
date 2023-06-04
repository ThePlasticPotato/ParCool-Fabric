package com.alrex.parcool.common.capability.capabilities;

import com.alrex.parcool.ParCool;
import com.alrex.parcool.common.capability.IStamina;
import com.alrex.parcool.common.capability.impl.Animation;
import com.alrex.parcool.common.capability.impl.Parkourability;
import com.alrex.parcool.server.command.CommandRegistry;
import dev.onyxstudios.cca.api.v3.component.Component;
import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Identifier;

;

public class Capabilities {
    public static final Identifier STAMINA_LOCATION = new Identifier(ParCool.MOD_ID, "stamina");

    public static final ComponentKey<IStamina> STAMINA_CAPABILITY = (ComponentKey<IStamina>) ComponentRegistry.get(STAMINA_LOCATION);

    public static final Identifier PARKOURABILITY_LOCATION = new Identifier(ParCool.MOD_ID, "parkourability");

    public static final ComponentKey<Parkourability> PARKOURABILITY_CAPABILITY = (ComponentKey<Parkourability>) ComponentRegistry.get(PARKOURABILITY_LOCATION);

    public static final Identifier ANIMATION_LOCATION = new Identifier(ParCool.MOD_ID, "animation");

    public static final ComponentKey<Animation> ANIMATION_CAPABILITY = (ComponentKey<Animation>) ComponentRegistry.get(ANIMATION_LOCATION);

    public static void register() {
        ComponentRegistry.getOrCreate(PARKOURABILITY_LOCATION, Parkourability.class);
        ComponentRegistry.getOrCreate(STAMINA_LOCATION, IStamina.class);
    }

    @Environment(EnvType.CLIENT)
    public static void registerClient() {
        ComponentRegistry.getOrCreate(ANIMATION_LOCATION, Animation.class);
    }
}
