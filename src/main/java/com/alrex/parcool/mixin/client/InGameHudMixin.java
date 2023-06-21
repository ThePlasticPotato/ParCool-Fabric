package com.alrex.parcool.mixin.client;

import com.alrex.parcool.ParCoolClient;
import com.alrex.parcool.client.hud.impl.StaminaHUDController;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class InGameHudMixin extends DrawableHelper {

    @Inject(method = "render", at = @At("HEAD"))
    public void atRender(MatrixStack matrices, float tickDelta, CallbackInfo ci) {
        ParCoolClient.staminaRenderer.render(new MatrixStack());
    }

}
