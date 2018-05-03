package mods.hinasch.unsaga.core.event;

import mods.hinasch.lib.config.EventConfigChanged;
import mods.hinasch.lib.util.HSLibs;
import mods.hinasch.unsaga.UnsagaMod;
import mods.hinasch.unsaga.core.client.event.EventRenderGameOverlay;
import mods.hinasch.unsaga.core.client.event.EventRenderLivingEffect;
import mods.hinasch.unsaga.damage.UnsagaEntityAttributes;

@Deprecated
public class UnsagaEvents {


//	LivingHurtEventLPProcess lpProcess;
//	LivingHurtEventAppendable hurtEventAppendable;
//	LivingUpdateAppendable updateAppendable;

	public UnsagaEvents(){

	}

	public void init(){
//		HSLibs.registerEvent(new EventUnsagaWeapon());
		EventConfigChanged.instance().addConfigHandler(UnsagaMod.MODID,UnsagaMod.configs);
//		HSLibs.registerEvent(new EventCapabilityAttach());
		HSLibs.registerEvent(new UnsagaEntityAttributes());
//		HSLibs.registerEvent(new EventCancelFallDamage());
//		HSLibs.registerEvent(new EventOnBed());
		HSLibs.registerEvent(new EventReplaceFoodStats());
//		WorldSaveDataUnsaga.registerEvents();
//		HSLibs.registerEvent(new WorldSaveDataUnsaga());
//		HSLibs.registerEvent(new EventGetSkillPoint());
//		HSLibs.registerEvent(new EventToolTipUnsaga());
//		HSLibs.registerEvent(new EventLearningChance());
//		HSLibs.registerEvent(new EventMartialArtsOnInteract());
//		HSLibs.registerEvent(new EventTranscriptWazaByAnvil());
//		HSLibs.registerEvent(new EventLivingDeath());

//		LPLogicManager.registerEvent();
//		ChestBehavior.registerEvents();

		(new  UnsagaMobDrops()).init();


//		HSLibEvents.livingUpdate.getEvents().add(new LivingUpdateLPEvent());
//		HSLibEvents.livingUpdate.getEvents().add(new LivingUpdateLPRestoreEvent());
//		HSLibEvents.livingUpdate.getEvents().add(new LivingUpdateEventArrowParticles());
//		HSLibEvents.livingHurt.getEventsMiddle().add(new LivingHurtEventZombieDebuffs());
//		HSLibEvents.livingHurt.getEventsMiddle().add(new LivingHurtEventArrowArts());
//		HSLibEvents.livingHurt.getEventsMiddle().add(new LivingHurtEventUnsagaBase(){
//
//			@Override
//			public boolean apply(LivingHurtEvent e, DamageSourceUnsaga dsu) {
//				// TODO 自動生成されたメソッド・スタブ
//				return true;
//			}
//
//			@Override
//			public DamageSource process(LivingHurtEvent e, DamageSourceUnsaga dsu) {
//				float modified = e.getAmount() + DamageTypeUnsaga.getDamageModifierFromType(dsu.getDamageTypeUnsaga()!=null ? dsu.getDamageTypeUnsaga() : EnumSet.of(General.SWORD) ,e.getEntityLiving(), e.getAmount());
//				e.setAmount(modified);
//				return dsu;
//			}
//
//			@Override
//			public int getWeight(){
//				return 50;
//			}
//
//			@Override
//			public String getName() {
//				// TODO 自動生成されたメソッド・スタブ
//				return "damage deffence attribute modifier";
//			}}
//
//
//		);
		//HSLib.eventLivingHurt.getEventsMiddle().add(new LivingHurtEventDebuffs());
//		HSLibEvents.livingHurt.getEventsPre().add(new LivingHurtEventReplaceDamageSource());
//		lpProcess = new LivingHurtEventLPProcess();
//		HSLibEvents.livingHurt.getEventsPost().add(new LivingHurtShieldEvent());
//		HSLibEvents.livingHurt.getEventsPost().add(lpProcess);
//		HSLibEvents.livingHurt.getEventsPost().add(new LivingHurtEventUnsagaBase(){
//
//			@Override
//			public boolean apply(LivingHurtEvent e, DamageSourceUnsaga dsu) {
//				UnsagaMod.logger.trace("swing", e.getEntityLiving().isSwingInProgress);
//
//				return e.getEntityLiving().isSwingInProgress && !dsu.isUnblockable() && e.getEntityLiving().getActiveHand()==EnumHand.OFF_HAND;
//			}
//
//			@Override
//			public DamageSource process(LivingHurtEvent e, DamageSourceUnsaga dsu) {
//				PacketSound.atPos(SoundEvents.ITEM_SHIELD_BLOCK, XYZPos.createFrom(e.getEntityLiving()));
//				if(dsu.isProjectile()){
//
//					e.setAmount(0);
//				}else{
//
//					float d = e.getAmount() * 0.33F;
//					e.setAmount(d);
//				}
//
//
//				return dsu;
//			}
//
//			@Override
//			public String getName() {
//				// TODO 自動生成されたメソッド・スタブ
//				return "weapon blocking";
//			}
//
//		});
//		HSLibEvents.livingHurt.getEventsPost().add(new LivingHurtEventUnsagaBase(){
//
//			@Override
//			public boolean apply(LivingHurtEvent e, DamageSourceUnsaga dsu) {
//				// TODO 自動生成されたメソッド・スタブ
//				return HSLib.configHandler.isDebug();
//			}
//
//			@Override
//			public String getName() {
//				// TODO 自動生成されたメソッド・スタブ
//				return "debug log";
//			}
//
//			@Override
//			public int getWeight(){
//				return 200;
//			}
//			@Override
//			public DamageSource process(final LivingHurtEvent e, final DamageSourceUnsaga dsu) {
//				EntityPlayer player = new Supplier<EntityPlayer>(){
//
//					@Override
//					public EntityPlayer get() {
//						if(dsu.getTrueSource() instanceof EntityPlayer){
//							return (EntityPlayer) dsu.getTrueSource();
//						}
//						if(e.getEntityLiving() instanceof EntityPlayer){
//							return (EntityPlayer) e.getEntityLiving();
//						}
//						return null;
//					}
//				}.get();
//				if(player!=null){
//					String str = String.format("Damage:%s LPDamage:%s DamageType:%s SubType:%s", String.valueOf(e.getAmount()),String.valueOf(dsu.getStrLPHurt()),dsu.getDamageTypeUnsaga().toString()
//							,dsu.getSubTypes().isEmpty() ? "none" : dsu.getSubTypes().stream().map(in -> in.getName()).collect(Collectors.joining(",")));
//					ChatHandler.sendChatToPlayer(player, str);
////					if(LPLogicManager.hasCapability(e.getEntityLiving())){
////						ILifePoint capa = LPLogicManager.getCapability(e.getEntityLiving());
////						String str2 = String.format("Current HP:%s Current LP:%s", String.valueOf(e.getEntityLiving().getHealth()),String.valueOf(capa.getLifePoint()));
////						ChatHandler.sendChatToPlayer(player, str2);
////					}
//				}
//				return dsu;
//			}}
//		);
//		hurtEventAppendable = new LivingHurtEventAppendable();
//		HSLibEvents.livingHurt.getEventsMiddle().add(hurtEventAppendable);
//		updateAppendable = new LivingUpdateAppendable();
//		HSLibEvents.livingUpdate.getEvents().add(updateAppendable);


	}

	public void initClientEvents(){

		HSLibs.registerEvent(EventRenderGameOverlay.RenderEnemyStatus.getEvent());
		HSLibs.registerEvent(EventRenderGameOverlay.RenderPlayerStatus.getEvent());
		HSLibs.registerEvent(new EventRenderLivingEffect());
	}

//	public LivingHurtEventLPProcess getLivingHurtEventLPProcess(){
//		return lpProcess;
//	}
//
//	public LivingHurtEventAppendable getLivingHurtEventAppendable(){
//		return this.hurtEventAppendable;
//	}
//
//	public LivingUpdateAppendable getLivingUpdateAppendable(){
//		return this.updateAppendable;
//	}
}
