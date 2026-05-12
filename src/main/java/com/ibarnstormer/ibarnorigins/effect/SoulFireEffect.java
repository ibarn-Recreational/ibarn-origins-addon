package com.ibarnstormer.ibarnorigins.effect;

import com.ibarnstormer.ibarnorigins.IbarnOriginsMain;
import com.ibarnstormer.ibarnorigins.entity.IbarnOriginsEntity;
import com.ibarnstormer.ibarnorigins.registry.IODamageSources;
import com.ibarnstormer.ibarnorigins.registry.IOEffects;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.*;
import net.minecraft.entity.effect.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;

public class SoulFireEffect extends StatusEffect implements IExtendedStatusEffect {

    private final Identifier ID = IbarnOriginsMain.IOIdentifier("knockback_prevention");

    public SoulFireEffect() {
        super(StatusEffectCategory.HARMFUL, 0x1f70f2);
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        int i;
        i = 20 - (amplifier * 5);
        if (i > 0) {
            return duration % i == 0 || duration == 1;
        } else {
            return true;
        }
    }

    @Override
    public boolean applyUpdateEffect(ServerWorld world, LivingEntity entity, int amplifier) {

        StatusEffectInstance instance = entity.getStatusEffect(IOEffects.SOUL_FIRE.getRef());

        if(instance instanceof OwnableStatusEffectInstance ownableInstance && ownableInstance.getOwner(entity.getEntityWorld()) != null && entity.getEntityWorld() instanceof ServerWorld serverWorld) {
            LivingEntity attacker = ownableInstance.getOwner(entity.getEntityWorld());

            EntityAttributeInstance kb = entity.getAttributeInstance(EntityAttributes.KNOCKBACK_RESISTANCE);
            EntityAttributeModifier kb_modifier = new EntityAttributeModifier(ID, 1D, EntityAttributeModifier.Operation.ADD_VALUE);

            float damage = 2.0F;
            if(attacker instanceof PlayerEntity player) {
                damage = Math.min(Math.max(2.0F, ((float) player.experienceLevel / 100) * (entity.getMaxHealth() / (100.0F / 7.5F))), Math.max(2.0F, entity.getMaxHealth() / (100.0F / 7.5F)));
            }

            if(kb != null) kb.addTemporaryModifier(kb_modifier);
            entity.damage(serverWorld, entity.getDamageSources().indirectMagic(attacker, attacker), damage / 2);
            entity.damage(serverWorld, IODamageSources.entityDamageSource("soul_burn", attacker, entity.getEntityWorld()), damage);
            if(kb != null) kb.removeModifier(kb_modifier);
        }
        else if (entity.getEntityWorld() instanceof ServerWorld serverWorld) {
            entity.damage(serverWorld, entity.getDamageSources().magic(), 1.0F);
            entity.damage(serverWorld, IODamageSources.damageSource("soul_burn", entity.getEntityWorld()), 2.0F);
        }

        return true;
    }

    @Override
    public void onApplied(LivingEntity entity, int amplifier) {
        if(entity instanceof IbarnOriginsEntity ioe && !ioe.isSoulMage()) ioe.setOnSoulMageFire(true);
    }

    @Override
    public void onStatusEffectRemoved(ServerWorld world, LivingEntity entity, int amplifier) {
        if(entity instanceof IbarnOriginsEntity ioe) ioe.setOnSoulMageFire(false);
    }
}
