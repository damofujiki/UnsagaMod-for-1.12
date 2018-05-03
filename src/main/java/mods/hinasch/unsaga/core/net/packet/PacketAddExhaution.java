package mods.hinasch.unsaga.core.net.packet;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketAddExhaution implements IMessage{

	float level;

	public static PacketAddExhaution create(float level){
		return new PacketAddExhaution(level);
	}
	public PacketAddExhaution(){

	}

	public PacketAddExhaution(float level){
		this.level = level;
	}
	@Override
	public void fromBytes(ByteBuf buf) {
		// TODO 自動生成されたメソッド・スタブ
		this.level = buf.readFloat();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		// TODO 自動生成されたメソッド・スタブ
		buf.writeFloat(level);
	}

	public static class Handler implements IMessageHandler<PacketAddExhaution,IMessage>{

		@Override
		public IMessage onMessage(PacketAddExhaution message, MessageContext ctx) {
			if(ctx.side.isServer()){
				EntityPlayer ep = ctx.getServerHandler().player;
				ep.addExhaustion(message.level);
			}
			return null;
		}

	}
}
