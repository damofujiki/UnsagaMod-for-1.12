package mods.hinasch.unsaga.villager;

import java.util.List;
import java.util.Random;

import com.google.common.collect.Lists;

import mods.hinasch.unsaga.UnsagaMod;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.VillagerRegistry.VillagerCareer;
import net.minecraftforge.fml.common.registry.VillagerRegistry.VillagerProfession;

public class UnsagaVillagerProfession {

	public static final VillagerProfession MERCHANT = new VillagerProfession(UnsagaMod.MODID+":merchant"
			,UnsagaMod.MODID+":textures/entity/villager/merchant.png"
			,"minecraft:textures/entity/zombie_villager/zombie_butcher.png");
	public static final VillagerProfession MAGIC_MERCHANT = new VillagerProfession(UnsagaMod.MODID+":magicMerchant"
			,UnsagaMod.MODID+":textures/entity/villager/magic_merchant.png"
			,"minecraft:textures/entity/zombie_villager/zombie_butcher.png");
	public static final VillagerProfession BLACKSMITH = new VillagerProfession(UnsagaMod.MODID+":blackSmith"
			,UnsagaMod.MODID+":textures/entity/villager/unsaga_smith.png"
			,"minecraft:textures/entity/zombie_villager/zombie_smith.png");
	public List<VillagerProfession> professions = Lists.newArrayList();

	protected static UnsagaVillagerProfession INSTANCE;
	public static UnsagaVillagerProfession instance(){
		if(INSTANCE == null){
			INSTANCE = new UnsagaVillagerProfession();
		}
		return INSTANCE;
	}

	public static boolean isUnsagaVillager(EntityVillager villager){
		UnsagaVillagerProfession professions = UnsagaVillagerProfession.instance();
		if(villager.getProfessionForge()==MERCHANT){
			return true;
		}
		if(villager.getProfessionForge()==MAGIC_MERCHANT){
			return true;
		}
		if(villager.getProfessionForge()==BLACKSMITH){
			return true;
		}
		return false;
	}

	public VillagerProfession getRandomProfession(Random rand){
		return professions.get(rand.nextInt(professions.size()));

	}
	protected UnsagaVillagerProfession(){
//		VillagerRegistry.instance().register(merchant);
		ForgeRegistries.VILLAGER_PROFESSIONS.register(MERCHANT);
		new VillagerCareer(MERCHANT,"merchant");
		this.professions.add(MERCHANT);

//		VillagerRegistry.instance().register(magicMerchant);
		ForgeRegistries.VILLAGER_PROFESSIONS.register(MAGIC_MERCHANT);
		new VillagerCareer(MAGIC_MERCHANT,"magicMerchant");
		this.professions.add(MAGIC_MERCHANT);

//		VillagerRegistry.instance().register(unsagaSmith);
		ForgeRegistries.VILLAGER_PROFESSIONS.register(BLACKSMITH);
		new VillagerCareer(BLACKSMITH,"blackSmith");
		this.professions.add(BLACKSMITH);
	}


}
