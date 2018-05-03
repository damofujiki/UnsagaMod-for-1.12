package mods.hinasch.unsaga.element.newele;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mojang.realmsclient.util.Pair;

import mods.hinasch.lib.util.HSLibs;
import mods.hinasch.lib.world.ScannerNew;
import mods.hinasch.unsaga.UnsagaMod;
import mods.hinasch.unsaga.element.FiveElements;
import mods.hinasch.unsaga.plugin.hac.UnsagaPluginHAC;
import mods.hinasch.unsaga.status.UnsagaStatus;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLog;
import net.minecraft.block.BlockOre;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;

public class ElementAssociationLibrary {

	Map<BiomeDictionary.Type,ElementTable> biomeMap = Maps.newHashMap();
	Map<Material,ElementTable> materialMap = Maps.newHashMap();
	Map<Block,ElementTable> blockMap = Maps.newHashMap();

	private static ElementAssociationLibrary INSTANCE;

	public static ElementAssociationLibrary instance(){
		if(INSTANCE == null){
			INSTANCE = new ElementAssociationLibrary();
		}
		return INSTANCE;
	}
	private ElementAssociationLibrary(){

	}
	public void register(){

		this.biomeMap.put(Type.HOT,new ElementTable(FiveElements.Type.FIRE,10));
		this.biomeMap.put(Type.BEACH,new ElementTable(FiveElements.Type.WATER,10));
		this.biomeMap.put(Type.DRY,new ElementTable(FiveElements.Type.WATER,-5));
		this.biomeMap.put(Type.WET,new ElementTable(FiveElements.Type.WATER,5));
		this.biomeMap.put(Type.SANDY,new ElementTable(FiveElements.Type.WATER,-5));
		this.biomeMap.put(Type.FOREST,new ElementTable(FiveElements.Type.WOOD,8));
		this.biomeMap.put(Type.SNOWY,new ElementTable(FiveElements.Type.WATER,10));
		this.biomeMap.put(Type.MAGICAL,new ElementTable(FiveElements.Type.FORBIDDEN,10));
		this.biomeMap.put(Type.WASTELAND,new ElementTable(FiveElements.Type.EARTH,20));
		this.biomeMap.put(Type.HILLS,new ElementTable(FiveElements.Type.EARTH,5));
		this.biomeMap.put(Type.MOUNTAIN,new ElementTable(FiveElements.Type.EARTH,10));
		this.biomeMap.put(Type.MESA,new ElementTable(FiveElements.Type.EARTH,15));
		this.biomeMap.put(Type.JUNGLE,new ElementTable(FiveElements.Type.WOOD,10F));
		this.biomeMap.put(Type.SWAMP,new ElementTable(0,5.0F,0,5.0F,0,0));
		this.biomeMap.put(Type.CONIFEROUS,new ElementTable(Pair.of(FiveElements.Type.WOOD, 5F),Pair.of(FiveElements.Type.WATER, 3F)));
		this.biomeMap.put(Type.NETHER,new ElementTable(FiveElements.Type.FORBIDDEN,15));
		this.biomeMap.put(Type.END,new ElementTable(FiveElements.Type.FORBIDDEN,20));
		this.biomeMap.put(Type.SPOOKY,new ElementTable(FiveElements.Type.FORBIDDEN,10));
		this.biomeMap.put(Type.SPARSE,new ElementTable(FiveElements.Type.WOOD,-1F));

		this.blockMap.put(Blocks.OBSIDIAN, new ElementTable(Pair.of(FiveElements.Type.EARTH, 0.05F),Pair.of(FiveElements.Type.FIRE, 0.05F)));
		this.blockMap.put(Blocks.BEDROCK, new ElementTable(FiveElements.Type.EARTH,1.0F));
		this.blockMap.put(Blocks.LAVA, new ElementTable(FiveElements.Type.FIRE,1.0F));
		this.blockMap.put(Blocks.FLOWING_LAVA, new ElementTable(FiveElements.Type.FIRE,0.5F));
		this.blockMap.put(Blocks.FIRE, new ElementTable(FiveElements.Type.FIRE,1.0F));
		this.blockMap.put(Blocks.FLOWING_WATER, new ElementTable(-0.04F,0,0,0.5F,0,0));
		this.blockMap.put(Blocks.WATER, new ElementTable(-0.04F,0,0,0.8F,0,0));
		this.blockMap.put(Blocks.LOG, new ElementTable(FiveElements.Type.WOOD,0.1F));
		this.blockMap.put(Blocks.LOG2, new ElementTable(FiveElements.Type.WOOD,0.1F));
		this.blockMap.put(Blocks.PLANKS, new ElementTable(FiveElements.Type.WOOD,0.1F));
		this.blockMap.put(Blocks.NETHERRACK, new ElementTable(FiveElements.Type.FORBIDDEN,0.01F));
		this.blockMap.put(Blocks.NETHER_BRICK, new ElementTable(FiveElements.Type.FORBIDDEN,0.05F));
		this.blockMap.put(Blocks.END_STONE, new ElementTable(FiveElements.Type.FORBIDDEN,0.1F));
		this.blockMap.put(Blocks.MAGMA, new ElementTable(1.0F,0,0,0,0,0.5F));
		this.blockMap.put(Blocks.STONE, new ElementTable(FiveElements.Type.EARTH,0.005F));
		this.blockMap.put(Blocks.COBBLESTONE, new ElementTable(FiveElements.Type.EARTH,0.01F));
		this.blockMap.put(Blocks.DIRT, new ElementTable(FiveElements.Type.EARTH,0.005F));
		this.blockMap.put(Blocks.GRAVEL, new ElementTable(FiveElements.Type.EARTH,0.01F));
		this.blockMap.put(Blocks.GRASS, new ElementTable(FiveElements.Type.WOOD,0.01F));
		this.blockMap.put(Blocks.TALLGRASS, new ElementTable(FiveElements.Type.WOOD,0.05F));
		this.blockMap.put(Blocks.REDSTONE_WIRE, new ElementTable(FiveElements.Type.METAL,0.05F));
		this.blockMap.put(Blocks.REDSTONE_TORCH, new ElementTable(FiveElements.Type.METAL,0.05F));

		this.materialMap.put(Material.WATER, new ElementTable(-0.05F,0,0,0.5F,0,0));
		this.materialMap.put(Material.SNOW, new ElementTable(-0.04F,0,0,0.5F,0,0));
		this.materialMap.put(Material.ICE, new ElementTable(-0.04F,0,0,0.5F,0,0));
		this.materialMap.put(Material.CRAFTED_SNOW, new ElementTable(-0.04F,0,0,0.8F,0,0));
		this.materialMap.put(Material.GRASS, new ElementTable(FiveElements.Type.WOOD,0.1F));
		this.materialMap.put(Material.WOOD, new ElementTable(FiveElements.Type.WOOD,0.05F));
		this.materialMap.put(Material.PLANTS, new ElementTable(FiveElements.Type.WOOD,0.02F));
		this.materialMap.put(Material.LAVA, new ElementTable(FiveElements.Type.FIRE,0.08F));
		this.materialMap.put(Material.LEAVES, new ElementTable(FiveElements.Type.WOOD,0.01F));
		this.materialMap.put(Material.IRON, new ElementTable(FiveElements.Type.METAL,1.0F));
		this.materialMap.put(Material.ROCK, new ElementTable(FiveElements.Type.METAL,0.001F));
		this.materialMap.put(Material.PORTAL, new ElementTable(FiveElements.Type.FORBIDDEN,0.3F));
	}

	public static ElementTable calcAllElements(World world,EntityLivingBase ep){
		return calcAllElementsSensitive(world, ep, false);
	}
	public static ElementTable calcAllElementsSensitive(World world,EntityLivingBase ep,boolean ignorePlayerElements){
			ElementTable.Mutable mutable = new ElementTable.Mutable();
			Biome biome = world.getBiome(ep.getPosition());

			mutable.add(instance().calcBiomeElements(world, biome));
			ScannerNew.create().base(ep).range(3, 3, 3).ready().stream().forEach(in ->{
				Optional<ElementTable> tableopt = ElementAssociationLibrary.instance().find(world.getBlockState(in));
				if(tableopt.isPresent()){
					mutable.add(tableopt.get());
				}
			});
			mutable.limit(-10.0F,10.0F);
			if(ignorePlayerElements){
				mutable.add(calcPlayerElements(ep));
			}


			return mutable.toImmutable();
	}
	protected static ElementTable calcPlayerElements(EntityLivingBase ep){
		ElementTable.Mutable mutable = new ElementTable.Mutable();
		for(FiveElements.Type type:FiveElements.Type.values()){
			if(ep.getEntityAttribute(UnsagaStatus.ENTITY_ELEMENTS.get(type))!=null){

				mutable.add(type, (float)ep.getEntityAttribute(UnsagaStatus.ENTITY_ELEMENTS.get(type)).getAttributeValue());
			}
		}
//		UnsagaMod.logger.trace("ElementTable", mutable.toImmutable());
		return mutable.toImmutable();
	}
	protected ElementTable calcBiomeElements(World world,Biome biome){
		ElementTable.Mutable mutable = new ElementTable.Mutable();
		List<BiomeDictionary.Type> types = Lists.newArrayList(BiomeDictionary.getTypes(biome));

		if(world.getWorldTime()>14000){
			mutable.add(FiveElements.Type.FORBIDDEN,1F);
		}else{
			mutable.add(FiveElements.Type.FIRE,1F);
		}

		if(world.isRaining()){
			mutable.add(FiveElements.Type.WATER,2F);
			mutable.add(FiveElements.Type.FIRE,-1F);
		}

		for(BiomeDictionary.Type type:types){
			if(this.biomeMap.containsKey(type)){
				mutable.add(this.biomeMap.get(type));
			}
		}

		return mutable.toImmutable();
	}
	public Optional<ElementTable> find(IBlockState state){

		ElementTable rt = null;
		if(state.getBlock()!=Blocks.AIR  ){
			if(this.isOre(state) && rt==null){
				rt = new ElementTable(FiveElements.Type.METAL,0.3F);
			}
			if(this.isLog(state) && rt==null){
				rt = new ElementTable(FiveElements.Type.WOOD,0.1F);
			}
		}

		if(this.blockMap.containsKey(state.getBlock()) && rt==null){
			rt = this.blockMap.get(state.getBlock());
		}
		if(UnsagaMod.pluginHandler.isLoadedHAC() && rt==null){
			if(UnsagaPluginHAC.getElementsTable(state.getBlock(), state.getBlock().getMetaFromState(state))!=null){
				rt = UnsagaPluginHAC.getElements(state.getBlock(), state.getBlock().getMetaFromState(state));
			}
		}
		if(this.materialMap.containsKey(state.getMaterial()) && rt==null){
			rt = this.materialMap.get(state.getMaterial());
		}
		return rt!=null ? Optional.of(rt) : Optional.empty();
	}
	protected boolean isOre(IBlockState state){
		Block block = state.getBlock();
		return HSLibs.getOreNames(new ItemStack(block,1)).stream().anyMatch(in -> in.startsWith("ore")) || block instanceof BlockOre;
	}

	protected boolean isLog(IBlockState state){
		Block block = state.getBlock();
		return HSLibs.getOreNames(new ItemStack(block,1)).stream().anyMatch(in -> in.startsWith("log")) || block instanceof BlockLog;
	}
}
