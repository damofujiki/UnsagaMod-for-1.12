package mods.hinasch.unsaga.lp;

import mods.hinasch.lib.capability.CapabilityAdapterFactory.CapabilityAdapterPlanImpl;
import mods.hinasch.lib.capability.CapabilityAdapterFrame;
import mods.hinasch.lib.capability.CapabilityStorage;
import mods.hinasch.lib.capability.ComponentCapabilityAdapters;
import mods.hinasch.lib.capability.ISyncCapability;
import mods.hinasch.lib.client.ClientHelper;
import mods.hinasch.lib.core.HSLib;
import mods.hinasch.lib.iface.IRequireInitializing;
import mods.hinasch.lib.network.PacketSyncCapability;
import mods.hinasch.lib.util.HSLibs;
import mods.hinasch.lib.util.UtilNBT;
import mods.hinasch.lib.world.WorldHelper;
import mods.hinasch.lib.world.XYZPos;
import mods.hinasch.unsaga.UnsagaMod;
import mods.hinasch.unsaga.core.event.old.HealTimerCalculator;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingHealEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class LifePoint {

	@CapabilityInject(ILifePoint.class)
	public static Capability<ILifePoint> CAPA;

	public static final String SYNC_ID = "lpsystem";

	public static final int RENDER_DAMAGE_TIME = 30;

	public static CapabilityAdapterFrame<ILifePoint> BUILDER = UnsagaMod.capabilityAdapterFactory.create(new CapabilityAdapterPlanImpl(
			()->CAPA,()->ILifePoint.class,()->DefaultImpl.class,Storage::new));
	public static ComponentCapabilityAdapters.Entity<ILifePoint> adapter = BUILDER.createChildEntity(SYNC_ID);

	static{
		adapter.setPredicate(ev -> ev.getObject() instanceof EntityLivingBase);
		adapter.setRequireSerialize(true);
	}
	public static interface ILifePoint extends IRequireInitializing,ISyncCapability{



		public void init(EntityLivingBase living);
		public int getHealTimer();
		public void setHealTimer(int timer);
		public float getLifeSaturation();
		public void setLifeSaturation(float par1);
		public int getLifePoint();
		public void setLifePoint(int par1);
		public void addLifePoint(int par1);
		public int getMaxLifePoint();
		public void setMaxLifePoint(int par1);
		public void restoreLifePoint();
		public int getHurtInterval();
		public void setHurtInterval(int par1);
		public void decrLifePoint(int par1);
//		@Deprecated
//		public void onHealed(EntityLivingBase living,float amount);
//		public void onUpdate(EntityLivingBase living);
		@SideOnly(Side.CLIENT)
		public int getRenderTick();
		@SideOnly(Side.CLIENT)
		public void setRenderTick(int par1);
		@SideOnly(Side.CLIENT)
		public void decrRenderTick(int par1);
		@SideOnly(Side.CLIENT)
		public void resetRenderTick();
		@SideOnly(Side.CLIENT)
		public XYZPos getRenderTextPos();
		@SideOnly(Side.CLIENT)
		public void setRenderTextPos(XYZPos pos);
		/**
		 * ダメージのレンダーを始める
		 * @param par1
		 */
		@SideOnly(Side.CLIENT)
		public void markDirtyRenderDamage(boolean par1);
		@SideOnly(Side.CLIENT)
		public boolean isDirty();
		@SideOnly(Side.CLIENT)
		public void setRenderDamage(int par1);
		@SideOnly(Side.CLIENT)
		public int getRenderDamage();
		public void incrHealTimer();
	}



	public static class DefaultImpl implements ILifePoint{


		protected boolean isInitialized = false;
		protected int MaxLifePoint = 5;
		protected int healTimer = 0;
		protected float lifeSaturation = 0;
		protected int lifeRestoreTimer = 0;
		protected int LifePoint = 5;

		protected int hurtInterval = 5;
		@SideOnly(Side.CLIENT)
		protected boolean marked = false;
		@SideOnly(Side.CLIENT)
		protected int damage;
		@SideOnly(Side.CLIENT)
		protected int renderTick;
		@SideOnly(Side.CLIENT)
		protected XYZPos renderTextPos;
		@Override
		public void addLifePoint(int par1) {
			this.LifePoint += par1;
			if(this.LifePoint<0){
				this.LifePoint = 0;
			}


			if(this.LifePoint>this.MaxLifePoint){
				this.LifePoint = this.MaxLifePoint;
			}

			UnsagaMod.logger.trace(par1+"LP:"+this.LifePoint);
		}
		@Override
		public void decrLifePoint(int par1) {
			// TODO 自動生成されたメソッド・スタブ

			this.addLifePoint(-par1);
		}

		@Override
		public void decrRenderTick(int par1) {
			// TODO 自動生成されたメソッド・スタブ
			this.renderTick -= par1;
		}

		@Override
		public int getHurtInterval() {
			// TODO 自動生成されたメソッド・スタブ
			return this.hurtInterval;
		}

		@Override
		public int getLifePoint() {
			// TODO 自動生成されたメソッド・スタブ
			return this.LifePoint;
		}

		@Override
		public int getMaxLifePoint() {
			// TODO 自動生成されたメソッド・スタブ
			return this.MaxLifePoint;
		}


		@SideOnly(Side.CLIENT)
		@Override
		public int getRenderDamage() {
			// TODO 自動生成されたメソッド・スタブ
			return this.damage;
		}

		@SideOnly(Side.CLIENT)
		@Override
		public XYZPos getRenderTextPos() {
			return renderTextPos;
		}

		@SideOnly(Side.CLIENT)
		@Override
		public int getRenderTick() {
			return renderTick;
		}



		public void init(EntityLivingBase living){

			if(!this.isInitialized){
				this.setMaxLifePoint(LPInitializeHelper.getLPFromEntity(living));
				this.restoreLifePoint();
				this.isInitialized = true;
			}


			//			if(living instanceof EntityPlayer){
			//				UnsagaMod.packetDispatcher.sendTo(PacketLPNew.create(living, this.getMaxLifePoint()), (EntityPlayerMP) living);
			//				if(living instanceof EntityTameable){
			//					EntityTameable tame = (EntityTameable) living;
			//					if(tame.getOwner() instanceof EntityPlayer){
			//						UnsagaMod.packetDispatcher.sendTo(PacketLPNew.create(living, this.getMaxLifePoint()), (EntityPlayerMP) tame.getOwner());
			//					}
			//				}
			//			}
		}

		@SideOnly(Side.CLIENT)
		@Override
		public boolean isDirty() {
			// TODO 自動生成されたメソッド・スタブ
			return this.marked;
		}


		@SideOnly(Side.CLIENT)
		@Override
		public void markDirtyRenderDamage(boolean par1) {
			// TODO 自動生成されたメソッド・スタブ

			this.renderTick = RENDER_DAMAGE_TIME;
			this.marked = par1;
		}

		@Override
		public void resetRenderTick() {
			// TODO 自動生成されたメソッド・スタブ
			this.renderTick = 0;
			this.markDirtyRenderDamage(false);
			this.renderTextPos = null;
		}

		@Override
		public void restoreLifePoint() {
			// TODO 自動生成されたメソッド・スタブ

			this.setLifePoint(getMaxLifePoint());
		}

		@Override
		public void setHurtInterval(int par1) {
			// TODO 自動生成されたメソッド・スタブ

			this.hurtInterval = par1;
		}


		@Override
		public void setLifePoint(int lp) {
			// TODO 自動生成されたメソッド・スタブ

			this.LifePoint = lp;
		}

		@Override
		public void setMaxLifePoint(int par1) {
			// TODO 自動生成されたメソッド・スタブ

			this.MaxLifePoint = par1;
		}

		@SideOnly(Side.CLIENT)
		@Override
		public void setRenderDamage(int par1) {
			// TODO 自動生成されたメソッド・スタブ

			this.damage = par1;
		}

		@SideOnly(Side.CLIENT)
		@Override
		public void setRenderTextPos(XYZPos renderTextPos) {
			this.renderTextPos = renderTextPos;
		}

		@SideOnly(Side.CLIENT)
		@Override
		public void setRenderTick(int renderTick) {
			this.renderTick = renderTick;
		}
		@Override
		public boolean hasInitialized() {
			// TODO 自動生成されたメソッド・スタブ
			return this.isInitialized;
		}
		@Override
		public void setInitialized(boolean par1) {
			// TODO 自動生成されたメソッド・スタブ
			this.isInitialized = par1;
		}

		@Override
		public void incrHealTimer(){
			this.healTimer ++;
		}

//		public void onHealed(EntityLivingBase liv,float amount){
//			//			UnsagaMod.logger.trace(this.getClass().getName(), "max",this.getLifePoint(),this.getMaxLifePoint());
//			if(liv.getHealth()>=liv.getMaxHealth() && this.getLifePoint()<this.getMaxLifePoint()){
//				float satu = this.getLifeSaturation();
//				this.setLifeSaturation((float) (satu+amount));
//				//                UnsagaMod.logger.trace(this.getClass().getName(), "saturation",this.getLifeSaturation());
//				if(this.getLifeSaturation()>=20.0F){
//					this.addLifePoint(1);
//					this.setLifeSaturation(0);
//					NBTTagCompound nbt = UtilNBT.compound();
//					nbt.setInteger("entityid", liv.getEntityId());
//					HSLib.getPacketDispatcher().sendToAll(PacketSyncCapability.create(CAPA, this,nbt));
//				}
//			}
//		}
//		@Override
//		public void onUpdate(EntityLivingBase liv){
//			if(WorldHelper.isClient(liv.getEntityWorld())){
//				return;
//			}
//			if(liv instanceof EntityPlayer){
//				this.playerNaturalHeal((EntityPlayer) liv);
//			}else{
//				if(UnsagaMod.configs.isEnabledLifePointSystem()){
//					this.incrHealTimer();
//					int healThreshold = HealTimerCalculator.calcHealTimer(liv);
//					if(this.getHealTimer()>=healThreshold){
//						liv.heal(1.0F);
//						this.setHealTimer(0);
//					}
//				}
//
//			}
//		}

		private void playerNaturalHeal(EntityPlayer ep){
			int healThreshold = HealTimerCalculator.calcHealTimer(ep);
			if(ep.getFoodStats().getSaturationLevel()>0.0F && ep.getFoodStats().getFoodLevel()>=20 && ep.shouldHeal()){
				this.incrHealTimer();
				if(this.getHealTimer() >=MathHelper.clamp(healThreshold-50, 10, 65535)){
					float f = Math.min(ep.getFoodStats().getSaturationLevel(), 4.0F);
					ep.heal(f / 4.0F);
					ep.getFoodStats().addExhaustion(f);
					this.setHealTimer(0);
					UnsagaMod.logger.trace(this.getClass().getName(), "healed");
				}
			}else if(ep.getFoodStats().getFoodLevel()>=18 && ep.shouldHeal()){
				this.incrHealTimer();
				UnsagaMod.logger.trace(this.getClass().getName(), "healed",this.getHealTimer());

				if(this.getHealTimer()>=healThreshold){
					ep.heal(1.0F);
					ep.getFoodStats().addExhaustion(4.0F);
					this.setHealTimer(0);

				}
			}
		}
		//		public void onupd(EntityLivingBase living) {
		//			if(living.getHealth()>=living.getMaxHealth() && this.getLifePoint()<this.getMaxLifePoint()){
		//				this.lifeRestoreTimer ++;
		////				UnsagaMod.logger.trace("lifeTimer",this.lifeRestoreTimer);
		//				PotionEffect effect = living.getActivePotionEffect(UnsagaPotions.instance().lifeBoost);
		//				int threshold = effect==null ? 1000 : 1000 - (100*effect.getAmplifier());
		//				if(this.lifeRestoreTimer>threshold){
		//					this.lifeRestoreTimer = 0;
		//					this.addLifePoint(1);
		//					if(living instanceof EntityPlayer){
		//						((EntityPlayer) living).getFoodStats().addExhaustion(1.0F);
		//					}
		//				}
		//			}
		//
		//		}
		@Override
		public float getLifeSaturation() {
			// TODO 自動生成されたメソッド・スタブ
			return this.lifeSaturation;
		}
		@Override
		public void setLifeSaturation(float par1) {
			// TODO 自動生成されたメソッド・スタブ
			this.lifeSaturation = par1;
		}
		@Override
		public NBTTagCompound getSendingData() {
			// TODO 自動生成されたメソッド・スタブ
			return (NBTTagCompound) CAPA.writeNBT(this, null);
		}
		@Override
		public void catchSyncData(NBTTagCompound nbt) {
			CAPA.readNBT(this, null, nbt);
		}
		@Override
		public void onPacket(PacketSyncCapability message, MessageContext ctx) {


			if(ctx.side==Side.SERVER){
				int entityid = message.getArgs().getInteger("entityid");

				Entity entity = ctx.getServerHandler().player.getEntityWorld().getEntityByID(entityid);
				if(message.getArgs().hasKey("isPlayer")){
					entity = ctx.getServerHandler().player;
				}
				if(entity!=null){
//					UnsagaMod.logger.trace("リクエストを受け取りました", adapter.getCapability(entity).getLifePoint(),entity.getName());
					HSLib.getPacketDispatcher().sendTo(PacketSyncCapability.create(CAPA, adapter.getCapability(entity), message.getArgs()), ctx.getServerHandler().player);
				}

			}
			if(ctx.side==Side.CLIENT){
				boolean isRenderLP = message.getArgs().hasKey("isRenderLP") ? message.getArgs().getBoolean("isRenderLP") : false;
				int id = message.getArgs().getInteger("entityid");
				Entity entity = ClientHelper.getWorld().getEntityByID(id);
				if(message.getArgs().hasKey("isPlayer")){
					entity = ClientHelper.getPlayer();
				}



				if(entity!=null){
					adapter.getCapability(entity).catchSyncData(message.getNbt());

//					UnsagaMod.logger.trace(this.getClass().getName(), entity.getName(),adapter.getCapability(entity).getLifePoint());
					if(isRenderLP){
						int damage = message.getArgs().getInteger("damage");
						adapter.getCapability(entity).setRenderDamage(damage);
						adapter.getCapability(entity).markDirtyRenderDamage(true);
					}
				}


				//				if(ClientHelper.isWorldPresent()){
				//					Entity entity = ClientHelper.getWorld().getEntityByID(id);
				//					if(entity instanceof EntityLivingBase){
				//						adapter.getCapability(entity).catchSyncData(message.getNbt());
				//					}
				//				}else{
				//					new Thread(new AsyncEvent(id,message.getNbt())).start();
				//				}

				//				World clientWorld = ClientHelper.getWorld();
				//				Entity entity = clientWorld.getEntityByID(id);
				//				UnsagaMod.logger.trace(this.getClass().getName(), entity);
				//				if(entity instanceof EntityLivingBase){
				//					adapter.getCapability((EntityLivingBase)entity).catchSyncData(message.getNbt());
				//				}
			}




		}
		@Override
		public String getIdentifyName() {
			// TODO 自動生成されたメソッド・スタブ
			return SYNC_ID;
		}
		@Override
		public int getHealTimer() {
			// TODO 自動生成されたメソッド・スタブ
			return this.healTimer;
		}
		@Override
		public void setHealTimer(int timer) {
			// TODO 自動生成されたメソッド・スタブ
			this.healTimer = timer;
		}

	}

	public static class Storage extends CapabilityStorage<ILifePoint>{



		@Override
		public void writeNBT(NBTTagCompound nbt, Capability<ILifePoint> capability, ILifePoint instance, EnumFacing side) {
			nbt.setInteger("LP", instance.getLifePoint());
			nbt.setInteger("maxLP", instance.getMaxLifePoint());
			nbt.setBoolean("initialized", instance.hasInitialized());
			nbt.setFloat("saturation", instance.getLifeSaturation());
		}

		@Override
		public void readNBT(NBTTagCompound comp, Capability<ILifePoint> capability, ILifePoint instance, EnumFacing side) {
			instance.setMaxLifePoint(comp.getInteger("maxLP"));
			//			UnsagaMod.logger.trace("lp read", comp.getInteger("LP"));
			instance.setLifePoint(comp.getInteger("LP"));
			instance.setInitialized(comp.getBoolean("initialized"));
			instance.setLifeSaturation(comp.getFloat("saturation"));
		}

	}

	public static void onUpdate(EntityLivingBase liv){
		if(LifePoint.adapter.hasCapability(liv)){
			ILifePoint instanceLP = LifePoint.adapter.getCapability(liv);

			if(WorldHelper.isClient(liv.getEntityWorld())){
				return;
			}
			if(liv instanceof EntityPlayer){
				playerNaturalHeal((EntityPlayer) liv, instanceLP);
			}else{
				//				if(UnsagaMod.configs.isEnabledLifePointSystem()){
				instanceLP.incrHealTimer();
				int healThreshold = HealTimerCalculator.calcHealTimer(liv);
				if(instanceLP.getHealTimer()>=healThreshold){
					liv.heal(1.0F);
					instanceLP.setHealTimer(0);
				}
				//				}

			}
		}

	}

	private static void playerNaturalHeal(EntityPlayer ep,ILifePoint instanceLP){
		int healThreshold = HealTimerCalculator.calcHealTimer(ep);
		if(ep.getFoodStats().getSaturationLevel()>0.0F && ep.getFoodStats().getFoodLevel()>=20 && ep.shouldHeal()){

			instanceLP.incrHealTimer();
			if(instanceLP.getHealTimer() >=MathHelper.clamp(healThreshold-50, 10, 65535)){
				float f = Math.min(ep.getFoodStats().getSaturationLevel(), 4.0F);
				ep.heal(f / 4.0F);
				ep.getFoodStats().addExhaustion(f);
				instanceLP.setHealTimer(0);
				UnsagaMod.logger.trace(LifePoint.class.getClass().getName(), "healed");
			}
		}else if(ep.getFoodStats().getFoodLevel()>=18 && ep.shouldHeal()){
			instanceLP.incrHealTimer();
			UnsagaMod.logger.trace(LifePoint.class.getClass().getName(), "healed",instanceLP.getHealTimer());

			if(instanceLP.getHealTimer()>=healThreshold){
				ep.heal(1.0F);
				ep.getFoodStats().addExhaustion(4.0F);
				instanceLP.setHealTimer(0);

			}
		}
	}
	public static class LPHealEvent{

		@SubscribeEvent
		public void onHeal(LivingHealEvent e){
			if(LifePoint.adapter.hasCapability(e.getEntityLiving())){
				onHealed(e.getEntityLiving(),e.getAmount());
			}

		}

		public static void onHealed(EntityLivingBase liv,float amount){
			if(liv.getHealth()>=liv.getMaxHealth()){
				ILifePoint capability = LifePoint.adapter.getCapability(liv);
				float satu = capability.getLifeSaturation();
				capability.setLifeSaturation((float) (satu+amount));
				//                UnsagaMod.logger.trace(this.getClass().getName(), "saturation",this.getLifeSaturation());
				if(capability.getLifeSaturation()>=20.0F){
					capability.addLifePoint(1);
					capability.setLifeSaturation(0);
					NBTTagCompound nbt = UtilNBT.compound();
					nbt.setInteger("entityid", liv.getEntityId());
					HSLib.getPacketDispatcher().sendToAll(PacketSyncCapability.create(CAPA, capability,nbt));
				}
			}
		}
	}

	public static void onEntityJoin(EntityJoinWorldEvent in){
		if(WorldHelper.isClient(in.getWorld()) && in.getEntity() instanceof EntityLivingBase){
			EntityLivingBase living = (EntityLivingBase) in.getEntity();
			NBTTagCompound args = UtilNBT.compound();
			args.setInteger("entityid", living.getEntityId());
			if(living instanceof EntityPlayer){
				args.setBoolean("isPlayer",true);
			}

//			UnsagaMod.logger.trace("packet", living.getEntityId(),living.getName());
			HSLib.getPacketDispatcher().sendToServer(PacketSyncCapability.createRequest(CAPA, adapter.getCapability(living),args));
		}
	}
	public static void registerEvents(){
		HSLibs.registerEvent(new LPHealEvent());
		PacketSyncCapability.registerSyncCapability(SYNC_ID, CAPA);
		//		EventEntityJoinWorld.addEvent(in ->{
		//			if(WorldHelper.isClient(in.getWorld()) && in.getEntity() instanceof EntityLivingBase){
		//				EntityLivingBase living = (EntityLivingBase) in.getEntity();
		//				NBTTagCompound args = UtilNBT.compound();
		//				args.setInteger("entityid", living.getEntityId());
		//				if(living instanceof EntityPlayer){
		//					args.setBoolean("isPlayer",true);
		//				}
		//
		//				UnsagaMod.logger.trace("packet", living.getEntityId(),living.getName());
		//				HSLib.getPacketDispatcher().sendToServer(PacketSyncCapability.createRequest(CAPA, adapter.getCapability(living),args));
		//			}
		//		});
//		HSLib.events.livingUpdate.getEvents().add(new ILivingUpdateEvent(){
//
//			@Override
//			public void update(LivingUpdateEvent e) {
//				if(LifePoint.adapter.hasCapability(e.getEntityLiving())){
//					LifePoint.adapter.getCapability(e.getEntityLiving()).onUpdate(e.getEntityLiving());
//				}
//
//			}
//
//			@Override
//			public String getName() {
//				// TODO 自動生成されたメソッド・スタブ
//				return "LP SAturation";
//			}}
//				);
		adapter.registerAttachEvent((inst,capa,facing,ev)->{
			if(!inst.hasInitialized() && ev.getObject() instanceof EntityLivingBase){
				inst.init((EntityLivingBase) ev.getObject());
			}
		});
	}

	//	/** クライアントのワールドが準備できてなかたら待つ*/
	//	public static class AsyncEvent implements Runnable{
	//
	//		int entityid;
	//		NBTTagCompound nbt;
	//
	//		public AsyncEvent(int entityid, NBTTagCompound nbt) {
	//			super();
	//			this.entityid = entityid;
	//			this.nbt = nbt;
	//		}
	//		@Override
	//		public void run() {
	//			// TODO 自動生成されたメソッド・スタブ
	//			while(!ClientHelper.isWorldPresent()){
	//
	//			}
	//			Entity entity = ClientHelper.getWorld().getEntityByID(entityid);
	//			UnsagaMod.logger.trace(this.getClass().getName(), "sync!",entityid);
	//			if(entity instanceof EntityLivingBase){
	//				adapter.getCapability(entity).catchSyncData(nbt);
	//			}
	//		}
	//
	//
	//	}
}
