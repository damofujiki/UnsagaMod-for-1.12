//package mods.hinasch.unsaga.lp;
//
//import java.util.Map;
//import java.util.Random;
//import java.util.UUID;
//import java.util.function.Function;
//
//import com.google.common.collect.Maps;
//
//import mods.hinasch.lib.core.event.EventEntityJoinWorld;
//import mods.hinasch.lib.misc.FunctionCall;
//import mods.hinasch.lib.network.PacketUtil;
//import mods.hinasch.lib.util.HSLibs;
//import mods.hinasch.lib.world.WorldHelper;
//import mods.hinasch.unsaga.UnsagaMod;
//import mods.hinasch.unsaga.core.event.livinghurt.LivingHurtEventLPProcess.IParentContainer;
//import mods.hinasch.unsaga.core.net.packet.PacketLP;
//import mods.hinasch.unsaga.damage.DamageSourceUnsaga;
//import mods.hinasch.unsaga.lp.LifePoint.ILifePoint;
//import net.minecraft.entity.Entity;
//import net.minecraft.entity.EntityLivingBase;
//import net.minecraft.entity.passive.EntityHorse;
//import net.minecraft.entity.passive.EntityTameable;
//import net.minecraft.entity.player.EntityPlayer;
//import net.minecraft.entity.player.EntityPlayerMP;
//import net.minecraftforge.event.entity.living.LivingHealEvent;
//import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
//import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;
//
//@Deprecated
//public class LPLogicManager {
//
//	public static class EventContainer{
//
//
//		EntityLivingBase victim;
//		float lpHurtStrength;
//		DamageSourceUnsaga damageSource;
//		int lpHurtOffset;
//		public EventContainer(EntityLivingBase victim, float lpHurtStrength, int lpHurtOffset,DamageSourceUnsaga isPoisonDamage) {
//			this.victim = victim;
//			this.lpHurtStrength = lpHurtStrength;
//			this.damageSource = isPoisonDamage;
//			this.lpHurtOffset = lpHurtOffset;
//		}
//		public int getLpHurtOffset() {
//			return lpHurtOffset;
//		}
//		public float getLpHurtStrength() {
//			return lpHurtStrength;
//		}
//		public EntityLivingBase getVictim() {
//			return victim;
//		}
//		public DamageSourceUnsaga getDamageSource() {
//			return damageSource;
//		}
//	}
//
//	/** ＬＰダメージをブロードキャストする*/
//	public static void broadcastRenderHurtLPPacket(int lp,EntityLivingBase syncEntity,TargetPoint target){
//		if(hasCapability(syncEntity)){
//			//			PacketLPNew psl = PacketLPNew.getPacketRenderDamagedLP(syncEntity.getEntityId(), lp);
//			UnsagaMod.packetDispatcher.sendToAllAround(PacketLP.createRenderDamagePacket(syncEntity, lp), target);
//		}
//
//	}
//
//	public static ILifePoint getCapability(EntityLivingBase living){
//		if(hasCapability(living)){
//			return LifePoint.adapter.getCapability(living);
//		}
//
//		return null;
//	}
//
//	public static float getLPHurtAmount(final EntityLivingBase damaged,float ammount,final float strLP,Random rand,final DamageSourceUnsaga dsu){
//		//総被ダメージ割合
//		final float var1 = FunctionCall.supplier(() ->{
//			float  base = damaged.getHealth() / damaged.getMaxHealth();
//			base = 1.0F - base;
//			return base;
//		}).get();
//
//		final float overkill = ammount - damaged.getHealth();
//		int offset = FunctionCall.supplier(() ->{
//			int base = 0;
//
//
//			for(Function<EventContainer,Integer> hook:hooks.values()){
//				base = hook.apply(new EventContainer(damaged, strLP, base, dsu));
//			}
//
//			//hooksに移動
//			//				if(AbilityHelperNew.hasArmorAbility(damaged, UnsagaMod.abilities.lifeGuard)){
//			//					base -= 10 * AbilityHelperNew.getArmorAbilityAmounts(damaged).get(UnsagaMod.abilities.lifeGuard);
//			//				}
//			/**
//			 * 総被ダメージによって被LPダメージ率を決定
//			 */
//			if(var1>0.7F){
//				base = 50;
//			}
//			if(var1>0.8F){
//				base = 70;
//			}
//			if(var1>0.9F){
//				base = 90;
//			}
//			if(overkill>0){
//				base += (overkill * 5.0F);
//			}
//			if(dsu.getEntity()==null && dsu.isMagicDamage() && dsu.isUnblockable()){
//				base = 20;
//			}
//			return base;
//		}).get();
//
//
//
//
//
//		//低体力時に高い数値になる
//		int var2 = (int)((var1+strLP)* 100F);
//		if(var2<2){
//			var2 = 2;
//		}
//
//
//		int rnd = rand.nextInt(var2) + offset;
//
//		//Unsaga.debug(rnd);
//		if(rnd<70){
//			return 0.0F;
//		}
//		if(rnd<100){
//			return 1.0F;
//		}
//		float f2 = rnd * 0.01F;
//		return f2;
//	}
//
//	public static UUID getOwnerName(EntityLivingBase living){
//		if(living instanceof EntityTameable){
//			return ((EntityTameable) living).getOwner().getCommandSenderEntity().getUniqueID();
//		}
//		if(living instanceof EntityHorse){
//			//もうちょっと細かく
//			UUID uuid =  ((EntityHorse) living).getOwnerUniqueId();
//			return uuid;
//			//return uuid == null?null : living.worldObj.getPlayerEntityByUUID(uuid).getCommandSenderEntity().getName();
//		}
//		return null;
//	}
//
//	public static boolean hasCapability(EntityLivingBase living){
//
//		return LifePoint.adapter.hasCapability(living);
//	}
//
//	public static void processLPHurt(Entity attacker,EntityLivingBase victim,float lpdamage){
//
//		if(!hasCapability(victim)){
//			return;
//		}
//		ILifePoint capa = getCapability(victim);
//		if(capa.getHurtInterval()>0){
//			return;
//		}
//		if(lpdamage>=1.0F){
//
//
//			UnsagaMod.logger.trace("lpdamage:"+(int)lpdamage);
//			capa.decrLifePoint((int)lpdamage);
//			capa.setHurtInterval(3);
//
//
//
//
//			UnsagaMod.packetDispatcher.sendToAllAround(PacketLP.create(victim, capa.getLifePoint()), PacketUtil.getTargetPointNear(victim));
//			UnsagaMod.packetDispatcher.sendToAllAround(PacketLP.createRenderDamagePacket(victim,(int) lpdamage), PacketUtil.getTargetPointNear(victim));
//
//
//
//			//			ExtendedDataLP.getExtended(entityHurt).decrLP((int)lpdamage);
//			//			ExtendedDataLP.getExtended(entityHurt).setHurtInterval(5);
//			//Unsaga.debug(entityHurt,ExtendedDataLP.getExtended(entityHurt).getLP());
//			//攻撃手がプレイヤーの場合はＬＰダメージをそのプレイヤーに表示させる
////			if(attacker instanceof EntityPlayer && WorldHelper.isServer(attacker.worldObj)){
////				if(!HSLibs.isEntityLittleMaidAvatar((EntityLivingBase) attacker)){
////					sendRenderHurtLPPacket((int)lpdamage,victim,(EntityPlayerMP) attacker);
////					String mes = String.format(I18n.translateToLocal("msg.lp.hurt"),victim.getName(),String.valueOf((int)lpdamage) );
////					ChatHandler.sendChatToPlayer((EntityPlayer) attacker,mes);
////				}
////
////
////			}
////			if(victim instanceof EntityPlayer){
////				sendSyncLPPacket((int) capa.getLifePoint(),victim,(EntityPlayerMP) victim);
////				String mes = String.format(I18n.translateToLocal("msg.lp.hurt.player"),victim.getName(),String.valueOf((int)lpdamage) );
////				ChatHandler.sendChatToPlayer((EntityPlayer) victim, mes);
////			}
////
////			if(HSLibs.isSameTeam(Minecraft.getMinecraft().thePlayer, victim)){
////				UUID ownerid = getOwnerName(victim);
////				World world = victim.worldObj;
////				if(ownerid!=null && world.getPlayerEntityByUUID(ownerid)!=null){
////					EntityPlayer owner = world.getPlayerEntityByUUID(ownerid);
////					sendSyncLPPacket((int)capa.getLifePoint(),victim,(EntityPlayerMP) owner);
////				}
////			}
////
////			/** 攻撃手も受け手もどっちもプレイヤーでない場合はブロードキャストでＬＰを表示*/
////			if(!(victim instanceof EntityPlayer) && !(attacker instanceof EntityPlayer)){
////				broadcastRenderHurtLPPacket((int)lpdamage,victim,PacketUtil.getTargetPointNear(victim));
////			}
//		}
//	}
//
//	/** 特定のプレイヤーにＬＰダメージレンダーのパケットを送る*/
//	public static void sendRenderHurtLPPacket(int lpdamage,EntityLivingBase entityHurt,EntityPlayerMP renderTo){
//		if(hasCapability(entityHurt)){
//			//			PacketLPNew psl = PacketLPNew.getPacketRenderDamagedLP(entityHurt.getEntityId(), lpdamage);
//			UnsagaMod.packetDispatcher.sendTo(PacketLP.createRenderDamagePacket(entityHurt, lpdamage), (EntityPlayerMP) renderTo);
//		}
//
//	}
//
//	/** ＬＰを同期*/
//	public static void sendSyncLPPacket(int lp,EntityLivingBase entitySync,EntityPlayerMP playerSync){
//		if(hasCapability(entitySync)){
//			//			PacketLPNew psl = PacketLPNew.getSyncPacket(entitySync.getEntityId(),lp);
//			UnsagaMod.packetDispatcher.sendTo(PacketLP.create(entitySync, lp), (EntityPlayerMP) playerSync);
//		}
//
//	}
//
//	public static Map<IParentContainer,Function<EventContainer,Integer>> getHooks(){
//		return hooks;
//	}
//
//	public static void registerEvent(){
//
//		EventEntityJoinWorld.addEvent(e ->{
////			UnsagaMod.logger.trace("entitjoin", e.getEntity(),e.getEntity().getEntityWorld());
//			if(e.getEntity() instanceof EntityLivingBase && WorldHelper.isClient(e.getEntity().getEntityWorld()) && LPLogicManager.hasCapability((EntityLivingBase) e.getEntity())){
////				UnsagaMod.logger.trace("entitjoin", e.getEntity().getEntityWorld().getLoadedEntityList());
//
//				if(e.getEntity() instanceof EntityPlayer){
//					EntityLivingBase living = (EntityLivingBase) e.getEntity();
//					UnsagaMod.packetDispatcher.sendToServer(PacketLP.createRequest(living));
//				}
//			}
//		});
//		HSLibs.registerEvent(new HealLifeEvent());
////		HSLib.eventLivingUpdate.getEvents().add(new ILivingUpdateEvent(){
////
////			@Override
////			public void update(LivingUpdateEvent e) {
////				if(LPLogicManager.hasCapability(e.getEntityLiving())){
////					LPLogicManager.getCapability(e.getEntityLiving()).onUpdate(e.getEntityLiving());
////				}
////
////			}
////
////			@Override
////			public String getName() {
////				// TODO 自動生成されたメソッド・スタブ
////				return "LP Regeneration";
////			}}
////		);
//	}
//
//	public static class HealLifeEvent{
//
//		@SubscribeEvent
//		public void onHeal(LivingHealEvent e){
//			if(e.getEntityLiving().getMaxHealth()<=e.getEntityLiving().getHealth()){
//				if(LPLogicManager.hasCapability(e.getEntityLiving())){
//					ILifePoint capa = LPLogicManager.getCapability(e.getEntityLiving());
//					if(capa.getLifeSaturation()>10.0F){
//						capa.setLifeSaturation(0);
//						capa.addLifePoint(1);
//					}
//					capa.setLifeSaturation(capa.getLifeSaturation()+e.getAmount());
//				}
//			}
//		}
//	}
//	static Map<IParentContainer,Function<EventContainer,Integer>> hooks = Maps.newHashMap();
//}
