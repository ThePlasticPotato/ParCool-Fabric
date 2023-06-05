package com.alrex.parcool.common.capability.impl;

import com.alrex.parcool.ParCoolConfig;
import com.alrex.parcool.client.animation.Animator;
import com.alrex.parcool.client.animation.PassiveCustomAnimation;
import com.alrex.parcool.client.animation.PlayerModelRotator;
import com.alrex.parcool.client.animation.PlayerModelTransformer;
import com.alrex.parcool.common.capability.capabilities.Capabilities;
import dev.onyxstudios.cca.api.v3.component.Component;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import io.github.fabricators_of_create.porting_lib.util.LazyOptional;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;

import static net.fabricmc.api.EnvType.CLIENT;

@Environment(CLIENT)
public class Animation implements Component {
	public static Animation get(PlayerEntity player) {
		Animation optional = player.getComponent(Capabilities.ANIMATION_CAPABILITY);
		return optional;
	}

	private Animator animator = null;
	private final PassiveCustomAnimation passiveAnimation = new PassiveCustomAnimation();

	public void setAnimator(Animator animator) {
		if (ParCoolConfig.CONFIG_CLIENT.disableAnimation.get()) return;
		ParCoolConfig.Client config = ParCoolConfig.CONFIG_CLIENT;
		if (!config.canAnimate(animator.getClass()).get()) return;
		this.animator = animator;
	}

	public boolean animatePre(PlayerEntity player, PlayerModelTransformer modelTransformer) {
		if (animator == null) return false;
		Parkourability parkourability = Parkourability.get(player);
		return animator.animatePre(player, parkourability, modelTransformer);
	}

	public void animatePost(PlayerEntity player, PlayerModelTransformer modelTransformer) {
		Parkourability parkourability = Parkourability.get(player);
		if (parkourability == null) return;
		if (animator == null) {
			passiveAnimation.animate(player, parkourability, modelTransformer);
			return;
		}
		animator.animatePost(player, parkourability, modelTransformer);
	}

	public void applyRotate(AbstractClientPlayerEntity player, PlayerModelRotator rotator) {
		Parkourability parkourability = Parkourability.get(player);
		if (parkourability == null) return;
		if (animator == null) {
			passiveAnimation.rotate(player, parkourability, rotator);
			return;
		}
		animator.rotate(player, parkourability, rotator);
	}

	public void cameraSetup(PlayerEntity player, Parkourability parkourability) {
		if (animator == null) return;
		if (player.isMainPlayer()
				&& MinecraftClient.getInstance().options.getPerspective().isFirstPerson()
				&& ParCoolConfig.CONFIG_CLIENT.disableFPVAnimation.get()
		) return;
		animator.onCameraSetUp(player, parkourability);
	}

	public void tick(PlayerEntity player, Parkourability parkourability) {
		passiveAnimation.tick(player, parkourability);
		if (animator != null) {
			animator.tick();
			if (animator.shouldRemoved(player, parkourability)) animator = null;
		}
	}

	public boolean hasAnimator() {
		return animator != null;
	}

	public void removeAnimator() {
		animator = null;
	}

	@Override
	public void readFromNbt(NbtCompound tag) {
		
	}

	@Override
	public void writeToNbt(NbtCompound tag) {

	}
}
