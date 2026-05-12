package com.ibarnstormer.ibarnorigins.mixin;

import com.ibarnstormer.ibarnorigins.client.ModModelLoader;
import com.ibarnstormer.ibarnorigins.entity.IbarnOriginsEntity;
import com.ibarnstormer.ibarnorigins.registry.IOEffects;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameOverlayRenderer;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.SpriteHolder;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.registry.entry.RegistryEntry;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameOverlayRenderer.class)
public class InGameOverlayRendererMixin {

    @Shadow @Final
    private MinecraftClient client;
    @Shadow @Final
    private VertexConsumerProvider vertexConsumers;
    @Shadow @Final
    private SpriteHolder spriteHolder;

    @Shadow
    private static void renderFireOverlay(MatrixStack matrices, VertexConsumerProvider vertexConsumers, Sprite sprite) {}

    @Inject(method = "renderOverlays", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;isOnFire()Z", shift = At.Shift.BEFORE))
    private void inGameOverlayRenderer$renderOverlays(boolean sleeping, float tickProgress, OrderedRenderCommandQueue queue, CallbackInfo ci, @Local MatrixStack matrices) {
        ClientPlayerEntity player = client.player;

        if(player != null) {
            StatusEffectInstance soulFireStrength = player.getStatusEffect(IOEffects.SOUL_FIRE_STRENGTH.getRef());
            if(soulFireStrength != null && soulFireStrength.getAmplifier() >= 1) {
                renderFireOverlay(matrices, this.vertexConsumers, this.spriteHolder.getSprite(ModModelLoader.SOUL_FIRE_1));
            }
            else if(player.hasStatusEffect(IOEffects.SOUL_FIRE.getRef())) {
                renderFireOverlay(matrices, this.vertexConsumers, this.spriteHolder.getSprite(ModModelLoader.SOUL_MAGE_FIRE_1));
            }
        }
    }

}
