package com.alrex.parcool.mixin.common;

import com.alrex.parcool.common.action.impl.ClingToCliff;
import com.alrex.parcool.common.capability.impl.Parkourability;
<<<<<<< Updated upstream
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
=======
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
>>>>>>> Stashed changes
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

;

<<<<<<< Updated upstream
@Mixin(Player.class)
public abstract class PlayerMixin extends LivingEntity {

	protected PlayerMixin(EntityType<? extends LivingEntity> p_20966_, Level p_20967_) {
		super(p_20966_, p_20967_);
	}

	@Inject(method = "Lnet/minecraft/world/entity/player/Player;tryToStartFallFlying()Z", at = @At("HEAD"), cancellable = true)
	public void onTryToStartFallFlying(CallbackInfoReturnable<Boolean> cir) {
		Player player = (Player) (Object) this;
=======
@Mixin(PlayerEntity.class)
public abstract class PlayerMixin extends LivingEntity {

//	@Unique
//	private boolean isExhausted = false;
//
//	@Unique
//	private int Stamina = 0;


	protected PlayerMixin(EntityType<? extends LivingEntity> p_20966_, World p_20967_) {
		super(p_20966_, p_20967_);
	}

	@Inject(method = "checkFallFlying", at = @At("HEAD"), cancellable = true)
	private void onTryToStartFallFlying(CallbackInfoReturnable<Boolean> cir) {
		PlayerEntity player = (PlayerEntity) (Object) this;
>>>>>>> Stashed changes
		Parkourability parkourability = Parkourability.get(player);
		if (parkourability != null && parkourability.get(ClingToCliff.class).isDoing()) {
			cir.setReturnValue(false);
			cir.cancel();
		}
	}
<<<<<<< Updated upstream
=======

//	@Inject(method = "tick", at = @At("HEAD"))
//	private void staminaTick(CallbackInfo ci) {
//
//	}

>>>>>>> Stashed changes
}
