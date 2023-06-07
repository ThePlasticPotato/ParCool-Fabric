//todo: stamina
//
//package com.alrex.parcool.client.hud;
//
//import com.alrex.parcool.client.hud.impl.StaminaHUDController;
//import net.minecraft.client.gui.DrawableHelper;
//import net.minecraft.client.gui.screen.Overlay;
//
//public class HUDRegistry {
//	private static HUDRegistry instance = null;
//	private static DrawableHelper Stamina_HUD = null;
//
//	public static HUDRegistry getInstance() {
//		if (instance == null) instance = new HUDRegistry();
//		return instance;
//	}
//
//	@SubscribeEvent
//	public void onSetup(FMLCommonSetupEvent event) {
//		Stamina_HUD = .registerOverlayTop("ParCool Stamina", StaminaHUDController.getInstance());
//	}
//
//	@SubscribeEvent
//	public void onTick(TickEvent.ClientTickEvent event) {
//		if (event.phase == TickEvent.Phase.START) return;
//		StaminaHUDController.getInstance().onTick(event);
//	}
//}
