package com.ibarnstormer.ibarnorigins.mixin;

import com.google.common.collect.ImmutableList;
import com.ibarnstormer.ibarnorigins.entity.IbarnOriginsEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.BehaviorControl;
import net.minecraft.world.entity.ai.behavior.CopyMemoryWithExpiry;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.monster.piglin.Piglin;
import net.minecraft.world.entity.monster.piglin.PiglinAi;
import net.minecraft.world.entity.player.Player;

@Mixin(PiglinAi.class)
public class PiglinBrainMixin {

    @Shadow @Final
    private static UniformInt RETREAT_DURATION;

    @ModifyArg(method = "initCoreActivity", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/ai/ActivityData;create(Lnet/minecraft/world/entity/schedule/Activity;ILcom/google/common/collect/ImmutableList;)Lnet/minecraft/world/entity/ai/ActivityData;"))
    private static <E extends BehaviorControl<?>> ImmutableList<E> piglinBrain$initCoreActivity(ImmutableList<E> list) {
        ImmutableList.Builder<E> builder = new ImmutableList.Builder<>();
        builder.addAll(list).add((E) makeFleeFromSoulMageTask());
        return builder.build();
    }

    @Inject(method = "findNearestValidAttackTarget", at = @At("RETURN"), cancellable = true)
    private static void piglinBrain$findNearestValidAttackTarget(ServerLevel world, Piglin piglin, CallbackInfoReturnable<Optional<? extends LivingEntity>> cir) {
        if(cir.getReturnValue().isPresent()) {
            LivingEntity entity = cir.getReturnValue().get();
            if(entity instanceof IbarnOriginsEntity ioe && ioe.isSoulMage()) {
                cir.setReturnValue(Optional.empty());
            }
        }
    }

    @Unique
    private static BehaviorControl<Piglin> makeFleeFromSoulMageTask() {
        return CopyMemoryWithExpiry.create(PiglinBrainMixin::getNearestSoulMage, MemoryModuleType.NEAREST_VISIBLE_PLAYER, MemoryModuleType.AVOID_TARGET, RETREAT_DURATION);
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
