package com.ibarnstormer.ibarnorigins.client.render;

import com.ibarnstormer.ibarnorigins.entity.HomingWitherSkullEntity;
import net.minecraft.client.model.*;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.SkullEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.projectile.WitherSkullEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

public class HomingWitherSkullEntityRenderer extends EntityRenderer<HomingWitherSkullEntity> {

    private static final Identifier INVULNERABLE_TEXTURE = new Identifier("textures/entity/wither/wither_invulnerable.png");
    private static final Identifier TEXTURE = new Identifier("textures/entity/wither/wither.png");
    private final SkullEntityModel model;


    public HomingWitherSkullEntityRenderer(EntityRendererFactory.Context ctx) {
        super(ctx);
        this.model = new SkullEntityModel(ctx.getPart(EntityModelLayers.WITHER_SKULL));
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        modelPartData.addChild("head", ModelPartBuilder.create().uv(0, 35).cuboid(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F), ModelTransform.NONE);
        return TexturedModelData.of(modelData, 64, 64);
    }

    protected int getBlockLight(WitherSkullEntity arg, BlockPos arg2) {
        return 15;
    }

    public void render(HomingWitherSkullEntity arg, float f, float g, MatrixStack arg2, VertexConsumerProvider arg3, int i) {
        arg2.push();
        arg2.scale(-1.0F, -1.0F, 1.0F);
        float h = MathHelper.lerpAngleDegrees(g, arg.prevYaw, arg.getYaw());
        float j = MathHelper.lerp(g, arg.prevPitch, arg.getPitch());
        VertexConsumer vertexConsumer = arg3.getBuffer(this.model.getLayer(this.getTexture(arg)));
        this.model.setHeadRotation(0.0F, h, j);
        this.model.render(arg2, vertexConsumer, i, OverlayTexture.DEFAULT_UV, 1.0F, 1.0F, 1.0F, 1.0F);
        arg2.pop();
        super.render(arg, f, g, arg2, arg3, i);
    }

    public Identifier getTexture(HomingWitherSkullEntity arg) {
        return arg.isCharged() ? INVULNERABLE_TEXTURE : TEXTURE;
    }
}
