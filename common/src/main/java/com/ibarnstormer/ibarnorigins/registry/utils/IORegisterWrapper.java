package com.ibarnstormer.ibarnorigins.registry.utils;

import java.util.function.Supplier;
import net.minecraft.resources.ResourceLocation;

public abstract class IORegisterWrapper<R, O> {

    protected final R register;

    public IORegisterWrapper(R register) {
        this.register = register;
    }

    public R getRegister() {
        return this.register;
    }

    public abstract RegistryObjectWrapper<O> register(ResourceLocation id, Supplier<O> supplier);

}
