package mods.hinasch.unsaga.status;

import java.util.Optional;

import mods.hinasch.lib.capability.CapabilityAdapterFactory.ICapabilityAdapterPlan;
import mods.hinasch.lib.capability.CapabilityAdapterFrame;
import mods.hinasch.lib.capability.ComponentCapabilityAdapters;
import mods.hinasch.lib.capability.StorageDummy;
import mods.hinasch.lib.network.PacketSyncCapability;
import mods.hinasch.unsaga.UnsagaMod;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class TargetHolderCapability {


	@CapabilityInject(ITargetHolder.class)
	public static Capability<ITargetHolder> CAPA;
	public static final String SYNC_ID = "unsagaTargetHolder";

	public static ICapabilityAdapterPlan<ITargetHolder> blueprint = new ICapabilityAdapterPlan(){

		@Override
		public Capability getCapability() {
			// TODO 自動生成されたメソッド・スタブ
			return CAPA;
		}

		@Override
		public Class getCapabilityClass() {
			// TODO 自動生成されたメソッド・スタブ
			return ITargetHolder.class;
		}

		@Override
		public Class getDefault() {
			// TODO 自動生成されたメソッド・スタブ
			return DefaultImpl.class;
		}

		@Override
		public IStorage getStorage() {
			// TODO 自動生成されたメソッド・スタブ
			return new StorageDummy();
		}

	};

	public static CapabilityAdapterFrame<ITargetHolder> adapterBase = UnsagaMod.capabilityAdapterFactory.create(blueprint);
	public static ComponentCapabilityAdapters.Entity<ITargetHolder> adapter = adapterBase.createChildEntity("unsagaTargetHolder");

	static{
		adapter.setPredicate(ev -> ev.getObject() instanceof EntityLivingBase);
//		adapter.setRequireSerialize(true);
	}

	public static class DefaultImpl implements ITargetHolder{

		Optional<EntityLivingBase> target = Optional.empty();

		@Override
		public void updateTarget(EntityLivingBase target) {
//			UnsagaMod.logger.trace("[target holder]"+owner.getName()+" targetted "+target.getName()+":"+WorldHelper.debug(owner.worldObj));
			this.target = Optional.of(target);
		}

		@Override
		public Optional<EntityLivingBase> getTarget() {
			// TODO 自動生成されたメソッド・スタブ
			return this.target;
		}

		@Override
		public double getTargetDistance(EntityLivingBase other) {
			// TODO 自動生成されたメソッド・スタブ
			return target.isPresent() ? target.get().getDistance(other) : 0.0D;
		}

		@Override
		public NBTTagCompound getSendingData() {
			// TODO 自動生成されたメソッド・スタブ
			return (NBTTagCompound) CAPA.getStorage().writeNBT(CAPA, this, null);
		}

		@Override
		public void catchSyncData(NBTTagCompound nbt) {
			// TODO 自動生成されたメソッド・スタブ
//			CAPA.getStorage().readNBT(CAPA, this, null, nbt);

		}

		@Override
		public void onPacket(PacketSyncCapability message, MessageContext ctx) {
			// TODO 自動生成されたメソッド・スタブ
			int entityid = message.getArgs().getInteger("entityid");
			UnsagaMod.logger.trace("called", "target");
			if(ctx.side.isServer()){
				World world = ctx.getServerHandler().player.getEntityWorld();
				Entity living;
				if(entityid==-2){
					living = ctx.getServerHandler().player;
				}else{
					living = world.getEntityByID(entityid);
				}
				if(living!=null && TargetHolderCapability.adapter.hasCapability(living)){
					int targetid = message.getArgs().getInteger("targetid");
					Entity target = world.getEntityByID(targetid);
					if(target instanceof EntityLivingBase){
						TargetHolderCapability.adapter.getCapability(living).updateTarget((EntityLivingBase) target);
					}

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

		PacketSyncCapability.registerSyncCapability(SYNC_ID, CAPA);
		adapter.registerAttachEvent();
	}
}
