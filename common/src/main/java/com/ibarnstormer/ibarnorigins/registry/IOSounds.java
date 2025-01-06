package com.ibarnstormer.ibarnorigins.registry;

import com.ibarnstormer.ibarnorigins.IbarnOriginsMain;
import com.ibarnstormer.ibarnorigins.registry.utils.IORegisterWrapper;
import com.ibarnstormer.ibarnorigins.registry.utils.RegistryObjectWrapper;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class IOSounds {

    public static final Identifier SOUL_BLAST_ID = IbarnOriginsMain.IOIdentifier("soul_blast");
    public static final Identifier KI_BLAST_CHARGE_ID = IbarnOriginsMain.IOIdentifier("ki_blast_charge");
    public static final Identifier KI_BLAST_FIRE_ID = IbarnOriginsMain.IOIdentifier("ki_blast_fire");

    public static RegistryObjectWrapper<SoundEvent> SOUL_BLAST;
    public static RegistryObjectWrapper<SoundEvent> KI_BLAST_CHARGE;
    public static RegistryObjectWrapper<SoundEvent> KI_BLAST_FIRE;


    public static void init(IORegisterWrapper<?, SoundEvent> register) {
        SOUL_BLAST = register.register(SOUL_BLAST_ID, () -> SoundEvent.of(SOUL_BLAST_ID));
        KI_BLAST_CHARGE = register.register(KI_BLAST_CHARGE_ID, () -> SoundEvent.of(KI_BLAST_CHARGE_ID));
        KI_BLAST_FIRE = register.register(KI_BLAST_FIRE_ID, () -> SoundEvent.of(KI_BLAST_FIRE_ID));
    }

}
