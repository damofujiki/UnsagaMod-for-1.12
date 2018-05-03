package mods.hinasch.unsaga.villager.bartering;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import mods.hinasch.lib.iface.IIntSerializable;
import mods.hinasch.lib.util.HSLibs;
import mods.hinasch.unsaga.material.UnsagaMaterial;
import mods.hinasch.unsaga.material.UnsagaMaterialRegistry;
import mods.hinasch.unsaga.material.UnsagaMaterials;
import mods.hinasch.unsaga.util.ToolCategory;
import mods.hinasch.unsaga.villager.UnsagaVillagerProfession;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.util.WeightedRandom;
import net.minecraft.village.Village;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;

public enum BarteringShopType implements IIntSerializable{

	UNKNOWN(0),ALL(1),MAGIC(2),SNOWY_VILLAGE(3),TROPICAL_VILLAGE(4),PORTTOWN(5),WANDA(6);

	final int meta;

	private BarteringShopType(int par1){
		this.meta = par1;

	}


	public EnumSet<BarteringMaterialCategory.Type> getAvailableTypes(){
		EnumSet set;
		switch(this){
		case ALL:
			return BarteringMaterialCategory.instance().merchandises;
		case MAGIC:
			return EnumSet.of(BarteringMaterialCategory.Type.BESTIAL);
		case PORTTOWN:
			set = BarteringMaterialCategory.instance().merchandises.clone();
			set.remove(BarteringMaterialCategory.Type.FEATHER);
			set.remove(BarteringMaterialCategory.Type.BONE);
			return set;
		case SNOWY_VILLAGE:
			set = BarteringMaterialCategory.instance().merchandises.clone();
			set.remove(BarteringMaterialCategory.Type.SCALE);
			set.remove(BarteringMaterialCategory.Type.METAL);
			set.remove(BarteringMaterialCategory.Type.METAL_PRECIOUS);
			return set;
		case TROPICAL_VILLAGE:
			set = EnumSet.noneOf(BarteringMaterialCategory.Type.class);
			set.add(BarteringMaterialCategory.Type.CLOTH);
			set.add(BarteringMaterialCategory.Type.LEATHER);
			set.add(BarteringMaterialCategory.Type.WOOD);
			set.add(BarteringMaterialCategory.Type.BONE);
			set.add(BarteringMaterialCategory.Type.SCALE);
			return set;
		case UNKNOWN:
			break;
		case WANDA:
			set = EnumSet.noneOf(BarteringMaterialCategory.Type.class);
			set.add(BarteringMaterialCategory.Type.CLOTH);
			set.add(BarteringMaterialCategory.Type.LEATHER);
			set.add(BarteringMaterialCategory.Type.WOOD);
			set.add(BarteringMaterialCategory.Type.BONE);
			set.add(BarteringMaterialCategory.Type.METAL);
			set.add(BarteringMaterialCategory.Type.METAL_PRECIOUS);
			return set;
		default:
			break;

		}
		return EnumSet.of(BarteringMaterialCategory.Type.BONE);
	}
	public Set<UnsagaMaterial> getAvailableMerchandiseMaterials(){
		return this.getAvailableTypes().stream().flatMap(in -> {
			Set<UnsagaMaterial> set = Sets.newHashSet();
			if(BarteringMaterialCategory.instance().getMaterialsFromType(in)!=null){
				set.addAll(BarteringMaterialCategory.instance().getMaterialsFromType(in));
				if(this==MAGIC){
					set.remove(UnsagaMaterials.SERPENTINE);
				}

			}
			return set.stream();
		}).filter(in -> UnsagaMaterialRegistry.instance().merchandiseMaterial.contains(in)).collect(Collectors.toSet());
//		UnsagaMaterials m = UnsagaMaterials.instance();
//		Set<UnsagaMaterial> set;
//		switch(this){
//		case ALL:
//			return Sets.newHashSet(m.merchandiseMaterial);
//		case MAGIC:
//			return Sets.newHashSet(BarteringMaterialCategory.Type.BESTIAL.getMaterials());
//		case PORTTOWN:
//			set = Sets.newHashSet(m.merchandiseMaterial);
//			set.remove(m.feather);
//			set.removeAll(BarteringMaterialCategory.Type.BONE.getMaterials());
//			return set;
//		case SNOWY_VILLAGE:
//			set = Sets.newHashSet(m.merchandiseMaterial);
//			set.remove(m.feather);
//			set.removeAll(BarteringMaterialCategory.Type.SCALE.getMaterials());
//			set.removeAll(BarteringMaterialCategory.Type.METAL.getMaterials());
//			set.removeAll(BarteringMaterialCategory.Type.METAL_PRECIOUS.getMaterials());
//			return set;
//		case TROPICAL_VILLAGE:
//			set = Sets.newHashSet();
//			set.add(m.feather);
//			set.addAll(BarteringMaterialCategory.Type.CLOTH.getMaterials());
//			set.addAll(BarteringMaterialCategory.Type.LEATHER.getMaterials());
//			set.addAll(BarteringMaterialCategory.Type.WOOD.getMaterials());
//			set.addAll(BarteringMaterialCategory.Type.BONE.getMaterials());
//			set.addAll(BarteringMaterialCategory.Type.SCALE.getMaterials());
//			return set;
//		case UNKNOWN:
//			break;
//		case WANDA:
//			set = Sets.newHashSet();
//			set.addAll(BarteringMaterialCategory.Type.CLOTH.getMaterials());
//			set.addAll(BarteringMaterialCategory.Type.LEATHER.getMaterials());
//			set.addAll(BarteringMaterialCategory.Type.WOOD.getMaterials());
//			set.addAll(BarteringMaterialCategory.Type.BONE.getMaterials());
//			set.addAll(BarteringMaterialCategory.Type.METAL.getMaterials());
//			set.addAll(BarteringMaterialCategory.Type.METAL_PRECIOUS.getMaterials());
//			return set;
//		default:
//			break;
//
//
//		}
//		return Sets.newHashSet(m.merchandiseMaterial);
	}
	public Set<ToolCategory> getAvailableMerchandiseCategory(){
		Set set;
		switch(this){
		case ALL:
			return ToolCategory.merchandiseSet;
		case MAGIC:
			return Sets.newHashSet(ToolCategory.KNIFE,ToolCategory.STAFF,ToolCategory.ACCESSORY);
		case PORTTOWN:
			set = Sets.newHashSet();
			set.addAll(ToolCategory.merchandiseSet);
			set.remove(ToolCategory.RAW_MATERIAL);
			set.remove(ToolCategory.ACCESSORY);
			return set;
		case SNOWY_VILLAGE:
			return Sets.newHashSet(ToolCategory.RAW_MATERIAL,ToolCategory.SWORD,ToolCategory.STAFF
					,ToolCategory.BOW,ToolCategory.ACCESSORY,ToolCategory.HELMET);
		case TROPICAL_VILLAGE:
			return Sets.newHashSet(ToolCategory.KNIFE,ToolCategory.AXE,ToolCategory.SPEAR,ToolCategory.ACCESSORY);
		case WANDA:
			set = Sets.newHashSet();
			set.addAll(ToolCategory.merchandiseSet);
			set.remove(ToolCategory.RAW_MATERIAL);
			set.remove(ToolCategory.STAFF);
			set.remove(ToolCategory.SPEAR);
			return set;
		default:
			break;

		}
		return ToolCategory.merchandiseSet;

	}



	@Override
	public int getMeta() {
		// TODO 自動生成されたメソッド・スタブ
		return meta;
	}

	public static BarteringShopType fromMeta(int meta){
		return HSLibs.fromMeta(BarteringShopType.values(), meta);
	}

	public static BarteringShopType decideBarteringType(World world,EntityVillager villager){
		List<WeightedRandomType> list = Lists.newArrayList();
		Biome biome = world.getBiome(villager.getPosition());
		if(villager.getProfessionForge()==UnsagaVillagerProfession.instance().MAGIC_MERCHANT){
			return BarteringShopType.MAGIC;
		}
		if(world.getVillageCollection()!=null && world.getVillageCollection().getNearestVillage(villager.getPosition(), 32)!=null){
			Village village = world.getVillageCollection().getNearestVillage(villager.getPosition(), 32);
			if(village.getNumVillagers()>15){
				list.add(new WeightedRandomType(10,BarteringShopType.PORTTOWN));
			}else{
				list.add(new WeightedRandomType(4,BarteringShopType.PORTTOWN));
			}
			if(village.getNumVillagers()>30){
				list.add(new WeightedRandomType(10,BarteringShopType.ALL));
			}else{
				list.add(new WeightedRandomType(2,BarteringShopType.ALL));
			}

		}else{
			list.add(new WeightedRandomType(2,BarteringShopType.PORTTOWN));
			list.add(new WeightedRandomType(1,BarteringShopType.ALL));
		}

		if(!BiomeDictionary.hasType(biome, BiomeDictionary.Type.SANDY)){
			list.add(new WeightedRandomType(10,BarteringShopType.TROPICAL_VILLAGE));
		}else{
			list.add(new WeightedRandomType(5,BarteringShopType.TROPICAL_VILLAGE));
		}
		if(BiomeDictionary.hasType(biome, BiomeDictionary.Type.COLD)){
			list.add(new WeightedRandomType(10,BarteringShopType.SNOWY_VILLAGE));
		}else{
			list.add(new WeightedRandomType(5,BarteringShopType.SNOWY_VILLAGE));
		}
		list.add(new WeightedRandomType(5,BarteringShopType.WANDA));
		return WeightedRandom.getRandomItem(world.rand, list).type;
	}

	public static class WeightedRandomType extends WeightedRandom.Item{

		public BarteringShopType type;
		public WeightedRandomType(int itemWeightIn,BarteringShopType type) {
			super(itemWeightIn);
			this.type =type;
			// TODO 自動生成されたコンストラクター・スタブ
		}

	}
}
