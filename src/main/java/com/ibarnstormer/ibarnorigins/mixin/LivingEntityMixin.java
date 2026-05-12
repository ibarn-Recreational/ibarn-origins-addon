package com.ibarnstormer.ibarnorigins.mixin;

import com.ibarnstormer.ibarnorigins.IbarnOriginsMain;
import com.ibarnstormer.ibarnorigins.effect.IExtendedStatusEffect;
import com.ibarnstormer.ibarnorigins.entity.IbarnOriginsEntity;
import com.ibarnstormer.ibarnorigins.entity.SoulFireBallEntity;
import com.ibarnstormer.ibarnorigins.registry.IOEffects;
import com.ibarnstormer.ibarnorigins.registry.IOParticles;
import com.ibarnstormer.ibarnorigins.utils.IOUtils;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CampfireBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Collection;
import java.util.Map;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements IbarnOriginsEntity {

    @Shadow public abstract float getBodyYaw();
    @Shadow public abstract void setBodyYaw(float bodyYaw);
    @Shadow public abstract float getHeadYaw();
    @Shadow public abstract boolean addStatusEffect(StatusEffectInstance effect);
    @Shadow public abstract boolean isInsideWall();
    @Shadow @Final private Map<RegistryEntry<StatusEffect>, StatusEffectInstance> activeStatusEffects;
    @Shadow protected abstract void onStatusEffectApplied(StatusEffectInstance effect, @Nullable Entity source);
    @Shadow protected abstract void onStatusEffectUpgraded(StatusEffectInstance effect, boolean reapplyEffect, @Nullable Entity source);
    @Shadow @Nullable private DamageSource lastDamageSource;
    @Shadow private long lastDamageTime;
    @Shadow public abstract boolean hasStatusEffect(RegistryEntry<StatusEffect> effect);

    @Shadow
    public abstract @Nullable LivingEntity getEntity();

    @Unique
    private StatusEffectInstance soulBurning = null;

    @Unique
    private final Identifier soulSpeedID = IbarnOriginsMain.IOIdentifier("soul_speed");

    @Unique
    private static final TrackedData<Integer> SPELL_CASTING_TICKS = DataTracker.registerData(LivingEntity.class, TrackedDataHandlerRegistry.INTEGER);
    @Unique
    private static final TrackedData<Boolean> IS_SOUL_MAGE = DataTracker.registerData(LivingEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    @Unique
    private static final TrackedData<Boolean> IS_SAND_PERSON = DataTracker.registerData(LivingEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    @Unique
    private static final TrackedData<Boolean> IS_ON_SOUL_MAGE_FIRE = DataTracker.registerData(LivingEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    @Unique
    private static final TrackedData<Boolean> IS_ON_SOUL_FIRE = DataTracker.registerData(LivingEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    @Unique
    private static final TrackedData<Boolean> IS_INFLATED = DataTracker.registerData(LivingEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    @Unique
    private static final TrackedData<Boolean> IS_SHAKING_FROM_FIRE_WEAKNESS = DataTracker.registerData(LivingEntity.class, TrackedDataHandlerRegistry.BOOLEAN);


    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Unique
    public void setSpellCastTicks(int i) {
        this.dataTracker.set(SPELL_CASTING_TICKS, i);
    }

    @Unique
    public int getSpellCastTicks() {
        return this.dataTracker.get(SPELL_CASTING_TICKS);
    }

    @Unique
    public boolean isSoulMage() {
        return this.dataTracker.get(IS_SOUL_MAGE);
    }

    @Unique
    public void setSoulMage(boolean b) {
        this.dataTracker.set(IS_SOUL_MAGE, b);
    }

    @Unique
    public boolean isSandPerson() {
        return this.dataTracker.get(IS_SAND_PERSON);
    }

    @Unique
    public void setSandPerson(boolean b) {
        this.dataTracker.set(IS_SAND_PERSON, b);
    }

    @Unique
    public boolean onSoulMageFire() {
        return this.dataTracker.get(IS_ON_SOUL_MAGE_FIRE);
    }

    @Unique
    public void setOnSoulMageFire(boolean b) {
        this.dataTracker.set(IS_ON_SOUL_MAGE_FIRE, b);
    }

    @Unique
    public boolean onSoulFire() {
        return this.dataTracker.get(IS_ON_SOUL_FIRE);
    }

    @Unique
    public void setOnSoulFire(boolean b) {
        this.dataTracker.set(IS_ON_SOUL_FIRE, b);
    }

    @Unique
    public boolean inflated() {
        return this.dataTracker.get(IS_INFLATED);
    }

    @Unique
    public void setInflated(boolean b) {
        this.dataTracker.set(IS_INFLATED, b);
    }

    @Unique
    public boolean fireWeaknessShaking() {
        return this.dataTracker.get(IS_SHAKING_FROM_FIRE_WEAKNESS);
    }

    @Unique
    public void setShakingFromFireWeakness(boolean b) {
        this.dataTracker.set(IS_SHAKING_FROM_FIRE_WEAKNESS, b);
    }

    @Unique
    private boolean isOnSoulSpeedBlock() {
        return this.getEntityWorld().getBlockState(this.getVelocityAffectingPos()).isIn(BlockTags.SOUL_SPEED_BLOCKS);
    }

    @Unique
    protected void displaySoulSpeedEffects() {
        Vec3d vec3d = this.getVelocity();
        this.getEntityWorld().addParticleClient(ParticleTypes.SOUL, this.getX() + (this.random.nextDouble() - (double)0.5F) * (double)this.getWidth(), this.getY() + 0.1, this.getZ() + (this.random.nextDouble() - (double)0.5F) * (double)this.getWidth(), vec3d.x * -0.2, 0.1, vec3d.z * -0.2);
        float f = this.random.nextFloat() * 0.4F + this.random.nextFloat() > 0.9F ? 0.6F : 0.0F;
        this.playSound(SoundEvents.PARTICLE_SOUL_ESCAPE.value(), f, 0.6F + this.random.nextFloat() * 0.4F);
    }

    @Inject(method = "initDataTracker", at = @At("TAIL"))
    private void livingEntity$initDataTracker(DataTracker.Builder builder, CallbackInfo ci) {
        builder.add(SPELL_CASTING_TICKS, 0);
        builder.add(IS_SOUL_MAGE, false);
        builder.add(IS_SAND_PERSON, false);
        builder.add(IS_ON_SOUL_MAGE_FIRE, false);
        builder.add(IS_ON_SOUL_FIRE, false);
        builder.add(IS_INFLATED, false);
        builder.add(IS_SHAKING_FROM_FIRE_WEAKNESS, false);
    }

    @Inject(method = "writeCustomData", at = @At("TAIL"))
    private void livingEntity$writeCustomData(WriteView view, CallbackInfo ci) {
        view.putInt("spellCastTicks", this.dataTracker.get(SPELL_CASTING_TICKS));
        view.putBoolean("isSoulMage", this.dataTracker.get(IS_SOUL_MAGE));
        view.putBoolean("isSandPerson", this.dataTracker.get(IS_SAND_PERSON));
        view.putBoolean("onSoulMageFire", this.dataTracker.get(IS_ON_SOUL_MAGE_FIRE));
        view.putBoolean("onSoulFire", this.dataTracker.get(IS_ON_SOUL_FIRE));
        view.putBoolean("isInflated", this.dataTracker.get(IS_INFLATED));
        view.putBoolean("isShakingFromFireWeakness", this.dataTracker.get(IS_SHAKING_FROM_FIRE_WEAKNESS));
    }

    @Inject(method = "readCustomData", at = @At("TAIL"))
    private void livingEntity$readCustomData(ReadView view, CallbackInfo ci) {
        this.dataTracker.set(SPELL_CASTING_TICKS, view.getInt("spellCastTicks", 0));
        this.dataTracker.set(IS_SOUL_MAGE, view.getBoolean("isSoulMage", false));
        this.dataTracker.set(IS_SAND_PERSON, view.getBoolean("isSandPerson", false));
        this.dataTracker.set(IS_ON_SOUL_MAGE_FIRE, view.getBoolean("onSoulMageFire", false));
        this.dataTracker.set(IS_ON_SOUL_FIRE, view.getBoolean("onSoulFire", false));
        this.dataTracker.set(IS_INFLATED, view.getBoolean("isInflated", false));
        this.dataTracker.set(IS_SHAKING_FROM_FIRE_WEAKNESS, view.getBoolean("isShakingFromFireWeakness", false));
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void livingEntity$tick(CallbackInfo ci) {
        int spellCastTicks = this.getSpellCastTicks();

        if(spellCastTicks > 0) {
            this.setSpellCastTicks(spellCastTicks - 1);
            this.setBodyYaw(this.getHeadYaw());

            if(this.getEntityWorld().isClient()) {

                float g = this.getBodyYaw() * 0.017453292F + MathHelper.cos((float)this.age * 0.6662F) * 0.25F;
                float h = MathHelper.cos(g);
                float i = MathHelper.sin(g);

                int xDelta = this.getEntityWorld().getRandom().nextBetween(-1, 1);
                int yDelta = this.getEntityWorld().getRandom().nextBetween(-1, 1);
                int zDelta = this.getEntityWorld().getRandom().nextBetween(-1, 1);

                this.getEntityWorld().addParticleClient(IOParticles.SOUL_MAGE_FLAME.get(), this.getX() + (double)h * 0.6, this.getY() + this.getBoundingBox().getLengthY() + 0.2, this.getZ() + (double)i * 0.6, 0.025 * xDelta, 0.01 * yDelta, 0.02 * zDelta);
                this.getEntityWorld().addParticleClient(IOParticles.SOUL_MAGE_FLAME.get(), this.getX() - (double)h * 0.6, this.getY() + this.getBoundingBox().getLengthY() + 0.2, this.getZ() - (double)i * 0.6, 0.025 * xDelta, 0.01 * yDelta, 0.02 * zDelta);
            }
        }

        // Inflation effect
        if(this.hasStatusEffect(IOEffects.INFLATION.getRef()) && this.isSneaking()) {
            this.addVelocity(this.getRotationVector().x * 0.035, -0.06, this.getRotationVector().z * 0.035);
            this.velocityDirty = true;
        }

        // Soul Mage built-in abilities
        if(this.isSoulMage()) {

            if(this.age % 20 == 0 && !this.getEntityWorld().isClient()) {
                // Optimized hardcoded power for fire / soul fire detection
                TagKey<Block> fire = TagKey.of(RegistryKeys.BLOCK, Identifier.of("ibarnorigins", "fire"));

                boolean foundSoulFire = false;
                boolean foundFire = false;

                for(int x = this.getBlockX() - 5; x < this.getBlockX() + 5; x++) {
                    for(int y = this.getBlockY() - 5; y < this.getBlockY() + 5; y++) {
                        for(int z = this.getBlockZ() - 5; z < this.getBlockZ() + 5; z++) {
                            BlockState block = this.getEntityWorld().getBlockState(new BlockPos(x, y, z));
                            try {
                                if(!foundSoulFire) foundSoulFire = (block.isIn(BlockTags.PIGLIN_REPELLENTS) && !block.isOf(Blocks.SOUL_CAMPFIRE)) || (block.isOf(Blocks.SOUL_CAMPFIRE) && block.get(CampfireBlock.LIT).equals(true));
                                if(!foundFire) foundFire = block.isIn(fire) || (block.isOf(Blocks.CAMPFIRE) && block.get(CampfireBlock.LIT).equals(true));
                            }
                            catch(Exception ignored) {}

                            if(foundFire && foundSoulFire) break;
                        }
                        if(foundFire && foundSoulFire) break;
                    }
                    if(foundFire && foundSoulFire) break;
                }

                if(foundSoulFire) {
                    this.addStatusEffect(new StatusEffectInstance(IOEffects.SOUL_FIRE_STRENGTH.getRef(), 30, 0, true, false, true));
                }

                if(foundFire) {
                    this.addStatusEffect(new StatusEffectInstance(IOEffects.FIRE_WEAKNESS.getRef(), 30, 0, true, false, true));
                    this.addStatusEffect(new StatusEffectInstance(StatusEffects.MINING_FATIGUE, 30, 0, false, false, false));
                }

            }

            if(this.getEntityWorld().isClient()) {
                // Particles
                if(this.hasStatusEffect(IOEffects.SOUL_FIRE_STRENGTH.getRef()) && this.age % 4 == 0) {
                    IOUtils.renderParticles(this.getEntityWorld(), (LivingEntity) (Object) this, ParticleTypes.SOUL_FIRE_FLAME);
                }

                if(this.isOnSoulSpeedBlock() && this.age % 5 == 0 && this.getVelocity().x != 0.0 && this.getVelocity().z != 0.0) {
                    this.displaySoulSpeedEffects();
                }
            }

            if(this.isOnSoulSpeedBlock()) {
                EntityAttributeInstance entityAttributeInstance = this.getEntity().getAttributeInstance(EntityAttributes.MOVEMENT_SPEED);
                if (entityAttributeInstance != null && !entityAttributeInstance.hasModifier(soulSpeedID)) {
                    entityAttributeInstance.addTemporaryModifier(new EntityAttributeModifier(soulSpeedID, 0.03F * (1.0F + (float) 3 * 0.35F), EntityAttributeModifier.Operation.ADD_VALUE));
                }
            }
            else {
                EntityAttributeInstance entityAttributeInstance = this.getEntity().getAttributeInstance(EntityAttributes.MOVEMENT_SPEED);
                if (entityAttributeInstance != null && entityAttributeInstance.hasModifier(soulSpeedID) && (!this.getEntityWorld().getBlockState(this.getSteppingPos()).isAir() || this.getEntity().isGliding())) {
                    entityAttributeInstance.removeModifier(soulSpeedID);
                }
            }

            if(this.getEntityWorld().getBlockState(this.getVelocityAffectingPos()).isOf(Blocks.SOUL_FIRE) || this.getEntityWorld().getBlockState(this.getBlockPos()).isOf(Blocks.SOUL_FIRE)) {
                this.addStatusEffect(new StatusEffectInstance(IOEffects.SOUL_FIRE_STRENGTH.getRef(), 60, 1, true, false, true));
                this.setFireTicks(0);
                this.setOnFire(false);
            }

            if(this.lastDamageSource != null && this.lastDamageSource.isIn(DamageTypeTags.IS_FIRE) && this.getEntityWorld().getTime() - this.lastDamageTime <= 2 && !this.getEntityWorld().isClient()) {
                this.addStatusEffect(new StatusEffectInstance(StatusEffects.BLINDNESS, 50, 0, true, false, false));
            }
        }

        // Sand Person built-in abilities
        if(this.isSandPerson()) {
            if((this.getEntityWorld().getBlockState(this.getVelocityAffectingPos()).isIn(BlockTags.SAND) || this.getEntityWorld().getBlockState(this.getBlockPos()).isIn(BlockTags.SAND)) && !this.getEntityWorld().isClient()) {
                this.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 20, 2, true, false, true));
                if(this.getEntityWorld().getBlockState(this.getBlockPos().up()).isIn(BlockTags.SAND)) {
                    this.addStatusEffect(new StatusEffectInstance(StatusEffects.INVISIBILITY, 10, 2, true, false, true));
                }
            }
        }
    }

    @Inject(method = "addStatusEffect(Lnet/minecraft/entity/effect/StatusEffectInstance;Lnet/minecraft/entity/Entity;)Z", at = @At(value = "HEAD"), cancellable = true)
    public void livingEntity$addStatusEffect(StatusEffectInstance effect, Entity source, CallbackInfoReturnable<Boolean> cir) {
        if(effect.getEffectType() == IOEffects.SOUL_FIRE.getRef()) {
            if(!this.isSoulMage()) {
                try {
                    StatusEffectInstance statusEffectInstance = this.activeStatusEffects.get(IOEffects.SOUL_FIRE.getRef());
                    if (statusEffectInstance == null) {
                        this.activeStatusEffects.put(effect.getEffectType(), effect);
                        this.onStatusEffectApplied(effect, source);

                        effect.onApplied(this.getEntity());
                        cir.setReturnValue(true);
                    } else if (statusEffectInstance.upgrade(effect)) {
                        this.onStatusEffectUpgraded(statusEffectInstance, true, source);

                        effect.onApplied(this.getEntity());
                        cir.setReturnValue(true);
                    } else {
                        effect.onApplied(this.getEntity());
                        cir.setReturnValue(false);
                    }
                } catch (Exception ignored) {
                    cir.setReturnValue(false);
                }
            }
            else {
                cir.setReturnValue(false);
            }
        }
    }

    @Inject(method = "clearStatusEffects", at = @At("HEAD"))
    public void livingEntity$clearStatusEffects_HEAD(CallbackInfoReturnable<Boolean> cir) {
        LivingEntity thisEntity = (LivingEntity) (Object) this;
        if(this.activeStatusEffects.containsKey(IOEffects.SOUL_FIRE.getRef()) && (!this.isSoulMage() && !(thisEntity instanceof PlayerEntity player && player.isCreative()))) {
            this.soulBurning = this.activeStatusEffects.get(IOEffects.SOUL_FIRE.getRef());
        }
    }

    @Inject(method = "clearStatusEffects", at = @At("TAIL"))
    public void livingEntity$clearStatusEffects_TAIL(CallbackInfoReturnable<Boolean> cir) {
        if(this.soulBurning != null) {
            this.addStatusEffect(soulBurning);
            this.soulBurning = null;
        }
    }

    @Inject(method = "onStatusEffectsRemoved", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/effect/StatusEffect;onRemoved(Lnet/minecraft/entity/attribute/AttributeContainer;)V"))
    private void livingEntity$onStatusEffectsRemoved(Collection<StatusEffectInstance> effects, CallbackInfo ci, @Local StatusEffectInstance instance) {
        if(instance.getEffectType().value() instanceof IExtendedStatusEffect extendedInstance && this.getEntity() != null && this.getEntity().getEntityWorld() instanceof ServerWorld serverWorld) {
            extendedInstance.onStatusEffectRemoved(serverWorld, this.getEntity(), instance.getAmplifier());
        }
    }

    @Inject(method = "onStatusEffectUpgraded", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/effect/StatusEffect;onRemoved(Lnet/minecraft/entity/attribute/AttributeContainer;)V"))
    private void livingEntity$onStatusEffectUpgraded(StatusEffectInstance instance, boolean reapplyEffect, Entity source, CallbackInfo ci) {
        if(instance.getEffectType().value() instanceof IExtendedStatusEffect extendedInstance && this.getEntity() != null && this.getEntity().getEntityWorld() instanceof ServerWorld serverWorld) {
            extendedInstance.onStatusEffectRemoved(serverWorld, this.getEntity(), instance.getAmplifier());
        }
    }

    @Inject(method = "getVelocityMultiplier", at = @At("RETURN"), cancellable = true)
    public void modulateVelocityMultiplier(CallbackInfoReturnable<Float> cir) {
        if(this.isSoulMage() && isOnSoulSpeedBlock()) cir.setReturnValue(1.0F);
    }


}
