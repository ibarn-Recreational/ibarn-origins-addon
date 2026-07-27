package com.ibarnstormer.ibarnorigins.mixin;

import com.ibarnstormer.ibarnorigins.client.ModModelLoader;
import com.ibarnstormer.ibarnorigins.entity.IbarnOriginsEntity;
import com.ibarnstormer.ibarnorigins.registry.IOEffects;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.ScreenEffectRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.effect.MobEffectInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ScreenEffectRenderer.class)
public class ScreenEffectRendererMixin {

    @Shadow
    private static void renderFire(Minecraft minecraft, PoseStack poseStack) {}

    @Inject(method = "renderScreenEffect", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;isOnFire()Z", shift = At.Shift.BEFORE))
    private static void inGameOverlayRenderer$renderOverlays(Minecraft minecraft, PoseStack poseStack, CallbackInfo ci) {
        LocalPlayer player = minecraft.player;

        if(player != null) {
            MobEffectInstance soulFireStrength = player.getEffect(IOEffects.SOUL_FIRE_STRENGTH.getRef());
            if(soulFireStrength != null && soulFireStrength.getAmplifier() >= 1) {
                renderFire(minecraft, poseStack);
            }
            else if(player.hasEffect(IOEffects.SOUL_FIRE.getRef())) {
                renderFire(minecraft, poseStack);
            }
        }
    }

    @ModifyVariable(method = "renderFire", at = @At(value = "STORE"), ordinal = 0)
    private static TextureAtlasSprite inGameOverlayRenderer$renderFireOverlay(TextureAtlasSprite sprite, @Local(argsOnly = true) Minecraft minecraft) {
        LocalPlayer player = minecraft.player;

        if(player != null) {
            MobEffectInstance soulFireStrength = player.getEffect(IOEffects.SOUL_FIRE_STRENGTH.getRef());
            if(soulFireStrength != null && soulFireStrength.getAmplifier() >= 1) {
                return ModModelLoader.SOUL_FIRE_1.sprite();
            }
            else if(player instanceof IbarnOriginsEntity ioe && ioe.onSoulMageFire()) {
                return ModModelLoader.SOUL_MAGE_FIRE_1.sprite();
            }
        }
        return sprite;
    }



}
