package com.ibarnstormer.ibarnorigins.registry;

import com.ibarnstormer.ibarnorigins.IbarnOriginsMain;
import com.ibarnstormer.ibarnorigins.effect.*;
import com.ibarnstormer.ibarnorigins.registry.utils.IORegisterWrapper;
import com.ibarnstormer.ibarnorigins.registry.utils.RegistryObjectWrapper;
import net.minecraft.world.effect.MobEffect;

public class IOEffects {

    public static RegistryObjectWrapper<MobEffect> SOUL_FIRE;
    public static RegistryObjectWrapper<MobEffect> CASTING_SOUL_FIREBALL;
    public static RegistryObjectWrapper<MobEffect> SOUL_FIRE_STRENGTH;
    public static RegistryObjectWrapper<MobEffect> FIRE_WEAKNESS;
    public static RegistryObjectWrapper<MobEffect> INFLATION;

    public static RegistryObjectWrapper<MobEffect> GRANT_SOUL_MAGE;
    public static RegistryObjectWrapper<MobEffect> GRANT_SAND_PERSON;
    public static RegistryObjectWrapper<MobEffect> REVOKE_SOUL_MAGE;
    public static RegistryObjectWrapper<MobEffect> REVOKE_SAND_PERSON;


    public static void init(IORegisterWrapper<?, MobEffect> register) {

        SOUL_FIRE = register.register(IbarnOriginsMain.IOIdentifier("soul_fire_effect"), SoulFireEffect::new);
        CASTING_SOUL_FIREBALL = register.register(IbarnOriginsMain.IOIdentifier("casting_soul_fireball_effect"), CastingSoulFireballEffect::new);
        SOUL_FIRE_STRENGTH = register.register(IbarnOriginsMain.IOIdentifier("soul_fire_strength_effect"), SoulFireStrengthEffect::new);
        FIRE_WEAKNESS = register.register(IbarnOriginsMain.IOIdentifier("fire_weakness_effect"), FireWeaknessEffect::new);
        INFLATION = register.register(IbarnOriginsMain.IOIdentifier("inflation_effect"), GhasterInflateEffect::new);

        GRANT_SOUL_MAGE = register.register(IbarnOriginsMain.IOIdentifier("grant_soul_mage_attributes"), GrantSoulMageAttributesEffect::new);
        GRANT_SAND_PERSON = register.register(IbarnOriginsMain.IOIdentifier("grant_sand_person_attributes"), GrantSandPersonAttributesEffect::new);
        REVOKE_SOUL_MAGE = register.register(IbarnOriginsMain.IOIdentifier("revoke_soul_mage_attributes"), RevokeSoulMageAttributesEffect::new);
        REVOKE_SAND_PERSON = register.register(IbarnOriginsMain.IOIdentifier("revoke_sand_person_attributes"), RevokeSandPersonAttributesEffect::new);
    }

}
