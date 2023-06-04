package dev.sterner;

import dev.sterner.registry.NyctoPlusBlockEntityTypes;
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
	public static ItemGroup NYCTOPLUS_GROUP;


	public static Identifier id(String name){
		return new Identifier(MODID, name);
	}

	@Override
	public void onInitialize() {
		NyctoPlusObjects.init();
		NyctoPlusBlockEntityTypes.init();


		NYCTOPLUS_GROUP = FabricItemGroup.builder()
				.displayName(Text.translatable("itemGroup.nyctoplus.itemGroup"))
				.icon(() -> new ItemStack(NyctoPlusObjects.PEACH_LOG.asItem()))
				.entries((ctx, entries) -> {
					entries.add(NyctoPlusObjects.PEACH_LOG);
					entries.add(NyctoPlusObjects.PEACH_CORE_LOG);
					entries.add(NyctoPlusObjects.PEACH);
				})
				.build();
	}
}