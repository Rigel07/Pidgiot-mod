package com.cutepigeons.client;

import com.cutepigeons.entity.CutePigeonEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

public class PigeonModel extends EntityModel<CutePigeonEntity> {
    public static final ModelLayerLocation LAYER = new ModelLayerLocation(new net.minecraft.resources.ResourceLocation("cute_pigeons", "pigeon"), "main");
    private final ModelPart body, head, leftWing, rightWing, tail;
    public PigeonModel(ModelPart root) {
        body=root.getChild("body"); head=root.getChild("head"); leftWing=root.getChild("left_wing"); rightWing=root.getChild("right_wing"); tail=root.getChild("tail");
    }
    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh=new MeshDefinition(); PartDefinition root=mesh.getRoot();
        root.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0,0).addBox(-4,-3,-5,8,6,10), PartPose.offset(0,18,0));
        root.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0,16).addBox(-3,-3,-3,6,6,6).texOffs(24,16).addBox(-1,-1,-5,2,2,2), PartPose.offset(0,14,-4));
        root.addOrReplaceChild("left_wing", CubeListBuilder.create().texOffs(24,0).addBox(0,-1,-4,1,2,8), PartPose.offset(4,17,0));
        root.addOrReplaceChild("right_wing", CubeListBuilder.create().texOffs(24,10).addBox(-1,-1,-4,1,2,8), PartPose.offset(-4,17,0));
        root.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(0,28).addBox(-3,-2,0,6,4,6), PartPose.offset(0,18,5));
        return LayerDefinition.create(mesh, 64, 64);
    }
    @Override public void setupAnim(CutePigeonEntity p,float limb,float limbAmount,float age,float yaw,float pitch){ head.yRot=yaw*(float)Math.PI/180f; head.xRot=pitch*(float)Math.PI/180f; float flap=p.wingFlap(age); leftWing.zRot=-0.15f-flap; rightWing.zRot=0.15f+flap; tail.xRot=0.15f; }
    @Override public void renderToBuffer(PoseStack pose,VertexConsumer vc,int light,int overlay,float r,float g,float b,float a){ body.render(pose,vc,light,overlay,r,g,b,a); head.render(pose,vc,light,overlay,r,g,b,a); leftWing.render(pose,vc,light,overlay,r,g,b,a); rightWing.render(pose,vc,light,overlay,r,g,b,a); tail.render(pose,vc,light,overlay,r,g,b,a); }
}
