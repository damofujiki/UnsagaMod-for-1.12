package mods.hinasch.unsaga.minsaga;

import mods.hinasch.lib.item.ItemDamageEvent;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EventItemToughness {

	@SubscribeEvent
	public void onItemDamage(ItemDamageEvent e){
		if(e.getPlayer()!=null){
			if(MinsagaForgeCapability.ADAPTER.hasCapability(e.getStack()) && MinsagaForgeCapability.ADAPTER.getCapability(e.getStack()).hasForged()){
				int modifier = Math.abs(MinsagaForgeCapability.ADAPTER.getCapability(e.getStack()).getDurabilityModifier());
				boolean isNegate = MinsagaForgeCapability.ADAPTER.getCapability(e.getStack()).getDurabilityModifier() >0;
				float prob = 0.15F * modifier;
				prob = MathHelper.clamp(prob, 0, 0.80F);
				int amount = e.getDamage();
				int amountmod = 0;
				if(modifier>0){
					for(int i=0;i<amount;i++){
						if(e.rand().nextFloat()<prob){
							if(isNegate){
								amountmod ++;
							}else{
								amountmod --;
							}
						}
					}


					amount -= amountmod;

					//マイナスになるとダメージが回復するので０でマイナスは足切りする
					e.setDamage(MathHelper.clamp(amount, 0, amount));

				}



			}
		}
	}
}
