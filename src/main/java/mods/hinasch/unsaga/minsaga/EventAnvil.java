package mods.hinasch.unsaga.minsaga;

import mods.hinasch.unsaga.UnsagaMod;
import mods.hinasch.unsaga.minsaga.MinsagaForgeCapability.IMinsagaForge;
import mods.hinasch.unsaga.minsaga.MinsagaForgeCapability.MaterialLayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EventAnvil {


	@SubscribeEvent
	public void onAnvilUpdate(AnvilUpdateEvent e){


		//		UnsagaMod.logger.trace("anvil", "called");

		if(e.getLeft().isEmpty() || e.getRight().isEmpty()){
			return;
		}

		if(MinsagaForgeCapability.ADAPTER.hasCapability(e.getLeft())){
			IMinsagaForge capa = MinsagaForgeCapability.ADAPTER.getCapability(e.getLeft());
			if(capa.hasForged()){

				MaterialLayer attribute = capa.getCurrentLayer();
				int cost = e.getCost() - capa.getCostModifier();
				//					e.setCanceled(true);

				UnsagaMod.logger.trace("material", attribute.getMaterial(),e.getRight(),e.getCost(),cost);
				if(attribute.getMaterial().isMaterialItem(e.getRight())){

					e.setCost(MathHelper.clamp(cost, 1, 256));
					int damage = e.getLeft().getItemDamage() - attribute.getMaterial().getRepairDamage();
					ItemStack newStack = e.getLeft().copy();
					newStack.setItemDamage(MathHelper.clamp(damage, 0, newStack.getMaxDamage()));
					newStack.setRepairCost(newStack.getRepairCost()+1);
					e.setOutput(newStack);

				}else{
					e.setCanceled(true);
				}
			}
		}

	}
}
