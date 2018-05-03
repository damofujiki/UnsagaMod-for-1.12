//package mods.hinasch.unsaga.core.world;
//
//import java.util.List;
//
//import javax.annotation.Nonnull;
//
//import com.google.common.collect.Lists;
//
//import mods.hinasch.lib.util.UtilNBT;
//import mods.hinasch.lib.world.XYZPos;
//import mods.hinasch.unsaga.UnsagaMod;
//import net.minecraft.nbt.NBTTagCompound;
//import net.minecraft.world.World;
//import net.minecraft.world.WorldSavedData;
//
//public class WorldSaveDataStructure extends WorldSavedData{
//
//	List<XYZPos> list = Lists.newArrayList();
//
//	public WorldSaveDataStructure() {
//		this(KEY);
//		// TODO 自動生成されたコンストラクター・スタブ
//	}
//
//	public WorldSaveDataStructure(String key) {
//		super(key);
//		// TODO 自動生成されたコンストラクター・スタブ
//	}
//	public static final String KEY = UnsagaMod.MODID+".structureData";
//
//	public static WorldSaveDataStructure get(@Nonnull World world) {
//		WorldSaveDataStructure data = (WorldSaveDataStructure) world.loadItemData(WorldSaveDataStructure.class, KEY);
//		if(data==null){
//			data = new WorldSaveDataStructure();
//			world.setItemData(KEY, data);
//		}
//		  return data;
//	}
//
//	public boolean isNearStructure(int chunkX,int chunkZ){
//		for(XYZPos p:list){
//			if(p.getDistance(chunkX, 0, chunkZ)<300.0D){
//				return true;
//			}
//		}
//		return false;
//	}
//
//	public void addCoods(int x,int z){
//		list.add(new XYZPos(x,0,z));
//	}
//
//	public List<XYZPos> getCoords(){
//		return this.list;
//	}
//	public boolean isSpawnedFirstMerchantHouse(){
//		return this.list.size()>=1;
//	}
//	@Override
//	public void readFromNBT(NBTTagCompound nbt) {
//		if(nbt.hasKey(KEY)){
//			list = UtilNBT.readListFromNBT(nbt, KEY, in ->XYZPos.readFromNBT(in));
//		}
//
//	}
//
//	@Override
//	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
//		if(!list.isEmpty()){
//			UtilNBT.writeListToNBT(list, compound, KEY);
//		}
//
//		return compound;
//	}
//
//}
