package com.alrex.parcool.client.input;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.language.LanguageAdapter;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

@Environment(EnvType.CLIENT)
public class KeyBindings {
	private static final GameOptions settings = MinecraftClient.getInstance().options;
	private static final KeyBinding keyBindCrawl = new KeyBinding("key.parcool.Crawl", GLFW.GLFW_KEY_C, "key.categories.parcool");
	private static final KeyBinding keyBindGrabWall = new KeyBinding("key.parcool.ClingToCliff", InputUtil.Type.MOUSE, GLFW.GLFW_MOUSE_BUTTON_RIGHT, "key.categories.parcool");
	private static final KeyBinding keyBindBreakfall = new KeyBinding("key.parcool.Breakfall", GLFW.GLFW_KEY_R, "key.categories.parcool");
	private static final KeyBinding keyBindFastRunning = new KeyBinding("key.parcool.FastRun", GLFW.GLFW_KEY_LEFT_CONTROL, "key.categories.parcool");
	private static final KeyBinding keyBindFlipping = new KeyBinding("key.parcool.Flipping", GLFW.GLFW_KEY_UNKNOWN, "key.categories.parcool");
	private static final KeyBinding keyBindVault = new KeyBinding("key.parcool.Vault", InputUtil.Type.MOUSE, GLFW.GLFW_MOUSE_BUTTON_RIGHT, "key.categories.parcool");
	private static final KeyBinding keyBindDodge = new KeyBinding("key.parcool.Dodge", GLFW.GLFW_KEY_R, "key.categories.parcool");
	private static final KeyBinding keyBindWallJump = new KeyBinding("key.parcool.WallJump", GLFW.GLFW_KEY_SPACE, "key.categories.parcool");
	private static final KeyBinding keyBindHangDown = new KeyBinding("key.parcool.HangDown", InputUtil.Type.MOUSE, GLFW.GLFW_MOUSE_BUTTON_RIGHT, "key.categories.parcool");
	private static final KeyBinding keyBindWallSlide = new KeyBinding("key.parcool.WallSlide", InputUtil.Type.MOUSE, GLFW.GLFW_MOUSE_BUTTON_RIGHT, "key.categories.parcool");
	private static final KeyBinding keyBindHorizontalWallRun = new KeyBinding("key.parcool.HorizontalWallRun", GLFW.GLFW_KEY_R, "key.categories.parcool");
	private static final KeyBinding keyBindQuickTurn = new KeyBinding("key.parcool.QuickTurn", GLFW.GLFW_KEY_UNKNOWN, "key.categories.parcool");
	private static final KeyBinding keyBindOpenSettings = new KeyBinding("key.parcool.openSetting", KeyConf.UNIVERSAL, KeyM.ALT, InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_P, "key.categories.parcool");

	public static KeyBinding getKeySprint() {
		return settings.keySprint;
	}

	public static KeyBinding getKeyJump() {
		return settings.keyJump;
	}

	public static KeyBinding getKeySneak() {
		return settings.keyShift;
	}

	public static KeyBinding getKeyLeft() {
		return settings.keyLeft;
	}

	public static KeyBinding getKeyRight() {
		return settings.keyRight;
	}

	public static KeyBinding getKeyForward() {
		return settings.keyUp;
	}

	public static KeyBinding getKeyBack() {
		return settings.keyDown;
	}

	public static KeyBinding getKeyCrawl() {
		return keyBindCrawl;
	}

	public static KeyBinding getKeyQuickTurn() {
		return keyBindQuickTurn;
	}

	public static KeyBinding getKeyGrabWall() {
		return keyBindGrabWall;
	}

	public static KeyBinding getKeyVault() {
		return keyBindVault;
	}

	public static KeyBinding getKeyActivateParCool() {
		return keyBindOpenSettings;
	}

	public static KeyBinding getKeyBreakfall() {
		return keyBindBreakfall;
	}

	public static KeyBinding getKeyFastRunning() {
		return keyBindFastRunning;
	}

	public static KeyBinding getKeyDodge() {
		return keyBindDodge;
	}

	public static KeyBinding getKeyWallSlide() {
		return keyBindWallSlide;
	}

	public static KeyBinding getKeyHangDown() {
		return keyBindHangDown;
	}

	public static KeyBinding getKeyHorizontalWallRun() {
		return keyBindHorizontalWallRun;
	}

	public static KeyBinding getKeyWallJump() {
		return keyBindWallJump;
	}

	public static KeyBinding getKeyFlipping() {
		return keyBindFlipping;
	}

	@SubscribeEvent
	public static void register(FMLClientSetupEvent event) {
		ClientRegistry.registerKeyBinding(keyBindCrawl);
		ClientRegistry.registerKeyBinding(keyBindGrabWall);
		ClientRegistry.registerKeyBinding(keyBindBreakfall);
		ClientRegistry.registerKeyBinding(keyBindFastRunning);
		ClientRegistry.registerKeyBinding(keyBindDodge);
		ClientRegistry.registerKeyBinding(keyBindWallSlide);
		ClientRegistry.registerKeyBinding(keyBindWallJump);
		ClientRegistry.registerKeyBinding(keyBindVault);
		ClientRegistry.registerKeyBinding(keyBindHorizontalWallRun);
		ClientRegistry.registerKeyBinding(keyBindOpenSettings);
		ClientRegistry.registerKeyBinding(keyBindQuickTurn);
		ClientRegistry.registerKeyBinding(keyBindFlipping);
		ClientRegistry.registerKeyBinding(keyBindHangDown);
	}
}
