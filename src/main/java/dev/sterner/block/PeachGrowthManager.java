package dev.sterner.block;

import dev.sterner.blockentity.LivingCoreBlockEntity;
import dev.sterner.blockentity.PeachBlockEntity;
import dev.sterner.registry.NyctoPlusObjects;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

public class PeachGrowthManager {

    private PeachGrowthManager() {

    }

    public static PeachGrowthManager create() {
        return new PeachGrowthManager();
    }

    public void growHead(PlayerEntity playerEntity, LivingCoreBlockEntity entity) {
        getPeach(playerEntity.getWorld(), entity);
        if (playerEntity.getWorld().getBlockEntity(entity.getGrowPos()) instanceof PeachBlockEntity peachBlockEntity) {
            peachBlockEntity.setSkullType(PeachBlockEntity.Type.PLAYER);
            peachBlockEntity.setOwner(playerEntity.getGameProfile());
            peachBlockEntity.markDirty();
        }
    }

    public void growHead(World world, LivingCoreBlockEntity entity, PeachBlockEntity.Type type) {
        getPeach(world, entity);
        if (world.getBlockEntity(entity.getGrowPos()) instanceof PeachBlockEntity peachBlockEntity) {
            peachBlockEntity.setSkullType(type);
            peachBlockEntity.markDirty();
        }
    }

    private void getPeach(World world, LivingCoreBlockEntity entity) {
        if (!world.getBlockState(entity.getGrowPos().up()).isOf(Blocks.OAK_LEAVES)) {
            entity.availableGrowthNodes.remove(entity.getGrowPos().up());
        } else if (world.getBlockState(entity.getGrowPos()).isReplaceable()) {
            int pitch = world.getRandom().nextInt(25);
            int rot = world.getRandom().nextInt(360);
            BlockState state = NyctoPlusObjects.PEACH.getDefaultState().with(PeachBlock.ROTATION, rot).with(PeachBlock.PITCH, pitch);
            world.setBlockState(entity.getGrowPos(), state);
        }
    }

    public void tick(WorldAccess world, BlockPos pos, Random random) {

    }

    public void readNbt(NbtCompound nbt) {

    }

    public void writeNbt(NbtCompound nbt) {

    }
}
