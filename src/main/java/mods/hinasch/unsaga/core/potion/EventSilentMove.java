package mods.hinasch.unsaga.core.potion;

import mods.hinasch.unsaga.skillpanel.SkillPanel;
import mods.hinasch.unsaga.skillpanel.SkillPanelAPI;
import mods.hinasch.unsaga.skillpanel.SkillPanels;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EventSilentMove {

	@SubscribeEvent
	public void modifierVisibility(PlayerEvent.Visibility ev){
		EntityPlayer ep = ev.getEntityPlayer();
		SkillPanel incospicious = SkillPanels.INCONSPICIOUS;
		Potion silentMove = UnsagaPotions.SILENT_MOVE;
		double modifier = 1.0D;
		if(ep.isPotionActive(silentMove)){
			if(SkillPanelAPI.hasPanel(ep, incospicious)){
				int level = SkillPanelAPI.getHighestPanelLevel(ep, incospicious).getAsInt();
				modifier = 0.2D - (0.04D*level);
			}else{
				modifier = 0.2D;
			}


		}else{
			if(SkillPanelAPI.hasPanel(ep, SkillPanels.INCONSPICIOUS)){
				modifier = 0.8D;
			}
		}
		ev.modifyVisibility(MathHelper.clamp(modifier, 0.01D, 2.0D));
	}
}
