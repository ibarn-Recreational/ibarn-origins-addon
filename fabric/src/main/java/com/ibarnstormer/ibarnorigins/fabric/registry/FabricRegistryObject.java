package com.ibarnstormer.ibarnorigins.fabric.registry;

import com.ibarnstormer.ibarnorigins.registry.utils.RegistryObjectWrapper;
import net.minecraft.util.Identifier;

public class FabricRegistryObject<T> implements RegistryObjectWrapper<T> {

    private final Identifier id;
    private final T value;

    public FabricRegistryObject(Identifier id, T value) {
        this.id = id;
        this.value = value;
    }

    @Override
    public Identifier getIdentifier() {
        return id;
    }

    @Override
    public T get() {
        return value;
    }

}
