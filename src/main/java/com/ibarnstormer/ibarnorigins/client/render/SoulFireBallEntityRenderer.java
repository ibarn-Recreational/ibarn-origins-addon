package com.ibarnstormer.ibarnorigins.client.render;

import com.ibarnstormer.ibarnorigins.entity.SoulFireBallEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.*;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.client.render.state.CameraRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.*;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

@Environment(EnvType.CLIENT)
public class SoulFireBallEntityRenderer extends EntityRenderer<SoulFireBallEntity, EntityRenderState> {

    private static final Identifier TEXTURE = Identifier.of("ibarnorigins:textures/entity/soul_fire_ball.png");
    private static final RenderLayer LAYER;

    public SoulFireBallEntityRenderer(EntityRendererFactory.Context dispatcher) {
        super(dispatcher);
    }

    protected int getBlockLight(SoulFireBallEntity soulFireballEntity, BlockPos blockPos) {
        return 15;
    }

    @Override
    public EntityRenderState createRenderState() {
        return new EntityRenderState();
    }

    public void render(EntityRenderState renderState, MatrixStack matrices, OrderedRenderCommandQueue queue, CameraRenderState cameraState) {
        matrices.push();
        matrices.scale(0.75F, 0.75F, 0.75F);
        matrices.multiply(cameraState.orientation);
        queue.submitCustom(matrices, LAYER, (matricesEntry, vertexConsumer) -> {
            produceVertex(vertexConsumer, matricesEntry, renderState.light, 0.0F, 0, 0, 1);
            produceVertex(vertexConsumer, matricesEntry, renderState.light, 1.0F, 0, 1, 1);
            produceVertex(vertexConsumer, matricesEntry, renderState.light, 1.0F, 1, 1, 0);
            produceVertex(vertexConsumer, matricesEntry, renderState.light, 0.0F, 1, 0, 0);
        });
        matrices.pop();
        super.render(renderState, matrices, queue, cameraState);
    }

    private static void produceVertex(VertexConsumer vertexConsumer, MatrixStack.Entry matrix, int light, float x, int z, int textureU, int textureV) {
        vertexConsumer.vertex(matrix, x - 0.5F, (float)z - 0.25F, 0.0F).color(-1).texture((float)textureU, (float)textureV).overlay(OverlayTexture.DEFAULT_UV).light(light).normal(matrix, 0.0F, 1.0F, 0.0F);
    }

    static {
        LAYER = RenderLayers.entityCutoutNoCull(TEXTURE);
    }
}
