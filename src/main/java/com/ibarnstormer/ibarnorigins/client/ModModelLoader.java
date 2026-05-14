package com.ibarnstormer.ibarnorigins.client;

import com.ibarnstormer.ibarnorigins.IbarnOriginsMain;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.resources.model.sprite.SpriteId;

@Environment(EnvType.CLIENT)
public class ModModelLoader {

    public static final SpriteId SOUL_FIRE_0 = Sheets.BLOCKS_MAPPER.defaultNamespaceApply("soul_fire_0");
    public static final SpriteId SOUL_FIRE_1 = Sheets.BLOCKS_MAPPER.defaultNamespaceApply("soul_fire_1");

    public static final SpriteId SOUL_MAGE_FIRE_0 = Sheets.BLOCKS_MAPPER.apply(IbarnOriginsMain.IOIdentifier("soul_mage_fire_0"));
    public static final SpriteId SOUL_MAGE_FIRE_1 = Sheets.BLOCKS_MAPPER.apply(IbarnOriginsMain.IOIdentifier("soul_mage_fire_1"));
}
