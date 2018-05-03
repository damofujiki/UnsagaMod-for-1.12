package mods.hinasch.unsaga.core.net.packet;

import java.util.List;

import io.netty.buffer.ByteBuf;
import mods.hinasch.lib.network.PacketUtil;
import mods.hinasch.lib.util.UtilNBT;
import mods.hinasch.unsaga.UnsagaMod;
import mods.hinasch.unsaga.chest.old.FieldChestInfo;
import mods.hinasch.unsaga.chest.old.IFieldChest;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketSyncFieldChest implements IMessage{

	NBTTagCompound nbt;

	public PacketSyncFieldChest(){
		this.nbt = UtilNBT.compound();
	}

	public static PacketSyncFieldChest create(){
		return new PacketSyncFieldChest();
	}
	@Override
	public void fromBytes(ByteBuf buf) {
		// TODO 自動生成されたメソッド・スタブ
		int len = buf.readInt();
		ByteBuf bytes = buf.readBytes(len);
		this.nbt = PacketUtil.bytesToNBT(bytes);
	}

	@Override
	public void toBytes(ByteBuf buf) {

//		Collection<IFieldChest> list = ChestPosCache.instance().values();
//
//		if(!list.isEmpty()){
//
//			UtilNBT.writeListToNBT(list, this.nbt, "chests");
//			UnsagaMod.logger.trace(this.getClass().getName(), this.nbt);
//			byte[] bytes = PacketUtil.nbtToBytes(this.nbt);
//			int len = bytes.length;
//			buf.writeInt(len);
//			buf.writeBytes(bytes);
//		}


	}

	public NBTTagCompound getNBT(){
		return this.nbt;
	}

	public static class Handler implements IMessageHandler<PacketSyncFieldChest,IMessage>{

		@Override
		public IMessage onMessage(PacketSyncFieldChest message, MessageContext ctx) {
			if(ctx.side.isClient()){

				List<IFieldChest> list = UtilNBT.readListFromNBT(message.getNBT(), "chests", FieldChestInfo.FUNC);
				UnsagaMod.logger.trace(this.getClass().getName(),list);
				list.stream().forEach(in ->{
//					ChestPosCache.instance().invalidateAll();
//					ChestPosCache.instance().put(in.getPos(), in);
					UnsagaMod.logger.trace(this.getClass().getName(), "チェストの場所同期",in.getPos());
				});
			}
			return null;
		}

	}
}
