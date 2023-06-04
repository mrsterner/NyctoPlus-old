package dev.sterner;

import dev.sterner.registry.NyctoPlusBlockEntityTypes;
import dev.sterner.registry.NyctoPlusObjects;
import dev.sterner.render.PeachBlockEntityRenderer;
import dev.sterner.render.PeachItemRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;

public class NyctoPlusClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		BlockEntityRendererFactories.register(NyctoPlusBlockEntityTypes.PEACH_BLOCK_ENTITY, PeachBlockEntityRenderer::new);
		BuiltinItemRendererRegistry.INSTANCE.register(NyctoPlusObjects.PEACH.asItem(), new PeachItemRenderer());
	}
}