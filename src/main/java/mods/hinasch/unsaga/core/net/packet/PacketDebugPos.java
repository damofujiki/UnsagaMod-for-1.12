package mods.hinasch.unsaga.core.net.packet;

import io.netty.buffer.ByteBuf;
import mods.hinasch.lib.client.ClientHelper;
import mods.hinasch.lib.util.ChatHandler;
import mods.hinasch.lib.world.XYZPos;
import mods.hinasch.unsaga.UnsagaMod;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketDebugPos implements IMessage{

	XYZPos pos;
	int bank;
	public PacketDebugPos(){

	}

	public PacketDebugPos(int bank,XYZPos pos){
		this.pos = new XYZPos(pos);
		this.bank = bank;
	}
	@Override
	public void fromBytes(ByteBuf buf) {
		this.pos = XYZPos.readFromBuffer(buf);
		this.bank = buf.readInt();
	}

	public XYZPos getPos(){
		return this.pos;
	}

	public int getBank(){
		return this.bank;
	}
	@Override
	public void toBytes(ByteBuf buf) {
		// TODO 自動生成されたメソッド・スタブ
		pos.writeToBuffer(buf);
		buf.writeInt(bank);
	}

	public static class Handler implements IMessageHandler<PacketDebugPos,IMessage>{

		@Override
		public IMessage onMessage(PacketDebugPos message, MessageContext ctx) {
			if(ctx.side.isClient()){

				UnsagaMod.proxy.setDebugPos(message.getBank(),message.getPos());
				ChatHandler.sendChatToPlayer(ClientHelper.getPlayer(), String.format("set BANK %d to %s", message.getBank(),message.getPos().toString()));
			}
			return null;
		}
	}
}
