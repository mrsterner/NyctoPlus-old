package dev.sterner.client.render;

import dev.sterner.client.model.PeachModel;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;

public class PeachItemRenderer implements BuiltinItemRendererRegistry.DynamicItemRenderer {

    @Override
    public void render(ItemStack stack, ModelTransformationMode mode, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {

        PeachModel peachModel = new PeachModel(MinecraftClient.getInstance().getEntityModelLoader().getModelPart(PeachModel.LAYER));
        RenderLayer renderLayer = RenderLayer.getEntityTranslucent(PeachBlockEntityRenderer.TEXTURE);
        matrices.push();
        matrices.translate(0.5, -1.0, 0.5);
        peachModel.setHeadRotation(0, 0, 0);
        peachModel.render(matrices, vertexConsumers.getBuffer(renderLayer), light, OverlayTexture.DEFAULT_UV, 1.0F, 1.0F, 1.0F, 1.0F);
        matrices.pop();
    }
}
