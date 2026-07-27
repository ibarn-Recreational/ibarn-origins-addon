package com.ibarnstormer.ibarnorigins.mixin;

import com.ibarnstormer.ibarnorigins.entity.IbarnOriginsEntity;
import com.ibarnstormer.ibarnorigins.registry.IOEffects;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntityRenderer.class)
public class LivingEntityRendererMixin {

    @Inject(method = "isShaking", at = @At("RETURN"), cancellable = true)
    protected <T extends LivingEntity> void livingEntityRenderer$isShaking(T entity, CallbackInfoReturnable<Boolean> cir) {
        if(entity != null && (((entity instanceof IbarnOriginsEntity ioe && ioe.fireWeaknessShaking()) || (entity instanceof IbarnOriginsEntity ioe2 && ioe2.isSandPerson() && !entity.level().getBlockState(entity.blockPosition()).is(BlockTags.SAND) && !entity.level().getBlockState(entity.blockPosition().below()).is(BlockTags.SAND))))) {
            cir.setReturnValue(true);
        }
    }

    @Inject(method = "render(Lnet/minecraft/world/entity/LivingEntity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/PoseStack;scale(FFF)V"))
    public <T extends LivingEntity> void livingEntityRenderer$render(T entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight, CallbackInfo ci) {
        if(entity instanceof IbarnOriginsEntity ioe && ioe.inflated()) {
            poseStack.scale(1.15F, 1.05F, 1.15F);
        }
    }

}
