package com.ibarnstormer.ibarnorigins.entity;

import com.ibarnstormer.ibarnorigins.registry.IOEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.TraceableEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.projectile.hurtingprojectile.AbstractHurtingProjectile;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import java.util.List;
import java.util.Optional;

public class HomingWitherSkullEntity extends AbstractHurtingProjectile {
    private static final EntityDataAccessor<Boolean> CHARGED;
    private static final EntityDataAccessor<Integer> DATA_CACHED_HOMING_TARGET;
    private static final EntityDataAccessor<Integer> DATA_TICK;

    public HomingWitherSkullEntity(Level world){
        super(IOEntities.HOMING_WITHER_SKULL_ENTITY.get(), world);
    }

    public HomingWitherSkullEntity(EntityType<HomingWitherSkullEntity> entityType, Level world) {
        super(entityType, world);
    }

    public HomingWitherSkullEntity(Level world, LivingEntity owner, Vec3 velocity) {
        super(IOEntities.HOMING_WITHER_SKULL_ENTITY.get(), owner, velocity, world);
    }

    protected float getInertia() {
        float lockOnFactor = this.entityData.get(DATA_CACHED_HOMING_TARGET) != 0 ? 0.15F : 0.0F;
        return this.isCharged() ? 0.73F - lockOnFactor : super.getInertia() - lockOnFactor;
    }

    public boolean isOnFire() {
        return false;
    }

    public float getBlockExplosionResistance(Explosion explosion, BlockGetter world, BlockPos pos, BlockState blockState, FluidState fluidState, float max) {
        return this.isCharged() && WitherBoss.canDestroy(blockState) ? Math.min(0.8F, max) : max;
    }

    @Override
    public void tick() {
        super.tick();
        if(this.entityData.get(DATA_TICK) <= 0) {
            if(this.getOwner() != null) {
                this.setRot(this.getOwner().getYRot() + 180, this.getOwner().getXRot());
            }
            if(this.getOwner() != null && !this.level().isClientSide()) {
                HitResult cast = rayCast(this.getOwner());
                if(cast != null) {
                    LivingEntity target = getClosestEntityTarget(this.getOwner(), cast.getLocation());
                    if(target != null) this.entityData.set(DATA_CACHED_HOMING_TARGET, target.getId());
                }
            }
            this.entityData.set(DATA_TICK, 1);
        }
        if(this.entityData.get(DATA_CACHED_HOMING_TARGET) != 0) {
            Entity target = this.level().getEntity(this.entityData.get(DATA_CACHED_HOMING_TARGET));
            if(target != null) {
                double targetX = target.getX();
                double targetY = target.getY() + target.getBoundingBox().getYsize() / 2;
                double targetZ = target.getZ();

                double dirX = targetX - this.getX();
                double dirY = targetY - this.getY();
                double dirZ = targetZ - this.getZ();

                this.reapplyPosition();

                this.setDeltaMovement(this.getDeltaMovement().scale(0.95D).add(new Vec3(dirX, dirY, dirZ).normalize().scale(0.25D)));
            }
            else if(!this.level().isClientSide()) {
                this.level().explode(this, this.getX(), this.getY(), this.getZ(), 1.0F, false, Level.ExplosionInteraction.NONE);
                this.discard();
            }
        }
    }

    private LivingEntity getClosestEntityTarget(Entity owner, Vec3 endPos) {
        Vec3 startPos = new Vec3(owner.getX(), owner.getY() + owner.getBbHeight(), owner.getZ());
        AABB box = new AABB(Math.min(owner.getX(), endPos.x), Math.min(owner.getY() + owner.getBbHeight(), endPos.y), Math.min(owner.getZ(), endPos.z), Math.max(owner.getX(), endPos.x), Math.max(owner.getY() + owner.getBbHeight(), endPos.y), Math.max(owner.getZ(), endPos.z)).inflate(1, 1, 1);
        List<LivingEntity> list = owner.level().getEntitiesOfClass(LivingEntity.class, box);

        Vec3 lookVec = endPos.subtract(startPos);

        double minDistance = Double.MAX_VALUE;
        LivingEntity closest = null;

        for(LivingEntity target : list) {
            if(target != owner && !target.getPassengers().contains(owner) && !(target instanceof TraceableEntity o && o.getOwner() == this.getOwner()) && !(target instanceof OwnableEntity t && t.getOwner() == this.getOwner()) && !target.isAlliedTo(this.getOwner()) && target.hasLineOfSight(owner) && computeCosineSim(lookVec, target.position().subtract(startPos)) > 0.99) {
                float f = owner.getPickRadius() + 0.15f;
                AABB box1 = owner.getBoundingBox().inflate(f, f, f);
                Optional<Vec3> hit = box1.clip(startPos, endPos);

                if(hit.isPresent() || box1.contains(startPos)) {
                    double distance = owner.distanceTo(target);
                    if (distance <= minDistance) {
                        minDistance = distance;
                        closest = target;
                    }
                }
            }
        }

        return closest;
    }

    private HitResult rayCast(Entity entity) {
        Vec3 camPos = new Vec3(entity.getX(), entity.getEyeY(), entity.getZ());
        Vec3 rotation = entity.getViewVector(1);
        double range = 128.0D;
        Vec3 end = camPos.add(rotation.x * range, rotation.y * range, rotation.z * range);
        return entity.level().clip(new ClipContext(camPos, end, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, entity));
    }

    protected void onHitEntity(EntityHitResult entityHitResult) {
        super.onHitEntity(entityHitResult);
        if (this.level() instanceof ServerLevel serverWorld) {
            Entity entity = entityHitResult.getEntity();
            Entity entity2 = this.getOwner();
            boolean bl2;
            if (entity2 instanceof LivingEntity) {
                LivingEntity livingEntity = (LivingEntity)entity2;
                float attackDamageScaler = 1;
                AttributeInstance damageAttribute = livingEntity.getAttribute(Attributes.ATTACK_DAMAGE);
                if (damageAttribute != null) {
                    attackDamageScaler = (float) damageAttribute.getValue();
                }

                DamageSource damageSource = this.damageSources().witherSkull(null, livingEntity);
                bl2 = entity.hurtServer(serverWorld, damageSource, 7.0F + attackDamageScaler);
                if (bl2) {
                    if (entity.isAlive()) {
                        EnchantmentHelper.doPostAttackEffects(serverWorld, entity, damageSource);
                    } else {
                        livingEntity.heal(1.0F);
                    }
                }
            } else {
                bl2 = entity.hurtServer(serverWorld, this.damageSources().magic(), 5.0F);
            }

            if (bl2 && entity instanceof LivingEntity) {
                ((LivingEntity)entity).addEffect(new MobEffectInstance(MobEffects.WITHER, 200, 1), this.getEffectSource());
            }

        }
    }

    protected void onHit(HitResult hitResult) {
        HitResult.Type type = hitResult.getType();
        if (type == HitResult.Type.ENTITY) {
            this.onHitEntity((EntityHitResult)hitResult);
        } else if (type == HitResult.Type.BLOCK) {
            this.onHitBlock((BlockHitResult)hitResult);
        }
        if (!this.level().isClientSide()) {
            this.level().explode(this, this.getX(), this.getY(), this.getZ(), 1.0F, false, Level.ExplosionInteraction.NONE);
            this.discard();
        }

    }

    @Override
    protected boolean canHitEntity(Entity entity) {
        if(entity == this.getOwner()) return false;
        else return super.canHitEntity(entity);
    }

    @Override
    public boolean hurtServer(ServerLevel world, DamageSource source, float amount) {
        return false;
    }

    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(CHARGED, false);
        builder.define(DATA_TICK, 0);
        builder.define(DATA_CACHED_HOMING_TARGET, 0);
    }

    public boolean isCharged() {
        return this.entityData.get(CHARGED);
    }

    public void setCharged(boolean charged) {
        this.entityData.set(CHARGED, charged);
    }

    public int getDataTick() {
        return this.entityData.get(DATA_TICK);
    }

    @Override
    protected boolean shouldBurn() {
        return false;
    }

    private double computeCosineSim(Vec3 vec1, Vec3 vec2) {
        return Math.abs((vec1.dot(vec2))/(vec1.length() * vec2.length()));
    }

    static {
        CHARGED = SynchedEntityData.defineId(HomingWitherSkullEntity.class, EntityDataSerializers.BOOLEAN);
        DATA_TICK = SynchedEntityData.defineId(HomingWitherSkullEntity.class, EntityDataSerializers.INT);
        DATA_CACHED_HOMING_TARGET = SynchedEntityData.defineId(HomingWitherSkullEntity.class, EntityDataSerializers.INT);
    }
}
