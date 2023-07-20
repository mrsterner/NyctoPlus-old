package dev.sterner.client.render;

import dev.sterner.NyctoPlus;
import dev.sterner.common.item.StoneMaskItem;
import mod.azure.azurelib.model.DefaultedItemGeoModel;
import mod.azure.azurelib.renderer.GeoArmorRenderer;
import net.minecraft.item.ItemStack;

public class StoneMaskArmorRenderer extends GeoArmorRenderer<StoneMaskItem> {
    public StoneMaskArmorRenderer() {
        super(new DefaultedItemGeoModel<>(NyctoPlus.id("armor/stone_mask")));
    }
}
