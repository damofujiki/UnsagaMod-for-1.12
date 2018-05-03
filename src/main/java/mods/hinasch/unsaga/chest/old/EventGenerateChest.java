//package mods.hinasch.unsaga.chest.old;
//
//import mods.hinasch.lib.util.UtilNBT;
//import mods.hinasch.unsaga.UnsagaMod;
//import net.minecraft.nbt.NBTTagCompound;
//import net.minecraftforge.event.world.ChunkDataEvent;
//import net.minecraftforge.event.world.ChunkEvent;
//import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
//
//public class EventGenerateChest {
//
//
////	//サーバでは初生成時、クライアントでは読み込み時毎回呼ばれる？
////	@SubscribeEvent
////	public void onChunkLoad(ChunkEvent.Load ev){
////		if(WorldHelper.isClient(ev.getWorld())){
////			return;
////		}
////		Random rand = ev.getWorld().rand;
////		rand.nextBoolean();
////		int chunkX = ev.getChunk().xPosition;
////		int chunkZ = ev.getChunk().zPosition;
////		IFieldChest fieldChest = ChestPosCache.instance().getCache().getIfPresent(ev.getChunk().getChunkCoordIntPair());
////		if(fieldChest==null){
////			if(rand.nextInt(5)==0){
////				int x = (chunkX << 4) + rand.nextInt(16);
////				int z = (chunkZ << 4) + rand.nextInt(16);
////				int height = ev.getChunk().getHeight(new BlockPos(x,0,z));
////
////				IFieldChest newChest = new FieldChestInfo(new BlockPos(x,height,z),FieldChestInfo.Type.FIELD);
////				ChestPosCache.instance().getCache().put(ev.getChunk().getChunkCoordIntPair(), newChest);
////				UnsagaMod.logger.trace("generate chest chunk", ev.getChunk().getChunkCoordIntPair(),newChest.getPos());
////
////			}else{
////				IFieldChest newChest = new FieldChestInfo(BlockPos.ORIGIN,FieldChestInfo.Type.NONE);
////				ChestPosCache.instance().getCache().put(ev.getChunk().getChunkCoordIntPair(), newChest);
////			}
//////			ev.getChunk().setChunkModified();
////		}
////	}
//
//	@SubscribeEvent
//    public void onChunkUnload(ChunkEvent.Unload ev){
//		if(ChestPosCache.instance().get(ev.getChunk().getChunkCoordIntPair())!=null){
//			IFieldChest data = ChestPosCache.instance().get(ev.getChunk().getChunkCoordIntPair());
//			ChestPosCache.instance().invalidate(ev.getChunk().getChunkCoordIntPair());
//			UnsagaMod.logger.trace("unload chest chunk:", data.getPos(),ev.getChunk().getChunkCoordIntPair());
////			if(data.isSaved()){
////				ChestPosCache.instance().getCache().invalidate(ev.getChunk().getChunkCoordIntPair());
////				UnsagaMod.logger.trace("unload chest chunk:", ev.getChunk().getChunkCoordIntPair());
////			}else{
////				data.setScheduledUnload(true);
////			}
//
//
//		}
//
//	}
//
//	public static final String NBTTAG = "fieldChest";
//	public static final int VERSION = 1;
//
//	@SubscribeEvent
//	public void onChunkLoad(ChunkDataEvent.Load ev){
////		UnsagaMod.logger.trace("loading chunk...", ev.getChunk().getChunkCoordIntPair());
//		if(ev.getData().hasKey(NBTTAG)){
//			if(ev.getData().hasKey("version")){
//				if(ev.getData().getInteger("version")!=VERSION){
//					UnsagaMod.logger.trace("バージョンが違います", ev.getChunk().getChunkCoordIntPair());
//					return;
//				}
//			}else{
//				UnsagaMod.logger.trace("バージョンが見つかりません", ev.getChunk().getChunkCoordIntPair());
//				return;
//			}
//			NBTTagCompound tag = ev.getData().getCompoundTag(NBTTAG);
//			IFieldChest fieldChest = FieldChestInfo.FUNC.apply(tag);
//			ChestPosCache.instance().put(ev.getChunk().getChunkCoordIntPair(), fieldChest);
//			UnsagaMod.logger.trace("loaded chest", ev.getChunk().getChunkCoordIntPair());
//		}else{
////			UnsagaMod.logger.trace("データが見つかりません", ev.getChunk().getChunkCoordIntPair());
//		}
//	}
//
//	@SubscribeEvent
//	public void onChunkSave(ChunkDataEvent.Save ev){
//		IFieldChest fieldChest = ChestPosCache.instance().get(ev.getChunk().getChunkCoordIntPair());
//		if(fieldChest!=null){
//
//			NBTTagCompound tag = UtilNBT.compound();
//			fieldChest.writeToNBT(tag);
//			ev.getData().setTag(NBTTAG, tag);
//			ev.getData().setInteger("version", VERSION);
//			UnsagaMod.logger.trace("save chest chunk", ev.getChunk().getChunkCoordIntPair());
//
////			fieldChest.setSaved(true);
////			if(fieldChest.isScheduledUnloaded()){
////				ChestPosCache.instance().getCache().invalidate(ev.getChunk().getChunkCoordIntPair());
////				UnsagaMod.logger.trace("unload chest chunk:", ev.getChunk().getChunkCoordIntPair());
////			}
//		}
//
//	}
//}
