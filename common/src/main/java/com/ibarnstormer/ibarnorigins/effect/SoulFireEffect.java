package com.ibarnstormer.ibarnorigins.effect;

import com.ibarnstormer.ibarnorigins.IbarnOriginsMain;
import com.ibarnstormer.ibarnorigins.entity.IbarnOriginsEntity;
import com.ibarnstormer.ibarnorigins.registry.IODamageSources;
import com.ibarnstormer.ibarnorigins.registry.IOEffects;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;

public class SoulFireEffect extends MobEffect implements IExtendedStatusEffect {

    private final ResourceLocation ID = IbarnOriginsMain.IOIdentifier("knockback_prevention");

    public SoulFireEffect() {
        super(MobEffectCategory.HARMFUL, 0x1f70f2);
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        int i;
        i = 20 - (amplifier * 5);
        if (i > 0) {
            return duration % i == 0 || duration == 1;
        } else {
            return true;
        }
    }

    @Override
    public boolean applyEffectTick(LivingEntity entity, int amplifier) {

        MobEffectInstance instance = entity.getEffect(IOEffects.SOUL_FIRE.getRef());

        if(instance instanceof OwnableStatusEffectInstance ownableInstance && ownableInstance.getOwner(entity.level()) != null && entity.level() instanceof ServerLevel serverWorld) {
            LivingEntity attacker = ownableInstance.getOwner(entity.level());

            AttributeInstance kb = entity.getAttribute(Attributes.KNOCKBACK_RESISTANCE);
            AttributeModifier kb_modifier = new AttributeModifier(ID, 1D, AttributeModifier.Operation.ADD_VALUE);

            float damage = 2.0F;
            if(attacker instanceof Player player) {
                damage = Math.min(Math.max(2.0F, ((float) player.experienceLevel / 100) * (entity.getMaxHealth() / (100.0F / 7.5F))), Math.max(2.0F, entity.getMaxHealth() / (100.0F / 7.5F)));
            }

            if(kb != null) kb.addTransientModifier(kb_modifier);
            entity.hurt(entity.damageSources().indirectMagic(attacker, attacker), damage / 2);
            entity.hurt(IODamageSources.entityDamageSource("soul_burn", attacker, entity.level()), damage);
            if(kb != null) kb.removeModifier(kb_modifier);
        }
        else if (entity.level() instanceof ServerLevel serverWorld) {
            entity.hurt(entity.damageSources().magic(), 1.0F);
            entity.hurt(IODamageSources.damageSource("soul_burn", entity.level()), 2.0F);
        }

        return true;
    }

    @Override
    public void onEffectStarted(LivingEntity entity, int amplifier) {
        if(entity instanceof IbarnOriginsEntity ioe && !ioe.isSoulMage()) ioe.setOnSoulMageFire(true);
    }

    @Override
    public void onStatusEffectRemoved(ServerLevel world, LivingEntity entity, int amplifier) {
        if(entity instanceof IbarnOriginsEntity ioe) ioe.setOnSoulMageFire(false);
    }
}
