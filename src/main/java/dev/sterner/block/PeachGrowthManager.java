package dev.sterner.block;

import dev.sterner.blockentity.PeachBlockEntity;
import dev.sterner.registry.NyctoPlusObjects;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

public class PeachGrowthManager {

    public static PeachGrowthManager create() {
        return new PeachGrowthManager();
    }

    public void growHead(PlayerEntity playerEntity, BlockPos pos) {
        World world = playerEntity.getWorld();
        BlockState state = NyctoPlusObjects.PEACH.getDefaultState();
        world.setBlockState(pos, state);
        if (world.getBlockEntity(pos) instanceof PeachBlockEntity peachBlockEntity) {
            peachBlockEntity.setOwner(playerEntity.getGameProfile());
            peachBlockEntity.markDirty();
        }
    }

    public void tick(WorldAccess world, BlockPos pos, Random random) {

    }

    public void readNbt(NbtCompound nbt) {

    }

    public void writeNbt(NbtCompound nbt) {

    }
}
