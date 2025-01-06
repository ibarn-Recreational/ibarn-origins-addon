package com.ibarnstormer.ibarnorigins.entity;

import com.ibarnstormer.ibarnorigins.effect.OwnableStatusEffectInstance;
import com.ibarnstormer.ibarnorigins.registry.IODamageSources;
import com.ibarnstormer.ibarnorigins.registry.IOEffects;
import com.ibarnstormer.ibarnorigins.registry.IOEntities;
import com.ibarnstormer.ibarnorigins.registry.IOParticles;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ExplosiveProjectileEntity;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class SoulFireBallEntity extends ExplosiveProjectileEntity {


    public SoulFireBallEntity(World world) {
        super(IOEntities.SOUL_FIRE_BALL_ENTITY.get(), world);
    }

    public SoulFireBallEntity(EntityType<? extends ExplosiveProjectileEntity> entityType, World world) {
        super(entityType, world);
    }


    public SoulFireBallEntity(double x, double y, double z, double directionX, double directionY, double directionZ, World world) {
        this(IOEntities.SOUL_FIRE_BALL_ENTITY.get(), world);
        this.refreshPositionAndAngles(x,y,z,this.getYaw(),this.getPitch());
        this.setVelocity(directionX,directionY,directionZ);
    }

    public SoulFireBallEntity(LivingEntity owner, double directionX, double directionY, double directionZ, World world) {
        super(IOEntities.SOUL_FIRE_BALL_ENTITY.get(), owner, directionX, directionY, directionZ, world);
    }


    @Override
    protected void onCollision(HitResult hitResult) {
        super.onCollision(hitResult);
        Entity entity = this.getOwner();
        if (!this.getWorld().isClient) {
            OwnedAreaEffectCloudEntity areaEffectCloudEntity = new OwnedAreaEffectCloudEntity(this.getWorld(), this.getOwner() instanceof LivingEntity le ? le : null, this.getX(), this.getY(), this.getZ());
            areaEffectCloudEntity.setParticleType(IOParticles.SOUL_MAGE_FLAME.get());
            areaEffectCloudEntity.setWaitTime(0);
            if(entity instanceof PlayerEntity player) {
                float exp_mult = player.experienceLevel;
                if(exp_mult > 100) exp_mult = 100;

                StatusEffectInstance soulFireStrength = player.getStatusEffect(IOEffects.SOUL_FIRE_STRENGTH.get());
                boolean charged = soulFireStrength != null && soulFireStrength.getAmplifier() >= 1;

                areaEffectCloudEntity.setRadius(1.5F);
                areaEffectCloudEntity.setDuration((int) (80 + exp_mult * 0.25F));

                if(charged && soulFireStrength.getAmplifier() % 2 == 1) {
                    areaEffectCloudEntity.setReapplicationDelay(15);
                }

                areaEffectCloudEntity.addEffect(new OwnableStatusEffectInstance(IOEffects.SOUL_FIRE.get(), 100, charged ? soulFireStrength.getAmplifier() : 0, true, false, true, player.getUuid()));
            }
            else {
                areaEffectCloudEntity.setRadius(1.5F);
                areaEffectCloudEntity.setDuration(80);
                areaEffectCloudEntity.addEffect(new OwnableStatusEffectInstance(IOEffects.SOUL_FIRE.get(), 100, 0, true, false, true, null));
            }
            if (hitResult.getType() == HitResult.Type.ENTITY) {
                areaEffectCloudEntity.setPosition(((EntityHitResult) hitResult).getEntity().getX(), ((EntityHitResult) hitResult).getEntity().getY(), ((EntityHitResult) hitResult).getEntity().getZ());
            }
            this.getWorld().spawnEntity(areaEffectCloudEntity);
            this.discard();
        }
        if(this.getWorld() instanceof ServerWorld serverWorld) serverWorld.playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.ENTITY_DRAGON_FIREBALL_EXPLODE, SoundCategory.PLAYERS, 2.0f, 0.75f);
    }


    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        super.onEntityHit(entityHitResult);
        if (!this.getWorld().isClient) {
            Entity entity = entityHitResult.getEntity();
            Entity entity2 = this.getOwner();
            if(entity2 instanceof PlayerEntity player) {
                float exp_mult = player.experienceLevel;
                if(exp_mult > 100) exp_mult = 100;
                float damage = 8.0F + exp_mult / 5;

                StatusEffectInstance soulFireStrength = player.getStatusEffect(IOEffects.SOUL_FIRE_STRENGTH.get());
                boolean charged = soulFireStrength != null && soulFireStrength.getAmplifier() >= 1;

                damage *= charged ? (float) 1.5 : 1;

                entity.damage(entity.getDamageSources().indirectMagic(this, player), damage / 2);
                entity.damage(IODamageSources.entityDamageSource("soul_blast", player, this.getWorld()), damage);
                if(entity instanceof LivingEntity livingEntity) livingEntity.addStatusEffect(new OwnableStatusEffectInstance(IOEffects.SOUL_FIRE.get(), 100, charged ? soulFireStrength.getAmplifier() : 0, true, false, true, player.getUuid()));
            }
            else {
                entity.damage(entity.getDamageSources().indirectMagic(this, entity2), 4.0F);
                entity.damage(IODamageSources.entityDamageSource("soul_blast", entity2, this.getWorld()), 8.0F);
                if(entity instanceof LivingEntity livingEntity) livingEntity.addStatusEffect(new StatusEffectInstance(IOEffects.SOUL_FIRE.get(), 100, 0, true, true));
            }
            if (entity2 instanceof LivingEntity) {
                this.applyDamageEffects((LivingEntity)entity2, entity);
            }
            if(entity instanceof LivingEntity le && entity2 != null) le.takeKnockback(1.0F, MathHelper.sin(entity2.getYaw() * 0.017453292F), -MathHelper.cos(entity2.getYaw() * 0.017453292F));
        }
    }

    @Override
    protected float getDrag() {
        return 0.95F;
    }

    protected ParticleEffect getParticleType() {
        return IOParticles.SOUL_MAGE_FLAME.get();
    }

    protected boolean isBurning() {
        return false;
    }

    public boolean canHit() {
        return false;
    }

    /*
    @Override
    public Packet<ClientPlayPacketListener> createSpawnPacket() {
        return ProjectileEntitySpawnPacket.createSpawnPacket(this, IOEntities.SOUL_FB_ID);
    }
     */
}
