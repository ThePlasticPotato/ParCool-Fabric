package com.alrex.parcool.client.hud.impl;


import com.alrex.parcool.ParCoolConfig;
import com.alrex.parcool.client.hud.Position;
import com.alrex.parcool.common.action.impl.CatLeap;
import com.alrex.parcool.common.action.impl.Dodge;
import com.alrex.parcool.common.capability.IStamina;
import com.alrex.parcool.common.capability.impl.Parkourability;
import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;

import java.util.function.Supplier;

;import static net.fabricmc.api.EnvType.CLIENT;

@Environment(CLIENT)
public class StaminaHUD extends DrawableHelper {
	public static final Identifier STAMINA = new Identifier("parcool", "textures/gui/stamina_bar.png");

	private final Supplier<Position> pos;

	public StaminaHUD(Supplier<Position> pos) {
		this.pos = pos;
	}

	private float shadowScale = 1f;
	//0,1,2
	private int renderGageType = 0;
	private int renderGageTick = 0;

	public void onTick(ClientPlayerEntity player) {
		if (++renderGageTick >= 5) {
			renderGageTick = 0;
			if (++renderGageType > 2) {
				renderGageType = 0;
			}
		}
	}

	public void render(MatrixStack stack, float partialTick, int width, int height) {
		ClientPlayerEntity player = MinecraftClient.getInstance().player;
		if (player == null) return;
		if (player.isCreative()) return;

		IStamina stamina = IStamina.get(player);
		Parkourability parkourability = Parkourability.get(player);
		if (stamina == null || parkourability == null) return;

		if (ParCoolConfig.CONFIG_CLIENT.infiniteStamina.get() && parkourability.getActionInfo().isInfiniteStaminaPermitted())
			return;

		final int boxWidth = 91;
		final int boxHeight = 17;
		final Pair<Integer, Integer> pos = this.pos.get().calculate(boxWidth, boxHeight, width, height);

		float staminaScale = (float) stamina.get() / stamina.getActualMaxStamina();
		float coolTimeScale =
				Math.min(
						parkourability.get(Dodge.class).getCoolDownPhase(),
						parkourability.get(CatLeap.class).getCoolDownPhase()
				);
		if (staminaScale < 0) staminaScale = 0;
		if (staminaScale > 1) staminaScale = 1;

		RenderSystem.setShaderTexture(0, STAMINA);
		drawTexture(stack, pos.getLeft(), pos.getRight(), 0, 0, 93, 17, 128, 128);
		if (!stamina.isExhausted()) {
			drawTexture(stack, pos.getLeft(), pos.getRight(), 0, 102, (int) Math.ceil(92 * coolTimeScale), 17, 128, 128);
			drawTexture(stack, pos.getLeft(), pos.getRight(), 0, 85, Math.round(16 + 69 * shadowScale) + 1, 12, 128, 128);
			drawTexture(stack, pos.getLeft(), pos.getRight(), 0, 17 * (renderGageType + 1), Math.round(16 + 69 * staminaScale) + 1, 12, 128, 128);
		} else {
			drawTexture(stack, pos.getLeft(), pos.getRight(), 0, 68, Math.round(16 + 69 * staminaScale) + 1, 17, 128, 128);
		}
		shadowScale = staminaScale - (staminaScale - shadowScale) / 1.1f;
	}
}
