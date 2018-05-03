//package mods.hinasch.unsaga.chest.old;
//
//import java.util.Collection;
//import java.util.Map;
//
//import javax.annotation.Nullable;
//
//import com.google.common.collect.Maps;
//
//import net.minecraft.util.math.BlockPos;
//import net.minecraft.util.math.ChunkPos;
//
//public class ChestPosCache {
//
//	Map<ChunkPos,IFieldChest> cache = Maps.newHashMap();
////	List<IFieldChest> clientCache = Lists.newArrayList();
//
//	private static ChestPosCache INSTANCE;
//
//	private ChestPosCache(){
//
//	}
//
//	public static ChestPosCache instance(){
//		if(INSTANCE == null){
//			INSTANCE = new ChestPosCache();
//		}
//		return INSTANCE;
//	}
//
////	public Cache<ChunkPos,IFieldChest> getCache(){
////		return this.cache;
////	}
//
//	public boolean isEmpty(){
//		return cache.isEmpty();
//	}
//	public void put(BlockPos pos,IFieldChest value){
//		this.put(new ChunkPos(pos), value);
//	}
//	public void put(ChunkPos pos,IFieldChest value){
//
//		if(cache.size()<=500){
//			cache.put(pos, value);
//		}
//	}
//	public @Nullable IFieldChest get(ChunkPos pos){
//		return cache.get(pos);
//	}
//
//	public @Nullable IFieldChest get(BlockPos pos){
//		return this.get(new ChunkPos(pos));
//	}
//
//	public Collection<IFieldChest> values(){
//		return cache.values();
//	}
//
//	public void invalidateAll(){
//		cache.clear();
//	}
//	public void invalidate(ChunkPos pos){
//		cache.remove(pos);
//	}
//	public void invalidate(BlockPos pos){
//		this.invalidate(new ChunkPos(pos));
//	}
//}
