package dev.sterner.render;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import dev.sterner.NyctoPlus;
import dev.sterner.block.PeachBlock;
import dev.sterner.blockentity.PeachBlockEntity;
import dev.sterner.model.LeafModel;
import dev.sterner.model.PeachModel;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.SkullEntityModel;
import net.minecraft.client.util.DefaultSkinHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.Uuids;
import net.minecraft.util.math.*;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class PeachBlockEntityRenderer implements BlockEntityRenderer<PeachBlockEntity> {
    private final PeachModel PEACH;
    private final LeafModel LEAF;
    private final SkullEntityModel MODEL;

    private static final Identifier TEXTURE = NyctoPlus.id("textures/block/peach.png");

    private static final int MAX_DISTANCE = 12;
    private static final int MIN_DISTANCE = 8;

    public PeachBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
        this.MODEL = new SkullEntityModel(ctx.getLayerRenderDispatcher().getModelPart(EntityModelLayers.PLAYER_HEAD));
        this.PEACH = new PeachModel(PeachModel.getTexturedModelData().createModel());
        this.LEAF = new LeafModel(LeafModel.getTexturedModelDataLeaf().createModel());
    }

    @Override
    public void render(PeachBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        BlockState blockState = entity.getCachedState();
        int k = blockState.get(PeachBlock.ROTATION);
        int j = blockState.get(PeachBlock.PITCH);
        float h = RotationPropertyHelper.toDegrees(k);
        float i = RotationPropertyHelper.toDegrees(j);
        RenderLayer renderLayer = getRenderLayer(entity.getOwner());
        renderSkull(new Vec3d(entity.getPos().getX(), entity.getPos().getY(), entity.getPos().getZ()),null, h, i, matrices, vertexConsumers, light, MODEL, PEACH, LEAF, renderLayer);
    }

    public static void renderSkull(@Nullable Vec3d vec3d, @Nullable Direction direction, float yaw, float pitch, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, SkullEntityModel model, PeachModel peachModel, LeafModel leaf, RenderLayer renderLayer) {
        float peachAlpha = 1.0F;

        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player != null && vec3d != null) {

            double distance = MathHelper.sqrt((float) client.player.squaredDistanceTo(vec3d));

            if (distance >= MAX_DISTANCE) {
                peachAlpha = 0.0F;
            } else if (distance <= MIN_DISTANCE) {
            } else {
                double t = (distance - MIN_DISTANCE) / (MAX_DISTANCE - MIN_DISTANCE);
                peachAlpha = (float) (1.0 - t);
            }
        }
        float skullAlpha = 1.0F - peachAlpha;

        matrices.push();
        float f = 0.25F;
        if (direction == null) {
            matrices.translate(0.5F, 0.55F, 0.5F);
        } else {
            matrices.translate(0.5F - (float) direction.getOffsetX() * f, f, 0.5F - (float) direction.getOffsetZ() * f);
        }

        matrices.scale(-1.0F, -1.0F, 1.0F);

        VertexConsumer vertexConsumer = vertexConsumers.getBuffer(renderLayer);
        model.setHeadRotation(0, yaw, pitch);
        model.render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV, 1.0F, 1.0F, 1.0F, peachAlpha);


        vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getEntityTranslucent(TEXTURE));
        matrices.translate(0, -1.5, 0);
        leaf.setHeadRotation(0, yaw, pitch);
        leaf.render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV, 1.0F, 1.0F, 1.0F, peachAlpha);

        peachModel.setHeadRotation(0, yaw, pitch);
        peachModel.render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV, 1.0F, 1.0F, 1.0F, skullAlpha);

        matrices.pop();
    }

    public static RenderLayer getRenderLayer(@Nullable GameProfile profile) {
        if (profile != null) {
            MinecraftClient minecraftClient = MinecraftClient.getInstance();
            Map<MinecraftProfileTexture.Type, MinecraftProfileTexture> map = minecraftClient.getSkinProvider().getTextures(profile);
            return map.containsKey(MinecraftProfileTexture.Type.SKIN) ? RenderLayer.getEntityTranslucent(minecraftClient.getSkinProvider().loadSkin(map.get(MinecraftProfileTexture.Type.SKIN), MinecraftProfileTexture.Type.SKIN)) : RenderLayer.getEntityTranslucent(DefaultSkinHelper.getTexture(Uuids.getUuidFromProfile(profile)));
        } else {
            return RenderLayer.getEntityTranslucent(DefaultSkinHelper.getTexture());
        }
    }
}
