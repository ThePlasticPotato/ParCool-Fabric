package com.alrex.parcool.client.input;

import committee.nova.mkb.api.IKeyBinding;
import committee.nova.mkb.keybinding.KeyConflictContext;
import committee.nova.mkb.keybinding.KeyModifier;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.impl.client.keybinding.KeyBindingRegistryImpl;
import net.fabricmc.loader.language.LanguageAdapter;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

@Environment(EnvType.CLIENT)
public class KeyBindings {
//	private static final GameOptions settings = MinecraftClient.getInstance().options;
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
	private static final KeyBinding keyBindOpenSettings = new KeyBinding("key.parcool.openSetting", GLFW.GLFW_KEY_P, "key.categories.parcool");

	public static KeyBinding getKeySprint() {
		return MinecraftClient.getInstance().options.sprintKey;
	}

	public static KeyBinding getKeyJump() {
		return MinecraftClient.getInstance().options.jumpKey;
	}

	public static KeyBinding getKeySneak() {
		return MinecraftClient.getInstance().options.sneakKey;
	}

	public static KeyBinding getKeyLeft() {
		return MinecraftClient.getInstance().options.leftKey;
	}

	public static KeyBinding getKeyRight() {
		return MinecraftClient.getInstance().options.rightKey;
	}

	public static KeyBinding getKeyForward() {
		return MinecraftClient.getInstance().options.forwardKey;
	}

	public static KeyBinding getKeyBack() {
		return MinecraftClient.getInstance().options.backKey;
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


	public static void register() {
		KeyBindingRegistryImpl.registerKeyBinding(keyBindCrawl);
		KeyBindingRegistryImpl.registerKeyBinding(keyBindGrabWall);
		KeyBindingRegistryImpl.registerKeyBinding(keyBindBreakfall);
		KeyBindingRegistryImpl.registerKeyBinding(keyBindFastRunning);
		KeyBindingRegistryImpl.registerKeyBinding(keyBindDodge);
		KeyBindingRegistryImpl.registerKeyBinding(keyBindWallSlide);
		KeyBindingRegistryImpl.registerKeyBinding(keyBindWallJump);
		KeyBindingRegistryImpl.registerKeyBinding(keyBindVault);
		KeyBindingRegistryImpl.registerKeyBinding(keyBindHorizontalWallRun);
		IKeyBinding keyBindOpenSettingsModified = (IKeyBinding) keyBindOpenSettings;
		keyBindOpenSettingsModified.setKeyConflictContext(KeyConflictContext.UNIVERSAL);
		keyBindOpenSettingsModified.setKeyModifierAndCode(KeyModifier.ALT, InputUtil.fromTranslationKey("key.keyboard.p"));
		KeyBindingRegistryImpl.registerKeyBinding((KeyBinding) keyBindOpenSettingsModified);
		KeyBindingRegistryImpl.registerKeyBinding(keyBindQuickTurn);
		KeyBindingRegistryImpl.registerKeyBinding(keyBindFlipping);
		KeyBindingRegistryImpl.registerKeyBinding(keyBindHangDown);
	}
}
