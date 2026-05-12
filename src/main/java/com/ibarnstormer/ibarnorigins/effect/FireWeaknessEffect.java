package com.ibarnstormer.ibarnorigins.effect;

import com.ibarnstormer.ibarnorigins.IbarnOriginsMain;
import com.ibarnstormer.ibarnorigins.entity.IbarnOriginsEntity;
import com.ibarnstormer.ibarnorigins.registry.IOEffects;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;

public class FireWeaknessEffect extends StatusEffect implements IExtendedStatusEffect {

    private final Identifier ID = IbarnOriginsMain.IOIdentifier("fire_weakness");
    private final EntityAttributeModifier DAMAGE_MODIFIER = new EntityAttributeModifier(ID, -0.9D, EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);

    public FireWeaknessEffect() {
        super(StatusEffectCategory.HARMFUL, 0xffffff);
    }

    @Override
    public void onApplied(LivingEntity entity, int amplifier) {
        EntityAttributeInstance damage = entity.getAttributeInstance(EntityAttributes.ATTACK_DAMAGE);
        if(damage != null) if(!damage.hasModifier(ID)) damage.addTemporaryModifier(DAMAGE_MODIFIER);

        if(entity instanceof IbarnOriginsEntity ioe) {
            ioe.setShakingFromFireWeakness(true);
        }
    }

    @Override
    public void onStatusEffectRemoved(ServerWorld world, LivingEntity entity, int amplifier) {
        EntityAttributeInstance damage = entity.getAttributeInstance(EntityAttributes.ATTACK_DAMAGE);
        if(damage != null && damage.hasModifier(ID)) damage.removeModifier(DAMAGE_MODIFIER);

        if(entity instanceof IbarnOriginsEntity ioe) {
            ioe.setShakingFromFireWeakness(false);
        }
    }
}
