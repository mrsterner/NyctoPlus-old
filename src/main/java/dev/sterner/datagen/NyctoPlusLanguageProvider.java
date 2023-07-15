package dev.sterner.datagen;

import dev.sterner.registry.NyctoPlusObjects;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;

public class NyctoPlusLanguageProvider extends FabricLanguageProvider {
    public NyctoPlusLanguageProvider(FabricDataOutput dataOutput) {
        super(dataOutput);
    }

    @Override
    public void generateTranslations(TranslationBuilder builder) {
        builder.add(NyctoPlusObjects.PEACH_LOG, "Peach Log");
        builder.add(NyctoPlusObjects.LIVING_CORE_LOG, "Living Core Log");
        builder.add(NyctoPlusObjects.PEACH, "Peach");
        builder.add(NyctoPlusObjects.DEBUG_WAND, "Debug Wand");
        builder.add(NyctoPlusObjects.SHIMENAWA, "Shimenawa");
        builder.add(NyctoPlusObjects.STONE_MASK, "Stone Mask");
    }
}
