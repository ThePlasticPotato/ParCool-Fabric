package com.alrex.parcool.utilities.ticker;

import com.alrex.parcool.ParCool;
import com.alrex.parcool.ParCoolClient;
import com.alrex.parcool.common.action.Action;
import com.alrex.parcool.common.action.ActionProcessor;
import com.alrex.parcool.common.capability.capabilities.Capabilities;
import com.alrex.parcool.common.capability.impl.Parkourability;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import io.github.fabricators_of_create.porting_lib.event.client.RenderTickStartCallback;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;

import java.util.HashMap;
@SuppressWarnings("ApiStatus.Experimental")
public class RenderTickHandler implements RenderTickStartCallback {

    @Override
    public void tick() {
        ParCool.ACTION_PROCESSOR.onRenderTick();
//        if (MinecraftClient.getInstance() != null) {
//            ParCoolClient.staminaRenderer.render(new MatrixStack());
//        }
    }


}
