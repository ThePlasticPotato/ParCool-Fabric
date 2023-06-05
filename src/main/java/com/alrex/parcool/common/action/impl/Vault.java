package com.alrex.parcool.common.action.impl;

import com.alrex.parcool.ParCoolConfig;
import com.alrex.parcool.client.animation.impl.KongVaultAnimator;
import com.alrex.parcool.client.animation.impl.SpeedVaultAnimator;
import com.alrex.parcool.client.input.KeyBindings;
import com.alrex.parcool.common.action.Action;
import com.alrex.parcool.common.action.StaminaConsumeTiming;
import com.alrex.parcool.common.capability.IStamina;
import com.alrex.parcool.common.capability.impl.Animation;
import com.alrex.parcool.common.capability.impl.Parkourability;
import com.alrex.parcool.utilities.BufferUtil;
import com.alrex.parcool.utilities.WorldUtil;
import net.fabricmc.api.Environment;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;

import java.nio.ByteBuffer;

import static net.fabricmc.api.EnvType.CLIENT;

public class Vault extends Action {
	public enum TypeSelectionMode {
		SpeedVault, KongVault, Dynamic
	}

	public enum AnimationType {
		SpeedVault((byte) 0), KongVault((byte) 1);
		private final byte code;

		AnimationType(byte code) {
			this.code = code;
		}

		public byte getCode() {
			return code;
		}

		@Nullable
		public static AnimationType fromCode(byte code) {
			switch (code) {
				case 0:
					return SpeedVault;
				case 1:
					return KongVault;
			}
			return null;
		}
	}

	//only in client
	private double stepHeight = 0;
	private Vec3d stepDirection = null;

	@Environment(CLIENT)
	@Override
	public boolean canStart(PlayerEntity player, Parkourability parkourability, IStamina stamina, ByteBuffer startInfo) {
		Vec3d lookVec = player.getRotationVector();
		lookVec = new Vec3d(lookVec.x, 0, lookVec.z).normalize();
		Vec3d step = WorldUtil.getVaultableStep(player);
		if (step == null) return false;
		step = step.normalize();
		//doing "vec/stepDirection" as complex number(x + z i) to calculate difference of player's direction to steps
		Vec3d dividedVec =
				new Vec3d(
						lookVec.x * step.x + lookVec.z * step.z, 0,
						-lookVec.x * step.z + lookVec.z * step.x
				).normalize();
		if (dividedVec.x < 0.707106) {
			return false;
		}
		AnimationType animationType = null;
		SpeedVaultAnimator.Type type = SpeedVaultAnimator.Type.Right;
		switch (ParCoolConfig.CONFIG_CLIENT.vaultAnimationMode.get()) {
			case KongVault:
				animationType = AnimationType.KongVault;
				break;
			case SpeedVault:
				animationType = AnimationType.SpeedVault;
				type = dividedVec.z > 0 ? SpeedVaultAnimator.Type.Right : SpeedVaultAnimator.Type.Left;
				break;
			default:
				if (dividedVec.x > 0.99) {
					animationType = AnimationType.KongVault;
				} else {
					animationType = AnimationType.SpeedVault;
					type = dividedVec.z > 0 ? SpeedVaultAnimator.Type.Right : SpeedVaultAnimator.Type.Left;
				}
				break;
		}
		double wallHeight = WorldUtil.getWallHeight(player);
		startInfo.put(animationType.getCode());
		BufferUtil.wrap(startInfo).putBoolean(type == SpeedVaultAnimator.Type.Right);
		startInfo
				.putDouble(step.x)
				.putDouble(step.y)
				.putDouble(step.z)
				.putDouble(wallHeight);

		return (parkourability.getActionInfo().can(Vault.class)
				&& !stamina.isExhausted()
				&& !(ParCoolConfig.CONFIG_CLIENT.vaultNeedKeyPressed.get() && !KeyBindings.getKeyVault().isPressed())
				&& parkourability.get(FastRun.class).canActWithRunning(player)
				&& !stamina.isExhausted()
				&& (player.isOnGround() || !ParCoolConfig.CONFIG_CLIENT.disableVaultInAir.get())
				&& wallHeight > player.getHeight() * 0.44 /*about 0.8*/
		);
	}

	@Override
	public boolean canContinue(PlayerEntity player, Parkourability parkourability, IStamina stamina) {
		return getDoingTick() < getVaultAnimateTime();
	}

	private int getVaultAnimateTime() {
		return 2;
	}

	@Environment(CLIENT)
	@Override
	public void onStartInLocalClient(PlayerEntity player, Parkourability parkourability, IStamina stamina, ByteBuffer startData) {
		AnimationType animationType = AnimationType.fromCode(startData.get());
		SpeedVaultAnimator.Type speedVaultType = BufferUtil.getBoolean(startData) ?
				SpeedVaultAnimator.Type.Right : SpeedVaultAnimator.Type.Left;
		stepDirection = new Vec3d(startData.getDouble(), startData.getDouble(), startData.getDouble());
		stepHeight = startData.getDouble();
		Animation animation = Animation.get(player);
		if (animation != null && animationType != null) {
			switch (animationType) {
				case SpeedVault:
					animation.setAnimator(new SpeedVaultAnimator(speedVaultType));
					break;
				case KongVault:
					animation.setAnimator(new KongVaultAnimator());
					break;
			}
		}
	}

	@Environment(CLIENT)
	@Override
	public void onStartInOtherClient(PlayerEntity player, Parkourability parkourability, ByteBuffer startData) {
		AnimationType animationType = AnimationType.fromCode(startData.get());
		SpeedVaultAnimator.Type speedVaultType = BufferUtil.getBoolean(startData) ?
				SpeedVaultAnimator.Type.Right : SpeedVaultAnimator.Type.Left;
		Animation animation = Animation.get(player);
		if (animation != null && animationType != null) {
			switch (animationType) {
				case SpeedVault:
					animation.setAnimator(new SpeedVaultAnimator(speedVaultType));
					break;
				case KongVault:
					animation.setAnimator(new KongVaultAnimator());
					break;
			}
		}
	}

	@Environment(CLIENT)
	@Override
	public void onWorkingTickInLocalClient(PlayerEntity player, Parkourability parkourability, IStamina stamina) {
		if (stepDirection == null) return;
		player.setVelocity(
				stepDirection.x / 10,
				((stepHeight + 0.02) / this.getVaultAnimateTime()) / (player.getHeight() / 1.8),
				stepDirection.z / 10
		);
	}

	@Override
	public StaminaConsumeTiming getStaminaConsumeTiming() {
		return StaminaConsumeTiming.OnStart;
	}

	@Environment(CLIENT)
	@Override
	public void onStopInLocalClient(PlayerEntity player) {
		stepDirection = stepDirection.normalize();
		player.setVelocity(
				stepDirection.x * 0.45,
				0.075 * (player.getHeight() / 1.8),
				stepDirection.z * 0.45
		);
	}
}
