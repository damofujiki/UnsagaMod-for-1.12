package mods.hinasch.unsaga.villager.bartering;

import java.util.EnumSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.google.common.collect.Maps;

import mods.hinasch.lib.util.HSLibs;
import mods.hinasch.unsaga.material.UnsagaMaterial;
import mods.hinasch.unsaga.material.UnsagaMaterials;

public class BarteringMaterialCategory {

	public static enum Type{
		FEATHER("feather"),CLOTH("cloth"),LEATHER("leather"),WOOD("wood"),BONE("bone")
		,SCALE("scale"),JEWEL_MAGICAL("preciousJewel"),
		ORE("ore"),BESTIAL("bestial"),JEWEL("jewel"),METAL("metal"),METAL_PRECIOUS("preciousMetal"),RARE("rare"),UNKNOWN("unknown");

		final String name;
		private Type(String name){
			this.name = name;
		}

		public String getName(){
			return this.name;
		}

		public String getLocalized(){
			return HSLibs.translateKey("gui.unsaga.bartering.shopType."+this.getName());
		}
		public Set<UnsagaMaterial> getMaterials(){
			return BarteringMaterialCategory.instance().getMaterialsFromType(this);
		}
	}

	public final EnumSet<Type> merchandises = EnumSet.of(Type.FEATHER,Type.CLOTH,Type.LEATHER
			,Type.WOOD,Type.BONE,Type.SCALE,Type.JEWEL,Type.JEWEL_MAGICAL
			,Type.ORE,Type.BESTIAL,Type.JEWEL,Type.METAL,Type.METAL_PRECIOUS);
	private static BarteringMaterialCategory INSTANCE;

	public static BarteringMaterialCategory instance(){
		if(INSTANCE == null){
			INSTANCE = new BarteringMaterialCategory();
		}
		return INSTANCE;
	}
	protected BarteringMaterialCategory(){

	}
	Map<UnsagaMaterial,Type> merchandiseMaterialCategory = Maps.newHashMap();
//	UnsagaMaterialRegistry m = UnsagaRegistries.MATERIALS;
	public void init(){
		this.add(Type.FEATHER, UnsagaMaterials.FEATHER);
		this.add(Type.CLOTH, UnsagaMaterials.COTTON,UnsagaMaterials.SILK,UnsagaMaterials.VELVET,UnsagaMaterials.LIVE_SILK);
		this.add(Type.LEATHER, UnsagaMaterials.FUR,UnsagaMaterials.SNAKE_LEATHER,UnsagaMaterials.CROCODILE_LEATHER,UnsagaMaterials.HYDRA_LEATHER);
		this.add(Type.WOOD, UnsagaMaterials.WOOD,UnsagaMaterials.CYPRESS,UnsagaMaterials.OAK,UnsagaMaterials.TONERIKO,UnsagaMaterials.ACACIA,UnsagaMaterials.BIRCH,UnsagaMaterials.SPRUCE,UnsagaMaterials.DARK_OAK,UnsagaMaterials.JUNGLE_WOOD);
		this.add(Type.BONE, UnsagaMaterials.TUSK1,UnsagaMaterials.TUSK2,UnsagaMaterials.BONE1,UnsagaMaterials.BONE2);
		this.add(Type.SCALE, UnsagaMaterials.THIN_SCALE,UnsagaMaterials.CHITIN,UnsagaMaterials.ANCIENT_FISH_SCALE,UnsagaMaterials.DRAGON_SCALE);
		this.add(Type.JEWEL_MAGICAL, UnsagaMaterials.DARK_STONE,UnsagaMaterials.LIGHT_STONE);
		this.add(Type.ORE, UnsagaMaterials.DEBRIS1,UnsagaMaterials.DEBRIS2);
		this.add(Type.BESTIAL, UnsagaMaterials.CARNELIAN,UnsagaMaterials.OPAL,UnsagaMaterials.RAVENITE,UnsagaMaterials.TOPAZ,UnsagaMaterials.LAZULI,UnsagaMaterials.SERPENTINE);
		this.add(Type.ORE, UnsagaMaterials.COPPER_ORE,UnsagaMaterials.QUARTZ,UnsagaMaterials.METEORITE,UnsagaMaterials.IRON_ORE,UnsagaMaterials.DEBRIS1,UnsagaMaterials.DEBRIS2);
		this.add(Type.JEWEL, UnsagaMaterials.SILVER,UnsagaMaterials.OBSIDIAN,UnsagaMaterials.RUBY,UnsagaMaterials.SAPPHIRE,UnsagaMaterials.DIAMOND);
		this.add(Type.METAL, UnsagaMaterials.COPPER,UnsagaMaterials.LEAD,UnsagaMaterials.IRON,UnsagaMaterials.METEORIC_IRON,UnsagaMaterials.STEEL1,UnsagaMaterials.STEEL2,UnsagaMaterials.GOLD,UnsagaMaterials.OSMIUM);
		this.add(Type.METAL_PRECIOUS,UnsagaMaterials.FAERIE_SILVER,UnsagaMaterials.DAMASCUS);
		this.add(Type.RARE, UnsagaMaterials.SIVA_QUEEN);
		this.add(Type.UNKNOWN, UnsagaMaterials.DUMMY);
	}

	private void add(Type type,UnsagaMaterial... materials){
		for(UnsagaMaterial mate:materials){
			merchandiseMaterialCategory.put(mate, type);
		}
	}

	public Set<UnsagaMaterial> getMaterialsFromType(BarteringMaterialCategory.Type type){
		return this.merchandiseMaterialCategory.entrySet().stream().filter(in -> in.getValue()==type).map(in -> in.getKey()).collect(Collectors.toSet());
	}


	public static Type getType(UnsagaMaterial m){
		if(BarteringMaterialCategory.instance().merchandiseMaterialCategory.containsKey(m)){
			return BarteringMaterialCategory.instance().merchandiseMaterialCategory.get(m);
		}
		return Type.UNKNOWN;
	}
}
