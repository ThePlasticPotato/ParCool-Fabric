package com.alrex.parcool.client.animation;

import com.alrex.parcool.common.capability.impl.Parkourability;
import net.minecraft.entity.player.PlayerEntity;

;

public abstract class Animator {
	private int tick = 0;

	public final void tick() {
		tick++;
	}

	protected int getTick() {
		return tick;
	}

	public abstract boolean shouldRemoved(PlayerEntity player, Parkourability parkourability);

	/**
	 * @return You should return true if you want to cancel vanilla animation to control all about rendering
	 */
	public boolean animatePre(
			PlayerEntity player,
			Parkourability parkourability,
			PlayerModelTransformer transformer
	) {
		return false;
	}

	/**
	 * Called after vanilla animation is done
	 * You can utilize this to use partially vanilla animation
	 */
	public void animatePost(
			PlayerEntity player,
			Parkourability parkourability,
			PlayerModelTransformer transformer
	) {
	}

	public void rotate(
			PlayerEntity player,
			Parkourability parkourability,
			PlayerModelRotator rotator
	) {
	}

	public void onCameraSetUp(
			PlayerEntity clientPlayer,
			Parkourability parkourability
	) {
	}
}
