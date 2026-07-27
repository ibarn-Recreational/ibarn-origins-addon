package com.ibarnstormer.ibarnorigins.mixin;

import com.ibarnstormer.ibarnorigins.effect.OwnableStatusEffectInstance;
import com.ibarnstormer.ibarnorigins.entity.IExtendedAECEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AreaEffectCloud.class)
public class AreaEffectCloudEntityMixin implements IExtendedAECEntity {

    @Shadow
    private @Nullable LivingEntity owner;
    @Unique
    private boolean assignsOwnableEffects = false;

    @Override
    public boolean assignsOwnableEffects() {
        return this.assignsOwnableEffects;
    }

    @Override
    public void setToAssignOwnableEffects(boolean b) {
        this.assignsOwnableEffects = b;
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    private void areaEffectCloudEntity$readAdditionalSaveData(CompoundTag compound, CallbackInfo ci) {
        this.assignsOwnableEffects = compound.getBoolean("assignsOwnables");
    }

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    private void areaEffectCloudEntity$addAdditionalSaveData(CompoundTag compound, CallbackInfo ci) {
        compound.putBoolean("assignsOwnables", this.assignsOwnableEffects);
    }

    @Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;addEffect(Lnet/minecraft/world/effect/MobEffectInstance;Lnet/minecraft/world/entity/Entity;)Z"))
    private boolean areaEffectCloudEntity$serverTick(LivingEntity instance, MobEffectInstance effect, Entity source) {
        LivingEntity owner = this.owner;
        if(this.assignsOwnableEffects && owner != null) {
            return instance.addEffect(new OwnableStatusEffectInstance(effect, owner.getUUID()), source);
        }
        else return instance.addEffect(new MobEffectInstance(effect), source);
    }
}
