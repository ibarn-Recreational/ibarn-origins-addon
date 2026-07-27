package com.ibarnstormer.ibarnorigins.mixin;

import com.ibarnstormer.ibarnorigins.client.render.entity.state.InflationRenderState;
import com.ibarnstormer.ibarnorigins.entity.IbarnOriginsEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntityRenderer.class)
public class LivingEntityRendererMixin<T extends LivingEntity, S extends LivingEntityRenderState, M extends EntityModel<? super S>> {

    @Inject(method = "submit(Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/state/level/CameraRenderState;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/LivingEntityRenderer;scale(Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;)V"))
    public void livingEntityRenderer$render(S state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, net.minecraft.client.renderer.state.level.CameraRenderState camera, CallbackInfo ci) {
        InflationRenderState inflationRenderState =  state.getData(InflationRenderState.KEY);
        if(inflationRenderState != null && inflationRenderState.renderInflated) {
            poseStack.scale(1.15F, 1.05F, 1.15F);
        }
    }

    @Inject(method = "extractRenderState(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;F)V", at = @At("TAIL"))
    private void livingEntityRenderer$updateRenderState(T livingEntity, S livingEntityRenderState, float f, CallbackInfo ci) {
        livingEntityRenderState.isFullyFrozen = livingEntityRenderState.isFullyFrozen || (livingEntity instanceof IbarnOriginsEntity ioe && (ioe.fireWeaknessShaking() || (ioe.isSandPerson() && !livingEntity.level().getBlockState(livingEntity.blockPosition()).is(BlockTags.SAND) && !livingEntity.level().getBlockState(livingEntity.blockPosition().below()).is(BlockTags.SAND))));
    }

}
