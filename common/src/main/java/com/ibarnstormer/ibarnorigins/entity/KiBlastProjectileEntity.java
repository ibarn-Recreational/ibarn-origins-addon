package com.ibarnstormer.ibarnorigins.entity;

import com.ibarnstormer.ibarnorigins.registry.IODamageSources;
import com.ibarnstormer.ibarnorigins.registry.IOEffects;
import com.ibarnstormer.ibarnorigins.registry.IOEntities;
import com.ibarnstormer.ibarnorigins.registry.IOParticles;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ExplosiveProjectileEntity;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.*;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class KiBlastProjectileEntity extends ExplosiveProjectileEntity {

    private static final TrackedData<Integer> TICK_LIFETIME;
    private static final TrackedData<Integer> CACHED_OWNER_ID;

    public KiBlastProjectileEntity(World world) {
        super(IOEntities.KI_BLAST_PROJECTILE_ENTITY.get(), world);
    }

    public KiBlastProjectileEntity(EntityType<? extends KiBlastProjectileEntity> entityType, World world) {
        super(entityType, world);
    }

    public KiBlastProjectileEntity(LivingEntity owner, double directionX, double directionY, double directionZ, World world) {
        super(IOEntities.KI_BLAST_PROJECTILE_ENTITY.get(), owner, directionX, directionY, directionZ, world);
        Objects.requireNonNull(this.getOwner());
        this.setPos(this.getOwner().getX(), this.getOwner().getY() + this.getOwner().getBoundingBox().getYLength() / 2, this.getOwner().getZ());
    }

    public void tick() {
        super.tick();
        this.ignoreCameraFrustum = true;
        if(this.dataTracker.get(CACHED_OWNER_ID) == 0 && this.getOwner() != null) this.dataTracker.set(CACHED_OWNER_ID, this.getOwner().getId());
        if(!getWorld().isClient) {
            Entity cam = this.getOwner();
            if(cam != null) {
                HitResult cast = rayCast(cam);
                processHitResult(cast.getPos());
                if(cam instanceof PlayerEntity player && this.getDataTracker().get(TICK_LIFETIME) % 10 == 0) {
                    if(!player.isCreative() && !player.isSpectator()) player.getHungerManager().addExhaustion(7.5F);
                    if(this.getDataTracker().get(TICK_LIFETIME) == 0) player.addStatusEffect(new StatusEffectInstance(IOEffects.CASTING_SOUL_FIREBALL.get(), 40, 0, true, false, false));
                }
                if(this.getDataTracker().get(TICK_LIFETIME) == 0) {
                    ((ServerWorld) getWorld()).spawnParticles(IOParticles.SOUL_BLAST.get(), cam.getX(), cam.getY() + cam.getBoundingBox().getYLength() / 2, cam.getZ(), 1, 0.0D, 0.0D, 0.0D, 0.0D);
                }
            }
        }
        this.setVelocity(0.0F, 0.0F, 0.0F);
        this.powerX = 0;
        this.powerY = 0;
        this.powerZ = 0;
        //Upon world reload, discard entity in case data gets wiped
        if(TICK_LIFETIME == null) this.discard();
        this.dataTracker.set(TICK_LIFETIME, this.dataTracker.get(TICK_LIFETIME) + 1);
        if(this.dataTracker.get(TICK_LIFETIME) >= 40) this.discard();
    }

    private void processHitResult(Vec3d blastEndPoint) {
        Objects.requireNonNull(this.getOwner());
        if(!this.getWorld().isClient) {
            Vec3d vec3d = new Vec3d(this.getOwner().getX(), this.getOwner().getY() + this.getOwner().getBoundingBox().getYLength() / 2, this.getOwner().getZ());
            Vec3d vec3d2 = blastEndPoint.subtract(vec3d);
            Vec3d vec3d3 = vec3d2.normalize();
            this.setPos(blastEndPoint.x, blastEndPoint.y, blastEndPoint.z);
            for(int i = 1; i < MathHelper.floor(vec3d2.length()) + 7; ++i) {
                Vec3d vec3d4 = vec3d.add(vec3d3.multiply(i));
                List<LivingEntity> list = this.getWorld().getNonSpectatingEntities(LivingEntity.class, new Box(new BlockPos(new Vec3i((int)vec3d4.x, (int)vec3d4.y, (int)vec3d4.z))).expand(0.125D, 0.125D, 0.125D));
                if (!list.isEmpty()) {
                    for (LivingEntity livingEntity : list) {
                        if (livingEntity != this.getOwner()) {
                            if(this.getOwner() instanceof PlayerEntity player) {
                                double exp_mult = Math.min(player.experienceLevel, 100);
                                livingEntity.damage(IODamageSources.entityDamageSource("soul_blast", this.getOwner(), this.getWorld()), 2.0F + (float) (exp_mult / 20D));
                            }
                            else livingEntity.damage(IODamageSources.entityDamageSource("soul_blast", this.getOwner(), this.getWorld()), 2.0F);
                            double d = 0.05D * (1.0D - livingEntity.getAttributeValue(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE));
                            double e = 0.025D * (1.0D - livingEntity.getAttributeValue(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE));
                            livingEntity.addVelocity(vec3d3.getX() * e, vec3d3.getY() * d, vec3d3.getZ() * e);
                        }
                    }
                }
            }
            ((ServerWorld) getWorld()).spawnParticles(ParticleTypes.SOUL_FIRE_FLAME, blastEndPoint.x, blastEndPoint.y, blastEndPoint.z, 4, 0.05D, 0.05D, 0.05D, 0.085D);
            ((ServerWorld) getWorld()).spawnParticles(ParticleTypes.SOUL_FIRE_FLAME, this.getOwner().getX(), this.getOwner().getY() + this.getOwner().getBoundingBox().getYLength() / 2, this.getOwner().getZ(), 4, 0.01, 0.01, 0.01, 0.03D);
        }
    }

    private HitResult rayCast(Entity entity) {
        Vec3d camPos = new Vec3d(entity.getX(), entity.getEyeY(), entity.getZ());
        Vec3d rotation = entity.getRotationVec(1);
        double range = 25.0D;
        Vec3d end = camPos.add(rotation.x * range, rotation.y * range, rotation.z * range);
        return entity.getWorld().raycast(new RaycastContext(camPos, end, RaycastContext.ShapeType.OUTLINE, RaycastContext.FluidHandling.NONE, entity));
    }

    protected void initDataTracker() {
        this.dataTracker.startTracking(TICK_LIFETIME, 0);
        this.dataTracker.startTracking(CACHED_OWNER_ID, 0);
    }

    @Nullable
    public LivingEntity getCachedOwner() {
        if(this.getWorld().isClient) {
            Entity owner = this.getWorld().getEntityById(this.dataTracker.get(CACHED_OWNER_ID));
            if(owner instanceof LivingEntity livingOwner) return livingOwner;
            else return null;
        }
        if(this.getOwner() instanceof LivingEntity livingOwner) return livingOwner;
        else return null;
    }

    @Override
    protected ParticleEffect getParticleType() {
        return ParticleTypes.SOUL_FIRE_FLAME;
    }

    public boolean canHit() {
        return false;
    }

    public boolean damage(DamageSource source, float amount) {
        return false;
    }

    protected boolean isBurning() {
        return false;
    }

    public boolean isOnFire() {
        return false;
    }

    static {
        TICK_LIFETIME = DataTracker.registerData(KiBlastProjectileEntity.class, TrackedDataHandlerRegistry.INTEGER);
        CACHED_OWNER_ID = DataTracker.registerData(KiBlastProjectileEntity.class, TrackedDataHandlerRegistry.INTEGER);
    }

    /*
    @Override
    public Packet<ClientPlayPacketListener> createSpawnPacket() {
        return ProjectileEntitySpawnPacket.createSpawnPacket(this, IOEntities.KI_BLAST_ID);
    }
     */

}
