package com.ibarnstormer.ibarnorigins.mixin;

import com.ibarnstormer.ibarnorigins.entity.IbarnOriginsEntity;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityRenderer.class)
public class EntityRendererMixin {

    @Inject(method = "getBlockLightLevel", at = @At("RETURN"), cancellable = true)
    public <T extends Entity> void entityRenderer$getBlockLight(T entity, BlockPos pos, CallbackInfoReturnable<Integer> cir) {
        if(entity instanceof IbarnOriginsEntity ioe) {
            cir.setReturnValue(ioe.onSoulMageFire() || ioe.onSoulFire() ? 15 : cir.getReturnValueI());
        }
    }

}
