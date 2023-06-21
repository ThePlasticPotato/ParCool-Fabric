package com.alrex.parcool.common.capability.impl;

import com.alrex.parcool.ParCoolConfig;
import com.alrex.parcool.client.animation.Animator;
import com.alrex.parcool.client.animation.PassiveCustomAnimation;
import com.alrex.parcool.client.animation.PlayerModelRotator;
import com.alrex.parcool.client.animation.PlayerModelTransformer;
import com.alrex.parcool.common.capability.capabilities.Capabilities;
import dev.onyxstudios.cca.api.v3.component.Component;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import io.github.fabricators_of_create.porting_lib.util.LazyOptional;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;

import java.util.ArrayList;
import java.util.List;

import static net.fabricmc.api.EnvType.CLIENT;

@Environment(CLIENT)
public class Animation implements Component, AutoSyncedComponent {
	public static Animation get(PlayerEntity player) {
		LazyOptional<Animation> optional = LazyOptional.of(() ->player.getComponent(Capabilities.ANIMATION_CAPABILITY));
		if (!optional.isPresent()) return null;
		return optional.orElseThrow(IllegalStateException::new);
	}

	//private Animator animator = null;

	private ArrayList<Animator> animators = new ArrayList<>();

	private final PassiveCustomAnimation passiveAnimation = new PassiveCustomAnimation();

//	public void setAnimator(Animator animator) {
//		if (ParCoolConfig.CONFIG_CLIENT.disableAnimation.get()) return;
//		ParCoolConfig.Client config = ParCoolConfig.CONFIG_CLIENT;
//		if (!config.canAnimate(animator.getClass()).get()) return;
//		this.animators = animator;
//	}
	public void addAnimator(Animator animator) {
		if (ParCoolConfig.CONFIG_CLIENT.disableAnimation.get()) return;
		ParCoolConfig.Client config = ParCoolConfig.CONFIG_CLIENT;
		if (!config.canAnimate(animator.getClass()).get()) return;
		animators.add(animator);
	}

	public ArrayList<Animator> getActiveAnimators() {
		return animators;
	}

	public void removeAnimator(Animator animator) {
		if (ParCoolConfig.CONFIG_CLIENT.disableAnimation.get()) return;
		ParCoolConfig.Client config = ParCoolConfig.CONFIG_CLIENT;
		if (!config.canAnimate(animator.getClass()).get()) return;
		animators.remove(animator);
	}

	public boolean animatePre(PlayerEntity player, PlayerModelTransformer modelTransformer) {
		if (animators == null || animators.isEmpty()) return false;
		Parkourability parkourability = Parkourability.get(player);
		ArrayList<Animator> animsSafe = new ArrayList<>(animators);
		return animsSafe.get(0).animatePre(player, parkourability, modelTransformer);
	}

	public void animatePost(PlayerEntity player, PlayerModelTransformer modelTransformer) {
		Parkourability parkourability = Parkourability.get(player);
		if (parkourability == null) return;
		if (animators == null || animators.isEmpty()) {
			passiveAnimation.animate(player, parkourability, modelTransformer);
			return;
		}
		ArrayList<Animator> animsSafe = new ArrayList<>(animators);
		for (Animator anim : animsSafe) {
			anim.animatePost(player, parkourability, modelTransformer);
		}
	}

	public void applyRotate(AbstractClientPlayerEntity player, PlayerModelRotator rotator) {
		Parkourability parkourability = Parkourability.get(player);
		if (parkourability == null) return;
		if (animators == null || animators.isEmpty()) {
			passiveAnimation.rotate(player, parkourability, rotator);
			return;
		}
		ArrayList<Animator> animsSafe = new ArrayList<>(animators);
		for (Animator anim : animsSafe) {
			anim.rotate(player, parkourability, rotator);
		}
	}

	public void cameraSetup(PlayerEntity player, Parkourability parkourability) {
		if (animators == null || animators.isEmpty()) return;
		if (player.isMainPlayer()
				&& MinecraftClient.getInstance().options.getPerspective().isFirstPerson()
				&& ParCoolConfig.CONFIG_CLIENT.disableFPVAnimation.get()
		) return;
		ArrayList<Animator> animsSafe = new ArrayList<>(animators);
		for (Animator anim : animsSafe) {
			anim.onCameraSetUp(player, parkourability);
		}
	}
	@Environment(CLIENT)
	public void tick(PlayerEntity player, Parkourability parkourability) {
		passiveAnimation.tick(player, parkourability);
		if (animators != null && !animators.isEmpty()) {
			ArrayList<Animator> animsSafe = new ArrayList<>(animators);
			for (Animator anim : animsSafe) {
				anim.tick();
				if (anim.shouldRemoved(player, parkourability)) removeAnimator(anim);
			}
		}
	}

	public boolean hasAnimator() {
		return (animators != null && !animators.isEmpty());
	}

	public void voidAnimator() {
		animators = new ArrayList<>();
	}

	@Override
	public void readFromNbt(NbtCompound tag) {
		
	}

	@Override
	public void writeToNbt(NbtCompound tag) {

	}
}
