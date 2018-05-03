package mods.hinasch.unsaga.core.advancement;

import mods.hinasch.unsaga.UnsagaMod;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.entity.player.AdvancementEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EventUnsagaAdvancement {


	public static final ResourceLocation GET_TABLET = new ResourceLocation(UnsagaMod.MODID,"unsaga/get_tablet");
	public static final ResourceLocation RECIPE_DECIPHERING_TABLE = new ResourceLocation(UnsagaMod.MODID,"deciphering_table");

	@SubscribeEvent
	public void onTriggerAdvancement(AdvancementEvent e){
		UnsagaMod.logger.trace("advancement", e.getAdvancement().getId());
		if(this.isResourceEqual(e.getAdvancement().getId(), GET_TABLET)){
			e.getEntityPlayer().unlockRecipes(new ResourceLocation[]{RECIPE_DECIPHERING_TABLE});
		}




	}

	public boolean isResourceEqual(ResourceLocation a,ResourceLocation b){
		if(a.getResourceDomain().equals(b.getResourceDomain())){
			return a.getResourcePath().equals(b.getResourcePath());
		}
		return false;
	}
}
