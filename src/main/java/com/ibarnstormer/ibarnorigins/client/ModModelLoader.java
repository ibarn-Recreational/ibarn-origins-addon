package com.ibarnstormer.ibarnorigins.client;

import com.ibarnstormer.ibarnorigins.IbarnOriginsMain;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.TexturedRenderLayers;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class ModModelLoader {

    public static final SpriteIdentifier SOUL_FIRE_0 = TexturedRenderLayers.BLOCK_SPRITE_MAPPER.mapVanilla("soul_fire_0");
    public static final SpriteIdentifier SOUL_FIRE_1 = TexturedRenderLayers.BLOCK_SPRITE_MAPPER.mapVanilla("soul_fire_1");

    public static final SpriteIdentifier SOUL_MAGE_FIRE_0 = TexturedRenderLayers.BLOCK_SPRITE_MAPPER.map(IbarnOriginsMain.IOIdentifier("soul_mage_fire_0"));
    public static final SpriteIdentifier SOUL_MAGE_FIRE_1 = TexturedRenderLayers.BLOCK_SPRITE_MAPPER.map(IbarnOriginsMain.IOIdentifier("soul_mage_fire_1"));
}
