//package mods.hinasch.unsaga.ability.specialmove.action;
//
//import java.util.Optional;
//
//import javax.annotation.Nullable;
//
//import mods.hinasch.lib.core.HSLib;
//import mods.hinasch.lib.iface.ILivingUpdateEvent;
//import mods.hinasch.lib.util.HSLibs;
//import mods.hinasch.unsaga.ability.specialmove.TechInvoker;
//import mods.hinasch.unsaga.common.specialaction.ActionMultipleMelee.MultipleAttackComponent;
//import mods.hinasch.unsaga.core.entity.State;
//import mods.hinasch.unsaga.core.entity.StateProperty;
//import mods.hinasch.unsaga.core.potion.UnsagaPotions;
//import mods.hinasch.unsaga.util.LivingHelper;
//import net.minecraft.entity.EntityLivingBase;
//import net.minecraft.util.EnumParticleTypes;
//import net.minecraftforge.event.entity.living.LivingAttackEvent;
//import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
//import net.minecraftforge.event.entity.living.LivingFallEvent;
//import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
//
//@Deprecated
///** 技関連の状態*/
//public class StatePropertySpecialMove extends StateProperty{
//
//	public StatePropertySpecialMove(String name) {
//		super(name);
//		// TODO 自動生成されたコンストラクター・スタブ
//	}
//
//	@Override
//	public StateSpecialMove createState(){
//		return new StateSpecialMove();
//	}
//
//	public static class StateSpecialMove extends State{
//
//		boolean isSpecialMoveProgress = false;
//
//		boolean isCancelFall = false;
//		int hurtCancelTick = 0;
//
//		boolean hasSetExplode = false;
//		int explodeTime = 0;
//
//		EnumParticleTypes type = EnumParticleTypes.CRIT;
//		boolean isFallParticle = false;
//
//		MultipleAttackComponent multipleAttackComp;
//		int multipleAttackTime = 0;
//		int multipleAttackNumber = 0;
//		TechInvoker multipleAttackState = null;
//
//		public StateSpecialMove() {
//			super(false);
//			// TODO 自動生成されたコンストラクター・スタブ
//		}
//
//		public void setSpecialMoveProgress(boolean par1){
//			this.isSpecialMoveProgress = par1;
//
//		}
//
//		public void setMultipleAttackComponent(MultipleAttackComponent comp){
//			this.multipleAttackComp = comp;
//		}
//
//		public @Nullable MultipleAttackComponent getMultipleAttackComponent(){
//			return this.multipleAttackComp;
//		}
//		public boolean isSpecialMoveProgress(){
//			return this.isSpecialMoveProgress;
//		}
////		public boolean isCancelFall(){
////			return this.isCancelFall;
////		}
//
//		public boolean isFallParticle(){
//			return this.isFallParticle;
//		}
//		public void setFallParticle(boolean par1){
//			this.isFallParticle = par1;
//		}
////		public boolean isCancelHurt(){
////			return this.hurtCancelTick>0;
////		}
//
////		public void setCancelFall(boolean par1){
////			this.isCancelFall = par1;
////
////		}
//
////		public void setCancelHurt(int time){
////			this.hurtCancelTick = time;
////		}
//
////		public void setScheduledExplode(int time){
////			this.explodeTime = time;
////			this.hasSetExplode = true;
////		}
//
////		public void cancelExplode(){
////			this.explodeTime = 0;
////			this.hasSetExplode = false;
////		}
//
////		public void decrExplodeTime(){
////			this.explodeTime -= 1;
////			if(this.explodeTime<0){
////				this.explodeTime = 0;
////			}
////		}
//	}
//
//	public static class Events{
//
//		@SubscribeEvent
//		public void onFall(LivingFallEvent e){
////			if(EntityStateCapability.adapter.hasCapability(e.getEntityLiving())){
////				StateSpecialMove state = (StateSpecialMove) EntityStateCapability.adapter.getCapability(e.getEntityLiving()).getState(StateRegistry.instance().stateSpecialMove);
//				if(LivingHelper.isStateActive(e.getEntityLiving(),UnsagaPotions.instance().CANCEL_FALL)){
//					e.setCanceled(true);
//				}
////				if(state.isCancelFall()){
////					e.setCanceled(true);
////				}
////			}
//		}
//
//		@SubscribeEvent
//		public void onAttack(LivingAttackEvent e){
////			if(EntityStateCapability.adapter.hasCapability(e.getEntityLiving())){
////				StateSpecialMove state = (StateSpecialMove) EntityStateCapability.adapter.getCapability(e.getEntityLiving()).getState(StateRegistry.instance().stateSpecialMove);
//				if(LivingHelper.isStateActive(e.getEntityLiving(),UnsagaPotions.instance().CANCEL_HURT)){
//					e.setCanceled(true);
////				}
////				if(state.isCancelHurt()){
////					e.setCanceled(true);
////				}
////				if(e.getSource() instanceof DamageSourceUnsaga){
////					DamageSourceUnsaga dsu = (DamageSourceUnsaga) e.getSource();
////					if(!dsu.isModified()){
////						UnsagaMod.logger.trace(this.getClass().getName(), "called");
////						if(state.getMultipleAttackComponent()!=null){
////							UnsagaMod.logger.trace(this.getClass().getName(), "called2");
////							MultipleAttackComponent comp = state.getMultipleAttackComponent();
////							if(comp.isMultipleAttackAllowing()){
////								UnsagaMod.logger.trace(this.getClass().getName(), "called3");
////								SpecialMoveInvoker invoker = comp.getInvoker();
////								if(invoker.getAction() instanceof IActionMultipleMelee){
////									IActionMultipleMelee action = (IActionMultipleMelee) invoker.getAction();
////									action.attack(e.getEntityLiving(), invoker);
////								}
////								comp.resetComboTime();
////								comp.reduceAttackNumber();
////								e.setCanceled(true);
////							}
////
////						}
////					}
////				}
//
//			}
//		}
//	}
//
//	public static Optional<StateSpecialMove> getState(EntityLivingBase e){
////		if(EntityStateCapability.adapter.hasCapability(e)){
////			StateSpecialMove state = (StateSpecialMove) EntityStateCapability.adapter.getCapability(e).getState(StateRegistry.instance().stateSpecialMove);
////			return Optional.of(state);
////		}
//		return Optional.empty();
//	}
//	public static void register(){
//		HSLibs.registerEvent(new Events());
//		HSLib.events.livingUpdate.getEvents().add(new ILivingUpdateEvent(){
//
//			@Override
//			public void update(LivingUpdateEvent e) {
////				if(EntityStateCapability.adapter.hasCapability(e.getEntityLiving())){
////					StateSpecialMove state = (StateSpecialMove) EntityStateCapability.adapter.getCapability(e.getEntityLiving()).getState(StateRegistry.instance().stateSpecialMove);
////					if(state.isCancelFall() && e.getEntityLiving().onGround){
////						state.setCancelFall(false);
////					}
//
////					if(e.getEntityLiving().isPotionActive(UnsagaPotions.instance().CANCEL_FALL) && e.getEntityLiving().onGround){
////						e.getEntityLiving().removePotionEffect(UnsagaPotions.instance().CANCEL_FALL);
////					}
////
////					if(e.getEntityLiving().isPotionActive(UnsagaPotions.instance().CANCEL_FALL)){
////						state.setFallParticle(true);
////					}
//////					if(state.isCancelFall()){
//////						state.setFallParticle(true);
//////					}
////					if(state.isFallParticle()){
////						World world = e.getEntityLiving().getEntityWorld();
////						ParticleHelper.MovingType.FLOATING.spawnParticle(world, XYZPos.createFrom(e.getEntityLiving()), state.type, world.rand	, 10, 0.05D);
////					}
////					if(state.isFallParticle && e.getEntityLiving().onGround){
////						state.setFallParticle(false);
////					}
//
//					if(e.getEntityLiving().isPotionActive(UnsagaPotions.instance().CANCEL_FALL)){
//						e.getEntityLiving().fallDistance = 0;
//					}
////					if(state.isCancelFall()){
////						e.getEntityLiving().fallDistance = 0;
////					}
//
////					if(state.getMultipleAttackComponent()!=null){
////						state.getMultipleAttackComponent().reduceComboTime();
////					}
//
////					Potion delayExplode = UnsagaPotions.instance().DELAYED_EXPLODE;
////					if(e.getEntityLiving().isPotionActive(delayExplode)){
////						if(e.getEntityLiving().getActivePotionEffect(delayExplode).getDuration()<=1){
////							e.getEntityLiving().removePotionEffect(delayExplode);
////							if(e.getEntityLiving().onGround){
////								XYZPos pos = XYZPos.createFrom(e.getEntityLiving());
////								WorldHelper.createExplosionSafe(e.getEntityLiving().getEntityWorld(),null,pos, 3, true);
////							}
////						}
////					}
////					if(e.getEntityLiving().ticksExisted % 5 ==0){
////
////						if(state.hasSetExplode){
////							state.decrExplodeTime();
////							if(state.explodeTime<=0){
////								state.cancelExplode();
////								if(e.getEntityLiving().onGround){
////									XYZPos pos = XYZPos.createFrom(e.getEntityLiving());
////									WorldHelper.createExplosionSafe(e.getEntityLiving().getEntityWorld(),null,pos, 3, true);
////								}
////
////							}
////						}
////					}
////				}
//			}
//
//			@Override
//			public String getName() {
//				// TODO 自動生成されたメソッド・スタブ
//				return "expire fallcancel";
//			}}
//		);
//	}
//}
