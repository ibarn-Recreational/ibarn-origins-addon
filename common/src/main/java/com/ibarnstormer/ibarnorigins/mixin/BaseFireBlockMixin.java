package com.ibarnstormer.ibarnorigins.mixin;

import com.ibarnstormer.ibarnorigins.entity.IbarnOriginsEntity;
import com.ibarnstormer.ibarnorigins.registry.IOEffects;
import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BaseFireBlock.class)
public class BaseFireBlockMixin extends Block {

    public BaseFireBlockMixin(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Inject(method = "entityInside", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;hurt(Lnet/minecraft/world/damagesource/DamageSource;F)Z", shift = At.Shift.BEFORE), cancellable = true)
    public void abstractFireBlock$onEntityCollision(BlockState state, Level level, BlockPos pos, Entity entity, CallbackInfo ci) {
        if(entity instanceof IbarnOriginsEntity ioe && ioe.isSoulMage() && state.is(Blocks.SOUL_FIRE)) {
            if(entity instanceof LivingEntity livingEntity) livingEntity.addEffect(new MobEffectInstance(IOEffects.SOUL_FIRE_STRENGTH.getRef(), 60, 1, true, false, true));
            entity.setRemainingFireTicks(0);
            entity.setSharedFlagOnFire(false);

            super.entityInside(state, level, pos, entity);
            ci.cancel();
        }
    }


}
