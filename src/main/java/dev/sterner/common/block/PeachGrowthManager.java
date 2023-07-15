package dev.sterner.common.block;

import dev.sterner.common.blockentity.LivingCoreBlockEntity;
import dev.sterner.common.blockentity.PeachBlockEntity;
import dev.sterner.registry.NyctoPlusObjects;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RotationPropertyHelper;
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
        BlockPos pos = getPeach(playerEntity.getWorld(), entity);
        if (pos != null && playerEntity.getWorld().getBlockEntity(pos) instanceof PeachBlockEntity peachBlockEntity) {
            peachBlockEntity.setSkullType(PeachBlockEntity.Type.PLAYER);
            peachBlockEntity.setOwner(playerEntity.getGameProfile());
            peachBlockEntity.markDirty();
        }
    }

    public void growHead(World world, LivingCoreBlockEntity entity, PeachBlockEntity.Type type) {
        BlockPos pos = getPeach(world, entity);
        if (pos != null && world.getBlockEntity(pos) instanceof PeachBlockEntity peachBlockEntity) {
            peachBlockEntity.setSkullType(type);
            peachBlockEntity.markDirty();
        }
    }

    private BlockPos getPeach(World world, LivingCoreBlockEntity entity) {
        BlockPos tryPos = entity.getGrowPos().down();
        if (!world.getBlockState(entity.getGrowPos()).isOf(Blocks.OAK_LEAVES)) {
            entity.availableGrowthNodes.remove(entity.getGrowPos());
        } else if (world.getBlockState(tryPos).isReplaceable()) {
            int pitch = world.getRandom().nextInt(25);
            int rot = world.getRandom().nextInt(360);
            BlockState state = NyctoPlusObjects.PEACH.getDefaultState().with(PeachBlock.ROTATION, RotationPropertyHelper.fromYaw(rot)).with(PeachBlock.PITCH, RotationPropertyHelper.fromYaw(pitch));
            world.setBlockState(tryPos, state);
            return tryPos;
        }
        return null;
    }

    public void tick(WorldAccess world, BlockPos pos, Random random) {

    }

    public void readNbt(NbtCompound nbt) {

    }

    public void writeNbt(NbtCompound nbt) {

    }
}
