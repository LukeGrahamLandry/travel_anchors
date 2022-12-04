package ca.lukegrahamlandry.travelstaff.util;

import ca.lukegrahamlandry.travelstaff.Constants;
import ca.lukegrahamlandry.travelstaff.platform.Services;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.storage.DimensionDataStorage;
import net.minecraft.world.phys.Vec3;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class TravelAnchorList extends SavedData {

    private static final TravelAnchorList clientInstance = new TravelAnchorList();

    public static TravelAnchorList get(Level level) {
        if (!level.isClientSide) {
            DimensionDataStorage storage = ((ServerLevel) level).getDataStorage();
            return storage.computeIfAbsent(TravelAnchorList::new, TravelAnchorList::new, Constants.MOD_ID);
        } else {
            return clientInstance;
        }
    }

    public TravelAnchorList() {
        
    }

    public TravelAnchorList(CompoundTag nbt) {
        this();
        this.load(nbt);
    }

    public final HashMap<BlockPos, Entry> anchors = new HashMap<>();

    public void load(@Nonnull CompoundTag nbt) {
        this.anchors.clear();
        if (nbt.contains("anchors", Tag.TAG_LIST)) {
            boolean anyOldFormat = false;
            ListTag list = nbt.getList("anchors", Tag.TAG_COMPOUND);
            for (int i = 0; i < list.size(); i++) {
                CompoundTag entryNBT = list.getCompound(i);
                if (entryNBT.contains("x") && entryNBT.contains("y") && entryNBT.contains("z") && entryNBT.contains("name")) {
                    BlockPos pos = new BlockPos(entryNBT.getInt("x"), entryNBT.getInt("y"), entryNBT.getInt("z")).immutable();
                    String name = entryNBT.getString("name");
                    if (!name.isEmpty()) {
                        BlockState state;

                        if (entryNBT.contains("state2")){
                            state = NbtUtils.readBlockState(entryNBT.getCompound("state2"));
                        }
                        else if (entryNBT.contains("state")){  // keep compat
                            state = Block.stateById(entryNBT.getInt("state"));
                            anyOldFormat = true;
                        }
                        else {
                            state = Constants.getTravelAnchor().defaultBlockState();
                        }

                        this.anchors.put(pos, new Entry(entryNBT.getString("name"), state));
                    }
                }
            }

            if (anyOldFormat) this.setDirty();
        }
    }

    @Nonnull
    @Override
    public CompoundTag save(@Nonnull CompoundTag compound) {
        ListTag list = new ListTag();
        for (Map.Entry<BlockPos, Entry> entry : this.anchors.entrySet()) {
            CompoundTag entryNBT = new CompoundTag();
            entryNBT.putInt("x", entry.getKey().getX());
            entryNBT.putInt("y", entry.getKey().getY());
            entryNBT.putInt("z", entry.getKey().getZ());
            entryNBT.putString("name", entry.getValue().name);
            entryNBT.put("state2", NbtUtils.writeBlockState(entry.getValue().state));
            list.add(entryNBT);
        }
        compound.put("anchors", list);
        return compound;
    }

    // during one run we trust the blockstate id to stay the same, so we get a smaller packet by not including full infop.
    // this also means network protocol doesn't change so compat with old versions
    public CompoundTag saveForNetwork() {
        CompoundTag compound = new CompoundTag();
        ListTag list = new ListTag();
        for (Map.Entry<BlockPos, Entry> entry : this.anchors.entrySet()) {
            CompoundTag entryNBT = new CompoundTag();
            entryNBT.putInt("x", entry.getKey().getX());
            entryNBT.putInt("y", entry.getKey().getY());
            entryNBT.putInt("z", entry.getKey().getZ());
            entryNBT.putString("name", entry.getValue().name);
            entryNBT.putInt("state", Block.getId(entry.getValue().state));
            list.add(entryNBT);
        }
        compound.put("anchors", list);
        return compound;
    }



    public void setAnchor(Level level, BlockPos pos, @Nullable String name, @Nullable BlockState state) {
        if (!level.isClientSide) {
            boolean needsUpdate = false;
            BlockPos immutablePos = pos.immutable();
            if (name == null || name.trim().isEmpty()) {
                if (this.anchors.containsKey(immutablePos)) {
                    this.anchors.remove(immutablePos);
                    needsUpdate = true;
                }
            } else {
                if (state == null) state = Constants.getTravelAnchor().defaultBlockState();
                Entry oldEntry = this.anchors.getOrDefault(immutablePos, null);
                if (oldEntry == null || !oldEntry.name.equals(name) || oldEntry.state != state) {
                    this.anchors.put(pos.immutable(), new Entry(name, state));
                    needsUpdate = true;
                }
            }
            this.setDirty();
            if (needsUpdate) {
                Services.NETWORK.sendAnchorListToClients((ServerLevel) level, this);
            }
        }
    }

    public String getAnchor(BlockPos pos) {
        Entry entry = this.getEntry(pos);
        return entry == null ? null : entry.name;
    }
    
    public Entry getEntry(BlockPos pos) {
        return this.anchors.getOrDefault(pos.immutable(), null);
    }

    public Stream<Pair<BlockPos, String>> getAnchorsAround(Vec3 pos, double maxDistanceSq) {
        return this.anchors.entrySet().stream()
                .filter(entry -> entry.getKey().distSqr(new Vec3i(pos.x, pos.y, pos.z)) < maxDistanceSq)
                .map(entry -> Pair.of(entry.getKey(), entry.getValue().name));
    }
    
    public static class Entry {
        
        public String name;
        public BlockState state;

        public Entry(String name, BlockState state) {
            this.name = name;
            this.state = state;
        }
    }
}

