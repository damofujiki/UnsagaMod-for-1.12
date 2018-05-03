package mods.hinasch.unsaga.villager.bartering;

import java.util.List;
import java.util.OptionalInt;

import mods.hinasch.lib.client.ClientHelper;
import mods.hinasch.unsaga.common.tool.ComponentDisplayInfo;
import mods.hinasch.unsaga.core.client.gui.GuiBartering;
import mods.hinasch.unsaga.core.event.EventToolTipUnsaga;
import mods.hinasch.unsaga.util.UnsagaTextFormatting;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class DisplayPriceEvent {

	public static void register(){
		EventToolTipUnsaga.list.add(new ComponentDisplayInfo(8,
				(is,ep,dispList,par4)->ClientHelper.getCurrentGui() instanceof GuiBartering && MerchandiseCapability.adapter.hasCapability(is)){

			@Override
			public void addInfo(ItemStack stack, EntityPlayer ep, List dispList, boolean par4) {
				if(MerchandiseCapability.adapter.getCapability(stack).canSell(stack)){
					GuiBartering gui = (GuiBartering) ClientHelper.getCurrentGui();
					OptionalInt originalPrice = OptionalInt.empty();
					gui.setMerchandiseTag();
					int price = MerchandiseCapability.adapter.getCapability(stack).getPrice(stack);
					if(MerchandiseCapability.adapter.getCapability(stack).isMerchadise()){
						price *= 2;
					}

					originalPrice = OptionalInt.of(price);
					price = BarteringUtil.getDiscountPrice(price, gui.getDiscount());
					if(price!=originalPrice.getAsInt() && MerchandiseCapability.adapter.getCapability(stack).isMerchadise()){
						dispList.add(UnsagaTextFormatting.SIGNIFICANT+String.format("Price:%d(%d)",price,originalPrice.getAsInt()));
					}else{
						dispList.add(UnsagaTextFormatting.SIGNIFICANT+"Price:"+price);
					}

				}

			}}
		);
	}
}
