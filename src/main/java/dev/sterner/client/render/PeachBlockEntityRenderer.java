package dev.sterner.client.render;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import dev.sterner.NyctoPlus;
import dev.sterner.common.block.PeachBlock;
import dev.sterner.common.blockentity.PeachBlockEntity;
import dev.sterner.client.model.LeafModel;
import dev.sterner.client.model.PeachModel;
import dev.sterner.client.model.TallPeachModel;
import dev.sterner.client.model.VillagerLikeHeadModel;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.block.entity.SkullBlockEntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.EntityModelLoader;
import net.minecraft.client.render.entity.model.PiglinHeadEntityModel;
import net.minecraft.client.render.entity.model.SkullEntityModel;
import net.minecraft.client.util.DefaultSkinHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.Uuids;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationPropertyHelper;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class PeachBlockEntityRenderer implements BlockEntityRenderer<PeachBlockEntity> {
    private final PeachModel PEACH;
    private final TallPeachModel.Peach PEACH_TALL;
    private final TallPeachModel.Pig PEACH_PIG;
    private final LeafModel LEAF;

    public static final Identifier TEXTURE = NyctoPlus.id("textures/block/peach.png");
    public static final Identifier TEXTURE_TALL = NyctoPlus.id("textures/block/peach_tall.png");
    public static final Identifier TEXTURE_PIG = NyctoPlus.id("textures/block/peach_pig.png");

    private static final int MAX_DISTANCE = 12;
    private static final int MIN_DISTANCE = 8;

    private final Map<PeachBlockEntity.Type, SkullBlockEntityModel> MODELS;

    private static final Map<PeachBlockEntity.Type, Identifier> TEXTURES = Util.make(Maps.newHashMap(), (map) -> {
        map.put(PeachBlockEntity.Type.PIGLIN, new Identifier("textures/entity/piglin/piglin.png"));
        map.put(PeachBlockEntity.Type.VILLAGER, new Identifier("textures/entity/villager/villager.png"));
        map.put(PeachBlockEntity.Type.PILLAGER, new Identifier("textures/entity/illager/pillager.png"));
        map.put(PeachBlockEntity.Type.PLAYER, DefaultSkinHelper.getTexture());
    });

    public static Map<PeachBlockEntity.Type, SkullBlockEntityModel> getModels(EntityModelLoader modelLoader) {
        ImmutableMap.Builder<PeachBlockEntity.Type, SkullBlockEntityModel> builder = ImmutableMap.builder();
        builder.put(PeachBlockEntity.Type.VILLAGER, new VillagerLikeHeadModel(modelLoader.getModelPart(VillagerLikeHeadModel.LAYER_LOCATION)));
        builder.put(PeachBlockEntity.Type.PILLAGER, new VillagerLikeHeadModel(modelLoader.getModelPart(VillagerLikeHeadModel.LAYER_LOCATION)));
        builder.put(PeachBlockEntity.Type.PIGLIN, new PiglinHeadEntityModel(modelLoader.getModelPart(EntityModelLayers.PIGLIN_HEAD)));
        builder.put(PeachBlockEntity.Type.PLAYER, new SkullEntityModel(modelLoader.getModelPart(EntityModelLayers.PLAYER_HEAD)));
        return builder.build();
    }

    public PeachBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
        this.MODELS = getModels(ctx.getLayerRenderDispatcher());

        this.PEACH = new PeachModel(PeachModel.getTexturedModelData().createModel());
        this.PEACH_TALL = new TallPeachModel.Peach(TallPeachModel.Peach.getTexturedModelData().createModel());
        this.PEACH_PIG = new TallPeachModel.Pig(TallPeachModel.Pig.getTexturedModelData().createModel());
        this.LEAF = new LeafModel(LeafModel.getTexturedModelDataLeaf().createModel());
    }

    @Override
    public void render(PeachBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        BlockState blockState = entity.getCachedState();
        int k = blockState.get(PeachBlock.ROTATION);
        int j = blockState.get(PeachBlock.PITCH);
        float h = RotationPropertyHelper.toDegrees(k);
        float i = RotationPropertyHelper.toDegrees(j);

        PeachBlockEntity.Type skullType = entity.getSkullType();
        if (skullType == null) {
            skullType = PeachBlockEntity.Type.PLAYER;
        }
        SkullBlockEntityModel model = this.MODELS.get(skullType);

        RenderLayer renderLayer = getRenderLayer(skullType, entity.getOwner());
        renderSkull(new Vec3d(entity.getPos().getX(), entity.getPos().getY(), entity.getPos().getZ()), null, h, i, matrices, vertexConsumers, light, model, PEACH, PEACH_TALL, PEACH_PIG, LEAF, renderLayer);
    }

    public static void renderSkull(@Nullable Vec3d vec3d, @Nullable Direction direction, float yaw, float pitch, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, SkullBlockEntityModel model, PeachModel peachModel, TallPeachModel.Peach peachTall, TallPeachModel.Pig pig, LeafModel leaf, RenderLayer renderLayer) {
        float skullAlpha = 1.0F;

        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player != null && vec3d != null) {

            double distance = MathHelper.sqrt((float) client.player.squaredDistanceTo(vec3d));

            if (distance >= MAX_DISTANCE) {
                skullAlpha = 0.0F;
            } else if (distance > MIN_DISTANCE) {
                double t = (distance - MIN_DISTANCE) / (MAX_DISTANCE - MIN_DISTANCE);
                skullAlpha = (float) (1.0 - t);
            }
        }
        float alpha = 1.0F - skullAlpha;

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
        model.render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV, 1.0F, 1.0F, 1.0F, skullAlpha);

        vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getEntityTranslucent(TEXTURE));
        matrices.translate(0, -1.5, 0);
        leaf.setHeadRotation(0, yaw, pitch);
        leaf.render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV, 1.0F, 1.0F, 1.0F, 1.0f);

        if (model instanceof PiglinHeadEntityModel) {
            vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getEntityTranslucent(TEXTURE_PIG));
            pig.setHeadRotation(0, yaw, pitch);
            pig.render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV, 1.0F, 1.0F, 1.0F, alpha);
        } else if (model instanceof VillagerLikeHeadModel) {
            vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getEntityTranslucent(TEXTURE_TALL));
            peachTall.setHeadRotation(0, yaw, pitch);
            peachTall.render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV, 1.0F, 1.0F, 1.0F, alpha);
        } else {
            peachModel.setHeadRotation(0, yaw, pitch);
            peachModel.render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV, 1.0F, 1.0F, 1.0F, alpha);
        }


        matrices.pop();
    }

    public static RenderLayer getRenderLayer(PeachBlockEntity.Type type, @Nullable GameProfile profile) {
        Identifier identifier = TEXTURES.get(type);
        if (profile != null) {
            MinecraftClient minecraftClient = MinecraftClient.getInstance();
            Map<MinecraftProfileTexture.Type, MinecraftProfileTexture> map = minecraftClient.getSkinProvider().getTextures(profile);
            return map.containsKey(MinecraftProfileTexture.Type.SKIN) ? RenderLayer.getEntityTranslucent(minecraftClient.getSkinProvider().loadSkin(map.get(MinecraftProfileTexture.Type.SKIN), MinecraftProfileTexture.Type.SKIN)) : RenderLayer.getEntityTranslucent(DefaultSkinHelper.getTexture(Uuids.getUuidFromProfile(profile)));
        } else {
            return RenderLayer.getEntityTranslucent(identifier == null ? DefaultSkinHelper.getTexture() : identifier);
        }
    }
}
