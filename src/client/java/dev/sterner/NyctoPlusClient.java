package dev.sterner;

import dev.sterner.model.LeafModel;
import dev.sterner.model.PeachModel;
import dev.sterner.model.TallPeachModel;
import dev.sterner.registry.NyctoPlusBlockEntityTypes;
import dev.sterner.registry.NyctoPlusObjects;
import dev.sterner.render.PeachBlockEntityRenderer;
import dev.sterner.render.PeachItemRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;

public class NyctoPlusClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        BlockEntityRendererFactories.register(NyctoPlusBlockEntityTypes.PEACH_BLOCK_ENTITY, PeachBlockEntityRenderer::new);
        //TODO replace with 2d model BuiltinItemRendererRegistry.INSTANCE.register(NyctoPlusObjects.PEACH.asItem(), new PeachItemRenderer());

        EntityModelLayerRegistry.registerModelLayer(PeachModel.LAYER, PeachModel::getTexturedModelData);
		EntityModelLayerRegistry.registerModelLayer(LeafModel.LAYER, LeafModel::getTexturedModelDataLeaf);
        EntityModelLayerRegistry.registerModelLayer(TallPeachModel.VillagerLikeModel.LAYER_LOCATION, TallPeachModel.VillagerLikeModel::getSkullTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(TallPeachModel.Peach.LAYER_LOCATION, TallPeachModel.Peach::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(TallPeachModel.Pig.LAYER_LOCATION, TallPeachModel.Pig::getTexturedModelData);
    }
}