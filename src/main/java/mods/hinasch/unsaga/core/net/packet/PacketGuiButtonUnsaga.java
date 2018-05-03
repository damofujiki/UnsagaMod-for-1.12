package mods.hinasch.unsaga.core.net.packet;

import mods.hinasch.lib.container.ContainerBase;
import mods.hinasch.lib.network.PacketGuiButtonBaseNew;
import mods.hinasch.lib.network.PacketGuiButtonHandlerBase;
import mods.hinasch.unsaga.init.UnsagaGui;
import net.minecraft.inventory.Container;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;


public class PacketGuiButtonUnsaga extends PacketGuiButtonBaseNew{


	public PacketGuiButtonUnsaga(){
		super();
	}


	public PacketGuiButtonUnsaga(UnsagaGui.Type guiID, int buttonID) {
		super(guiID,buttonID,null);
	}

	public PacketGuiButtonUnsaga(UnsagaGui.Type guiID,int buttonID,NBTTagCompound bytearg){
		super(guiID,buttonID,bytearg);
	}

	public static PacketGuiButtonUnsaga create(UnsagaGui.Type guiID,int buttonID,NBTTagCompound bytearg){
		return new PacketGuiButtonUnsaga(guiID,buttonID,bytearg);
	}

	@Deprecated
	@Override
	public ContainerBase getContainer(Container openContainer,int guiID){
		return (ContainerBase) openContainer;
//		switch(UnsagaGui.Type.fromMeta(guiID)){
//		case EQUIPMENT:
//			return  (ContainerEquipment)openContainer;
//		default:
//			break;
//
//		}
//		switch(guiID){
//		case guiNumber.SMITH:
//
//			return (ContainerSmithUnsaga)openContainer;
//		case guiNumber.BARTERING:
//			return (ContainerBartering)openContainer;
//		case guiNumber.SKILLPANEL:
//			return (ContainerSkillPanel)openContainer;
//		case guiNumber.EQUIP:
//			return (ContainerEquipment)openContainer;
//		case guiNumber.CHEST:
//			return (ContainerChestUnsaga)openContainer;
//		case guiNumber.BLENDER:
//			return (ContainerBlender)openContainer;
//		case guiNumber.CARRIER:
//			return (ContainerCarrier)openContainer;
//		case guiNumber.TABLET:
//			return (ContainerTabletDeciphering)openContainer;
//		}
//		return null;
	}


	public static class Handler extends PacketGuiButtonHandlerBase implements IMessageHandler<PacketGuiButtonUnsaga,IMessage> {

		@Override
		public IMessage onMessage(PacketGuiButtonUnsaga message, MessageContext ctx) {
			// TODO 自動生成されたメソッド・スタブ
			return this.onPacketData(message, ctx);
		}

	}
}
