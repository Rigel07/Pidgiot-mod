package com.cutepigeons.client;

import com.cutepigeons.CutePigeons;
import com.cutepigeons.registry.ModEntities;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid=CutePigeons.MOD_ID, bus=Mod.EventBusSubscriber.Bus.MOD, value=Dist.CLIENT)
public class ClientEvents {
    @SubscribeEvent public static void layers(EntityRenderersEvent.RegisterLayerDefinitions e){ e.registerLayerDefinition(PigeonModel.LAYER, PigeonModel::createBodyLayer); }
    @SubscribeEvent public static void renderers(EntityRenderersEvent.RegisterRenderers e){ e.registerEntityRenderer(ModEntities.PIGEON.get(), PigeonRenderer::new); }
    public static class PigeonRenderer extends MobRenderer<com.cutepigeons.entity.CutePigeonEntity, PigeonModel> {
        private static final ResourceLocation TEX = new ResourceLocation(CutePigeons.MOD_ID,"textures/entity/pigeon.png");
        public PigeonRenderer(EntityRendererProvider.Context ctx){ super(ctx,new PigeonModel(ctx.bakeLayer(PigeonModel.LAYER)),0.28f); }
        @Override public ResourceLocation getTextureLocation(com.cutepigeons.entity.CutePigeonEntity e){ return TEX; }
    }
}
