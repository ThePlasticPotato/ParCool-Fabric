package com.alrex.parcool.client.animation.impl;

import com.alrex.parcool.client.animation.Animator;
import com.alrex.parcool.client.animation.PlayerModelRotator;
import com.alrex.parcool.client.animation.PlayerModelTransformer;
import com.alrex.parcool.common.action.impl.Dive;
import com.alrex.parcool.common.action.impl.Slide;
import com.alrex.parcool.common.action.impl.Spindash;
import com.alrex.parcool.common.capability.impl.Parkourability;
import com.alrex.parcool.utilities.EasingFunctions;
import com.alrex.parcool.utilities.MathUtil;
import net.minecraft.entity.player.PlayerEntity;

import static com.alrex.parcool.utilities.MathUtil.lerp;

public class SpindashAnimator extends Animator {

    private boolean notInitialized = true;

    @Override
    public boolean shouldRemoved(PlayerEntity player, Parkourability parkourability) {
        return !parkourability.get(Spindash.class).isDoing();
    }



    @Override
    public void animatePost(PlayerEntity player, Parkourability parkourability, PlayerModelTransformer transformer) {
        double ySpeed = parkourability.get(Dive.class).getPlayerYSpeed(transformer.getPartialTick());
        float phase = (getTick() + transformer.getPartialTick()) / 16f;
        float factor = factorFunc(phase);
    }

    @Override
    public void rotate(PlayerEntity player, Parkourability parkourability, PlayerModelRotator rotator) {

        float phase = (getTick() + rotator.getPartialTick()) / 16f;
        if (phase > 4) {
            return;
        }

        float factor = factorFunc(phase);
        rotator.startBasedTop()
                .rotateRightward(lerp(0, 360, factor))
                .end();
    }

    private float factorFunc(float phase) {
        if (phase < 0.5) {
            return 1 - 4 * MathUtil.squaring(phase - 0.5f);
        } else {
            return 1 - EasingFunctions.SinInOutBySquare(2 * (phase - 0.5f));
        }
    }
}
