package com.alrex.parcool.common.action.impl;

import com.alrex.parcool.ParCoolConfig;
import com.alrex.parcool.client.animation.impl.SpindashAnimator;
import com.alrex.parcool.client.input.KeyRecorder;
import com.alrex.parcool.common.action.Action;
import com.alrex.parcool.common.action.StaminaConsumeTiming;
import com.alrex.parcool.common.capability.IStamina;
import com.alrex.parcool.common.capability.impl.Animation;
import com.alrex.parcool.common.capability.impl.Parkourability;
import com.alrex.parcool.utilities.EntityUtil;
import net.fabricmc.api.Environment;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;

import java.nio.ByteBuffer;

import static net.fabricmc.api.EnvType.CLIENT;

public class Spindash extends Action {



    public static final int MAX_COOL_DOWN_TICK = 10;

    private int coolTime = 0;
    private int successivelyCount = 0;
    private int successivelyCoolTick = 0;

    @Environment(CLIENT)
    @Override
    public void onClientTick(PlayerEntity player, Parkourability parkourability, IStamina stamina) {
        if (coolTime > 0) coolTime--;
        if (successivelyCoolTick > 0) {
            successivelyCoolTick--;
        } else {
            successivelyCount = 0;
        }
    }

    @Override
    public void onStartInServer(PlayerEntity player, Parkourability parkourability, ByteBuffer startData) {
        player.setInvulnerable(true);
    }

    @Override
    public void onStopInServer(PlayerEntity player) {
        player.setInvulnerable(false);
    }

    @Override
    public boolean canStart(PlayerEntity player, Parkourability parkourability, IStamina stamina, ByteBuffer startInfo) {
        return false;
//        return (parkourability.getActionInfo().can(Spindash.class)
//                && KeyRecorder.keyDodge.isPressed()
//                && !isInSuccessiveCoolDown()
//                && coolTime <= 0
//                && player.isInsideWaterOrBubbleColumn()
//                && player.isSwimming()
//                && player.isInSwimmingPose()
//                && !player.isSneaking()
//                && !stamina.isExhausted()
//        );
    }

    @Environment(CLIENT)
    @Override
    public boolean canContinue(PlayerEntity player, Parkourability parkourability, IStamina stamina) {
        return false;
//        return !(!player.isInsideWaterOrBubbleColumn()
//                || parkourability.get(ClingToCliff.class).isDoing()
//                || !KeyRecorder.keyDodge.isPressed()
//                || player.isFallFlying()
//                || player.isSwimming()
//                || !player.isInSwimmingPose()
//                || player.getAbilities().flying
//                || !parkourability.getActionInfo().can(Dodge.class)
//        );
    }

    public int getCoolTime() {
        return coolTime;
    }

    public int getSuccessivelyCoolTick() {
        return successivelyCoolTick;
    }

    public boolean isInSuccessiveCoolDown() {
        return successivelyCount >= 3;
    }

    public float getCoolDownPhase() {
        return Math.min(
                (float) (MAX_COOL_DOWN_TICK - getCoolTime()) / MAX_COOL_DOWN_TICK,
                isInSuccessiveCoolDown() ? (float) (MAX_COOL_DOWN_TICK * 3 - getSuccessivelyCoolTick()) / (MAX_COOL_DOWN_TICK * 3.0f) : 1
        );
    }

    @Override
    public StaminaConsumeTiming getStaminaConsumeTiming() {
        return null;
    }
    @Override
    public void onStartInLocalClient(PlayerEntity player, Parkourability parkourability, IStamina stamina, ByteBuffer startData) {
        Vec3d lookVec = player.getRotationVector();
        lookVec = new Vec3d(lookVec.x, lookVec.y, lookVec.z).normalize();

        Vec3d dodgeVec = Vec3d.ZERO;

        dodgeVec = lookVec.multiply(0.6 * ParCoolConfig.CONFIG_CLIENT.dodgeSpeedModifier.get());

        EntityUtil.addVelocity(player, dodgeVec);

        Animation animation = Animation.get(player);
        if (animation != null) animation.setAnimator(new SpindashAnimator());
    }

    @Override
    public void onStartInOtherClient(PlayerEntity player, Parkourability parkourability, ByteBuffer startData) {
        Animation animation = Animation.get(player);
        if (animation != null) animation.setAnimator(new SpindashAnimator());
    }
}
