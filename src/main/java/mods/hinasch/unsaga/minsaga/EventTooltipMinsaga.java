package mods.hinasch.unsaga.minsaga;

import mods.hinasch.lib.client.ClientHelper;
import mods.hinasch.unsaga.core.client.event.UnsagaTooltips;
import mods.hinasch.unsaga.core.client.gui.GuiToolCustomizeMinsaga;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EventTooltipMinsaga {
	@SubscribeEvent
	public void onTooltip(ItemTooltipEvent e){
		ItemStack is = e.getItemStack();
		if(MinsagaForgeCapability.ADAPTER.hasCapability(is) && MinsagaForgeCapability.ADAPTER.getCapability(is).hasForged()){
			UnsagaTooltips.addTooltips(is, e.getToolTip(), e.getFlags(), UnsagaTooltips.Type.MINSAGA_MATERIALS);
		}
		if(ClientHelper.getCurrentGui() instanceof GuiToolCustomizeMinsaga){
			if(MinsagaForging.instance().getMaterialFromItemStack(is).isPresent()){
				UnsagaTooltips.addTooltips(is, e.getToolTip(), e.getFlags(), UnsagaTooltips.Type.MINSAGA_MODIFIERS);
			}
		}
	}
}
