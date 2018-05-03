package mods.hinasch.unsaga.core.potion.old;

import mods.hinasch.lib.item.ItemUtil;
import mods.hinasch.lib.world.WorldHelper;
import mods.hinasch.lib.world.XYZPos;
import mods.hinasch.unsaga.core.potion.UnsagaPotions;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EventTouchGold {

	@SubscribeEvent
	public void onLivingDeath(LivingDeathEvent e){
		Potion goldFinger = UnsagaPotions.instance().GOLD_FINGER;
		if(e.getSource().getTrueSource() instanceof EntityLivingBase){
			EntityLivingBase attacker = (EntityLivingBase) e.getSource().getTrueSource();
			if(attacker.isPotionActive(goldFinger) && attacker.getActivePotionEffect(goldFinger)!=null){
				PotionEffect effect = attacker.getActivePotionEffect(goldFinger);

				float prob = 0.1F * (1.0F + (float)effect.getAmplifier());
				if(attacker.getRNG().nextFloat()<prob){
					if(WorldHelper.isServer(e.getEntityLiving().getEntityWorld())){
						ItemStack goldNugget = new ItemStack(Items.GOLD_NUGGET);
						ItemUtil.dropItem(attacker.getEntityWorld(), goldNugget, XYZPos.createFrom(e.getEntityLiving()));;
					}
				}
			}
		}
	}
}
