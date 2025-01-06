package com.ibarnstormer.ibarnorigins.mixin;

import com.ibarnstormer.ibarnorigins.entity.IbarnOriginsEntity;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BipedEntityModel.class)
public class BipedEntityModelMixin<T extends LivingEntity> {

    @Shadow @Final
    public ModelPart rightArm;
    @Shadow @Final
    public ModelPart leftArm;

    @Inject(method = "setAngles(Lnet/minecraft/entity/LivingEntity;FFFFF)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/ModelPart;copyTransform(Lnet/minecraft/client/model/ModelPart;)V", shift = At.Shift.BEFORE))
    private void BipedEntityModel$setAngles(T livingEntity, float f, float g, float h, float i, float j, CallbackInfo ci) {
        if(livingEntity instanceof IbarnOriginsEntity spellCaster && spellCaster.getSpellCastTicks() > 0) {
            this.rightArm.pivotZ = 0.0F;
            this.rightArm.pivotX = -5.0F;
            this.leftArm.pivotZ = 0.0F;
            this.leftArm.pivotX = 5.0F;
            this.rightArm.pitch = MathHelper.cos(h * 0.6662F) * 0.25F;
            this.leftArm.pitch = MathHelper.cos(h * 0.6662F) * 0.25F;
            this.rightArm.roll = 2.3561945F;
            this.leftArm.roll = -2.3561945F;
            this.rightArm.yaw = 0.0F;
            this.leftArm.yaw = 0.0F;
        }

    }

}
