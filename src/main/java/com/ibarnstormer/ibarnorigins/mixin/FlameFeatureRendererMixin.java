package com.ibarnstormer.ibarnorigins.mixin;

import com.ibarnstormer.ibarnorigins.client.ModModelLoader;
import com.ibarnstormer.ibarnorigins.client.render.entity.state.SoulFireRenderState;
import com.ibarnstormer.ibarnorigins.client.render.entity.state.SoulMageFireRenderState;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.renderer.feature.FeatureFrameContext;
import net.minecraft.client.renderer.feature.FlameFeatureRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.sprite.SpriteId;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(FlameFeatureRenderer.class)
public class FlameFeatureRendererMixin {

    @Unique
    private static boolean shouldRenderSoulFire = false;
    @Unique
    private static boolean shouldRenderSoulMageFire = false;

    @Unique
    private SoulFireRenderState.Command getSoulFireRenderCommand(FlameFeatureRenderer.Submit command) {
        return (SoulFireRenderState.Command) (Object) command;
    }
    @Unique
    private SoulMageFireRenderState.Command getSoulMageFireRenderCommand(FlameFeatureRenderer.Submit command) {
        return (SoulMageFireRenderState.Command) (Object) command;
    }

    @Inject(method = "buildGroup", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/feature/FlameFeatureRenderer;prepare(Lnet/minecraft/client/renderer/feature/FlameFeatureRenderer$Submit;Lcom/mojang/blaze3d/vertex/VertexConsumer;Lnet/minecraft/client/renderer/texture/TextureAtlasSprite;Lnet/minecraft/client/renderer/texture/TextureAtlasSprite;)V", shift = At.Shift.BEFORE))
    private void fireCommandRenderer$renderSolid(FeatureFrameContext context, List<FlameFeatureRenderer.Submit> submits, CallbackInfo ci, @Local(name = "submit") FlameFeatureRenderer.Submit submit) {
        shouldRenderSoulFire = this.getSoulFireRenderCommand(submit).renderSoulFire();
        shouldRenderSoulMageFire = this.getSoulMageFireRenderCommand(submit).renderSoulMageFire();
    }

    @ModifyArg(method = "buildGroup", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/feature/FlameFeatureRenderer;prepare(Lnet/minecraft/client/renderer/feature/FlameFeatureRenderer$Submit;Lcom/mojang/blaze3d/vertex/VertexConsumer;Lnet/minecraft/client/renderer/texture/TextureAtlasSprite;Lnet/minecraft/client/renderer/texture/TextureAtlasSprite;)V"), index = 2)
    private TextureAtlasSprite fireCommandRenderer$renderFlame0(TextureAtlasSprite fire1, @Local(name = "context") FeatureFrameContext context) {
        if(shouldRenderSoulMageFire) return context.atlasManager().get(ModModelLoader.SOUL_MAGE_FIRE_0);
        if(shouldRenderSoulFire) return context.atlasManager().get(ModModelLoader.SOUL_FIRE_0);
        else return fire1;
    }

    @ModifyArg(method = "buildGroup", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/feature/FlameFeatureRenderer;prepare(Lnet/minecraft/client/renderer/feature/FlameFeatureRenderer$Submit;Lcom/mojang/blaze3d/vertex/VertexConsumer;Lnet/minecraft/client/renderer/texture/TextureAtlasSprite;Lnet/minecraft/client/renderer/texture/TextureAtlasSprite;)V"), index = 3)
    private TextureAtlasSprite fireCommandRenderer$renderFlame1(TextureAtlasSprite fire2, @Local(name = "context") FeatureFrameContext context) {
        if(shouldRenderSoulMageFire) return context.atlasManager().get(ModModelLoader.SOUL_MAGE_FIRE_1);
        if(shouldRenderSoulFire) return context.atlasManager().get(ModModelLoader.SOUL_FIRE_1);
        else return fire2;
    }


}
