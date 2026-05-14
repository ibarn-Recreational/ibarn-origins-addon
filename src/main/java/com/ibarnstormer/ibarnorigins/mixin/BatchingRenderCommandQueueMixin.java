package com.ibarnstormer.ibarnorigins.mixin;

import com.ibarnstormer.ibarnorigins.client.render.entity.state.SoulFireRenderState;
import com.ibarnstormer.ibarnorigins.client.render.entity.state.SoulMageFireRenderState;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.renderer.SubmitNodeCollection;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(SubmitNodeCollection.class)
public class BatchingRenderCommandQueueMixin {

    @ModifyArg(method = "submitFlame", at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z"))
    private <E> E batchingRenderCommandQueue$submitFlame(E e, @Local(argsOnly = true) EntityRenderState state) {

        if (e instanceof SoulMageFireRenderState.Command command) {
            SoulMageFireRenderState soulMageFireRenderState = state.getData(SoulMageFireRenderState.KEY);
            if (soulMageFireRenderState != null) {
                command.setRenderSoulMageFire(soulMageFireRenderState.renderSoulMageFire);
            }
        }

        if (e instanceof SoulFireRenderState.Command command) {
            SoulFireRenderState soulFireRenderState = state.getData(SoulFireRenderState.KEY);
            if (soulFireRenderState != null) {
                command.setRenderSoulFire(soulFireRenderState.renderSoulFire);
            }
        }

        return e;
    }

}
