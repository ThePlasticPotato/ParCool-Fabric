package com.alrex.parcool.common.action.impl;

import com.alrex.parcool.client.input.KeyRecorder;
import com.alrex.parcool.common.action.Action;
import com.alrex.parcool.common.action.StaminaConsumeTiming;
import com.alrex.parcool.common.capability.IStamina;
import com.alrex.parcool.common.capability.impl.Parkourability;
import com.alrex.parcool.utilities.VectorUtil;
import net.minecraft.world.entity.player.PlayerEntity;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.TickEvent;

import java.nio.ByteBuffer;

;

public class QuickTurn extends Action {
	private static final int AnimationTickLength = 4;
	private boolean turnRightward = false;
	private Vec3 startAngle = null;

	@Override
	public boolean canStart(PlayerEntity player, Parkourability parkourability, IStamina stamina, ByteBuffer startInfo) {
		Vec3 angle = player.getLookAngle();
		startInfo
				.putDouble(angle.x)
				.putDouble(angle.z);
		return KeyRecorder.keyQuickTurn.isPressed()
				&& !parkourability.get(Vault.class).isDoing()
				&& !parkourability.get(Roll.class).isDoing()
				&& !parkourability.get(Flipping.class).isDoing()
				&& !parkourability.get(ClingToCliff.class).isDoing();
	}

	@Override
	public boolean canContinue(PlayerEntity player, Parkourability parkourability, IStamina stamina) {
		return getDoingTick() < AnimationTickLength;
	}

	@Override
	public void onStartInLocalClient(PlayerEntity player, Parkourability parkourability, IStamina stamina, ByteBuffer startData) {
		turnRightward = !turnRightward;
		startAngle = new Vec3(
				startData.getDouble(),
				0,
				startData.getDouble()
		).normalize();
	}

	@Override
	public void onRenderTick(TickEvent.RenderTickEvent event, PlayerEntity player, Parkourability parkourability) {
		if (isDoing() && startAngle != null) {
			float renderTick = getDoingTick() + event.renderTickTime;
			float animationPhase = renderTick / AnimationTickLength;
			Vec3 rotatedAngle = startAngle.yRot((float) (Math.PI * animationPhase * (turnRightward ? -1 : 1)));
			player.setYRot((float) VectorUtil.toYawDegree(rotatedAngle));
		}
	}

	@Override
	public StaminaConsumeTiming getStaminaConsumeTiming() {
		return StaminaConsumeTiming.None;
	}
}
