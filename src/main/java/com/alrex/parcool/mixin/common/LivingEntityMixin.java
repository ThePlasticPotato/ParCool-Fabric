package com.alrex.parcool.mixin.common;

<<<<<<< Updated upstream
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.ForgeConfig;
=======
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
>>>>>>> Stashed changes
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;



@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {

<<<<<<< Updated upstream
	public LivingEntityMixin(EntityType<?> p_19870_, Level p_19871_) {
		super(p_19870_, p_19871_);
	}

	@Inject(method = "net.minecraft.world.entity.LivingEntity.onClimbable", at = @At("HEAD"), cancellable = true)
=======
	public LivingEntityMixin(EntityType<?> p_19870_, World p_19871_) {
		super(p_19870_, p_19871_);
	}

	@Inject(method = "isClimbing", at = @At("HEAD"), cancellable = true)
>>>>>>> Stashed changes
	public void onClimbable(CallbackInfoReturnable<Boolean> cir) {
		cir.cancel();
		LivingEntity entity = (LivingEntity) (Object) this;
		if (entity.isSpectator()) {
			cir.setReturnValue(false);
		} else {
<<<<<<< Updated upstream
			BlockPos blockpos = entity.blockPosition();
			BlockState blockstate = this.getFeetBlockState();
			cir.setReturnValue(isLivingOnLadder(blockstate, entity.level, blockpos, entity));
		}
	}

	public boolean isLivingOnLadder(@Nonnull BlockState state, @Nonnull Level world, @Nonnull BlockPos pos, @Nonnull LivingEntity entity) {
		boolean isSpectator = (entity instanceof Player && entity.isSpectator());
		if (isSpectator) return false;
		if (!ForgeConfig.SERVER.fullBoundingBoxLadders.get()) {
			return isLadder(state, world, pos, entity);
		} else {
			AABB bb = entity.getBoundingBox();
			int mX = Mth.floor(bb.minX);
			int mY = Mth.floor(bb.minY);
			int mZ = Mth.floor(bb.minZ);
			for (int y2 = mY; y2 < bb.maxY; y2++) {
				for (int x2 = mX; x2 < bb.maxX; x2++) {
					for (int z2 = mZ; z2 < bb.maxZ; z2++) {
						BlockPos tmp = new BlockPos(x2, y2, z2);
						if (!world.isLoaded(pos)) {
							return false;
						}
						state = world.getBlockState(tmp);
						if (isLadder(state, world, tmp, entity)) {
							return true;
						}
=======
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
>>>>>>> Stashed changes
					}
				}
			}
		}
		return false;
	}

<<<<<<< Updated upstream
	private boolean isLadder(BlockState state, LevelAccessor world, BlockPos pos, LivingEntity entity) {
		Block block = state.getBlock();
		if (block instanceof CrossCollisionBlock) {
			int zCount = 0;
			int xCount = 0;
			if (state.getValue(CrossCollisionBlock.NORTH)) zCount++;
			if (state.getValue(CrossCollisionBlock.SOUTH)) zCount++;
			if (state.getValue(CrossCollisionBlock.EAST)) xCount++;
			if (state.getValue(CrossCollisionBlock.WEST)) xCount++;
=======
	private boolean isLadder(BlockState state, WorldAccess world, BlockPos pos, LivingEntity entity) {
		Block block = state.getBlock();
		if (block instanceof HorizontalConnectingBlock) {
			int zCount = 0;
			int xCount = 0;
			if (state.get(HorizontalConnectingBlock.NORTH)) zCount++;
			if (state.get(HorizontalConnectingBlock.SOUTH)) zCount++;
			if (state.get(HorizontalConnectingBlock.EAST)) xCount++;
			if (state.get(HorizontalConnectingBlock.WEST)) xCount++;
>>>>>>> Stashed changes
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
<<<<<<< Updated upstream
			return block.isLadder(state, world, pos, entity);
=======
//			return block.isLadder(state, world, pos, entity);

			//todo forge sucks
			return state.getBlock() instanceof LadderBlock;
>>>>>>> Stashed changes
		}
	}
}
