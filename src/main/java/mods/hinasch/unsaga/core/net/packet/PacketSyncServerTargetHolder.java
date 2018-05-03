package mods.hinasch.unsaga.core.net.packet;

import io.netty.buffer.ByteBuf;
import mods.hinasch.unsaga.UnsagaMod;
import mods.hinasch.unsaga.status.TargetHolderCapability;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketSyncServerTargetHolder implements IMessage{

	public enum Type{PLAYER(0),LIVING(1);

		final byte meta;


		public byte getMeta() {
			return meta;
		}


		private Type(int meta){

			this.meta = (byte)meta;
		}

		public static Type fromMeta(int meta){
			for(Type type:Type.values()){
				if(type.getMeta()==meta)return type;
			}

			return null;
		}

	};
	public Type getType() {
		return type;
	}

	public int getOwnerid() {
		return ownerid;
	}

	public int getTargetid() {
		return targetid;
	}

	Type type;
	int ownerid;
	int targetid;

	public PacketSyncServerTargetHolder(){

	}

	protected PacketSyncServerTargetHolder(EntityLivingBase target){

		type = Type.PLAYER;
		this.targetid = target.getEntityId();
	}

	protected PacketSyncServerTargetHolder(EntityLivingBase owner,EntityLivingBase target){
		type = Type.LIVING;
		this.ownerid = owner.getEntityId();
		this.targetid = target.getEntityId();
	}

	public static PacketSyncServerTargetHolder create(EntityLivingBase target){
		return new PacketSyncServerTargetHolder(target);
	}

	public static PacketSyncServerTargetHolder create(EntityLivingBase owner,EntityLivingBase target){
		return new PacketSyncServerTargetHolder(owner,target);
	}
	@Override
	public void fromBytes(ByteBuf buf) {
		this.type = Type.fromMeta((int)buf.readByte());
		switch(type){
		case LIVING:
			this.ownerid = buf.readInt();
			this.targetid = buf.readInt();
			break;
		case PLAYER:
			this.targetid = buf.readInt();
			break;
		default:
			break;

		}

	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeByte(this.type.getMeta());
		switch(type){
		case LIVING:
			buf.writeInt(ownerid);
			buf.writeInt(targetid);
			break;
		case PLAYER:
			buf.writeInt(targetid);
			break;
		default:
			break;

		}

	}

	public static class Handler implements IMessageHandler<PacketSyncServerTargetHolder,IMessage>{

		@Override
		public IMessage onMessage(PacketSyncServerTargetHolder message, MessageContext ctx) {
			if(ctx.side.isServer()){
//				TargetHolder holder = UnsagaModCore.instance().targetHolder;
				EntityPlayerMP playerMP = ctx.getServerHandler().player;
				World worldServer = playerMP.getEntityWorld();
				switch(message.getType()){
				case LIVING:
					if(playerMP.world!=null){
						Entity owner = worldServer.getEntityByID(message.getOwnerid());
						Entity target = worldServer.getEntityByID(message.getTargetid());
						if(owner instanceof EntityLivingBase && target instanceof EntityLivingBase){
							TargetHolderCapability.adapter.getCapability(owner).updateTarget((EntityLivingBase) target);
//							holder.updateTarget((EntityLivingBase)owner, (EntityLivingBase)target);
						}
					}
					break;
				case PLAYER:
					if(playerMP!=null && playerMP.world!=null){
						Entity entity = worldServer.getEntityByID(message.getTargetid());
						if(entity instanceof EntityLivingBase){
							UnsagaMod.logger.trace("Server Target Update", entity);
							TargetHolderCapability.adapter.getCapability(playerMP).updateTarget((EntityLivingBase) entity);
//							holder.updateTarget(playerMP, (EntityLivingBase) entity);
						}

					}

					break;
				default:
					break;

				}
			}
			return null;
		}

	}
}
