package com.alrex.parcool.client.hud.impl;

import com.alrex.parcool.ParCoolConfig;
import com.alrex.parcool.common.capability.IStamina;
import com.alrex.parcool.common.capability.impl.Parkourability;
import com.alrex.parcool.mixin.client.IngameGuiAccessor;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.gui.hud.InGameOverlayRenderer;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.util.math.MatrixStack;

;

public class LightStaminaHUD extends DrawableHelper {
	private long lastStaminaChangedTick = 0;
	//1-> recovering, -1->consuming, 0->no changing
	private int lastChangingSign = 0;
	private int changingSign = 0;
	private long changingTimeTick = 0;
	private int randomOffset = 0;
	private boolean justBecameMax = false;

	public void onTick(ClientPlayerEntity player) {
		IStamina stamina = IStamina.get(player);
		if (stamina == null) return;
		changingSign = (int) Math.signum(stamina.get() - stamina.getOldValue());
		final long gameTime = player.getWorld().getTime();
		if (changingSign != lastChangingSign) {
			lastChangingSign = changingSign;
			changingTimeTick = 0;
		} else {
			changingTimeTick++;
		}
		if (player.getRandom().nextInt(5) == 0) {
			randomOffset += player.getRandom().nextBoolean() ? 1 : -1;
		} else {
			randomOffset = 0;
		}
		if (stamina.get() != stamina.getOldValue() || stamina.isExhausted()) {
			lastStaminaChangedTick = gameTime;
		}
		justBecameMax = stamina.getOldValue() < stamina.get() && stamina.get() == stamina.getActualMaxStamina();
	}

	public void render(MatrixStack stack, float partialTick, int width, int height) {
		ClientPlayerEntity player = MinecraftClient.getInstance().player;
		if (player == null || player.isCreative()) return;

		IStamina stamina = IStamina.get(player);
		Parkourability parkourability = Parkourability.get(player);
		if (stamina == null || parkourability == null) return;

		if (ParCoolConfig.CONFIG_CLIENT.infiniteStamina.get() && parkourability.getActionInfo().isInfiniteStaminaPermitted())
			return;

		long gameTime = player.getWorld().getTime();
		if (gameTime - lastStaminaChangedTick > 40) return;
		float staminaScale = (float) stamina.get() / stamina.getActualMaxStamina();
		if (staminaScale < 0) staminaScale = 0;
		if (staminaScale > 1) staminaScale = 1;
		staminaScale *= 10;
		MinecraftClient mc = MinecraftClient.getInstance();

		RenderSystem.setShaderTexture(0, StaminaHUD.STAMINA);
		int baseX = width / 2 + 92;
		int baseY = height - ((IngameGuiAccessor) MinecraftClient.getInstance().inGameHud).getScaledHeight();
		final boolean exhausted = stamina.isExhausted();
		for (int i = 0; i < 10; i++) {
			int x = baseX - i * 8 - 9;
			int offsetY = 0;
			int textureX = exhausted ? 27 : 0;
			if (justBecameMax) {
				textureX = 81;
			} else if (staminaScale <= i) {//empty
				textureX += 18;
			} else if (staminaScale < i + 0.5f) {//not full
				textureX += 9;
			}
			if (justBecameMax) {
				offsetY = -1;
			} else if (changingSign == 1) {
				if ((changingTimeTick & 31) == i) {
					offsetY = -1;
				}
			} else if (i + 1 > staminaScale && staminaScale > i && changingSign == -1) {
				offsetY = randomOffset;
			}

			drawTexture(stack, x, baseY + offsetY, textureX, 119, 9, 9, 129, 128);
		}
		((IngameGuiAccessor) MinecraftClient.getInstance().inGameHud).setScaledHeight(((IngameGuiAccessor) MinecraftClient.getInstance().inGameHud).getScaledHeight()+ 10);
	}
}
