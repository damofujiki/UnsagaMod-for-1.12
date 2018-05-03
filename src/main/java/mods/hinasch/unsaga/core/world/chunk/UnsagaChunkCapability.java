package mods.hinasch.unsaga.core.world.chunk;

import java.util.List;

import mods.hinasch.lib.capability.CapabilityAdapterFactory.CapabilityAdapterPlanImpl;
import mods.hinasch.lib.capability.CapabilityAdapterFrame;
import mods.hinasch.lib.capability.CapabilityStorage;
import mods.hinasch.lib.capability.ComponentCapabilityAdapters;
import mods.hinasch.lib.client.ClientHelper;
import mods.hinasch.lib.network.PacketSyncCapability;
import mods.hinasch.lib.util.UtilNBT;
import mods.hinasch.unsaga.UnsagaMod;
import mods.hinasch.unsaga.chest.ChunkChestInfo;
import mods.hinasch.unsaga.core.client.gui.ChestPosCache;
import mods.hinasch.unsaga.element.AspectOffset;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class UnsagaChunkCapability {

	@CapabilityInject(IUnsagaChunk.class)
	public static Capability<IUnsagaChunk> CAPA;
	public static final String SYNC_ID = "unsaga.chunk";

	public static final CapabilityAdapterFrame<IUnsagaChunk> BUILDER = UnsagaMod.capabilityAdapterFactory.create(new CapabilityAdapterPlanImpl(
			()->CAPA,()->IUnsagaChunk.class,()->DefaultImpl.class,Storage::new));
	public static final ComponentCapabilityAdapters.Chunk<IUnsagaChunk> ADAPTER = BUILDER.createChildChunk(SYNC_ID);

	static{

		ADAPTER.setPredicate(ev -> ev.getObject() instanceof Chunk);
		ADAPTER.setRequireSerialize(true);
	}

	public static class DefaultImpl implements IUnsagaChunk{
		AspectOffset aspect = AspectOffset.ZERO;
		//		ElementTable fluctuation = ElementTable.ZERO;
		ChunkChestInfo chestInfo = new ChunkChestInfo();


		@Override
		public AspectOffset getAspectOffset() {
			// TODO 自動生成されたメソッド・スタブ
			return this.aspect;
		}

		@Override
		public void setAspectOffset(AspectOffset elm) {
			// TODO 自動生成されたメソッド・スタブ

		}

		@Override
		public ChunkChestInfo getChunkChestInfo() {
			// TODO 自動生成されたメソッド・スタブ
			return this.chestInfo;
		}

		@Override
		public void setChunkChestInfo(ChunkChestInfo info) {
			// TODO 自動生成されたメソッド・スタブ
			this.chestInfo = info;
		}

		@Override
		public NBTTagCompound getSendingData() {
			// TODO 自動生成されたメソッド・スタブ
			return null;
		}

		@Override
		public void catchSyncData(NBTTagCompound nbt) {
			// TODO 自動生成されたメソッド・スタブ

		}

		@Override
		public void onPacket(PacketSyncCapability message, MessageContext ctx) {
			// TODO 自動生成されたメソッド・スタブ
			if(ctx.side.isClient()){

				WorldClient world = (WorldClient) ClientHelper.getWorld();
//				int operation = message.getArgs().getInteger("op");
				if(message.getArgs().hasKey("chests")){
					List<ChunkChestInfo> chests = UtilNBT.readListFromNBT(message.getArgs(), "chests", ChunkChestInfo.RESTORE_FUC);
					ChestPosCache.setChestPosCache(0,chests);
				}


			}

		}

		@Override
		public String getIdentifyName() {
			// TODO 自動生成されたメソッド・スタブ
			return SYNC_ID;
		}

	}

	public static class Storage extends CapabilityStorage<IUnsagaChunk>{

		@Override
		public void writeNBT(NBTTagCompound comp, Capability<IUnsagaChunk> capability,
				IUnsagaChunk instance, EnumFacing side) {
			//			if(instance.getFluctuation()!=ElementTable.ZERO){
			//				int[] intarray = new int[6];
			//				int index = 0;
			//				Arrays.fill(intarray, 0);
			//				for(int i=0;i<6;i++){
			//					FiveElements.Type type = FiveElements.Type.fromMeta(i);
			//					intarray[i] = instance.getFluctuation().getInt(type);
			//				}
			//
			//				comp.setIntArray("elements", intarray);
			//			}
			//			if(!instance.hasChestAppeared()){
			//				new XYZPos(instance.getChestPos()).writeToNBT(comp);
			//				comp.setByte("chestType", (byte) instance.getChestType().getMeta());
			//			}
			//			comp.setBoolean("initialize", instance.hasInitialized());
			NBTTagCompound nbt = UtilNBT.compound();

			instance.getChunkChestInfo().writeToNBT(nbt);
			comp.setTag("chestInfo", nbt);
		}

		@Override
		public void readNBT(NBTTagCompound comp, Capability<IUnsagaChunk> capability,
				IUnsagaChunk instance, EnumFacing side) {
			//			if(comp.hasKey("elements")){
			//				ElementTable.Mutable mutable = new ElementTable.Mutable();
			//				int[] intarray = comp.getIntArray("elements");
			//				for(int i=0;i<6;i++){
			//					FiveElements.Type type = FiveElements.Type.fromMeta(i);
			//					mutable.add(type, intarray[i]);
			//				}
			//				instance.setFluctuation(mutable.toImmutable());
			//			}
			//			XYZPos pos = XYZPos.readFromNBT(comp);
			//			if(pos!=null){
			//				instance.setChestPos(pos);
			//			}
			//			if(comp.hasKey("chestType")){
			//				instance.setChestType(FieldChestType.fromMeta(comp.getByte("chestType")));
			//			}
			//			if(comp.hasKey("initialize")){
			//				instance.setInitialized(comp.getBoolean("initialize"));
			//			}
			if(comp.hasKey("chestInfo")){
				NBTTagCompound child = (NBTTagCompound) comp.getTag("chestInfo");
				ChunkChestInfo info = instance.getChunkChestInfo().RESTORE_FUC.apply(child);
				instance.setChunkChestInfo(info);
			}

		}

	}

	public static void onLivingUpdate(LivingUpdateEvent e){
		World world = e.getEntityLiving().getEntityWorld();
		Chunk chunk = world.getChunkFromBlockCoords(e.getEntityLiving().getPosition());
		if(ADAPTER.hasCapability(chunk)){
			IUnsagaChunk instance = ADAPTER.getCapability(chunk);
			long current = world.getTotalWorldTime();

			if(instance.getAspectOffset().getNextEffectTime()>0){

				if(instance.getAspectOffset().getNextEffectTime()<current){
					UnsagaMod.logger.trace("aspect", "平衡がすすみました");
					instance.getAspectOffset().processEquilibrium(world.rand, current);
				}
			}else{
				instance.setAspectOffset(AspectOffset.ZERO);
			}
		}
	}
	public static void registerEvents(){
		//		HSLibs.registerEvent(new EventChunkLoad());
		PacketSyncCapability.registerSyncCapability(SYNC_ID, CAPA);
		ADAPTER.registerAttachEvent((inst,capa,facing,ev)->{

		});
	}


}
