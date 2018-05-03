package mods.hinasch.unsaga.ability;

import java.util.UUID;

import mods.hinasch.lib.entity.ModifierHelper;
import mods.hinasch.lib.util.Statics;
import mods.hinasch.unsaga.UnsagaMod;
import mods.hinasch.unsaga.core.inventory.AccessorySlotCapability;
import mods.hinasch.unsaga.material.UnsagaMaterialCapability;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EventRefleshAbilityModifier {

	public static final UUID ID_ACCESSORY_ARMOR = UUID.fromString("d842988e-470c-47f1-bb41-c94d22d03a09");
	@SubscribeEvent
	public void onEquipChanged(LivingEquipmentChangeEvent ev){
		this.refresh(ev.getEntityLiving());
	}


	public static void refresh(EntityLivingBase el){
//		EntityLivingBase el = ev.getEntityLiving();
		if(el instanceof EntityPlayer){
			AbilityRegistry.instance().getModifierBaseMaps().entrySet().forEach(entry ->{

				double d = AbilityAPI.getAllAbilityModifiers(el).stream().filter(in -> in.getFirst()==entry.getKey())
						.mapToDouble(in -> in.getSecond()).peek(in -> UnsagaMod.logger.trace(entry.getKey().getName(), in)).sum();

//				if(d>0.0F){
					AttributeModifier mod = new AttributeModifier(entry.getValue().getID(),entry.getValue().getName(),d,entry.getValue().getOperation());
					UnsagaMod.logger.trace("modifier", "apply",mod);
					ModifierHelper.refleshModifier(el, entry.getKey(), mod);
//				}

			});

			if(AccessorySlotCapability.adapter.hasCapability(el)){
				int armor = AccessorySlotCapability.adapter.getCapability(el).getEquippedList().stream().filter(in -> !in.isEmpty() && UnsagaMaterialCapability.adapter.hasCapability(in))
						.mapToInt(in -> AbilityAssociateRegistry.instance().getAbilityArmorModifier(UnsagaMaterialCapability.adapter.getCapability(in).getMaterial())).sum();
				ModifierHelper.refleshModifier(el, SharedMonsterAttributes.ARMOR,new AttributeModifier(ID_ACCESSORY_ARMOR,UnsagaMod.MODID+".accessory_armor",(double)armor,Statics.OPERATION_INCREMENT));
			}
			UnsagaMod.logger.trace("changed", "called");


//			AttributeModifier healModifier = new AttributeModifier(UUID.fromString("562a1c22-6d62-423a-9cc2-55fc15ba6525"), "healAttribute", -amount * 1.5D, Statics.OPERATION_INCREMENT);
//			ModifierHelper.refleshModifier(el, AdditionalStatus.NATURAL_HEAL_SPEED, healModifier);

		}
	}
}
