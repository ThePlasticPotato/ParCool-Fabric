package com.alrex.parcool.utilities;

import com.alrex.parcool.common.action.impl.HangDown;
import net.minecraft.block.*;
import net.minecraft.block.enums.WallShape;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class WorldUtil {

	public static Vec3d getRunnableWall(LivingEntity entity, double range) {
		double width = entity.getWidth() / 2;
		double wallX = 0;
		double wallZ = 0;
		Vec3d pos = entity.getPos();

		Box baseBox = new Box(
				pos.x - width,
				pos.y,
				pos.z - width,
				pos.x + width,
				pos.y + entity.getWidth(),
				pos.z + width
		);

		if (!entity.world.isSpaceEmpty(baseBox.stretch(range, 0, 0))) {
			wallX++;
		}
		if (!entity.world.isSpaceEmpty(baseBox.stretch(-range, 0, 0))) {
			wallX--;
		}
		if (!entity.world.isSpaceEmpty(baseBox.stretch(0, 0, range))) {
			wallZ++;
		}
		if (!entity.world.isSpaceEmpty(baseBox.stretch(0, 0, -range))) {
			wallZ--;
		}
		if (wallX == 0 && wallZ == 0) return null;

		return new Vec3d(wallX, 0, wallZ);
	}

	@Nullable
	public static Vec3d getWall(LivingEntity entity) {
		double range = entity.getWidth() / 2;
		final double width = entity.getWidth() * 0.5;
		double wallX = 0;
		double wallZ = 0;
		Vec3d pos = entity.getPos();

		Box baseBox = new Box(
				pos.x - width,
				pos.y,
				pos.z - width,
				pos.x + width,
				pos.y + entity.getHeight(),
				pos.z + width
		);

		if (!entity.world.isSpaceEmpty(baseBox.stretch(range, 0, 0))) {
			wallX++;
		}
		if (!entity.world.isSpaceEmpty(baseBox.stretch(-range, 0, 0))) {
			wallX--;
		}
		if (!entity.world.isSpaceEmpty(baseBox.stretch(0, 0, range))) {
			wallZ++;
		}
		if (!entity.world.isSpaceEmpty(baseBox.stretch(0, 0, -range))) {
			wallZ--;
		}
		if (wallX == 0 && wallZ == 0) return null;

		return new Vec3d(wallX, 0, wallZ);
	}

	@Nullable
	public static Vec3d getVaultableStep(LivingEntity entity) {
		final double d = entity.getWidth() * 0.5;
		World world = entity.world;
		double distance = entity.getWidth() / 2;
		double baseLine = Math.min(entity.getHeight() * 0.86, getWallHeight(entity));
		double stepX = 0;
		double stepZ = 0;
		Vec3d pos = entity.getPos();

		Box baseBoxBottom = new Box(
				pos.x - d,
				pos.y,
				pos.z - d,
				pos.x + d,
				pos.y + baseLine,
				pos.z + d
		);
		Box baseBoxTop = new Box(
				pos.x - d,
				pos.y + baseLine,
				pos.z - d,
				pos.x + d,
				pos.y + baseLine + entity.getHeight(),
				pos.z + d
		);
		if (!world.isSpaceEmpty(baseBoxBottom.stretch(distance, 0, 0)) && world.isSpaceEmpty(baseBoxTop.stretch((distance + 1.8), 0, 0))) {
			stepX++;
		}
		if (!world.isSpaceEmpty(baseBoxBottom.stretch(-distance, 0, 0)) && world.isSpaceEmpty(baseBoxTop.stretch(-(distance + 1.8), 0, 0))) {
			stepX--;
		}
		if (!world.isSpaceEmpty(baseBoxBottom.stretch(0, 0, distance)) && world.isSpaceEmpty(baseBoxTop.stretch(0, 0, (distance + 1.8)))) {
			stepZ++;
		}
		if (!world.isSpaceEmpty(baseBoxBottom.stretch(0, 0, -distance)) && world.isSpaceEmpty(baseBoxTop.stretch(0, 0, -(distance + 1.8)))) {
			stepZ--;
		}
		if (stepX == 0 && stepZ == 0) return null;

		return new Vec3d(stepX, 0, stepZ);
	}

	public static double getWallHeight(LivingEntity entity) {
		Vec3d wall = getWall(entity);
		if (wall == null) return 0;
		World world = entity.world;
		final double accuracy = entity.getHeight() / 18; // normally about 0.1
		final double d = entity.getWidth() * 0.5;
		int loopNum = (int) Math.round(entity.getHeight() / accuracy);
		Vec3d pos = entity.getPos();
		double x1 = pos.x + d + (wall.x > 0 ? 1 : 0);
		double y1 = pos.y;
		double z1 = pos.z + d + (wall.z > 0 ? 1 : 0);
		double x2 = pos.x - d + (wall.x < 0 ? -1 : 0);
		double z2 = pos.z - d + (wall.z < 0 ? -1 : 0);
		boolean canReturn = false;
		for (int i = 0; i < loopNum; i++) {
			Box box = new Box(
					x1, y1 + accuracy * i, z1, x2, y1 + accuracy * (i + 1), z2
			);

			if (!world.isSpaceEmpty(box)) {
				canReturn = true;
			} else {
				if (canReturn) return accuracy * i;
			}
		}
		return entity.getHeight();
	}

	@Nullable
	public static HangDown.BarAxis getHangableBars(LivingEntity entity) {
		final double bbWidth = entity.getWidth() / 4;
		final double bbHeight = 0.35;
		Box bb = new Box(
				entity.getX() - bbWidth,
				entity.getY() + entity.getHeight(),
				entity.getZ() - bbWidth,
				entity.getX() + bbWidth,
				entity.getY() + entity.getHeight() + bbHeight,
				entity.getZ() + bbWidth
		);
		if (entity.world.isSpaceEmpty(bb)) return null;
		BlockPos pos = new BlockPos(
				entity.getX(),
				entity.getY() + entity.getHeight() + 0.4,
				entity.getZ()
		);
		if (!entity.world.isChunkLoaded(pos)) return null;
		BlockState state = entity.world.getBlockState(pos);
		Block block = state.getBlock();
		HangDown.BarAxis axis = null;
		if (block instanceof PillarBlock) {
			Direction.Axis pillarAxis = state.get(PillarBlock.AXIS);
			if (state.isFullCube(entity.world, pos)) {
				return null;
			}
			switch (pillarAxis) {
				case X:
					axis = HangDown.BarAxis.X;
					break;
				case Z:
					axis = HangDown.BarAxis.Z;
					break;
			}
		} else if (block instanceof EndRodBlock) {
			Direction direction = state.get(FacingBlock.FACING);
			switch (direction) {
				case EAST:
				case WEST:
					axis = HangDown.BarAxis.X;
					break;
				case NORTH:
				case SOUTH:
					axis = HangDown.BarAxis.Z;
			}
		} else if (block instanceof HorizontalConnectingBlock) {
			int zCount = 0;
			int xCount = 0;
			if (state.get(HorizontalConnectingBlock.NORTH)) zCount++;
			if (state.get(HorizontalConnectingBlock.SOUTH)) zCount++;
			if (state.get(HorizontalConnectingBlock.EAST)) xCount++;
			if (state.get(HorizontalConnectingBlock.WEST)) xCount++;
			if (zCount > 0 && xCount == 0) axis = HangDown.BarAxis.Z;
			if (xCount > 0 && zCount == 0) axis = HangDown.BarAxis.X;
		} else if (block instanceof WallBlock) {
			int zCount = 0;
			int xCount = 0;
			if (state.get(WallBlock.NORTH_SHAPE) != WallShape.NONE) zCount++;
			if (state.get(WallBlock.SOUTH_SHAPE) != WallShape.NONE) zCount++;
			if (state.get(WallBlock.EAST_SHAPE) != WallShape.NONE) xCount++;
			if (state.get(WallBlock.WEST_SHAPE) != WallShape.NONE) xCount++;
			if (zCount > 0 && xCount == 0) axis = HangDown.BarAxis.Z;
			if (xCount > 0 && zCount == 0) axis = HangDown.BarAxis.X;
		}

		return axis;
	}

	public static boolean existsDivableSpace(LivingEntity entity) {
		World world = entity.world;
		Vec3d lookAngle = entity.getRotationVector();
		Vec3d center = entity.getPos().add(new Vec3d(lookAngle.x, 0, lookAngle.z).normalize().multiply(3, 0, 3));
		if (!world.isChunkLoaded(new BlockPos(center))) {
			return false;
		}
		BlockPos centerPos = new BlockPos(center);
		final int neededSpaceHeight = 9;
		boolean hasSpace = true;
		for (int i = 0; i < neededSpaceHeight; i++) {
			hasSpace = !world.getBlockState(centerPos).getMaterial().blocksMovement();
			hasSpace = hasSpace && world.isChunkLoaded(centerPos.west()) && !world.getBlockState(centerPos.west()).getMaterial().blocksMovement();
			hasSpace = hasSpace && world.isChunkLoaded(centerPos.east()) && !world.getBlockState(centerPos.east()).getMaterial().blocksMovement();
			hasSpace = hasSpace && world.isChunkLoaded(centerPos.north()) && !world.getBlockState(centerPos.north()).getMaterial().blocksMovement();
			hasSpace = hasSpace && world.isChunkLoaded(centerPos.south()) && !world.getBlockState(centerPos.south()).getMaterial().blocksMovement();
			if (!hasSpace) break;
			centerPos = centerPos.down();
		}
		if (!hasSpace) return false;
		center = entity.getPos().add(new Vec3d(lookAngle.x, 0, lookAngle.z).normalize().multiply(5, 0, 5));
		centerPos = new BlockPos(center);
		for (int i = 0; i < neededSpaceHeight; i++) {
			hasSpace = !world.getBlockState(centerPos).getMaterial().blocksMovement();
			hasSpace = hasSpace && world.isChunkLoaded(centerPos.west()) && !world.getBlockState(centerPos.west()).getMaterial().blocksMovement();
			hasSpace = hasSpace && world.isChunkLoaded(centerPos.east()) && !world.getBlockState(centerPos.east()).getMaterial().blocksMovement();
			hasSpace = hasSpace && world.isChunkLoaded(centerPos.north()) && !world.getBlockState(centerPos.north()).getMaterial().blocksMovement();
			hasSpace = hasSpace && world.isChunkLoaded(centerPos.south()) && !world.getBlockState(centerPos.south()).getMaterial().blocksMovement();
			if (!hasSpace) break;
			centerPos = centerPos.down();
		}
		return hasSpace;
	}

	@Nullable
	public static Vec3d getGrabbableWall(LivingEntity entity) {
		final double d = entity.getWidth() * 0.5;
		World world = entity.world;
		double distance = entity.getWidth() / 2;
		double baseLine1 = entity.getStandingEyeHeight() + (entity.getHeight() - entity.getStandingEyeHeight()) / 2;
		double baseLine2 = entity.getHeight() + (entity.getHeight() - entity.getStandingEyeHeight()) / 2;
		Vec3d wall1 = getGrabbableWall(entity, distance, baseLine1);
		if (wall1 != null) return wall1;
		return getGrabbableWall(entity, distance, baseLine2);
	}

	@Nullable
	private static Vec3d getGrabbableWall(LivingEntity entity, double distance, double baseLine) {
		final double d = entity.getWidth() * 0.49;
		World world = entity.world;
		Vec3d pos = entity.getPos();
		Box baseBoxSide = new Box(
				pos.x - d,
				pos.y + baseLine - entity.getHeight() / 6,
				pos.z - d,
				pos.x + d,
				pos.y + baseLine,
				pos.z + d
		);
		Box baseBoxTop = new Box(
				pos.x - d,
				pos.y + baseLine,
				pos.z - d,
				pos.x + d,
				pos.y + entity.getHeight(),
				pos.z + d
		);
		int xDirection = 0;
		int zDirection = 0;

		if (!world.isSpaceEmpty(baseBoxSide.stretch(distance, 0, 0)) && world.isSpaceEmpty(baseBoxTop.stretch(distance, 0, 0)))
			xDirection++;
		if (!world.isSpaceEmpty(baseBoxSide.stretch(-distance, 0, 0)) && world.isSpaceEmpty(baseBoxTop.stretch(-distance, 0, 0)))
			xDirection--;
		if (!world.isSpaceEmpty(baseBoxSide.stretch(0, 0, distance)) && world.isSpaceEmpty(baseBoxTop.stretch(0, 0, distance)))
			zDirection++;
		if (!world.isSpaceEmpty(baseBoxSide.stretch(0, 0, -distance)) && world.isSpaceEmpty(baseBoxTop.stretch(0, 0, -distance)))
			zDirection--;
		if (xDirection == 0 && zDirection == 0) {
			return null;
		}
		float slipperiness;
		if (xDirection != 0 && zDirection != 0) {
			BlockPos blockPos1 = new BlockPos(
					entity.getX() + xDirection,
					entity.getBoundingBox().minY + baseLine - 0.3,
					entity.getZ()
			);
			BlockPos blockPos2 = new BlockPos(
					entity.getX(),
					entity.getBoundingBox().minY + baseLine - 0.3,
					entity.getZ() + zDirection
			);
			if (!entity.world.isChunkLoaded(blockPos1)) return null;
			if (!entity.world.isChunkLoaded(blockPos2)) return null;
			slipperiness = Math.min(
					entity.world.getBlockState(blockPos1).getBlock().getSlipperiness(),
					entity.world.getBlockState(blockPos2).getBlock().getSlipperiness()
			);
		} else {
			BlockPos blockPos = new BlockPos(
					entity.getX() + xDirection,
					entity.getBoundingBox().minY + baseLine - 0.3,
					entity.getZ() + zDirection
			);
			if (!entity.world.isChunkLoaded(blockPos)) return null;
			slipperiness = entity.world.getBlockState(blockPos).getBlock().getSlipperiness();
		}
		return slipperiness <= 0.9 ? new Vec3d(xDirection, 0, zDirection) : null;
	}
}
