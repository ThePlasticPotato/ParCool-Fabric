package com.alrex.parcool.common.potion;

import io.github.fabricators_of_create.porting_lib.brewing.BrewingRecipeRegistry;
public class PotionRecipeRegistry {
	public static void register() {
		BrewingRecipeRegistry.addRecipe(new ParCoolBrewingRecipe());
	}
}
