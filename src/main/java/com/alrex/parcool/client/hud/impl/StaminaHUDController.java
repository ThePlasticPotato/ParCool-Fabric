
//todo: stamina

//package com.alrex.parcool.client.hud.impl;
//
//import com.alrex.parcool.ParCoolConfig;
//import com.alrex.parcool.client.hud.Position;
//import net.fabricmc.api.EnvType;
//import net.fabricmc.api.Environment;
//
//@Environment(EnvType.CLIENT)
//public class StaminaHUDController implements Ingame {
//	private static StaminaHUDController instance = null;
//
//	public static StaminaHUDController getInstance() {
//		if (instance == null) instance = new StaminaHUDController();
//		return instance;
//	}
//
//	LightStaminaHUD lightStaminaHUD;
//	StaminaHUD staminaHUD;
//
//	Position getStaminaHUDPosition() {
//		return new Position(
//				ParCoolConfig.CONFIG_CLIENT.alignHorizontalStaminaHUD.get(),
//				ParCoolConfig.CONFIG_CLIENT.alignVerticalStaminaHUD.get(),
//				ParCoolConfig.CONFIG_CLIENT.marginHorizontalStaminaHUD.get(),
//				ParCoolConfig.CONFIG_CLIENT.marginVerticalStaminaHUD.get()
//		);
//	}
//
//	private StaminaHUDController() {
//		lightStaminaHUD = new LightStaminaHUD();
//		staminaHUD = new StaminaHUD(this::getStaminaHUDPosition);
//	}
//
//	public void onTick(TickEvent.ClientTickEvent event) {
//		LocalPlayer player = Minecraft.getInstance().player;
//		if (player == null || player.isCreative()) return;
//		lightStaminaHUD.onTick(event, player);
//		staminaHUD.onTick(event, player);
//	}
//
//	@Override
//	public void render(ForgeIngameGui gui, PoseStack poseStack, float partialTick, int width, int height) {
//		ParCoolConfig.Client config = ParCoolConfig.CONFIG_CLIENT;
//		if (config.hideStaminaHUD.get()
//				|| !config.parCoolActivation.get()
//				|| config.useHungerBarInsteadOfStamina.get()
//				|| FeathersManager.isUsingFeathers()
//		) return;
//
//		if (config.useLightHUD.get()) {
//			lightStaminaHUD.render(gui, poseStack, partialTick, width, height);
//		} else {
//			staminaHUD.render(gui, poseStack, partialTick, width, height);
//		}
//	}
//}
