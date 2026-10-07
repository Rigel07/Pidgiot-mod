package com.cutepigeons.registry;

import com.cutepigeons.CutePigeons;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, CutePigeons.MOD_ID);
    public static final RegistryObject<Item> PIGEON_SPAWN_EGG = ITEMS.register("pigeon_spawn_egg", () ->
        new SpawnEggItem(ModEntities.PIGEON, 0xE7E1D9, 0xF2A8C8, new Item.Properties()));
}
