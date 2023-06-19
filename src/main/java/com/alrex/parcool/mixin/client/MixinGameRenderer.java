package com.alrex.parcool.mixin.client;

import com.alrex.parcool.ParCool;
import com.alrex.parcool.common.action.ActionProcessor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.world.ClientWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class MixinGameRenderer {
    @Inject(method = "render", at = @At("HEAD"))
    private void preRender(final float tickDelta, final long startTime, final boolean tick, final CallbackInfo ci) {
//        MinecraftClient.getInstance().getTickDelta() = tickDelta;
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void preTick(final CallbackInfo ci) {
    }

}
