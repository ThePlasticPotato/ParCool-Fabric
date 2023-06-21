package com.alrex.parcool.client.hud.impl;

import com.alrex.parcool.ParCoolConfig;
import com.alrex.parcool.common.capability.IStamina;
import com.alrex.parcool.common.capability.impl.Stamina;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.network.ClientPlayerEntity;

@Environment(EnvType.CLIENT)
public class StaminaHudManager {

    private int maxStamina = ParCoolConfig.CONFIG_SERVER.staminaMax.get(); // Get directly from config instead of Configurator/StaminaManager because neither initialized
    private final float[] staminaBarChunks = new float[181];

    private int currentStamina = 0;

    public StaminaHudManager() {
        float chunkAmount = (float) (ParCoolConfig.CONFIG_SERVER.staminaMax.get() / 181.0);
        staminaBarChunks[0] = chunkAmount;

        for (int i = 1; i <= 180; i++) {
            staminaBarChunks[i] = staminaBarChunks[i - 1] + chunkAmount;
        }
    }

    private void recalculateBarChunks() {
        float chunkAmount = (float) (maxStamina / 181.0); // 181, being the length of pixels on the bar
        staminaBarChunks[0] = chunkAmount;

        for (int i = 1; i <= 180; i++) {
            staminaBarChunks[i] = staminaBarChunks[i - 1] + chunkAmount;
        }
    }

    public void refreshMaxStamina(int stam) {
        maxStamina = stam;
        recalculateBarChunks();
    }

    public int getBarCoords() {
        if (StaminaHUDController.getInstance() == null) { return 100;}

        int stamina = currentStamina;

        if (stamina == maxStamina) { return 181; }

        for (int i = 180; i >= 0; i--) {
            if (stamina > staminaBarChunks[i]) { return i;}
        }

        return 0;
    }

    public void tick(ClientPlayerEntity player) {
        if (player == null) { return; }
        IStamina stamina = IStamina.get(player);
        if (stamina != null) {

            refreshMaxStamina(stamina.getActualMaxStamina());

            currentStamina = Math.min(stamina.get(), maxStamina);
        }
    }

//    public int getStaminaValue() {
//        return staminaManager.getStamina() < 0 ? 0 : Math.min(staminaManager.getStamina(), maxStamina);
//
//    }
}