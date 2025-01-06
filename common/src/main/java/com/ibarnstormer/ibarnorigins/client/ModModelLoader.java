package com.ibarnstormer.ibarnorigins.client;

import com.ibarnstormer.ibarnorigins.IbarnOriginsMain;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class ModModelLoader {

    public static final SpriteIdentifier SOUL_FIRE_0 = new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, new Identifier("block/soul_fire_0"));
    public static final SpriteIdentifier SOUL_FIRE_1 = new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, new Identifier("block/soul_fire_1"));

    public static final SpriteIdentifier SOUL_MAGE_FIRE_0 = new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, IbarnOriginsMain.IOIdentifier("block/soul_mage_fire_0"));
    public static final SpriteIdentifier SOUL_MAGE_FIRE_1 = new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, IbarnOriginsMain.IOIdentifier("block/soul_mage_fire_1"));




}
