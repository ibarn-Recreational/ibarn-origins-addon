package com.ibarnstormer.ibarnorigins.mixin;

import com.ibarnstormer.ibarnorigins.client.render.entity.state.SoulFireRenderState;
import com.ibarnstormer.ibarnorigins.client.render.entity.state.SoulMageFireRenderState;
import net.minecraft.client.renderer.SubmitNodeStorage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(SubmitNodeStorage.FlameSubmit.class)
public class FireCommandMixin implements SoulFireRenderState.Command, SoulMageFireRenderState.Command {

    @Unique
    private boolean renderSoulMageFire = false;
    @Unique
    private boolean renderSoulFire = false;

    @Override
    public boolean renderSoulFire() {
        return renderSoulFire;
    }

    @Override
    public void setRenderSoulFire(boolean renderFire) {
        this.renderSoulFire = renderFire;
    }

    @Override
    public boolean renderSoulMageFire() {
        return renderSoulMageFire;
    }

    @Override
    public void setRenderSoulMageFire(boolean renderFire) {
        this.renderSoulMageFire = renderFire;
    }
}
