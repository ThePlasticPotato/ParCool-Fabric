package com.alrex.parcool.mixin.client;

import com.alrex.parcool.utilities.IParCoolCamera;
import net.minecraft.client.render.Camera;
import net.minecraft.util.math.Quaternion;
import net.minecraft.util.math.Vec3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.Inject;

@Mixin(Camera.class)
public class MixinCamera implements IParCoolCamera {

    @Unique
    private float roll;

    @Shadow
    private float pitch;

    @Shadow
    private float yaw;

    @Shadow
    private Quaternion rotation;

    @Shadow
    private Vec3f horizontalPlane;

    @Shadow
    private Vec3f verticalPlane;

    @Shadow
    private Vec3f diagonalPlane;

    @Override
    public void setRoll(float roll) {
        this.roll = roll;
    }

    /**
     * @author Potaot
     * @reason hell
     */
    @Overwrite
    protected void setRotation(float yaw, float pitch) {
        this.pitch = pitch;
        this.yaw = yaw;
        this.rotation.set(0.0F, 0.0F, 0.0F, 1.0F);
        this.rotation.hamiltonProduct(Vec3f.POSITIVE_Z.getDegreesQuaternion(roll));
        this.rotation.hamiltonProduct(Vec3f.POSITIVE_Y.getDegreesQuaternion(-yaw));
        this.rotation.hamiltonProduct(Vec3f.POSITIVE_X.getDegreesQuaternion(pitch));
        this.horizontalPlane.set(0.0F, 0.0F, 1.0F);
        this.horizontalPlane.rotate(this.rotation);
        this.verticalPlane.set(0.0F, 1.0F, 0.0F);
        this.verticalPlane.rotate(this.rotation);
        this.diagonalPlane.set(1.0F, 0.0F, 0.0F);
        this.diagonalPlane.rotate(this.rotation);
    }
}
