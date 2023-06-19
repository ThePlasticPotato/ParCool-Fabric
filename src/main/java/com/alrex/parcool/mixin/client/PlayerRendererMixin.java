package com.alrex.parcool.mixin.client;

import com.alrex.parcool.client.animation.PlayerModelRotator;
import com.alrex.parcool.common.capability.impl.Animation;
<<<<<<< Updated upstream
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
=======
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
>>>>>>> Stashed changes
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

<<<<<<< Updated upstream
@Mixin(PlayerRenderer.class)
public abstract class PlayerRendererMixin extends LivingEntityRenderer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {
	public PlayerRendererMixin(EntityRendererProvider.Context p_174289_, PlayerModel<AbstractClientPlayer> p_174290_, float p_174291_) {
		super(p_174289_, p_174290_, p_174291_);
	}

	@Inject(method = "Lnet/minecraft/client/renderer/entity/player/PlayerRenderer;setupRotations(Lnet/minecraft/client/player/AbstractClientPlayer;Lcom/mojang/blaze3d/vertex/PoseStack;FFF)V", at = @At("HEAD"))
	protected void onSetupRotations(AbstractClientPlayer player, PoseStack stack, float xRot, float yRot, float zRot, CallbackInfo ci) {
=======
@Mixin(PlayerEntityRenderer.class)
public abstract class PlayerRendererMixin extends LivingEntityRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> {
	public PlayerRendererMixin(EntityRendererFactory.Context p_174289_, PlayerEntityModel<AbstractClientPlayerEntity> p_174290_, float p_174291_) {
		super(p_174289_, p_174290_, p_174291_);
	}

	@Inject(method = "setupTransforms(Lnet/minecraft/client/network/AbstractClientPlayerEntity;Lnet/minecraft/client/util/math/MatrixStack;FFF)V", at = @At("HEAD"))
	protected void onSetupRotations(AbstractClientPlayerEntity player, MatrixStack stack, float xRot, float yRot, float zRot, CallbackInfo ci) {
>>>>>>> Stashed changes
		// arg names may be incorrect
		Animation animation = Animation.get(player);
		if (animation == null) {
			return;
		}
		PlayerModelRotator rotator = new PlayerModelRotator(stack, player, MinecraftClient.getInstance().getTickDelta());
		animation.applyRotate(player, rotator);
	}
}
