package com.ibarnstormer.ibarnorigins.registry;

import com.ibarnstormer.ibarnorigins.registry.utils.RegistryObjectWrapper;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;

public class FabricRegistryObject<T> implements RegistryObjectWrapper<T> {

    private final ResourceLocation id;
    private final Holder<T> holder;

    public FabricRegistryObject(ResourceLocation id, Holder<T> holder) {
        this.id = id;
        this.holder = holder;
    }

    @Override
    public ResourceLocation getIdentifier() {
        return id;
    }

    @Override
    public Holder<T> getRef() {
        return this.holder;
    }

    @Override
    public T get() {
        return this.holder.value();
    }

}
