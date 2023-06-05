package com.alrex.parcool.client.input;

import net.fabricmc.api.Environment;
import net.minecraft.client.option.KeyBinding;

import static net.fabricmc.api.EnvType.CLIENT;


@Environment(CLIENT)
public class KeyRecorder {
	public static final KeyState keyForward = new KeyState();
	public static final KeyState keyBack = new KeyState();
	public static final KeyState keyRight = new KeyState();
	public static final KeyState keyLeft = new KeyState();
	public static final KeyState keySneak = new KeyState();
	public static final KeyState keyJumpState = new KeyState();
	public static final KeyState keySprintState = new KeyState();
	public static final KeyState keyCrawlState = new KeyState();
	public static final KeyState keyOpenSettingsState = new KeyState();
	public static final KeyState keyFastRunning = new KeyState();
	public static final KeyState keyDodge = new KeyState();
	public static final KeyState keyBreakfall = new KeyState();
	public static final KeyState keyWallJump = new KeyState();
	public static final KeyState keyQuickTurn = new KeyState();
	public static final KeyState keyFlipping = new KeyState();

	//todo tick sussy

	public static void onClientTick() {
		record(KeyBindings.getKeyForward(), keyForward);
		record(KeyBindings.getKeyBack(), keyBack);
		record(KeyBindings.getKeyRight(), keyRight);
		record(KeyBindings.getKeyLeft(), keyLeft);
		record(KeyBindings.getKeySneak(), keySneak);
		record(KeyBindings.getKeyJump(), keyJumpState);
		record(KeyBindings.getKeySprint(), keySprintState);
		record(KeyBindings.getKeyCrawl(), keyCrawlState);
		record(KeyBindings.getKeyActivateParCool(), keyOpenSettingsState);
		record(KeyBindings.getKeyFastRunning(), keyFastRunning);
		record(KeyBindings.getKeyDodge(), keyDodge);
		record(KeyBindings.getKeyBreakfall(), keyBreakfall);
		record(KeyBindings.getKeyWallJump(), keyWallJump);
		record(KeyBindings.getKeyQuickTurn(), keyQuickTurn);
		record(KeyBindings.getKeyFlipping(), keyFlipping);
	}

	private static void record(KeyBinding keyBinding, KeyState state) {
		state.pressed = (keyBinding.isPressed() && state.tickKeyDown == 0);
		state.released = (!keyBinding.isPressed() && state.tickNotKeyDown == 0);
		state.doubleTapped = (keyBinding.isPressed() && 0 < state.tickNotKeyDown && state.tickNotKeyDown <= 2);
		if (keyBinding.isPressed()) {
			state.tickKeyDown++;
			state.tickNotKeyDown = 0;
		} else {
			state.tickKeyDown = 0;
			state.tickNotKeyDown++;
		}
	}

	public static class KeyState {
		private boolean pressed = false;
		private boolean released = false;
		private boolean doubleTapped = false;
		private int tickKeyDown = 0;
		private int tickNotKeyDown = 0;

		public boolean isPressed() {
			return pressed;
		}

		public boolean isReleased() {
			return released;
		}

		public boolean isDoubleTapped() {
			return doubleTapped;
		}

		public int getTickKeyDown() {
			return tickKeyDown;
		}

		public int getTickNotKeyDown() {
			return tickNotKeyDown;
		}
	}
}
