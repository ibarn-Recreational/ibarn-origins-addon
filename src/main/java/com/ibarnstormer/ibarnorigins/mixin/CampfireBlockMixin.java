package com.ibarnstormer.ibarnorigins.mixin;

import com.ibarnstormer.ibarnorigins.entity.IbarnOriginsEntity;
import com.ibarnstormer.ibarnorigins.registry.IOEffects;
import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.InsideBlockEffectApplier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CampfireBlock.class)
public class CampfireBlockMixin extends Block {

    public CampfireBlockMixin(Properties settings) {
        super(settings);
    }

    @Inject(method = "entityInside", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;hurt(Lnet/minecraft/world/damagesource/DamageSource;F)V", shift = At.Shift.BEFORE), cancellable = true)
    public void campFireBlock$entityInside(BlockState state, Level world, BlockPos pos, Entity entity, InsideBlockEffectApplier handler, boolean bl, CallbackInfo ci) {
        if(entity instanceof IbarnOriginsEntity ioe && ioe.isSoulMage() && state.is(Blocks.SOUL_CAMPFIRE)) {
            if(entity instanceof LivingEntity livingEntity) livingEntity.addEffect(new MobEffectInstance(IOEffects.SOUL_FIRE_STRENGTH.getRef(), 20, 1, true, false, true));

            super.entityInside(state, world, pos, entity, handler, bl);
            ci.cancel();
        }
    }

}
