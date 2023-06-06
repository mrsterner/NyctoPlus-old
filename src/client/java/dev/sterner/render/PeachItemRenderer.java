package dev.sterner.render;

import com.mojang.authlib.GameProfile;
import dev.sterner.model.LeafModel;
import dev.sterner.model.PeachModel;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.minecraft.block.entity.SkullBlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.SkullEntityModel;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.util.Util;

import java.util.UUID;

public class PeachItemRenderer implements BuiltinItemRendererRegistry.DynamicItemRenderer {

    @Override
    public void render(ItemStack stack, ModelTransformationMode mode, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        GameProfile gameProfile = null;
        if (stack.hasNbt()) {
            NbtCompound nbtCompound = stack.getNbt();
            if (nbtCompound.contains("SkullOwner", NbtElement.COMPOUND_TYPE)) {
                gameProfile = NbtHelper.toGameProfile(nbtCompound.getCompound("SkullOwner"));
            } else if (nbtCompound.contains("SkullOwner", NbtElement.STRING_TYPE) && !Util.isBlank(nbtCompound.getString("SkullOwner"))) {
                gameProfile = new GameProfile(null, nbtCompound.getString("SkullOwner"));
                nbtCompound.remove("SkullOwner");
                SkullBlockEntity.loadProperties(gameProfile, (profile) -> {
                    nbtCompound.put("SkullOwner", NbtHelper.writeGameProfile(new NbtCompound(), profile));
                });
            }
        }

        SkullEntityModel skullBlockEntityModel = new SkullEntityModel(MinecraftClient.getInstance().getEntityModelLoader().getModelPart(EntityModelLayers.PLAYER_HEAD));
        PeachModel peachModel = new PeachModel(MinecraftClient.getInstance().getEntityModelLoader().getModelPart(PeachModel.LAYER));
        LeafModel leaf = new LeafModel(MinecraftClient.getInstance().getEntityModelLoader().getModelPart(LeafModel.LAYER));
        RenderLayer renderLayer = PeachBlockEntityRenderer.getRenderLayer(gameProfile);
        PeachBlockEntityRenderer.renderSkull(null, null, 180.0F, 0, matrices, vertexConsumers, light, skullBlockEntityModel, peachModel, leaf, renderLayer);
    }
}
