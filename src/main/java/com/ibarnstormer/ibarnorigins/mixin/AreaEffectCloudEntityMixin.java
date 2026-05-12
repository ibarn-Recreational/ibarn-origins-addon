package com.ibarnstormer.ibarnorigins.mixin;

import com.ibarnstormer.ibarnorigins.effect.OwnableStatusEffectInstance;
import com.ibarnstormer.ibarnorigins.entity.IExtendedAECEntity;
import net.minecraft.entity.AreaEffectCloudEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LazyEntityReference;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AreaEffectCloudEntity.class)
public class AreaEffectCloudEntityMixin implements IExtendedAECEntity {

    @Shadow
    private @Nullable LazyEntityReference<LivingEntity> owner;
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

    @Inject(method = "readCustomData", at = @At("TAIL"))
    private void areaEffectCloudEntity$readCustomData(ReadView view, CallbackInfo ci) {
        this.assignsOwnableEffects = view.getBoolean("assignsOwnables", false);
    }

    @Inject(method = "writeCustomData", at = @At("TAIL"))
    private void areaEffectCloudEntity$writeCustomData(WriteView view, CallbackInfo ci) {
        view.putBoolean("assignsOwnables", this.assignsOwnableEffects);
    }

    @Redirect(method = "serverTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;addStatusEffect(Lnet/minecraft/entity/effect/StatusEffectInstance;Lnet/minecraft/entity/Entity;)Z"))
    private boolean areaEffectCloudEntity$serverTick(LivingEntity instance, StatusEffectInstance effect, Entity source) {
        LivingEntity owner = LazyEntityReference.getLivingEntity(this.owner, instance.getEntityWorld());
        if(this.assignsOwnableEffects && owner != null) {
            return instance.addStatusEffect(new OwnableStatusEffectInstance(effect, owner.getUuid()), source);
        }
        else return instance.addStatusEffect(new StatusEffectInstance(effect), source);
    }
}
