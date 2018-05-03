package mods.hinasch.unsaga.element;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import mods.hinasch.lib.item.ItemUtil;
import mods.hinasch.unsaga.UnsagaMod;
import mods.hinasch.unsaga.element.FiveElements.Type;
import mods.hinasch.unsaga.element.newele.ElementTable;
import net.minecraft.init.Biomes;
import net.minecraft.util.NonNullList;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class BiomeElements {

	public static final int BIOME_ELEMENTS_SIZE = 32;
	static Map<BiomeDictionary.Type,ElementTable> map = Maps.newHashMap();
	static Map<Biome,ElementTable> biomeElement = Maps.newHashMap();
	public static void register(){
		map.put(BiomeDictionary.Type.BEACH, ElementTable.of(FiveElements.Type.WATER, 3.0F).add(FiveElements.Type.EARTH,2.0F));
		map.put(BiomeDictionary.Type.COLD, ElementTable.of(FiveElements.Type.WATER, 1.0F));
		map.put(BiomeDictionary.Type.CONIFEROUS, ElementTable.of(FiveElements.Type.WATER, 1.0F).add(FiveElements.Type.WOOD, 1));
		map.put(BiomeDictionary.Type.DEAD, ElementTable.of(FiveElements.Type.FORBIDDEN, 1.0F));
		map.put(BiomeDictionary.Type.DENSE, ElementTable.of(FiveElements.Type.WOOD, 1.0F));
		map.put(BiomeDictionary.Type.DRY, ElementTable.of(FiveElements.Type.EARTH, 1.0F).add(FiveElements.Type.METAL,1.0F));
		map.put(BiomeDictionary.Type.END, ElementTable.of(FiveElements.Type.FORBIDDEN, 1.0F));
		map.put(BiomeDictionary.Type.FOREST, ElementTable.of(FiveElements.Type.WOOD, 3.0F));
		map.put(BiomeDictionary.Type.HILLS, ElementTable.of(FiveElements.Type.EARTH, 1.0F).add(FiveElements.Type.METAL,1.0F));
		map.put(BiomeDictionary.Type.HOT, ElementTable.of(FiveElements.Type.FIRE, 3.0F));
		map.put(BiomeDictionary.Type.JUNGLE, ElementTable.of(FiveElements.Type.WOOD, 1.0F).add(FiveElements.Type.FIRE,1.0F));
		map.put(BiomeDictionary.Type.LUSH, ElementTable.of(FiveElements.Type.WOOD, 1.0F));
		map.put(BiomeDictionary.Type.MAGICAL, ElementTable.of(FiveElements.Type.FORBIDDEN, 1.0F));
		map.put(BiomeDictionary.Type.MESA, ElementTable.of(FiveElements.Type.EARTH, 1.0F).add(FiveElements.Type.METAL,3.0F));
		map.put(BiomeDictionary.Type.MOUNTAIN, ElementTable.of(FiveElements.Type.EARTH, 2.0F).add(FiveElements.Type.METAL,2.0F));
		map.put(BiomeDictionary.Type.MUSHROOM, ElementTable.of(FiveElements.Type.WOOD, 1.0F).add(FiveElements.Type.FORBIDDEN,1.0F));
		map.put(BiomeDictionary.Type.NETHER, ElementTable.of(FiveElements.Type.FIRE, 1.0F).add(FiveElements.Type.FORBIDDEN,1.0F));
		map.put(BiomeDictionary.Type.OCEAN, ElementTable.of(FiveElements.Type.WATER, 1.0F).add(FiveElements.Type.FORBIDDEN,1.0F));
		map.put(BiomeDictionary.Type.RIVER, ElementTable.of(FiveElements.Type.WATER, 1.0F));
		map.put(BiomeDictionary.Type.SANDY, ElementTable.of(FiveElements.Type.EARTH, 3.0F).add(FiveElements.Type.METAL,2.0F));
		map.put(BiomeDictionary.Type.SAVANNA, ElementTable.of(FiveElements.Type.EARTH, 1.0F).add(FiveElements.Type.FIRE,1.0F));
		map.put(BiomeDictionary.Type.SNOWY, ElementTable.of(FiveElements.Type.WATER, 1.0F).add(FiveElements.Type.METAL,1.0F));
		map.put(BiomeDictionary.Type.SPARSE, ElementTable.of(FiveElements.Type.WATER, 1.0F).add(FiveElements.Type.METAL,1.0F));
		map.put(BiomeDictionary.Type.SPOOKY, ElementTable.of(FiveElements.Type.FORBIDDEN, 2.0F));
		map.put(BiomeDictionary.Type.SWAMP, ElementTable.of(FiveElements.Type.WATER, 1.0F));
		map.put(BiomeDictionary.Type.VOID, ElementTable.of(FiveElements.Type.METAL, 1.0F).add(FiveElements.Type.FORBIDDEN,1.0F));
		map.put(BiomeDictionary.Type.WASTELAND, ElementTable.of(FiveElements.Type.FORBIDDEN, 2.0F));
		map.put(BiomeDictionary.Type.WATER, ElementTable.of(FiveElements.Type.WATER, 4.0F));
		map.put(BiomeDictionary.Type.WET, ElementTable.of(FiveElements.Type.WATER, 1.0F));

	}

	public static void registerBiomes(){
		for(Biome biome:ForgeRegistries.BIOMES){
			ElementTable.Mutable base = new ElementTable.Mutable(new ElementTable(4,4,4,4,4,12));
			ElementTable additional = getElementsFromType(biome);
			for(FiveElements.Type type:FiveElements.VALUES){
				if(base.get(Type.FORBIDDEN)>0){
					int value = additional.getInt(type);
					base.add(type, value);
					base.add(Type.FORBIDDEN, -value);
				}
			}
			biomeElement.put(biome, base.toImmutable());
			UnsagaMod.logger.trace("register biome elements value", biome,biomeElement.get(biome));
		}
	}
	public static void registerBiomeElements(){
		for(Biome biome:ForgeRegistries.BIOMES){
			ElementTable base = getBiomeElements(biome);
			List<FiveElements.Type> list = Lists.newArrayList();
			FiveElements.VALUES.forEach(in ->{
				int amount = base.getInt(in);
				if(amount>0){
					amount = (int) Math.pow(2, amount);
					for(int i=0;i<amount;i++){
						list.add(in);
					}
				}
			});
			int index = 0;
			for(int i=0;i<BIOME_ELEMENTS_SIZE-list.size();i++){
				list.add(FiveElements.Type.fromMeta(index));
				index ++;
			}

			NonNullList<FiveElements.Type> lastValue = ItemUtil.toNonNull(list, FiveElements.Type.FORBIDDEN);
			ElementTable lastTable = ElementTable.of(lastValue);
			biomeElement.put(biome, lastTable);
			UnsagaMod.logger.trace("register biome elements value", lastTable);
		}
	}
	public static ElementTable getBiomeElements(Biome biome){
		if(biomeElement.containsKey(biome)){
			return biomeElement.get(biome);
		}
		return biomeElement.get(Biomes.PLAINS);
	}
	public static ElementTable getElementsFromType(Biome biome){
		Set<BiomeDictionary.Type> types = BiomeDictionary.getTypes(biome);
		ElementTable.Mutable mutable = new ElementTable.Mutable();
		for(BiomeDictionary.Type type:types){
			ElementTable table = map.get(type);
			if(table!=null){
				mutable.add(table);
			}
		}
		return mutable.toImmutable();
	}
}
