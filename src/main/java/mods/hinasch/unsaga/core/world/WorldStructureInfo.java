package mods.hinasch.unsaga.core.world;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.google.common.collect.Maps;

import mods.hinasch.lib.iface.INBTWritable;
import mods.hinasch.lib.util.UtilNBT;
import mods.hinasch.lib.util.UtilNBT.RestoreFunc;
import mods.hinasch.lib.world.XYZPos;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.ChunkPos;

public class WorldStructureInfo implements INBTWritable{

	public static final RestoreFunc<WorldStructureInfo> RESTORE_FUC = comp ->{
		WorldStructureInfo instance = new WorldStructureInfo();
		Map<ResourceLocation,XYZPos> map = Maps.newHashMap();
		NBTTagList tagList = UtilNBT.getTagList(comp, "structures");
		tagList.iterator().forEachRemaining(in ->{
			NBTTagCompound nbt = (NBTTagCompound) in;
			XYZPos pos = XYZPos.readFromNBT(nbt);
			String name = nbt.getString("name");
			map.put(new ResourceLocation(name), pos);
		});
		instance.setMap(map);
		return instance;
	};
	public static final ResourceLocation MERCHANT_HOUSE = new ResourceLocation("merchant_house");
	Map<ResourceLocation,XYZPos> structures = Maps.newHashMap();


	public boolean isNearStructure(ResourceLocation id, ChunkPos coords) {
		// TODO 自動生成されたメソッド・スタブ
		return this.getCoords(id).stream().allMatch(in -> in.getDistance(coords.x, 0, coords.z)<300.0D);
	}


	public void addCoods(ResourceLocation id, ChunkPos coords) {
		// TODO 自動生成されたメソッド・スタブ
		this.structures.put(id, new XYZPos(coords.x,0,coords.z));
	}


	public List<XYZPos> getCoords(ResourceLocation id) {
		return structures.entrySet().stream().filter(in -> in.getKey()==MERCHANT_HOUSE).map(in -> in.getValue()).collect(Collectors.toList());
	}


	public boolean isSpawnedFirstMerchantHouse() {
		// TODO 自動生成されたメソッド・スタブ
		return this.getCoords(MERCHANT_HOUSE).size()>=1;
	}


	public Map<ResourceLocation, XYZPos> getMap() {
		// TODO 自動生成されたメソッド・スタブ
		return this.structures;
	}


	public void setMap(Map<ResourceLocation, XYZPos> map) {
		// TODO 自動生成されたメソッド・スタブ
		this.structures = map;
	}

	@Override
	public void writeToNBT(NBTTagCompound comp) {
		NBTTagList tagList = UtilNBT.tagList();
		this.getMap().entrySet().stream().forEach(in ->{
			NBTTagCompound child = UtilNBT.compound();
			in.getValue().writeToNBT(child);
			child.setString("name", in.getKey().getResourcePath());
			tagList.appendTag(child);
		});
		comp.setTag("structures", tagList);
	}
}
