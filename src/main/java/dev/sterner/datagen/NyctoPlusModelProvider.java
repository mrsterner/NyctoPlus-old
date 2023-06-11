package dev.sterner.datagen;

import dev.sterner.block.PeachLogBlock;
import dev.sterner.registry.NyctoPlusObjects;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.block.Block;
import net.minecraft.data.client.*;
import net.minecraft.util.Identifier;

public class NyctoPlusModelProvider extends FabricModelProvider {
    public NyctoPlusModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator generator) {
        generator.registerSimpleCubeAll(NyctoPlusObjects.LIVING_CORE_LOG);
        registerPeachLog(generator, NyctoPlusObjects.PEACH_LOG);
        generator.registerBuiltinWithParticle(NyctoPlusObjects.PEACH, NyctoPlusObjects.PEACH.asItem());
    }

    @Override
    public void generateItemModels(ItemModelGenerator generator) {
        generator.register(NyctoPlusObjects.PEACH.asItem(), Models.GENERATED);
        generator.register(NyctoPlusObjects.DEBUG_WAND, Models.GENERATED);
        generator.register(NyctoPlusObjects.SHIMENAWA.asItem(), Models.GENERATED);
    }

    private void registerPeachLog(BlockStateModelGenerator generator, Block block) {
        Identifier identifier = TextureMap.getSubId(block, "_side_variant");
        generator.blockStateCollector.accept(VariantsBlockStateSupplier.create(block)
                .coordinate(BlockStateModelGenerator.createAxisRotatedVariantMap())
                .coordinate(BlockStateVariantMap.create(PeachLogBlock.VARIANTS)
                        .register(0, BlockStateVariant.create()
                                .put(VariantSettings.MODEL, TexturedModel.CUBE_BOTTOM_TOP.upload(block, generator.modelCollector)))
                        .register(1, BlockStateVariant.create()
                                .put(VariantSettings.MODEL, TexturedModel.CUBE_BOTTOM_TOP.get(block).textures((textureMap) -> {
                                    textureMap.put(TextureKey.SIDE, identifier);
                                }).upload(block, "_variant", generator.modelCollector)))));
    }
}
