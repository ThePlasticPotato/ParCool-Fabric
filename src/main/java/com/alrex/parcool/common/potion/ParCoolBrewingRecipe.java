package com.alrex.parcool.common.potion;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.PotionUtil;
import net.minecraft.potion.Potions;

import io.github.fabricators_of_create.porting_lib.brewing.IBrewingRecipe;
import net.minecraft.potion.Potion;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

public class ParCoolBrewingRecipe implements IBrewingRecipe {
	private static class MixPredicate {
		MixPredicate(Potion from, Item ingredient, Potion result) {
			this.from = from;
			this.ingredient = ingredient;
			this.result = result;
		}

		private final Potion from;
		private final Item ingredient;
		private final Potion result;
	}

	private static final List<MixPredicate> MIXES = Arrays.asList(
			addMix(Potions.AWKWARD, Items.POISONOUS_POTATO, com.alrex.parcool.common.potion.Potions.POOR_ENERGY_DRINK),
			addMix(Potions.AWKWARD, Items.CHICKEN, com.alrex.parcool.common.potion.Potions.POOR_ENERGY_DRINK),
			addMix(com.alrex.parcool.common.potion.Potions.POOR_ENERGY_DRINK, Items.QUARTZ, com.alrex.parcool.common.potion.Potions.ENERGY_DRINK),
			addMix(Potions.AWKWARD, Items.QUARTZ, com.alrex.parcool.common.potion.Potions.ENERGY_DRINK)
	);

	private static MixPredicate addMix(Potion from, Item ingredient, Potion result) {
		return new MixPredicate(from, ingredient, result);
	}

	@Nullable
	private static Potion mix(ItemStack input, ItemStack ingredient) {
		Potion inputPotion = PotionUtil.getPotion(input);
		Item ingredientItem = ingredient.getItem();
		return MIXES.stream()
				.filter((MixPredicate it) -> it.from == inputPotion && it.ingredient == ingredientItem)
				.findFirst()
				.orElse(new MixPredicate(null, null, null))
				.result;
	}

	private static boolean isPotionIngredient(Item item) {
		return MIXES.stream().anyMatch((MixPredicate it) -> item == it.ingredient);
	}

	@Override
	public boolean isInput(ItemStack input) {
		Item item = input.getItem();
		return item == Items.POTION || item == Items.SPLASH_POTION || item == Items.LINGERING_POTION;
	}

	@Override
	public boolean isIngredient(ItemStack ingredient) {
		Item item = ingredient.getItem();
		return isPotionIngredient(item);
	}

	@NotNull
	@Override
	public ItemStack getOutput(ItemStack input, @NotNull ItemStack ingredient) {

		if (!input.isEmpty() && !ingredient.isEmpty() && isIngredient(ingredient)) {
			Potion result = mix(input, ingredient);
			if (result != null) return PotionUtil.setPotion(new ItemStack(input.getItem()), result);
		}
		return ItemStack.EMPTY;
	}
}
