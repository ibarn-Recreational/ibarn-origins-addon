package com.ibarnstormer.ibarnorigins.effect;

import com.ibarnstormer.ibarnorigins.IbarnOriginsMain;
import com.ibarnstormer.ibarnorigins.entity.IbarnOriginsEntity;
import com.ibarnstormer.ibarnorigins.entity.SoulFireBallEntity;
import com.ibarnstormer.ibarnorigins.registry.IOEffects;
import com.ibarnstormer.ibarnorigins.registry.IOSounds;
import java.util.UUID;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class SoulFireStrengthEffect extends MobEffect implements IExtendedStatusEffect {

    private final Identifier ID = IbarnOriginsMain.IOIdentifier("soul_fire_strength");

    public SoulFireStrengthEffect() {
        super(MobEffectCategory.BENEFICIAL, 0x1f70f2);
    }

    @Override
    public void onEffectStarted(LivingEntity entity, int amplifier) {
        AttributeInstance damage = entity.getAttribute(Attributes.ATTACK_DAMAGE);

        if(entity.hasEffect(IOEffects.SOUL_FIRE_STRENGTH.getRef())) {
            MobEffectInstance current = entity.getEffect(IOEffects.SOUL_FIRE_STRENGTH.getRef());
            if(current.getAmplifier() < amplifier) entity.removeEffect(IOEffects.SOUL_FIRE_STRENGTH.getRef());
        }

        AttributeModifier modifier = new AttributeModifier(ID, Math.min(0.5D + (double) amplifier / 2.0D, 1.0D), AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);

        if(damage != null && damage.getModifier(ID) == null) damage.addTransientModifier(modifier);

        if(amplifier >= 1 && entity instanceof IbarnOriginsEntity ioe) {
            ioe.setOnSoulFire(true);
        }
    }

    @Override
    public void onStatusEffectRemoved(ServerLevel world, LivingEntity entity, int amplifier) {
        AttributeInstance damage = entity.getAttribute(Attributes.ATTACK_DAMAGE);
        if (damage != null && damage.getModifier(ID) != null)
            damage.removeModifier(ID);

        if(amplifier >= 1 && entity instanceof IbarnOriginsEntity ioe) {
            ioe.setOnSoulFire(false);
        }
    }
}
