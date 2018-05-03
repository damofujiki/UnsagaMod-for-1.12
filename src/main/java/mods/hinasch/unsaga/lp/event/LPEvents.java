package mods.hinasch.unsaga.lp.event;

import mods.hinasch.lib.network.PacketSyncCapability;
import mods.hinasch.unsaga.lp.LifePoint;

public class LPEvents {

	public static void register(){
		PacketSyncCapability.registerSyncCapability(LifePoint.SYNC_ID, LifePoint.CAPA);

//		HSLibs.registerEvent(new LivingDeathEventLP());
//		HSLib.events.livingHurt.getEventsPost().add(new LivingDecrLPEvent());
//		HSLib.events.livingUpdate.getEvents().add(new LivingLPHurtIntervalEvent());
//		EventEntityJoinWorld.addEvent(e ->{
//			if(e.getEntity() instanceof EntityLivingBase && WorldHelper.isClient(e.getEntity().getEntityWorld())
//					&& LifePoint.adapter.hasCapability((EntityLivingBase) e.getEntity())){
//				if(e.getEntity() instanceof EntityPlayer){
//					EntityLivingBase living = (EntityLivingBase) e.getEntity();
//					UnsagaMod.packetDispatcher.sendToServer(PacketLP.createRequest(living));
//				}
//			}
//		});

		//TODO : エンティティがスポーンした時も
	}
}
