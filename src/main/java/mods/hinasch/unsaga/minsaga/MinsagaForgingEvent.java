package mods.hinasch.unsaga.minsaga;

import mods.hinasch.lib.core.HSLibEvents;
import mods.hinasch.lib.util.HSLibs;
import mods.hinasch.unsaga.material.RawMaterialRegistry;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;



public class MinsagaForgingEvent {


	public static enum Type{
		WEAPON,ARMOR;
	}

	public static void registerEvents(){

		OreDictionary.registerOre("hardShell", RawMaterialRegistry.instance().chitin.getItemStack(1));
		OreDictionary.registerOre("hardShell", new ItemStack(Items.SHULKER_SHELL));
		HSLibs.registerEvent(new EventTooltipMinsaga());
		HSLibs.registerEvent(new EventRefleshMinsagaForged());

//		HSLib.events.livingHurt.getEventsMiddle().add(new EventFittingProgressMinsaga());

//		HSLibs.registerEvent(new EventApplyForgedAbility());
		HSLibs.registerEvent(new EventHarvestPlus());
		HSLibs.registerEvent(new EventItemUseFinished());
		/**
		 * かなとこでの修理
		 */
		HSLibs.registerEvent(new EventAnvil());

		/**
		 * 採掘速度の実装
		 */
		HSLibEvents.breakSpeed.addEvent(ev -> {
			ItemStack is = ev.getEntityPlayer().getHeldItemMainhand();
			if(is!=null && MinsagaForgeCapability.ADAPTER.hasCapability(is)){
				float speed = ev.getOriginalSpeed() + MinsagaForgeCapability.ADAPTER.getCapability(is).getEfficiencyModifier();
				if(ev.getEntityPlayer().isInWater() && MinsagaUtil.getAbilities(ev.getEntityPlayer()).contains(MinsagaForging.Ability.SEA)){
					speed *= 2.0F;
				}
				ev.setNewSpeed(speed);
			}
		});

//		/**
//		 * 強度の実装
//		 */
//		EventSaveDamage.events.add(living ->{
//			if(living instanceof EntityPlayer){
//				EntityPlayer ep = (EntityPlayer) living;
//				World world = ep.getEntityWorld();
//				ItemStack is = living.getHeldItemMainhand();
//				if(is!=null && MinsagaForgeCapability.ADAPTER.hasCapability(is)){
//					MinsagaUtil.damageToolProcess(ep,is,MinsagaForgeCapability.ADAPTER.getCapability(is).getDurabilityModifier(),ep.getRNG());
//				}
//
//			}
//		});
		//		LivingHelper.registerEquipmentsChangedEvent(living -> {
		//
		//		});
	}
}
