package dev.sterner.client.render;

import dev.sterner.NyctoPlus;
import dev.sterner.common.item.StoneMaskItem;
import mod.azure.azurelib.model.DefaultedItemGeoModel;
import mod.azure.azurelib.renderer.GeoItemRenderer;

public class StoneMaskItemRenderer extends GeoItemRenderer<StoneMaskItem> {
    public StoneMaskItemRenderer() {
        super(new DefaultedItemGeoModel<>(NyctoPlus.id("armor/stone_mask")));
    }
}
