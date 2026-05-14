package com.ibarnstormer.ibarnorigins.registry;

import com.ibarnstormer.ibarnorigins.registry.utils.RegistryObjectWrapper;
import net.minecraft.core.Holder;
import net.minecraft.resources.Identifier;

public class FabricRegistryObject<T> implements RegistryObjectWrapper<T> {

    private final Identifier id;
    private final Holder.Reference<T> value;

    public FabricRegistryObject(Identifier id, Holder.Reference<T> value) {
        this.id = id;
        this.value = value;
    }

    @Override
    public Identifier getIdentifier() {
        return id;
    }

    @Override
    public T get() {
        return value.value();
    }

    @Override
    public Holder.Reference<T> getRef() {
        return value;
    }

}
