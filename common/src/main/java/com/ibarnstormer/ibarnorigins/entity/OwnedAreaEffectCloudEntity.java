package com.ibarnstormer.ibarnorigins.entity;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.ibarnstormer.ibarnorigins.effect.OwnableStatusEffectInstance;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.logging.LogUtils;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.command.argument.ParticleEffectArgumentType;
import net.minecraft.entity.*;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtil;
import net.minecraft.potion.Potions;
import net.minecraft.registry.Registries;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.TypeFilter;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class OwnedAreaEffectCloudEntity extends Entity implements Ownable {

    private static final Logger LOGGER = LogUtils.getLogger();
    private static final TrackedData<Float> RADIUS = DataTracker.registerData(OwnedAreaEffectCloudEntity.class, TrackedDataHandlerRegistry.FLOAT);;
    private static final TrackedData<Integer> COLOR = DataTracker.registerData(OwnedAreaEffectCloudEntity.class, TrackedDataHandlerRegistry.INTEGER);;
    private static final TrackedData<Boolean> WAITING = DataTracker.registerData(OwnedAreaEffectCloudEntity.class, TrackedDataHandlerRegistry.BOOLEAN);;
    private static final TrackedData<ParticleEffect> PARTICLE_ID = DataTracker.registerData(OwnedAreaEffectCloudEntity.class, TrackedDataHandlerRegistry.PARTICLE);;
    private Potion potion;
    private final List<OwnableStatusEffectInstance> effects;
    private final Map<Entity, Integer> affectedEntities;
    private int duration;
    private int waitTime;
    private int reapplicationDelay;
    private boolean customColor;
    private int durationOnUse;
    private float radiusOnUse;
    private float radiusGrowth;
    @Nullable
    private LivingEntity owner;
    @Nullable
    private UUID ownerUuid;

    public OwnedAreaEffectCloudEntity(EntityType<?> type, World world) {
        super(type, world);
        this.potion = Potions.EMPTY;
        this.effects = Lists.newArrayList();
        this.affectedEntities = Maps.newHashMap();
        this.duration = 600;
        this.waitTime = 20;
        this.reapplicationDelay = 20;
        this.noClip = true;
    }

    public OwnedAreaEffectCloudEntity(World world, double x, double y, double z) {
        this(EntityType.AREA_EFFECT_CLOUD, world);
        this.setPosition(x, y, z);
    }

    public OwnedAreaEffectCloudEntity(World world, @Nullable LivingEntity owner, double x, double y, double z) {
        this(world, x, y, z);
        this.owner = owner;
        this.ownerUuid = owner != null ? owner.getUuid() : null;
    }

    @Override
    protected void initDataTracker() {
        this.getDataTracker().startTracking(COLOR, 0);
        this.getDataTracker().startTracking(RADIUS, 3.0F);
        this.getDataTracker().startTracking(WAITING, false);
        this.getDataTracker().startTracking(PARTICLE_ID, ParticleTypes.ENTITY_EFFECT);
    }

    public void setRadius(float radius) {
        if (!this.getWorld().isClient) {
            this.getDataTracker().set(RADIUS, MathHelper.clamp(radius, 0.0F, 32.0F));
        }

    }

    public void calculateDimensions() {
        double d = this.getX();
        double e = this.getY();
        double f = this.getZ();
        super.calculateDimensions();
        this.setPosition(d, e, f);
    }

    public float getRadius() {
        return this.getDataTracker().get(RADIUS);
    }

    public void setPotion(Potion potion) {
        this.potion = potion;
        if (!this.customColor) {
            this.updateColor();
        }

    }

    private void updateColor() {
        if (this.potion == Potions.EMPTY && this.effects.isEmpty()) {
            this.getDataTracker().set(COLOR, 0);
        } else {
            List<StatusEffectInstance> list = this.effects.stream().collect(Collectors.toUnmodifiableList());
            this.getDataTracker().set(COLOR, PotionUtil.getColor(PotionUtil.getPotionEffects(this.potion, list)));
        }

    }

    public void addEffect(OwnableStatusEffectInstance effect) {
        this.effects.add(effect);
        if (!this.customColor) {
            this.updateColor();
        }

    }

    public int getColor() {
        return this.getDataTracker().get(COLOR);
    }

    public void setColor(int rgb) {
        this.customColor = true;
        this.getDataTracker().set(COLOR, rgb);
    }

    public ParticleEffect getParticleType() {
        return this.getDataTracker().get(PARTICLE_ID);
    }

    public void setParticleType(ParticleEffect particle) {
        this.getDataTracker().set(PARTICLE_ID, particle);
    }

    protected void setWaiting(boolean waiting) {
        this.getDataTracker().set(WAITING, waiting);
    }

    public boolean isWaiting() {
        return this.getDataTracker().get(WAITING);
    }

    public int getDuration() {
        return this.duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getReapplicationDelay() {
        return this.reapplicationDelay;
    }

    public void setReapplicationDelay(int i) {
        this.reapplicationDelay = i;
    }

    public void tick() {
        super.tick();
        boolean bl = this.isWaiting();
        float f = this.getRadius();
        if (this.getWorld().isClient) {
            if (bl && this.random.nextBoolean()) {
                return;
            }

            ParticleEffect particleEffect = this.getParticleType();
            int i;
            float g;
            if (bl) {
                i = 2;
                g = 0.2F;
            } else {
                i = MathHelper.ceil(3.1415927F * f * f);
                g = f;
            }

            for(int j = 0; j < i; ++j) {
                float h = this.random.nextFloat() * 6.2831855F;
                float k = MathHelper.sqrt(this.random.nextFloat()) * g;
                double d = this.getX() + (double)(MathHelper.cos(h) * k);
                double e = this.getY();
                double l = this.getZ() + (double)(MathHelper.sin(h) * k);
                double n;
                double o;
                double p;
                if (particleEffect.getType() != ParticleTypes.ENTITY_EFFECT) {
                    if (bl) {
                        n = 0.0;
                        o = 0.0;
                        p = 0.0;
                    } else {
                        n = (0.5 - this.random.nextDouble()) * 0.15;
                        o = 0.009999999776482582;
                        p = (0.5 - this.random.nextDouble()) * 0.15;
                    }
                } else {
                    int m = bl && this.random.nextBoolean() ? 16777215 : this.getColor();
                    n = (float)(m >> 16 & 255) / 255.0F;
                    o = (float)(m >> 8 & 255) / 255.0F;
                    p = (float)(m & 255) / 255.0F;
                }

                this.getWorld().addImportantParticle(particleEffect, d, e, l, n, o, p);
            }
        } else {
            if (this.age >= this.waitTime + this.duration) {
                this.discard();
                return;
            }

            boolean bl2 = this.age < this.waitTime;
            if (bl != bl2) {
                this.setWaiting(bl2);
            }

            if (bl2) {
                return;
            }

            if (this.radiusGrowth != 0.0F) {
                f += this.radiusGrowth;
                if (f < 0.5F) {
                    this.discard();
                    return;
                }

                this.setRadius(f);
            }

            if (this.age % 5 == 0) {
                this.affectedEntities.entrySet().removeIf((entry) -> this.age >= entry.getValue());
                List<OwnableStatusEffectInstance> list = Lists.newArrayList();

                for (StatusEffectInstance statusEffectInstance : this.potion.getEffects()) {
                    list.add(new OwnableStatusEffectInstance(statusEffectInstance.getEffectType(), statusEffectInstance.mapDuration((ix) -> ix / 4), statusEffectInstance.getAmplifier(), statusEffectInstance.isAmbient(), statusEffectInstance.shouldShowParticles(), this.ownerUuid));
                }

                list.addAll(this.effects);
                if (list.isEmpty()) {
                    this.affectedEntities.clear();
                } else {
                    List<LivingEntity> list2 = this.getWorld().getEntitiesByType(TypeFilter.instanceOf(LivingEntity.class), this.getBoundingBox(), e -> !e.isSpectator() && this.ownerUuid != e.getUuid() && !(e instanceof Ownable o && o.getOwner() == this.getOwner()));
                    if (!list2.isEmpty()) {
                        Iterator<LivingEntity> var27 = list2.iterator();

                        while(true) {
                            double s;
                            LivingEntity livingEntity;
                            do {
                                do {
                                    do {
                                        if (!var27.hasNext()) {
                                            return;
                                        }

                                        livingEntity = var27.next();
                                    } while(this.affectedEntities.containsKey(livingEntity));
                                } while(!livingEntity.isAffectedBySplashPotions());

                                double q = livingEntity.getX() - this.getX();
                                double r = livingEntity.getZ() - this.getZ();
                                s = q * q + r * r;
                            } while(!(s <= (double)(f * f)));

                            this.affectedEntities.put(livingEntity, this.age + this.reapplicationDelay);

                            for (StatusEffectInstance statusEffectInstance2 : list) {
                                if (statusEffectInstance2.getEffectType().isInstant()) {
                                    statusEffectInstance2.getEffectType().applyInstantEffect(this, this.getOwner(), livingEntity, statusEffectInstance2.getAmplifier(), 0.5);
                                } else {
                                    livingEntity.addStatusEffect(new OwnableStatusEffectInstance(statusEffectInstance2, this.ownerUuid), this);
                                }
                            }

                            if (this.radiusOnUse != 0.0F) {
                                f += this.radiusOnUse;
                                if (f < 0.5F) {
                                    this.discard();
                                    return;
                                }

                                this.setRadius(f);
                            }

                            if (this.durationOnUse != 0) {
                                this.duration += this.durationOnUse;
                                if (this.duration <= 0) {
                                    this.discard();
                                    return;
                                }
                            }
                        }
                    }
                }
            }
        }

    }

    public float getRadiusOnUse() {
        return this.radiusOnUse;
    }

    public void setRadiusOnUse(float radiusOnUse) {
        this.radiusOnUse = radiusOnUse;
    }

    public float getRadiusGrowth() {
        return this.radiusGrowth;
    }

    public void setRadiusGrowth(float radiusGrowth) {
        this.radiusGrowth = radiusGrowth;
    }

    public int getDurationOnUse() {
        return this.durationOnUse;
    }

    public void setDurationOnUse(int durationOnUse) {
        this.durationOnUse = durationOnUse;
    }

    public int getWaitTime() {
        return this.waitTime;
    }

    public void setWaitTime(int waitTime) {
        this.waitTime = waitTime;
    }

    public void setOwner(@Nullable LivingEntity owner) {
        this.owner = owner;
        this.ownerUuid = owner == null ? null : owner.getUuid();
    }

    @Nullable
    public LivingEntity getOwner() {
        if (this.owner == null && this.ownerUuid != null && this.getWorld() instanceof ServerWorld) {
            Entity entity = ((ServerWorld)this.getWorld()).getEntity(this.ownerUuid);
            if (entity instanceof LivingEntity) {
                this.owner = (LivingEntity)entity;
            }
        }

        return this.owner;
    }

    protected void readCustomDataFromNbt(NbtCompound nbt) {
        this.age = nbt.getInt("Age");
        this.duration = nbt.getInt("Duration");
        this.waitTime = nbt.getInt("WaitTime");
        this.reapplicationDelay = nbt.getInt("ReapplicationDelay");
        this.durationOnUse = nbt.getInt("DurationOnUse");
        this.radiusOnUse = nbt.getFloat("RadiusOnUse");
        this.radiusGrowth = nbt.getFloat("RadiusPerTick");
        this.setRadius(nbt.getFloat("Radius"));
        if (nbt.containsUuid("Owner")) {
            this.ownerUuid = nbt.getUuid("Owner");
        }

        if (nbt.contains("Particle", 8)) {
            try {
                this.setParticleType(ParticleEffectArgumentType.readParameters(new StringReader(nbt.getString("Particle")), Registries.PARTICLE_TYPE.getReadOnlyWrapper()));
            } catch (CommandSyntaxException var5) {
                CommandSyntaxException commandSyntaxException = var5;
                LOGGER.warn("Couldn't load custom particle {}", nbt.getString("Particle"), commandSyntaxException);
            }
        }

        if (nbt.contains("Color", 99)) {
            this.setColor(nbt.getInt("Color"));
        }

        if (nbt.contains("Potion", 8)) {
            this.setPotion(PotionUtil.getPotion(nbt));
        }

        if (nbt.contains("Effects", 9)) {
            NbtList nbtList = nbt.getList("Effects", 10);
            this.effects.clear();

            for(int i = 0; i < nbtList.size(); ++i) {
                OwnableStatusEffectInstance instance = OwnableStatusEffectInstance.fromNbt(nbtList.getCompound(i));
                this.addEffect(instance);
            }
        }

    }

    protected void writeCustomDataToNbt(NbtCompound nbt) {
        nbt.putInt("Age", this.age);
        nbt.putInt("Duration", this.duration);
        nbt.putInt("WaitTime", this.waitTime);
        nbt.putInt("ReapplicationDelay", this.reapplicationDelay);
        nbt.putInt("DurationOnUse", this.durationOnUse);
        nbt.putFloat("RadiusOnUse", this.radiusOnUse);
        nbt.putFloat("RadiusPerTick", this.radiusGrowth);
        nbt.putFloat("Radius", this.getRadius());
        nbt.putString("Particle", this.getParticleType().asString());
        if (this.ownerUuid != null) {
            nbt.putUuid("Owner", this.ownerUuid);
        }

        if (this.customColor) {
            nbt.putInt("Color", this.getColor());
        }

        if (this.potion != Potions.EMPTY) {
            nbt.putString("Potion", Registries.POTION.getId(this.potion).toString());
        }

        if (!this.effects.isEmpty()) {
            NbtList nbtList = new NbtList();

            for (OwnableStatusEffectInstance statusEffectInstance : this.effects) {
                nbtList.add(statusEffectInstance.writeNbt(new NbtCompound()));
            }

            nbt.put("Effects", nbtList);
        }

    }

    public void onTrackedDataSet(TrackedData<?> data) {
        if (RADIUS.equals(data)) {
            this.calculateDimensions();
        }

        super.onTrackedDataSet(data);
    }

    public Potion getPotion() {
        return this.potion;
    }

    public PistonBehavior getPistonBehavior() {
        return PistonBehavior.IGNORE;
    }

    public EntityDimensions getDimensions(EntityPose pose) {
        return EntityDimensions.changing(this.getRadius() * 2.0F, 0.5F);
    }
}
