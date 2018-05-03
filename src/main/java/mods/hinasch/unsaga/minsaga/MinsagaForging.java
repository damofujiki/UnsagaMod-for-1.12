package mods.hinasch.unsaga.minsaga;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import com.google.gson.JsonObject;
import com.mojang.realmsclient.util.Pair;

import mods.hinasch.lib.misc.JsonApplier;
import mods.hinasch.lib.misc.JsonApplier.IJsonApply;
import mods.hinasch.lib.misc.JsonApplier.IJsonParser;
import mods.hinasch.lib.util.HSLibs;
import mods.hinasch.unsaga.UnsagaMod;
import mods.hinasch.unsaga.material.RawMaterialRegistry;
import mods.hinasch.unsaga.minsaga.MinsagaForging.ParserMaterial;
import mods.hinasch.unsaga.villager.smith.SmithMaterialRegistry;
import mods.hinasch.unsaga.villager.smith.SmithMaterialRegistry.IGetItemStack;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.RegistrySimple;
import net.minecraftforge.oredict.OreDictionary;

public class MinsagaForging implements IJsonApply<ParserMaterial>{
	private static final ResourceLocation MATERIALS_RES = new ResourceLocation(UnsagaMod.MODID,"data/minsaga_materials.json");
	public static enum Ability implements IStringSerializable{SEA("Sea"),ABYSS("Abyss"),LOOT("Looting"),FAERIE("Faerie")
		,QUICKSILVER("Quicksilver"),HARVEST("Harvest +1"),METEOR("Meteor"),DARK("dark"),WEAKNESS("weak");

		String name;

		private Ability(String name){
			this.name = name;
		}
		@Override
		public String getName() {
			// TODO 自動生成されたメソッド・スタブ
			return name;
		}


	};

	public static class ParserMaterial implements IJsonParser{

		String name;
		float attackModifier;
		int durabilityToughness;
		int repair;
		ArmorModifier armorModifier;
		float efficiency;
		int cost;

		@Override
		public void parse(JsonObject jo) {
			this.name = jo.get("material").getAsString();
			this.durabilityToughness = Integer.valueOf(jo.get("durability").getAsString());
			this.repair = Integer.valueOf(jo.get("repair").getAsString());
			float armor_melee = Float.valueOf(jo.get("armor.melee").getAsString());
			float armor_magic = Float.valueOf(jo.get("armor.magic").getAsString());
			this.armorModifier = new ArmorModifier(armor_melee,armor_magic);
			this.efficiency = Float.valueOf(jo.get("efficiency").getAsString());
			this.cost = Integer.valueOf(jo.get("cost").getAsString());
			this.attackModifier = Float.valueOf(jo.get("attack").getAsString());
		}

	}
	public static MinsagaForging INSTANCE;

	public static MinsagaForging instance(){
		if(INSTANCE==null){
			INSTANCE = new MinsagaForging();
		}
		return INSTANCE;
	}
	public RegistrySimple<ResourceLocation,MinsagaMaterial> allMaterials = new RegistrySimple();
	public static final MinsagaMaterial EMPTY = new MinsagaMaterial("empty");
	public static final MinsagaMaterial SILVER = new MinsagaMaterial("silver");
	public static final MinsagaMaterial BRONZE = new MinsagaMaterial("bronze");
	/** 朱砂*/
	public static final MinsagaMaterial CINNABAR = new MinsagaMaterial("cinnabar");
	/** 蒼鉛*/
	public static final MinsagaMaterial BISMUTH = new MinsagaMaterial("bismuth");
	public static final MinsagaMaterial ELECTRUM = new MinsagaMaterial("electrum");
	/** 白鉄鉱*/
	public static final MinsagaMaterial MARCASITE = new MinsagaMaterial("marcasite");
	public static final MinsagaMaterial METEORIC_IRON = new MinsagaMaterial("meteoricIron");
	/** 甲獣のカラ*/
	public static final MinsagaMaterial HARD_SHELL = new MinsagaMaterial("hard_shell");
	public static final MinsagaMaterial DAMASCUS = new MinsagaMaterial("damascus");
	public static final MinsagaMaterial ABYSS_CRYSTAL = new MinsagaMaterial("abyss_crystal");

	public static final MinsagaMaterial DARK_CRYSTAL = new MinsagaMaterial("dark_crystal");
	/** 魔草の種子*/
	public static final MinsagaMaterial DEVILS_WEED_SEED = new MinsagaMaterial("devilsweed_seed");
	public static final MinsagaMaterial GOLD = new MinsagaMaterial("gold");
	/** 怪魚の石鱗*/
	public static final MinsagaMaterial ANCIENT_FISH_SCALE = new MinsagaMaterial("ancient_fish_scale");
	/** 精霊銀プレート*/
	public static final MinsagaMaterial FAERIE_SILVER_PLATE = new MinsagaMaterial("faerie_silver");
	public static final MinsagaMaterial HALITE = new MinsagaMaterial("halite");
	/** 樹精結晶*/
	public static final MinsagaMaterial AMBER = new MinsagaMaterial("amber");

	public static final MinsagaMaterial DEBRIS1 = new MinsagaMaterial("debris1");
	public static final MinsagaMaterial DEBRIS2 = new MinsagaMaterial("debris2");
	public static final MinsagaMaterial DEBRIS3 = new MinsagaMaterial("debris3");
	public static final MinsagaMaterial DEBRIS4 = new MinsagaMaterial("debris4");
	public static final MinsagaMaterial DEBRIS5 = new MinsagaMaterial("debris5");
	public static final MinsagaMaterial DEBRIS6 = new MinsagaMaterial("debris6");
	public static final MinsagaMaterial DEBRIS7 = new MinsagaMaterial("debris7");

	protected MinsagaForging(){

	}
	public void init(){
		UnsagaMod.logger.get().info("registering minsaga forging properties...");
		this.initPredicates();
//		this.initModifiers();
//		this.initRepairData();
//		this.initAbility();
//		this.initWeight();
//		this.initDurability();
		this.registerMaterials();
		this.loadJson(MATERIALS_RES);
		MinsagaForgingEvent.registerEvents();
		//		UnsagaMod.logger.trace("test", this.allMaterials.getKeys().stream().map(in -> this.allMaterials.getObject(in)).collect(Collectors.toList()));
	}
	private void registerMaterials(){
		this.registerMaterial(EMPTY);
		this.registerMaterial(SILVER);
		this.registerMaterial(BRONZE);
		this.registerMaterial(CINNABAR);
		this.registerMaterial(BISMUTH);
		this.registerMaterial(ELECTRUM);
		this.registerMaterial(MARCASITE);
		this.registerMaterial(METEORIC_IRON);
		this.registerMaterial(HARD_SHELL);
		this.registerMaterial(DAMASCUS);
		this.registerMaterial(ABYSS_CRYSTAL);
		this.registerMaterial(DARK_CRYSTAL);
		this.registerMaterial(GOLD);
		this.registerMaterial(ANCIENT_FISH_SCALE);
		this.registerMaterial(FAERIE_SILVER_PLATE);
		this.registerMaterial(HALITE);
		this.registerMaterial(AMBER);
		this.registerMaterial(DEVILS_WEED_SEED);
		this.registerMaterial(DEBRIS1);
		this.registerMaterial(DEBRIS2);
		this.registerMaterial(DEBRIS3);
		this.registerMaterial(DEBRIS4);
		this.registerMaterial(DEBRIS5);
		this.registerMaterial(DEBRIS6);
		this.registerMaterial(DEBRIS7);

	}


	public void loadJson(ResourceLocation loadFile){
		JsonApplier<MinsagaForging,ParserMaterial> applier = new JsonApplier(loadFile,ParserMaterial::new,in ->this);
		applier.parseAndApply();
	}
	public Optional<MinsagaMaterial> getMaterialFromItemStack(ItemStack is){
		return this.allMaterials.getKeys().stream().map(in -> this.allMaterials.getObject(in))
				.filter(in -> in.isMaterialItem(is)).findFirst();
	}

	private void registerMaterial(MinsagaMaterial material){
		UnsagaMod.logger.trace("registering minsaga material", material.getPropertyName());
		this.allMaterials.putObject(new ResourceLocation(material.getPropertyName()),material);
	}

	public RegistrySimple<ResourceLocation,MinsagaMaterial> registry(){
		return this.allMaterials;
	}
//	private void initDurability(){
//		SILVER.setDurabilityModifier(+2);
//		BRONZE.setDurabilityModifier(+1);
//		CINNABAR.setDurabilityModifier(+1);
//		BISMUTH.setDurabilityModifier(+1);
//		MARCASITE.setDurabilityModifier(-1);
//		METEORIC_IRON.setDurabilityModifier(-1);
//		HARD_SHELL.setDurabilityModifier(+1);
//		DAMASCUS.setDurabilityModifier(-1);
//		ABYSS_CRYSTAL.setDurabilityModifier(-3);
//		DARK_CRYSTAL.setDurabilityModifier(-3);
//		GOLD.setDurabilityModifier(+2);
//		ANCIENT_FISH_SCALE.setDurabilityModifier(+1);
//		FAERIE_SILVER_PLATE.setDurabilityModifier(+1);
//		AMBER.setDurabilityModifier(+1);
//		HALITE.setDurabilityModifier(-2);
//		DEVILS_WEED_SEED.setDurabilityModifier(0);
//		DEBRIS1.setDurabilityModifier(0);
//		DEBRIS2.setDurabilityModifier(+1);
//		DEBRIS3.setDurabilityModifier(-1);
//		DEBRIS4.setDurabilityModifier(-1);
//		DEBRIS5.setDurabilityModifier(-2);
//		DEBRIS6.setDurabilityModifier(-3);
//		DEBRIS7.setDurabilityModifier(-2);
//	}
	private void initPredicates(){
		SILVER.setMaterialChecker(this.getOreDictChecker("ingotSilver"));
		BRONZE.setMaterialChecker(this.getOreDictChecker("ingotBronze"));
		CINNABAR.setMaterialChecker(this.getOreDictChecker("crystalCinnabar"));
		BISMUTH.setMaterialChecker(this.getOreDictChecker("ingotBismuth"));
		ELECTRUM.setMaterialChecker(this.getOreDictChecker("ingotElectrum"));
		MARCASITE.setMaterialChecker(this.getOreDictChecker("oreMarcasite"));
		METEORIC_IRON.setMaterialChecker(this.getOreDictChecker("meteoricIron"));
		HARD_SHELL.setMaterialChecker(this.getOreDictChecker("hardShell"));
		DAMASCUS.setMaterialChecker(this.getOreDictChecker("ingotDamascusSteel"));
		ABYSS_CRYSTAL.setMaterialChecker(this.getOreDictChecker("enderpearl"));
		DARK_CRYSTAL.setMaterialChecker(this.getOreDictChecker("gemDemonite"));
		GOLD.setMaterialChecker(this.getOreDictChecker("ingotGold"));
		ANCIENT_FISH_SCALE.setMaterialChecker(this.getOreDictChecker("scaleFish"));
		FAERIE_SILVER_PLATE.setMaterialChecker(this.getOreDictChecker("ingotFaerieSilver"));
		AMBER.setMaterialChecker(this.getOreDictChecker("gemAmber"));
		DEVILS_WEED_SEED.setMaterialChecker(this.getOreDictChecker("cropNetherWart"));
		DEBRIS1.setMaterialChecker(this.getItemStackChecker(RawMaterialRegistry.instance().debris1.getItem(), RawMaterialRegistry.instance().debris1.getMeta()));
		DEBRIS2.setMaterialChecker(this.getItemStackChecker(RawMaterialRegistry.instance().debris2.getItem(), RawMaterialRegistry.instance().debris2.getMeta()));
	}

//	private void initWeight(){
//		SILVER.setWeight(2);
//		BRONZE.setWeight(1);
//		CINNABAR.setWeight(2);
//		BISMUTH.setWeight(2);
//		ELECTRUM.setWeight(2);
//		MARCASITE.setWeight(1);
//		METEORIC_IRON.setWeight(2);
//		HARD_SHELL.setWeight(1);
//		DAMASCUS.setWeight(2);
//		ABYSS_CRYSTAL.setWeight(2);
//		DARK_CRYSTAL.setWeight(1);
//		GOLD.setWeight(3);
//		AMBER.setWeight(1);
//		ANCIENT_FISH_SCALE.setWeight(2);
//		FAERIE_SILVER_PLATE.setWeight(1);
//		HALITE.setWeight(1);
//		DEVILS_WEED_SEED.setWeight(1);
//		DEBRIS1.setWeight(1);
//		DEBRIS2.setWeight(1);
//		DEBRIS3.setWeight(1);
//		DEBRIS4.setWeight(1);
//		DEBRIS5.setWeight(1);
//		DEBRIS6.setWeight(2);
//		DEBRIS7.setWeight(1);
//	}
//	private void initRepairData(){
//		SILVER.setRepairDamage(20).setRepairCost(-1);
//		BRONZE.setRepairDamage(40).setRepairCost(-1);
//		CINNABAR.setRepairDamage(40).setRepairCost(+1);
//		BISMUTH.setRepairCost(-2).setRepairDamage(60);
//		ELECTRUM.setRepairDamage(30).setRepairCost(-2);
//		MARCASITE.setRepairDamage(30).setRepairCost(+2);
//		METEORIC_IRON.setRepairDamage(40).setRepairCost(+2);
//		HARD_SHELL.setRepairDamage(15).setRepairCost(-1);
//		DAMASCUS.setRepairDamage(40).setRepairCost(-1);
//		ABYSS_CRYSTAL.setRepairCost(+3).setRepairDamage(30);
//		DARK_CRYSTAL.setRepairCost(+3).setRepairDamage(30);
//		GOLD.setRepairDamage(40).setRepairCost(-1);
//		ANCIENT_FISH_SCALE.setRepairCost(-1).setRepairDamage(20);
//		FAERIE_SILVER_PLATE.setRepairCost(-1).setRepairDamage(20);
//		HALITE.setRepairDamage(15).setRepairCost(+2);
//		AMBER.setRepairCost(+1).setRepairDamage(15);
//		DEVILS_WEED_SEED.setRepairCost(+1).setRepairDamage(10);
//		DEBRIS1.setRepairDamage(30);
//		DEBRIS2.setRepairCost(-1).setRepairDamage(15);
//
//	}
//
//	private void initModifiers(){
//		SILVER.setAttackModifier(-0.5F).setArmorModifier(0.0F,0.5F).setEfficiencyModifier(-0.5F);
//		BRONZE.setAttackModifier(+0.5F).setArmorModifier(0.5F,0.0F).setEfficiencyModifier(1.0F);
//		CINNABAR.setArmorModifier(0.5F, 0.5F);
//		BISMUTH.setAttackModifier(-0.5F).setArmorModifier(0.2F, 0.3F).setEfficiencyModifier(-0.5F);
//		ELECTRUM.setArmorModifier(-0.5F,0.5F).setEfficiencyModifier(-0.5F);
//		MARCASITE.setArmorModifier(0.5F,-0.5F).setEfficiencyModifier(1.0F);
//		METEORIC_IRON.setAttackModifier(0.5F).setArmorModifier(0.0F,0.5F).setEfficiencyModifier(1.5F);
//		HARD_SHELL.setArmorModifier(0.3F, 0.3F);
//		DAMASCUS.setAttackModifier(+1.5F).setArmorModifier(1.5F, -0.5F).setEfficiencyModifier(2.0F);
//		ABYSS_CRYSTAL.setAttackModifier(+1.5F).setArmorModifier(-1.0F, 1.0F);
//		DARK_CRYSTAL.setAttackModifier(+1.5F).setArmorModifier(-1.0F, 1.0F);
//		GOLD.setAttackModifier(-0.5F).setArmorModifier(-0.5F, 0.5F).setEfficiencyModifier(-0.5F);
//		ANCIENT_FISH_SCALE.setRepairCost(-1).setArmorModifier(0.3F, -0.3F);
//		FAERIE_SILVER_PLATE.setRepairCost(-1).setArmorModifier(0.4F, 0.4F);
//		AMBER.setAttackModifier(+0.5F).setArmorModifier(0.0F, 1.0F);
//		HALITE.setAttackModifier(-1.0F).setArmorModifier(0.0F, -0.3F);
//		DEVILS_WEED_SEED.setAttackModifier(-0.5F).setArmorModifier(+0.5F, 0);
//		DEBRIS1.setAttackModifier(-1.0F).setArmorModifier(0.3F, 0.3F);
//		DEBRIS2.setAttackModifier(-0.5F).setArmorModifier(0.0F, 0.3F);
//	}
//
//	private void initAbility(){
//		ANCIENT_FISH_SCALE.setAbility(Ability.SEA);
//		ABYSS_CRYSTAL.setAbility(Ability.ABYSS);
//		DARK_CRYSTAL.setAbility(Ability.DARK);
//		FAERIE_SILVER_PLATE.setAbility(Ability.FAERIE);
//		ELECTRUM.setAbility(Ability.LOOT);
//		CINNABAR.setAbility(Ability.QUICKSILVER);
//		METEORIC_IRON.setAbility(Ability.METEOR);
//		DEVILS_WEED_SEED.setAbility(MinsagaForging.Ability.WEAKNESS);
//
//
//	}
	private Predicate<ItemStack> getOreDictChecker(String orename){
		return new SmithMaterialRegistry.PredicateOre(orename);
	}

	private Predicate<ItemStack> getItemStackChecker(Item item,int damage){
		return new SmithMaterialRegistry.PredicateItem(item, damage);
	}

	public MinsagaMaterial getMaterial(String name){
		ResourceLocation res = new ResourceLocation(name);
		if(this.allMaterials.containsKey(res)){
			return this.allMaterials.getObject(res);
		}
		return this.DEBRIS1;
	}
	public static class OreNameChecker implements Predicate<ItemStack>,IGetItemStack{

		final String orename;

		public OreNameChecker(String orename){
			this.orename = orename;
		}

		public String getOreName(){
			return this.orename;
		}
		@Override
		public boolean test(ItemStack is) {
			//			UnsagaMod.logger.trace("ores", this.getOreName(),HSLibs.getOreNames(is));
			if(is!=null){

				return HSLibs.getOreNames(is).stream().anyMatch(in -> in.equals(this.getOreName()));
			}
			return false;
		}

		@Override
		public List<ItemStack> getItemStack() {
			// TODO 自動生成されたメソッド・スタブ
			return OreDictionary.getOres(orename);
		}

	}


	public static class ArmorModifier extends Pair<Float,Float>{
		public static final ArmorModifier ZERO = new ArmorModifier(0F,0F);
		public ArmorModifier(Float first, Float second) {
			super(first, second);
			// TODO 自動生成されたコンストラクター・スタブ
		}

		public float melee(){
			return this.first();
		}

		public float magic(){
			return this.second();
		}
	}


	@Override
	public void applyJson(ParserMaterial parser) {
		MinsagaMaterial material = allMaterials.getObject(new ResourceLocation(parser.name));
		if(material!=null){
			material.setAttackModifier(0.5F*(parser.attackModifier*10));
			material.setArmorModifier(0.5F*(parser.armorModifier.melee()*10), 0.5F*(parser.armorModifier.magic()));
			material.setDurabilityModifier(parser.durabilityToughness);
			material.setEfficiencyModifier(parser.efficiency);
			material.setRepairCost(parser.repair);
			material.setRepairCost(parser.cost);
		}
	}
}
