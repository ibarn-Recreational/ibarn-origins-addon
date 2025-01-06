package com.ibarnstormer.ibarnorigins.mixin;

import com.ibarnstormer.ibarnorigins.entity.IbarnOriginsEntity;
import com.ibarnstormer.ibarnorigins.registry.IOEffects;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CampfireBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CampfireBlock.class)
public class CampfireBlockMixin extends Block {

    public CampfireBlockMixin(Settings settings) {
        super(settings);
    }

    @Inject(method = "onEntityCollision", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;damage(Lnet/minecraft/entity/damage/DamageSource;F)Z", shift = At.Shift.BEFORE), cancellable = true)
    public void campFireBlock$onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity, CallbackInfo ci) {
        if(entity instanceof IbarnOriginsEntity ioe && ioe.isSoulMage() && state.isOf(Blocks.SOUL_CAMPFIRE)) {
            if(entity instanceof LivingEntity livingEntity) livingEntity.addStatusEffect(new StatusEffectInstance(IOEffects.SOUL_FIRE_STRENGTH.get(), 20, 1, true, false, false));

            super.onEntityCollision(state, world, pos, entity);
            ci.cancel();
        }
    }

}
