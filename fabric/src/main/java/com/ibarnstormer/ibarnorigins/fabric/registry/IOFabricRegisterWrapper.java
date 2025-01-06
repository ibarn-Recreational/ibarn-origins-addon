package com.ibarnstormer.ibarnorigins.fabric.registry;

import com.ibarnstormer.ibarnorigins.registry.utils.IORegisterWrapper;
import com.ibarnstormer.ibarnorigins.registry.utils.RegistryObjectWrapper;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.function.Supplier;

public class IOFabricRegisterWrapper<T> extends IORegisterWrapper<Registry<T>, T> {

    public IOFabricRegisterWrapper(Registry<T> register) {
        super(register);
    }

    @Override
    public RegistryObjectWrapper<T> register(Identifier id, Supplier<T> supplier) {
        T value = Registry.register(this.register, id, supplier.get());
        return new FabricRegistryObject<>(id, value);
    }
}