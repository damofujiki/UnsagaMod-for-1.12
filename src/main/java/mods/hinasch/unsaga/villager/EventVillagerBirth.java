//package mods.hinasch.unsaga.villager;
//
//import mods.hinasch.unsaga.UnsagaMod;
//import net.minecraft.entity.passive.EntityVillager;
//import net.minecraft.nbt.NBTTagCompound;
//import net.minecraft.world.World;
//import net.minecraft.world.WorldSavedData;
//import net.minecraftforge.event.entity.living.BabyEntitySpawnEvent;
//import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
//import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
//
//public class EventVillagerBirth {
//
//	@SubscribeEvent
//	public void onBirth(BabyEntitySpawnEvent e){
//
//		if(e.getChild() instanceof EntityVillager && UnsagaMod.configHandler.isEnabledHighProbBirthUnsagaVillagers()){
//			EntityVillager child = (EntityVillager) e.getChild();
//			if(child.getRNG().nextFloat()<0.5){
//				child.setProfession(UnsagaVillagerProfession.instance().getRandomProfession(child.getRNG()));
//			}
//
//			e.setChild(child);
//		}
//	}
//
//	@SubscribeEvent
//	public void onEntityConstruct(LivingUpdateEvent e){
////		if(!UnsagaMod.configHandler.isForceToSpawnMerchantHouse()){
////			return;
////		}
////		if(e.getEntity() instanceof EntityVillager && WorldHelper.isServer(e.getEntityLiving().worldObj)){
////			EntityVillager villager = (EntityVillager) e.getEntity();
////			WorldSaveDataVillagerBirth data = WorldSaveDataVillagerBirth.get(e.getEntity().worldObj);
////			if(!data.hasBornMerchant){
////				data.hasBornMerchant = true;
////				villager.setProfession(UnsagaVillagerProfession.instance().merchant);
////				return;
////			}
////			if(!data.hasBornMagicMerchant){
////				data.hasBornMagicMerchant = true;
////				villager.setProfession(UnsagaVillagerProfession.instance().magicMerchant);
////				return;
////			}
////			if(!data.hasBornSmith){
////				data.hasBornSmith = true;
////				villager.setProfession(UnsagaVillagerProfession.instance().unsagaSmith);
////				return;
////			}
////		}
//	}
//
//
//	public static class WorldSaveDataVillagerBirth extends WorldSavedData{
//
//		public static final String ID = UnsagaMod.MODID+".villagerBirth";
//		boolean hasBornMerchant = false;
//		boolean hasBornMagicMerchant = false;
//		boolean hasBornSmith = false;
//
//		public static WorldSaveDataVillagerBirth get(World world){
//			WorldSaveDataVillagerBirth data = (WorldSaveDataVillagerBirth) world.loadItemData(WorldSaveDataVillagerBirth.class, ID);
//			if(data==null){
//				data = new WorldSaveDataVillagerBirth();
//				world.setItemData(ID, data);
//			}
//			  return data;
//		}
//		public WorldSaveDataVillagerBirth(){
//			this(ID);
//		}
//		public WorldSaveDataVillagerBirth(String name) {
//			super(name);
//			// TODO 自動生成されたコンストラクター・スタブ
//		}
//
//		@Override
//		public void readFromNBT(NBTTagCompound nbt) {
//			// TODO 自動生成されたメソッド・スタブ
//			this.hasBornMerchant = nbt.getBoolean("hasBornMerchant");
//			this.hasBornMagicMerchant = nbt.getBoolean("hasBornMagicMerchant");
//			this.hasBornSmith = nbt.getBoolean("hasBornSmith");
//		}
//
//		@Override
//		public NBTTagCompound writeToNBT(NBTTagCompound compound) {
//			compound.setBoolean("hasBornMerchant", this.hasBornMerchant);
//			compound.setBoolean("hasBornMagicMerchant", this.hasBornMagicMerchant);
//			compound.setBoolean("hasBornSmith", this.hasBornSmith);
//			return compound;
//		}
//
//	}
//}
