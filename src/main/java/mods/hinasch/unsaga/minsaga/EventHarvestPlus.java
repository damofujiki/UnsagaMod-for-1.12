package mods.hinasch.unsaga.minsaga;

import java.util.Set;

import mods.hinasch.lib.item.ItemUtil;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerEvent.HarvestCheck;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EventHarvestPlus {

	@SubscribeEvent
	public void onHarvestCheck(HarvestCheck e){
		ItemStack held = e.getEntityLiving().getHeldItemMainhand();
		if(ItemUtil.isItemStackPresent(held)){
			if(MinsagaForgeCapability.ADAPTER.hasCapability(held)){
				if(!MinsagaForgeCapability.ADAPTER.getCapability(held).getAbilities().contains(MinsagaForging.Ability.HARVEST)){
					return;
				}
			}
			Set<String> classes = held.getItem().getToolClasses(held);
			int blockHarvestLevel = e.getTargetBlock().getBlock().getHarvestLevel(e.getTargetBlock());
			if(classes.stream().anyMatch(in ->{
				int hl = held.getItem().getHarvestLevel(held, in, e.getEntityPlayer(), e.getTargetBlock());
				return hl + 1 >= blockHarvestLevel;
			})){
				e.setCanHarvest(true);
			}
		}



	}
}
