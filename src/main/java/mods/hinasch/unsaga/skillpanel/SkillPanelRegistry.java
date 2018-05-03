package mods.hinasch.unsaga.skillpanel;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.google.common.collect.ImmutableList;
import com.mojang.realmsclient.util.Pair;

import mods.hinasch.lib.registry.PropertyRegistry;
import mods.hinasch.lib.util.Statics;
import mods.hinasch.unsaga.core.item.misc.skillpanel.SkillPanelCapability;
import mods.hinasch.unsaga.init.UnsagaItems;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.item.ItemStack;
import net.minecraft.util.WeightedRandom;
import net.minecraft.util.math.MathHelper;

public class SkillPanelRegistry extends PropertyRegistry<SkillPanel>{

	protected static SkillPanelRegistry INSTANCE;

//	public SkillPanel punch,kick,throwing,shield,specialMoveMaster,punchMaster;
//	public SkillPanel ironBody,toughness,ironWill;
//	public SkillPanel familiarFire,familiarWater,familiarEarth,familiarWood,familiarMetal;
//	public SkillPanel magicExpert,thriftSaver;
//	public SkillPanel zombiePhobia,creeperPhobia;
//	public SkillPanel magicBlend;
//	public SkillPanel roadGuide,caveGuide,knowledgeBuildings;
//	public SkillPanel swimming,avoidTrap;
//	public SkillPanel locksmith,defuse,sharpEye;
//	public SkillPanel eavesdrop;
//	public SkillPanel chestMaster;
//	public SkillPanel smartMove,obstacleCrossing;
//
//	/** gratuity=マハラジャ*/
//	public SkillPanel monger,maharaja,fashionable;
//	public SkillPanel artiste,inconspicious;
//	public SkillPanel quickFix;
//	public SkillPanel fortuneTeller,arcaneTongue;
//	public SkillPanel pacifistForAnimals;
//	public SkillPanel toolCustomize,accessoryForge;
//	public SkillPanel watchingOut,fortify;
//	public SkillPanel adaptability,dummy;

	public List<SkillPanel> familiars;
	public List<SkillPanel> damageAgainstSkills;
	public List<SkillPanel> statusUpSkills;
	public List<Pair<SkillPanel,Predicate<Entity>>> negativeSkills1;
	public List<Pair<SkillPanel,Predicate<Entity>>> negativeSkills2;

	public static final int RARITY_MIRACLE = 2;
	public static final int RARITY_ULTRARARE = 5;
	public static final int RARITY_RARE = 10;
	public static final int RARITY_UNCOMMON = 20;
	public static final int RARITY_COMMON = 30;

	public static final AttributeModifier SWIM_MODIFIER = new AttributeModifier(UUID.fromString("509167a3-a2ef-42fb-9dda-f4258b6a88f7"),"skillPanel.swim",30.0D,Statics.OPERATION_INCREMENT);
	public static SkillPanelRegistry instance(){
		if(INSTANCE==null){
			INSTANCE = new SkillPanelRegistry();
		}
		return INSTANCE;
	}
	protected SkillPanelRegistry(){

	}
	@Override
	public void init() {
		// TODO 自動生成されたメソッド・スタブ
		EventSkillPanel.register();
	}

	@Override
	public void preInit() {
		// TODO 自動生成されたメソッド・スタブ
		this.registerObjects();
	}

	@Override
	protected void registerObjects() {

//
//		this.dummy = new SkillPanel("dummy");
//		//宝箱関連
//		this.locksmith = new SkillPanel("unlock");
//		this.defuse = new SkillPanel("defuse");
//		this.sharpEye = new SkillPanel("penetration");
//		this.fortuneTeller = new SkillPanel("divination").setIcon(IconType.ROLL).setRarity(RARITY_UNCOMMON);
//		this.avoidTrap = new SkillPanel("agility");
//		this.chestMaster = new SkillPanel("chest_master").setIcon(IconType.KEY).setRarity(RARITY_ULTRARARE);
//		//店
//		this.artiste = new SkillPanel("luckyfind").setIcon(IconType.COMMUNICATION);
//		this.monger = new SkillPanel("discount").setIcon(IconType.COMMUNICATION).setRarity(RARITY_UNCOMMON);
//		this.maharaja = new SkillPanel("gratuity").setIcon(IconType.COMMUNICATION).setRarity(RARITY_UNCOMMON);
//		this.fashionable = new SkillPanel("fashionable").setIcon(IconType.COMMUNICATION).setRarity(RARITY_RARE);
//		//術
//		this.arcaneTongue = new SkillPanel("arcane_tongue").setIcon(IconType.ROLL);
//		this.magicBlend = new SkillPanel("magic_blend").setIcon(IconType.ROLL).setRarity(RARITY_RARE);
//		this.magicExpert = new SkillPanel("magic_expert").setIcon(IconType.ROLL).setRarity(RARITY_ULTRARARE);
//		//コンバット
//		this.specialMoveMaster = new SkillPanel("weapon_master").setIcon(IconType.MELEE).setRarity(RARITY_ULTRARARE);
//		this.punch = new SkillPanel("punch").setIcon(IconType.MELEE);
//		this.ironBody = new SkillPanel("super_armor").setIcon(IconType.PROTECT).setRarity(RARITY_RARE)
//				.setAttributeModifier(SharedMonsterAttributes.ARMOR, new AttributeModifier(UUID.fromString("1a2e5ddc-08f5-491c-a5a1-9af9d30aae34"),"skillPanel.ironBody",0.25D,0));
//		this.fortify = new SkillPanel("fortify").setIcon(IconType.PROTECT).setRarity(RARITY_RARE)
//				.setAttributeModifier(AdditionalStatus.MENTAL, new AttributeModifier(UUID.fromString("fe8f7d76-6f97-4c50-b133-a4d36cbee89d"),"skillPanel.fortify",0.25D,0));
//		this.toughness = new SkillPanel("toughness").setIcon(IconType.PROTECT).setRarity(RARITY_UNCOMMON).setDamageAgainst(DamageSource.FALL);
//		this.ironWill = new SkillPanel("iron_mind").setIcon(IconType.PROTECT).setRarity(RARITY_RARE).setDamageAgainst(DamageSource.STARVE,DamageSource.WITHER);
//		this.eavesdrop = new SkillPanel("eavesdrop").setIcon(IconType.KEY).setRarity(RARITY_RARE);
//		this.shield = new SkillPanel("shield").setIcon(IconType.PROTECT).setRarity(RARITY_UNCOMMON);
//
//		//案内
//		this.roadGuide = new SkillPanel("road_adviser").setIcon(IconType.ROLL);
//		this.caveGuide = new SkillPanel("cavern_exprorer").setIcon(IconType.ROLL);
//
//		//アイテム
//		this.toolCustomize = new SkillPanel("tool_customize").setIcon(IconType.MELEE).setRarity(RARITY_RARE);
//		this.thriftSaver = new SkillPanel("saving_damage").setIcon(IconType.ROLL).setRarity(RARITY_RARE);
//		this.quickFix = new SkillPanel("easy_repair").setIcon(IconType.ROLL).setRarity(RARITY_RARE);
//		//ネガティヴ
//		this.zombiePhobia = new SkillPanel("zombie_phobia").setIcon(IconType.NEGATIVE).setNegativeSkill(true).setRarity(RARITY_RARE);
//		this.creeperPhobia = new SkillPanel("creeper_phobia").setIcon(IconType.NEGATIVE).setNegativeSkill(true).setRarity(RARITY_RARE);
//		this.pacifistForAnimals = new SkillPanel("no_killing_animals").setIcon(IconType.NEGATIVE).setNegativeSkill(true).setRarity(RARITY_RARE);
//		//ファミリア
//		this.familiarEarth = new SkillPanel("familiar_earth").setIcon(IconType.FAMILIAR).setRarity(RARITY_COMMON).setElement(FiveElements.Type.EARTH);
//		this.familiarFire = new SkillPanel("familiar_Fire").setIcon(IconType.FAMILIAR).setRarity(RARITY_COMMON).setElement(FiveElements.Type.FIRE);
//		this.familiarMetal = new SkillPanel("familiar_metal").setIcon(IconType.FAMILIAR).setRarity(RARITY_COMMON).setElement(FiveElements.Type.METAL);
//		this.familiarWater = new SkillPanel("familiar_water").setIcon(IconType.FAMILIAR).setRarity(RARITY_COMMON).setElement(FiveElements.Type.WATER);
//		this.familiarWood = new SkillPanel("familiar_wood").setIcon(IconType.FAMILIAR).setRarity(RARITY_COMMON).setElement(FiveElements.Type.WOOD);
//		//動作系
//		this.swimming = new SkillPanel("swimming").setIcon(IconType.ROLL).setRarity(RARITY_UNCOMMON);
//		this.adaptability = new SkillPanel("adaptability").setIcon(IconType.ROLL).setRarity(RARITY_RARE);
//		this.smartMove = new SkillPanel("smart_move").setIcon(IconType.KEY).setRarity(RARITY_RARE);
//		this.obstacleCrossing = new SkillPanel("obstacle_crossing").setIcon(IconType.ROLL);
//		//その他
//		this.inconspicious = new SkillPanel("inconspicious").setIcon(IconType.COMMUNICATION).setRarity(RARITY_ULTRARARE);
//
//		//実装するか微妙
//
//		this.accessoryForge = new SkillPanel("accessory_forge").setIcon(IconType.MELEE).setRarity(RARITY_RARE);
//		this.watchingOut = new SkillPanel("watching_out").setRarity(RARITY_RARE);
//		this.knowledgeBuildings = new SkillPanel("knowledge_buildings").setIcon(IconType.ROLL);

//		this.usableSkills = Lists.newArrayList(roadAdviser,cavernExprorer);
		this.familiars = ImmutableList.of(SkillPanels.FAMILIAR_EARTH,SkillPanels.FAMILIAR_FIRE,SkillPanels.FAMILIAR_METAL
				,SkillPanels.FAMILIAR_WATER,SkillPanels.FAMILIAR_WOOD);
		this.damageAgainstSkills = ImmutableList.of(SkillPanels.TOUGHNESS,SkillPanels.IRON_WILL);
		this.negativeSkills1 = ImmutableList.of(Pair.of(SkillPanels.PHOBIA_ZOMBIE, in -> in instanceof EntityZombie),Pair.of(SkillPanels.PHOBIA_CREEPER, in -> in instanceof EntityCreeper));
		this.negativeSkills2 = ImmutableList.of(Pair.of(SkillPanels.PACIFIST_ANIMALS, in -> in instanceof EntityAnimal));
		this.statusUpSkills = ImmutableList.of(SkillPanels.FORTIFY,SkillPanels.IRON_BODY);

		SkillPanels.register();
//		this.put(punch,arcaneTongue,magicBlend,locksmith,defuse,sharpEye,artiste);
//		this.put(monger,maharaja,fortuneTeller,zombiePhobia,creeperPhobia,pacifistForAnimals);
//		this.put(ironBody,toughness,roadGuide,caveGuide);
//		this.put(toolCustomize,ironWill,fashionable);
//		this.put(swimming,adaptability,shield,quickFix);
//		this.put(familiarEarth,familiarFire,familiarWater,familiarMetal,familiarWood);
//		this.put(thriftSaver,eavesdrop,specialMoveMaster,magicExpert,smartMove,obstacleCrossing);
	}

	public void register(SkillPanel... panels){
		this.put(panels);
	}
	public ItemStack getItemStack(SkillPanel panel,int level){
		ItemStack stack = new ItemStack(UnsagaItems.GROWTH_PANEL,1);
		if(SkillPanelCapability.adapter.hasCapability(stack)){
			SkillPanelCapability.adapter.getCapability(stack).setPanel(panel);
			SkillPanelCapability.adapter.getCapability(stack).setLevel(MathHelper.clamp(level, 1, 5));
		}
		return stack;
	}
	public static class WeightedRandomPanel extends WeightedRandom.Item{

		public final  SkillPanel panel;
		public int level;
		public WeightedRandomPanel(int par1,SkillPanel data) {
			super(par1);
			this.panel = data;
			// TODO 自動生成されたコンストラクター・スタブ
		}

		public WeightedRandomPanel(int par1,SkillPanel data,int level){
			this(par1,data);
			this.level = level;

		}

		@Override
		public String toString(){
			return "["+this.panel.getPropertyName()+" level:"+this.level+" weight:"+this.itemWeight+"]";
		}
	}

	public List<WeightedRandomPanel> makeWeightedRandomPanels(){

		return this.getProperties().stream().flatMap(data ->{
			List<WeightedRandomPanel> list = new ArrayList();
//			if(data.isWIP){
//				return list.stream();
//			}
			for(int i=0;i<5;i++){
				list.add(new WeightedRandomPanel(data.getRarity(),data,i));
			}
			return list.stream();
		}).collect(Collectors.toList());

	}
}
