package dev.sterner.registry;

import dev.sterner.NyctoPlus;
import dev.sterner.blockentity.PeachBlockEntity;
import dev.sterner.blockentity.PeachCoreBlockEntity;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.LinkedHashMap;
import java.util.Map;

public interface NyctoPlusBlockEntityTypes {
    Map<BlockEntityType<?>, Identifier> BLOCK_ENTITY_TYPES = new LinkedHashMap<>();

    BlockEntityType<PeachCoreBlockEntity> PEACH_CORE_BLOCK_ENTITY = register("peach_core", FabricBlockEntityTypeBuilder
            .create(PeachCoreBlockEntity::new, NyctoPlusObjects.PEACH_CORE_LOG).build(null));

    BlockEntityType<PeachBlockEntity> PEACH_BLOCK_ENTITY = register("peach", FabricBlockEntityTypeBuilder
            .create(PeachBlockEntity::new, NyctoPlusObjects.PEACH).build(null));

    static <T extends BlockEntity> BlockEntityType<T> register(String name, BlockEntityType<T> type) {
        BLOCK_ENTITY_TYPES.put(type, NyctoPlus.id(name));
        return type;
    }

    static void init() {
        BLOCK_ENTITY_TYPES.keySet().forEach(blockEntityType -> Registry.register(Registries.BLOCK_ENTITY_TYPE, BLOCK_ENTITY_TYPES.get(blockEntityType), blockEntityType));
    }
}
