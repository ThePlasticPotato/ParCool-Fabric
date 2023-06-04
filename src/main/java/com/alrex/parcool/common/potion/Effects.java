package com.alrex.parcool.common.potion;


import com.alrex.parcool.ParCool;
import com.alrex.parcool.common.potion.effects.InexhaustibleEffect;
import io.github.fabricators_of_create.porting_lib.util.LazyRegistrar;
import io.github.fabricators_of_create.porting_lib.util.RegistryObject;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.util.registry.Registry;

public class Effects {
	private static final LazyRegistrar<StatusEffect> EFFECTS = LazyRegistrar.create(Registry.STATUS_EFFECT, ParCool.MOD_ID);
	public static final StatusEffect INEXHAUSTIBLE = new InexhaustibleEffect();
	public static final RegistryObject<StatusEffect> INEXHAUSTIBLE_REGISTRY = EFFECTS.register(
			"inexhaustible", () -> INEXHAUSTIBLE
	);

	public static void registerAll() {
		EFFECTS.register();
	}
}
