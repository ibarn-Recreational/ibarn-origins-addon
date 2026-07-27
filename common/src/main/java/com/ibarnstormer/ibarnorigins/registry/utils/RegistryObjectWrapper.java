package com.ibarnstormer.ibarnorigins.registry.utils;

import java.util.function.Supplier;

import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;

public interface RegistryObjectWrapper<T> extends Supplier<T> {

    ResourceLocation getIdentifier();
    Holder<T> getRef();
    T get();


}
