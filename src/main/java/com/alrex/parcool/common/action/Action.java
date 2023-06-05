package com.alrex.parcool.common.action;

import com.alrex.parcool.common.capability.IStamina;
import com.alrex.parcool.common.capability.impl.Parkourability;
import io.github.fabricators_of_create.porting_lib.event.client.RenderTickStartCallback;
import net.fabricmc.api.Environment;
import net.minecraft.entity.player.PlayerEntity;

import java.nio.ByteBuffer;

import static net.fabricmc.api.EnvType.CLIENT;

public abstract class Action {
	private boolean doing = false;
	private int doingTick = 0;
	private int notDoingTick = 0;

	public void setDoingTick(int doingTick) {
		this.doingTick = doingTick;
	}

	public void setNotDoingTick(int notDoingTick) {
		this.notDoingTick = notDoingTick;
	}

	public int getDoingTick() {
		return doingTick;
	}

	public int getNotDoingTick() {
		return notDoingTick;
	}

	public boolean isDoing() {
		return doing;
	}

	public void setDoing(boolean value) {
		doing = value;
	}

	@Environment(CLIENT)
	public abstract boolean canStart(PlayerEntity player, Parkourability parkourability, IStamina stamina, ByteBuffer startInfo);

	@Environment(CLIENT)
	public abstract boolean canContinue(PlayerEntity player, Parkourability parkourability, IStamina stamina);

	public void onStart(PlayerEntity player, Parkourability parkourability) {
	}

	public void onStartInServer(PlayerEntity player, Parkourability parkourability, ByteBuffer startData) {
	}

	@Environment(CLIENT)
	public void onStartInOtherClient(PlayerEntity player, Parkourability parkourability, ByteBuffer startData) {
	}

	@Environment(CLIENT)
	public void onStartInLocalClient(PlayerEntity player, Parkourability parkourability, IStamina stamina, ByteBuffer startData) {
	}

	public void onStop(PlayerEntity player) {
	}

	public void onStopInServer(PlayerEntity player) {
	}

	public void onStopInOtherClient(PlayerEntity player) {
	}

	public void onStopInLocalClient(PlayerEntity player) {
	}

	public void onWorkingTick(PlayerEntity player, Parkourability parkourability, IStamina stamina) {
	}

	public void onWorkingTickInServer(PlayerEntity player, Parkourability parkourability, IStamina stamina) {
	}

	@Environment(CLIENT)
	public void onWorkingTickInClient(PlayerEntity player, Parkourability parkourability, IStamina stamina) {
	}

	@Environment(CLIENT)
	public void onWorkingTickInLocalClient(PlayerEntity player, Parkourability parkourability, IStamina stamina) {
	}

	public void onTick(PlayerEntity player, Parkourability parkourability, IStamina stamina) {
	}

	public void onServerTick(PlayerEntity player, Parkourability parkourability, IStamina stamina) {
	}

	@Environment(CLIENT)
	public void onClientTick(PlayerEntity player, Parkourability parkourability, IStamina stamina) {
	}

	@Environment(CLIENT)
	public void onRenderTick(RenderTickStartCallback callback, PlayerEntity player, Parkourability parkourability) {
	}

	public void restoreSynchronizedState(ByteBuffer buffer) {
	}

	public void saveSynchronizedState(ByteBuffer buffer) {
	}

	public abstract StaminaConsumeTiming getStaminaConsumeTiming();
}
