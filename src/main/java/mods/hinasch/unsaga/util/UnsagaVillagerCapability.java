package mods.hinasch.unsaga.util;

import java.util.Random;

import joptsimple.internal.Strings;
import mods.hinasch.lib.capability.CapabilityAdapterFactory.CapabilityAdapterPlanImpl;
import mods.hinasch.lib.capability.CapabilityAdapterFrame;
import mods.hinasch.lib.capability.CapabilityStorage;
import mods.hinasch.lib.capability.ComponentCapabilityAdapters;
import mods.hinasch.lib.iface.IIntSerializable;
import mods.hinasch.lib.iface.IRequireInitializing;
import mods.hinasch.lib.item.ItemUtil;
import mods.hinasch.lib.util.HSLibs;
import mods.hinasch.lib.util.Statics;
import mods.hinasch.lib.util.UtilNBT;
import mods.hinasch.unsaga.UnsagaMod;
import mods.hinasch.unsaga.util.UnsagaVillagerCapability.IUnsagaVillager.SmithProfessionality;
import mods.hinasch.unsaga.util.UnsagaVillagerCapability.IUnsagaVillager.VillagerType;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

public class UnsagaVillagerCapability {

	public static class DefaultImpl implements IUnsagaVillager{

		protected VillagerType type = VillagerType.UNKNOWN;
		protected NonNullList<ItemStack> merchantInventory = ItemUtil.createStackList(10);
		protected NonNullList<ItemStack> merchantInvSecret = ItemUtil.createStackList(3);
		protected SmithProfessionality smithType = SmithProfessionality.NORMAL;
		protected boolean initMerchandice = false;
		protected boolean foundSecret = false;
		protected int distributionLevel = 0;
		protected int carrierID = -1;
		protected long purchaceInterval = 0;
		@Override
		public int getCarrierID() {
			// TODO 自動生成されたメソッド・スタブ
			return this.carrierID;
		}

		@Override
		public int getDistributionLevel() {
			// TODO 自動生成されたメソッド・スタブ
			return this.distributionLevel;
		}

		@Override
		public ItemStack getMerchandise(int par1) {
			// TODO 自動生成されたメソッド・スタブ
			return this.merchantInventory.get(par1);
		}

		@Override
		public NonNullList<ItemStack> getMerchandises() {
			// TODO 自動生成されたメソッド・スタブ
			return this.merchantInventory;
		}

		@Override
		public long getPurchaseData() {
			// TODO 自動生成されたメソッド・スタブ
			return this.purchaceInterval;
		}

		@Override
		public ItemStack getSecretMerchandise(int par1) {
			// TODO 自動生成されたメソッド・スタブ
			return this.merchantInvSecret.get(par1);
		}

		@Override
		public NonNullList<ItemStack> getSecretMerchandises() {
			// TODO 自動生成されたメソッド・スタブ
			return this.merchantInvSecret;
		}

		@Override
		public SmithProfessionality getSmithProfessionality() {
			// TODO 自動生成されたメソッド・スタブ
			return this.smithType;
		}

		@Override
		public VillagerType getType() {
			// TODO 自動生成されたメソッド・スタブ
			return this.type;
		}

		@Override
		public boolean hasFoundSecret() {
			// TODO 自動生成されたメソッド・スタブ
			return this.foundSecret;
		}

		@Override
		public boolean hasInitialized() {
			// TODO 自動生成されたメソッド・スタブ
			return this.initMerchandice;
		}



		@Override
		public void setCarrierID(int par1) {
			// TODO 自動生成されたメソッド・スタブ
			this.carrierID = par1;
		}


		@Override
		public void setDistributionLevel(int par1) {
			// TODO 自動生成されたメソッド・スタブ
			this.distributionLevel = par1;
		}

		@Override
		public void setFoundSecret(boolean par1) {
			// TODO 自動生成されたメソッド・スタブ
			this.foundSecret = par1;
		}

		@Override
		public void setInitialized(boolean par1) {
			// TODO 自動生成されたメソッド・スタブ
			this.initMerchandice = par1;
		}

		@Override
		public void setMerchandise(int slot, ItemStack is) {
			// TODO 自動生成されたメソッド・スタブ
			this.merchantInventory.set(slot, is);
		}

		@Override
		public void setMerchandises(NonNullList<ItemStack> stacks) {
			this.merchantInventory = stacks;
//			Arrays.fill(merchantInventory, 0, this.merchantInventory.length, null);
//
//
//			for(int i=0;i<this.merchantInventory.length;i++){
//
//				if(stacks.size()>i){
//					this.merchantInventory[i] = stacks.get(i);
//				}
//
//			}
			//			this.merchantInventory = stacks;
		}

		@Override
		public void setPurchaseDate(long par1) {
			// TODO 自動生成されたメソッド・スタブ
			this.purchaceInterval = par1;
		}

		@Override
		public void setSecretMerchandise(int slot, ItemStack is) {
			// TODO 自動生成されたメソッド・スタブ

			this.merchantInvSecret.set(slot, is);
		}

		@Override
		public void setSecretMerchandises(NonNullList<ItemStack> stacks) {
			this.merchantInvSecret = stacks;
//			Arrays.fill(merchantInvSecret, 0, this.merchantInvSecret.length, null);
//
//
//			for(int i=0;i<this.merchantInvSecret.length;i++){
//
//				if(stacks.size()>i){
//					this.merchantInvSecret[i] = stacks.get(i);
//				}
//
//			}
			//			this.merchantInvSecret = stacks;
		}

		@Override
		public void setSmithProfessionality(SmithProfessionality par1) {
			// TODO 自動生成されたメソッド・スタブ
			this.smithType = par1;
		}

		@Override
		public void setType(VillagerType par1) {
			// TODO 自動生成されたメソッド・スタブ
			this.type = par1;
		}

	}

	public static interface IUnsagaVillager extends IRequireInitializing{
		public static enum SmithProfessionality implements IIntSerializable{
			NORMAL(0,Strings.EMPTY),ABILITY(1,"gui.smith.abilityPro"),DURABILITY(2,"gui.smith.repairPro");

			public static SmithProfessionality fromMeta(int meta){
				return HSLibs.fromMeta(SmithProfessionality.values(), meta);
			}
			public static SmithProfessionality getRandomType(Random rand){
				return fromMeta(rand.nextInt(3));
			}

			private int meta;

			private String unlocalizedName;

			private SmithProfessionality(int meta,String unlname) {
				this.meta = meta;
				this.unlocalizedName = unlname;
			}
			public int getMeta() {
				return meta;
			}

			public String getUnlocalizedName(){
				return this.unlocalizedName;
			}
		}
		public static enum VillagerType implements IIntSerializable{
			BARTERING(0),SMITH(1),CARRIER(2),UNKNOWN(999);

			public static VillagerType fromMeta(int meta){
				return HSLibs.fromMeta(VillagerType.values(), meta);
			}

			private int meta;

			private VillagerType(int meta) {
				this.meta = meta;
			}

			public int getMeta() {
				return meta;
			}
		}

		//		public void setMerchandiseInitialized(boolean par1);
		public int getCarrierID();
		public int getDistributionLevel();
		public ItemStack getMerchandise(int par1);
		public NonNullList<ItemStack> getMerchandises();
		public long getPurchaseData();
		public ItemStack getSecretMerchandise(int par1);
		public NonNullList<ItemStack> getSecretMerchandises();
		public SmithProfessionality getSmithProfessionality();
		public VillagerType getType();
		public boolean hasFoundSecret();
		public void setCarrierID(int par1);
		public void setDistributionLevel(int par1);
		//		public boolean hasMerchandiseInitialized();
		public void setFoundSecret(boolean par1);
		public void setMerchandise(int slot,ItemStack is);
		public void setMerchandises(NonNullList<ItemStack> stacks);
		public void setPurchaseDate(long par1);
		public void setSecretMerchandise(int slot,ItemStack is);
		public void setSecretMerchandises(NonNullList<ItemStack> stacks);
		public void setSmithProfessionality(SmithProfessionality par1);
		public void setType(VillagerType par1);


	}
	public static class Storage extends  CapabilityStorage<IUnsagaVillager>{


		@Override
		public void readNBT(NBTTagCompound comp, Capability<IUnsagaVillager> capability, IUnsagaVillager instance,
				EnumFacing side) {
			instance.setType(VillagerType.fromMeta(comp.getInteger("type")));
			//			UnsagaMod.logger.trace("load vilalger", instance.getType());
			if(instance.getType()==VillagerType.BARTERING){
				NBTTagList tagMerchandise = comp.getTagList("items", UtilNBT.NBTKEY_COMPOUND);
				NBTTagList tagSecret = comp.getTagList("itemsSecret", UtilNBT.NBTKEY_COMPOUND);
				instance.setMerchandises(UtilNBT.getItemStacksFromNBT(tagMerchandise, instance.getMerchandises().size()));
				instance.setSecretMerchandises(UtilNBT.getItemStacksFromNBT(tagSecret, instance.getSecretMerchandises().size()));
				instance.setPurchaseDate(comp.getLong("interval"));
				instance.setDistributionLevel(comp.getInteger("distributionLevel"));
				UnsagaMod.logger.trace("load distribution level", instance.getDistributionLevel());
				instance.setFoundSecret(comp.getBoolean("hasFoundSecret"));

			}
			if(instance.getType()==VillagerType.CARRIER){
				instance.setCarrierID(comp.getInteger("carrierID"));
			}
			if(instance.getType()==VillagerType.SMITH){

				instance.setSmithProfessionality(SmithProfessionality.fromMeta(comp.getInteger("smithType")));
				UnsagaMod.logger.trace("load smith type", instance.getSmithProfessionality());
			}
			instance.setInitialized(comp.getBoolean("hasInit"));
		}

		@Override
		public void writeNBT(NBTTagCompound comp, Capability<IUnsagaVillager> capability, IUnsagaVillager instance,
				EnumFacing side) {
			comp.setInteger("type",instance.getType().getMeta());

			if(instance.getType()==VillagerType.BARTERING){
				NBTTagList tagList = new NBTTagList();
				NBTTagList tagListSecret = new NBTTagList();
				UtilNBT.writeItemStacksToNBTTag(tagList, instance.getMerchandises());
				UtilNBT.writeItemStacksToNBTTag(tagListSecret, instance.getSecretMerchandises());
				comp.setLong("interval", instance.getPurchaseData());
				comp.setTag("items", tagList);
				comp.setTag("itemsSecret", tagListSecret);
				UnsagaMod.logger.trace("save distribution level", instance.getDistributionLevel());
				comp.setInteger("distributionLevel", instance.getDistributionLevel());
				comp.setBoolean("hasFoundSecret", instance.hasFoundSecret());

			}
			if(instance.getType()==VillagerType.SMITH){
				UnsagaMod.logger.trace("save smith type", instance.getSmithProfessionality());
				comp.setInteger("smithType", instance.getSmithProfessionality().getMeta());
			}
			if(instance.getType()==VillagerType.CARRIER){
				comp.setInteger("carrierID", instance.getCarrierID());
			}
			comp.setBoolean("hasInit", instance.hasInitialized());
		}

	}
	@CapabilityInject(IUnsagaVillager.class)
	public static Capability<IUnsagaVillager> CAPA;


	public static CapabilityAdapterFrame<IUnsagaVillager> base = UnsagaMod.capabilityAdapterFactory.create(
			new CapabilityAdapterPlanImpl(()->CAPA,()->IUnsagaVillager.class,()->DefaultImpl.class,Storage::new));


	public static ComponentCapabilityAdapters.Entity<IUnsagaVillager> adapter = base.createChildEntity("villager");


	static{
		adapter.setPredicate(ev -> ev.getObject() instanceof EntityVillager);
		adapter.setRequireSerialize(true);
	}

	public static IUnsagaVillager getCapability(EntityVillager villager){
		if(hasCapability(villager)){
			return adapter.getCapability(villager);
		}
		return null;
	}
	public static boolean hasCapability(EntityVillager villager){
		return adapter.hasCapability(villager);
	}

	public static void registerEvents(){
		adapter.registerAttachEvent((inst,capa,facing,e)->{
			EntityVillager villager = (EntityVillager) e.getObject();
			if(Statics.checkProfession(villager.getProfessionForge(), Statics.VILLAGER_BLACKSMITH) && !inst.hasInitialized()){


				inst.setType(VillagerType.SMITH);
				inst.setSmithProfessionality(SmithProfessionality.getRandomType(villager.getRNG()));

				//				inst.setSmithProfessionality(SmithProfessionality.getRandomType(villager.getRNG()));
			}else{

				inst.setType(VillagerType.BARTERING);


			}

		});
	}


}
