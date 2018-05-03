package mods.hinasch.unsaga.core.client.event;

import mods.hinasch.unsaga.UnsagaMod;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EventDurabilityTooltip {

	@SubscribeEvent
	public void onTooltip(ItemTooltipEvent e){

		if(e.getItemStack().isItemStackDamageable() && UnsagaMod.configs.isEnabledDisplayToolDurability()){
			UnsagaTooltips.addTooltips(e.getItemStack(), e.getToolTip(), e.getFlags(), UnsagaTooltips.Type.DURABILITY);
		}


	}
}
