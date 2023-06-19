package com.alrex.parcool.mixin.client;

import com.alrex.parcool.ParCoolConfig;
import com.alrex.parcool.client.animation.PlayerModelTransformer;
import com.alrex.parcool.common.capability.impl.Animation;
<<<<<<< Updated upstream
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
=======
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
>>>>>>> Stashed changes
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Function;

<<<<<<< Updated upstream
@Mixin(PlayerModel.class)
public abstract class PlayerModelMixin<T extends LivingEntity> extends HumanoidModel<T> {
=======
@Mixin(PlayerEntityModel.class)
public abstract class PlayerModelMixin<T extends LivingEntity> extends BipedEntityModel<T> {
>>>>>>> Stashed changes
	@Shadow
	@Final
	private boolean thinArms;
	@Shadow
	@Final
	public ModelPart jacket;
	@Shadow
	@Final
	public ModelPart rightPants;
	@Shadow
	@Final
	public ModelPart rightSleeve;
	@Shadow
	@Final
	public ModelPart leftPants;
	@Shadow
	@Final
	public ModelPart leftSleeve;

	@Shadow
	@Final
	private ModelPart ear;
	private PlayerModelTransformer transformer = null;

	public PlayerModelMixin(ModelPart p_170677_) {
		super(p_170677_);
	}

<<<<<<< Updated upstream
	public PlayerModelMixin(ModelPart p_170679_, Function<ResourceLocation, RenderType> p_170680_) {
		super(p_170679_, p_170680_);
	}

	@Inject(method = "Lnet/minecraft/client/model/PlayerModel;setupAnim(Lnet/minecraft/world/entity/LivingEntity;FFFFF)V", at = @At("HEAD"), cancellable = true)
	protected void onSetupAnimHead(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, CallbackInfo info) {
		if (!(entity instanceof Player)) return;
		PlayerModel model = (PlayerModel) (Object) this;
		Player player = (Player) entity;
		if (player.isLocalPlayer()
				&& Minecraft.getInstance().options.getCameraType().isFirstPerson()
=======
	public PlayerModelMixin(ModelPart p_170679_, Function<Identifier, RenderLayer> p_170680_) {
		super(p_170679_, p_170680_);
	}

	@Inject(method = "setAngles(Lnet/minecraft/entity/LivingEntity;FFFFF)V", at = @At("HEAD"), cancellable = true)
	protected void onSetupAnimHead(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, CallbackInfo info) {
		if (!(entity instanceof PlayerEntity)) return;
		PlayerEntityModel<PlayerEntity> model = (PlayerEntityModel<PlayerEntity>) (Object) this;
		PlayerEntity player = (PlayerEntity) entity;
		if (player.isMainPlayer()
				&& MinecraftClient.getInstance().options.getPerspective().isFirstPerson()
>>>>>>> Stashed changes
				&& ParCoolConfig.CONFIG_CLIENT.disableFPVAnimation.get()
		) return;

		transformer = new PlayerModelTransformer(
				player,
				model,
				thinArms,
				ageInTicks,
				limbSwing,
				limbSwingAmount,
				netHeadYaw,
				headPitch
		);
		transformer.reset();

		Animation animation = Animation.get(player);
		if (animation == null) return;

		boolean shouldCancel = animation.animatePre(player, transformer);
		transformer.copyFromBodyToWear();
		if (shouldCancel) {
			transformer = null;
			info.cancel();
		}
	}

<<<<<<< Updated upstream
	@Inject(method = "Lnet/minecraft/client/model/PlayerModel;setupAnim(Lnet/minecraft/world/entity/LivingEntity;FFFFF)V", at = @At("TAIL"))
	protected void onSetupAnimTail(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, CallbackInfo info) {
		if (!(entity instanceof Player)) return;
		Player player = (Player) entity;
		if (player.isLocalPlayer()
				&& Minecraft.getInstance().options.getCameraType().isFirstPerson()
=======
	@Inject(method = "setAngles(Lnet/minecraft/entity/LivingEntity;FFFFF)V", at = @At("TAIL"))
	protected void onSetupAnimTail(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, CallbackInfo info) {
		if (!(entity instanceof PlayerEntity)) return;
		PlayerEntity player = (PlayerEntity) entity;
		if (player.isMainPlayer()
				&& MinecraftClient.getInstance().options.getPerspective().isFirstPerson()
>>>>>>> Stashed changes
				&& ParCoolConfig.CONFIG_CLIENT.disableFPVAnimation.get()
		) return;

		Animation animation = Animation.get(player);
		if (animation == null) {
			transformer = null;
			return;
		}

		if (transformer != null) {
			animation.animatePost(player, transformer);
			transformer.copyFromBodyToWear();
			transformer = null;
		}
	}

}
