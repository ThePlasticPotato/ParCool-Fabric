package com.alrex.parcool.client.input;

import com.mojang.blaze3d.platform.InputConstants;
import committee.nova.mkb.api.IKeyBinding;
import committee.nova.mkb.keybinding.KeyBindingMap;
import committee.nova.mkb.keybinding.KeyConflictContext;
import committee.nova.mkb.keybinding.KeyModifier;
import io.github.fabricators_of_create.porting_lib.util.KeyBindingHelper;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.keybinding.KeyBindingRegistry;
import net.fabricmc.fabric.impl.client.keybinding.KeyBindingRegistryImpl;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import org.lwjgl.glfw.GLFW;

@Environment(EnvType.CLIENT)
public class KeyBindings {
//	private static final Options Minecraft.getInstance().options = Minecraft.getInstance().options;
	private static final KeyMapping keyBindCrawl = new KeyMapping("key.parcool.Crawl", GLFW.GLFW_KEY_C, "key.categories.parcool");
	private static final KeyMapping keyBindGrabWall = new KeyMapping("key.parcool.ClingToCliff", InputConstants.Type.MOUSE, GLFW.GLFW_MOUSE_BUTTON_RIGHT, "key.categories.parcool");
	private static final KeyMapping keyBindBreakfall = new KeyMapping("key.parcool.Breakfall", GLFW.GLFW_KEY_R, "key.categories.parcool");
	private static final KeyMapping keyBindFastRunning = new KeyMapping("key.parcool.FastRun", GLFW.GLFW_KEY_LEFT_CONTROL, "key.categories.parcool");
	private static final KeyMapping keyBindFlipping = new KeyMapping("key.parcool.Flipping", GLFW.GLFW_KEY_UNKNOWN, "key.categories.parcool");
	private static final KeyMapping keyBindVault = new KeyMapping("key.parcool.Vault", InputConstants.Type.MOUSE, GLFW.GLFW_MOUSE_BUTTON_RIGHT, "key.categories.parcool");
	private static final KeyMapping keyBindDodge = new KeyMapping("key.parcool.Dodge", GLFW.GLFW_KEY_R, "key.categories.parcool");
	private static final KeyMapping keyBindWallJump = new KeyMapping("key.parcool.WallJump", GLFW.GLFW_KEY_SPACE, "key.categories.parcool");
	private static final KeyMapping keyBindHangDown = new KeyMapping("key.parcool.HangDown", InputConstants.Type.MOUSE, GLFW.GLFW_MOUSE_BUTTON_RIGHT, "key.categories.parcool");
	private static final KeyMapping keyBindWallSlide = new KeyMapping("key.parcool.WallSlide", InputConstants.Type.MOUSE, GLFW.GLFW_MOUSE_BUTTON_RIGHT, "key.categories.parcool");
	private static final KeyMapping keyBindHorizontalWallRun = new KeyMapping("key.parcool.HorizontalWallRun", GLFW.GLFW_KEY_R, "key.categories.parcool");
	private static final KeyMapping keyBindQuickTurn = new KeyMapping("key.parcool.QuickTurn", GLFW.GLFW_KEY_UNKNOWN, "key.categories.parcool");
	private static final KeyMapping keyBindOpenSettings = new KeyMapping("key.parcool.openSetting", GLFW.GLFW_KEY_P, "key.categories.parcool");

	public static KeyMapping getKeySprint() {
		return Minecraft.getInstance().options.keySprint;
	}

	public static KeyMapping getKeyJump() {
		return Minecraft.getInstance().options.keyJump;
	}

	public static KeyMapping getKeySneak() {
		return Minecraft.getInstance().options.keyShift;
	}

	public static KeyMapping getKeyLeft() {
		return Minecraft.getInstance().options.keyLeft;
	}

	public static KeyMapping getKeyRight() {
		return Minecraft.getInstance().options.keyRight;
	}

	public static KeyMapping getKeyForward() {
		return Minecraft.getInstance().options.keyUp;
	}

	public static KeyMapping getKeyBack() {
		return Minecraft.getInstance().options.keyDown;
	}

	public static KeyMapping getKeyCrawl() {
		return keyBindCrawl;
	}

	public static KeyMapping getKeyQuickTurn() {
		return keyBindQuickTurn;
	}

	public static KeyMapping getKeyGrabWall() {
		return keyBindGrabWall;
	}

	public static KeyMapping getKeyVault() {
		return keyBindVault;
	}

	public static KeyMapping getKeyActivateParCool() {
		return keyBindOpenSettings;
	}

	public static KeyMapping getKeyBreakfall() {
		return keyBindBreakfall;
	}

	public static KeyMapping getKeyFastRunning() {
		return keyBindFastRunning;
	}

	public static KeyMapping getKeyDodge() {
		return keyBindDodge;
	}

	public static KeyMapping getKeyWallSlide() {
		return keyBindWallSlide;
	}

	public static KeyMapping getKeyHangDown() {
		return keyBindHangDown;
	}

	public static KeyMapping getKeyHorizontalWallRun() {
		return keyBindHorizontalWallRun;
	}

	public static KeyMapping getKeyWallJump() {
		return keyBindWallJump;
	}

	public static KeyMapping getKeyFlipping() {
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
		IKeyBinding extendedSettingsKey = (IKeyBinding) keyBindOpenSettings;
		extendedSettingsKey.setKeyModifierAndCode(KeyModifier.ALT, InputConstants.getKey("key.keyboard.p"));
		extendedSettingsKey.setKeyConflictContext(KeyConflictContext.UNIVERSAL);
		KeyBindingRegistryImpl.registerKeyBinding(keyBindOpenSettings);
		KeyBindingRegistryImpl.registerKeyBinding(keyBindQuickTurn);
		KeyBindingRegistryImpl.registerKeyBinding(keyBindFlipping);
		KeyBindingRegistryImpl.registerKeyBinding(keyBindHangDown);
	}
}
