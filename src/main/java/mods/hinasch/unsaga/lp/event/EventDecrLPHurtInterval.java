package mods.hinasch.unsaga.lp.event;

import mods.hinasch.unsaga.UnsagaMod;
import mods.hinasch.unsaga.lp.LifePoint;
import mods.hinasch.unsaga.lp.LifePoint.ILifePoint;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;

public class EventDecrLPHurtInterval{

	public static void update(LivingUpdateEvent e) {
		if(UnsagaMod.configs.isEnabledLifePointSystem()){
			if(LifePoint.adapter.hasCapability(e.getEntityLiving())){
				ILifePoint capa = LifePoint.adapter.getCapability(e.getEntityLiving());
				if(capa.getHurtInterval()>0){
					if(e.getEntityLiving().ticksExisted % 20 * 12 == 0){
						capa.setHurtInterval(capa.getHurtInterval() -1);
					}
				}
			}
		}
	}


}
