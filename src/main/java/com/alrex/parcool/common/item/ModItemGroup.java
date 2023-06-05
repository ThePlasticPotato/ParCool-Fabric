package com.alrex.parcool.common.item;

import com.alrex.parcool.ParCool;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

public class ModItemGroup {

    public static ItemGroup PARCOOL;

    static {
        PARCOOL = FabricItemGroupBuilder.build(new Identifier(ParCool.MOD_ID, "walkietalkie"),() -> new ItemStack(ItemRegistry.PARCOOL_GUIDE))
                .setName("itemGroup.walkietalkie.walkietalkie");

    }
}