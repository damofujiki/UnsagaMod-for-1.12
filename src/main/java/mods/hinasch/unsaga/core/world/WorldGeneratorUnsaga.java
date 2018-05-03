package mods.hinasch.unsaga.core.world;

import java.util.List;
import java.util.Random;

import com.google.common.collect.Lists;

import mods.hinasch.lib.core.HSLib;
import mods.hinasch.lib.world.OreGenerator;
import mods.hinasch.lib.world.WorldGeneratorBase;
import mods.hinasch.unsaga.UnsagaMod;
import mods.hinasch.unsaga.chest.FieldChestType;
import mods.hinasch.unsaga.core.world.chunk.UnsagaChunkCapability;
import mods.hinasch.unsaga.init.UnsagaConfigHandler.OreSetting;
import mods.hinasch.unsaga.init.UnsagaOres;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DimensionType;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

public class WorldGeneratorUnsaga extends WorldGeneratorBase{


	MerchantHouseGenerator merchantHouseGenerator = new MerchantHouseGenerator();
	ChestGenerator chestGenerator = new ChestGenerator();
	private static WorldGeneratorUnsaga INSTANCE;

	public static WorldGeneratorUnsaga instance(){
		if(INSTANCE==null){
			INSTANCE = new WorldGeneratorUnsaga();
		}
		return INSTANCE;
	}

	Random rand2;
//	BlockOrePropertyRegistry ores = UnsagaRegistries.ORES;
	OreSetting config = UnsagaMod.configs.getOreSetting();

	OreGenerator copperGen = new OreGenerator(UnsagaOres.COPPER.getBlock(),config.enableCopper).setMinMax(10,40).setDensity(8).setGenerateChance(20);
	OreGenerator leadGen = new OreGenerator(UnsagaOres.LEAD.getBlock(),config.enableLead).setMinMax(30, 80).setDensity(8).setGenerateChance(4);
	OreGenerator silverGen = new OreGenerator(UnsagaOres.SILVER.getBlock(),config.enableSilver).setMinMax(20, 40).setDensity(8).setGenerateChance(2);
	OreGenerator SapphireGen = new OreGenerator(UnsagaOres.SAPPHIRE.getBlock(),config.enableSapphire).setMinMax(0, 24).setDensity(6).setGenerateChance(1);
	OreGenerator rubyGen = new OreGenerator(UnsagaOres.RUBY.getBlock(),config.enableRuby).setMinMax(0, 24).setDensity(6).setGenerateChance(1);
	OreGenerator serpentine;// = new OreGenerator(UnsagaMod.blocks.stonesAndMetals,config.enableSerpentine).setMinMax(0,80).setDensity(15).setGenerateChance(10);
//	OreGenerator chestInStone = new OreGenerator(UnsagaMod.blocks.bonusChest,true).setMinMax(0,60).setDensity(1).setGenerateChance(10);
	protected List<OreGenerator> listOreGeneration = Lists.newArrayList(copperGen, leadGen,silverGen,SapphireGen,rubyGen,serpentine);
	public void generateEnd(World var1, Random var2, int var3, int var4) {

		this.generateChest(var1, var2, var3, var4, var1.provider.getDimensionType());
	}

	public void generateNether(World var1, Random var2, int var3, int var4) {

		this.generateChest(var1, var2, var3, var4, var1.provider.getDimensionType());
	}

	boolean generated = false;
	public synchronized void generateStructure(World world, Random rand, int x, int z) {

	}

	@Override
	public boolean arrowGenerationInOverworld(){
		return HSLib.configHandler.isAllowOreGenerationOverworld();
	}

	public void addOreGeneration(OreGenerator gen){
		this.listOreGeneration.add(gen);
	}

	public void generateChest(World world,Random rand,int x,int z,DimensionType dimType){
		Chunk chunk = world.getChunkFromChunkCoords(x >> 4 , z >> 4);
		UnsagaMod.logger.trace(this.getClass().getName(), chunk);
		if(UnsagaChunkCapability.ADAPTER.hasCapability(chunk)){
//			Chunk chunk = e.getChunk();
//			Random rand = world.rand;
//			int x = chunk.getPos().x << 4;
//			int z = chunk.getPos().z << 4;

			UnsagaMod.logger.trace(this.getClass().getName(), "called");
			if(rand.nextInt(3)==0){
				BlockPos chestPos = null;
				FieldChestType type = rand.nextInt(2)==0 ? FieldChestType.CAVE : FieldChestType.FIELD;
				if(dimType==DimensionType.NETHER){
					type = FieldChestType.CAVE;
				}
				if(dimType==DimensionType.THE_END){
					type = FieldChestType.FIELD;
				}
				UnsagaChunkCapability.ADAPTER.getCapability(chunk).getChunkChestInfo().setFieldChestType(type);
				chestPos = new BlockPos(x,0,z);
				if(type==FieldChestType.FIELD){
					chestPos = world.getHeight(new BlockPos(x,0,z));
				}else{
					BlockPos height = world.getHeight(new BlockPos(x,0,z));
					if(height.getY()>0){
						int y = rand.nextInt(height.getY()) + 1;
						chestPos = new BlockPos(x,y,z);
					}else{
						return;
					}

				}
				if(chestPos.getY()>0){
					UnsagaChunkCapability.ADAPTER.getCapability(chunk).getChunkChestInfo().setChestPos(chestPos);
					UnsagaMod.logger.trace(this.getClass().getName(), "Chest Generated:",chestPos);
				}

			}
		}
//		if(rand.nextInt(5)==0){
//			BlockPos chestPos = new BlockPos(x + rand.nextInt(16),0,z + rand.nextInt(16));
//			BlockPos height = world.getHeight(chestPos);
////			IFieldChest chest = new FieldChestInfo(height, Type.FIELD);
////			UnsagaMod.logger.trace("chest generated", height);
////			ChestPosCache.instance().put(new ChunkPos(chunkX,chunkZ), chest);
//
//			EntityUnsagaChestNew chest = new EntityUnsagaChestNew(world);
//			switch(type){
//			case NETHER:
//				if(rand.nextInt(2)==0){
//					chest.setPosition(height.getX(), height.getY(), height.getZ());
////						if(!world.isMaterialInBB(HSLibs.getBounding(height, 1.0D, 1.0D), Material.LAVA)){
//					chest.setVisiblityType(FieldChestType.CAVE);
////						}
//				}else{
//					chest.setPosition(height.getX(), rand.nextInt(height.getY()+1), height.getZ());
//					chest.setVisiblityType(FieldChestType.CAVE);
//				}
//				break;
//			case OVERWORLD:
//				if(rand.nextInt(2)==0){
//					chest.setPosition(height.getX(), height.getY(), height.getZ());
//					chest.setVisiblityType(FieldChestType.FIELD);
//				}else{
//					chest.setPosition(height.getX(), rand.nextInt(height.getY()+1), height.getZ());
//					chest.setVisiblityType(FieldChestType.CAVE);
//				}
//				break;
//			case THE_END:
//					chest.setPosition(height.getX(), height.getY(), height.getZ());
//					chest.setVisiblityType(FieldChestType.FIELD);
//				break;
//			default:
//				if(rand.nextInt(2)==0){
//					chest.setPosition(height.getX(), height.getY(), height.getZ());
//					chest.setVisiblityType(FieldChestType.FIELD);
//				}else{
//					chest.setPosition(height.getX(), rand.nextInt(height.getY()+1), height.getZ());
//					chest.setVisiblityType(FieldChestType.CAVE);
//				}
//				break;
//
//			}
//			world.spawnEntity(chest);
//		}
	}
	@Override
	public void generateOverworld(World world, Random random, int i, int j) {
		this.chestGenerator.generateOverworld(world, random, i, j);
		this.merchantHouseGenerator.generateOverworld(world, random, i, j);
	}

}
