package com.ibarnstormer.ibarnorigins.forge.registry;

import com.ibarnstormer.ibarnorigins.registry.utils.IORegisterWrapper;
import com.ibarnstormer.ibarnorigins.registry.utils.RegistryObjectWrapper;
import net.minecraft.util.Identifier;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class IOForgeRegisterWrapper<T, O extends T> extends IORegisterWrapper<DeferredRegister<T>, O> {

    public IOForgeRegisterWrapper(DeferredRegister<T> register) {
        super(register);
    }

    @Override
    public RegistryObjectWrapper<O> register(Identifier id, Supplier<O> supplier) {
        RegistryObject<O> registryObject = this.register.register(id.getPath(), supplier);
        return new ForgeRegistryObject<>(id, registryObject);
    }
}
