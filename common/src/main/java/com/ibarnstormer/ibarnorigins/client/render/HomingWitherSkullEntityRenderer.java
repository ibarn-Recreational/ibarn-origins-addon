package com.ibarnstormer.ibarnorigins.client.render;

import com.ibarnstormer.ibarnorigins.entity.HomingWitherSkullEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.*;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class HomingWitherSkullEntityRenderer extends EntityRenderer<HomingWitherSkullEntity> {

    private static final ResourceLocation INVULNERABLE_TEXTURE = ResourceLocation.withDefaultNamespace("textures/entity/wither/wither_invulnerable.png");
    private static final ResourceLocation TEXTURE = ResourceLocation.withDefaultNamespace("textures/entity/wither/wither.png");
    private final SkullModel model;


    public HomingWitherSkullEntityRenderer(EntityRendererProvider.Context ctx) {
        super(ctx);
        this.model = new SkullModel(ctx.bakeLayer(ModelLayers.WITHER_SKULL));
    }

    public static LayerDefinition getTexturedModelData() {
        MeshDefinition modelData = new MeshDefinition();
        PartDefinition modelPartData = modelData.getRoot();
        modelPartData.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 35).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F), PartPose.ZERO);
        return LayerDefinition.create(modelData, 64, 64);
    }

    protected int getBlockLight(HomingWitherSkullEntity arg, BlockPos arg2) {
        return 15;
    }

    public void render(HomingWitherSkullEntity arg, float f, float g, PoseStack arg2, MultiBufferSource arg3, int i) {
        arg2.pushPose();
        arg2.scale(-1.0F, -1.0F, 1.0F);
        float h = Mth.rotLerp(g, arg.yRotO, arg.getYRot());
        float j = Mth.lerp(g, arg.xRotO, arg.getXRot());
        VertexConsumer vertexConsumer = arg3.getBuffer(this.model.renderType(this.getTextureLocation(arg)));
        this.model.setupAnim(0.0F, h, j);
        this.model.renderToBuffer(arg2, vertexConsumer, i, OverlayTexture.NO_OVERLAY);
        arg2.popPose();
        super.render(arg, f, g, arg2, arg3, i);
    }

    @Override
    public ResourceLocation getTextureLocation(HomingWitherSkullEntity arg) {
        return arg.isCharged() ? INVULNERABLE_TEXTURE : TEXTURE;
    }
}
