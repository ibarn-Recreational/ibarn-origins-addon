package com.ibarnstormer.ibarnorigins.mixin;

import com.ibarnstormer.ibarnorigins.entity.IbarnOriginsEntity;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayer.class)
public class ServerPlayerEntityMixin {

    @Inject(method = "restoreFrom", at = @At("TAIL"))
    public void serverPlayerEntity$copyFrom(ServerPlayer oldPlayer, boolean alive, CallbackInfo ci) {
        ServerPlayer newPlayer = (ServerPlayer) (Object) this;
        if(newPlayer instanceof IbarnOriginsEntity newIOE && oldPlayer instanceof IbarnOriginsEntity oldIOE) {
            newIOE.setSandPerson(oldIOE.isSandPerson());
            newIOE.setSoulMage(oldIOE.isSoulMage());
            if(!oldPlayer.isDeadOrDying()) newIOE.setOnSoulMageFire(oldIOE.onSoulMageFire());
            if(!oldPlayer.isDeadOrDying()) newIOE.setOnSoulFire(oldIOE.onSoulFire());
            if(!oldPlayer.isDeadOrDying()) newIOE.setInflated(oldIOE.inflated());
            newIOE.setShakingFromFireWeakness(oldIOE.fireWeaknessShaking());
        }
    }

}
