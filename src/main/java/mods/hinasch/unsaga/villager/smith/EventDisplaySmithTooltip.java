package mods.hinasch.unsaga.villager.smith;

import mods.hinasch.lib.client.ClientHelper;
import mods.hinasch.lib.item.ItemUtil;
import mods.hinasch.lib.util.HSLibs;
import mods.hinasch.unsaga.core.client.gui.GuiBlacksmithUnsaga;
import mods.hinasch.unsaga.material.MaterialItemAssociatings;
import mods.hinasch.unsaga.material.SuitableLists;
import mods.hinasch.unsaga.material.UnsagaMaterial;
import mods.hinasch.unsaga.material.UnsagaMaterialCapability;
import mods.hinasch.unsaga.util.ToolCategory;
import mods.hinasch.unsaga.util.UnsagaTextFormatting;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * TIPSへの表示
 */
public class EventDisplaySmithTooltip {


	@SubscribeEvent
	public void onDisplayToolTip(ItemTooltipEvent ev){
		ItemStack stack = ev.getItemStack();
		if(ClientHelper.getCurrentGui() instanceof GuiBlacksmithUnsaga){
			GuiBlacksmithUnsaga gui = (GuiBlacksmithUnsaga) ClientHelper.getCurrentGui();
			ToolCategory selectedCategory = ToolCategory.toolArray.get(gui.getCurrentCategory());
			if(ItemUtil.isItemStackPresent(stack)){
				UnsagaMaterial material = null;
				if(MaterialItemAssociatings.instance().getMaterialFromStack(stack).isPresent()){
					material = MaterialItemAssociatings.instance().getMaterialFromStack(stack).get();
				}
				if(SmithMaterialRegistry.instance().find(stack).isPresent()){
					material = SmithMaterialRegistry.instance().find(stack).get().getMaterial();
				}
				if(UnsagaMaterialCapability.adapter.hasCapability(stack)){

					material = UnsagaMaterialCapability.adapter.getCapability(stack).getMaterial();

				}

				if(material!=null){
					ev.getToolTip().add(UnsagaTextFormatting.SIGNIFICANT+HSLibs.translateKey("gui.unsaga.smith.materialInfo", material.getLocalized()));
					if(SuitableLists.instance().getSuitables(selectedCategory).contains(material)){
						ev.getToolTip().add(UnsagaTextFormatting.SIGNIFICANT+HSLibs.translateKey("gui.unsaga.smith.suitableInfo", selectedCategory.getLocalized()));
					}

				}

				if(ValidPaymentRegistry.getValue(stack).isPresent()){
					ev.getToolTip().add(UnsagaTextFormatting.SIGNIFICANT+HSLibs.translateKey("gui.unsaga.smith.paymentInfo"));
				}
			}
		}

	}
}
