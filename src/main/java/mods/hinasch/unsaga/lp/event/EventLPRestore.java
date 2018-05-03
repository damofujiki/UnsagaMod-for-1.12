package mods.hinasch.unsaga.lp.event;

import mods.hinasch.lib.util.HSLibs;
import mods.hinasch.lib.world.WorldHelper;
import mods.hinasch.unsaga.UnsagaMod;
import mods.hinasch.unsaga.core.advancement.UnsagaTriggers;
import mods.hinasch.unsaga.core.net.packet.PacketLP;
import mods.hinasch.unsaga.lp.LifePoint;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;

public class EventLPRestore {


	public static void onLivingUpdate(LivingUpdateEvent e) {
		if(e.getEntityLiving() instanceof EntityPlayer){
			World world = e.getEntityLiving().getEntityWorld();
			EntityPlayer ep = (EntityPlayer) e.getEntityLiving();
			if(world.playerEntities.size()>1){ //マルチでの処理
				if(ep.isPlayerSleeping() && ep.ticksExisted % 12 ==0){
					if(LifePoint.adapter.hasCapability(ep)){
						float satu = LifePoint.adapter.getCapability(ep).getLifeSaturation();
						LifePoint.adapter.getCapability(ep).setLifeSaturation(satu+1);
					}
				}
			}else{
				if(ep.getSleepTimer()==99){
					if(LifePoint.adapter.hasCapability(ep)){
						LifePoint.adapter.getCapability(ep).restoreLifePoint();
						int lp = LifePoint.adapter.getCapability(ep).getLifePoint();
						if(WorldHelper.isServer(ep.getEntityWorld())){
							UnsagaTriggers.LP_RESTORE.trigger((EntityPlayerMP) ep);
							UnsagaMod.packetDispatcher.sendTo(PacketLP.create(ep, lp), (EntityPlayerMP) ep);
						}

//						ep.addStat(UnsagaModCore.instance().achievements.restoreLP);
						restoreSameTeamLP(ep);
					}
				}
			}

		}

	}

	public static void restoreSameTeamLP(final EntityPlayer ep){
		ep.getEntityWorld().getEntitiesWithinAABB(EntityLivingBase.class, ep.getEntityBoundingBox().expand(50.0D, 50.0D, 50.0D))
		.stream().filter(input -> HSLibs.isSameTeam(ep, input)).forEach(input ->{
			if(LifePoint.adapter.hasCapability(input) && WorldHelper.isServer(ep.getEntityWorld())){
				LifePoint.adapter.getCapability(input).restoreLifePoint();
				int lp = LifePoint.adapter.getCapability(input).getLifePoint();
				UnsagaTriggers.LP_RESTORE_OTHERS.trigger((EntityPlayerMP) ep);
				UnsagaMod.packetDispatcher.sendTo(PacketLP.create(input, lp), (EntityPlayerMP) ep);
			}
		});
	}

}
