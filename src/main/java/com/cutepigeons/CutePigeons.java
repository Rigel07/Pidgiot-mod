package com.cutepigeons;

import com.cutepigeons.registry.ModEntities;
import com.cutepigeons.registry.ModItems;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.event.entity.SpawnPlacementRegisterEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(CutePigeons.MOD_ID)
public class CutePigeons {
    public static final String MOD_ID = "cute_pigeons";
    public CutePigeons() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        ModEntities.ENTITY_TYPES.register(bus);
        ModItems.ITEMS.register(bus);
    }

    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class CommonEvents {
        @SubscribeEvent
        public static void registerSpawnPlacements(SpawnPlacementRegisterEvent event) {
            event.register(ModEntities.PIGEON.get(), SpawnPlacements.Type.NO_RESTRICTIONS, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                    com.cutepigeons.entity.CutePigeonEntity::canSpawn, SpawnPlacementRegisterEvent.Operation.REPLACE);
        }
    }
}
