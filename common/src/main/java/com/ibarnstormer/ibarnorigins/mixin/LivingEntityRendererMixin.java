package com.ibarnstormer.ibarnorigins.mixin;

import com.ibarnstormer.ibarnorigins.entity.IbarnOriginsEntity;
import com.ibarnstormer.ibarnorigins.registry.IOEffects;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.tag.BlockTags;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntityRenderer.class)
public class LivingEntityRendererMixin {

    @Inject(method = "isShaking", at = @At("RETURN"), cancellable = true)
    protected <T extends LivingEntity> void livingEntityRenderer$isShaking(T entity, CallbackInfoReturnable<Boolean> cir) {
        if(entity != null && ((entity.hasStatusEffect(IOEffects.FIRE_WEAKNESS.get()) || (entity instanceof IbarnOriginsEntity ioe && ioe.isSandPerson() && !entity.getWorld().getBlockState(entity.getBlockPos()).isIn(BlockTags.SAND) && !entity.getWorld().getBlockState(entity.getBlockPos().down()).isIn(BlockTags.SAND))))) {
            cir.setReturnValue(true);
        }
    }

    @Inject(method = "render(Lnet/minecraft/entity/LivingEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/LivingEntityRenderer;scale(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/client/util/math/MatrixStack;F)V"))
    public <T extends LivingEntity> void livingEntityRenderer$render(T livingEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, CallbackInfo ci) {
        if(livingEntity instanceof PlayerEntity) {
            if (livingEntity.hasStatusEffect(IOEffects.INFLATION.get())) {
                matrixStack.scale(1.15F, 1.05F, 1.15F);
            }
        }
    }

}
