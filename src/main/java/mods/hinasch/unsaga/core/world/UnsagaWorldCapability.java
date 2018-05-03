package mods.hinasch.unsaga.core.world;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import com.google.common.collect.Maps;

import mods.hinasch.lib.capability.CapabilityAdapterFactory.CapabilityAdapterPlanImpl;
import mods.hinasch.lib.capability.CapabilityAdapterFrame;
import mods.hinasch.lib.capability.CapabilityStorage;
import mods.hinasch.lib.capability.ComponentCapabilityAdapters;
import mods.hinasch.lib.client.ClientHelper;
import mods.hinasch.lib.core.HSLib;
import mods.hinasch.lib.iface.INBTWritable;
import mods.hinasch.lib.network.PacketSyncCapability;
import mods.hinasch.lib.util.UtilNBT;
import mods.hinasch.lib.util.UtilNBT.RestoreFunc;
import mods.hinasch.lib.world.WorldHelper;
import mods.hinasch.unsaga.UnsagaMod;
import mods.hinasch.unsaga.core.inventory.InventorySkillPanel;
import mods.hinasch.unsaga.core.inventory.container.MatrixAdapterItemStack;
import mods.hinasch.unsaga.skillpanel.SkillPanelAPI;
import mods.hinasch.unsaga.skillpanel.SkillPanelBonus;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class UnsagaWorldCapability {
	@CapabilityInject(IUnsagaWorld.class)
	public static Capability<IUnsagaWorld> CAPA;
	public static final String SYNC_ID = "unsaga.world";
	public static final UUID UUID_DEBUG = UUID.fromString("db8ff9a2-9ef5-4371-9e98-42d694add74a");

	public static final CapabilityAdapterFrame<IUnsagaWorld> BUILDER = UnsagaMod.capabilityAdapterFactory.create(
			new CapabilityAdapterPlanImpl(()->CAPA,()->IUnsagaWorld.class,()->DefaultImpl.class,Storage::new));
	public static final ComponentCapabilityAdapters.World<IUnsagaWorld> ADAPTER = BUILDER.createChildWorld(SYNC_ID);

	static{
		ADAPTER.setPredicate(ev -> ev.getObject() instanceof World);
	}

	public static class DefaultImpl implements IUnsagaWorld{

		WorldStructureInfo worldStructureInfo = new WorldStructureInfo();
		protected Map<UUID,NonNullList<ItemStack>> panelDataPerUser = Maps.newHashMap();

		@Override
		public NonNullList<ItemStack> getPanels(UUID uuid) {
			return this.panelDataPerUser.get(HSLib.isDebug() ? UUID_DEBUG : uuid);
		}

		@Override
		public void setPanels(UUID uuid, NonNullList<ItemStack> list) {
			this.panelDataPerUser.put(HSLib.isDebug() ?UUID_DEBUG : uuid , list);
		}

		@Override
		public void dumpData() {
			// TODO 自動生成されたメソッド・スタブ

		}

		@Override
		public Map<UUID, NonNullList<ItemStack>> getRawPanelMap() {
			// TODO 自動生成されたメソッド・スタブ
			return this.panelDataPerUser;
		}

		@Override
		public void clearData() {
			// TODO 自動生成されたメソッド・スタブ
			this.panelDataPerUser = Maps.newHashMap();
		}

		@Override
		public NBTTagCompound getSendingData() {
			// TODO 自動生成されたメソッド・スタブ
			return (NBTTagCompound) CAPA.writeNBT(this, null);
		}

		@Override
		public void catchSyncData(NBTTagCompound nbt) {
			// TODO 自動生成されたメソッド・スタブ
			CAPA.readNBT(this, null, nbt);
		}

		@Override
		public void onPacket(PacketSyncCapability message, MessageContext ctx) {
			if(ctx.side.isServer()){
				EntityPlayer ep = ctx.getServerHandler().player;
				World world = ctx.getServerHandler().player.getEntityWorld();
				HSLib.getPacketDispatcher().sendTo(PacketSyncCapability.create(CAPA, UnsagaWorldCapability.ADAPTER.getCapability(world)), (EntityPlayerMP) ep);
				InventorySkillPanel inv = new InventorySkillPanel();
				//				WorldSaveDataSkillPanel data = WorldSaveDataSkillPanel.get(e.getWorld());
				NonNullList<ItemStack> panelList = SkillPanelAPI.getPanelStacks(ep);
				inv.applyItemStackList(panelList);
				MatrixAdapterItemStack matrix = new MatrixAdapterItemStack(inv);
				boolean lineBonus = !matrix.checkLine().isEmpty();
				SkillPanelBonus.applyBonus(matrix, ep, lineBonus);
			}
			if(ctx.side.isClient()){
				//				EntityPlayer ep = ClientHelper.getPlayer();
				//				if(message.getPanelList()!=null){
				UnsagaMod.logger.trace(this.getClass().getName(), "スキルパネル同期完了");
				UnsagaWorldCapability.ADAPTER.getCapability(ClientHelper.getWorld()).catchSyncData(message.getNbt());
//				this.catchSyncData(message.getNbt());
//				AsyncUpdateEvent ev = new AsyncEvent(ClientHelper.getPlayer(),"sync",message.getNbt());
//				HSLib.addAsyncEvent(ev.getSender(), ev);
				//					HSLib.core().events.scannerEventPool.addEvent(new WaitUntilWorldPresent(ClientHelper.getPlayer(),"sync",message));
				//					WorldSaveDataSkillPanel.get(ClientHelper.getWorld()).setPanels(message.getUUID(), message.getPanelList());
				//				}

			}
		}

		@Override
		public String getIdentifyName() {
			// TODO 自動生成されたメソッド・スタブ
			return SYNC_ID;
		}

		@Override
		public WorldStructureInfo getWorldStructureInfo() {
			// TODO 自動生成されたメソッド・スタブ
			return this.worldStructureInfo;
		}

		@Override
		public void setWorldStructureInfo(WorldStructureInfo info) {
			// TODO 自動生成されたメソッド・スタブ
			this.worldStructureInfo = info;
		}

	}

	public static class Storage extends CapabilityStorage<IUnsagaWorld>{

		@Override
		public void writeNBT(NBTTagCompound comp, Capability<IUnsagaWorld> capability,
				IUnsagaWorld instance, EnumFacing side) {
			//			UnsagaMod.logger.trace(this.getClass().getName(), "saving...");
			List<SkillPanelEntry> list = instance.getRawPanelMap().entrySet().stream().map(in -> new SkillPanelEntry(in.getKey(),in.getValue()))
					.collect(Collectors.toList());
			if(!list.isEmpty()){
				UtilNBT.writeListToNBT(list, comp, "panel_per_user");
			}
			instance.getWorldStructureInfo().writeToNBT(comp);
		}

		@Override
		public void readNBT(NBTTagCompound nbt, Capability<IUnsagaWorld> capability, IUnsagaWorld instance,
				EnumFacing side) {
			//			UnsagaMod.logger.trace(this.getClass().getName(), "loading...");
			//			panelDataPerUser = Maps.newHashMap();
			if(nbt.hasKey("panel_per_user")){

				List<SkillPanelEntry> list  = UtilNBT.readListFromNBT(nbt, "panel_per_user", SkillPanelEntry.RESTORE_FUNC);
				UnsagaMod.logger.trace("loadします",list);
				for(SkillPanelEntry entry:list){
					instance.setPanels(entry.uuid, entry.list);
				}
			}
			WorldStructureInfo info = WorldStructureInfo.RESTORE_FUC.apply(nbt);
			instance.setWorldStructureInfo(info);
		}

	}


	public static void registerEvents(){
		PacketSyncCapability.registerSyncCapability(SYNC_ID, CAPA);
		ADAPTER.registerAttachEvent();

//		HSLibs.registerEvent(new SkillPanelSyncEvent());
	}

	public static void onEntityJoinWorld(EntityJoinWorldEvent e){
		//クライアントからリクエストを送って同期
		if(e.getEntity() instanceof EntityPlayer && WorldHelper.isClient(e.getWorld())){
			HSLib.getPacketDispatcher().sendToServer(PacketSyncCapability.createRequest(CAPA, ADAPTER.getCapability(e.getWorld())));
		}
	}
	public static class SkillPanelEntry implements INBTWritable{

		final UUID uuid;
		final NonNullList<ItemStack> list;
		public SkillPanelEntry(UUID uuid,NonNullList<ItemStack> panels){

			this.uuid = uuid;
			this.list = panels;
		}
		@Override
		public void writeToNBT(NBTTagCompound stream) {
			stream.setUniqueId("uuid", uuid);
			NBTTagList tagList = new NBTTagList();
			UtilNBT.writeItemStacksToNBTTag(tagList, list);
			//			this.list.writeToNBT(tagList);
			stream.setTag("items", tagList);
			UnsagaMod.logger.trace(this.getClass().getName(),uuid, this.list);

		}

		public static final RestoreFunc<SkillPanelEntry> RESTORE_FUNC = new RestoreFunc<SkillPanelEntry>(){

			@Override
			public SkillPanelEntry apply(NBTTagCompound input) {
				UUID uuid = input.getUniqueId("uuid");
				NBTTagList tagList =UtilNBT.getTagList(input, "items");
				NonNullList<ItemStack> list = UtilNBT.getItemStacksFromNBT(tagList, 7);
				//				UnsagaMod.logger.trace(this.getClass().getName(), tagList);
				UnsagaMod.logger.trace("loadpanel", uuid,list);
				return new SkillPanelEntry(uuid,list);
			}};
	}


}
