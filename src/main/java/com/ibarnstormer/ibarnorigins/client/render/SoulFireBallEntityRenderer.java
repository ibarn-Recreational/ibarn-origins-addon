package com.ibarnstormer.ibarnorigins.client.render;

import com.ibarnstormer.ibarnorigins.entity.SoulFireBallEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;

@Environment(EnvType.CLIENT)
public class SoulFireBallEntityRenderer extends EntityRenderer<SoulFireBallEntity, EntityRenderState> {

    private static final Identifier TEXTURE = Identifier.parse("ibarnorigins:textures/entity/soul_fire_ball.png");
    private static final RenderType LAYER;

    public SoulFireBallEntityRenderer(EntityRendererProvider.Context dispatcher) {
        super(dispatcher);
    }

    @Override
    protected int getBlockLightLevel(SoulFireBallEntity soulFireballEntity, BlockPos blockPos) {
        return 15;
    }

    @Override
    public EntityRenderState createRenderState() {
        return new EntityRenderState();
    }

    @Override
    public void submit(EntityRenderState renderState, PoseStack matrices, SubmitNodeCollector queue, CameraRenderState cameraState) {
        matrices.pushPose();
        matrices.scale(0.75F, 0.75F, 0.75F);
        matrices.mulPose(cameraState.orientation);
        queue.submitCustomGeometry(matrices, LAYER, (matricesEntry, vertexConsumer) -> {
            produceVertex(vertexConsumer, matricesEntry, renderState.lightCoords, 0.0F, 0, 0, 1);
            produceVertex(vertexConsumer, matricesEntry, renderState.lightCoords, 1.0F, 0, 1, 1);
            produceVertex(vertexConsumer, matricesEntry, renderState.lightCoords, 1.0F, 1, 1, 0);
            produceVertex(vertexConsumer, matricesEntry, renderState.lightCoords, 0.0F, 1, 0, 0);
        });
        matrices.popPose();
        super.submit(renderState, matrices, queue, cameraState);
    }

    private static void produceVertex(VertexConsumer vertexConsumer, PoseStack.Pose matrix, int light, float x, int z, int textureU, int textureV) {
        vertexConsumer.addVertex(matrix, x - 0.5F, (float)z - 0.25F, 0.0F).setColor(-1).setUv((float)textureU, (float)textureV).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(matrix, 0.0F, 1.0F, 0.0F);
    }

    static {
        LAYER = RenderTypes.entityCutout(TEXTURE);
    }
}
