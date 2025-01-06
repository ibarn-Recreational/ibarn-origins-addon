package com.ibarnstormer.ibarnorigins.registry.utils;

import net.minecraft.util.Identifier;

import java.util.function.Supplier;

public abstract class IORegisterWrapper<R, O> {

    protected final R register;

    public IORegisterWrapper(R register) {
        this.register = register;
    }

    public R getRegister() {
        return this.register;
    }

    public abstract RegistryObjectWrapper<O> register(Identifier id, Supplier<O> supplier);

}
