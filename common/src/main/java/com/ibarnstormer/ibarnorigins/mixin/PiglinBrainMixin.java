package com.ibarnstormer.ibarnorigins.mixin;

import com.google.common.collect.ImmutableList;
import com.ibarnstormer.ibarnorigins.entity.IbarnOriginsEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.MemoryTransferTask;
import net.minecraft.entity.ai.brain.task.Task;
import net.minecraft.entity.mob.PiglinBrain;
import net.minecraft.entity.mob.PiglinEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(PiglinBrain.class)
public class PiglinBrainMixin {

    @Shadow @Final
    private static UniformIntProvider AVOID_MEMORY_DURATION;

    @ModifyArg(method = "addCoreActivities", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/ai/brain/Brain;setTaskList(Lnet/minecraft/entity/ai/brain/Activity;ILcom/google/common/collect/ImmutableList;)V"))
    private static <E extends Task<?>> ImmutableList<E> piglinBrain$addCoreActivities(ImmutableList<E> list) {
        ImmutableList.Builder<E> builder = new ImmutableList.Builder<>();
        builder.addAll(list).add((E) makeFleeFromSoulMageTask());
        return builder.build();
    }

    @Inject(method = "getPreferredTarget", at = @At("RETURN"), cancellable = true)
    private static void piglinBrain$getPreferredTarget(PiglinEntity piglin, CallbackInfoReturnable<Optional<? extends LivingEntity>> cir) {
        if(cir.getReturnValue().isPresent()) {
            LivingEntity entity = cir.getReturnValue().get();
            if(entity instanceof IbarnOriginsEntity ioe && ioe.isSoulMage()) {
                cir.setReturnValue(Optional.empty());
            }
        }
    }

    @Unique
    private static Task<PiglinEntity> makeFleeFromSoulMageTask() {
        return MemoryTransferTask.create(PiglinBrainMixin::getNearestSoulMage, MemoryModuleType.NEAREST_VISIBLE_PLAYER, MemoryModuleType.AVOID_TARGET, AVOID_MEMORY_DURATION);
    }

    @Unique
    private static boolean getNearestSoulMage(PiglinEntity piglin) {
        Brain<PiglinEntity> brain = piglin.getBrain();
        if (brain.hasMemoryModule(MemoryModuleType.NEAREST_VISIBLE_PLAYER)) {
            PlayerEntity player = brain.getOptionalRegisteredMemory(MemoryModuleType.NEAREST_VISIBLE_PLAYER).get();
            return piglin.isInRange(player, 7.0) && (player instanceof IbarnOriginsEntity ioe && ioe.isSoulMage() && !player.isCreative() && !player.isSpectator());
        } else {
            return false;
        }
    }

}
