package com.ibarnstormer.ibarnorigins.entity;

import com.ibarnstormer.ibarnorigins.registry.IOEntities;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.*;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.projectile.ExplosiveProjectileEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.server.world.ServerWorld;
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

public class HomingWitherSkullEntity extends ExplosiveProjectileEntity {
    private static final TrackedData<Boolean> CHARGED;
    private static final TrackedData<Integer> DATA_CACHED_HOMING_TARGET;
    private static final TrackedData<Integer> DATA_TICK;

    public HomingWitherSkullEntity(World world){
        super(IOEntities.HOMING_WITHER_SKULL_ENTITY.get(), world);
    }

    public HomingWitherSkullEntity(EntityType<HomingWitherSkullEntity> entityType, World world) {
        super(entityType, world);
    }

    public HomingWitherSkullEntity(World world, LivingEntity owner, Vec3d velocity) {
        super(IOEntities.HOMING_WITHER_SKULL_ENTITY.get(), owner, velocity, world);
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
            if(this.getOwner() != null && !this.getEntityWorld().isClient()) {
                HitResult cast = rayCast(this.getOwner());
                if(cast != null) {
                    LivingEntity target = getClosestEntityTarget(this.getOwner(), cast.getPos());
                    if(target != null) this.dataTracker.set(DATA_CACHED_HOMING_TARGET, target.getId());
                }
            }
            this.dataTracker.set(DATA_TICK, 1);
        }
        if(this.dataTracker.get(DATA_CACHED_HOMING_TARGET) != 0) {
            Entity target = this.getEntityWorld().getEntityById(this.dataTracker.get(DATA_CACHED_HOMING_TARGET));
            if(target != null) {
                double targetX = target.getX();
                double targetY = target.getY() + target.getBoundingBox().getLengthY() / 2;
                double targetZ = target.getZ();

                double dirX = targetX - this.getX();
                double dirY = targetY - this.getY();
                double dirZ = targetZ - this.getZ();

                this.refreshPosition();

                this.setVelocity(this.getVelocity().multiply(0.95D).add(new Vec3d(dirX, dirY, dirZ).normalize().multiply(0.25D)));
            }
            else if(!this.getEntityWorld().isClient()) {
                this.getEntityWorld().createExplosion(this, this.getX(), this.getY(), this.getZ(), 1.0F, false, World.ExplosionSourceType.NONE);
                this.discard();
            }
        }
    }

    private LivingEntity getClosestEntityTarget(Entity owner, Vec3d endPos) {
        Vec3d startPos = new Vec3d(owner.getX(), owner.getY() + owner.getHeight(), owner.getZ());
        Box box = new Box(Math.min(owner.getX(), endPos.x), Math.min(owner.getY() + owner.getHeight(), endPos.y), Math.min(owner.getZ(), endPos.z), Math.max(owner.getX(), endPos.x), Math.max(owner.getY() + owner.getHeight(), endPos.y), Math.max(owner.getZ(), endPos.z)).expand(1, 1, 1);
        List<LivingEntity> list = owner.getEntityWorld().getNonSpectatingEntities(LivingEntity.class, box);

        Vec3d lookVec = endPos.subtract(startPos);

        double minDistance = Double.MAX_VALUE;
        LivingEntity closest = null;

        for(LivingEntity target : list) {
            if(target != owner && !target.getPassengerList().contains(owner) && !(target instanceof Ownable o && o.getOwner() == this.getOwner()) && !(target instanceof Tameable t && t.getOwner() == this.getOwner()) && !target.isTeammate(this.getOwner()) && target.canSee(owner) && computeCosineSim(lookVec, target.getEntityPos().subtract(startPos)) > 0.99) {
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
        return entity.getEntityWorld().raycast(new RaycastContext(camPos, end, RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, entity));
    }

    protected void onEntityHit(EntityHitResult entityHitResult) {
        super.onEntityHit(entityHitResult);
        if (this.getEntityWorld() instanceof ServerWorld serverWorld) {
            Entity entity = entityHitResult.getEntity();
            Entity entity2 = this.getOwner();
            boolean bl2;
            if (entity2 instanceof LivingEntity) {
                LivingEntity livingEntity = (LivingEntity)entity2;
                float attackDamageScaler = 1;
                EntityAttributeInstance damageAttribute = livingEntity.getAttributeInstance(EntityAttributes.ATTACK_DAMAGE);
                if (damageAttribute != null) {
                    attackDamageScaler = (float) damageAttribute.getValue();
                }

                DamageSource damageSource = this.getDamageSources().witherSkull(null, livingEntity);
                bl2 = entity.damage(serverWorld, damageSource, 7.0F + attackDamageScaler);
                if (bl2) {
                    if (entity.isAlive()) {
                        EnchantmentHelper.onTargetDamaged(serverWorld, entity, damageSource);
                    } else {
                        livingEntity.heal(1.0F);
                    }
                }
            } else {
                bl2 = entity.damage(serverWorld, this.getDamageSources().magic(), 5.0F);
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
        if (!this.getEntityWorld().isClient()) {
            this.getEntityWorld().createExplosion(this, this.getX(), this.getY(), this.getZ(), 1.0F, false, World.ExplosionSourceType.NONE);
            this.discard();
        }

    }

    @Override
    protected boolean canHit(Entity entity) {
        if(entity == this.getOwner()) return false;
        else return super.canHit(entity);
    }

    @Override
    public boolean damage(ServerWorld world, DamageSource source, float amount) {
        return false;
    }

    protected void initDataTracker(DataTracker.Builder builder) {
        builder.add(CHARGED, false);
        builder.add(DATA_TICK, 0);
        builder.add(DATA_CACHED_HOMING_TARGET, 0);
    }

    public boolean isCharged() {
        return this.dataTracker.get(CHARGED);
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
