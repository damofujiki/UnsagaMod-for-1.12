package mods.hinasch.unsaga.common.specialaction.option;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;

public class TargetOptionItem extends TargetOption{

	public TargetOptionItem(String name) {
		super(name);
		// TODO 自動生成されたコンストラクター・スタブ
	}

	public EntityEquipmentSlot getEquipmentSlot(){
		if(this==TargetOptions.OFF_HAND){
			return EntityEquipmentSlot.OFFHAND;
		}
		if(this==TargetOptions.HEAD){
			return EntityEquipmentSlot.HEAD;
		}
		if(this==TargetOptions.BODY){
			return EntityEquipmentSlot.CHEST;
		}
		if(this==TargetOptions.LEGS){
			return EntityEquipmentSlot.LEGS;
		}
		if(this==TargetOptions.FEET){
			return EntityEquipmentSlot.FEET;
		}
		return EntityEquipmentSlot.OFFHAND;
	}
	public ItemStack getStackFromLiving(EntityLivingBase living){
		return living.getItemStackFromSlot(getEquipmentSlot());
	}
}
