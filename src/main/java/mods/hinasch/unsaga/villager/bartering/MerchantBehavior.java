package mods.hinasch.unsaga.villager.bartering;

import java.util.Set;

import javax.annotation.Nullable;

import mods.hinasch.lib.item.ItemUtil;
import mods.hinasch.lib.misc.Triplet;
import mods.hinasch.lib.world.WorldHelper;
import mods.hinasch.unsaga.core.advancement.UnsagaTriggers;
import mods.hinasch.unsaga.material.UnsagaMaterial;
import mods.hinasch.unsaga.util.ToolCategory;
import mods.hinasch.unsaga.villager.UnsagaVillagerCapability;
import mods.hinasch.unsaga.villager.village.VillageDistributionCapability;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.MathHelper;
import net.minecraft.village.Village;
import net.minecraft.world.World;

public class MerchantBehavior {

	World world;
	EntityVillager villager;
	@Nullable Village villageObj;
	MerchandiseFactory factory;

	public static final int MAX_DIST_LEVEL = 30;

	public MerchantBehavior(World world,EntityVillager villager){
		this.world = world;
		this.villager = villager;
		this.villageObj = this.world.villageCollection.getNearestVillage(villager.getPosition(), 30);
		this.factory = new MerchandiseFactory(world.rand);
	}
	public boolean hasComeUpdateTime(){
		if(UnsagaVillagerCapability.ADAPTER.hasCapability(villager)){
			if(UnsagaVillagerCapability.ADAPTER.getCapability(villager).getRecentStockedTime()<=0){
				return true;
			}
			if(world.getTotalWorldTime() - UnsagaVillagerCapability.ADAPTER.getCapability(villager).getRecentStockedTime()>24000){
				return true;
			}

		}

		return false;
	}


	public void addTransactionPoint(int point){
		if(UnsagaVillagerCapability.ADAPTER.hasCapability(villager)){
			int prev = UnsagaVillagerCapability.ADAPTER.getCapability(villager).getTransactionPoint();
			UnsagaVillagerCapability.ADAPTER.getCapability(villager).setTransactionPoint(prev + point);
		}
	}

	private void syncDistributionLevel(){
		//		OptionalInt optMaxDistLevel = this.world.getEntitiesWithinAABB(EntityVillager.class, this.villager.getEntityBoundingBox().grow(30.0D)).stream().filter(in-> UnsagaVillagerCapability.ADAPTER.hasCapability(in))
		//				.mapToInt(in -> UnsagaVillagerCapability.ADAPTER.getCapability(in).getDistributionLevel()).max();
		int villageDistribution = this.villageObj!=null ? VillageDistributionCapability.ADAPTER.getCapability(villageObj).getDistributionPoint() : 0;
		int current = UnsagaVillagerCapability.ADAPTER.getCapability(villager).getDistributionLevel();

		//		if(optMaxDistLevel.isPresent()){
		if(current<villageDistribution){
			UnsagaVillagerCapability.ADAPTER.getCapability(villager).setDistributionLevel(villageDistribution);
		}else{
			if(this.villageObj!=null){
				VillageDistributionCapability.ADAPTER.getCapability(villageObj).setDistributionPoint(current);
			}
		}
		//		}
	}
	private void checkDistributionLevelUp(EntityPlayer ep){
		if(UnsagaVillagerCapability.ADAPTER.hasCapability(villager)){
			if(UnsagaVillagerCapability.ADAPTER.getCapability(villager).getDistributionLevel()>MAX_DIST_LEVEL){
				return;
			}
			int threshold = BarteringUtil.calcNextTransactionThreshold(UnsagaVillagerCapability.ADAPTER.getCapability(villager).getDistributionLevel());
			if(UnsagaVillagerCapability.ADAPTER.getCapability(villager).getTransactionPoint()>=threshold){
				int base = UnsagaVillagerCapability.ADAPTER.getCapability(villager).getDistributionLevel();
				UnsagaVillagerCapability.ADAPTER.getCapability(villager).setDistributionLevel(base+1);
				UnsagaVillagerCapability.ADAPTER.getCapability(villager).setTransactionPoint(0);

				if(WorldHelper.isServer(ep.getEntityWorld())){
					if(UnsagaVillagerCapability.ADAPTER.getCapability(villager).getDistributionLevel()>=5){
						//						ep.addStat(UnsagaMod.core.achievements.bartering2);
						UnsagaTriggers.BARTERING_TIER2.trigger((EntityPlayerMP) ep);
					}
					if(UnsagaVillagerCapability.ADAPTER.getCapability(villager).getDistributionLevel()>=10){
						//						ep.addStat(UnsagaMod.core.achievements.bartering3);
						UnsagaTriggers.BARTERING_TIER3.trigger((EntityPlayerMP) ep);
					}
				}

			}

			this.syncDistributionLevel();
		}
	}
	public boolean hasDisplayedSecrets(){
		if(UnsagaVillagerCapability.ADAPTER.hasCapability(villager)){
			return UnsagaVillagerCapability.ADAPTER.getCapability(villager).hasDisplayedSecretMerchandises();
		}
		return false;
	}

	public void resetDisplayedSecretMerchandise(){
		if(UnsagaVillagerCapability.ADAPTER.hasCapability(villager)){
			UnsagaVillagerCapability.ADAPTER.getCapability(villager).setHasDisplayedSecrets(false);
		}
	}
	/**
	 * 生成レベル１：店レベル/10・・・商品の半分<br>
	 * 生成レベル２：(店レベル +流通レベル*4)/10・・・商品の半分<br>
	 * 生成レベル３：生成レベル２に＋２したもの・・・目利き<br>
	 */
	public void updateMerchandises(EntityPlayer ep){



		if(UnsagaVillagerCapability.ADAPTER.hasCapability(villager)){

			this.checkDistributionLevelUp(ep);
			UnsagaVillagerCapability.ADAPTER.getCapability(villager).setStockedTime(this.world.getTotalWorldTime());
			int distLV = UnsagaVillagerCapability.ADAPTER.getCapability(villager).getDistributionLevel();
			Triplet<Integer,Integer,Integer> generateLevel = this.getGenerateLevels(distLV);
			Set<ToolCategory> category = UnsagaVillagerCapability.ADAPTER.getCapability(villager).getShopType().getAvailableMerchandiseCategory();
			Set<UnsagaMaterial> stricts = UnsagaVillagerCapability.ADAPTER.getCapability(villager).getShopType().getAvailableMerchandiseMaterials();
			NonNullList<ItemStack> merchandises1 = factory.createMerchandises(4, generateLevel.first, category,stricts);
			NonNullList<ItemStack> merchandises2 = factory.createMerchandises(5, generateLevel.second, category,stricts);
			//			NonNullList<ItemStack> list = ItemUtil.createStackList(9);
			merchandises1.addAll(merchandises2);
			//			list.set(merchandises1);
			UnsagaVillagerCapability.ADAPTER.getCapability(villager).setMerchandises(merchandises1);
			UnsagaVillagerCapability.ADAPTER.getCapability(villager).setSecretMerchandises(ItemUtil.createStackList(9));
		}
	}

	public NonNullList<ItemStack> createSecretMerchandises(int num){
		if(UnsagaVillagerCapability.ADAPTER.hasCapability(villager) && !UnsagaVillagerCapability.ADAPTER.getCapability(villager).hasDisplayedSecretMerchandises()){
			int distLV = UnsagaVillagerCapability.ADAPTER.getCapability(villager).getDistributionLevel();
			Triplet<Integer,Integer,Integer> generateLevel = this.getGenerateLevels(distLV);
			Set<ToolCategory> category = UnsagaVillagerCapability.ADAPTER.getCapability(villager).getShopType().getAvailableMerchandiseCategory();
			Set<UnsagaMaterial> stricts = UnsagaVillagerCapability.ADAPTER.getCapability(villager).getShopType().getAvailableMerchandiseMaterials();
			NonNullList<ItemStack> merchandises1 = factory.createMerchandises(4, generateLevel.first, category,stricts);
			NonNullList<ItemStack> merchandises2 = factory.createMerchandises(5, generateLevel.second, category,stricts);
			NonNullList<ItemStack> merchandises3 = factory.createMerchandises(num, generateLevel.third(), category,stricts);
			//			NonNullList<ItemStack> list = ItemUtil.createStackList(9);
			//			list.set(merchandises3);
			//			UnsagaVillagerCapability.adapter.getCapability(villager).setSecretMerchandises(list);
			UnsagaVillagerCapability.ADAPTER.getCapability(villager).setHasDisplayedSecrets(true);
			return merchandises3;
		}
		return NonNullList.create();
	}

	private Triplet<Integer,Integer,Integer> getGenerateLevels(int distLV){
		int generateLevel1 = MathHelper.clamp(this.calcShopLevel(villager)/10,2,20);
		int generateLevel2 = MathHelper.clamp((this.calcShopLevel(villager) + distLV * 4)/10,2,20);
		int generateLevel3 = MathHelper.clamp(generateLevel2  + 2, 2,20);
		return Triplet.of(generateLevel1, generateLevel2, generateLevel3);
	}
	/**
	 * ベース店レベル＋村の半径/3+村人数/3
	 * @param villager
	 * @return
	 */
	public int calcShopLevel(EntityVillager villager){
		Village village = villager.getEntityWorld().getVillageCollection().getNearestVillage(villager.getPosition(), 32);
		if(UnsagaVillagerCapability.ADAPTER.hasCapability(villager)){
			if(village!=null){
				return (int)(village.getVillageRadius() / 3 + village.getNumVillagers() / 3) + UnsagaVillagerCapability.ADAPTER.getCapability(villager).getBaseShopLevel();
			}else{
				return UnsagaVillagerCapability.ADAPTER.getCapability(villager).getBaseShopLevel();
			}
		}

		return 1;
	}
}
