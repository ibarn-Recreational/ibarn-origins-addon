package com.ibarnstormer.ibarnorigins.client.render.entity.state;

import net.fabricmc.fabric.api.client.rendering.v1.RenderStateDataKey;

public class SoulMageFireRenderState {

    public static final RenderStateDataKey<SoulMageFireRenderState> KEY = RenderStateDataKey.create(() -> "soul_mage_fire");

    public boolean renderSoulMageFire = false;

    public interface Command {
        boolean renderSoulMageFire();

        void setRenderSoulMageFire(boolean renderFire);
    }
}
