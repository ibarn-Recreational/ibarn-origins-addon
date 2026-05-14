package com.ibarnstormer.ibarnorigins.effect;

import com.ibarnstormer.ibarnorigins.IbarnOriginsMain;
import com.ibarnstormer.ibarnorigins.entity.IbarnOriginsEntity;
import com.ibarnstormer.ibarnorigins.registry.IOEffects;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class FireWeaknessEffect extends MobEffect implements IExtendedStatusEffect {

    private final Identifier ID = IbarnOriginsMain.IOIdentifier("fire_weakness");
    private final AttributeModifier DAMAGE_MODIFIER = new AttributeModifier(ID, -0.9D, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);

    public FireWeaknessEffect() {
        super(MobEffectCategory.HARMFUL, 0xffffff);
    }

    @Override
    public void onEffectStarted(LivingEntity entity, int amplifier) {
        AttributeInstance damage = entity.getAttribute(Attributes.ATTACK_DAMAGE);
        if(damage != null) if(!damage.hasModifier(ID)) damage.addTransientModifier(DAMAGE_MODIFIER);

        if(entity instanceof IbarnOriginsEntity ioe) {
            ioe.setShakingFromFireWeakness(true);
        }
    }

    @Override
    public void onStatusEffectRemoved(ServerLevel world, LivingEntity entity, int amplifier) {
        AttributeInstance damage = entity.getAttribute(Attributes.ATTACK_DAMAGE);
        if(damage != null && damage.hasModifier(ID)) damage.removeModifier(DAMAGE_MODIFIER);

        if(entity instanceof IbarnOriginsEntity ioe) {
            ioe.setShakingFromFireWeakness(false);
        }
    }
}
