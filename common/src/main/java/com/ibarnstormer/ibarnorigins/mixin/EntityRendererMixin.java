package com.ibarnstormer.ibarnorigins.mixin;

import com.ibarnstormer.ibarnorigins.entity.IbarnOriginsEntity;
import com.ibarnstormer.ibarnorigins.registry.IOEffects;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityRenderer.class)
public class EntityRendererMixin {

    @Inject(method = "getBlockLight", at = @At("RETURN"), cancellable = true)
    public <T extends Entity> void entityRenderer$getBlockLight(T entity, BlockPos pos, CallbackInfoReturnable<Integer> cir) {
        if(entity instanceof IbarnOriginsEntity ioe) {
            boolean onSoulFire = false;
            if(entity instanceof LivingEntity living) {
                StatusEffectInstance soulStrength = living.getStatusEffect(IOEffects.SOUL_FIRE_STRENGTH.get());
                if(soulStrength != null) {
                    onSoulFire = soulStrength.getAmplifier() >= 1;
                }
            }
            cir.setReturnValue(ioe.onSoulMageFire() || onSoulFire ? 15 : cir.getReturnValueI());
        }
    }

}
