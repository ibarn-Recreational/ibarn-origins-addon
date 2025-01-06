package com.ibarnstormer.ibarnorigins.mixin;

import com.ibarnstormer.ibarnorigins.entity.IbarnOriginsEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public class ServerPlayerEntityMixin {

    @Inject(method = "copyFrom", at = @At("TAIL"))
    public void serverPlayerEntity$copyFrom(ServerPlayerEntity oldPlayer, boolean alive, CallbackInfo ci) {
        ServerPlayerEntity newPlayer = (ServerPlayerEntity) (Object) this;
        if(newPlayer instanceof IbarnOriginsEntity newIOE && oldPlayer instanceof IbarnOriginsEntity oldIOE) {
            if(oldIOE.isSandPerson()) newIOE.setSandPerson(true);
            if(oldIOE.isSoulMage()) newIOE.setSoulMage(true);
        }
    }

}
