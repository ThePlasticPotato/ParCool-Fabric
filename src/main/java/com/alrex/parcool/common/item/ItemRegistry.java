package com.alrex.parcool.common.item;

import com.alrex.parcool.ParCool;
import io.github.fabricators_of_create.porting_lib.util.LazyRegistrar;
import io.github.fabricators_of_create.porting_lib.util.RegistryObject;
import net.minecraft.item.Item;
import net.minecraft.util.registry.Registry;


public class ItemRegistry {
	public static final LazyRegistrar<Item> ITEMS = LazyRegistrar.create(Registry.ITEM, ParCool.MOD_ID);
	public static final RegistryObject<Item> PARCOOL_GUIDE_REGISTRY = ITEMS.register("parcool_guide", () -> new Item(new Item.Settings().maxCount(1)));
	public static final Item PARCOOL_GUIDE = null;

	public static void registerAll() {}
}