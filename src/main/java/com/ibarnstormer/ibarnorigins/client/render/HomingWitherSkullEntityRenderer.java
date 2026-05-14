package com.ibarnstormer.ibarnorigins.client.render;

import com.ibarnstormer.ibarnorigins.client.render.entity.state.HomingWitherSkullEntityRenderState;
import com.ibarnstormer.ibarnorigins.entity.HomingWitherSkullEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.*;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.object.skull.SkullModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;

@Environment(EnvType.CLIENT)
public class HomingWitherSkullEntityRenderer extends EntityRenderer<HomingWitherSkullEntity, HomingWitherSkullEntityRenderState> {

    private static final Identifier INVULNERABLE_TEXTURE = Identifier.withDefaultNamespace("textures/entity/wither/wither_invulnerable.png");
    private static final Identifier TEXTURE = Identifier.withDefaultNamespace("textures/entity/wither/wither.png");
    private final SkullModel model;


    public HomingWitherSkullEntityRenderer(EntityRendererProvider.Context ctx) {
        super(ctx);
        this.model = new SkullModel(ctx.bakeLayer(ModelLayers.WITHER_SKULL));
    }

    private Identifier getTexture(HomingWitherSkullEntityRenderState state) {
        return state.charged ? INVULNERABLE_TEXTURE : TEXTURE;
    }

    @Override
    protected int getBlockLightLevel(HomingWitherSkullEntity arg, BlockPos blockPos) {
        return 15;
    }

    @Override
    public HomingWitherSkullEntityRenderState createRenderState() {
        return new HomingWitherSkullEntityRenderState();
    }

    public void render(HomingWitherSkullEntityRenderState renderState, PoseStack matrixStack, SubmitNodeCollector orderedRenderCommandQueue, CameraRenderState cameraRenderState) {
        matrixStack.pushPose();
        matrixStack.scale(-1.0F, -1.0F, 1.0F);
        orderedRenderCommandQueue.submitModel(this.model, renderState.skullState, matrixStack, this.model.renderType(this.getTexture(renderState)), renderState.lightCoords, OverlayTexture.NO_OVERLAY, renderState.outlineColor, null);
        matrixStack.popPose();
        super.submit(renderState, matrixStack, orderedRenderCommandQueue, cameraRenderState);
    }

    public void updateRenderState(HomingWitherSkullEntity entity, HomingWitherSkullEntityRenderState renderState, float f) {
        super.extractRenderState(entity, renderState, f);
        renderState.charged = entity.isCharged();
        renderState.skullState.animationPos = 0.0F;
        renderState.skullState.yRot = entity.getYRot(f);
        renderState.skullState.xRot = entity.getXRot(f);
    }
}
