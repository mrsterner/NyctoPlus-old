package dev.sterner;

import dev.sterner.model.LeafModel;
import dev.sterner.model.PeachModel;
import dev.sterner.model.TallPeachModel;
import dev.sterner.model.VillagerLikeHeadModel;
import dev.sterner.registry.NyctoPlusBlockEntityTypes;
import dev.sterner.render.PeachBlockEntityRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;

public class NyctoPlusClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        BlockEntityRendererFactories.register(NyctoPlusBlockEntityTypes.PEACH_BLOCK_ENTITY, PeachBlockEntityRenderer::new);

        EntityModelLayerRegistry.registerModelLayer(PeachModel.LAYER, PeachModel::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(LeafModel.LAYER, LeafModel::getTexturedModelDataLeaf);
        EntityModelLayerRegistry.registerModelLayer(VillagerLikeHeadModel.LAYER_LOCATION, VillagerLikeHeadModel::getSkullTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(TallPeachModel.Peach.LAYER_LOCATION, TallPeachModel.Peach::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(TallPeachModel.Pig.LAYER_LOCATION, TallPeachModel.Pig::getTexturedModelData);
    }
}