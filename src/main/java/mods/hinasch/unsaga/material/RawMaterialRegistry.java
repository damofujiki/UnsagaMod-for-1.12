package mods.hinasch.unsaga.material;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Nullable;

import mods.hinasch.lib.client.ClientHelper.IIconName;
import mods.hinasch.lib.item.ItemProperty;
import mods.hinasch.lib.item.PropertyRegistryItem;
import mods.hinasch.lib.registry.PropertyElementWithID;
import mods.hinasch.lib.util.HSLibs;
import mods.hinasch.unsaga.init.UnsagaItemRegistry;
import mods.hinasch.unsaga.material.RawMaterialRegistry.RawMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.furnace.FurnaceFuelBurnTimeEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.oredict.OreDictionary;

public class RawMaterialRegistry extends PropertyRegistryItem<RawMaterial>{

	protected static RawMaterialRegistry INSTANCE;

	public RawMaterial cotton,silk,liveSilk,velvet,fur,snakeLeather,hydraLeather;
	public RawMaterial cypress,oak,fraxinus,tusk1,tusk2,bone,thinScale;
	public RawMaterial chitin,ancientFishScale,dragonScale,crocodileLeather,lightStone,darkStone;
	public RawMaterial debris1,debris2,carnelian,ravenite,opal,topaz,lapis;
	public RawMaterial meteorite,silver,ruby,sapphire,copper,lead,meteoricIron;
	public RawMaterial steel1,steel2,faerieSilver,damascus,dragonHeart;
	public RawMaterial jungleWood,birch,darkOak,spruce,acacia;
	public static RawMaterialRegistry instance(){
		if(INSTANCE == null){
			INSTANCE = new RawMaterialRegistry();
		}
		return INSTANCE;
	}

	protected RawMaterialRegistry(){


	}
	//	UnsagaMaterialRegistry m = UnsagaRegistries.MATERIALS;
	@Override
	public void init() {
		this.associateToMaterial();
		this.registerOreDicts();
		HSLibs.registerEvent(this);
	}

	@Override
	public void preInit() {
		this.registerObjects();

	}

	public @Nullable RawMaterial getPropertyFromStack(ItemStack is){
		return UnsagaItemRegistry.instance().rawMaterials.getNameForObject(is.getItem());
	}

	public ItemStack getStackFromProperty(RawMaterial prop){
		int damage = prop.getMeta();
		return new ItemStack(UnsagaItemRegistry.instance().rawMaterials.getObject(prop),1);
	}
	public List<String> getIconNames(){
		return this.getProperties().stream().map(in -> in.getIconName()).collect(Collectors.toList());
	}


	protected RawMaterial put(ItemProperty par1){
		return super.put((RawMaterial) par1);
	}

	@Override
	protected void registerObjects() {
		// TODO 自動生成されたメソッド・スタブ
		cotton = this.put(new RawMaterial(1,"cotton",UnsagaMaterials.COTTON).setOreDictID("cloth").setIconName("cloth"));
		silk = this.put(new RawMaterial(2,"silk",UnsagaMaterials.SILK).setOreDictID("cloth").setIconName("cloth"));
		velvet = this.put(new RawMaterial(3,"velvet",UnsagaMaterials.VELVET).setOreDictID("cloth").setIconName("cloth"));
		liveSilk = this.put(new RawMaterial(4,"live_silk",UnsagaMaterials.LIVE_SILK).setOreDictID("cloth").setIconName("cloth"));
		fur = this.put(new RawMaterial(5,"fur",UnsagaMaterials.FUR));
		snakeLeather = this.put(new RawMaterial(6,"snake_leather",UnsagaMaterials.SNAKE_LEATHER).setOreDictID("leather"));
		hydraLeather = this.put(new RawMaterial(7,"hydra_leather",UnsagaMaterials.HYDRA_LEATHER).setOreDictID("leather"));
		crocodileLeather = this.put(new RawMaterial(8,"crocodile_leather",UnsagaMaterials.CROCODILE_LEATHER).setOreDictID("leather"));
		cypress = this.put(new RawMaterial(9,"cypress",UnsagaMaterials.CYPRESS).setItemColored(true).setIconName("woodpile").setOreDictID("woodpile"));
		oak = this.put(new RawMaterial(10,"oak",UnsagaMaterials.OAK).setItemColored(true).setIconName("woodpile").setOreDictID("woodpile"));
		fraxinus = this.put(new RawMaterial(11,"toneriko",UnsagaMaterials.TONERIKO).setItemColored(true).setIconName("woodpile").setOreDictID("woodpile"));
		tusk1 = this.put(new RawMaterial(12,"tusk1","tusk",UnsagaMaterials.TUSK1));
		tusk2 = this.put(new RawMaterial(13,"tusk2","tusk",UnsagaMaterials.TUSK2));
		bone = this.put(new RawMaterial(14,"bone",UnsagaMaterials.BONE2).setOreDictID("bone"));
		thinScale = this.put(new RawMaterial(15,"thin_scale",UnsagaMaterials.THIN_SCALE));
		chitin = this.put(new RawMaterial(16,"chitin",UnsagaMaterials.CHITIN));
		ancientFishScale = this.put(new RawMaterial(17,"ancient_fish_scale",UnsagaMaterials.ANCIENT_FISH_SCALE));
		dragonScale = this.put(new RawMaterial(18,"dragon_scale",UnsagaMaterials.DRAGON_SCALE));
		lightStone = this.put(new RawMaterial(19,"light_stone",UnsagaMaterials.LIGHT_STONE).setOreDictID("gemLightStone"));
		darkStone = this.put(new RawMaterial(20,"dark_stone",UnsagaMaterials.DARK_STONE).setOreDictID("gemDarkStone"));
		debris1 = this.put(new RawMaterial(21,"debris1","debris",UnsagaMaterials.DEBRIS1).setOreDictID("debris")).setAmount(0.125F);
		debris2 = this.put(new RawMaterial(22,"debris2","debris",UnsagaMaterials.DEBRIS2).setOreDictID("debris")).setAmount(0.125F);
		carnelian = this.put(new RawMaterial(23,"carnelian",UnsagaMaterials.CARNELIAN).setOreDictID("gemCarnelian"));
		topaz = this.put(new RawMaterial(24,"topaz",UnsagaMaterials.TOPAZ).setOreDictID("gemTopaz"));
		opal = this.put(new RawMaterial(25,"opal",UnsagaMaterials.OPAL).setOreDictID("gemOpal"));
		ravenite = this.put(new RawMaterial(26,"ravenite",UnsagaMaterials.RAVENITE).setOreDictID("gemRavenite"));
		lapis = this.put(new RawMaterial(27,"lapis",UnsagaMaterials.LAZULI).setOreDictID("gemLapis"));
		meteorite = this.put(new RawMaterial(28,"meteorite",UnsagaMaterials.METEORITE).setOreDictID("stoneMeteorite"));
		silver = this.put(new RawMaterial(29,"silver",UnsagaMaterials.SILVER).setOreDictID("ingotSilver"));
		ruby = this.put(new RawMaterial(30,"ruby",UnsagaMaterials.RUBY).setOreDictID("gemRuby"));
		sapphire = this.put(new RawMaterial(31,"sapphire",UnsagaMaterials.SAPPHIRE).setOreDictID("gemSapphire"));
		copper = this.put(new RawMaterial(32,"copper",UnsagaMaterials.COPPER).setOreDictID("ingotCopper"));
		lead = this.put(new RawMaterial(33,"lead",UnsagaMaterials.LEAD).setOreDictID("ingotLead"));
		meteoricIron = this.put(new RawMaterial(34,"meteoric_iron",UnsagaMaterials.METEORIC_IRON).setOreDictID("ingotMeteoricIron"));
		steel1 = this.put(new RawMaterial(35,"steel1","steel",UnsagaMaterials.STEEL1).setOreDictID("ingotSteel"));
		steel2 = this.put(new RawMaterial(36,"steel2","steel",UnsagaMaterials.STEEL2).setOreDictID("ingotSteel"));
		faerieSilver = this.put(new RawMaterial(37,"faerie_silver",UnsagaMaterials.FAERIE_SILVER).setOreDictID("ingotFaerieSilver"));
		damascus = this.put(new RawMaterial(38,"damascus",UnsagaMaterials.DAMASCUS).setOreDictID("ingotDamascus"));
		dragonHeart = this.put(new RawMaterial(39,"dragon_heart",UnsagaMaterials.DRAGON_HEART)).setAmount(1.0F);
		jungleWood = this.put(new RawMaterial(40,"jungle_wood",UnsagaMaterials.JUNGLE_WOOD).setItemColored(true).setIconName("woodpile").setOreDictID("woodpile"));
		birch = this.put(new RawMaterial(41,"birch",UnsagaMaterials.BIRCH).setItemColored(true).setIconName("woodpile").setOreDictID("woodpile"));
		spruce = this.put(new RawMaterial(42,"spruce",UnsagaMaterials.SPRUCE).setItemColored(true).setIconName("woodpile").setOreDictID("woodpile"));
		acacia = this.put(new RawMaterial(43,"acacia",UnsagaMaterials.ACACIA).setItemColored(true).setIconName("woodpile").setOreDictID("woodpile"));
		darkOak = this.put(new RawMaterial(44,"dark_oak",UnsagaMaterials.DARK_OAK).setItemColored(true).setIconName("woodpile").setOreDictID("woodpile"));
	}

	protected void associateToMaterial(){
		this.getProperties().forEach(in ->{
			ItemStack is = new ItemStack(UnsagaItemRegistry.instance().rawMaterials.getObject(in),1);
			MaterialItemAssociatings.instance().registerAssociation(in.getAssociatedMaterial(), is);
			MaterialItemAssociatings.instance().materialRepairAmountMap.put(is,in.getAmount());
		});
	}
	protected void registerOreDicts(){
		this.getProperties().forEach(in -> {
			if(in.getOreDictID().isPresent()){
				//				UnsagaMod.logger.trace(this.getClass().getName(), this.getStackFromProperty(in));
				OreDictionary.registerOre(in.getOreDictID().get(), getStackFromProperty(in));
			}
		});

		OreDictionary.registerOre("gemBestial", this.carnelian.getItemStack(1));
		OreDictionary.registerOre("gemBestial", this.lapis.getItemStack(1));
		OreDictionary.registerOre("gemBestial", this.opal.getItemStack(1));
		OreDictionary.registerOre("gemBestial", this.topaz.getItemStack(1));
		OreDictionary.registerOre("gemBestial", this.ravenite.getItemStack(1));
		OreDictionary.registerOre("ancientFishScale", this.ancientFishScale.getItemStack(1));
		//		RecipeUtilNew.RecipeShaped.create().setBase("###","PPP").addAssociation('#', "cloth")
		//		.addAssociation('P', new ItemStack(Blocks.PLANKS,1,OreDictionary.WILDCARD_VALUE)).setOutput(new ItemStack(Items.BED,1))
		//		.register();
		//
		//		RecipeUtilNew.RecipeShaped.create().setBase("P").addAssociation('P',"woodpile").setOutput(new ItemStack(Items.STICK,4))
		//		.register();
		//
		//		RecipeUtilNew.RecipeShaped.create().setBase("C").addAssociation('C',this.getStackFromProperty(silk)).setOutput(new ItemStack(Items.STRING,4))
		//		.register();
		//
		//		RecipeUtilNew.RecipeShaped.create().setBase("SS","SS").addAssociation('S', "debris").setOutput(new ItemStack(Blocks.COBBLESTONE,1))
		//		.register();


	}

	@SubscribeEvent
	public void onFurnaceBurn(FurnaceFuelBurnTimeEvent e){
		if(this.getPropertyFromStack(e.getItemStack())!=null){
			RawMaterial prop = this.getPropertyFromStack(e.getItemStack());
			if(prop.getOreDictID().equals("woodpile")){
				e.setBurnTime(80);
			}
		}
	}
	public static class RawMaterial extends ItemProperty implements IIconName{

		final UnsagaMaterial m;
		boolean isItemColor = false;
		String iconname;
		float amount = 0.5F;
		public RawMaterial(int id, String name,UnsagaMaterial m) {
			super(id, name);
			this.m = m;
			this.iconname = name;

			// TODO 自動生成されたコンストラクター・スタブ
		}

		public RawMaterial(int id, String name,String unlname,UnsagaMaterial m) {
			super(id, name,unlname);
			this.m = m;
			this.iconname = unlname;
			// TODO 自動生成されたコンストラクター・スタブ
		}

		@Override
		public int compareTo(PropertyElementWithID o) {
			if(o instanceof RawMaterial){
				return Integer.valueOf(this.getAssociatedMaterial().rank).compareTo(((RawMaterial) o).getAssociatedMaterial().rank);
			}
			return super.compareTo(o);
		}

		public RawMaterial setItemColored(boolean par1){
			this.isItemColor = par1;
			return this;
		}

		public boolean isItemColored(){
			return this.isItemColor;
		}


		/** ツール生成時の耐久力の割合。初期値は0.5（半分）*/
		public float getAmount(){
			return this.amount;
		}

		public RawMaterial setAmount(float par1){
			this.amount = par1;
			return this;
		}
		public UnsagaMaterial getAssociatedMaterial(){
			return this.m;
		}
		@Override
		public Item getItem() {
			// TODO 自動生成されたメソッド・スタブ
			return UnsagaItemRegistry.instance().rawMaterials.getObject(this);
		}
		@Override
		public ItemStack getItemStack(int amount){
			return new ItemStack(this.getItem(),amount);
		}
	}

}
