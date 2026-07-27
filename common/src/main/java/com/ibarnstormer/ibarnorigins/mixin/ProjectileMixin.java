package com.ibarnstormer.ibarnorigins.mixin;

import com.ibarnstormer.ibarnorigins.entity.IExtendedProjectileEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Projectile.class)
public class ProjectileMixin implements IExtendedProjectileEntity {

    @Unique
    private boolean ignoresOwnerRelativeMovement = false;

    @Override
    public boolean ignoresOwnerRelativeMovement() {
        return this.ignoresOwnerRelativeMovement;
    }

    @Override
    public void setIgnoreOwnerRelativeMovement(boolean b) {
        this.ignoresOwnerRelativeMovement = b;
    }

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    private void projectile$addAdditionalSaveData(CompoundTag compound, CallbackInfo ci) {
        compound.putBoolean("ignoresOwnerRelativeMovement", this.ignoresOwnerRelativeMovement);
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    private void projectile$readAdditionalSaveData(CompoundTag compound, CallbackInfo ci) {
        this.ignoresOwnerRelativeMovement = compound.getBoolean("ignoresOwnerRelativeMovement");
    }

    @Redirect(method = "shootFromRotation", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;getKnownMovement()Lnet/minecraft/world/phys/Vec3;"))
    private Vec3 projectile$shootFromRotation(Entity instance) {
        return this.ignoresOwnerRelativeMovement ? Vec3.ZERO : instance.getKnownMovement();
    }
}
