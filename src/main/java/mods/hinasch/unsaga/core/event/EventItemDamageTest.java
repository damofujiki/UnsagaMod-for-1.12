package mods.hinasch.unsaga.core.event;

import mods.hinasch.lib.item.ItemDamageEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EventItemDamageTest {

	@SubscribeEvent
	public void onItemDamage(ItemDamageEvent e){


//		e.setCanceled(true);
//		ItemStack stack = e.damageStack;
//		int amount = e.damage;
//		EntityPlayerMP damager = e.damager;
//		UnsagaMod.logger.trace(this.getClass().getName(), "called");
//        if (!stack.isItemStackDamageable())
//        {
//            return ;
//        }
//        else
//        {
//            if (amount > 0)
//            {
//                int i = EnchantmentHelper.getEnchantmentLevel(Enchantments.UNBREAKING, stack);
//                int j = 0;
//
//                for (int k = 0; i > 0 && k < amount; ++k)
//                {
//                    if (EnchantmentDurability.negateDamage(this, i, rand))
//                    {
//                        ++j;
//                    }
//                }
//
//                amount -= j;
//
//                if (amount <= 0)
//                {
//                    return false;
//                }
//            }
//
//            if (damager != null && amount != 0)
//            {
//                CriteriaTriggers.ITEM_DURABILITY_CHANGED.trigger(damager, this, stack.itemDamage + amount);
//            }
//
//            stack.setItemDamage(stack.getItemDamage() + amount); //Redirect through Item's callback if applicable.
////            return stack.getItemDamage() > getMaxDamage();
//        }
	}
}
