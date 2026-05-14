package com.ibarnstormer.ibarnorigins.mixin;

import com.ibarnstormer.ibarnorigins.entity.IbarnOriginsEntity;
import com.ibarnstormer.ibarnorigins.registry.IOEffects;
import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.InsideBlockEffectApplier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BaseFireBlock.class)
public class AbstractFireBlockMixin extends Block {

    public AbstractFireBlockMixin(Properties settings) {
        super(settings);
    }

    @Inject(method = "entityInside", at = @At(value = "HEAD"), cancellable = true)
    public void abstractFireBlock$entityInside(BlockState state, Level world, BlockPos pos, Entity entity, InsideBlockEffectApplier handler, boolean bl, CallbackInfo ci) {
        if(entity instanceof IbarnOriginsEntity ioe && ioe.isSoulMage() && state.is(Blocks.SOUL_FIRE)) {
            if(entity instanceof LivingEntity livingEntity) livingEntity.addEffect(new MobEffectInstance(IOEffects.SOUL_FIRE_STRENGTH.getRef(), 60, 1, true, false, true));
            entity.setRemainingFireTicks(0);
            entity.setSharedFlagOnFire(false);

            ci.cancel();
        }
    }


}
