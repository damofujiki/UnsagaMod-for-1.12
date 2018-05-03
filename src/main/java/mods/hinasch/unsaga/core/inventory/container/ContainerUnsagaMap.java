package mods.hinasch.unsaga.core.inventory.container;

import java.util.List;

import com.google.common.collect.Lists;

import mods.hinasch.lib.client.GuiMapBase;
import mods.hinasch.lib.container.ContainerBase;
import mods.hinasch.lib.core.HSLib;
import mods.hinasch.lib.network.PacketGuiButtonBaseNew;
import mods.hinasch.lib.network.PacketSyncCapability;
import mods.hinasch.lib.util.UtilNBT;
import mods.hinasch.lib.world.WorldHelper;
import mods.hinasch.unsaga.UnsagaMod;
import mods.hinasch.unsaga.chest.ChunkChestInfo;
import mods.hinasch.unsaga.core.client.gui.GuiUnsagaMap;
import mods.hinasch.unsaga.core.world.chunk.IUnsagaChunk;
import mods.hinasch.unsaga.core.world.chunk.UnsagaChunkCapability;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;

public class ContainerUnsagaMap extends ContainerBase{

	public ContainerUnsagaMap(EntityPlayer ep) {
		super(ep, new InventoryBasic("",false,1));

		if(WorldHelper.isServer(ep.getEntityWorld())){
			//周辺チャンク情報をクライアントへキャッシュ
			int maxPos = GuiMapBase.MAP_MAX_SIZE / GuiUnsagaMap.RESOLUTION_MAP;
			int chunkMax = maxPos / 16 + 1;
			BlockPos mapCorner = GuiUnsagaMap.calcMapCornerPos(ep.getPosition(),maxPos);
			List<ChunkChestInfo> chestList = Lists.newArrayList();
			for(int i=0;i<chunkMax;i++){
				for(int j=0;j<chunkMax;j++){
					BlockPos chunkPos = mapCorner.add(i*16,0,j*16);
					Chunk chunk = ep.world.getChunkFromBlockCoords(chunkPos);
					IUnsagaChunk instance = UnsagaChunkCapability.ADAPTER.getCapability(chunk);
					chestList.add(instance.getChunkChestInfo());
				}

			}
			NBTTagCompound args = UtilNBT.compound();
			UtilNBT.writeListToNBT(chestList, args, "chests");
			HSLib.getPacketDispatcher().sendTo(PacketSyncCapability.create(UnsagaChunkCapability.CAPA, UnsagaChunkCapability.CAPA.getDefaultInstance(), args), (EntityPlayerMP) ep);
//			if(!ChestPosCache.instance().isEmpty()){
//				UnsagaMod.logger.trace(this.getClass().getName(), "called");
//				UnsagaMod.packetDispatcher.sendTo(PacketSyncFieldChest.create(), (EntityPlayerMP) ep);
//			}

		}
	}

	@Override
	public boolean isShowPlayerInv(){
		return false;
	}

	@Override
	public PacketGuiButtonBaseNew getPacketGuiButton(int guiID, int buttonID, NBTTagCompound args) {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	@Override
	public SimpleNetworkWrapper getPacketPipeline() {
		// TODO 自動生成されたメソッド・スタブ
		return UnsagaMod.packetDispatcher;
	}

	@Override
	public int getGuiID() {
		// TODO 自動生成されたメソッド・スタブ
		return 0;
	}

	@Override
	public void onPacketData() {
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	public boolean canInteractWith(EntityPlayer var1) {
		// TODO 自動生成されたメソッド・スタブ
		return true;
	}
}
