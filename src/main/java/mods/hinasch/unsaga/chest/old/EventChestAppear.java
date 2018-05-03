//package mods.hinasch.unsaga.chest.old;
//
//import mods.hinasch.lib.event.PlayerSwingHandEvent;
//import mods.hinasch.lib.util.ChatHandler;
//import mods.hinasch.lib.world.WorldHelper;
//import mods.hinasch.unsaga.chest.old.FieldChestInfo.Type;
//import mods.hinasch.unsaga.core.entity.passive.EntityUnsagaChest;
//import net.minecraft.init.SoundEvents;
//import net.minecraft.util.math.BlockPos;
//import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
//
//public class EventChestAppear {
//
//	@SubscribeEvent
//	public void onSwingHand(PlayerSwingHandEvent ev){
//		BlockPos pos = ev.getEntityLiving().getPosition();
//		IFieldChest chest = ChestPosCache.instance().get(pos);
//		if(chest!=null){
//			if(chest.getPos().getDistance(pos.getX(), pos.getY(), pos.getZ())<=5.0D && chest.getType()!=Type.EXPIRED){
//				EntityUnsagaChest entityChest = new EntityUnsagaChest(ev.getEntityLiving().getEntityWorld());
//				entityChest.setPosition(pos.getX(), pos.getY(), pos.getZ());
//
//				if(WorldHelper.isServer(ev.getEntityLiving().getEntityWorld())){
//					WorldHelper.safeSpawn(ev.getEntityLiving().getEntityWorld(), entityChest);
//				}else{
//					ChatHandler.sendChatToPlayer(ev.getEntityPlayer(), "you found chest!");
//					ev.getEntityLiving().playSound(SoundEvents.BLOCK_ANVIL_USE, 1.0F, 1.0F);
//				}
//
//				IFieldChest expired = new FieldChestInfo(chest.getPos(),Type.EXPIRED);
//				ChestPosCache.instance().put(pos, expired);
//			}
//
//		}
//	}
//}
