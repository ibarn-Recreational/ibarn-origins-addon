package com.ibarnstormer.ibarnorigins.mixin;

import com.ibarnstormer.ibarnorigins.client.ModModelLoader;
import com.ibarnstormer.ibarnorigins.registry.IOEffects;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.ScreenEffectRenderer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.sprite.SpriteGetter;
import net.minecraft.world.effect.MobEffectInstance;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ScreenEffectRenderer.class)
public class ScreenEffectRendererMixin {

    @Shadow @Final
    private Minecraft minecraft;
    @Shadow @Final
    private SpriteGetter sprites;

    @Shadow
    private static void submitFire(final PoseStack poseStack, final SubmitNodeCollector submitNodeCollector, final TextureAtlasSprite sprite) {}

    @Inject(method = "submit", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;isOnFire()Z", shift = At.Shift.BEFORE))
    private void inGameOverlayRenderer$renderOverlays(boolean isFirstPerson, boolean isSleeping, float partialTicks, SubmitNodeCollector submitNodeCollector, boolean hideGui, CallbackInfo ci, @Local PoseStack matrices) {
        LocalPlayer player = minecraft.player;

        if(player != null) {
            MobEffectInstance soulFireStrength = player.getEffect(IOEffects.SOUL_FIRE_STRENGTH.getRef());
            if(soulFireStrength != null && soulFireStrength.getAmplifier() >= 1) {
                submitFire(matrices, submitNodeCollector, this.sprites.get(ModModelLoader.SOUL_FIRE_1));
            }
            else if(player.hasEffect(IOEffects.SOUL_FIRE.getRef())) {
                submitFire(matrices, submitNodeCollector, this.sprites.get(ModModelLoader.SOUL_MAGE_FIRE_1));
            }
        }
    }

}
