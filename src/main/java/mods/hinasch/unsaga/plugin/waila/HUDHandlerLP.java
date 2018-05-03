package mods.hinasch.unsaga.plugin.waila;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaEntityAccessor;
import mcp.mobius.waila.api.IWailaEntityProvider;
import mcp.mobius.waila.api.IWailaRegistrar;
import mods.hinasch.lib.util.HSLibs;
import mods.hinasch.unsaga.UnsagaMod;
import mods.hinasch.unsaga.core.net.packet.PacketLP;
import mods.hinasch.unsaga.lp.LifePoint;
import mods.hinasch.unsaga.status.UnsagaStatus;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class HUDHandlerLP implements IWailaEntityProvider{

	Queue<Entity> cache = new ArrayBlockingQueue(10);
	@Override
	public Entity getWailaOverride(IWailaEntityAccessor paramIWailaEntityAccessor,
			IWailaConfigHandler paramIWailaConfigHandler) {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	@Override
	public List<String> getWailaHead(Entity paramEntity, List<String> paramList,
			IWailaEntityAccessor paramIWailaEntityAccessor, IWailaConfigHandler paramIWailaConfigHandler) {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	@Override
	public List<String> getWailaBody(Entity paramEntity, List<String> paramList,
			IWailaEntityAccessor paramIWailaEntityAccessor, IWailaConfigHandler paramIWailaConfigHandler) {
		if(paramEntity instanceof EntityLivingBase){

			EntityLivingBase living = (EntityLivingBase) paramEntity;
			if(LifePoint.adapter.hasCapability(paramEntity)){

				int max = LifePoint.adapter.getCapability(paramEntity).getMaxLifePoint();
				int lp = LifePoint.adapter.getCapability(paramEntity).getLifePoint();
				String lpStr = String.format("LP %d/%d", lp,max);
				if(paramIWailaEntityAccessor.getPlayer().isCreative()){


					if(!this.cache.contains(paramEntity)){
						UnsagaMod.packetDispatcher.sendToServer(PacketLP.createRequest((EntityLivingBase) paramEntity));
						if(!this.cache.offer(paramEntity)){
							this.cache.poll();
							this.cache.offer(paramEntity);
						}
					}


					double dex = living.getEntityAttribute(UnsagaStatus.DEXTALITY).getBaseValue();
					double inte = living.getEntityAttribute(UnsagaStatus.INTELLIGENCE).getBaseValue();
					double men = living.getEntityAttribute(UnsagaStatus.MENTAL).getBaseValue();
					double lpdef = living.getEntityAttribute(UnsagaStatus.RESISTANCE_LP_HURT).getBaseValue();
					String st = HSLibs.translateKey("waila.unsaga.entity_status.general", dex,inte,men,lpdef);
					paramList.add(st);
					double[] params1 = UnsagaStatus.GENERALS.values()
							.stream().mapToDouble(in -> living.getEntityAttribute(in).getAttributeValue()).toArray();
					String generals = HSLibs.translateKey("waila.unsaga.entity_status.defences1", params1[0],params1[1],params1[2],params1[3]);
					double[] params2 = UnsagaStatus.SUBS.values()
							.stream().mapToDouble(in -> living.getEntityAttribute(in).getAttributeValue()).toArray();
					String subs = HSLibs.translateKey("waila.unsaga.entity_status.defences2", params2[0],params2[1],params2[2],params2[3]);
					paramList.add(generals);
					paramList.add(subs);
				}
				paramList.add(lpStr);
			}
		}
		return paramList;
	}

	@Override
	public List<String> getWailaTail(Entity paramEntity, List<String> paramList,
			IWailaEntityAccessor paramIWailaEntityAccessor, IWailaConfigHandler paramIWailaConfigHandler) {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	@Override
	public NBTTagCompound getNBTData(EntityPlayerMP paramEntityPlayerMP, Entity paramEntity,
			NBTTagCompound paramNBTTagCompound, World paramWorld) {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	public static void register(IWailaRegistrar registrar){
		registrar.addConfig("UnsagaMod", "hinasch.unsaga.showLP",true);

		registrar.registerBodyProvider(new HUDHandlerLP(), EntityLivingBase.class);
	}
}
