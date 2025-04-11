package com.ibarnstormer.ibarnorigins.mixin;

import com.ibarnstormer.ibarnorigins.entity.IbarnOriginsEntity;
import com.ibarnstormer.ibarnorigins.registry.IOEffects;
import com.ibarnstormer.ibarnorigins.registry.IOParticles;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.util.math.MathHelper;
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

import java.util.Map;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements IbarnOriginsEntity {

    @Shadow
    protected abstract boolean isOnSoulSpeedBlock();

    @Shadow public abstract float getBodyYaw();

    @Shadow public abstract void setBodyYaw(float bodyYaw);

    @Shadow public abstract float getHeadYaw();

    @Shadow public abstract boolean addStatusEffect(StatusEffectInstance effect);

    @Shadow public abstract boolean isInsideWall();

    @Shadow protected abstract void onStatusEffectApplied(StatusEffectInstance effect, @Nullable Entity source);

    @Shadow @Final private Map<StatusEffect, StatusEffectInstance> activeStatusEffects;

    @Shadow protected abstract void onStatusEffectUpgraded(StatusEffectInstance effect, boolean reapplyEffect, @Nullable Entity source);

    @Shadow public abstract boolean addStatusEffect(StatusEffectInstance effect, @Nullable Entity source);

    @Shadow @Nullable private DamageSource lastDamageSource;
    @Shadow private long lastDamageTime;
    @Unique
    private StatusEffectInstance soulBurning = null;

    @Unique
    private static final TrackedData<Integer> SPELL_CASTING_TICKS = DataTracker.registerData(LivingEntity.class, TrackedDataHandlerRegistry.INTEGER);
    @Unique
    private static final TrackedData<Boolean> IS_SOUL_MAGE = DataTracker.registerData(LivingEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    @Unique
    private static final TrackedData<Boolean> IS_SAND_PERSON = DataTracker.registerData(LivingEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    @Unique
    private static final TrackedData<Boolean> IS_ON_SOUL_MAGE_FIRE = DataTracker.registerData(LivingEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

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

    @Inject(method = "initDataTracker", at = @At("TAIL"))
    private void livingEntity$initDataTracker(CallbackInfo ci) {
        this.dataTracker.startTracking(SPELL_CASTING_TICKS, 0);
        this.dataTracker.startTracking(IS_SOUL_MAGE, false);
        this.dataTracker.startTracking(IS_SAND_PERSON, false);
        this.dataTracker.startTracking(IS_ON_SOUL_MAGE_FIRE, false);
    }

    @Inject(method = "writeCustomDataToNbt", at = @At("TAIL"))
    private void livingEntity$writeCustomDataToNbt(NbtCompound nbt, CallbackInfo ci) {
        nbt.putInt("spellCastTicks", this.dataTracker.get(SPELL_CASTING_TICKS));
        nbt.putBoolean("isSoulMage", this.dataTracker.get(IS_SOUL_MAGE));
        nbt.putBoolean("isSandPerson", this.dataTracker.get(IS_SAND_PERSON));
        nbt.putBoolean("onSoulMageFire", this.dataTracker.get(IS_ON_SOUL_MAGE_FIRE));
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
    private void livingEntity$readCustomDataFromNbt(NbtCompound nbt, CallbackInfo ci) {
        this.dataTracker.set(SPELL_CASTING_TICKS, nbt.getInt("spellCastTicks"));
        this.dataTracker.set(IS_SOUL_MAGE, nbt.getBoolean("isSoulMage"));
        this.dataTracker.set(IS_SAND_PERSON, nbt.getBoolean("isSandPerson"));
        this.dataTracker.set(IS_ON_SOUL_MAGE_FIRE, nbt.getBoolean("onSoulMageFire"));
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void livingEntity$tick(CallbackInfo ci) {
        int spellCastTicks = this.getSpellCastTicks();

        if(spellCastTicks > 0) {
            this.setSpellCastTicks(spellCastTicks - 1);
            this.setBodyYaw(this.getHeadYaw());

            if(this.getWorld().isClient()) {

                float g = this.getBodyYaw() * 0.017453292F + MathHelper.cos((float)this.age * 0.6662F) * 0.25F;
                float h = MathHelper.cos(g);
                float i = MathHelper.sin(g);

                int xDelta = getWorld().getRandom().nextBetween(-1, 1);
                int zDelta = getWorld().getRandom().nextBetween(-1, 1);

                getWorld().addParticle(IOParticles.SOUL_MAGE_FLAME.get(), this.getX() + (double)h * 0.6, this.getY() + this.getBoundingBox().getYLength() + 0.2, this.getZ() + (double)i * 0.6, 0.04 * xDelta, 0.03, 0.035 * zDelta);
                getWorld().addParticle(IOParticles.SOUL_MAGE_FLAME.get(), this.getX() - (double)h * 0.6, this.getY() + this.getBoundingBox().getYLength() + 0.2, this.getZ() - (double)i * 0.6, 0.04 * xDelta, 0.03, 0.035 * zDelta);
            }
        }

        // Soul Mage built-in abilities
        if(this.isSoulMage()) {
            if(this.getWorld().getBlockState(this.getVelocityAffectingPos()).isOf(Blocks.SOUL_FIRE) || this.getWorld().getBlockState(this.getBlockPos()).isOf(Blocks.SOUL_FIRE)) {
                this.addStatusEffect(new StatusEffectInstance(IOEffects.SOUL_FIRE_STRENGTH.get(), 60, 1, true, false, false));
                this.setFireTicks(0);
                this.setOnFire(false);
            }

            if(this.lastDamageSource != null && this.lastDamageSource.isIn(DamageTypeTags.IS_FIRE) && this.getWorld().getTime() - this.lastDamageTime <= 2) {
                this.addStatusEffect(new StatusEffectInstance(StatusEffects.BLINDNESS, 50, 0, true, false, false));
            }
        }

        // Sand Person built-in abilities
        if(this.isSandPerson()) {
            if(this.getWorld().getBlockState(this.getVelocityAffectingPos()).isIn(BlockTags.SAND) || this.getWorld().getBlockState(this.getBlockPos()).isIn(BlockTags.SAND)) {
                this.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 20, 2, true, false, false));
                if(this.getWorld().getBlockState(this.getBlockPos().up()).isIn(BlockTags.SAND)) {
                    this.addStatusEffect(new StatusEffectInstance(StatusEffects.INVISIBILITY, 10, 2, true, false, false));
                }
            }
        }
    }

    @Inject(method = "addStatusEffect(Lnet/minecraft/entity/effect/StatusEffectInstance;Lnet/minecraft/entity/Entity;)Z", at = @At(value = "HEAD"), cancellable = true)
    public void livingEntity$addStatusEffect(StatusEffectInstance effect, Entity source, CallbackInfoReturnable<Boolean> cir) {
        if(effect.getEffectType() == IOEffects.SOUL_FIRE.get() && !this.isSoulMage()) {
            try {
                StatusEffectInstance statusEffectInstance = this.activeStatusEffects.get(effect.getEffectType());
                if (statusEffectInstance == null) {
                    this.activeStatusEffects.put(effect.getEffectType(), effect);
                    this.onStatusEffectApplied(effect, source);
                    cir.setReturnValue(true);
                } else if (statusEffectInstance.upgrade(effect)) {
                    this.onStatusEffectUpgraded(statusEffectInstance, true, source);
                    cir.setReturnValue(true);
                } else {
                    cir.setReturnValue(false);
                }
            }
            catch(Exception ignored) {
                cir.setReturnValue(false);
            }
        }
    }

    @Inject(method = "clearStatusEffects", at = @At("HEAD"))
    public void livingEntity$clearStatusEffects_HEAD(CallbackInfoReturnable<Boolean> cir) {
        LivingEntity thisEntity = (LivingEntity) (Object) this;
        if(this.activeStatusEffects.containsKey(IOEffects.SOUL_FIRE.get()) && (!this.isSoulMage() && !(thisEntity instanceof PlayerEntity player && player.isCreative()))) {
            this.soulBurning = this.activeStatusEffects.get(IOEffects.SOUL_FIRE.get());
        }
    }

    @Inject(method = "clearStatusEffects", at = @At("TAIL"))
    public void livingEntity$clearStatusEffects_TAIL(CallbackInfoReturnable<Boolean> cir) {
        if(this.soulBurning != null) {
            this.addStatusEffect(soulBurning);
            this.soulBurning = null;
        }
    }

    @ModifyVariable(method = "addSoulSpeedBoostIfNeeded()V", at = @At(value = "STORE"), ordinal = 0)
    public int modifySoulSpeedLevel(int i) {
        if(this.isSoulMage()) {
            return i + 3;
        }
        return i;
    }

    @Inject(method = "addSoulSpeedBoostIfNeeded()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;damage(ILnet/minecraft/entity/LivingEntity;Ljava/util/function/Consumer;)V"), cancellable = true)
    public void preventItemBreak(CallbackInfo ci) {
        if(this.isSoulMage()) ci.cancel();
    }

    @Inject(method = "getVelocityMultiplier", at = @At("RETURN"), cancellable = true)
    public void modulateVelocityMultiplier(CallbackInfoReturnable<Float> cir) {
        if(this.isSoulMage() && isOnSoulSpeedBlock()) cir.setReturnValue(1.0F);
    }

    @Inject(method = "shouldDisplaySoulSpeedEffects", at = @At("RETURN"), cancellable = true)
    public void displaySoulParticles(CallbackInfoReturnable<Boolean> cir) {
        if(this.isSoulMage()) {
            cir.setReturnValue(this.age % 5 == 0 && this.getVelocity().x != 0.0 && this.getVelocity().z != 0.0 && !this.isSpectator() && this.isOnSoulSpeedBlock());
        }
    }

}
