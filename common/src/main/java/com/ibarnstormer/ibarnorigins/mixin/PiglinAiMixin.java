package com.ibarnstormer.ibarnorigins.mixin;

import com.google.common.collect.ImmutableList;
import com.ibarnstormer.ibarnorigins.entity.IbarnOriginsEntity;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.BehaviorControl;
import net.minecraft.world.entity.ai.behavior.CopyMemoryWithExpiry;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.monster.piglin.Piglin;
import net.minecraft.world.entity.monster.piglin.PiglinAi;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(PiglinAi.class)
public class PiglinAiMixin {

    @Shadow @Final
    private static UniformInt AVOID_ZOMBIFIED_DURATION;

    @ModifyArg(method = "initCoreActivity", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/ai/Brain;addActivity(Lnet/minecraft/world/entity/schedule/Activity;ILcom/google/common/collect/ImmutableList;)V"))
    private static <E extends BehaviorControl<?>> ImmutableList<E> piglinAi$initCoreActivity(ImmutableList<E> list) {
        ImmutableList.Builder<E> builder = new ImmutableList.Builder<>();
        builder.addAll(list).add((E) makeFleeFromSoulMageTask());
        return builder.build();
    }

    @Inject(method = "findNearestValidAttackTarget", at = @At("RETURN"), cancellable = true)
    private static void piglinAi$findNearestValidAttackTarget(Piglin piglin, CallbackInfoReturnable<Optional<? extends LivingEntity>> cir) {
        if(cir.getReturnValue().isPresent()) {
            LivingEntity entity = cir.getReturnValue().get();
            if(entity instanceof IbarnOriginsEntity ioe && ioe.isSoulMage()) {
                cir.setReturnValue(Optional.empty());
            }
        }
    }

    @Unique
    private static BehaviorControl<Piglin> makeFleeFromSoulMageTask() {
        return CopyMemoryWithExpiry.create(PiglinAiMixin::getNearestSoulMage, MemoryModuleType.NEAREST_VISIBLE_PLAYER, MemoryModuleType.AVOID_TARGET, AVOID_ZOMBIFIED_DURATION);
    }

    @Unique
    private static boolean getNearestSoulMage(Piglin piglin) {
        Brain<Piglin> brain = piglin.getBrain();
        if (brain.hasMemoryValue(MemoryModuleType.NEAREST_VISIBLE_PLAYER)) {
            Player player = brain.getMemory(MemoryModuleType.NEAREST_VISIBLE_PLAYER).get();
            return piglin.closerThan(player, 7.0) && (player instanceof IbarnOriginsEntity ioe && ioe.isSoulMage() && !player.isCreative() && !player.isSpectator());
        } else {
            return false;
        }
    }

}
