package com.ibarnstormer.ibarnorigins.entity;

import com.ibarnstormer.ibarnorigins.effect.OwnableStatusEffectInstance;
import com.ibarnstormer.ibarnorigins.registry.IODamageSources;
import com.ibarnstormer.ibarnorigins.registry.IOEffects;
import com.ibarnstormer.ibarnorigins.registry.IOEntities;
import com.ibarnstormer.ibarnorigins.registry.IOParticles;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.hurtingprojectile.AbstractHurtingProjectile;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class SoulFireBallEntity extends AbstractHurtingProjectile {


    public SoulFireBallEntity(Level world) {
        super(IOEntities.SOUL_FIRE_BALL_ENTITY.get(), world);
    }

    public SoulFireBallEntity(EntityType<? extends AbstractHurtingProjectile> entityType, Level world) {
        super(entityType, world);
    }


    public SoulFireBallEntity(double x, double y, double z, double directionX, double directionY, double directionZ, Level world) {
        this(IOEntities.SOUL_FIRE_BALL_ENTITY.get(), world);
        this.snapTo(x,y,z,this.getYRot(),this.getXRot());
        this.setDeltaMovement(directionX,directionY,directionZ);
    }

    public SoulFireBallEntity(LivingEntity owner, Vec3 velocity, Level world) {
        super(IOEntities.SOUL_FIRE_BALL_ENTITY.get(), owner, velocity, world);
    }

    @Override
    public void tick() {
        super.tick();

        Entity owner = this.getOwner();
        if (this.level().isClientSide() || (owner == null || !owner.isRemoved()) && this.level().hasChunkAt(this.blockPosition())) {

            Vec3 vec3d = this.getDeltaMovement();
            double d = this.getX() - vec3d.x;
            double e = this.getY() - vec3d.y;
            double f = this.getZ() - vec3d.z;

            RandomSource random = this.level().getRandom();

            double xv = (random.nextBoolean() ? 1 : -1) * random.nextIntBetweenInclusive(1, 100) * 2e-4;
            double yv = (random.nextBoolean() ? 1 : -1) * random.nextIntBetweenInclusive(1, 100) * 2e-4;
            double zv = (random.nextBoolean() ? 1 : -1) * random.nextIntBetweenInclusive(1, 100) * 2e-4;

            this.level().addParticle(ParticleTypes.SMOKE, d, e + 0.2, f, xv, yv, zv);
        }
    }


    @Override
    protected void onHit(HitResult hitResult) {
        super.onHit(hitResult);
        Entity entity = this.getOwner();
        if (!this.level().isClientSide()) {
            AreaEffectCloud areaEffectCloudEntity = new AreaEffectCloud(this.level(), this.getX(), this.getY(), this.getZ());
            areaEffectCloudEntity.setCustomParticle(IOParticles.SOUL_MAGE_FLAME.get());
            areaEffectCloudEntity.setWaitTime(0);
            areaEffectCloudEntity.setOwner(this.getOwner() instanceof LivingEntity le ? le : null);
            ((IExtendedAECEntity) areaEffectCloudEntity).setToAssignOwnableEffects(true);

            if(entity instanceof Player player) {
                float exp_mult = player.experienceLevel;
                if(exp_mult > 100) exp_mult = 100;

                MobEffectInstance soulFireStrength = player.getEffect(IOEffects.SOUL_FIRE_STRENGTH.getRef());
                boolean charged = soulFireStrength != null && soulFireStrength.getAmplifier() >= 1;

                areaEffectCloudEntity.setRadius(1.5F);
                areaEffectCloudEntity.setDuration((int) (80 + exp_mult * 0.25F));

                if(charged && soulFireStrength.getAmplifier() % 2 == 1) {
                    areaEffectCloudEntity.reapplicationDelay = 15;
                }

                areaEffectCloudEntity.addEffect(new OwnableStatusEffectInstance(IOEffects.SOUL_FIRE.getRef(), 100, charged ? soulFireStrength.getAmplifier() : 0, false, false, true, player.getUUID()));
            }
            else {
                areaEffectCloudEntity.setRadius(1.5F);
                areaEffectCloudEntity.setDuration(80);
                areaEffectCloudEntity.addEffect(new OwnableStatusEffectInstance(IOEffects.SOUL_FIRE.getRef(), 100, 0, false, false, true, null));
            }
            if (hitResult.getType() == HitResult.Type.ENTITY) {
                areaEffectCloudEntity.setPos(((EntityHitResult) hitResult).getEntity().getX(), ((EntityHitResult) hitResult).getEntity().getY(), ((EntityHitResult) hitResult).getEntity().getZ());
            }
            this.level().addFreshEntity(areaEffectCloudEntity);
            this.discard();
        }
        if(this.level() instanceof ServerLevel serverWorld) serverWorld.playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.DRAGON_FIREBALL_EXPLODE, SoundSource.PLAYERS, 2.0f, 0.75f);
    }


    @Override
    protected void onHitEntity(EntityHitResult entityHitResult) {
        super.onHitEntity(entityHitResult);
        if (this.level() instanceof ServerLevel serverWorld) {
            Entity entity = entityHitResult.getEntity();
            Entity entity2 = this.getOwner();
            if(entity2 instanceof Player player) {
                float exp_mult = player.experienceLevel;
                if(exp_mult > 100) exp_mult = 100;
                float damage = 8.0F + exp_mult / 5;

                MobEffectInstance soulFireStrength = player.getEffect(IOEffects.SOUL_FIRE_STRENGTH.getRef());
                boolean charged = soulFireStrength != null && soulFireStrength.getAmplifier() >= 1;

                damage *= charged ? (float) 1.5 : 1;

                entity.hurtServer(serverWorld, entity.damageSources().indirectMagic(this, player), damage / 2);
                entity.hurtServer(serverWorld, IODamageSources.entityDamageSource("soul_blast", player, this.level()), damage);
                if(entity instanceof LivingEntity livingEntity) livingEntity.addEffect(new OwnableStatusEffectInstance(IOEffects.SOUL_FIRE.getRef(), 100, charged ? soulFireStrength.getAmplifier() : 0, false, false, true, player.getUUID()));
            }
            else {
                entity.hurtServer(serverWorld, entity.damageSources().indirectMagic(this, entity2), 4.0F);
                entity.hurtServer(serverWorld, IODamageSources.entityDamageSource("soul_blast", entity2, this.level()), 8.0F);
                if(entity instanceof LivingEntity livingEntity) livingEntity.addEffect(new MobEffectInstance(IOEffects.SOUL_FIRE.getRef(), 100, 0, false, false, true));
            }
            if (entity2 instanceof Player player) {
                EnchantmentHelper.doPostAttackEffects(serverWorld, entity, entity.damageSources().playerAttack(player));
            }
        }
    }

    @Override
    protected float getInertia() {
        return 0.95F;
    }

    @Override
    protected boolean canHitEntity(Entity entity) {
        if(entity == this.getOwner()) return false;
        else return super.canHitEntity(entity);
    }

    @Override
    protected ParticleOptions getTrailParticle() {
        return IOParticles.EMPTY_PARTICLE.get();
    }

    @Override
    protected boolean shouldBurn() {
        return false;
    }

    @Override
    public boolean isPickable() {
        return false;
    }
}
