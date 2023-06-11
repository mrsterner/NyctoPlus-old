package dev.sterner.block;

import dev.sterner.blockentity.LivingCoreBlockEntity;
import dev.sterner.registry.NyctoPlusObjects;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;

public class LivingCoreLogBlock extends BlockWithEntity {
    public LivingCoreLogBlock(Settings settings) {
        super(settings);
    }

    public static void generateTree(World world, BlockPos blockPos) {
        if(world.getBlockState(blockPos).isOf(Blocks.OAK_LOG)){
            world.setBlockState(blockPos, NyctoPlusObjects.LIVING_CORE_LOG.getDefaultState());
            if (world.getBlockEntity(blockPos) instanceof LivingCoreBlockEntity blockEntity) {
                blockEntity.startGenerateTree();
            }
        }
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new LivingCoreBlockEntity(pos, state);
    }


    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return (tickerWorld, pos, tickerState, blockEntity) -> {
            if (blockEntity instanceof LivingCoreBlockEntity be) {
                be.tick(world, pos, state);
            }
        };
    }

    @Override
    public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        if (world.getBlockEntity(pos) instanceof LivingCoreBlockEntity blockEntity) {
            blockEntity.destroyTree(world);
        }

        super.onBreak(world, pos, state, player);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }
}
