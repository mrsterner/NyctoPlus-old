package dev.sterner.client.model;

import dev.sterner.NyctoPlus;
import net.minecraft.client.model.*;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.util.math.MatrixStack;

public class ShimenawaModel extends Model {
    public static final EntityModelLayer LAYER = new EntityModelLayer(NyctoPlus.id("shimenawa"), "main");

    private final ModelPart base;

    public ShimenawaModel(ModelPart root) {
        super(RenderLayer::getEntityTranslucent);
        this.base = root.getChild("base");

    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData base = modelPartData.addChild("base", ModelPartBuilder.create().uv(0, 23).cuboid(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 16.0F, 0.0F));

        ModelPartData rope = base.addChild("rope", ModelPartBuilder.create().uv(0, 0).cuboid(-10.0F, -1.5F, -10.0F, 20.0F, 3.0F, 20.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.2618F, 0.0F, 0.0F));
        rope.addChild("details", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData bone = base.addChild("bone", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 8.0F, 0.0F));

        bone.addChild("cube_r1", ModelPartBuilder.create().uv(0, 18).cuboid(18.0F, -3.0F, -1.5F, 0.0F, 5.0F, 5.0F, new Dilation(0.0F)).uv(0, 18).cuboid(0.0F, -3.0F, -1.5F, 0.0F, 5.0F, 5.0F, new Dilation(0.0F)), ModelTransform.of(-9.0F, -2.5F, -6.5F, 0.7854F, 0.0F, 0.0F));
        bone.addChild("cube_r2", ModelPartBuilder.create().uv(0, 18).cuboid(18.0F, -2.0F, -1.5F, 0.0F, 5.0F, 5.0F, new Dilation(0.0F)).uv(0, 18).cuboid(0.0F, -2.0F, -1.5F, 0.0F, 5.0F, 5.0F, new Dilation(0.0F)), ModelTransform.of(-9.0F, -5.5F, 2.5F, 0.7854F, 0.0F, 0.0F));
        bone.addChild("cube_r3", ModelPartBuilder.create().uv(0, 18).cuboid(18.0F, -3.5F, -3.5F, 0.0F, 5.0F, 5.0F, new Dilation(0.0F)).uv(0, 18).cuboid(0.0F, -3.5F, -3.5F, 0.0F, 5.0F, 5.0F, new Dilation(0.0F)), ModelTransform.of(-9.0F, -4.5F, -0.5F, 0.7854F, 0.0F, 0.0F));
        bone.addChild("cube_r4", ModelPartBuilder.create().uv(0, 23).cuboid(-2.5F, -2.5F, 0.0F, 5.0F, 5.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(3.5F, -2.5F, -9.0F, 0.0F, 0.0F, 0.7854F));
        bone.addChild("cube_r5", ModelPartBuilder.create().uv(0, 23).cuboid(-2.5F, -2.5F, 0.0F, 5.0F, 5.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(7.5F, -2.5F, -9.0F, 0.0F, 0.0F, 0.7854F));
        bone.addChild("cube_r6", ModelPartBuilder.create().uv(0, 23).cuboid(-2.5F, -2.5F, 0.0F, 5.0F, 5.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(-3.5F, -2.5F, -9.0F, 0.0F, 0.0F, 0.7854F));
        bone.addChild("cube_r7", ModelPartBuilder.create().uv(0, 23).cuboid(-2.5F, -2.5F, 0.0F, 5.0F, 5.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(3.5F, -7.1569F, 9.0F, 0.0F, 0.0F, 0.7854F));
        bone.addChild("cube_r8", ModelPartBuilder.create().uv(0, 23).cuboid(-2.5F, -2.5F, 0.0F, 5.0F, 5.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(7.5F, -7.1569F, 9.0F, 0.0F, 0.0F, 0.7854F));
        bone.addChild("cube_r9", ModelPartBuilder.create().uv(0, 23).cuboid(-2.5F, -2.5F, 0.0F, 5.0F, 5.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(-3.5F, -7.1569F, 9.0F, 0.0F, 0.0F, 0.7854F));
        bone.addChild("cube_r10", ModelPartBuilder.create().uv(0, 23).cuboid(-2.5F, -2.5F, 0.0F, 5.0F, 5.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(-7.5F, -7.1569F, 9.0F, 0.0F, 0.0F, 0.7854F));
        bone.addChild("cube_r11", ModelPartBuilder.create().uv(0, 23).cuboid(-2.5F, -2.5F, 0.0F, 5.0F, 5.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(-7.5F, -2.5F, -9.0F, 0.0F, 0.0F, 0.7854F));

        ModelPartData bone2 = bone.addChild("bone2", ModelPartBuilder.create(), ModelTransform.pivot(-9.0F, -7.0F, 7.5F));
        bone2.addChild("cube_r12", ModelPartBuilder.create().uv(0, 18).cuboid(18.0F, -1.5F, -1.5F, 0.0F, 5.0F, 5.0F, new Dilation(0.0F)).uv(0, 18).cuboid(0.0F, -1.5F, -1.5F, 0.0F, 5.0F, 5.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, -1.4142F, 0.7854F, 0.0F, 0.0F));
        return TexturedModelData.of(modelData, 128, 128);
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
        base.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
    }
}
