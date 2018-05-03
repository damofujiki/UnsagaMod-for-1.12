package mods.hinasch.unsaga.core.net.packet;

import java.util.UUID;

import io.netty.buffer.ByteBuf;
import mods.hinasch.lib.client.ClientHelper;
import mods.hinasch.lib.core.HSLib;
import mods.hinasch.lib.network.PacketUtil;
import mods.hinasch.lib.sync.AsyncUpdateEvent;
import mods.hinasch.lib.util.UtilNBT;
import mods.hinasch.unsaga.UnsagaMod;
import mods.hinasch.unsaga.core.world.UnsagaWorldCapability;
import mods.hinasch.unsaga.skillpanel.SkillPanelAPI;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketSyncSkillPanel implements IMessage{

	EntityPlayer ep;
	UUID uuid;
	NonNullList<ItemStack> panels;

	public static PacketSyncSkillPanel create(EntityPlayer ep){
		return new PacketSyncSkillPanel(ep);
	}
	public PacketSyncSkillPanel(){

	}
	public PacketSyncSkillPanel(EntityPlayer ep){
		if(HSLib.configHandler.isDebug()){
			this.uuid = UnsagaWorldCapability.UUID_DEBUG;
		}else{

			this.uuid = ep.getGameProfile().getId();
		}

		this.ep = ep;
	}

	public NonNullList<ItemStack> getPanelList(){
		return this.panels;
	}
	public UUID getUUID(){
		return this.uuid;
	}
	@Override
	public void fromBytes(ByteBuf buf) {
		int length = buf.readInt();
		ByteBuf child = buf.readBytes(length);
		NBTTagCompound nbt = PacketUtil.bytesToNBT(child);

		this.uuid = nbt.getUniqueId("uuid");
		if(nbt.hasKey("panels")){
			NBTTagList tagList = UtilNBT.getTagList(nbt, "panels");
			this.panels = UtilNBT.getItemStacksFromNBT(tagList, 7);
		}


	}

	@Override
	public void toBytes(ByteBuf buf) {
		NBTTagCompound comp = UtilNBT.compound();
		comp.setUniqueId("uuid", this.getUUID());
		NBTTagList tagList = UtilNBT.tagList();
		if(SkillPanelAPI.getPanelStacks(ep)!=null){
//			SkillPanelAPI.getPanelStacks(ep).writeToNBT(tagList);
			UtilNBT.writeItemStacksToNBTTag(tagList, SkillPanelAPI.getPanelStacks(ep));
			comp.setTag("panels", tagList);


		}
		byte[] bytes = PacketUtil.nbtToBytes(comp);
		buf.writeInt(bytes.length);
		buf.writeBytes(bytes);
	}

	public static class Handler implements IMessageHandler<PacketSyncSkillPanel,IMessage>{

		@Override
		public IMessage onMessage(PacketSyncSkillPanel message, MessageContext ctx) {
			if(ctx.side.isClient()){
				UnsagaMod.logger.trace(this.getClass().getName(), message.getUUID()+"を同期");
				if(message.getPanelList()!=null){
					AsyncUpdateEvent ev = new AsyncWait(ClientHelper.getPlayer(),"sync",message);
					HSLib.addAsyncEvent(ev.getSender(), ev);
//					HSLib.core().events.scannerEventPool.addEvent(new WaitUntilWorldPresent(ClientHelper.getPlayer(),"sync",message));
//					WorldSaveDataSkillPanel.get(ClientHelper.getWorld()).setPanels(message.getUUID(), message.getPanelList());
				}

			}
			return null;
		}

	}

	/** クライアントのワールドが準備できてなかたら待つ*/
	public static class AsyncWait extends AsyncUpdateEvent{

		boolean hasSend = false;
		final PacketSyncSkillPanel message;
		public AsyncWait(EntityLivingBase sender, String identifyName,PacketSyncSkillPanel message) {
			super(sender, identifyName);
			this.message = message;
		}

		@Override
		public boolean hasFinished() {
			// TODO 自動生成されたメソッド・スタブ
			return this.hasSend;
		}

		@Override
		public void loop() {

			UnsagaMod.logger.trace("sync", "sync");
			if(ClientHelper.getWorld()!=null && !this.hasSend){
				HSLib.logger.trace(this.getClass().getName(), "skillpanelを同期できました！");
				this.hasSend = true;
				UnsagaWorldCapability.ADAPTER.getCapability(ClientHelper.getWorld()).setPanels(message.getUUID(), message.getPanelList());
//				WorldSaveDataSkillPanel.get(ClientHelper.getWorld()).setPanels(message.getUUID(), message.getPanelList());

			}

		}

	}
}
