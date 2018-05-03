package mods.hinasch.unsaga.util;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

import mods.hinasch.lib.capability.CapabilityAdapterFactory.CapabilityAdapterPlanImpl;
import mods.hinasch.lib.capability.CapabilityAdapterFrame;
import mods.hinasch.lib.capability.CapabilityStorage;
import mods.hinasch.lib.capability.ComponentCapabilityAdapters;
import mods.hinasch.lib.capability.ISyncCapability;
import mods.hinasch.lib.client.ClientHelper;
import mods.hinasch.lib.core.HSLib;
import mods.hinasch.lib.network.PacketSyncCapability;
import mods.hinasch.lib.util.UtilNBT;
import mods.hinasch.unsaga.UnsagaMod;
import mods.hinasch.unsaga.common.tool.ItemWeaponUnsaga;
import mods.hinasch.unsaga.core.entity.passive.EntityUnsagaChest;
import mods.hinasch.unsaga.core.potion.LivingState;
import mods.hinasch.unsaga.villager.IInteractionInfo;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class LivingHelper {

	//unsagamodへ移行

	public static String SYNC_ID = "living_helper";

	@CapabilityInject(ILivingHelper.class)
	public static Capability<ILivingHelper> CAPA;

	public static final CapabilityAdapterFrame BUILDER = HSLib.capabilityFactory.create(new CapabilityAdapterPlanImpl(()->CAPA,()->ILivingHelper.class,()->DefaultImpl.class,Storage::new));
	public static final ComponentCapabilityAdapters.Entity<ILivingHelper> ADAPTER = BUILDER.createChildEntity(SYNC_ID);

	static{
		ADAPTER.setRequireSerialize(true);
		ADAPTER.setPredicate(input -> input.getObject() instanceof EntityLivingBase);
	}
	public static interface ILivingHelper extends ISyncCapability,IInteractionInfo{

//		public boolean checkStacks(Iterable<ItemStack> ite,ItemStack isIn);
		public PotionEffect getState(Potion p);
		public int getWeaponGuardCooling();
		public void decrWeaponGuardProgress();
		public void resetWeaponGuardProgress();
		public void decrWeaponGuardCooling();
		public int getWeaponGuardProgress();
		public int getSprintTimer();
		public void setSprintTimer(int timer);
		public Map<Potion,PotionEffect> getStateMap();
		public void setStateMap(Map<Potion,PotionEffect> map);
		public boolean isStateActive(Potion potion);
		public void removeState(Potion potion);
		public void addState(PotionEffect effect);
		public Queue<Potion> getRemoveStateQueue();
	}

	public static class DefaultImpl implements ILivingHelper{
		int guardCooling = 0;
		int guardProgress = 0;
		int sprintTimer = 0;
		boolean init = false;
		boolean isTechActionProgress = false;

		ImmutableMap<Potion,PotionEffect> states = ImmutableMap.of();
		Queue<Potion> removeQueue = new ArrayBlockingQueue(20);

		Optional<EntityVillager> villager = Optional.empty();
		Optional<EntityUnsagaChest> chest = Optional.empty();

//		@Override
//		public Map<CacheType, List<Equipment>> getMap() {
//			// TODO 自動生成されたメソッド・スタブ
//			return this.map;
//		}

		@Override
		public NBTTagCompound getSendingData() {
			return (NBTTagCompound) CAPA.getStorage().writeNBT(CAPA, this, null);
		}

		@Override
		public void catchSyncData(NBTTagCompound nbt) {
			// TODO 自動生成されたメソッド・スタブ
			CAPA.getStorage().readNBT(CAPA, this, null, nbt);
		}

		@Override
		public void onPacket(PacketSyncCapability message, MessageContext ctx) {

			if(ctx.side.isServer()){
				EntityPlayer ep = ctx.getServerHandler().player;
				int timer = message.getNbt().getInteger("sprint");
				ADAPTER.getCapability(ep).catchSyncData(message.getNbt());
				ItemStack is = ep.getHeldItemMainhand();
				if(!is.isEmpty() && is.getItem() instanceof ItemWeaponUnsaga){
					is.getItem().onItemRightClick(ep.getEntityWorld(), ep, EnumHand.MAIN_HAND);
				}
			}else{
				NBTTagCompound args = message.getArgs();
				int operation = args.getInteger("operation");
				if(operation==1){
					int id = args.getInteger("entity");
					String name = args.getString("potion");
					Potion potion = ForgeRegistries.POTIONS.getValue(new ResourceLocation(name));
					Entity entity = ClientHelper.getWorld().getEntityByID(id);
					UnsagaMod.logger.trace(this.getClass().getName(), potion,entity,name);
					if(potion!=null && entity instanceof EntityLivingBase){
						ADAPTER.getCapability(entity).removeState(potion);
					}
				}

			}
		}

		@Override
		public String getIdentifyName() {
			// TODO 自動生成されたメソッド・スタブ
			return SYNC_ID;
		}


		@Override
		public int getWeaponGuardProgress() {
			// TODO 自動生成されたメソッド・スタブ
			return this.guardProgress;
		}

		@Override
		public int getWeaponGuardCooling() {
			return this.guardCooling;

		}

		@Override
		public void resetWeaponGuardProgress() {
			this.guardProgress = 4;
			this.guardCooling = 10;
		}

		@Override
		public void decrWeaponGuardProgress() {
			this.guardProgress -= 1;
			if(this.guardProgress<0){
				this.guardProgress = 0;
			}
		}

		@Override
		public void decrWeaponGuardCooling() {
			// TODO 自動生成されたメソッド・スタブ
			this.guardCooling -= 1;
			if(this.guardCooling<0){
				this.guardCooling = 0;
			}
		}
		@Override
		public int getSprintTimer() {
			// TODO 自動生成されたメソッド・スタブ
			return this.sprintTimer;
		}
		@Override
		public void setSprintTimer(int timer) {
			// TODO 自動生成されたメソッド・スタブ
			this.sprintTimer = timer;
		}

		@Override
		public Map<Potion, PotionEffect> getStateMap() {
			// TODO 自動生成されたメソッド・スタブ
			return ImmutableMap.copyOf(this.states);
		}

		@Override
		public void setStateMap(Map<Potion, PotionEffect> map) {
			// TODO 自動生成されたメソッド・スタブ
			this.states = ImmutableMap.copyOf(map);
		}

		@Override
		public boolean isStateActive(Potion potion) {
			// TODO 自動生成されたメソッド・スタブ
			return this.getStateMap().containsKey(potion);
		}

		@Override
		public void removeState(Potion potion) {
			Map<Potion,PotionEffect> map = Maps.newHashMap(this.states);
			map.remove(potion);
			this.states = ImmutableMap.copyOf(map);

//			this.removeQueue.offer(potion);
		}

		@Override
		public void addState(PotionEffect effect) {
			Map<Potion,PotionEffect> map = Maps.newHashMap(this.states);
			map.put(effect.getPotion(), effect);
			this.states = ImmutableMap.copyOf(map);
		}

		@Override
		public Queue<Potion> getRemoveStateQueue() {
			// TODO 自動生成されたメソッド・スタブ
			return this.removeQueue;
		}

		@Override
		public void setMerchant(Optional<EntityVillager> villager) {
			// TODO 自動生成されたメソッド・スタブ
			this.villager = villager;
		}

		@Override
		public Optional<EntityVillager> getMerchant() {
			// TODO 自動生成されたメソッド・スタブ
			return this.villager;
		}

		@Override
		public void setEntityChest(EntityUnsagaChest chest) {
			// TODO 自動生成されたメソッド・スタブ
			this.chest = chest!=null ? Optional.of(chest) : Optional.empty();
		}

		@Override
		public Optional<EntityUnsagaChest> getChest() {
			// TODO 自動生成されたメソッド・スタブ
			return this.chest;
		}

		@Override
		public PotionEffect getState(Potion p) {
			// TODO 自動生成されたメソッド・スタブ
			return this.getStateMap().get(p);
		}
	}

	@SubscribeEvent
	public static void onLivingUpdate(LivingUpdateEvent e){
		if(ADAPTER.hasCapability(e.getEntityLiving())){
			ILivingHelper instance = ADAPTER.getCapability(e.getEntityLiving());
	        Iterator<Potion> iterator = instance.getStateMap().keySet().iterator();

	        try
	        {
	            while (iterator.hasNext())
	            {
	                Potion potion = iterator.next();
	                PotionEffect potioneffect = instance.getStateMap().get(potion);

//	                UnsagaMod.logger.trace(potioneffect.getEffectName(), potioneffect.getDuration());
	                if (!potioneffect.onUpdate(e.getEntityLiving()))
	                {
	                    if (!e.getEntityLiving().world.isRemote)
	                    {
	                        ADAPTER.getCapability(e.getEntityLiving()).removeState(potion);
	                        if (!e.getEntityLiving().world.isRemote)
	                        {
	                        	potioneffect.getPotion().removeAttributesModifiersFromEntity(e.getEntityLiving(), e.getEntityLiving().getAttributeMap(), potioneffect.getAmplifier());
	                        }
	            			NBTTagCompound args = UtilNBT.compound();
	            			args.setString("potion", potion.getRegistryName().toString());
	            			args.setInteger("entity", e.getEntityLiving().getEntityId());
	            			args.setInteger("operation", 1);
	            			HSLib.getPacketDispatcher().sendToAll(PacketSyncCapability.create(CAPA, ADAPTER.getCapability(e.getEntityLiving()), args));
//	                        e.getEntityLiving().onFinishedPotionEffect(potioneffect);
	                    }
	                }
	            }
	        }
	        catch (ConcurrentModificationException var11)
	        {
	            ;
	        }

//	        ADAPTER.getCapability(e.getEntityLiving()).getRemoveStateQueue()
//	        .forEach(in ->ADAPTER.getCapability(e.getEntityLiving()).getStateMap().remove(in));
		}

	}

	public static void onEntityHurt(LivingHurtEvent e){
		for(PotionEffect state:getCapability(e.getEntityLiving()).getStateMap().values()){
			if(state.getPotion() instanceof LivingState){
				((LivingState)state.getPotion()).affectOnHurt(e, state.getAmplifier());
			}
		}
	}
	public static void registerEvents(){
		PacketSyncCapability.registerSyncCapability(SYNC_ID, CAPA);

		ADAPTER.registerAttachEvent();

	}

	public static class Storage extends CapabilityStorage<ILivingHelper>{

		@Override
		public void writeNBT(NBTTagCompound comp, Capability<ILivingHelper> capability, ILivingHelper instance,
				EnumFacing side) {
			// TODO 自動生成されたメソッド・スタブ
			comp.setInteger("sprint", instance.getSprintTimer());
		}

		@Override
		public void readNBT(NBTTagCompound comp, Capability<ILivingHelper> capability, ILivingHelper instance,
				EnumFacing side) {
			// TODO 自動生成されたメソッド・スタブ
			if(comp.hasKey("sprint")){
				instance.setSprintTimer(comp.getInteger("sprint"));
			}
		}

	}

	public static ILivingHelper getCapability(Entity entity){
		return ADAPTER.getCapability(entity);
	}

	public static boolean isStateActive(EntityLivingBase liv,Potion potion){
		return getCapability(liv).isStateActive(potion);
	}
	public static PotionEffect getState(EntityLivingBase liv,Potion potion){
		return getCapability(liv).getState(potion);
	}
}
