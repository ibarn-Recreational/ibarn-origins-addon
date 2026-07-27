package com.ibarnstormer.ibarnorigins.client.render;

import com.ibarnstormer.ibarnorigins.IbarnOriginsMain;
import com.ibarnstormer.ibarnorigins.entity.SoulFireBallEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

public class SoulFireBallEntityRenderer extends EntityRenderer<SoulFireBallEntity> {

    private static final ResourceLocation TEXTURE = IbarnOriginsMain.IOIdentifier("textures/entity/soul_fire_ball.png");
    private static final RenderType LAYER;

    public SoulFireBallEntityRenderer(EntityRendererProvider.Context dispatcher) {
        super(dispatcher);
    }

    protected int getBlockLight(SoulFireBallEntity soulFireballEntity, BlockPos blockPos) {
        return 15;
    }

    public void render(SoulFireBallEntity soulFireballEntity, float f, float g, PoseStack matrixStack, MultiBufferSource vertexConsumerProvider, int i) {
        matrixStack.pushPose();
        matrixStack.scale(0.75F, 0.75F, 0.75F);
        matrixStack.mulPose(this.entityRenderDispatcher.cameraOrientation());
        matrixStack.mulPose(Axis.YP.rotationDegrees(180.0F));
        PoseStack.Pose entry = matrixStack.last();
        Matrix4f matrix4f = entry.pose();
        Matrix3f matrix3f = entry.normal();
        VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(LAYER);
        produceVertex(vertexConsumer, matrix4f, matrix3f, i, 0.0F, 0, 0, 1);
        produceVertex(vertexConsumer, matrix4f, matrix3f, i, 1.0F, 0, 1, 1);
        produceVertex(vertexConsumer, matrix4f, matrix3f, i, 1.0F, 1, 1, 0);
        produceVertex(vertexConsumer, matrix4f, matrix3f, i, 0.0F, 1, 0, 0);
        matrixStack.popPose();
        super.render(soulFireballEntity, f, g, matrixStack, vertexConsumerProvider, i);
    }

    private static void produceVertex(VertexConsumer vertexConsumer, Matrix4f modelMatrix, Matrix3f normalMatrix, int light, float x, int y, int textureU, int textureV) {
        vertexConsumer.addVertex(modelMatrix, x - 0.5F, (float)y - 0.25F, 0.0F).setColor(255, 255, 255, 255).setUv((float)textureU, (float)textureV).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(0.0F, 1.0F, 0.0F);
    }

    @Override
    public ResourceLocation getTextureLocation(SoulFireBallEntity entity) {
        return TEXTURE;
    }

    static {
        LAYER = RenderType.entityCutoutNoCull(TEXTURE);
    }
}
