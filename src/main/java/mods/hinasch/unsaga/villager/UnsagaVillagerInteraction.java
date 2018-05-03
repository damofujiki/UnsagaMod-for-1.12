package mods.hinasch.unsaga.villager;

import java.util.Map;
import java.util.Optional;

import com.google.common.collect.Maps;

import mods.hinasch.lib.client.IGuiAttribute;
import mods.hinasch.lib.util.HSLibs;
import mods.hinasch.lib.world.WorldHelper;
import mods.hinasch.lib.world.XYZPos;
import mods.hinasch.unsaga.UnsagaMod;
import mods.hinasch.unsaga.init.UnsagaGui;
import mods.hinasch.unsaga.util.LivingHelper;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.inventory.ContainerPlayer;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.EntityInteract;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.VillagerRegistry.VillagerProfession;

public class UnsagaVillagerInteraction {

	public static Map<VillagerProfession,UnsagaGui.Type> map = Maps.newHashMap();

	static{
		map.put(UnsagaVillagerProfession.BLACKSMITH, UnsagaGui.Type.SMITH);
		map.put(UnsagaVillagerProfession.MERCHANT, UnsagaGui.Type.BARTERING);
		map.put(UnsagaVillagerProfession.MAGIC_MERCHANT, UnsagaGui.Type.BARTERING);
	}
	@SubscribeEvent
	public void onInteract(EntityInteract e){
		if(e.getTarget() instanceof EntityVillager && UnsagaVillagerProfession.isUnsagaVillager((EntityVillager) e.getTarget())){
			e.setCanceled(true);

			EntityVillager villager = (EntityVillager) e.getTarget();
			if(!villager.isChild()){
				if(LivingHelper.ADAPTER.hasCapability(e.getEntityPlayer())){
					LivingHelper.ADAPTER.getCapability(e.getEntityPlayer()).setMerchant(Optional.of(villager));
					UnsagaMod.logger.trace("villager", villager.getEntityWorld().isRemote,LivingHelper.ADAPTER.getCapability(e.getEntityPlayer()).getMerchant());
					if(map.containsKey(villager.getProfessionForge())){
						this.openGui(e, map.get(villager.getProfessionForge()));
					}
				}
			}


		}
	}

	private void openGui(EntityInteract e,IGuiAttribute gui){
		if(WorldHelper.isServer(e.getWorld()) && e.getEntityPlayer().openContainer instanceof ContainerPlayer){
			HSLibs.openGui(e.getEntityPlayer(), UnsagaMod.instance, gui.getMeta(), e.getWorld(), XYZPos.createFrom(e.getEntityPlayer()));;
		}
	}
}
