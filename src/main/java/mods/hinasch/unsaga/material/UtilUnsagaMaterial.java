package mods.hinasch.unsaga.material;

import mods.hinasch.unsaga.UnsagaMod;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.EnumHelper;

public class UtilUnsagaMaterial {


	public static ToolMaterial addToolMaterial(String name,int harvestLevel,int maxUses,float efficiency,float damage,int enchantability){
		return EnumHelper.addToolMaterial(UnsagaMod.MODID+name, harvestLevel, maxUses, efficiency, damage, enchantability);
	}

	public static ArmorMaterial addArmorMaterial(String name,int armor,int[] reduces,int enchant,int... toughIn){
		int toughness = toughIn.length>0 ? toughIn[0] : 0;
		ResourceLocation res = new ResourceLocation(name);
		return EnumHelper.addArmorMaterial(UnsagaMod.MODID+"."+name, "testarmortex",armor, reduces, enchant,SoundEvents.ITEM_ARMOR_EQUIP_GENERIC,toughness);

	}

	public static ToolMaterial getVanilla(String name,ToolMaterial tool,Float... objs){
		int hl = objs[0]<0 ? tool.getHarvestLevel() : objs[0].intValue();
		int maxuse = objs[1]<0 ? tool.getMaxUses() : objs[1].intValue();
		float ef = objs[2]<0 ? tool.getEfficiency() : objs[2];
		float vs = objs[3]<0 ? tool.getAttackDamage() : objs[3];
		int enchant =objs[4]<0 ? tool.getEnchantability() : objs[4].intValue();

		return EnumHelper.addToolMaterial(name, hl, maxuse, ef, vs, enchant);
	}

	public static ArmorMaterial getVanilla(String name,String tex,ArmorMaterial armor,int factor,int enchant,float tough){

		int[] arra = new int[]{armor.getDamageReductionAmount(EntityEquipmentSlot.HEAD),armor.getDamageReductionAmount(EntityEquipmentSlot.CHEST)
				,armor.getDamageReductionAmount(EntityEquipmentSlot.LEGS),armor.getDamageReductionAmount(EntityEquipmentSlot.FEET)};
		int enc = enchant < 0 ? armor.getEnchantability() : enchant;
		float t = tough <0 ? armor.getToughness() : tough;
		return EnumHelper.addArmorMaterial(name, tex, factor, arra, enchant, armor.getSoundEvent(),t);
	}
}
