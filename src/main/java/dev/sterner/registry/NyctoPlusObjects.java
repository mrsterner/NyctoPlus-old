package dev.sterner.registry;

import dev.sterner.NyctoPlus;
import dev.sterner.block.PeachBlock;
import dev.sterner.block.PeachCoreLogBlock;
import dev.sterner.block.PeachLogBlock;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.LinkedHashMap;
import java.util.Map;

public interface NyctoPlusObjects {
    Map<Block, Identifier> BLOCKS = new LinkedHashMap<>();
    Map<Item, Identifier> ITEMS = new LinkedHashMap<>();

    Block PEACH_LOG = register("peach_log", new PeachLogBlock(FabricBlockSettings.copyOf(Blocks.OAK_LOG).dropsNothing()), true);
    Block PEACH_CORE_LOG = register("peach_core_log", new PeachCoreLogBlock(FabricBlockSettings.copyOf(Blocks.CRIMSON_NYLIUM).dropsNothing()), true);

    Block PEACH = register("peach", new PeachBlock(FabricBlockSettings.copyOf(Blocks.BONE_BLOCK)), true);

    static <T extends Item> T register(String name, T item) {
        ITEMS.put(item, NyctoPlus.id(name));
        return item;
    }

    static <T extends Block> T register(String name, T block, Item.Settings settings, boolean createItem) {
        BLOCKS.put(block, NyctoPlus.id(name));
        if (createItem) {
            ITEMS.put(new BlockItem(block, settings), BLOCKS.get(block));
        }
        return block;
    }

    static <T extends Block> T register(String name, T block, boolean createItem) {
        return register(name, block, new Item.Settings(), createItem);
    }

    static void init() {
        BLOCKS.keySet().forEach(block -> Registry.register(Registries.BLOCK, BLOCKS.get(block), block));
        ITEMS.keySet().forEach(item -> Registry.register(Registries.ITEM, ITEMS.get(item), item));
    }
}
