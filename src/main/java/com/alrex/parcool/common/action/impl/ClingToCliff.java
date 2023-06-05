package com.alrex.parcool.common.action.impl;

import com.alrex.parcool.client.animation.impl.ClingToCliffAnimator;
import com.alrex.parcool.client.input.KeyBindings;
import com.alrex.parcool.common.action.Action;
import com.alrex.parcool.common.action.StaminaConsumeTiming;
import com.alrex.parcool.common.capability.IStamina;
import com.alrex.parcool.common.capability.impl.Animation;
import com.alrex.parcool.common.capability.impl.Parkourability;
import com.alrex.parcool.utilities.VectorUtil;
import com.alrex.parcool.utilities.WorldUtil;
import io.github.fabricators_of_create.porting_lib.event.client.RenderTickStartCallback;
import net.fabricmc.api.Environment;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;

import java.nio.ByteBuffer;

;import static net.fabricmc.api.EnvType.CLIENT;

public class ClingToCliff extends Action {
	private float armSwingAmount = 0;
	private FacingDirection facingDirection = FacingDirection.ToWall;
	@Nullable
	private Vec3d clingWallDirection = null;

	public float getArmSwingAmount() {
		return armSwingAmount;
	}

	@Override
	public void onWorkingTick(PlayerEntity player, Parkourability parkourability, IStamina stamina) {
		player.fallDistance = 0;
	}

	public FacingDirection getFacingDirection() {
		return facingDirection;
	}

	@Environment(CLIENT)
	@Override
	public boolean canStart(PlayerEntity player, Parkourability parkourability, IStamina stamina, ByteBuffer startInfo) {
		boolean value = (!stamina.isExhausted()
				&& player.getVelocity().y < 0.2
				&& parkourability.getActionInfo().can(ClingToCliff.class)
				&& KeyBindings.getKeyGrabWall().isPressed()
		);
		if (!value) return false;
		Vec3d wallVec = WorldUtil.getGrabbableWall(player);

		if (wallVec == null) return false;
		startInfo.putDouble(wallVec.x)
				.putDouble(wallVec.z);
		//Check whether player is facing to wall
		return 0.5 < wallVec.normalize().dotProduct(player.getRotationVector().multiply(1, 0, 1).normalize());
	}

	@Environment(CLIENT)
	@Override
	public boolean canContinue(PlayerEntity player, Parkourability parkourability, IStamina stamina) {
		return (!stamina.isExhausted()
				&& parkourability.getActionInfo().can(ClingToCliff.class)
				&& KeyBindings.getKeyGrabWall().isPressed()
				&& !parkourability.get(ClimbUp.class).isDoing()
				&& WorldUtil.getGrabbableWall(player) != null
		);
	}

	@Environment(CLIENT)
	@Override
	public void onStartInLocalClient(PlayerEntity player, Parkourability parkourability, IStamina stamina, ByteBuffer startData) {
		clingWallDirection = new Vec3d(startData.getDouble(), 0, startData.getDouble());
		facingDirection = FacingDirection.ToWall;
		armSwingAmount = 0;
		Animation animation = Animation.get(player);
		if (animation != null) animation.setAnimator(new ClingToCliffAnimator());
	}

	@Override
	public void onStartInOtherClient(PlayerEntity player, Parkourability parkourability, ByteBuffer startData) {
		clingWallDirection = new Vec3d(startData.getDouble(), 0, startData.getDouble());
		facingDirection = FacingDirection.ToWall;
		armSwingAmount = 0;
		Animation animation = Animation.get(player);
		if (animation != null) animation.setAnimator(new ClingToCliffAnimator());
	}

	@Environment(CLIENT)
	@Override
	public void onWorkingTickInLocalClient(PlayerEntity player, Parkourability parkourability, IStamina stamina) {
		armSwingAmount += player.getVelocity().multiply(1, 0, 1).lengthSquared();
		if (KeyBindings.getKeyLeft().isPressed() && KeyBindings.getKeyRight().isPressed()) {
			player.setVelocity(0, 0, 0);
		} else {
			if (clingWallDirection != null && facingDirection == FacingDirection.ToWall) {
				Vec3d vec = clingWallDirection.rotateY((float) (Math.PI / 2)).normalize().multiply(0.1);
				if (KeyBindings.getKeyLeft().isPressed()) player.setVelocity(vec);
				else if (KeyBindings.getKeyRight().isPressed()) player.setVelocity(vec.negate());
				else player.setVelocity(0, 0, 0);
			} else {
				player.setVelocity(0, 0, 0);
			}
		}
	}

	@Override
	public void onWorkingTickInClient(PlayerEntity player, Parkourability parkourability, IStamina stamina) {
		clingWallDirection = WorldUtil.getGrabbableWall(player);
		if (clingWallDirection == null) return;
		clingWallDirection = clingWallDirection.normalize();
		Vec3d lookingAngle = player.getRotationVector().multiply(1, 0, 1).normalize();
		Vec3d angle =
				new Vec3d(
						clingWallDirection.x * lookingAngle.x + clingWallDirection.z * lookingAngle.z, 0,
						-clingWallDirection.x * lookingAngle.z + clingWallDirection.z * lookingAngle.x
				).normalize();
		if (angle.x > 0.342) {
			facingDirection = FacingDirection.ToWall;
		} else if (angle.z < 0) {
			facingDirection = FacingDirection.RightAgainstWall;
		} else {
			facingDirection = FacingDirection.LeftAgainstWall;
		}
	}

	@Override
	public void saveSynchronizedState(ByteBuffer buffer) {
		buffer.putFloat(armSwingAmount);
	}

	@Override
	public void restoreSynchronizedState(ByteBuffer buffer) {
		armSwingAmount = buffer.getFloat();
	}

	@Environment(CLIENT)
	@Override
	public void onRenderTick(PlayerEntity player, Parkourability parkourability) {
		if (isDoing() && clingWallDirection != null) {
			switch (facingDirection) {
				case ToWall:
					player.setBodyYaw((float) VectorUtil.toYawDegree(clingWallDirection));
					break;
				case RightAgainstWall:
					player.setBodyYaw((float) VectorUtil.toYawDegree(clingWallDirection.rotateY((float) (-Math.PI / 2))));
					break;
				case LeftAgainstWall:
					player.setBodyYaw((float) VectorUtil.toYawDegree(clingWallDirection.rotateY((float) (Math.PI / 2))));
			}
		}
	}

	@Override
	public StaminaConsumeTiming getStaminaConsumeTiming() {
		return StaminaConsumeTiming.OnWorking;
	}

	public enum FacingDirection {
		ToWall, RightAgainstWall, LeftAgainstWall
	}
}
