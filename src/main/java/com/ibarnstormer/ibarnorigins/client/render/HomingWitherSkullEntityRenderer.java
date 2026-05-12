package com.ibarnstormer.ibarnorigins.client.render;

import com.ibarnstormer.ibarnorigins.client.render.entity.state.HomingWitherSkullEntityRenderState;
import com.ibarnstormer.ibarnorigins.entity.HomingWitherSkullEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.*;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.SkullEntityModel;
import net.minecraft.client.render.state.CameraRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

@Environment(EnvType.CLIENT)
public class HomingWitherSkullEntityRenderer extends EntityRenderer<HomingWitherSkullEntity, HomingWitherSkullEntityRenderState> {

    private static final Identifier INVULNERABLE_TEXTURE = Identifier.ofVanilla("textures/entity/wither/wither_invulnerable.png");
    private static final Identifier TEXTURE = Identifier.ofVanilla("textures/entity/wither/wither.png");
    private final SkullEntityModel model;


    public HomingWitherSkullEntityRenderer(EntityRendererFactory.Context ctx) {
        super(ctx);
        this.model = new SkullEntityModel(ctx.getPart(EntityModelLayers.WITHER_SKULL));
    }

    private Identifier getTexture(HomingWitherSkullEntityRenderState state) {
        return state.charged ? INVULNERABLE_TEXTURE : TEXTURE;
    }

    protected int getBlockLight(HomingWitherSkullEntity arg, BlockPos arg2) {
        return 15;
    }

    @Override
    public HomingWitherSkullEntityRenderState createRenderState() {
        return new HomingWitherSkullEntityRenderState();
    }

    public void render(HomingWitherSkullEntityRenderState renderState, MatrixStack matrixStack, OrderedRenderCommandQueue orderedRenderCommandQueue, CameraRenderState cameraRenderState) {
        matrixStack.push();
        matrixStack.scale(-1.0F, -1.0F, 1.0F);
        orderedRenderCommandQueue.submitModel(this.model, renderState.skullState, matrixStack, this.model.getLayer(this.getTexture(renderState)), renderState.light, OverlayTexture.DEFAULT_UV, renderState.outlineColor, null);
        matrixStack.pop();
        super.render(renderState, matrixStack, orderedRenderCommandQueue, cameraRenderState);
    }

    public void updateRenderState(HomingWitherSkullEntity entity, HomingWitherSkullEntityRenderState renderState, float f) {
        super.updateRenderState(entity, renderState, f);
        renderState.charged = entity.isCharged();
        renderState.skullState.poweredTicks = 0.0F;
        renderState.skullState.yaw = entity.getLerpedYaw(f);
        renderState.skullState.pitch = entity.getLerpedPitch(f);
    }
}
