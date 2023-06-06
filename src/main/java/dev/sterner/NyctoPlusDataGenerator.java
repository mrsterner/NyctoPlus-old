package dev.sterner;

import dev.sterner.datagen.NyctoPlusLanguageProvider;
import dev.sterner.datagen.NyctoPlusModelProvider;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class NyctoPlusDataGenerator implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator dataGenerator) {
        FabricDataGenerator.Pack pack = dataGenerator.createPack();
        pack.addProvider(NyctoPlusLanguageProvider::new);
        pack.addProvider(NyctoPlusModelProvider::new);
    }
}
