package mods.hinasch.unsaga.core.entity.projectile.custom_arrow;

import mods.hinasch.lib.capability.CapabilityAdapterFactory.CapabilityAdapterPlanImpl;
import mods.hinasch.lib.capability.CapabilityAdapterFrame;
import mods.hinasch.lib.capability.CapabilityStorage;
import mods.hinasch.lib.capability.ComponentCapabilityAdapters;
import mods.hinasch.lib.client.ClientHelper;
import mods.hinasch.lib.core.HSLib;
import mods.hinasch.lib.network.PacketSyncCapability;
import mods.hinasch.lib.util.UtilNBT;
import mods.hinasch.unsaga.UnsagaMod;
import mods.hinasch.unsaga.lp.LPAttribute;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class CustomArrowCapability {


	@CapabilityInject(ICustomArrow.class)
	public static Capability<ICustomArrow> CAPA;
	public static final String SYNC_ID = "unsaga.custom_arrow";


	public static final CapabilityAdapterFrame<ICustomArrow> BUILDER = UnsagaMod.capabilityAdapterFactory.create(new CapabilityAdapterPlanImpl(
			()->CAPA,()->ICustomArrow.class,()->DefaultImpl.class,Storage::new));
	public static final ComponentCapabilityAdapters.Entity<ICustomArrow> ADAPTER = BUILDER.createChildEntity(SYNC_ID);

	static{

		ADAPTER.setPredicate(ev ->ev.getObject() instanceof EntityArrow);
		ADAPTER.setRequireSerialize(true);
	}

	public static class DefaultImpl implements ICustomArrow{

		LPAttribute lpStrength = new LPAttribute(0.3F,1);
		int ticksInGround = -1;
		SpecialArrowType type = SpecialArrowType.NONE;

		@Override
		public SpecialArrowType getArrowType() {
			// TODO 自動生成されたメソッド・スタブ
			return type;
		}

		@Override
		public void setArrowType(SpecialArrowType state) {
			// TODO 自動生成されたメソッド・スタブ
			this.type = state;
		}

		@Override
		public LPAttribute getArrowLPStrength() {
			// TODO 自動生成されたメソッド・スタブ
			return this.lpStrength;
		}

		@Override
		public void setArrowLPStrength(LPAttribute f) {
			// TODO 自動生成されたメソッド・スタブ
			this.lpStrength = f;
		}

		@Override
		public NBTTagCompound getSendingData() {
			// TODO 自動生成されたメソッド・スタブ
			return (NBTTagCompound) CAPA.getStorage().writeNBT(CAPA, this, null);
		}

		@Override
		public void catchSyncData(NBTTagCompound nbt) {
			// TODO 自動生成されたメソッド・スタブ
			CAPA.getStorage().readNBT(CAPA, this, null, nbt);
		}

		@Override
		public void onPacket(PacketSyncCapability message, MessageContext ctx) {
			// TODO 自動生成されたメソッド・スタブ
			if(ctx.side.isServer()){
				this.catchSyncData(message.getNbt());
				EntityPlayerMP epmp = ctx.getServerHandler().player;
				HSLib.getPacketDispatcher().sendTo(PacketSyncCapability.create(CAPA, this, message.getArgs()),epmp);
			}
			if(ctx.side.isClient()){
				int entityid = message.getArgs().getInteger("entityid");
				Entity entity = ClientHelper.getWorld().getEntityByID(entityid);
				UnsagaMod.logger.trace(this.getClass().getName(), entity,entityid);
				if(entity instanceof EntityArrow){
					UnsagaMod.logger.trace(this.getClass().getName(),message.getNbt());
					ADAPTER.getCapability(entity).catchSyncData(message.getNbt());
				}
			}
		}

		@Override
		public String getIdentifyName() {
			// TODO 自動生成されたメソッド・スタブ
			return SYNC_ID;
		}

	}


	public static void registerEvents(){
		ADAPTER.registerAttachEvent();
		PacketSyncCapability.registerSyncCapability(SYNC_ID, CAPA);
	}

	public static class Storage extends CapabilityStorage<ICustomArrow>{

		@Override
		public void writeNBT(NBTTagCompound comp, Capability<ICustomArrow> capability, ICustomArrow instance,
				EnumFacing side) {
			NBTTagCompound child = UtilNBT.compound();
			instance.getArrowLPStrength().writeToNBT(child);
			comp.setTag("lpstr",child);
			comp.setInteger("type", instance.getArrowType().getMeta());
//			comp.setInteger("tickInGround", instance.getTickInGround());
		}

		@Override
		public void readNBT(NBTTagCompound comp, Capability<ICustomArrow> capability, ICustomArrow instance,
				EnumFacing side) {
			// TODO 自動生成されたメソッド・スタブ
			if(comp.hasKey("lpstr")){
				NBTTagCompound child = (NBTTagCompound) comp.getTag("lpstr");
				instance.setArrowLPStrength(LPAttribute.RESTORE.apply(child));
			}

			if(comp.hasKey("type")){
				instance.setArrowType(SpecialArrowType.fromMeta(comp.getInteger("type")));
			}
//			instance.setTickInGround(comp.getInteger("tickInGround"));

		}


	}
}
