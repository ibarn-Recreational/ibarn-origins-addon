package com.ibarnstormer.ibarnorigins.mixin;

import com.ibarnstormer.ibarnorigins.client.render.entity.state.InflationRenderState;
import com.ibarnstormer.ibarnorigins.client.render.entity.state.SoulFireRenderState;
import com.ibarnstormer.ibarnorigins.client.render.entity.state.SoulMageFireRenderState;
import com.ibarnstormer.ibarnorigins.client.render.entity.state.SoulMageSpellCastState;
import com.ibarnstormer.ibarnorigins.entity.IbarnOriginsEntity;
import com.ibarnstormer.ibarnorigins.entity.SoulFireBallEntity;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityRenderer.class)
public class EntityRendererMixin<T extends Entity, S extends EntityRenderState> {

    @Inject(method = "getBlockLightLevel", at = @At("RETURN"), cancellable = true)
    public <T extends Entity> void entityRenderer$getBlockLightLevel(T entity, BlockPos pos, CallbackInfoReturnable<Integer> cir) {
        if(entity instanceof IbarnOriginsEntity ioe) {
            cir.setReturnValue(ioe.onSoulMageFire() || ioe.onSoulFire() ? 15 : cir.getReturnValueI());
        }
    }

    @Inject(method = "extractRenderState", at = @At("TAIL"))
    private void entityRenderer$extractRenderState(T entity, S state, float tickProgress, CallbackInfo ci) {
        SoulFireRenderState soulFireRenderState = new SoulFireRenderState();
        SoulMageFireRenderState soulMageFireRenderState = new SoulMageFireRenderState();
        SoulMageSpellCastState soulMageSpellCastState = new SoulMageSpellCastState();
        InflationRenderState inflationRenderState = new InflationRenderState();

        soulFireRenderState.renderSoulFire = entity instanceof IbarnOriginsEntity ioe && ioe.onSoulFire();
        soulMageFireRenderState.renderSoulMageFire = (entity instanceof IbarnOriginsEntity ioe && ioe.onSoulMageFire()) || entity instanceof SoulFireBallEntity;
        inflationRenderState.renderInflated = entity instanceof IbarnOriginsEntity ioe && ioe.inflated();
        soulMageSpellCastState.renderSpellCast = entity instanceof IbarnOriginsEntity ioe && ioe.getSoulFireballChargeTicks() > 0;

        state.setData(SoulMageFireRenderState.KEY, soulMageFireRenderState);
        state.setData(SoulMageSpellCastState.KEY, soulMageSpellCastState);
        state.setData(SoulFireRenderState.KEY, soulFireRenderState);
        state.setData(InflationRenderState.KEY, inflationRenderState);

        state.displayFireAnimation = state.displayFireAnimation || soulFireRenderState.renderSoulFire || soulMageFireRenderState.renderSoulMageFire;
    }

}
