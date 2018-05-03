package mods.hinasch.unsaga.core.event;

import mods.hinasch.unsaga.ability.AbilityRegistry;
import mods.hinasch.unsaga.ability.specialmove.EventSprintTimer;
import mods.hinasch.unsaga.core.potion.UnsagaPotions;
import mods.hinasch.unsaga.core.world.chunk.UnsagaChunkCapability;
import mods.hinasch.unsaga.lp.LifePoint;
import mods.hinasch.unsaga.lp.event.EventDecrLPHurtInterval;
import mods.hinasch.unsaga.lp.event.EventLPRestore;
import mods.hinasch.unsaga.util.LivingHelper;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class LivingUpdateEventsUnsaga {

	@SubscribeEvent
	public void onLivingUpdate(LivingUpdateEvent e){
//		UnsagaMod.logger.trace("livingupdate", "called");
		UnsagaPotions.onLivingUpdate(e); //ぽーしょん
		EventSprintTimer.onLivingUpdate(e); //スプリント情報
		EventLPRestore.onLivingUpdate(e); //LP回復
		EventDecrLPHurtInterval.update(e); //LPダメージ表示時間
		LifePoint.onUpdate(e.getEntityLiving()); //自然回復
		LivingHelper.onLivingUpdate(e); //ステイト
		UnsagaChunkCapability.onLivingUpdate(e); //五行相平衡
		AbilityRegistry.onLivingUpdate(e); //パッシブアビリティ
	}
}
