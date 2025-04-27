package com.ibarnstormer.ibarnorigins.entity;

import com.ibarnstormer.ibarnorigins.registry.IOEntities;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.projectile.WitherSkullEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.BlockView;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;

import java.util.List;
import java.util.Optional;

public class HomingWitherSkullEntity extends WitherSkullEntity {
    private static final TrackedData<Boolean> CHARGED;
    private static final TrackedData<Integer> DATA_CACHED_HOMING_TARGET;
    private static final TrackedData<Integer> DATA_TICK;

    public HomingWitherSkullEntity(World world){
        super(IOEntities.HOMING_WITHER_SKULL_ENTITY.get(), world);
    }

    public HomingWitherSkullEntity(EntityType<? extends WitherSkullEntity> entityType, World world) {
        super(entityType, world);
    }

    public HomingWitherSkullEntity(World world, LivingEntity owner, double directionX, double directionY, double directionZ) {
        super(world, owner, directionX, directionY, directionZ);
    }

    protected float getDrag() {
        float lockOnFactor = this.dataTracker.get(DATA_CACHED_HOMING_TARGET) != 0 ? 0.15F : 0.0F;
        return this.isCharged() ? 0.73F - lockOnFactor : super.getDrag() - lockOnFactor;
    }

    public boolean isOnFire() {
        return false;
    }

    public float getEffectiveExplosionResistance(Explosion explosion, BlockView world, BlockPos pos, BlockState blockState, FluidState fluidState, float max) {
        return this.isCharged() && WitherEntity.canDestroy(blockState) ? Math.min(0.8F, max) : max;
    }

    @Override
    public void tick() {
        super.tick();
        if(this.dataTracker.get(DATA_TICK) <= 0) {
            if(this.getOwner() != null) {
                this.setRotation(this.getOwner().getYaw() + 180, this.getOwner().getPitch());
            }
            if(this.getOwner() != null && !this.getWorld().isClient()) {
                HitResult cast = rayCast(this.getOwner());
                if(cast != null) {
                    LivingEntity target = getClosestEntityTarget(this.getOwner(), cast.getPos());
                    if(target != null) this.dataTracker.set(DATA_CACHED_HOMING_TARGET, target.getId());
                }
            }
            this.dataTracker.set(DATA_TICK, 1);
        }
        if(this.dataTracker.get(DATA_CACHED_HOMING_TARGET) != 0) {
            Entity target = this.getWorld().getEntityById(this.dataTracker.get(DATA_CACHED_HOMING_TARGET));
            if(target != null) {
                double targetX = target.getX();
                double targetY = target.getY() + target.getBoundingBox().getYLength() / 2;
                double targetZ = target.getZ();

                double dirX = targetX - this.getX();
                double dirY = targetY - this.getY();
                double dirZ = targetZ - this.getZ();

                this.refreshPosition();

                double d0 = Math.sqrt(dirX * dirX + dirY * dirY + dirZ * dirZ);
                if (d0 != 0.0D) {
                    this.powerX = dirX / d0 * 0.1D;
                    this.powerY = dirY / d0 * 0.1D;
                    this.powerZ = dirZ / d0 * 0.1D;
                }
                this.setVelocity(this.getVelocity().multiply(0.95D).add(new Vec3d(dirX, dirY, dirZ).normalize().multiply(0.25D)));
            }
            else if(!this.getWorld().isClient()) {
                this.getWorld().createExplosion(this, this.getX(), this.getY(), this.getZ(), 1.0F, false, World.ExplosionSourceType.NONE);
                this.discard();
            }
        }
    }

    private LivingEntity getClosestEntityTarget(Entity owner, Vec3d endPos) {
        Vec3d startPos = new Vec3d(owner.getX(), owner.getY() + owner.getHeight(), owner.getZ());
        Box box = new Box(Math.min(owner.getX(), endPos.x), Math.min(owner.getY() + owner.getHeight(), endPos.y), Math.min(owner.getZ(), endPos.z), Math.max(owner.getX(), endPos.x), Math.max(owner.getY() + owner.getHeight(), endPos.y), Math.max(owner.getZ(), endPos.z)).expand(1, 1, 1);
        List<LivingEntity> list = owner.getWorld().getNonSpectatingEntities(LivingEntity.class, box);

        Vec3d lookVec = endPos.subtract(startPos);

        double minDistance = Double.MAX_VALUE;
        LivingEntity closest = null;

        for(LivingEntity target : list) {
            if(target != owner && !target.getPassengerList().contains(owner) && target.canSee(owner) && computeCosineSim(lookVec, target.getPos().subtract(startPos)) > 0.99) {
                float f = owner.getTargetingMargin() + 0.15f;
                Box box1 = owner.getBoundingBox().expand(f, f, f);
                Optional<Vec3d> hit = box1.raycast(startPos, endPos);

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
        Vec3d camPos = new Vec3d(entity.getX(), entity.getEyeY(), entity.getZ());
        Vec3d rotation = entity.getRotationVec(1);
        double range = 128.0D;
        Vec3d end = camPos.add(rotation.x * range, rotation.y * range, rotation.z * range);
        return entity.getWorld().raycast(new RaycastContext(camPos, end, RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, entity));
    }

    protected void onEntityHit(EntityHitResult entityHitResult) {
        super.onEntityHit(entityHitResult);
        if (!this.getWorld().isClient) {
            Entity entity = entityHitResult.getEntity();
            Entity entity2 = this.getOwner();
            boolean bl2;
            if (entity2 instanceof LivingEntity) {
                LivingEntity livingEntity = (LivingEntity)entity2;
                bl2 = entity.damage(this.getDamageSources().witherSkull(this, livingEntity), 8.0F);
                if (bl2) {
                    if (entity.isAlive()) {
                        this.applyDamageEffects(livingEntity, entity);
                    } else {
                        this.applyDamageEffects(livingEntity, entity);
                        livingEntity.heal(1.0F);
                    }
                }
            } else {
                bl2 = entity.damage(this.getDamageSources().magic(), 5.0F);
            }

            if (bl2 && entity instanceof LivingEntity) {
                ((LivingEntity)entity).addStatusEffect(new StatusEffectInstance(StatusEffects.WITHER, 200, 1), this.getEffectCause());
            }

        }
    }

    protected void onCollision(HitResult hitResult) {
        HitResult.Type type = hitResult.getType();
        if (type == HitResult.Type.ENTITY) {
            this.onEntityHit((EntityHitResult)hitResult);
        } else if (type == HitResult.Type.BLOCK) {
            this.onBlockHit((BlockHitResult)hitResult);
        }
        if (!this.getWorld().isClient) {
            this.getWorld().createExplosion(this, this.getX(), this.getY(), this.getZ(), 1.0F, false, World.ExplosionSourceType.NONE);
            this.discard();
        }

    }

    @Override
    protected boolean canHit(Entity entity) {
        if(entity == this.getOwner()) return false;
        else return super.canHit(entity);
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        return false;
    }

    protected void initDataTracker() {
        this.dataTracker.startTracking(CHARGED, false);
        this.dataTracker.startTracking(DATA_TICK, 0);
        this.dataTracker.startTracking(DATA_CACHED_HOMING_TARGET, 0);
    }

    public boolean isCharged() {
        return (Boolean)this.dataTracker.get(CHARGED);
    }

    public void setCharged(boolean charged) {
        this.dataTracker.set(CHARGED, charged);
    }

    public int getDataTick() {
        return this.dataTracker.get(DATA_TICK);
    }

    @Override
    protected boolean isBurning() {
        return false;
    }

    private double computeCosineSim(Vec3d vec1, Vec3d vec2) {
        return Math.abs((vec1.dotProduct(vec2))/(vec1.length() * vec2.length()));
    }

    static {
        CHARGED = DataTracker.registerData(HomingWitherSkullEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
        DATA_TICK = DataTracker.registerData(HomingWitherSkullEntity.class, TrackedDataHandlerRegistry.INTEGER);
        DATA_CACHED_HOMING_TARGET = DataTracker.registerData(HomingWitherSkullEntity.class, TrackedDataHandlerRegistry.INTEGER);
    }
}
