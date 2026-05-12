package com.ibarnstormer.ibarnorigins.client.render.entity.state;

import net.fabricmc.fabric.api.client.rendering.v1.RenderStateDataKey;

public class InflationRenderState {

    public static final RenderStateDataKey<InflationRenderState> KEY = RenderStateDataKey.create(() -> "inflation");

    public boolean renderInflated = false;

}
