package com.ibarnstormer.ibarnorigins.client.render;

import com.ibarnstormer.ibarnorigins.entity.SoulFireBallEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.*;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

@Environment(EnvType.CLIENT)
public class SoulFireBallEntityRenderer extends EntityRenderer<SoulFireBallEntity> {

    private static final Identifier TEXTURE = new Identifier("ibarnorigins:textures/entity/soul_fire_ball.png");
    private static final RenderLayer LAYER;

    public SoulFireBallEntityRenderer(EntityRendererFactory.Context dispatcher) {
        super(dispatcher);
    }

    protected int getBlockLight(SoulFireBallEntity soulFireballEntity, BlockPos blockPos) {
        return 15;
    }

    public void render(SoulFireBallEntity soulFireballEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        matrixStack.push();
        matrixStack.scale(0.75F, 0.75F, 0.75F);
        matrixStack.multiply(this.dispatcher.getRotation());
        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180.0F));
        MatrixStack.Entry entry = matrixStack.peek();
        Matrix4f matrix4f = entry.getPositionMatrix();
        Matrix3f matrix3f = entry.getNormalMatrix();
        VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(LAYER);
        produceVertex(vertexConsumer, matrix4f, matrix3f, i, 0.0F, 0, 0, 1);
        produceVertex(vertexConsumer, matrix4f, matrix3f, i, 1.0F, 0, 1, 1);
        produceVertex(vertexConsumer, matrix4f, matrix3f, i, 1.0F, 1, 1, 0);
        produceVertex(vertexConsumer, matrix4f, matrix3f, i, 0.0F, 1, 0, 0);
        matrixStack.pop();
        super.render(soulFireballEntity, f, g, matrixStack, vertexConsumerProvider, i);
    }

    private static void produceVertex(VertexConsumer vertexConsumer, Matrix4f modelMatrix, Matrix3f normalMatrix, int light, float x, int y, int textureU, int textureV) {
        vertexConsumer.vertex(modelMatrix, x - 0.5F, (float)y - 0.25F, 0.0F).color(255, 255, 255, 255).texture((float)textureU, (float)textureV).overlay(OverlayTexture.DEFAULT_UV).light(light).normal(normalMatrix, 0.0F, 1.0F, 0.0F).next();
    }

    @Override
    public Identifier getTexture(SoulFireBallEntity entity) {
        return TEXTURE;
    }

    static {
        LAYER = RenderLayer.getEntityCutoutNoCull(TEXTURE);
    }
}
