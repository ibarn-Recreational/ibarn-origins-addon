package com.ibarnstormer.ibarnorigins.mixin;

import com.ibarnstormer.ibarnorigins.client.render.entity.state.SoulMageSpellCastState;
import com.ibarnstormer.ibarnorigins.entity.IbarnOriginsEntity;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.state.BipedEntityRenderState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BipedEntityModel.class)
public class BipedEntityModelMixin<T extends BipedEntityRenderState> {

    @Shadow @Final
    public ModelPart rightArm;
    @Shadow @Final
    public ModelPart leftArm;

    @Inject(method = "setAngles(Lnet/minecraft/client/render/entity/state/BipedEntityRenderState;)V", at = @At(value = "TAIL"))
    private void BipedEntityModel$setAngles(T bipedEntityRenderState, CallbackInfo ci) {
        SoulMageSpellCastState spellCastState = bipedEntityRenderState.getData(SoulMageSpellCastState.KEY);
        if(spellCastState != null && spellCastState.renderSpellCast) {
            this.rightArm.originZ = 0.0F;
            this.rightArm.originX = -5.0F;
            this.leftArm.originZ = 0.0F;
            this.leftArm.originX = 5.0F;
            this.rightArm.pitch = MathHelper.cos(bipedEntityRenderState.age * 0.6662F) * 0.25F;
            this.leftArm.pitch = MathHelper.cos(bipedEntityRenderState.age * 0.6662F) * 0.25F;
            this.rightArm.roll = 2.3561945F;
            this.leftArm.roll = -2.3561945F;
            this.rightArm.yaw = 0.0F;
            this.leftArm.yaw = 0.0F;
        }

    }

}
