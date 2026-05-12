package com.ibarnstormer.ibarnorigins.registry.utils;

import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;

import java.util.function.Supplier;

public interface RegistryObjectWrapper<T> extends Supplier<T> {

    Identifier getIdentifier();
    T get();
    RegistryEntry.Reference<T> getRef();


}
