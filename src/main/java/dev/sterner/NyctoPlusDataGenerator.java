package dev.sterner;

import dev.sterner.datagen.NyctoPlusLanguageProvider;
import dev.sterner.datagen.NyctoPlusLootTableProvider;
import dev.sterner.datagen.NyctoPlusModelProvider;
import dev.sterner.datagen.NyctoPlusTagProvider;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class NyctoPlusDataGenerator implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator dataGenerator) {
        FabricDataGenerator.Pack pack = dataGenerator.createPack();
        pack.addProvider(NyctoPlusLanguageProvider::new);
        pack.addProvider(NyctoPlusModelProvider::new);
        pack.addProvider(NyctoPlusTagProvider.NyctoPlusBlockTags::new);
        pack.addProvider(NyctoPlusLootTableProvider.Blocks::new);
        pack.addProvider(NyctoPlusLootTableProvider.EntityTypes::new);
    }
}
