package com.alrex.parcool.utilities;


import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;

public class EntityUtil {
	public static void addVelocity(Entity entity, Vec3d vec) {
		entity.setVelocity(entity.getVelocity().add(vec));
	}
}
