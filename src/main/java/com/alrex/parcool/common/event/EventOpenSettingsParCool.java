package com.alrex.parcool.common.event;

import com.alrex.parcool.ParCoolConfig;
import com.alrex.parcool.client.gui.ParCoolSettingScreen;
import com.alrex.parcool.client.input.KeyRecorder;
import com.alrex.parcool.common.capability.impl.Parkourability;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.text.LiteralText;

;import static net.fabricmc.api.EnvType.CLIENT;

@Environment(CLIENT)
public class EventOpenSettingsParCool {

	public static void onTick() {
		if (true) {return;}
		if (KeyRecorder.keyOpenSettingsState.isPressed()) {
			ClientPlayerEntity player = MinecraftClient.getInstance().player;
			if (player == null) return;
			Parkourability parkourability = Parkourability.get(player);
			if (parkourability == null) return;
			MinecraftClient.getInstance().setScreen(new ParCoolSettingScreen(new LiteralText("ParCool Setting"), parkourability.getActionInfo(), ParCoolConfig.CONFIG_CLIENT.guiColorTheme.get()));
		}
	}
}
