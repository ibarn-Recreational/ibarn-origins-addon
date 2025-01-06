package com.ibarnstormer.ibarnorigins.mixin;

import com.ibarnstormer.ibarnorigins.entity.IbarnOriginsEntity;
import com.ibarnstormer.ibarnorigins.registry.IOEffects;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.registry.tag.BlockTags;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntityRenderer.class)
public class LivingEntityRendererMixin {

    @Inject(method = "isShaking", at = @At("RETURN"), cancellable = true)
    protected <T extends LivingEntity> void livingEntityRenderer$isShaking(T entity, CallbackInfoReturnable<Boolean> cir) {
        if(entity != null && ((entity.hasStatusEffect(IOEffects.FIRE_WEAKNESS.get()) || (entity instanceof IbarnOriginsEntity ioe && ioe.isSandPerson() && !entity.getWorld().getBlockState(entity.getBlockPos()).isIn(BlockTags.SAND) && !entity.getWorld().getBlockState(entity.getBlockPos().down()).isIn(BlockTags.SAND))))) {
            cir.setReturnValue(true);
        }
    }

}
