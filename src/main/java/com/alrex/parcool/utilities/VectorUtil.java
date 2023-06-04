package com.alrex.parcool.utilities;

import net.minecraft.util.math.Vec3d;

public class VectorUtil {
	public static double toYawDegree(Vec3d vec) {
		return (Math.atan2(vec.z, vec.x) * 180.0 / Math.PI - 90);
	}

	public static double toPitchDegree(Vec3d vec) {
		return -(Math.atan2(vec.y, Math.sqrt(vec.x * vec.x + vec.z * vec.z)) * 180.0 / Math.PI);
	}

	public static Vec3d fromYawDegree(double degree) {
		return new Vec3d(-Math.sin(Math.toRadians(degree)), 0, Math.cos(Math.toRadians(degree)));
	}
}
