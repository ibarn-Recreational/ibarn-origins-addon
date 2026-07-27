package com.ibarnstormer.ibarnorigins.registry;

import com.ibarnstormer.ibarnorigins.registry.utils.RegistryObjectWrapper;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.DeferredHolder;

public class NeoForgeRegistryObject<T> implements RegistryObjectWrapper<T> {

    private final ResourceLocation id;
    private final DeferredHolder<T, T> value;

    public NeoForgeRegistryObject(ResourceLocation id, DeferredHolder<T, T> value) {
        this.id = id;
        this.value = value;
    }

    @Override
    public ResourceLocation getIdentifier() {
        return id;
    }

    @Override
    public Holder<T> getRef() {
        return value.getDelegate();
    }

    @Override
    public T get() {
        return value.get();
    }
}
