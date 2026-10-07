package com.cutepigeons.registry;

import com.cutepigeons.CutePigeons;
import com.cutepigeons.entity.CutePigeonEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, CutePigeons.MOD_ID);
    public static final RegistryObject<EntityType<CutePigeonEntity>> PIGEON = ENTITY_TYPES.register("pigeon", () ->
        EntityType.Builder.of(CutePigeonEntity::new, MobCategory.CREATURE)
            .sized(0.55F, 0.65F)
            .clientTrackingRange(8)
            .build("cute_pigeons:pigeon"));
}
