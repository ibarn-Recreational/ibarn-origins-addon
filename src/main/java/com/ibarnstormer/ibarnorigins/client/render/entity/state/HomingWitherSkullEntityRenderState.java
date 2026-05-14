package com.ibarnstormer.ibarnorigins.client.render.entity.state;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.object.skull.SkullModelBase;
import net.minecraft.client.renderer.entity.state.EntityRenderState;

@Environment(EnvType.CLIENT)
public class HomingWitherSkullEntityRenderState extends EntityRenderState {
    public boolean charged;
    public final SkullModelBase.State skullState = new SkullModelBase.State();
}