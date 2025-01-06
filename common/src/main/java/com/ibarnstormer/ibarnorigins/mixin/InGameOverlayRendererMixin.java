package com.ibarnstormer.ibarnorigins.mixin;

import com.ibarnstormer.ibarnorigins.client.ModModelLoader;
import com.ibarnstormer.ibarnorigins.entity.IbarnOriginsEntity;
import com.ibarnstormer.ibarnorigins.registry.IOEffects;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameOverlayRenderer;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.effect.StatusEffectInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameOverlayRenderer.class)
public class InGameOverlayRendererMixin {

    @Shadow
    private static void renderFireOverlay(MinecraftClient client, MatrixStack matrices) {}

    @Inject(method = "renderOverlays", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;isOnFire()Z", shift = At.Shift.BEFORE))
    private static void inGameOverlayRenderer$renderOverlays(MinecraftClient client, MatrixStack matrices, CallbackInfo ci) {
        ClientPlayerEntity player = client.player;

        if(player != null) {
            StatusEffectInstance soulFireStrength = player.getStatusEffect(IOEffects.SOUL_FIRE_STRENGTH.get());
            if(soulFireStrength != null && soulFireStrength.getAmplifier() >= 1) {
                renderFireOverlay(client, matrices);
            }
            else if(player.hasStatusEffect(IOEffects.SOUL_FIRE.get())) {
                renderFireOverlay(client, matrices);
            }
        }
    }

    @ModifyVariable(method = "renderFireOverlay", at = @At(value = "STORE"), ordinal = 0)
    private static Sprite inGameOverlayRenderer$renderFireOverlay(Sprite sprite, @Local(argsOnly = true) MinecraftClient client) {
        ClientPlayerEntity player = client.player;

        if(player != null) {
            StatusEffectInstance soulFireStrength = player.getStatusEffect(IOEffects.SOUL_FIRE_STRENGTH.get());
            if(soulFireStrength != null && soulFireStrength.getAmplifier() >= 1) {
                return ModModelLoader.SOUL_FIRE_1.getSprite();
            }
            else if(player instanceof IbarnOriginsEntity ioe && ioe.onSoulMageFire()) {
                return ModModelLoader.SOUL_MAGE_FIRE_1.getSprite();
            }
        }
        return sprite;
    }



}
