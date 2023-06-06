package dev.sterner.block;

import com.mojang.authlib.GameProfile;
import dev.sterner.blockentity.PeachBlockEntity;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.SkullBlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RotationPropertyHelper;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class PeachBlock extends BlockWithEntity {
    public static final int MAX_ROTATION_INDEX = RotationPropertyHelper.getMax();
    private static final int MAX_ROTATIONS = MAX_ROTATION_INDEX + 1;
    public static final IntProperty ROTATION = Properties.ROTATION;
    public static final IntProperty PITCH = IntProperty.of("pitch", 0, RotationPropertyHelper.getMax());
    private Type type = Type.NONE;

    public PeachBlock(Settings settings) {
        super(settings);
        this.setDefaultState((this.stateManager.getDefaultState()).with(ROTATION, 0).with(PITCH, 0));
    }

    public Type getSkullType() {
        return this.type;
    }

    public void setSkullType(Type type){
        this.type = type;
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return Block.createCuboidShape(4.0, 9.0, 4.0, 12.0, 16.0, 12.0);
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new PeachBlockEntity(pos, state);
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        super.onPlaced(world, pos, state, placer, itemStack);
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof SkullBlockEntity skullBlockEntity) {
            GameProfile gameProfile = null;
            if (itemStack.hasNbt()) {
                NbtCompound nbtCompound = itemStack.getNbt();
                if (nbtCompound.contains("SkullOwner", NbtElement.COMPOUND_TYPE)) {
                    gameProfile = NbtHelper.toGameProfile(nbtCompound.getCompound("SkullOwner"));
                } else if (nbtCompound.contains("SkullOwner", NbtElement.STRING_TYPE) && !Util.isBlank(nbtCompound.getString("SkullOwner"))) {
                    gameProfile = new GameProfile(null, nbtCompound.getString("SkullOwner"));
                }
            }

            skullBlockEntity.setOwner(gameProfile);
        }
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(ROTATION, RotationPropertyHelper.fromYaw(ctx.getPlayerYaw())).with(PITCH, RotationPropertyHelper.fromYaw(- ctx.getPlayer().getPitch()));
    }

    @Override
    public BlockState rotate(BlockState state, BlockRotation rotation) {
        return state.with(ROTATION, rotation.rotate(state.get(ROTATION), MAX_ROTATIONS));
    }

    @Override
    public BlockState mirror(BlockState state, BlockMirror mirror) {
        return state.with(ROTATION, mirror.mirror(state.get(ROTATION), MAX_ROTATIONS));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(ROTATION).add(PITCH);
    }

    enum Type{
        NONE,
        PLAYER,
        VILLAGER,
        PILLAGER,
        PIGLIN
    }
}
