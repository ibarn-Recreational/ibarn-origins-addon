package com.ibarnstormer.ibarnorigins.registry.utils;

import java.util.function.Supplier;
import net.minecraft.core.Holder;
import net.minecraft.resources.Identifier;

public interface RegistryObjectWrapper<T> extends Supplier<T> {

    Identifier getIdentifier();
    T get();
    Holder.Reference<T> getRef();


}
