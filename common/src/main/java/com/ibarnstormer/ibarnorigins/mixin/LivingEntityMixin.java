package com.ibarnstormer.ibarnorigins.mixin;

import com.ibarnstormer.ibarnorigins.IbarnOriginsMain;
import com.ibarnstormer.ibarnorigins.effect.IExtendedStatusEffect;
import com.ibarnstormer.ibarnorigins.entity.IbarnOriginsEntity;
import com.ibarnstormer.ibarnorigins.registry.IOEffects;
import com.ibarnstormer.ibarnorigins.registry.IOParticles;
import com.ibarnstormer.ibarnorigins.utils.IOUtils;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements IbarnOriginsEntity {

    @Shadow public abstract float getVisualRotationYInDegrees();
    @Shadow public abstract void setYBodyRot(float bodyYaw);
    @Shadow public abstract float getYHeadRot();
    @Shadow public abstract boolean addEffect(MobEffectInstance effect);
    @Shadow public abstract boolean isInWall();
    @Shadow @Final private Map<Holder<MobEffect>, MobEffectInstance> activeEffects;
    @Shadow protected abstract void onEffectAdded(MobEffectInstance effect, @Nullable Entity source);
    @Shadow protected abstract void onEffectUpdated(MobEffectInstance effect, boolean reapplyEffect, @Nullable Entity source);
    @Shadow @Nullable private DamageSource lastDamageSource;
    @Shadow private long lastDamageStamp;
    @Shadow public abstract boolean hasEffect(Holder<MobEffect> effect);

    @Unique
    public LivingEntity asLivingEntity() {
        return (LivingEntity) (Object) this;
    }

    @Unique
    private MobEffectInstance soulBurning = null;

    @Unique
    private final ResourceLocation soulSpeedID = IbarnOriginsMain.IOIdentifier("soul_speed");

    @Unique
    private static final EntityDataAccessor<Integer> SPELL_CASTING_TICKS = SynchedEntityData.defineId(LivingEntity.class, EntityDataSerializers.INT);
    @Unique
    private static final EntityDataAccessor<Boolean> IS_SOUL_MAGE = SynchedEntityData.defineId(LivingEntity.class, EntityDataSerializers.BOOLEAN);
    @Unique
    private static final EntityDataAccessor<Boolean> IS_SAND_PERSON = SynchedEntityData.defineId(LivingEntity.class, EntityDataSerializers.BOOLEAN);
    @Unique
    private static final EntityDataAccessor<Boolean> IS_ON_SOUL_MAGE_FIRE = SynchedEntityData.defineId(LivingEntity.class, EntityDataSerializers.BOOLEAN);
    @Unique
    private static final EntityDataAccessor<Boolean> IS_ON_SOUL_FIRE = SynchedEntityData.defineId(LivingEntity.class, EntityDataSerializers.BOOLEAN);
    @Unique
    private static final EntityDataAccessor<Boolean> IS_INFLATED = SynchedEntityData.defineId(LivingEntity.class, EntityDataSerializers.BOOLEAN);
    @Unique
    private static final EntityDataAccessor<Boolean> IS_SHAKING_FROM_FIRE_WEAKNESS = SynchedEntityData.defineId(LivingEntity.class, EntityDataSerializers.BOOLEAN);


    public LivingEntityMixin(EntityType<?> type, Level world) {
        super(type, world);
    }

    @Unique
    public void setSpellCastTicks(int i) {
        this.entityData.set(SPELL_CASTING_TICKS, i);
    }

    @Unique
    public int getSpellCastTicks() {
        return this.entityData.get(SPELL_CASTING_TICKS);
    }

    @Unique
    public boolean isSoulMage() {
        return this.entityData.get(IS_SOUL_MAGE);
    }

    @Unique
    public void setSoulMage(boolean b) {
        this.entityData.set(IS_SOUL_MAGE, b);
    }

    @Unique
    public boolean isSandPerson() {
        return this.entityData.get(IS_SAND_PERSON);
    }

    @Unique
    public void setSandPerson(boolean b) {
        this.entityData.set(IS_SAND_PERSON, b);
    }

    @Unique
    public boolean onSoulMageFire() {
        return this.entityData.get(IS_ON_SOUL_MAGE_FIRE);
    }

    @Unique
    public void setOnSoulMageFire(boolean b) {
        this.entityData.set(IS_ON_SOUL_MAGE_FIRE, b);
    }

    @Unique
    public boolean onSoulFire() {
        return this.entityData.get(IS_ON_SOUL_FIRE);
    }

    @Unique
    public void setOnSoulFire(boolean b) {
        this.entityData.set(IS_ON_SOUL_FIRE, b);
    }

    @Unique
    public boolean inflated() {
        return this.entityData.get(IS_INFLATED);
    }

    @Unique
    public void setInflated(boolean b) {
        this.entityData.set(IS_INFLATED, b);
    }

    @Unique
    public boolean fireWeaknessShaking() {
        return this.entityData.get(IS_SHAKING_FROM_FIRE_WEAKNESS);
    }

    @Unique
    public void setShakingFromFireWeakness(boolean b) {
        this.entityData.set(IS_SHAKING_FROM_FIRE_WEAKNESS, b);
    }

    @Unique
    private boolean isOnSoulSpeedBlock() {
        return this.level().getBlockState(this.getBlockPosBelowThatAffectsMyMovement()).is(BlockTags.SOUL_SPEED_BLOCKS);
    }

    @Unique
    protected void displaySoulSpeedEffects() {
        Vec3 vec3d = this.getDeltaMovement();
        this.level().addParticle(ParticleTypes.SOUL, this.getX() + (this.random.nextDouble() - (double)0.5F) * (double)this.getBbWidth(), this.getY() + 0.1, this.getZ() + (this.random.nextDouble() - (double)0.5F) * (double)this.getBbWidth(), vec3d.x * -0.2, 0.1, vec3d.z * -0.2);
        float f = this.random.nextFloat() * 0.4F + this.random.nextFloat() > 0.9F ? 0.6F : 0.0F;
        this.playSound(SoundEvents.SOUL_ESCAPE.value(), f, 0.6F + this.random.nextFloat() * 0.4F);
    }

    @Inject(method = "defineSynchedData", at = @At("TAIL"))
    private void livingEntity$initDataTracker(SynchedEntityData.Builder builder, CallbackInfo ci) {
        builder.define(SPELL_CASTING_TICKS, 0);
        builder.define(IS_SOUL_MAGE, false);
        builder.define(IS_SAND_PERSON, false);
        builder.define(IS_ON_SOUL_MAGE_FIRE, false);
        builder.define(IS_ON_SOUL_FIRE, false);
        builder.define(IS_INFLATED, false);
        builder.define(IS_SHAKING_FROM_FIRE_WEAKNESS, false);
    }

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    private void livingEntity$writeCustomData(CompoundTag compound, CallbackInfo ci) {
        compound.putInt("spellCastTicks", this.entityData.get(SPELL_CASTING_TICKS));
        compound.putBoolean("isSoulMage", this.entityData.get(IS_SOUL_MAGE));
        compound.putBoolean("isSandPerson", this.entityData.get(IS_SAND_PERSON));
        compound.putBoolean("onSoulMageFire", this.entityData.get(IS_ON_SOUL_MAGE_FIRE));
        compound.putBoolean("onSoulFire", this.entityData.get(IS_ON_SOUL_FIRE));
        compound.putBoolean("isInflated", this.entityData.get(IS_INFLATED));
        compound.putBoolean("isShakingFromFireWeakness", this.entityData.get(IS_SHAKING_FROM_FIRE_WEAKNESS));
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    private void livingEntity$readCustomData(CompoundTag compound, CallbackInfo ci) {
        this.entityData.set(SPELL_CASTING_TICKS, compound.getInt("spellCastTicks"));
        this.entityData.set(IS_SOUL_MAGE, compound.getBoolean("isSoulMage"));
        this.entityData.set(IS_SAND_PERSON, compound.getBoolean("isSandPerson"));
        this.entityData.set(IS_ON_SOUL_MAGE_FIRE, compound.getBoolean("onSoulMageFire"));
        this.entityData.set(IS_ON_SOUL_FIRE, compound.getBoolean("onSoulFire"));
        this.entityData.set(IS_INFLATED, compound.getBoolean("isInflated"));
        this.entityData.set(IS_SHAKING_FROM_FIRE_WEAKNESS, compound.getBoolean("isShakingFromFireWeakness"));
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void livingEntity$tick(CallbackInfo ci) {
        int spellCastTicks = this.getSpellCastTicks();

        if(spellCastTicks > 0) {
            this.setSpellCastTicks(spellCastTicks - 1);
            this.setYBodyRot(this.getYHeadRot());

            if(this.level().isClientSide()) {

                float g = this.getVisualRotationYInDegrees() * 0.017453292F + Mth.cos((float)this.tickCount * 0.6662F) * 0.25F;
                float h = Mth.cos(g);
                float i = Mth.sin(g);

                int xDelta = this.level().getRandom().nextIntBetweenInclusive(-1, 1);
                int yDelta = this.level().getRandom().nextIntBetweenInclusive(-1, 1);
                int zDelta = this.level().getRandom().nextIntBetweenInclusive(-1, 1);

                this.level().addParticle(IOParticles.SOUL_MAGE_FLAME.get(), this.getX() + (double)h * 0.6, this.getY() + this.getBoundingBox().getYsize() + 0.2, this.getZ() + (double)i * 0.6, 0.025 * xDelta, 0.01 * yDelta, 0.02 * zDelta);
                this.level().addParticle(IOParticles.SOUL_MAGE_FLAME.get(), this.getX() - (double)h * 0.6, this.getY() + this.getBoundingBox().getYsize() + 0.2, this.getZ() - (double)i * 0.6, 0.025 * xDelta, 0.01 * yDelta, 0.02 * zDelta);
            }
        }

        // Inflation effect
        if(this.hasEffect(IOEffects.INFLATION.getRef()) && this.isShiftKeyDown()) {
            this.push(this.getLookAngle().x * 0.035, -0.06, this.getLookAngle().z * 0.035);
            this.hasImpulse = true;
        }

        // Soul Mage built-in abilities
        if(this.isSoulMage()) {

            if(this.tickCount % 20 == 0 && !this.level().isClientSide()) {
                // Optimized hardcoded power for fire / soul fire detection
                TagKey<Block> fire = TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath("ibarnorigins", "fire"));

                boolean foundSoulFire = false;
                boolean foundFire = false;

                for(int x = this.getBlockX() - 5; x < this.getBlockX() + 5; x++) {
                    for(int y = this.getBlockY() - 5; y < this.getBlockY() + 5; y++) {
                        for(int z = this.getBlockZ() - 5; z < this.getBlockZ() + 5; z++) {
                            BlockState block = this.level().getBlockState(new BlockPos(x, y, z));
                            try {
                                if(!foundSoulFire) foundSoulFire = (block.is(BlockTags.PIGLIN_REPELLENTS) && !block.is(Blocks.SOUL_CAMPFIRE)) || (block.is(Blocks.SOUL_CAMPFIRE) && block.getValue(CampfireBlock.LIT).equals(true));
                                if(!foundFire) foundFire = block.is(fire) || (block.is(Blocks.CAMPFIRE) && block.getValue(CampfireBlock.LIT).equals(true));
                            }
                            catch(Exception ignored) {}

                            if(foundFire && foundSoulFire) break;
                        }
                        if(foundFire && foundSoulFire) break;
                    }
                    if(foundFire && foundSoulFire) break;
                }

                if(foundSoulFire) {
                    this.addEffect(new MobEffectInstance(IOEffects.SOUL_FIRE_STRENGTH.getRef(), 30, 0, true, false, true));
                }

                if(foundFire) {
                    this.addEffect(new MobEffectInstance(IOEffects.FIRE_WEAKNESS.getRef(), 30, 0, true, false, true));
                    this.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 30, 0, false, false, false));
                }

            }

            if(this.level().isClientSide()) {
                // Particles
                if(this.hasEffect(IOEffects.SOUL_FIRE_STRENGTH.getRef()) && this.tickCount % 4 == 0) {
                    IOUtils.renderParticles(this.level(), (LivingEntity) (Object) this, ParticleTypes.SOUL_FIRE_FLAME);
                }

                if(this.isOnSoulSpeedBlock() && this.tickCount % 5 == 0 && this.getDeltaMovement().x != 0.0 && this.getDeltaMovement().z != 0.0) {
                    this.displaySoulSpeedEffects();
                }
            }

            if(this.isOnSoulSpeedBlock()) {
                AttributeInstance entityAttributeInstance = this.asLivingEntity().getAttribute(Attributes.MOVEMENT_SPEED);
                if (entityAttributeInstance != null && !entityAttributeInstance.hasModifier(soulSpeedID)) {
                    entityAttributeInstance.addTransientModifier(new AttributeModifier(soulSpeedID, 0.03F * (1.0F + (float) 3 * 0.35F), AttributeModifier.Operation.ADD_VALUE));
                }
            }
            else {
                AttributeInstance entityAttributeInstance = this.asLivingEntity().getAttribute(Attributes.MOVEMENT_SPEED);
                if (entityAttributeInstance != null && entityAttributeInstance.hasModifier(soulSpeedID) && (!this.level().getBlockState(this.getOnPos()).isAir() || this.asLivingEntity().isFallFlying())) {
                    entityAttributeInstance.removeModifier(soulSpeedID);
                }
            }

            if(this.level().getBlockState(this.getBlockPosBelowThatAffectsMyMovement()).is(Blocks.SOUL_FIRE) || this.level().getBlockState(this.blockPosition()).is(Blocks.SOUL_FIRE)) {
                this.addEffect(new MobEffectInstance(IOEffects.SOUL_FIRE_STRENGTH.getRef(), 60, 1, true, false, true));
                this.setRemainingFireTicks(0);
                this.setSharedFlagOnFire(false);
            }

            if(this.lastDamageSource != null && this.lastDamageSource.is(DamageTypeTags.IS_FIRE) && this.level().getGameTime() - this.lastDamageStamp <= 2 && !this.level().isClientSide()) {
                this.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 50, 0, true, false, false));
            }
        }

        // Sand Person built-in abilities
        if(this.isSandPerson()) {
            if((this.level().getBlockState(this.getBlockPosBelowThatAffectsMyMovement()).is(BlockTags.SAND) || this.level().getBlockState(this.blockPosition()).is(BlockTags.SAND)) && !this.level().isClientSide()) {
                this.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 20, 2, true, false, true));
                if(this.level().getBlockState(this.blockPosition().above()).is(BlockTags.SAND)) {
                    this.addEffect(new MobEffectInstance(MobEffects.INVISIBILITY, 10, 2, true, false, true));
                }
            }
        }
    }

    @Inject(method = "addEffect(Lnet/minecraft/world/effect/MobEffectInstance;Lnet/minecraft/world/entity/Entity;)Z", at = @At(value = "HEAD"), cancellable = true)
    public void livingEntity$addStatusEffect(MobEffectInstance effect, Entity source, CallbackInfoReturnable<Boolean> cir) {
        if(effect.getEffect() == IOEffects.SOUL_FIRE.getRef()) {
            if(!this.isSoulMage()) {
                try {
                    MobEffectInstance statusEffectInstance = this.activeEffects.get(IOEffects.SOUL_FIRE.getRef());
                    if (statusEffectInstance == null) {
                        this.activeEffects.put(effect.getEffect(), effect);
                        this.onEffectAdded(effect, source);

                        effect.onEffectStarted(this.asLivingEntity());
                        cir.setReturnValue(true);
                    } else if (statusEffectInstance.update(effect)) {
                        this.onEffectUpdated(statusEffectInstance, true, source);

                        effect.onEffectStarted(this.asLivingEntity());
                        cir.setReturnValue(true);
                    } else {
                        effect.onEffectStarted(this.asLivingEntity());
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

    @Inject(method = "removeAllEffects", at = @At("HEAD"))
    public void livingEntity$clearStatusEffects_HEAD(CallbackInfoReturnable<Boolean> cir) {
        LivingEntity thisEntity = (LivingEntity) (Object) this;
        if(this.activeEffects.containsKey(IOEffects.SOUL_FIRE.getRef()) && (!this.isSoulMage() && !(thisEntity instanceof Player player && player.isCreative()))) {
            this.soulBurning = this.activeEffects.get(IOEffects.SOUL_FIRE.getRef());
        }
    }

    @Inject(method = "removeAllEffects", at = @At("TAIL"))
    public void livingEntity$clearStatusEffects_TAIL(CallbackInfoReturnable<Boolean> cir) {
        if(this.soulBurning != null) {
            this.addEffect(soulBurning);
            this.soulBurning = null;
        }
    }

    @Inject(method = "onEffectRemoved", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/effect/MobEffect;removeAttributeModifiers(Lnet/minecraft/world/entity/ai/attributes/AttributeMap;)V"))
    private void livingEntity$onStatusEffectsRemoved(MobEffectInstance effectInstance, CallbackInfo ci, @Local MobEffectInstance instance) {
        if(instance.getEffect().value() instanceof IExtendedStatusEffect extendedInstance && this.asLivingEntity() != null && this.asLivingEntity().level() instanceof ServerLevel serverWorld) {
            extendedInstance.onStatusEffectRemoved(serverWorld, this.asLivingEntity(), instance.getAmplifier());
        }
    }

    @Inject(method = "onEffectUpdated", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/effect/MobEffect;removeAttributeModifiers(Lnet/minecraft/world/entity/ai/attributes/AttributeMap;)V"))
    private void livingEntity$onStatusEffectUpgraded(MobEffectInstance instance, boolean reapplyEffect, Entity source, CallbackInfo ci) {
        if(instance.getEffect().value() instanceof IExtendedStatusEffect extendedInstance && this.asLivingEntity() != null && this.asLivingEntity().level() instanceof ServerLevel serverWorld) {
            extendedInstance.onStatusEffectRemoved(serverWorld, this.asLivingEntity(), instance.getAmplifier());
        }
    }

    @Inject(method = "getBlockSpeedFactor", at = @At("RETURN"), cancellable = true)
    public void modulateVelocityMultiplier(CallbackInfoReturnable<Float> cir) {
        if(this.isSoulMage() && isOnSoulSpeedBlock()) cir.setReturnValue(1.0F);
    }

}
