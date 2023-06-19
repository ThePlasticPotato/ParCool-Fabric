package com.alrex.parcool.mixin.client;

import net.minecraft.client.gui.hud.InGameHud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(InGameHud.class)
public interface IngameGuiAccessor {
    @Accessor
    int getScaledHeight();

    @Accessor
    void setScaledHeight(int scaledHeight);

    @Accessor
    int getScaledWidth();

    @Accessor
    void setScaledWidth(int scaledWidth);
}
