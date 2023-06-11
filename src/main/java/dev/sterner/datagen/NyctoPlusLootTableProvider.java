package dev.sterner.datagen;

import com.google.common.collect.Maps;
import dev.sterner.registry.NyctoPlusObjects;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.fabricmc.fabric.api.datagen.v1.provider.SimpleFabricLootTableProvider;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.CopyNbtLootFunction;
import net.minecraft.loot.provider.nbt.ContextLootNbtProvider;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.util.Identifier;

import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class NyctoPlusLootTableProvider {
    public static class Blocks extends FabricBlockLootTableProvider {
        public Blocks(FabricDataOutput dataOutput) {
            super(dataOutput);
        }

        @Override
        public void generate() {
            this.addDrop(NyctoPlusObjects.PEACH, (block) -> LootTable.builder().pool(
                    this.addSurvivesExplosionCondition(block, LootPool.builder()
                            .rolls(ConstantLootNumberProvider.create(1.0F))
                            .with(ItemEntry.builder(block)
                                    .apply(CopyNbtLootFunction.builder(ContextLootNbtProvider.BLOCK_ENTITY)
                                            .withOperation("SkullOwner", "SkullOwner")
                                            .withOperation("Type", "Type")
                                    )))));
        }
    }

    public static class NyctoEntityTypes extends SimpleFabricLootTableProvider {
        private final Map<Identifier, LootTable.Builder> loot = Maps.newHashMap();

        public NyctoEntityTypes(FabricDataOutput output) {
            super(output, LootContextTypes.ENTITY);
        }

        private void generateLoot() {

        }

        public <T extends Entity> void addDrop(EntityType<T> type, Function<EntityType<T>, LootTable.Builder> function) {
            loot.put(type.getLootTableId(), function.apply(type));
        }

        @Override
        public void accept(BiConsumer<Identifier, LootTable.Builder> consumer) {
            this.generateLoot();
            for (Map.Entry<Identifier, LootTable.Builder> entry : loot.entrySet()) {
                consumer.accept(entry.getKey(), entry.getValue());
            }
        }
    }
}
