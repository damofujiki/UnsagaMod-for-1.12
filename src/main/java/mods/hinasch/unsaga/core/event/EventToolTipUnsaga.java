package mods.hinasch.unsaga.core.event;

import java.util.Collections;
import java.util.List;

import com.google.common.collect.Lists;

import mods.hinasch.unsaga.common.tool.IComponentDisplayInfo;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EventToolTipUnsaga {



	public static List<IComponentDisplayInfo> list = Lists.newArrayList();
	@SubscribeEvent
	public void toolTipHooks(ItemTooltipEvent ev){


		Collections.sort(list);

		for(IComponentDisplayInfo comp:list){
			if(comp.predicate(ev.getItemStack(), ev.getEntityPlayer(), ev.getToolTip(), false)){
				comp.addInfo(ev.getItemStack(), ev.getEntityPlayer(), ev.getToolTip(), false);
			}
		}

	}
}
