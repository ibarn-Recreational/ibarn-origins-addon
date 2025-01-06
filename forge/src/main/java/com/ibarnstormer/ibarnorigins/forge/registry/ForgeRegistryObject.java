package com.ibarnstormer.ibarnorigins.forge.registry;

import com.ibarnstormer.ibarnorigins.registry.utils.RegistryObjectWrapper;
import net.minecraft.util.Identifier;
import net.minecraftforge.registries.RegistryObject;

public class ForgeRegistryObject<T> implements RegistryObjectWrapper<T> {

    private final Identifier id;
    private final RegistryObject<T> value;

    public ForgeRegistryObject(Identifier id, RegistryObject<T> value) {
        this.id = id;
        this.value = value;
    }

    @Override
    public Identifier getIdentifier() {
        return id;
    }

    @Override
    public T get() {
        return value.get();
    }
}
