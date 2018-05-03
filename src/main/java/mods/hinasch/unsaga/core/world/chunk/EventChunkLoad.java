package mods.hinasch.unsaga.core.world.chunk;

import java.util.Random;

import mods.hinasch.unsaga.UnsagaMod;
import mods.hinasch.unsaga.chest.FieldChestType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DimensionType;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EventChunkLoad {


	@SubscribeEvent
	public void onChunkLoad(ChunkEvent.Load e){
		if(UnsagaChunkCapability.ADAPTER.hasCapability(e.getChunk())){
			Chunk chunk = e.getChunk();
			Random rand = e.getWorld().rand;
			int x = chunk.getPos().x << 4;
			int z = chunk.getPos().z << 4;

			if(rand.nextInt(3)==0){
				FieldChestType type = rand.nextInt(2)==0 ? FieldChestType.CAVE : FieldChestType.FIELD;
				if(e.getWorld().provider.getDimensionType()==DimensionType.NETHER){
					type = FieldChestType.CAVE;
				}
				if(e.getWorld().provider.getDimensionType()==DimensionType.THE_END){
					type = FieldChestType.FIELD;
				}
				UnsagaChunkCapability.ADAPTER.getCapability(chunk).getChunkChestInfo().setFieldChestType(type);
				int y = 0;
				if(type==FieldChestType.FIELD){
					y = chunk.getHeight(new BlockPos(x,0,z));
				}else{
					int height = chunk.getHeight(new BlockPos(x,0,z));
					y = rand.nextInt(height) + 1;
				}
				UnsagaChunkCapability.ADAPTER.getCapability(chunk).getChunkChestInfo().setChestPos(new BlockPos(x,y,z));
				UnsagaMod.logger.trace(this.getClass().getName(), "Chest Generated:",x,y,z);
			}
		}
	}
}
