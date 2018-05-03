package mods.hinasch.unsaga.core.event;

import mods.hinasch.unsaga.core.potion.PotionGravity;
import mods.hinasch.unsaga.core.potion.LivingState;
import mods.hinasch.unsaga.skillpanel.EventSkillPanel;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EventCancelFalling {

	@SubscribeEvent
	public void onFall(LivingFallEvent e){
		LivingState.onFall(e);
		PotionGravity.onFall(e);
		EventSkillPanel.onFall(e);
	}
}
