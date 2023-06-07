package dev.sterner.datagen;

import com.google.common.collect.Maps;
import dev.sterner.registry.NyctoPlusObjects;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.fabricmc.fabric.api.datagen.v1.provider.SimpleFabricLootTableProvider;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.context.LootContextTypes;
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
            addDrop(NyctoPlusObjects.PEACH);
        }
    }

    public static class EntityTypes extends SimpleFabricLootTableProvider {
        private final Map<Identifier, LootTable.Builder> loot = Maps.newHashMap();

        public EntityTypes(FabricDataOutput output) {
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
