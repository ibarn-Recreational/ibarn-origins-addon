package com.ibarnstormer.ibarnorigins.registry;

import com.ibarnstormer.ibarnorigins.registry.utils.RegistryObjectWrapper;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;

public class FabricRegistryObject<T> implements RegistryObjectWrapper<T> {

    private final Identifier id;
    private final RegistryEntry.Reference<T> value;

    public FabricRegistryObject(Identifier id, RegistryEntry.Reference<T> value) {
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
    public RegistryEntry.Reference<T> getRef() {
        return value;
    }

}
