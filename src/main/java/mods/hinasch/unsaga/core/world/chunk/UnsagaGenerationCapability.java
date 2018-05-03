//package mods.hinasch.unsaga.core.world.chunk;
//
//import java.util.List;
//import java.util.Map;
//import java.util.stream.Collectors;
//
//import com.google.common.collect.Maps;
//
//import mods.hinasch.lib.capability.CapabilityAdapterFactory.CapabilityAdapterPlanImpl;
//import mods.hinasch.lib.capability.CapabilityAdapterFactory.ICapabilityAdapterPlan;
//import mods.hinasch.lib.capability.CapabilityAdapterFrame;
//import mods.hinasch.lib.capability.CapabilityStorage;
//import mods.hinasch.lib.capability.ComponentCapabilityAdapters;
//import mods.hinasch.lib.util.UtilNBT;
//import mods.hinasch.lib.world.XYZPos;
//import mods.hinasch.unsaga.UnsagaMod;
//import mods.hinasch.unsaga.core.world.IUnsagaGeneration;
//import net.minecraft.nbt.NBTTagCompound;
//import net.minecraft.nbt.NBTTagList;
//import net.minecraft.util.EnumFacing;
//import net.minecraft.util.ResourceLocation;
//import net.minecraft.util.math.ChunkPos;
//import net.minecraftforge.common.capabilities.Capability;
//import net.minecraftforge.common.capabilities.CapabilityInject;
//
//public class UnsagaGenerationCapability {
//
//	@CapabilityInject(IUnsagaGeneration.class)
//	public static Capability<IUnsagaGeneration> CAPA;
//	public static final String SYNC_ID = "unsaga.ability_attachable";
//	public static final ResourceLocation MERCHANT_HOUSE = new ResourceLocation("merchant_house");
//
//	public static ICapabilityAdapterPlan<IUnsagaGeneration> blueprint =
//			new CapabilityAdapterPlanImpl(()->CAPA,()->IUnsagaGeneration.class,()->DefaultImpl.class,Storage::new);
//
//	public static CapabilityAdapterFrame<IUnsagaGeneration> builder = UnsagaMod.capabilityAdapterFactory.create(blueprint);
//	public static ComponentCapabilityAdapters.World<IUnsagaGeneration> adapter = builder.createChildWorld(SYNC_ID);
//
//	static{
//
//		adapter.setRequireSerialize(true);
//	}
//
//
//	public static class DefaultImpl implements IUnsagaGeneration{
//
//		Map<ResourceLocation,XYZPos> structures = Maps.newHashMap();
//
//		@Override
//		public boolean isNearStructure(ResourceLocation id, ChunkPos coords) {
//			// TODO 自動生成されたメソッド・スタブ
//			return this.getCoords(id).stream().allMatch(in -> in.getDistance(coords.x, 0, coords.z)<300.0D);
//		}
//
//		@Override
//		public void addCoods(ResourceLocation id, ChunkPos coords) {
//			// TODO 自動生成されたメソッド・スタブ
//			this.structures.put(id, new XYZPos(coords.x,0,coords.z));
//		}
//
//		@Override
//		public List<XYZPos> getCoords(ResourceLocation id) {
//			return structures.entrySet().stream().filter(in -> in.getKey()==MERCHANT_HOUSE).map(in -> in.getValue()).collect(Collectors.toList());
//		}
//
//		@Override
//		public boolean isSpawnedFirstMerchantHouse() {
//			// TODO 自動生成されたメソッド・スタブ
//			return this.getCoords(MERCHANT_HOUSE).size()>=1;
//		}
//
//		@Override
//		public Map<ResourceLocation, XYZPos> getMap() {
//			// TODO 自動生成されたメソッド・スタブ
//			return this.structures;
//		}
//
//		@Override
//		public void setMap(Map<ResourceLocation, XYZPos> map) {
//			// TODO 自動生成されたメソッド・スタブ
//			this.structures = map;
//		}
//
//	}
//
//	public static class Storage extends CapabilityStorage<IUnsagaGeneration>{
//
//		@Override
//		public void writeNBT(NBTTagCompound comp, Capability<IUnsagaGeneration> capability,
//				IUnsagaGeneration instance, EnumFacing side) {
//			NBTTagList tagList = UtilNBT.tagList();
//			instance.getMap().entrySet().stream().forEach(in ->{
//				NBTTagCompound child = UtilNBT.compound();
//				in.getValue().writeToNBT(child);
//				child.setString("name", in.getKey().getResourcePath());
//				tagList.appendTag(child);
//			});
//			comp.setTag("structures", tagList);
//		}
//
//		@Override
//		public void readNBT(NBTTagCompound comp, Capability<IUnsagaGeneration> capability, IUnsagaGeneration instance,
//				EnumFacing side) {
//			Map<ResourceLocation,XYZPos> map = Maps.newHashMap();
//			NBTTagList tagList = UtilNBT.getTagList(comp, "structures");
//			tagList.iterator().forEachRemaining(in ->{
//				NBTTagCompound nbt = (NBTTagCompound) in;
//				XYZPos pos = XYZPos.readFromNBT(nbt);
//				String name = nbt.getString("name");
//				map.put(new ResourceLocation(name), pos);
//			});
//			instance.setMap(map);
//		}
//
//	}
//}
