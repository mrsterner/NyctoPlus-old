package dev.sterner.blockentity;

import com.google.common.collect.Lists;
import dev.sterner.block.PeachBlock;
import dev.sterner.block.PeachGrowthManager;
import dev.sterner.block.PeachLogBlock;
import dev.sterner.registry.NyctoPlusBlockEntityTypes;
import dev.sterner.registry.NyctoPlusObjects;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.nbt.NbtList;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.*;
import net.minecraft.world.World;
import net.minecraft.world.event.BlockPositionSource;
import net.minecraft.world.event.GameEvent;
import net.minecraft.world.event.PositionSource;
import net.minecraft.world.event.listener.GameEventListener;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

public class LivingCoreBlockEntity extends BlockEntity implements GameEventListener.Holder<LivingCoreBlockEntity.Listener> {
    private final Listener eventListener;
    private final List<BlockPos> availableGrowthNodes = Lists.newArrayList();
    private final List<BlockPos> blocksToReplace = Lists.newArrayList();
    private final List<BlockPos> leaves = Lists.newArrayList();
    private boolean foundLeaves = false;
    private int ticker = 0;

    public LivingCoreBlockEntity(BlockPos pos, BlockState state) {
        super(NyctoPlusBlockEntityTypes.LIVING_CORE_BLOCK_ENTITY, pos, state);
        this.eventListener = new Listener(new BlockPositionSource(pos), this);
    }

    public void tick(World world, BlockPos pos, BlockState state) {
        eventListener.manager.tick(world, pos, world.getRandom());

        if(!blocksToReplace.isEmpty()){
            ticker++;
            if(ticker >= 2){
                BlockPos nextPos = blocksToReplace.get(0);
                if(world.getBlockState(nextPos).isOf(Blocks.OAK_LOG)){
                    BlockState wood = world.getBlockState(nextPos);
                    world.breakBlock(nextPos, false);
                    world.setBlockState(nextPos, NyctoPlusObjects.PEACH_LOG.getDefaultState().with(PeachLogBlock.VARIANTS, world.getRandom().nextInt(2)).with(Properties.AXIS, wood.get(Properties.AXIS)));
                }
                blocksToReplace.remove(nextPos);
                ticker = 0;
            }
        }
    }

    public void populateNodes(World world){
        BlockPos origin = this.getPos().add(0,2,0);
        this.availableGrowthNodes.clear();
        this.collect(world, origin);

        leaves.addAll(leaves.stream().filter(blockPos -> world.getBlockState(blockPos.down()).isReplaceable()).toList());

        for (BlockPos leafPos : leaves) {
            if(world.getRandom().nextBoolean()){
                availableGrowthNodes.add(leafPos.toImmutable());
            }
        }
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        this.eventListener.manager.readNbt(nbt);
        if (nbt.contains("NodeList")) {
            NbtList list = nbt.getList("NodeList", NbtElement.COMPOUND_TYPE);
            for (int i = 0; i < list.size(); ++i) {
                NbtCompound nbtCompound = list.getCompound(i);
                availableGrowthNodes.add(NbtHelper.toBlockPos(nbtCompound));
            }
        }

        if (nbt.contains("BreakList")) {
            NbtList list = nbt.getList("BreakList", NbtElement.COMPOUND_TYPE);
            for (int i = 0; i < list.size(); ++i) {
                NbtCompound nbtCompound = list.getCompound(i);
                blocksToReplace.add(NbtHelper.toBlockPos(nbtCompound));
            }
        }
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        this.eventListener.manager.writeNbt(nbt);

        NbtList nbtList = new NbtList();
        for (BlockPos pos : availableGrowthNodes) {
            nbtList.add(NbtHelper.fromBlockPos(pos));
        }
        if (!nbtList.isEmpty()) {
            nbt.put("NodeList", nbtList);
        }

        NbtList nbtList2 = new NbtList();
        for (BlockPos pos : blocksToReplace) {
            nbtList2.add(NbtHelper.fromBlockPos(pos));
        }
        if (!nbtList2.isEmpty()) {
            nbt.put("BreakList", nbtList2);
        }
        super.writeNbt(nbt);
    }

    public void collect(World world, BlockPos startPos){

        Iterable<BlockPos> blockPosIterable = BlockPos.iterateOutwards(startPos,1 ,1 ,1);

        for (BlockPos nextPos: blockPosIterable) {
            if(nextPos != startPos){
                if (world.getBlockState(nextPos).isOf(Blocks.OAK_LEAVES) && !leaves.contains(nextPos)) {
                    foundLeaves = true;
                    leaves.add(nextPos);
                    collect(world, nextPos);



                } else if (world.getBlockState(nextPos).isOf(Blocks.OAK_LOG) && !foundLeaves) {
                    collect(world, nextPos);
                }
            }
        }
    }

    private BlockPos getGrowPos() {
        if (availableGrowthNodes.isEmpty() || world == null) {
            return null;
        }
        int picked = world.getRandom().nextBetween(0, availableGrowthNodes.size());
        BlockPos pos = availableGrowthNodes.get(picked);
        availableGrowthNodes.remove(picked);
        return pos;
    }

    public void startGenerateTree() {
        if (this.world == null) {
            return;
        }

        BlockPos pos = this.getPos();
        //Trunk base
        for (Direction dir : Direction.Type.HORIZONTAL) {
            if (world.getBlockState(pos.offset(dir)).isReplaceable()) {
                BlockState state = NyctoPlusObjects.PEACH_LOG.getDefaultState().with(PeachLogBlock.VARIANTS, world.getRandom().nextInt(2));
                BlockPos offsetPos = pos.offset(dir);
                if(world.getBlockState(offsetPos.up()).isReplaceable() && world.getRandom().nextBoolean()){
                    world.setBlockState(offsetPos.up(), state.with(Properties.AXIS, dir.getAxis()));
                } else {
                    state = state.with(Properties.AXIS, dir.getAxis());
                }
                world.setBlockState(offsetPos, state);
                if(world.getBlockState(offsetPos.down()).isReplaceable()){
                    world.setBlockState(offsetPos.down(), state);
                }
            } else {
                world.breakBlock(pos, false);
                return;
            }
        }

        //if trunk was places successfully, start with the rest of the tree
        blocksToReplace.clear();
        replaceOakLogs(world, getPos(), getPos());
    }

    private void replaceOakLogs(World world, BlockPos startPos, BlockPos livingCorePos) {
        Iterable<BlockPos> checkCube = BlockPos.iterateOutwards(startPos, 1,1,1);

        for (BlockPos nextPos : checkCube) {
            if (nextPos != startPos && !blocksToReplace.contains(nextPos) && nextPos.isWithinDistance(livingCorePos, 16)) {
                if (world.getBlockState(nextPos).isOf(Blocks.OAK_LOG)) {
                    blocksToReplace.add(nextPos.toImmutable());
                    replaceOakLogs(world, nextPos, livingCorePos);
                }
            }
        }
    }

    @Override
    public Listener getEventListener() {
        return this.eventListener;
    }

    public static class Listener implements GameEventListener {
        private final PositionSource positionSource;
        private final PeachGrowthManager manager;
        private final LivingCoreBlockEntity blockEntity;

        public Listener(PositionSource positionSource, LivingCoreBlockEntity livingCoreBlockEntity) {
            this.positionSource = positionSource;
            this.manager = PeachGrowthManager.create();
            this.blockEntity = livingCoreBlockEntity;
        }

        @Override
        public PositionSource getPositionSource() {
            return this.positionSource;
        }

        @Override
        public int getRange() {
            return 8;
        }

        @Override
        public GameEventListener.TriggerOrder getTriggerOrder() {
            return GameEventListener.TriggerOrder.BY_DISTANCE;
        }

        @Override
        public boolean listen(ServerWorld world, GameEvent event, GameEvent.Emitter emitter, Vec3d emitterPos) {
            if (event == GameEvent.ENTITY_DIE) {
                Entity entity = emitter.sourceEntity();
                if (blockEntity.getGrowPos() != null) {
                    if (entity instanceof PlayerEntity playerEntity) {
                        this.manager.growHead(playerEntity, blockEntity.getGrowPos());
                        return true;
                    } else if (entity instanceof VillagerEntity){
                        this.manager.growHead(world, blockEntity.getGrowPos(), PeachBlock.Type.VILLAGER);
                        return true;
                    }
                }


            }
            return false;
        }
    }
}
