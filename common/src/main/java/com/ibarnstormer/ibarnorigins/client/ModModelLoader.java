package com.ibarnstormer.ibarnorigins.client;

import com.ibarnstormer.ibarnorigins.IbarnOriginsMain;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceLocation;

public class ModModelLoader {

    public static final Material SOUL_FIRE_0 = new Material(TextureAtlas.LOCATION_BLOCKS, ResourceLocation.withDefaultNamespace("block/soul_fire_0"));
    public static final Material SOUL_FIRE_1 = new Material(TextureAtlas.LOCATION_BLOCKS, ResourceLocation.withDefaultNamespace("block/soul_fire_1"));

    public static final Material SOUL_MAGE_FIRE_0 = new Material(TextureAtlas.LOCATION_BLOCKS, IbarnOriginsMain.IOIdentifier("block/soul_mage_fire_0"));
    public static final Material SOUL_MAGE_FIRE_1 = new Material(TextureAtlas.LOCATION_BLOCKS, IbarnOriginsMain.IOIdentifier("block/soul_mage_fire_1"));




}
