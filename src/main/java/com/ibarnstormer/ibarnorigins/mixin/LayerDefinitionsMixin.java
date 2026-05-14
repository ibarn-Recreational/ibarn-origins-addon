package com.ibarnstormer.ibarnorigins.mixin;

import com.google.common.collect.ImmutableMap;
import com.ibarnstormer.ibarnorigins.client.ModModelLoader;
import com.ibarnstormer.ibarnorigins.client.render.HomingWitherSkullEntityRenderer;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.model.geom.LayerDefinitions;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;

@Mixin(LayerDefinitions.class)
public class LayerDefinitionsMixin {

    @Inject(method = "createRoots", at = @At(value = "INVOKE", target = "Lcom/google/common/collect/ImmutableMap$Builder;build()Lcom/google/common/collect/ImmutableMap;", shift = At.Shift.BEFORE))
    private static void layerDefinitions$createRoots(CallbackInfoReturnable<Map<ModelLayerLocation, LayerDefinition>> cir, @Local(name = "result") ImmutableMap.Builder<ModelLayerLocation, LayerDefinition> result) {
        result.put(ModModelLoader.HOMING_WS_ML, HomingWitherSkullEntityRenderer.createSkullLayer());
    }

}
