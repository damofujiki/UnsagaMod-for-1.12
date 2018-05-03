package mods.hinasch.unsaga.material;

public class UnsagaMaterials {
	public static final UnsagaMaterial DUMMY = new UnsagaMaterial("dummy");
	public static final UnsagaMaterial FEATHER = new UnsagaMaterial("feather");
	public static final UnsagaMaterial COTTON = new UnsagaMaterial("cotton");

	public static final UnsagaMaterial SILK = new UnsagaMaterial("silk");
	public static final UnsagaMaterial VELVET = new UnsagaMaterial("velvet");
	public static final UnsagaMaterial LIVE_SILK = new UnsagaMaterial("live_silk");
	public static final UnsagaMaterial FUR = new UnsagaMaterial("fur");

	public static final UnsagaMaterial SNAKE_LEATHER = new UnsagaMaterial("snake_leather");
	public static final UnsagaMaterial CROCODILE_LEATHER = new UnsagaMaterial("crocodile_leather");
	public static final UnsagaMaterial HYDRA_LEATHER = new UnsagaMaterial("hydra_leather");
	/** 雑木*/
	public static final UnsagaMaterial WOOD = new UnsagaMaterial("wood");
	/** ヒノキ*/
	public static final UnsagaMaterial CYPRESS = new UnsagaMaterial("cypress");
	public static final UnsagaMaterial OAK = new UnsagaMaterial("oak");
	public static final UnsagaMaterial TONERIKO = new UnsagaMaterial("toneriko");
	public static final UnsagaMaterial TUSK1 = new UnsagaMaterial("tusk1","tusk");

	public static final UnsagaMaterial TUSK2 = new UnsagaMaterial("tusk2","tusk");
	public static final UnsagaMaterial BONE1 = new UnsagaMaterial("bone1","bone");
	public static final UnsagaMaterial BONE2 = new UnsagaMaterial("bone2","bone");
	public static final UnsagaMaterial THIN_SCALE = new UnsagaMaterial("thin_scale");
	/** 甲板*/
	public static final UnsagaMaterial CHITIN = new UnsagaMaterial("chitin");
	public static final UnsagaMaterial ANCIENT_FISH_SCALE = new UnsagaMaterial("ancient_fish_scale");

	public static final UnsagaMaterial DRAGON_SCALE = new UnsagaMaterial("dragon_scale");
	public static final UnsagaMaterial LIGHT_STONE = new UnsagaMaterial("light_stone");

	public static final UnsagaMaterial DARK_STONE = new UnsagaMaterial("dark_stone");
	public static final UnsagaMaterial DEBRIS1 = new UnsagaMaterial("debris1","debris");
	public static final UnsagaMaterial DEBRIS2 = new UnsagaMaterial("debris2","debris");
	/** 朱雀石*/
	public static final UnsagaMaterial CARNELIAN = new UnsagaMaterial("carnelian");
	/** 黄龍石（土）*/
	public static final UnsagaMaterial TOPAZ = new UnsagaMaterial("topaz");
	/** 玄武石（水）*/
	public static final UnsagaMaterial RAVENITE = new UnsagaMaterial("ravenite");
	/** 蒼龍石（木）*/
	public static final UnsagaMaterial LAZULI = new UnsagaMaterial("lazuli");
	/** 白虎石（金）*/
	public static final UnsagaMaterial OPAL = new UnsagaMaterial("opal");
	public static final UnsagaMaterial SERPENTINE = new UnsagaMaterial("serpentine");
	public static final UnsagaMaterial COPPER_ORE = new UnsagaMaterial("copper_ore");
	public static final UnsagaMaterial QUARTZ = new UnsagaMaterial("quartz");
	public static final UnsagaMaterial METEORITE = new UnsagaMaterial("meteorite");
	public static final UnsagaMaterial IRON_ORE = new UnsagaMaterial("iron_ore");
	public static final UnsagaMaterial SILVER = new UnsagaMaterial("silver");

	public static final UnsagaMaterial OBSIDIAN = new UnsagaMaterial("obsidian");
	public static final UnsagaMaterial RUBY = new UnsagaMaterial("ruby","corundum");
	public static final UnsagaMaterial SAPPHIRE = new UnsagaMaterial("sapphire","corundum");
	public static final UnsagaMaterial DIAMOND = new UnsagaMaterial("diamond");
	public static final UnsagaMaterial COPPER = new UnsagaMaterial("copper");
	public static final UnsagaMaterial LEAD = new UnsagaMaterial("lead");
	public static final UnsagaMaterial IRON = new UnsagaMaterial("iron");

	public static final UnsagaMaterial METEORIC_IRON = new UnsagaMaterial("meteoric_iron");
	public static final UnsagaMaterial STEEL1 = new UnsagaMaterial("steel1","steel");
	public static final UnsagaMaterial STEEL2 = new UnsagaMaterial("steel2","steel");
	public static final UnsagaMaterial FAERIE_SILVER = new UnsagaMaterial("faerie_silver");
	public static final UnsagaMaterial DAMASCUS = new UnsagaMaterial("damascus");
	public static final UnsagaMaterial DRAGON_HEART = new UnsagaMaterial("dragon_heart");
	public static final UnsagaMaterial SIVA_QUEEN = new UnsagaMaterial("siva_queen");
	public static final UnsagaMaterial ROADSTER = new UnsagaMaterial("roadster");
	//////////////////こっから追加要素

	public static final UnsagaMaterial JUNGLE_WOOD = new UnsagaMaterial("jungle_wood");
	public static final UnsagaMaterial SPRUCE= new UnsagaMaterial("spruce");
	public static final UnsagaMaterial BIRCH= new UnsagaMaterial("birch");
	public static final UnsagaMaterial ACACIA = new UnsagaMaterial("acacia");
	public static final UnsagaMaterial DARK_OAK = new UnsagaMaterial("dark_oak");
	public static final UnsagaMaterial GOLD = new UnsagaMaterial("gold");
	public static final UnsagaMaterial BRASS = new UnsagaMaterial("brass");
	public static final UnsagaMaterial NICKEL_SILVER = new UnsagaMaterial("nickel_silver");
	public static final UnsagaMaterial CHALCEDONY = new UnsagaMaterial("chalcedony");


	public static final UnsagaMaterial BAMBOO = new UnsagaMaterial("bamboo");
	public static final UnsagaMaterial OSMIUM = new UnsagaMaterial("osmium");

	public static final UnsagaMaterial STONE = new UnsagaMaterial("stone");
	public static final UnsagaMaterial PRISMARINE = new UnsagaMaterial("prismarine");
	public static final UnsagaMaterial SHULKER = new UnsagaMaterial("shulker");


	public static void register(){
		put(FEATHER,COTTON,SILK,VELVET,LIVE_SILK);
		put(FUR,SNAKE_LEATHER,CROCODILE_LEATHER,HYDRA_LEATHER);
		put(WOOD,CYPRESS,TONERIKO,OAK);
		put(TUSK1,TUSK2,BONE1,BONE2);
		put(THIN_SCALE,CHITIN,ANCIENT_FISH_SCALE,DRAGON_SCALE);
		put(LIGHT_STONE,DARK_STONE);
		put(DEBRIS1,DEBRIS2);
		put(CARNELIAN,TOPAZ,RAVENITE,OPAL,LAZULI,SERPENTINE);
		put(COPPER,COPPER_ORE,IRON,IRON_ORE,SILVER,OBSIDIAN);
		put(METEORITE,METEORIC_IRON);
		put(RUBY,SAPPHIRE,OBSIDIAN,QUARTZ);
		put(DIAMOND,LEAD,STEEL1,STEEL2);
		put(FAERIE_SILVER,DAMASCUS,DRAGON_HEART,SIVA_QUEEN,ROADSTER);

		put(JUNGLE_WOOD,BIRCH,SPRUCE,ACACIA,DARK_OAK);
		put(GOLD,BRASS,NICKEL_SILVER,CHALCEDONY,BAMBOO,OSMIUM,STONE);
		put(PRISMARINE,SHULKER);
	}

	public static void put(UnsagaMaterial... m){
		UnsagaMaterialRegistry.instance().register(m);
	}
}
