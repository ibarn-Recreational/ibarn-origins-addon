package com.ibarnstormer.ibarnorigins.mixin;

import net.minecraft.world.entity.AreaEffectCloud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(AreaEffectCloud.class)
public interface AreaEffectCloudAccessor {

    @Accessor("reapplicationDelay")
    void setReapplicationDelay(int i);

}
