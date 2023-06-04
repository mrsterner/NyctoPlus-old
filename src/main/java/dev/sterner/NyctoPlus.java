package dev.sterner;

import dev.sterner.registry.NyctoPlusBlockEntityTypes;
import dev.sterner.registry.NyctoPlusObjects;
import net.fabricmc.api.ModInitializer;

import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NyctoPlus implements ModInitializer {
	public static final String MODID = "nyctoplus";
    public static final Logger LOGGER = LoggerFactory.getLogger(MODID);

	public static Identifier id(String name){
		return new Identifier(MODID, name);
	}

	@Override
	public void onInitialize() {
		NyctoPlusObjects.init();
		NyctoPlusBlockEntityTypes.init();
	}
}