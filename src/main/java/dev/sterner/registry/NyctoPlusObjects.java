package dev.sterner.registry;

import dev.sterner.NyctoPlus;
import dev.sterner.common.block.LivingCoreLogBlock;
import dev.sterner.common.block.PeachBlock;
import dev.sterner.common.block.PeachLogBlock;
import dev.sterner.common.block.ShimenawaBlock;
import dev.sterner.common.item.DebugWand;
import dev.sterner.common.item.PeachBlockItem;
import dev.sterner.common.item.ShimenawaItem;
import dev.sterner.common.item.StoneMaskItem;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.MapColor;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;

import java.util.LinkedHashMap;
import java.util.Map;

public interface NyctoPlusObjects {
    Map<Block, Identifier> BLOCKS = new LinkedHashMap<>();
    Map<Item, Identifier> ITEMS = new LinkedHashMap<>();

    Block PEACH_LOG = register("peach_log", new PeachLogBlock(FabricBlockSettings.copyOf(Blocks.OAK_LOG).dropsNothing()), true);
    Block LIVING_CORE_LOG = register("living_core_log", new LivingCoreLogBlock(FabricBlockSettings.copyOf(Blocks.SHROOMLIGHT).dropsNothing()), true);

    Block PEACH = registerPeach("peach", new PeachBlock(AbstractBlock.Settings.create().mapColor(MapColor.DARK_RED).noCollision().sounds(BlockSoundGroup.FUNGUS).pistonBehavior(PistonBehavior.DESTROY)), true);
    Block SHIMENAWA = registerShimenawa("shimenawa", new ShimenawaBlock(AbstractBlock.Settings.create().mapColor(MapColor.BROWN).noCollision().sounds(BlockSoundGroup.WOOL).pistonBehavior(PistonBehavior.BLOCK)));

    Item STONE_MASK = register("stone_mask", new StoneMaskItem(new Item.Settings()));

    Item DEBUG_WAND = register("debug_wand", new DebugWand(new Item.Settings()));

    static <T extends Item> T register(String name, T item) {
        ITEMS.put(item, NyctoPlus.id(name));
        return item;
    }

    static <T extends Block> T register(String name, T block, BlockItem blockItem, boolean createItem) {
        BLOCKS.put(block, NyctoPlus.id(name));
        if (createItem) {
            ITEMS.put(blockItem, BLOCKS.get(block));
        }
        return block;
    }

    static <T extends Block> T register(String name, T block, boolean createItem) {
        return register(name, block, new BlockItem(block, new Item.Settings()), createItem);
    }

    static <T extends Block> T registerPeach(String name, T block, boolean createItem) {
        return register(name, block, new PeachBlockItem(block, new Item.Settings()), createItem);
    }

    static <T extends Block> T registerShimenawa(String name, T block) {
        return register(name, block, new ShimenawaItem(block, new Item.Settings()), true);
    }

    static void init() {
        BLOCKS.keySet().forEach(block -> Registry.register(Registries.BLOCK, BLOCKS.get(block), block));
        ITEMS.keySet().forEach(item -> Registry.register(Registries.ITEM, ITEMS.get(item), item));
    }
}
