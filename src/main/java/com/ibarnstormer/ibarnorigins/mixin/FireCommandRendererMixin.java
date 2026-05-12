package com.ibarnstormer.ibarnorigins.mixin;

import com.ibarnstormer.ibarnorigins.client.ModModelLoader;
import com.ibarnstormer.ibarnorigins.client.render.entity.state.SoulFireRenderState;
import com.ibarnstormer.ibarnorigins.client.render.entity.state.SoulMageFireRenderState;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.command.BatchingRenderCommandQueue;
import net.minecraft.client.render.command.FireCommandRenderer;
import net.minecraft.client.render.command.OrderedRenderCommandQueueImpl;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.client.texture.AtlasManager;
import net.minecraft.client.util.SpriteIdentifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FireCommandRenderer.class)
public class FireCommandRendererMixin {

    @Unique
    private static boolean shouldRenderSoulFire = false;
    @Unique
    private static boolean shouldRenderSoulMageFire = false;

    @Unique
    private SoulFireRenderState.Command getSoulFireRenderCommand(OrderedRenderCommandQueueImpl.FireCommand command) {
        return (SoulFireRenderState.Command) (Object) command;
    }
    @Unique
    private SoulMageFireRenderState.Command getSoulMageFireRenderCommand(OrderedRenderCommandQueueImpl.FireCommand command) {
        return (SoulMageFireRenderState.Command) (Object) command;
    }

    @Inject(method = "render(Lnet/minecraft/client/render/command/BatchingRenderCommandQueue;Lnet/minecraft/client/render/VertexConsumerProvider$Immediate;Lnet/minecraft/client/texture/AtlasManager;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/command/FireCommandRenderer;render(Lnet/minecraft/client/util/math/MatrixStack$Entry;Lnet/minecraft/client/render/VertexConsumerProvider;Lnet/minecraft/client/render/entity/state/EntityRenderState;Lorg/joml/Quaternionf;Lnet/minecraft/client/texture/AtlasManager;)V", shift = At.Shift.BEFORE))
    private void fireCommandRenderer$render(BatchingRenderCommandQueue queue, VertexConsumerProvider.Immediate vertexConsumers, AtlasManager atlasManager, CallbackInfo ci, @Local OrderedRenderCommandQueueImpl.FireCommand command) {
        shouldRenderSoulFire = this.getSoulFireRenderCommand(command).renderSoulFire();
        shouldRenderSoulMageFire = this.getSoulMageFireRenderCommand(command).renderSoulMageFire();
    }

    @ModifyArg(method = "render(Lnet/minecraft/client/util/math/MatrixStack$Entry;Lnet/minecraft/client/render/VertexConsumerProvider;Lnet/minecraft/client/render/entity/state/EntityRenderState;Lorg/joml/Quaternionf;Lnet/minecraft/client/texture/AtlasManager;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/texture/AtlasManager;getSprite(Lnet/minecraft/client/util/SpriteIdentifier;)Lnet/minecraft/client/texture/Sprite;", ordinal = 0))
    private SpriteIdentifier fireCommandRenderer$render0(SpriteIdentifier id) {
        if(shouldRenderSoulMageFire) return ModModelLoader.SOUL_MAGE_FIRE_0;
        if(shouldRenderSoulFire) return ModModelLoader.SOUL_FIRE_0;
        else return id;
    }

    @ModifyArg(method = "render(Lnet/minecraft/client/util/math/MatrixStack$Entry;Lnet/minecraft/client/render/VertexConsumerProvider;Lnet/minecraft/client/render/entity/state/EntityRenderState;Lorg/joml/Quaternionf;Lnet/minecraft/client/texture/AtlasManager;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/texture/AtlasManager;getSprite(Lnet/minecraft/client/util/SpriteIdentifier;)Lnet/minecraft/client/texture/Sprite;", ordinal = 1))
    private SpriteIdentifier fireCommandRenderer$render1(SpriteIdentifier id) {
        if(shouldRenderSoulMageFire) return ModModelLoader.SOUL_MAGE_FIRE_1;
        if(shouldRenderSoulFire) return ModModelLoader.SOUL_FIRE_1;
        else return id;
    }


}
