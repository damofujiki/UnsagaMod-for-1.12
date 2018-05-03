package mods.hinasch.unsaga.core.net.packet;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketSprintTech implements IMessage{

	int sprint;
	public PacketSprintTech(){

	}

	public PacketSprintTech(int timer){
		this.sprint = timer;
	}
	@Override
	public void fromBytes(ByteBuf buf) {
		// TODO 自動生成されたメソッド・スタブ
		this.sprint = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		// TODO 自動生成されたメソッド・スタブ
		buf.writeInt(sprint);
	}

	public int getTimer(){
		return this.sprint;
	}


	public static class Handler implements IMessageHandler<PacketSprintTech,IMessage>{

		@Override
		public IMessage onMessage(PacketSprintTech message, MessageContext ctx) {
			if(ctx.side.isServer()){
				EntityPlayer ep = ctx.getServerHandler().player;
//				EventSprintTimer.
			}
			return null;
		}

	}
}
