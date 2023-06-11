package dev.sterner.render;

import dev.sterner.NyctoPlus;
import dev.sterner.block.ShimenawaBlock;
import dev.sterner.blockentity.ShimenawaBlockEntity;
import dev.sterner.model.ShimenawaModel;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.RotationAxis;

public class ShimenawaBlockEntityRenderer implements BlockEntityRenderer<ShimenawaBlockEntity> {
    private final Identifier TEXTURE = NyctoPlus.id("textures/block/shimenawa.png");
    private final ShimenawaModel MODEL;

    public ShimenawaBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
        this.MODEL = new ShimenawaModel(ctx.getLayerModelPart(ShimenawaModel.LAYER));
    }

    @Override
    public void render(ShimenawaBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        BlockState blockState = entity.getCachedState();
        Direction direction = blockState.get(ShimenawaBlock.ROTATION);

        matrices.push();
        matrices.translate(0.5F, 0.5F, 0.5F);
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-direction.asRotation()));
        matrices.translate(-0.5F, -0.5F, -0.5F);

        switch (direction) {
            case SOUTH, NORTH -> renderBlock(entity, matrices, vertexConsumers);
            case WEST, EAST -> {
                matrices.push();
                matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(90));
                matrices.translate(-1, 0, 0);
                renderBlock(entity, matrices, vertexConsumers);
                matrices.pop();
            }
        }

        matrices.translate(0.5F, 1.5F, 0.5F);
        matrices.scale(-1.0F, -1.0F, 1.0F);
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180));
        MODEL.render(matrices, vertexConsumers.getBuffer(RenderLayer.getEntityCutout(TEXTURE)), light, overlay, 1, 1, 1, 1);
        matrices.pop();
    }

    private void renderBlock(ShimenawaBlockEntity entity, MatrixStack matrices, VertexConsumerProvider vertexConsumerProvider) {
        if (entity.getWorld() != null) {
            MinecraftClient.getInstance().getBlockRenderManager()
                    .renderBlock(
                            entity.getLogState(),
                            entity.getPos(),
                            entity.getWorld(),
                            matrices,
                            vertexConsumerProvider.getBuffer(RenderLayers.getBlockLayer(entity.getLogState())),
                            false,
                            entity.getWorld().getRandom());
        }
    }
}
