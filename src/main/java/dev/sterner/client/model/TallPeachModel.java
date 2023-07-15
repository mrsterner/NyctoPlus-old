package dev.sterner.client.model;

import dev.sterner.NyctoPlus;
import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.block.entity.SkullBlockEntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.entity.model.EntityModelPartNames;
import net.minecraft.client.util.math.MatrixStack;

public class TallPeachModel {

    public static class Peach extends SkullBlockEntityModel {
        public static final EntityModelLayer LAYER_LOCATION = new EntityModelLayer(NyctoPlus.id("peach_tall"), "main");
        private final ModelPart root;
        private final ModelPart head;

        public Peach(ModelPart root) {
            this.root = root;
            this.head = root.getChild(EntityModelPartNames.HEAD);
        }

        public static TexturedModelData getTexturedModelData() {
            ModelData modelData = new ModelData();
            ModelPartData modelPartData = modelData.getRoot();
            ModelPartData head = modelPartData.addChild("head", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 24.0F, 0.0F));
            head.addChild("peach", ModelPartBuilder.create().uv(0, 0).cuboid(-4.0F, -8.0F, -4.0F, 8.0F, 10.0F, 8.0F, new Dilation(0.1F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));
            head.addChild("leaf", ModelPartBuilder.create().uv(0, 18).cuboid(-4.0F, -8.0F, -4.0F, 8.0F, 5.0F, 8.0F, new Dilation(0.3F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));
            head.addChild("nose", ModelPartBuilder.create().uv(0, 9).cuboid(-1.0F, -1.0F, -6.0F, 2.0F, 4.0F, 2.0F, new Dilation(0.1F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

            return TexturedModelData.of(modelData, 32, 32);
        }

        @Override
        public void setHeadRotation(float animationProgress, float yaw, float pitch) {
            this.head.yaw = yaw * 0.017453292F;
            this.head.pitch = pitch * 0.017453292F;
        }

        @Override
        public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha) {
            this.root.render(matrices, vertices, light, overlay, red, green, blue, alpha);
        }
    }

    public static class Pig extends SkullBlockEntityModel {
        public static final EntityModelLayer LAYER_LOCATION = new EntityModelLayer(NyctoPlus.id("peach_pig"), "main");
        private final ModelPart root;
        private final ModelPart head;

        public Pig(ModelPart root) {
            this.root = root;
            this.head = root.getChild(EntityModelPartNames.HEAD);
        }

        public static TexturedModelData getTexturedModelData() {
            ModelData modelData = new ModelData();
            ModelPartData modelPartData = modelData.getRoot();

            ModelPartData head = modelPartData.addChild("head", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 24.0F, 0.0F));
            ModelPartData peach = head.addChild("peach", ModelPartBuilder.create().uv(0, 0).cuboid(-5.0F, -8.0F, -4.0F, 10.0F, 8.0F, 8.0F, new Dilation(0.1F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));
            ModelPartData leaf = head.addChild("leaf", ModelPartBuilder.create().uv(0, 16).cuboid(-5.0F, -8.0F, -4.0F, 10.0F, 5.0F, 8.0F, new Dilation(0.3F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));
            ModelPartData nose = head.addChild("nose", ModelPartBuilder.create().uv(28, 0).cuboid(-3.0F, -4.0F, -5.0F, 6.0F, 4.0F, 1.0F, new Dilation(0.1F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));
            ModelPartData lEar = head.addChild("lEar", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 0.0F));
            ModelPartData cube_r1 = lEar.addChild("cube_r1", ModelPartBuilder.create().uv(24, 16).cuboid(-5.0F, -0.5F, -2.0F, 5.0F, 1.0F, 4.0F, new Dilation(0.1F)), ModelTransform.of(-5.0F, -6.5F, 0.0F, 0.0F, 0.0F, -0.9163F));
            ModelPartData lEar2 = head.addChild("lEar2", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 0.0F));
            ModelPartData cube_r2 = lEar2.addChild("cube_r2", ModelPartBuilder.create().uv(24, 16).mirrored().cuboid(0.0F, -0.5F, -2.0F, 5.0F, 1.0F, 4.0F, new Dilation(0.1F)).mirrored(false), ModelTransform.of(5.0F, -6.5F, 0.0F, 0.0F, 0.0F, 0.9163F));
            return TexturedModelData.of(modelData, 64, 64);
        }

        @Override
        public void setHeadRotation(float animationProgress, float yaw, float pitch) {
            this.head.yaw = yaw * 0.017453292F;
            this.head.pitch = pitch * 0.017453292F;
        }

        @Override
        public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha) {
            this.root.render(matrices, vertices, light, overlay, red, green, blue, alpha);
        }
    }
}
