package mods.hinasch.unsaga.skillpanel;

import java.util.UUID;

import mods.hinasch.unsaga.element.FiveElements;
import mods.hinasch.unsaga.skillpanel.SkillPanel.IconType;
import mods.hinasch.unsaga.status.UnsagaStatus;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.util.DamageSource;

public class SkillPanels {
	public static final SkillPanel DUMMY = new SkillPanel("dummy");
	//宝箱関連
	public static final SkillPanel LOCKSMITH = new SkillPanel("unlock");
	public static final SkillPanel DEFUSE = new SkillPanel("defuse");
	/** 見破る*/
	public static final SkillPanel SHARP_EYE = new SkillPanel("penetration");
	public static final SkillPanel FORTUNE = new SkillPanel("divination").setIcon(IconType.ROLL).setRarity(SkillPanelRegistry.RARITY_UNCOMMON);
	public static final SkillPanel AVOID_TRAP = new SkillPanel("agility");
	public static final SkillPanel CHEST_MASTER = new SkillPanel("chest_master").setIcon(IconType.KEY).setRarity(SkillPanelRegistry.RARITY_ULTRARARE);
	//店
	/** 目利き（隠し商品発見・アビ引き出し率アップ）*/
	public static final SkillPanel ARTISTE = new SkillPanel("artiste").setIcon(IconType.COMMUNICATION);
	/** 値切り*/
	public static final SkillPanel MONGER = new SkillPanel("discount").setIcon(IconType.COMMUNICATION).setRarity(SkillPanelRegistry.RARITY_UNCOMMON);
	public static final SkillPanel MAHARAJA = new SkillPanel("gratuity").setIcon(IconType.COMMUNICATION).setRarity(SkillPanelRegistry.RARITY_UNCOMMON);
	public static final SkillPanel FASHIONABLE = new SkillPanel("fashionable").setIcon(IconType.COMMUNICATION).setRarity(SkillPanelRegistry.RARITY_RARE);
	//術
	public static final SkillPanel ARCANE_TONGUE = new SkillPanel("arcane_tongue").setIcon(IconType.ROLL);
	public static final SkillPanel MAGIC_BLEND = new SkillPanel("magic_blend").setIcon(IconType.ROLL).setRarity(SkillPanelRegistry.RARITY_RARE);
	public static final SkillPanel MAGIC_EXPERT = new SkillPanel("magic_expert").setIcon(IconType.ROLL).setRarity(SkillPanelRegistry.RARITY_ULTRARARE);
	//コンバット
	public static final SkillPanel WEAPON_MASTER = new SkillPanel("weapon_master").setIcon(IconType.MELEE).setRarity(SkillPanelRegistry.RARITY_ULTRARARE);
	public static final SkillPanel PUNCH = new SkillPanel("punch").setIcon(IconType.MELEE);
	public static final SkillPanel GUN = new SkillPanel("gun").setIcon(IconType.MELEE);
	/** 肉の鎧（防御がアップ）*/
	public static final SkillPanel IRON_BODY = new SkillPanel("super_armor").setIcon(IconType.PROTECT).setRarity(SkillPanelRegistry.RARITY_RARE)
			.setAttributeModifier(SharedMonsterAttributes.ARMOR, new AttributeModifier(UUID.fromString("1a2e5ddc-08f5-491c-a5a1-9af9d30aae34"),"skillPanel.ironBody",0.25D,0));
	/** 不屈（精神がアップ）*/
	public static final SkillPanel FORTIFY = new SkillPanel("fortify").setIcon(IconType.PROTECT).setRarity(SkillPanelRegistry.RARITY_RARE)
			.setAttributeModifier(UnsagaStatus.MENTAL, new AttributeModifier(UUID.fromString("fe8f7d76-6f97-4c50-b133-a4d36cbee89d"),"skillPanel.fortify",0.25D,0));
	/** タフネス（落下耐性）*/
	public static final SkillPanel TOUGHNESS = new SkillPanel("toughness").setIcon(IconType.PROTECT).setRarity(SkillPanelRegistry.RARITY_UNCOMMON).setDamageAgainst(DamageSource.FALL);
	/** 鋼の意思（ウィザー・腹減り耐性）*/
	public static final SkillPanel IRON_WILL = new SkillPanel("iron_mind").setIcon(IconType.PROTECT).setRarity(SkillPanelRegistry.RARITY_RARE).setDamageAgainst(DamageSource.STARVE,DamageSource.WITHER);
	/** 聞き耳*/
	public static final SkillPanel EAVESDROP = new SkillPanel("eavesdrop").setIcon(IconType.KEY).setRarity(SkillPanelRegistry.RARITY_RARE);
	/** 盾（盾のアビリティ発動）*/
	public static final SkillPanel SHIELD = new SkillPanel("shield").setIcon(IconType.PROTECT).setRarity(SkillPanelRegistry.RARITY_UNCOMMON);

	//案内
	public static final SkillPanel GUIDE_ROAD = new SkillPanel("road_adviser").setIcon(IconType.ROLL);
	public static final SkillPanel GUIDE_CAVE = new SkillPanel("cavern_exprorer").setIcon(IconType.ROLL);

	//アイテム
	public static final SkillPanel TOOL_CUSTOMIZE = new SkillPanel("tool_customize").setIcon(IconType.MELEE).setRarity(SkillPanelRegistry.RARITY_RARE);
	/** 節約魂*/
	public static final SkillPanel THRIFT_SAVER = new SkillPanel("saving_damage").setIcon(IconType.ROLL).setRarity(SkillPanelRegistry.RARITY_RARE);
	/** 簡易修理*/
	public static final SkillPanel QUICK_FIX = new SkillPanel("easy_repair").setIcon(IconType.ROLL).setRarity(SkillPanelRegistry.RARITY_RARE);
	//ネガティヴ
	public static final SkillPanel PHOBIA_ZOMBIE = new SkillPanelNegative("zombie_phobia",in -> in instanceof EntityZombie,SkillPanelNegative.Type.WEAKNESS).setIcon(IconType.NEGATIVE).setNegativeSkill(true).setRarity(SkillPanelRegistry.RARITY_RARE);
	public static final SkillPanel PHOBIA_CREEPER = new SkillPanelNegative("creeper_phobia",in -> in instanceof EntityCreeper,SkillPanelNegative.Type.WEAKNESS).setIcon(IconType.NEGATIVE).setNegativeSkill(true).setRarity(SkillPanelRegistry.RARITY_RARE);
	public static final SkillPanel PACIFIST_ANIMALS = new SkillPanelNegative("no_killing_animals",in -> in instanceof EntityAnimal,SkillPanelNegative.Type.DAMAGE).setIcon(IconType.NEGATIVE).setNegativeSkill(true).setRarity(SkillPanelRegistry.RARITY_RARE);
	//ファミリア
	public static final SkillPanel FAMILIAR_EARTH = new SkillPanel("familiar_earth").setIcon(IconType.FAMILIAR).setRarity(SkillPanelRegistry.RARITY_COMMON).setElement(FiveElements.Type.EARTH);
	public static final SkillPanel FAMILIAR_FIRE = new SkillPanel("familiar_fire").setIcon(IconType.FAMILIAR).setRarity(SkillPanelRegistry.RARITY_COMMON).setElement(FiveElements.Type.FIRE);
	public static final SkillPanel FAMILIAR_METAL = new SkillPanel("familiar_metal").setIcon(IconType.FAMILIAR).setRarity(SkillPanelRegistry.RARITY_COMMON).setElement(FiveElements.Type.METAL);
	public static final SkillPanel FAMILIAR_WATER = new SkillPanel("familiar_water").setIcon(IconType.FAMILIAR).setRarity(SkillPanelRegistry.RARITY_COMMON).setElement(FiveElements.Type.WATER);
	public static final SkillPanel FAMILIAR_WOOD = new SkillPanel("familiar_wood").setIcon(IconType.FAMILIAR).setRarity(SkillPanelRegistry.RARITY_COMMON).setElement(FiveElements.Type.WOOD);
	//動作系
	public static final SkillPanel SWIMMING = new SkillPanel("swimming").setIcon(IconType.ROLL).setRarity(SkillPanelRegistry.RARITY_UNCOMMON);
	public static final SkillPanel ADAPTABILITY = new SkillPanel("adaptability").setIcon(IconType.ROLL).setRarity(SkillPanelRegistry.RARITY_RARE);
	/** 身のこなし*/
	public static final SkillPanel SMART_MOVE = new SkillPanel("smart_move").setIcon(IconType.KEY).setRarity(SkillPanelRegistry.RARITY_RARE);
	public static final SkillPanel OBSTACLE_CROSSING = new SkillPanel("obstacle_crossing").setIcon(IconType.ROLL);
	//その他
	/** 地味（身のこなしの効力アップ）*/
	public static final SkillPanel INCONSPICIOUS = new SkillPanel("inconspicious").setIcon(IconType.COMMUNICATION).setRarity(SkillPanelRegistry.RARITY_ULTRARARE);

	//実装するか微妙

	public static final SkillPanel ACCESSORY_FORGE = new SkillPanel("accessory_forge").setIcon(IconType.MELEE).setRarity(SkillPanelRegistry.RARITY_RARE);
	public static final SkillPanel WATCHING_OUT = new SkillPanel("watching_out").setRarity(SkillPanelRegistry.RARITY_RARE);
	public static final SkillPanel KNOWLEDGE_BUILDINGS = new SkillPanel("knowledge_buildings").setIcon(IconType.ROLL);

	public static void register(){
		put(PUNCH,ARCANE_TONGUE,MAGIC_BLEND,LOCKSMITH,DEFUSE,SHARP_EYE,ARTISTE);
		put(MONGER,MAHARAJA,FORTUNE,PHOBIA_CREEPER,PHOBIA_ZOMBIE,PACIFIST_ANIMALS);
		put(IRON_BODY,TOUGHNESS,GUIDE_ROAD,GUIDE_CAVE);
		put(TOOL_CUSTOMIZE,IRON_WILL,FASHIONABLE,GUN);
		put(SWIMMING,ADAPTABILITY,SHIELD,QUICK_FIX);
		put(FAMILIAR_EARTH,FAMILIAR_FIRE,FAMILIAR_WATER,FAMILIAR_METAL,FAMILIAR_WOOD);
		put(THRIFT_SAVER,EAVESDROP,WEAPON_MASTER,MAGIC_EXPERT,SMART_MOVE,OBSTACLE_CROSSING);
	}

	public static void put(SkillPanel... panels){
		SkillPanelRegistry.instance().register(panels);
	}
}
