package com.ibarnstormer.ibarnorigins.client.render.entity.state;

import net.fabricmc.fabric.api.client.rendering.v1.RenderStateDataKey;

public class SoulFireRenderState {

    public static final RenderStateDataKey<SoulFireRenderState> KEY = RenderStateDataKey.create(() -> "soul_fire");

    public boolean renderSoulFire = false;

    public interface Command {
        boolean renderSoulFire();

        void setRenderSoulFire(boolean renderFire);
    }

}
