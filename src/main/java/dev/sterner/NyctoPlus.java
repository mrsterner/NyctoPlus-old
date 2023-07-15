package dev.sterner;

import dev.sterner.registry.NyctoPlusBlockEntityTypes;
import dev.sterner.registry.NyctoPlusEntityTypes;
import dev.sterner.registry.NyctoPlusObjects;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NyctoPlus implements ModInitializer {
    public static final String MODID = "nyctoplus";
    public static final Logger LOGGER = LoggerFactory.getLogger(MODID);
    public static final RegistryKey<ItemGroup> NYCTOPLUS_ITEM_GROUP = RegistryKey.of(RegistryKeys.ITEM_GROUP, id("nyctoplus"));

    public static Identifier id(String name) {
        return new Identifier(MODID, name);
    }

    @Override
    public void onInitialize() {
        NyctoPlusObjects.init();
        NyctoPlusBlockEntityTypes.init();
        NyctoPlusEntityTypes.init();

        Registry.register(Registries.ITEM_GROUP, NYCTOPLUS_ITEM_GROUP, FabricItemGroup.builder()
                .icon(() -> new ItemStack(NyctoPlusObjects.PEACH))
                .displayName(Text.translatable(MODID + ".group.main"))
                .build());

        ItemGroupEvents.modifyEntriesEvent(NYCTOPLUS_ITEM_GROUP).register(this::mainGroup);
    }

    private void mainGroup(FabricItemGroupEntries entries) {
        entries.add(NyctoPlusObjects.STONE_MASK);
        entries.add(NyctoPlusObjects.PEACH);
        entries.add(NyctoPlusObjects.PEACH_LOG);
        entries.add(NyctoPlusObjects.LIVING_CORE_LOG);
    }

}