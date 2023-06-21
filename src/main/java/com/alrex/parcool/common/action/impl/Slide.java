package com.alrex.parcool.common.action.impl;

import com.alrex.parcool.ParCoolConfig;
import com.alrex.parcool.client.animation.impl.CrawlAnimator;
import com.alrex.parcool.client.animation.impl.SlidingAnimator;
import com.alrex.parcool.client.input.KeyRecorder;
import com.alrex.parcool.common.action.Action;
import com.alrex.parcool.common.action.StaminaConsumeTiming;
import com.alrex.parcool.common.capability.IStamina;
import com.alrex.parcool.common.capability.impl.Animation;
import com.alrex.parcool.common.capability.impl.Parkourability;
import com.alrex.parcool.utilities.EntityUtil;
import com.alrex.parcool.utilities.VectorUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;

import java.nio.ByteBuffer;

;

public class Slide extends Action {
	private Vec3d slidingVec = null;

	@Override
	public boolean canStart(PlayerEntity player, Parkourability parkourability, IStamina stamina, ByteBuffer startInfo) {
		return (parkourability.getActionInfo().can(Crawl.class)
				&& !stamina.isExhausted()
				&& parkourability.getActionInfo().can(Slide.class)
				&& KeyRecorder.keyCrawlState.isPressed()
				&& !parkourability.get(Roll.class).isDoing()
				&& !parkourability.get(Tap.class).isDoing()
				&& parkourability.get(Crawl.class).isDoing()
				&& !player.isInsideWaterOrBubbleColumn()
				&& (player.isOnGround() || !ParCoolConfig.CONFIG_CLIENT.disableCrawlInAir.get())
				&& parkourability.get(FastRun.class).getDashTick(parkourability.getAdditionalProperties()) > 5
		);
	}

	@Override
	public boolean canContinue(PlayerEntity player, Parkourability parkourability, IStamina stamina) {
		final int maxSlidingTick = ParCoolConfig.CONFIG_CLIENT.slidingContinuableTick.get();
		return getDoingTick() < maxSlidingTick
				&& parkourability.get(Crawl.class).isDoing();
	}

	@Override
	public void onStartInLocalClient(PlayerEntity player, Parkourability parkourability, IStamina stamina, ByteBuffer startData) {
		slidingVec = player.getRotationVector().multiply(1, 0, 1).normalize();
		Animation animation = Animation.get(player);
		if (animation != null) {
			animation.addAnimator(new SlidingAnimator());
		}
	}

	@Override
	public void onStartInOtherClient(PlayerEntity player, Parkourability parkourability, ByteBuffer startData) {
		Animation animation = Animation.get(player);
		if (animation != null && !animation.hasAnimator()) {
			animation.addAnimator(new SlidingAnimator());
		}
	}

	@Override
	public void onWorkingTickInLocalClient(PlayerEntity player, Parkourability parkourability, IStamina stamina) {
		if (player.isOnGround() && slidingVec != null) {
			Vec3d vec = slidingVec.multiply(0.2);
			EntityUtil.addVelocity(player, vec);
		}
	}

	@Override
	public void onStopInLocalClient(PlayerEntity player) {
		Animation animation = Animation.get(player);
		if (animation != null && !animation.hasAnimator()) {
			animation.addAnimator(new CrawlAnimator());
		}
	}

	@Override
	public void onStopInOtherClient(PlayerEntity player) {
		Animation animation = Animation.get(player);
		if (animation != null && !animation.hasAnimator()) {
			animation.addAnimator(new CrawlAnimator());
		}
	}

	@Override
	public void onRenderTick(PlayerEntity player, Parkourability parkourability) {
		if (slidingVec == null || !isDoing()) return;
		player.setYaw((float) VectorUtil.toYawDegree(slidingVec));
	}

	@Override
	public StaminaConsumeTiming getStaminaConsumeTiming() {
		return StaminaConsumeTiming.None;
	}
}
