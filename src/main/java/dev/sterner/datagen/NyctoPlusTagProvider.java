package dev.sterner.datagen;

import dev.sterner.registry.NyctoPlusObjects;
import dev.sterner.registry.NyctoPlusTagKeys;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.entity.EntityType;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.BlockTags;

import java.util.concurrent.CompletableFuture;

public class NyctoPlusTagProvider {
    public static class NyctoPlusBlockTags extends FabricTagProvider.BlockTagProvider {

        public NyctoPlusBlockTags(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
            super(output, registriesFuture);
        }

        @Override
        protected void configure(RegistryWrapper.WrapperLookup arg) {
            getOrCreateTagBuilder(BlockTags.LOGS).add(NyctoPlusObjects.PEACH_LOG);
        }
    }

    public static class NyctoPlusEntityTypeTags extends FabricTagProvider.EntityTypeTagProvider {

        public NyctoPlusEntityTypeTags(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
            super(output, registriesFuture);
        }

        @Override
        protected void configure(RegistryWrapper.WrapperLookup arg) {
            getOrCreateTagBuilder(NyctoPlusTagKeys.HUMANOIDS)
                    .add(EntityType.PLAYER)
                    .add(EntityType.PIGLIN)
                    .add(EntityType.PIGLIN_BRUTE)
                    .add(EntityType.VILLAGER)
                    .add(EntityType.PILLAGER)
                    .add(EntityType.EVOKER)
                    .add(EntityType.VINDICATOR)
                    .add(EntityType.WANDERING_TRADER);
        }
    }
}
