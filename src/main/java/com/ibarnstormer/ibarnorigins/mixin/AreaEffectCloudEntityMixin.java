package com.ibarnstormer.ibarnorigins.mixin;

import com.ibarnstormer.ibarnorigins.effect.OwnableStatusEffectInstance;
import com.ibarnstormer.ibarnorigins.entity.IExtendedAECEntity;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityReference;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jspecify.annotations.Nullable;
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
    private @Nullable EntityReference<LivingEntity> owner;
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
    private void areaEffectCloudEntity$readAdditionalSaveData(ValueInput view, CallbackInfo ci) {
        this.assignsOwnableEffects = view.getBooleanOr("assignsOwnables", false);
    }

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    private void areaEffectCloudEntity$addAdditionalSaveData(ValueOutput view, CallbackInfo ci) {
        view.putBoolean("assignsOwnables", this.assignsOwnableEffects);
    }

    @Redirect(method = "serverTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;addEffect(Lnet/minecraft/world/effect/MobEffectInstance;Lnet/minecraft/world/entity/Entity;)Z"))
    private boolean areaEffectCloudEntity$serverTick(LivingEntity instance, MobEffectInstance effect, Entity source) {
        LivingEntity owner = EntityReference.getLivingEntity(this.owner, instance.level());
        if(this.assignsOwnableEffects && owner != null) {
            return instance.addEffect(new OwnableStatusEffectInstance(effect, owner.getUUID()), source);
        }
        else return instance.addEffect(new MobEffectInstance(effect), source);
    }
}
