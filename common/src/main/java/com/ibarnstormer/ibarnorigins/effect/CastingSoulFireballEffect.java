package com.ibarnstormer.ibarnorigins.effect;

import com.ibarnstormer.ibarnorigins.IbarnOriginsMain;
import com.ibarnstormer.ibarnorigins.entity.IbarnOriginsEntity;
import com.ibarnstormer.ibarnorigins.entity.SoulFireBallEntity;
import com.ibarnstormer.ibarnorigins.registry.IOEffects;
import com.ibarnstormer.ibarnorigins.registry.IOSounds;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class CastingSoulFireballEffect extends MobEffect implements IExtendedStatusEffect {

    private final ResourceLocation ID = IbarnOriginsMain.IOIdentifier("casting_slowdown");
    private final AttributeModifier MOVEMENT_MODIFIER = new AttributeModifier(ID, -0.5D, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);

    public CastingSoulFireballEffect() {
        super(MobEffectCategory.NEUTRAL, 0xffffff);
    }

    @Override
    public void onEffectStarted(LivingEntity entity, int amplifier) {

        if(entity.level() instanceof ServerLevel serverWorld) {
            serverWorld.playSound(null, entity.getX(), entity.getY(), entity.getZ(), IOSounds.KI_BLAST_CHARGE.get(), SoundSource.PLAYERS, 1.25f, 1);
        }

        if(entity instanceof IbarnOriginsEntity spellCaster && entity.hasEffect(IOEffects.CASTING_SOUL_FIREBALL.getRef())) spellCaster.setSpellCastTicks(entity.getEffect(IOEffects.CASTING_SOUL_FIREBALL.getRef()).getDuration());

        AttributeInstance movement = entity.getAttribute(Attributes.MOVEMENT_SPEED);
        if(movement != null && !movement.hasModifier(ID))  movement.addTransientModifier(MOVEMENT_MODIFIER);

        super.onEffectStarted(entity, amplifier);
    }


    @Override
    public void onStatusEffectRemoved(ServerLevel world, LivingEntity entity, int amplifier) {
        world.playSound(null, entity.getX(), entity.getY(), entity.getZ(), IOSounds.KI_BLAST_FIRE.get(), SoundSource.PLAYERS, 1.25f, 1);

        SoulFireBallEntity fireball = new SoulFireBallEntity(entity, entity.getLookAngle(), entity.level());
        fireball.setPosRaw(entity.getX(), entity.getEyeY(), entity.getZ());

        world.addFreshEntity(fireball);

        AttributeInstance movement = entity.getAttribute(Attributes.MOVEMENT_SPEED);
        if(movement != null) if(movement.hasModifier(ID)) movement.removeModifier(MOVEMENT_MODIFIER);
    }
}
