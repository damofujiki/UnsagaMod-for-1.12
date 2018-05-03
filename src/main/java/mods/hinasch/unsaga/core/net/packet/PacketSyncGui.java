//package mods.hinasch.unsaga.core.net.packet;
//
//import io.netty.buffer.ByteBuf;
//import mods.hinasch.lib.client.ClientHelper;
//import mods.hinasch.lib.container.ContainerBase;
//import mods.hinasch.lib.network.PacketUtil;
//import mods.hinasch.lib.util.UtilNBT;
//import mods.hinasch.unsaga.UnsagaMod;
//import mods.hinasch.unsaga.core.inventory.container.ContainerBartering;
//import mods.hinasch.unsaga.core.inventory.container.ContainerSmithUnsaga;
//import mods.hinasch.unsaga.util.UnsagaVillager.IUnsagaVillager.SmithProfessionality;
//import mods.hinasch.unsagamagic.inventory.container.ContainerTabletDeciphering;
//import net.minecraft.entity.player.EntityPlayer;
//import net.minecraft.item.ItemStack;
//import net.minecraft.nbt.NBTTagCompound;
//import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
//import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
//import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
//
//@Deprecated
//public class PacketSyncGui implements IMessage {
//
//	public static enum Type{
//		REPAIR(0),SYNC_DECIPHERING(1),BARTERING_PRICE(2),BARTERING_SECRET(3),BARTERING_TAGS(4)
//		,SMITH_PROFESSIONALITY(5);
//
//		private int meta;
//
//		private Type(int meta) {
//			this.meta = meta;
//		}
//
//		public int getMeta() {
//			return meta;
//		}
//
//		public static Type fromMeta(int meta){
//			for(Type type:Type.values()){
//				if(type.getMeta()==meta){
//					return type;
//				}
//			}
//			return null;
//		}
//	}
//	Type type;
//	NBTTagCompound comp;
//	public PacketSyncGui(){
//
//	}
//
//	public PacketSyncGui(Type type,NBTTagCompound nbt){
//		this.type = type;
//		this.comp = nbt;
//		if(this.comp==null){
//			this.comp = UtilNBT.compound();
//			this.comp.setBoolean("dummy", true);
//		}
//	}
//
//	public static PacketSyncGui create(Type type,NBTTagCompound additional){
//		return new PacketSyncGui(type,additional);
//	}
//
//	@Override
//	public void fromBytes(ByteBuf buf) {
//		this.type = Type.fromMeta(buf.readInt());
//		int length = buf.readInt();
//		ByteBuf bytes = buf.readBytes(length);
//		comp = PacketUtil.bytesToNBT(bytes);
//	}
//
//	@Override
//	public void toBytes(ByteBuf buf) {
//		buf.writeInt(type.getMeta());
//		byte[] bytes = PacketUtil.nbtToBytes(comp);
//		buf.writeInt(bytes.length);
//		buf.writeBytes(bytes);
//	}
//
//	public static class Handler implements IMessageHandler<PacketSyncGui,IMessage>{
//
//		@Override
//		public IMessage onMessage(PacketSyncGui message, MessageContext ctx) {
//			switch(message.type){
//			case REPAIR:
//				if(ctx.side.isServer()){
//					EntityPlayer ep = ctx.getServerHandler().player;
//					ep.getFoodStats().addExhaustion(1.4F);
//					ep.getHeldItemMainhand().setItemDamage(message.comp.getInteger("damage"));
//				}
//				break;
//			case SYNC_DECIPHERING:
//				if(ctx.side.isClient()){
//					ContainerBase container = (ContainerBase) ClientHelper.getPlayer().openContainer;
//
//					if(container instanceof ContainerTabletDeciphering){
//						ContainerTabletDeciphering containerDeciphering = (ContainerTabletDeciphering) container;
//						containerDeciphering.syncDecipheringMap(message.comp);
//					}
//				}
//				break;
//			case BARTERING_PRICE:
//				if(ctx.side.isClient()){
//
//					ContainerBase container = (ContainerBase) ClientHelper.getPlayer().openContainer;
//					if(container instanceof ContainerBartering){
//						int priceUp = message.comp.getInteger("priceUp");
//						int priceDown = message.comp.getInteger("priceDown");
////						((ContainerBartering) container).setPriceDown(priceDown);
////						((ContainerBartering) container).setPriceUp(priceUp);
//						UnsagaMod.logger.trace(this.getClass().getName(), "値切りをシンクしました",priceDown,priceUp);
//					}
//				}
//				break;
//			case BARTERING_SECRET:
//				if(ctx.side.isClient()){
//					ContainerBase container = (ContainerBase) ClientHelper.getPlayer().openContainer;
//					if(container instanceof ContainerBartering){
//						ContainerBartering bartering = (ContainerBartering)container;
//						ItemStack is = ItemStack.loadItemStackFromNBT(message.comp);
//						bartering.syncSecretSlot(is);
//					}
//				}
//
//				break;
//			case BARTERING_TAGS:
//				if(ctx.side.isClient()){
//					UnsagaMod.logger.trace(this.getClass().getName(), "商品札をつけました");
//					ContainerBase container = (ContainerBase) ClientHelper.getPlayer().openContainer;
//					if(container instanceof ContainerBartering){
//						ContainerBartering bartering = (ContainerBartering)container;
//						bartering.setMerchandiseTag();
//					}
//				}
//				if(ctx.side.isServer()){
//					return PacketSyncGui.create(Type.BARTERING_TAGS, null);
//				}
//				break;
//			case SMITH_PROFESSIONALITY:
//				if(ctx.side.isClient()){
//					ContainerBase container = (ContainerBase) ClientHelper.getPlayer().openContainer;
//					if(container instanceof ContainerSmithUnsaga){
//						ContainerSmithUnsaga bartering = (ContainerSmithUnsaga)container;
//						SmithProfessionality type = SmithProfessionality.fromMeta(message.comp.getInteger("type"));
////						bartering.setSmithType(type);
//						UnsagaMod.logger.trace(this.getClass().getName(), "called");
//					}
//				}
//				if(ctx.side.isServer()){
//					EntityPlayer ep = ctx.getServerHandler().player;
//					ContainerBase container = (ContainerBase) ep.openContainer;
//					if(container instanceof ContainerSmithUnsaga){
//						ContainerSmithUnsaga bartering = (ContainerSmithUnsaga)container;
//						NBTTagCompound nbt = UtilNBT.compound();
////						nbt.setInteger("type", bartering.getSmithType().getMeta());
//						return PacketSyncGui.create(Type.SMITH_PROFESSIONALITY, nbt);
//					}
//
//				}
//				break;
//			default:
//				break;
//
//			}
//			return null;
//		}
//
//	}
//}
