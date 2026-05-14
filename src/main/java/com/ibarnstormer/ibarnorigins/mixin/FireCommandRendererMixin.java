package com.ibarnstormer.ibarnorigins.mixin;

import com.ibarnstormer.ibarnorigins.client.ModModelLoader;
import com.ibarnstormer.ibarnorigins.client.render.entity.state.SoulFireRenderState;
import com.ibarnstormer.ibarnorigins.client.render.entity.state.SoulMageFireRenderState;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.SubmitNodeCollection;
import net.minecraft.client.renderer.SubmitNodeStorage;
import net.minecraft.client.renderer.feature.FlameFeatureRenderer;
import net.minecraft.client.resources.model.sprite.SpriteId;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FlameFeatureRenderer.class)
public class FireCommandRendererMixin {

    @Unique
    private static boolean shouldRenderSoulFire = false;
    @Unique
    private static boolean shouldRenderSoulMageFire = false;

    @Unique
    private SoulFireRenderState.Command getSoulFireRenderCommand(SubmitNodeStorage.FlameSubmit command) {
        return (SoulFireRenderState.Command) (Object) command;
    }
    @Unique
    private SoulMageFireRenderState.Command getSoulMageFireRenderCommand(SubmitNodeStorage.FlameSubmit command) {
        return (SoulMageFireRenderState.Command) (Object) command;
    }

    @Inject(method = "renderSolid", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/feature/FlameFeatureRenderer;renderFlame(Lcom/mojang/blaze3d/vertex/PoseStack$Pose;Lnet/minecraft/client/renderer/MultiBufferSource;Lnet/minecraft/client/renderer/entity/state/EntityRenderState;Lorg/joml/Quaternionf;Lnet/minecraft/client/resources/model/sprite/AtlasManager;)V", shift = At.Shift.BEFORE))
    private void fireCommandRenderer$renderSolid(SubmitNodeCollection nodeCollection, MultiBufferSource.BufferSource bufferSource, net.minecraft.client.resources.model.sprite.AtlasManager atlasManager, CallbackInfo ci, @Local SubmitNodeStorage.FlameSubmit command) {
        shouldRenderSoulFire = this.getSoulFireRenderCommand(command).renderSoulFire();
        shouldRenderSoulMageFire = this.getSoulMageFireRenderCommand(command).renderSoulMageFire();
    }

    @ModifyArg(method = "renderFlame", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/resources/model/sprite/AtlasManager;get(Lnet/minecraft/client/resources/model/sprite/SpriteId;)Lnet/minecraft/client/renderer/texture/TextureAtlasSprite;", ordinal = 0))
    private SpriteId fireCommandRenderer$renderFlame0(SpriteId sprite) {
        if(shouldRenderSoulMageFire) return ModModelLoader.SOUL_MAGE_FIRE_0;
        if(shouldRenderSoulFire) return ModModelLoader.SOUL_FIRE_0;
        else return sprite;
    }

    @ModifyArg(method = "renderFlame", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/resources/model/sprite/AtlasManager;get(Lnet/minecraft/client/resources/model/sprite/SpriteId;)Lnet/minecraft/client/renderer/texture/TextureAtlasSprite;", ordinal = 1))
    private SpriteId fireCommandRenderer$renderFlame1(SpriteId sprite) {
        if(shouldRenderSoulMageFire) return ModModelLoader.SOUL_MAGE_FIRE_1;
        if(shouldRenderSoulFire) return ModModelLoader.SOUL_FIRE_1;
        else return sprite;
    }


}
