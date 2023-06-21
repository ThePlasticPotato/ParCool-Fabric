package com.alrex.parcool.client.hud.impl;

import com.alrex.parcool.ParCoolConfig;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class StaminaRenderer extends DrawableHelper {

    private static final Identifier STAMINA_BAR = new Identifier("parcool:textures/gui/stamina_bar.png");
    private final MinecraftClient client;
    public final StaminaHudManager staminaHudManager;

    public StaminaRenderer(MinecraftClient client) {
        this.client = client;
        this.staminaHudManager = new StaminaHudManager();
    }

//    private String temp(int i) { // For Stamina Renderer
//        return switch (i) {
//            case 1 -> "Cooldown: ";
//            case 2 -> "Stamina Deduction: ";
//            case 3 -> "Stamina Restoration: ";
//            case 4 -> "Stamina Debuff: ";
//            case 5 -> "Stamina Debuff Switch: ";
//            case 6 -> "Check If Enabled Delay: ";
//            case 7 -> "Check If Enabled Cache: ";
//            default -> "ERR: ";
//        };
//    }

    public void tick() {
        ClientPlayerEntity player = client.player;

        if (player != null) {
            staminaHudManager.tick(player);
        }
    }

    public void render(MatrixStack stack) {
        ClientPlayerEntity player = client.player;

        int scaledWidth = this.client.getWindow().getScaledWidth() / 2;
        int scaledHeight = this.client.getWindow().getScaledHeight();

//        short temp = 40;
//
//        for (short i = 1; i < 8; i++) { // Stamina Renderer
//
//            client.textRenderer.draw(stack, this.temp(i) + ActionStamina.temp(i), scaledWidth - 175, scaledHeight - temp, 0xFFFFFF);
//            temp = (short) (temp + 20);
//        }

//        client.textRenderer.draw(stack,
//                String.valueOf(SprintOMeter.staminaManager.getStamina()),
//                scaledWidth - 9,
//                scaledHeight - 45,
//                0xFFFF00
//        );



        // Display
        if (!ParCoolConfig.CONFIG_CLIENT.hideStaminaHUD.get() && player != null && !player.isSpectator()) {

//            client.textRenderer.drawWithShadow(stack,
//                    String.valueOf(staminaHudManager.getStaminaValue()),
//                    scaledWidth - 115,
//                    scaledHeight - staminaHudManager.getNumberCoords(),
//                    staminaHudManager.getNumberColor()
//            );
//
//            RenderSystem.setShaderTexture(0, STAMINA_OVERLAY_ICON);
//                int staminaIconCoords = staminaHudManager.getIconCoords();
//                drawTexture(stack, scaledWidth - 112, scaledHeight - staminaIconCoords, 0, 0, 18, 18, 18, 18);
//            tempCache = (byte) staminaIconCoords;

            RenderSystem.setShaderTexture(0, STAMINA_BAR);
            drawTexture(stack, scaledWidth - 91, scaledHeight - 32 + 5, 0, 0, 182, 3);
            drawTexture(stack,scaledWidth - 91, scaledHeight - 32 + 5,0, 3, staminaHudManager.getBarCoords(), 3);

        }

    }
}