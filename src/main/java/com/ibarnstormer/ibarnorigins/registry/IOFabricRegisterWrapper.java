package com.ibarnstormer.ibarnorigins.registry;

import com.ibarnstormer.ibarnorigins.registry.utils.IORegisterWrapper;
import com.ibarnstormer.ibarnorigins.registry.utils.RegistryObjectWrapper;
import java.util.function.Supplier;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;

public class IOFabricRegisterWrapper<T> extends IORegisterWrapper<Registry<T>, T> {

    public IOFabricRegisterWrapper(Registry<T> register) {
        super(register);
    }

    @Override
    public RegistryObjectWrapper<T> register(Identifier id, Supplier<T> supplier) {
        Holder.Reference<T> value = Registry.registerForHolder(this.register, id, supplier.get());
        return new FabricRegistryObject<>(id, value);
    }
}