package mods.hinasch.unsaga.minsaga;

import mods.hinasch.lib.core.HSLib;
import mods.hinasch.lib.item.ItemUtil;
import mods.hinasch.unsaga.minsaga.MinsagaForgeCapability.IMinsagaForge;
import mods.hinasch.unsaga.minsaga.MinsagaForgeCapability.MaterialLayer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

public class EventFittingProgressMinsaga{



	public static boolean apply(LivingHurtEvent e) {
		if(e.getSource().getTrueSource() instanceof EntityLivingBase){
			EntityLivingBase el = (EntityLivingBase) e.getSource().getTrueSource();
			return ItemUtil.isItemStackPresent(el.getHeldItemMainhand())
					&& MinsagaForgeCapability.ADAPTER.hasCapability(el.getHeldItemMainhand());
		}

		return false;
	}


	public static void process(LivingHurtEvent e) {

		EntityLivingBase el = (EntityLivingBase) e.getSource().getTrueSource();
		ItemStack stack = el.getHeldItemMainhand();

		IMinsagaForge capa = MinsagaForgeCapability.ADAPTER.getCapability(stack);
		if(capa.hasForged() && capa.getCurrentLayer()!=null){
			MaterialLayer attribute = capa.getCurrentLayer();
			if(attribute.getFittingProgress()<attribute.getMaxFittingProgress()){
				attribute.setFittingProgress(attribute.getFittingProgress()+ (HSLib.configHandler.isDebug() ? 50 : 1));
			}

//			MinsagaUtil.damageToolProcess(el,stack,capa.getDurabilityModifier(),el.getRNG());
		}

	}



}
