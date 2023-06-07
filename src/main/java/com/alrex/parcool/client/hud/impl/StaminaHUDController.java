package com.alrex.parcool.client.hud.impl;

import com.alrex.parcool.ParCoolConfig;
import com.alrex.parcool.client.hud.Position;
import com.alrex.parcool.mixin.client.IngameGuiAccessor;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.util.math.MatrixStack;

@Environment(EnvType.CLIENT)
public class StaminaHUDController {
	private static StaminaHUDController instance = null;

	public static StaminaHUDController getInstance() {
		if (instance == null) instance = new StaminaHUDController();
		return instance;
	}

	LightStaminaHUD lightStaminaHUD;
	StaminaHUD staminaHUD;

	Position getStaminaHUDPosition() {
		return new Position(
				ParCoolConfig.CONFIG_CLIENT.alignHorizontalStaminaHUD.get(),
				ParCoolConfig.CONFIG_CLIENT.alignVerticalStaminaHUD.get(),
				ParCoolConfig.CONFIG_CLIENT.marginHorizontalStaminaHUD.get(),
				ParCoolConfig.CONFIG_CLIENT.marginVerticalStaminaHUD.get()
		);
	}

	private StaminaHUDController() {
		lightStaminaHUD = new LightStaminaHUD();
		staminaHUD = new StaminaHUD(this::getStaminaHUDPosition);
	}

	public void onTick() {
		ClientPlayerEntity player = MinecraftClient.getInstance().player;
		if (player == null || player.isCreative()) return;
		lightStaminaHUD.onTick(player);
		staminaHUD.onTick(player);
	}

	public void render(MatrixStack poseStack, float partialTick) {
		ParCoolConfig.Client config = ParCoolConfig.CONFIG_CLIENT;
		if (config.hideStaminaHUD.get()
				|| !config.parCoolActivation.get()
				|| config.useHungerBarInsteadOfStamina.get()
		) return;

        int height = ((IngameGuiAccessor) MinecraftClient.getInstance().inGameHud).getScaledHeight();
        int width = ((IngameGuiAccessor) MinecraftClient.getInstance().inGameHud).getScaledWidth();

		if (config.useLightHUD.get()) {
			lightStaminaHUD.render(poseStack, partialTick, width, height);
		} else {
			staminaHUD.render(poseStack, partialTick, width, height);
		}
	}
}
