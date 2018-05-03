package mods.hinasch.unsaga;

import mods.hinasch.lib.config.EventConfigChanged;
import mods.hinasch.lib.util.HSLibs;
import mods.hinasch.unsaga.core.advancement.EventUnsagaAdvancement;
import mods.hinasch.unsaga.core.client.event.EventDurabilityTooltip;
import mods.hinasch.unsaga.core.entity.projectile.custom_arrow.CustomArrowEvents;
import mods.hinasch.unsaga.core.event.DamageEventsUnsaga;
import mods.hinasch.unsaga.core.event.EntityJoinWorldEventsUnsaga;
import mods.hinasch.unsaga.core.event.EventCancelFalling;
import mods.hinasch.unsaga.core.event.EventReplaceFoodStats;
import mods.hinasch.unsaga.core.event.EventToolTipUnsaga;
import mods.hinasch.unsaga.core.event.LivingUpdateEventsUnsaga;
import mods.hinasch.unsaga.core.event.UnsagaMobDrops;
import mods.hinasch.unsaga.init.UnsagaItems;
import mods.hinasch.unsaga.status.UnsagaStatus;
import mods.hinasch.unsaga.villager.UnsagaVillagerInteraction;

public class UnsagaModEvents {

	private static UnsagaModEvents INSTANCE;
	public static UnsagaModEvents instance(){
		if(INSTANCE==null){
			INSTANCE = new UnsagaModEvents();
		}
		return INSTANCE;
	}

	public void regiser(){



		HSLibs.registerEvent(new CustomArrowEvents());
		HSLibs.registerEvent(new DamageEventsUnsaga());
		HSLibs.registerEvent(new LivingUpdateEventsUnsaga());
		HSLibs.registerEvent(new EntityJoinWorldEventsUnsaga());
		HSLibs.registerEvent(new EventCancelFalling());
		HSLibs.registerEvent(new EventUnsagaAdvancement());
		HSLibs.registerEvent(new EventReplaceFoodStats());
//		HSLibs.registerEvent(new EventItemDamageTest());
//		LPEvents.register();
		UnsagaStatus.register();
//		AdditionalStatusEvents.register();
		(new UnsagaMobDrops()).init();
//		StatePropertySpecialMove.register();
		HSLibs.registerEvent(UnsagaItems.WAZA_BOOK);
		EventConfigChanged.instance().addConfigHandler(UnsagaMod.MODID, UnsagaMod.configs);
//		HSLib.core().events.livingHurt.getEventsMiddle().add(new ILivingHurtEvent(){
//
//			@Override
//			public boolean apply(LivingHurtEvent e, DamageSource dsu) {
//				UnsagaMod.logger.trace(this.getName(), "shsfhhsd1",e.getSource().getSourceOfDamage(),e.getSource().getEntity());
//				return e.getSource().getSourceOfDamage() instanceof EntityArrow;
//			}
//
//			@Override
//			public String getName() {
//				// TODO 自動生成されたメソッド・スタブ
//				return "arrow fix";
//			}
//
//			@Override
//			public DamageSource process(LivingHurtEvent e, DamageSource dsu) {
//				UnsagaMod.logger.trace(this.getName(), "shsfhhsd2");
//				EntityArrow arrow = (EntityArrow) e.getSource().getSourceOfDamage();
//				if(EntityStateCapability.adapter.hasCapability(arrow)){
//					UnsagaMod.logger.trace(this.getName(), "shsfhhsd3");
//					StateArrow state = (StateArrow) EntityStateCapability.adapter.getCapability(arrow).getState(StateRegistry.instance().stateArrow);
//					if(state.isCancelHurtRegistance()){
//						e.getEntityLiving().hurtResistantTime = 0;
//						e.getEntityLiving().hurtTime=0;
//					}
//				}
//				return dsu;
//			}}
//		);
//		HSLibs.registerEvent(new EventOnBed());
		HSLibs.registerEvent(new EventToolTipUnsaga());
//		HSLibs.registerEvent(new EventAbilityLearning());
//		HSLibs.registerEvent(new EventReplaceFoodStats());
		HSLibs.registerEvent(new UnsagaVillagerInteraction());
//		HSLibs.registerEvent(new EventMartialArtsOnInteract());
//		HSLibs.registerEvent(new WorldSaveDataSkillPanel.SkillPanelSyncEvent());
//		HSLibEvents.livingUpdate.getEvents().add(new LivingUpdateLPRestoreEvent());

		HSLibs.registerEvent(new EventDurabilityTooltip());

	}
}
