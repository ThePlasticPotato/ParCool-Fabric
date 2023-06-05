package com.alrex.parcool.mixin.common;

import com.alrex.parcool.ParCoolConfig;
import net.minecraft.block.*;
import net.minecraft.client.RunArgs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {

	public LivingEntityMixin(EntityType<?> p_19870_, World p_19871_) {
		super(p_19870_, p_19871_);
	}

	@Inject(method = "isClimbing", at = @At("HEAD"), cancellable = true)
	public void onClimbable(CallbackInfoReturnable<Boolean> cir) {
		cir.cancel();
		LivingEntity entity = (LivingEntity) (Object) this;
		if (entity.isSpectator()) {
			cir.setReturnValue(false);
		} else {
			BlockPos blockpos = entity.getBlockPos();
			BlockState blockstate = this.getBlockStateAtPos();
			cir.setReturnValue(isLivingOnLadder(blockstate, entity.world, blockpos, entity));
		}
	}

	public boolean isLivingOnLadder(@NotNull BlockState state, @NotNull World world, @NotNull BlockPos pos, @NotNull LivingEntity entity) {
		boolean isSpectator = (entity instanceof PlayerEntity && entity.isSpectator());
		if (isSpectator) return false;

		Box bb = entity.getBoundingBox();
		int mX = MathHelper.floor(bb.minX);
		int mY = MathHelper.floor(bb.minY);
		int mZ = MathHelper.floor(bb.minZ);
		for (int y2 = mY; y2 < bb.maxY; y2++) {
			for (int x2 = mX; x2 < bb.maxX; x2++) {
				for (int z2 = mZ; z2 < bb.maxZ; z2++) {
					BlockPos tmp = new BlockPos(x2, y2, z2);
					if (!world.isChunkLoaded(pos)) {
						return false;
					}
					state = world.getBlockState(tmp);
					if (isLadder(state, world, tmp, entity)) {
						return true;
					}
				}
			}
		}
		return false;
	}

	private boolean isLadder(BlockState state, WorldAccess world, BlockPos pos, LivingEntity entity) {
		Block block = state.getBlock();
		if (block instanceof HorizontalConnectingBlock) {
			int zCount = 0;
			int xCount = 0;
			if (state.get(HorizontalConnectingBlock.NORTH)) zCount++;
			if (state.get(HorizontalConnectingBlock.SOUTH)) zCount++;
			if (state.get(HorizontalConnectingBlock.EAST)) xCount++;
			if (state.get(HorizontalConnectingBlock.WEST)) xCount++;
			return (zCount + xCount <= 1) || (zCount == 1 && xCount == 1);
		} else if (block instanceof PillarBlock) {
			return state.get(PillarBlock.AXIS).isVertical();
		} else if (block instanceof EndRodBlock) {
			Direction direction = state.get(FacingBlock.FACING);
			if (direction == Direction.UP || direction == Direction.DOWN) {
				return true;
			}
			return false;
		} else {
//			return block.isLadder(state, world, pos, entity);

			//todo forge sucks
			return state.getBlock() instanceof LadderBlock;
		}
	}
}
