package dev.sterner.registry;

import dev.sterner.NyctoPlus;
import net.minecraft.entity.EntityType;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;

public interface NyctoPlusTagKeys {
    TagKey<EntityType<?>> HUMANOIDS = TagKey.of(RegistryKeys.ENTITY_TYPE, NyctoPlus.id("humanoids"));


}
