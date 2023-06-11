package dev.sterner;

import dev.sterner.model.*;
import dev.sterner.registry.NyctoPlusBlockEntityTypes;
import dev.sterner.render.PeachBlockEntityRenderer;
import dev.sterner.render.ShimenawaBlockEntityRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;

public class NyctoPlusClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        BlockEntityRendererFactories.register(NyctoPlusBlockEntityTypes.PEACH_BLOCK_ENTITY, PeachBlockEntityRenderer::new);
        BlockEntityRendererFactories.register(NyctoPlusBlockEntityTypes.SHIMENAWA, ShimenawaBlockEntityRenderer::new);

        EntityModelLayerRegistry.registerModelLayer(PeachModel.LAYER, PeachModel::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(LeafModel.LAYER, LeafModel::getTexturedModelDataLeaf);
        EntityModelLayerRegistry.registerModelLayer(VillagerLikeHeadModel.LAYER_LOCATION, VillagerLikeHeadModel::getSkullTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(TallPeachModel.Peach.LAYER_LOCATION, TallPeachModel.Peach::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(TallPeachModel.Pig.LAYER_LOCATION, TallPeachModel.Pig::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(ShimenawaModel.LAYER, ShimenawaModel::getTexturedModelData);
    }
}