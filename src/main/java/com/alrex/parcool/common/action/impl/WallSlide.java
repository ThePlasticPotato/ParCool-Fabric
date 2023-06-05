package com.alrex.parcool.common.action.impl;

import com.alrex.parcool.client.animation.impl.WallSlideAnimator;
import com.alrex.parcool.client.input.KeyBindings;
import com.alrex.parcool.common.action.Action;
import com.alrex.parcool.common.action.StaminaConsumeTiming;
import com.alrex.parcool.common.capability.IStamina;
import com.alrex.parcool.common.capability.impl.Animation;
import com.alrex.parcool.common.capability.impl.Parkourability;
import com.alrex.parcool.utilities.WorldUtil;
import net.fabricmc.api.Environment;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;

import java.nio.ByteBuffer;

;import static net.fabricmc.api.EnvType.CLIENT;

public class WallSlide extends Action {
	private Vec3d leanedWallDirection = null;

	@Nullable
	public Vec3d getLeanedWallDirection() {
		return leanedWallDirection;
	}

	@Environment(CLIENT)
	@Override
	public boolean canStart(PlayerEntity player, Parkourability parkourability, IStamina stamina, ByteBuffer startInfo) {
		return canContinue(player, parkourability, stamina);
	}

	@Override
	public boolean canContinue(PlayerEntity player, Parkourability parkourability, IStamina stamina) {
		Vec3d wall = WorldUtil.getWall(player);
		return (wall != null
				&& !player.isOnGround()
				&& parkourability.getActionInfo().can(WallSlide.class)
				&& !parkourability.get(FastRun.class).isDoing()
				&& !parkourability.get(Dodge.class).isDoing()
				&& !player.getAbilities().flying
				&& player.getVelocity().y <= 0
				&& KeyBindings.getKeyWallSlide().isPressed()
				&& !stamina.isExhausted()
				&& !parkourability.get(ClingToCliff.class).isDoing()
				&& parkourability.get(ClingToCliff.class).getNotDoingTick() > 12
		);
	}

	@Environment(CLIENT)
	@Override
	public void onWorkingTickInClient(PlayerEntity player, Parkourability parkourability, IStamina stamina) {
		Animation animation = Animation.get(player);
		if (animation != null && !animation.hasAnimator()) {
			animation.setAnimator(new WallSlideAnimator());
		}
	}

	@Override
	public StaminaConsumeTiming getStaminaConsumeTiming() {
		return StaminaConsumeTiming.OnWorking;
	}

	@Override
	public void onWorkingTick(PlayerEntity player, Parkourability parkourability, IStamina stamina) {
		leanedWallDirection = WorldUtil.getWall(player);
		if (leanedWallDirection != null) {
			BlockPos leanedBlock = new BlockPos(
					player.getX() + leanedWallDirection.x,
					player.getBoundingBox().minY + player.getHeight() * 0.75,
					player.getZ() + leanedWallDirection.z
			);
			if (!player.world.isChunkLoaded(leanedBlock)) return;
			float slipperiness = player.world.getBlockState(leanedBlock).getBlock().getSlipperiness();
			slipperiness = (float) Math.sqrt(slipperiness);
			player.fallDistance *= slipperiness;
			player.setVelocity(player.getVelocity().multiply(0.8, slipperiness, 0.8));
		}
	}
}
