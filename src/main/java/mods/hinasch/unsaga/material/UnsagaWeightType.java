package mods.hinasch.unsaga.material;

import com.google.common.collect.Lists;

import mods.hinasch.lib.item.ItemUtil;
import mods.hinasch.unsaga.common.tool.ComponentUnsagaWeapon;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemArmor;

public enum UnsagaWeightType {




	HEAVY,LIGHT;

	public String getRegitryName(){
		return this.getName().toLowerCase();
	}
	public String getName(){
		return this == HEAVY ? "Heavy" : "Light";
	}

	public static final int THRESHOLD_ARMOR = 10;

	public static UnsagaWeightType fromWeight(int weight){
		if(ComponentUnsagaWeapon.THRESHOLD_WEIGHT<weight){
			return HEAVY;
		}
		return LIGHT;
	}

	public static UnsagaWeightType fromString(String string){
		if(string.equals("heavy")){
			return HEAVY;
		}
		return LIGHT;
	}

	public static UnsagaWeightType calcArmorWeightType(EntityPlayer ep){
		if(calcArmorWeight(ep)>THRESHOLD_ARMOR){
			return HEAVY;
		}
		return LIGHT;
	}
	public static int calcArmorWeight(EntityPlayer ep){
		int weight = Lists.newArrayList(ep.getArmorInventoryList()).stream().mapToInt(in ->{
			if(ItemUtil.isItemStackNull(in)){
				return 0;
			}
			if(UnsagaMaterialCapability.adapter.hasCapability(in)){
				return UnsagaMaterialCapability.adapter.getCapability(in).getMaterial().weight;
			}
			if(in.getItem() instanceof ItemArmor){
				ItemArmor armor = (ItemArmor) in.getItem();
				switch(armor.getArmorMaterial()){
				case CHAIN:
					return UnsagaMaterials.SILVER.weight;
				case DIAMOND:
					return UnsagaMaterials.DIAMOND.weight;
				case GOLD:
					return UnsagaMaterials.GOLD.weight;
				case IRON:
					return UnsagaMaterials.IRON.weight;
				case LEATHER:
					return UnsagaMaterials.CROCODILE_LEATHER.weight;
				default:
					break;

				}
			}
			return UnsagaMaterials.IRON.weight;
		}).sum();

		return weight;
	}
}
