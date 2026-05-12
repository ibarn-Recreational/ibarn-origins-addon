package com.ibarnstormer.ibarnorigins.client.render.entity.state;

import net.fabricmc.fabric.api.client.rendering.v1.RenderStateDataKey;

public class SoulMageSpellCastState {

    public static final RenderStateDataKey<SoulMageSpellCastState> KEY = RenderStateDataKey.create(() -> "soul_mage_spell_cast");

    public boolean renderSpellCast = false;
}
