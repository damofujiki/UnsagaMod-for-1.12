package mods.hinasch.unsaga.core.world;

import java.util.Random;

import mods.hinasch.lib.world.WorldGeneratorBase;
import mods.hinasch.unsaga.UnsagaMod;
import mods.hinasch.unsaga.chest.FieldChestType;
import mods.hinasch.unsaga.core.world.chunk.IUnsagaChunk;
import mods.hinasch.unsaga.core.world.chunk.UnsagaChunkCapability;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DimensionType;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

public class ChestGenerator extends WorldGeneratorBase{

	@Override
	public void generateNether(World world, Random random, int i, int j) {
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	public void generateOverworld(World world, Random rand, int gx, int gz) {
		Chunk chunk = world.getChunkFromChunkCoords(gx >> 4 , gz >> 4);
		UnsagaMod.logger.trace(this.getClass().getName(), chunk);
		rand.nextFloat();
		if(UnsagaChunkCapability.ADAPTER.hasCapability(chunk)){
//			Chunk chunk = e.getChunk();
//			Random rand = world.rand;
//			int x = chunk.getPos().x << 4;
//			int z = chunk.getPos().z << 4;
			DimensionType dimType = world.provider.getDimensionType();
			IUnsagaChunk instance = UnsagaChunkCapability.ADAPTER.getCapability(chunk);
			if(instance.getChunkChestInfo().hasInitialized()){
				return;
			}
			instance.getChunkChestInfo().setHasInitialized(true);
//			UnsagaMod.logger.trace(this.getClass().getName(), "called");
			if(rand.nextInt(3)==0){

				BlockPos chestPos = null;
				FieldChestType type = rand.nextInt(2)==0 ? FieldChestType.CAVE : FieldChestType.FIELD;
				if(dimType==DimensionType.NETHER){
					type = FieldChestType.CAVE;
				}
				if(dimType==DimensionType.THE_END){
					type = FieldChestType.FIELD;
				}
				instance.getChunkChestInfo().setFieldChestType(type);
				int x = gx + rand.nextInt(16);
				int z = gz + rand.nextInt(16);
				chestPos = new BlockPos(x,0,z);
				if(type==FieldChestType.FIELD){ //チェストタイプがフィールドなら一番上に配置
					chestPos = world.getHeight(new BlockPos(x,0,z));
				}else{ //それ以外なら地中に配置
					BlockPos height = world.getHeight(new BlockPos(x,0,z));
					if(height.getY()>0){
						int y = rand.nextInt(height.getY()) + 1;
						chestPos = new BlockPos(x,y,z);
					}else{
						return;
					}

				}
				if(chestPos.getY()>0){
					instance.getChunkChestInfo().setChestPos(chestPos);
					UnsagaMod.logger.trace(this.getClass().getName(), "Chest Generated:",chestPos);
				}

			}
		}
	}

	@Override
	public void generateEnd(World world, Random random, int i, int j) {
		// TODO 自動生成されたメソッド・スタブ

	}

}
