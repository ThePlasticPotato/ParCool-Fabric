package com.alrex.parcool.common.potion;

import com.alrex.parcool.ParCool;
import io.github.fabricators_of_create.porting_lib.mixin.common.MobEffectInstanceMixin;
import io.github.fabricators_of_create.porting_lib.util.LazyRegistrar;
import io.github.fabricators_of_create.porting_lib.util.RegistryObject;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.potion.Potion;
import net.minecraft.util.registry.Registry;

public class Potions {
	private static final LazyRegistrar<Potion> POTIONS = LazyRegistrar.create(Registry.POTION, ParCool.MOD_ID);
	public static final Potion POOR_ENERGY_DRINK =
			new Potion(
					new StatusEffectInstance(Effects.INEXHAUSTIBLE, 2400/*2 min*/),
					new StatusEffectInstance(StatusEffects.HUNGER, 100),
					new StatusEffectInstance(StatusEffects.POISON, 100)
			);
	public static final Potion ENERGY_DRINK =
			new Potion(
					new StatusEffectInstance(Effects.INEXHAUSTIBLE, 9600/*8 min*/)
			);
	private static final RegistryObject<Potion> POOR_ENERGY_DRINK_REGISTRY =
			POTIONS.register(
					"poor_energy_drink",
					() -> POOR_ENERGY_DRINK
			);
	public static final RegistryObject<Potion> ENERGY_DRINK_REGISTRY =
			POTIONS.register(
					"energy_drink",
					() -> ENERGY_DRINK
			);

	public static void registerAll() {
		POTIONS.register();
	}
}
