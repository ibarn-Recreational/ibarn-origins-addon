package com.ibarnstormer.ibarnorigins.registry;

import com.ibarnstormer.ibarnorigins.registry.utils.IORegisterWrapper;
import com.ibarnstormer.ibarnorigins.registry.utils.RegistryObjectWrapper;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;

import java.util.function.Supplier;

public class IOFabricRegisterWrapper<T> extends IORegisterWrapper<Registry<T>, T> {

    public IOFabricRegisterWrapper(Registry<T> register) {
        super(register);
    }

    @Override
    public RegistryObjectWrapper<T> register(Identifier id, Supplier<T> supplier) {
        RegistryEntry.Reference<T> value = Registry.registerReference(this.register, id, supplier.get());
        return new FabricRegistryObject<>(id, value);
    }
}