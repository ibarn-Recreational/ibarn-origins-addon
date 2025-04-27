package com.ibarnstormer.ibarnorigins.effect;

import com.ibarnstormer.ibarnorigins.entity.IbarnOriginsEntity;
import com.ibarnstormer.ibarnorigins.registry.IODamageSources;
import com.ibarnstormer.ibarnorigins.registry.IOEffects;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.*;
import net.minecraft.entity.effect.*;
import net.minecraft.entity.player.PlayerEntity;

public class SoulFireEffect extends StatusEffect {
    public SoulFireEffect() {
        super(StatusEffectCategory.HARMFUL, 0x1f70f2);
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        int i;
        i = 20 - (amplifier * 5);
        if (i > 0) {
            return duration % i == 0;
        } else {
            return true;
        }
    }

    @Override
    public void applyUpdateEffect(LivingEntity entity, int amplifier) {

        StatusEffectInstance instance = entity.getStatusEffect(IOEffects.SOUL_FIRE.get());

        if(instance instanceof OwnableStatusEffectInstance ownableInstance && ownableInstance.getOwner(entity.getWorld()) != null) {
            LivingEntity attacker = ownableInstance.getOwner(entity.getWorld());

            EntityAttributeInstance kb = entity.getAttributeInstance(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE);
            EntityAttributeModifier kb_modifier = new EntityAttributeModifier("knockback_prevention", 1D, EntityAttributeModifier.Operation.ADDITION);

            float damage = 1.0F;
            if(attacker instanceof PlayerEntity player) {
                damage = Math.min(Math.max(1.0F, ((float) player.experienceLevel / 100) * (entity.getMaxHealth() / 10.0F)), Math.max(1.0F, entity.getMaxHealth() / 10.0F));
            }

            if(kb != null) kb.addTemporaryModifier(kb_modifier);
            entity.damage(entity.getDamageSources().indirectMagic(attacker, attacker), damage / 2);
            entity.damage(IODamageSources.entityDamageSource("soul_burn", attacker, entity.getWorld()), damage);
            if(kb != null) kb.removeModifier(kb_modifier);
        }
        else {
            entity.damage(entity.getDamageSources().magic(), 0.5F);
            entity.damage(IODamageSources.damageSource("soul_burn", entity.getWorld()), 1.0F);
        }
    }

    @Override
    public void onApplied(LivingEntity entity, AttributeContainer attributes, int amplifier) {
        if(entity instanceof IbarnOriginsEntity ioe) ioe.setOnSoulMageFire(true);
    }

    @Override
    public void onRemoved(LivingEntity entity, AttributeContainer attributes, int amplifier) {
        if(entity instanceof IbarnOriginsEntity ioe) ioe.setOnSoulMageFire(false);
    }

}
