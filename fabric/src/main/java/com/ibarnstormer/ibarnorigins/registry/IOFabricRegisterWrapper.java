package com.ibarnstormer.ibarnorigins.registry;

import com.ibarnstormer.ibarnorigins.registry.utils.IORegisterWrapper;
import com.ibarnstormer.ibarnorigins.registry.utils.RegistryObjectWrapper;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Supplier;

public class IOFabricRegisterWrapper<T> extends IORegisterWrapper<Registry<T>, T> {

    public IOFabricRegisterWrapper(Registry<T> register) {
        super(register);
    }

    @Override
    public RegistryObjectWrapper<T> register(ResourceLocation id, Supplier<T> supplier) {
        Holder<T> holder = Registry.registerForHolder(this.register, id, supplier.get());
        return new FabricRegistryObject<>(id, holder);
    }
}