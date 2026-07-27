package com.ibarnstormer.ibarnorigins.registry;

import com.ibarnstormer.ibarnorigins.registry.utils.IORegisterWrapper;
import com.ibarnstormer.ibarnorigins.registry.utils.RegistryObjectWrapper;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class IONeoForgeRegisterWrapper<T> extends IORegisterWrapper<DeferredRegister<T>, T> {

    public IONeoForgeRegisterWrapper(DeferredRegister<T> register) {
        super(register);
    }

    @Override
    public RegistryObjectWrapper<T> register(ResourceLocation id, Supplier<T> supplier) {
        DeferredHolder<T, T> registryObject = this.register.register(id.getPath(), supplier);
        return new NeoForgeRegistryObject<>(id, registryObject);
    }
}
