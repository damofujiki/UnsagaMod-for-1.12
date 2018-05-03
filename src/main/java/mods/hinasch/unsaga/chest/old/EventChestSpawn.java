//package mods.hinasch.unsaga.chest.old;
//
//import mods.hinasch.unsaga.UnsagaMod;
//import mods.hinasch.unsaga.core.entity.passive.EntityUnsagaChestNew;
//import net.minecraft.entity.passive.EntityAnimal;
//import net.minecraftforge.event.entity.living.LivingSpawnEvent.SpecialSpawn;
//import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
//
//public class EventChestSpawn {
//
//	@SubscribeEvent
//	public void specialSpawn(SpecialSpawn ev){
//		UnsagaMod.logger.trace("spawn", ev.getEntity());
//		if(ev.getEntityLiving() instanceof EntityAnimal){
//			if(ev.getEntityLiving().getRNG().nextInt(5)==0){
//				EntityUnsagaChestNew chest = new EntityUnsagaChestNew(ev.getWorld());
//				chest.setPosition(ev.getX(), ev.getY(), ev.getZ());
//				ev.getWorld().spawnEntityInWorld(chest);
//			}
//		}
//	}
//}
