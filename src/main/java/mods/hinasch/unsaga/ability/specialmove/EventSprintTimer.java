package mods.hinasch.unsaga.ability.specialmove;

import mods.hinasch.unsaga.util.LivingHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;

public class EventSprintTimer {

//	static Map<EntityLivingBase,Integer> map = Maps.newHashMap();

	public static int getSprintTimer(EntityLivingBase ep){
		return LivingHelper.ADAPTER.hasCapability(ep) ? LivingHelper.ADAPTER.getCapability(ep).getSprintTimer() : 0;
	}
	public static void resetTimer(EntityLivingBase ep){
		LivingHelper.ADAPTER.getCapability(ep).setSprintTimer(0);
	}

	public static void onLivingUpdate(LivingUpdateEvent e){
		if(e.getEntityLiving() instanceof EntityPlayer){
			if(e.getEntityLiving().isSprinting()){
				int sprint = LivingHelper.ADAPTER.getCapability(e.getEntityLiving()).getSprintTimer();

				sprint ++;
//				UnsagaMod.logger.trace("spritn", sprint);
				if(sprint>100){
					sprint = 100;
				}
				LivingHelper.ADAPTER.getCapability(e.getEntityLiving()).setSprintTimer(sprint);
			}else{
				int sprint = LivingHelper.ADAPTER.getCapability(e.getEntityLiving()).getSprintTimer();

				sprint --;
//				UnsagaMod.logger.trace("spritn", sprint);
				if(sprint<0){
					sprint = 0;

				}
				LivingHelper.ADAPTER.getCapability(e.getEntityLiving()).setSprintTimer(sprint);
			}
		}

	}
}
