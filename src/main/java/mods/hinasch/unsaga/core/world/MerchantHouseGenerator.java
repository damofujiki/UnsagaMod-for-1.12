package mods.hinasch.unsaga.core.world;

import java.util.Random;

import mods.hinasch.lib.world.WorldGeneratorBase;
import mods.hinasch.unsaga.UnsagaMod;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;

public class MerchantHouseGenerator extends WorldGeneratorBase{

	Random rand2;

	@Override
	public void generateNether(World world, Random random, int i, int j) {
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	public void generateOverworld(World world, Random rand, int x, int z) {
		if(!UnsagaMod.configs.isEnabledSpawnMerchantHouse()){
			return;
		}



		int chunkX = x >> 4;
		int chunkZ = z >> 4;


		boolean isForce = UnsagaMod.configs.isForceToSpawnMerchantHouse() && !UnsagaWorldCapability.ADAPTER.getCapability(world).getWorldStructureInfo().isSpawnedFirstMerchantHouse();


		rand2 = new Random(world.getSeed() + chunkX + chunkZ * 31);
		rand2.nextFloat();
		int r = (int) (rand2.nextFloat() * 1000);
//		UnsagaMod.logger.trace("rand",r );


		if((r<5 || isForce) && !UnsagaWorldCapability.ADAPTER.getCapability(world).getWorldStructureInfo().isNearStructure(WorldStructureInfo.MERCHANT_HOUSE,new ChunkPos(chunkX,chunkZ))){
			int px = x + rand.nextInt(8);
			int pz = z + rand.nextInt(8);

			BlockPos pos = world.getHeight(new BlockPos(px,0,pz));
			Biome biome = world.getBiome(pos);
			if(world.getVillageCollection().getNearestVillage(pos, 6)!=null){
				return;
			}
			if(BiomeDictionary.hasType(biome, BiomeDictionary.Type.PLAINS)
					|| BiomeDictionary.hasType(biome, BiomeDictionary.Type.SANDY)){
				StructureMerchantHouse house = new StructureMerchantHouse();
				house.build(world, pos);
				UnsagaWorldCapability.ADAPTER.getCapability(world).getWorldStructureInfo().addCoods(WorldStructureInfo.MERCHANT_HOUSE, new ChunkPos(x,z));
//				WorldSaveDataStructure.get(world).addCoods(chunkX, chunkZ);
//				WorldSaveDataStructure.get(world).markDirty();
//				this.generated = true;
			}

		}
	}

	@Override
	public void generateEnd(World world, Random random, int i, int j) {
		// TODO 自動生成されたメソッド・スタブ

	}

}
