package com.ibarnstormer.ibarnorigins.registry;

import com.ibarnstormer.ibarnorigins.registry.utils.RegistryObjectWrapper;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.RegistryObject;

public class ForgeRegistryObject<T> implements RegistryObjectWrapper<T> {

    private final ResourceLocation id;
    private final RegistryObject<T> value;

    public ForgeRegistryObject(ResourceLocation id, RegistryObject<T> value) {
        this.id = id;
        this.value = value;
    }

    @Override
    public ResourceLocation getIdentifier() {
        return id;
    }

    @Override
    public Holder<T> getRef() {
        return value.getHolder().isPresent() ? value.getHolder().get() : null;
    }

    @Override
    public T get() {
        return value.get();
    }
}
