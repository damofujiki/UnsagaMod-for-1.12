package mods.hinasch.unsaga.ability.specialmove;

import java.util.Map;

import com.google.common.collect.Maps;

import mods.hinasch.lib.core.HSLib;
import mods.hinasch.lib.network.PacketSound;
import mods.hinasch.lib.network.PacketUtil;
import mods.hinasch.lib.registry.PropertyRegistry;
import mods.hinasch.unsaga.ability.specialmove.TechInvoker.InvokeType;
import mods.hinasch.unsaga.ability.specialmove.action.TechActionBase;
import mods.hinasch.unsaga.ability.specialmove.action.TechActionFactory;
import mods.hinasch.unsaga.ability.specialmove.action.TechActionFactoryHelper;
import mods.hinasch.unsaga.ability.specialmove.action.TechArrow;
import mods.hinasch.unsaga.common.specialaction.ActionBase.IAction;
import mods.hinasch.unsaga.common.specialaction.ActionCharged;
import mods.hinasch.unsaga.common.specialaction.ISpecialAction;
import mods.hinasch.unsaga.core.entity.projectile.EntityJavelin;
import mods.hinasch.unsaga.core.entity.projectile.custom_arrow.SpecialArrowType;
import mods.hinasch.unsaga.damage.DamageTypeUnsaga.General;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumParticleTypes;

public class TechRegistry extends PropertyRegistry<Tech>{

	private static TechRegistry INSTANCE;

	Map<Tech,ISpecialAction<TechInvoker>> moveActionAssociation = Maps.newHashMap();


	public static final Tech CALEIDO_SCOPE = new Tech("caleidoscope");
	public static final Tech SMASH = new Tech("smash").setCost(2).setStrength(4.0F, 1.3F);
	public static final Tech VANDALIZE = new Tech("vandalize").setCost(10).setStrength(6.0F, 1.5F).setCoolingTime(10);
	public static final Tech CHARGE_BLADE = new Tech("charge_blade").setStrength(1.5F, 1.0F).setCoolingTime(3);
	public static final Tech GUST = new Tech("gust").setCoolingTime(5).setStrength(3.0F, 1.3F);
	public static final Tech HAWK_BLADE = new Tech("hawk_blade").setCoolingTime(4).setStrength(1.0F, 1.0F).setCost(4);
	public static final Tech TWIN_BLADE = new Tech("twin_blade").setCoolingTime(2).setStrength(3.0F, 1.5F).setCost(4);

	public static final Tech TOMAHAWK = new Tech("tomahawk").setCost(3).setStrength(2.0F, 1.5F);
	public static final Tech FUJI_VIEW = new Tech("fuji_view").setCost(10).setStrength(6.5F, 1.0F);
	public static final Tech SKY_DRIVE = new Tech("sky_drive").setCost(10).setStrength(2.5F, 2.0F).setRequireTarget(true).setCoolingTime(10);
	public static final Tech WOOD_CHOPPER = new Tech("wood_chopper").setCost(4).setStrength(0.0F, 1.5F);
	public static final Tech CROSSING = new Tech("crossing");
	public static final Tech YOYO = new Tech("yoyo").setCost(3).setStrength(0.5F, 1.5F);
	public static final Tech FIREWOOD_CHOPPER = new Tech("firewood_chopper").setStrength(1.0F, 1.5F).setCost(5);

	public static final Tech AIMING = new Tech("aiming").setCost(2).setStrength(2.0F, 1.8F);
	public static final Tech ACUPUNCTURE = new Tech("acupuncture").setCost(15).setStrength(5.0F, 2.5F).setCoolingTime(10);
	public static final Tech SWING = new Tech("swing").setCost(8).setCoolingTime(3).setStrength(-1.0F, 1.2F);
	public static final Tech GRASSHOPPER = new Tech("grasshopper").setCost(4).setStrength(-3.0F, 0.1F);
	public static final Tech ARM_OF_LIGHT = new Tech("arm_of_light").setCost(8).setStrength(1.5F, 1.8F);
	public static final Tech TRIPLE_SUPREMACY = new Tech("triple_supremacy").setCoolingTime(3).setStrength(4F, 1.8F).setCost(6);
	public static final Tech JAVELIN = new Tech("javelin").setCoolingTime(3).setStrength(1.5F, 1.8F).setCost(3);
	public static final Tech WIND_MILL = new Tech("wind_mill").setCoolingTime(3).setStrength(1.5F, 1.8F).setCost(3);


	public static final Tech EARTH_DRAGON = new Tech("earth_dragon").setStrength(-2.0F, 0.1F).setCost(8).setCoolingTime(3);
	public static final Tech SKULL_CRASH = new Tech("skull_crash").setStrength(0.5F, 1.0F).setCost(4);
	public static final Tech PULVERIZER = new Tech("pulverizer").setStrength(0.5F, 1.0F).setCost(4);
	public static final Tech GRANDSLAM = new Tech("grand_slam").setStrength(0.0F, 0.2F).setCost(15).setCoolingTime(10);
	public static final Tech GONGER = new Tech("gonger").setStrength(0.5F, 1.0F).setCost(3);
	public static final Tech ROCK_CRASHER = new Tech("rock_crash").setStrength(1.5F, 1.5F).setCost(4);

	public static final Tech DOUBLE_SHOT = new Tech("double_shot");
	public static final Tech TRIPLE_SHOT = new Tech("triple_shot");
	public static final Tech ZAPPER = new Tech("zapper").setStrength(4.0F, 2.5F).setCost(10);
	public static final Tech EXORCIST = new Tech("exorcist").setStrength(0.5F, 1.5F).setCost(7);
	public static final Tech SHADOW_STITCHING = new Tech("shadow_stitching").setStrength(0.0F, 1.0F).setCost(2);
	public static final Tech PHOENIX = new Tech("phoenix").setStrength(3.0F, 2.0F).setCost(15);
	public static final Tech ARROW_RAIN = new Tech("arrow_rain");
	public static final Tech QUICK_CHECKER = new Tech("quick_checker").setCost(4);

	public static final Tech KNIFE_THROW = new Tech("knife_throw").setCost(3).setStrength(1.5F, 2.0F);
	public static final Tech STUNNER = new Tech("stunner").setCost(3).setStrength(0.0F, 2.0F);
	public static final Tech BLOODMARY = new Tech("bloody_mary").setCost(8).setStrength(1.0F, 1.7F);
	public static final Tech CUTIN = new Tech("cutIn");
	public static final Tech BLITZ = new Tech("blitz").setCoolingTime(4).setStrength(3.0F, 2.0F);
	public static final Tech LIGHTNING_THRUST = new Tech("lightning_thrust").setCost(10).setStrength(1.5F, 2.0F).setCoolingTime(5);

	public static final Tech AIR_THROW = new Tech("air_throw").setCost(3).setCoolingTime(2);
	public static final Tech CYCLONE = new Tech("cyclone").setCost(8).setCoolingTime(8);
	public static final Tech CALLBACK = new Tech("callback").setCost(3).setCoolingTime(2);
	public static final Tech SINKER = new Tech("sinker").setCoolingTime(3).setCost(3);
	public static final Tech WATERFALL = new Tech("waterfall").setCost(5);
	public static final Tech RAKSHA = new Tech("raksha").setCoolingTime(10).setCost(8);
	public static final Tech TRIANGLE_KICK = new Tech("triangle_kick");
	public static final Tech THUNDER_KICK = new Tech("thunder_kick").setCost(5).setCoolingTime(5);
	public static final Tech FLYING_KNEE = new Tech("flying_knee").setCost(5).setCoolingTime(4);
	public static final Tech THREE_DRAGON = new Tech("three_dragon");

	private MartialArtsSetting martialArtsSetting;

	private TechRegistry(){

	}

	public static TechRegistry instance(){
		if(INSTANCE==null){
			INSTANCE = new TechRegistry();
		}
		return INSTANCE;
	}
	@Override
	public void init() {
		// TODO 自動生成されたメソッド・スタブ

		this.associate();
		MartialArtsSetting.instance().init();
	}

	@Override
	public void preInit() {
		// TODO 自動生成されたメソッド・スタブ
		this.registerObjects();
	}

	public TechActionBase getAssociatedAction(Tech move){
		return (TechActionBase) this.moveActionAssociation.get(move);
	}
	protected void associate(){

		//弓系
		IAction<TechInvoker> arrowSound = in ->{
			HSLib.getPacketDispatcher().sendToAllAround(PacketSound.atEntity(SoundEvents.ENTITY_ZOMBIE_VILLAGER_CURE, in.getPerformer()), PacketUtil.getTargetPointNear(in.getPerformer()));
			return EnumActionResult.PASS;
		};

		moveActionAssociation.put(QUICK_CHECKER, TechActionFactory.quickChecker());
		moveActionAssociation.put(EXORCIST, new TechArrow().setArrowType(SpecialArrowType.EXORCIST));
		moveActionAssociation.put(PHOENIX, new TechArrow().setArrowType(SpecialArrowType.PHOENIX).addAction(arrowSound,0));
		moveActionAssociation.put(ZAPPER, new TechArrow().setArrowType(SpecialArrowType.ZAPPER).addAction(arrowSound,0));
//		moveActionAssociation.put(doubleShot, new SpecialMoveRapidArrow(1));
//		moveActionAssociation.put(tripleShot, new SpecialMoveRapidArrow(2));
		moveActionAssociation.put(SHADOW_STITCHING, new TechArrow().setArrowType(SpecialArrowType.SHADOW_STITCH));



		//槍
		moveActionAssociation.put(SWING, TechActionFactory.swing());
		moveActionAssociation.put(GRASSHOPPER, TechActionFactory.grassHopper());
		moveActionAssociation.put(AIMING, TechActionBase.create(InvokeType.CHARGE)
				.addAction(ActionCharged.simpleChargedMelee(0,General.SPEAR,General.PUNCH).setChargeThreshold(15)));
//		moveActionAssociation.put(aiming, new SpecialMoveChargedAttack(InvokeType.CHARGE,General.SPEAR,General.PUNCH,General.SWORD).setChargeThreshold(15));
		moveActionAssociation.put(ACUPUNCTURE,TechActionFactory.acupuncture());
		moveActionAssociation.put(ARM_OF_LIGHT, TechActionFactory.armOfLight());
		moveActionAssociation.put(TRIPLE_SUPREMACY, TechActionFactoryHelper.createMultiAttack(3,EnumParticleTypes.CRIT,false,false,General.SPEAR));
		moveActionAssociation.put(JAVELIN, TechActionFactory.javelin());
		moveActionAssociation.put(WIND_MILL, TechActionFactoryHelper.createProjectile(EntityJavelin::swinging, 10));
		//杖
		moveActionAssociation.put(GRANDSLAM, TechActionFactory.grandslam());
		moveActionAssociation.put(EARTH_DRAGON, TechActionFactory.earthDragon());
		moveActionAssociation.put(PULVERIZER, TechActionFactory.connectedBlocksCrasher());
		moveActionAssociation.put(ROCK_CRASHER, TechActionFactory.rockCrasher());
		moveActionAssociation.put(GONGER, TechActionFactory.gonger());
		moveActionAssociation.put(SKULL_CRASH, TechActionFactory.skullCrasher());
		//剣
		moveActionAssociation.put(GUST, TechActionFactory.gust());
		moveActionAssociation.put(HAWK_BLADE, TechActionFactory.hawkBlade());//短剣でも
		moveActionAssociation.put(CHARGE_BLADE, TechActionFactory.chargeBlade());
		moveActionAssociation.put(SMASH, TechActionBase.create(InvokeType.CHARGE)
				.addAction(ActionCharged.simpleChargedMelee(5.0F,General.SWORD).setChargeThreshold(15)));
		moveActionAssociation.put(VANDALIZE, TechActionFactory.vandalize());
		moveActionAssociation.put(TWIN_BLADE, TechActionFactoryHelper.createMultiAttack(2,EnumParticleTypes.CRIT,false,true,General.SWORD));
		//斧

		moveActionAssociation.put(WOOD_CHOPPER, TechActionFactory.woodChopper());
		moveActionAssociation.put(FUJI_VIEW, TechActionFactory.fujiView());
		moveActionAssociation.put(TOMAHAWK, TechActionFactory.tomahawk());
		moveActionAssociation.put(SKY_DRIVE, TechActionFactory.skyDrive());
		moveActionAssociation.put(YOYO, TechActionFactory.tomahawk());
		moveActionAssociation.put(FIREWOOD_CHOPPER, TechActionFactory.firewoodChopper());
		//短剣

		moveActionAssociation.put(BLITZ, TechActionFactory.blitz());
		moveActionAssociation.put(BLOODMARY, TechActionFactoryHelper.createMultiAttack(5,EnumParticleTypes.REDSTONE,false,false,General.SPEAR));
		moveActionAssociation.put(STUNNER, TechActionFactory.stunner());
		moveActionAssociation.put(LIGHTNING_THRUST, TechActionFactory.lightningThrust());
		moveActionAssociation.put(KNIFE_THROW, TechActionFactory.knifeThrow());

		moveActionAssociation.put(AIR_THROW, TechActionFactory.airThrow());
		moveActionAssociation.put(CALLBACK, TechActionFactory.callBack());
		moveActionAssociation.put(CYCLONE, TechActionFactory.cyclone());
		moveActionAssociation.put(SINKER, TechActionFactory.kick());
		moveActionAssociation.put(THUNDER_KICK, TechActionFactory.kick());
		moveActionAssociation.put(FLYING_KNEE, TechActionFactory.flyingKnee());
		moveActionAssociation.put(RAKSHA, TechActionFactory.raksha());
		moveActionAssociation.put(TRIANGLE_KICK, TechActionFactory.triangleKick());
		moveActionAssociation.put(WATERFALL, TechActionFactory.waterfall());
	}
	@Override
	protected void registerObjects() {

//		this.put(caleidoscope);
		this.put(SMASH);
		this.put(VANDALIZE);
		this.put(CHARGE_BLADE);
		this.put(GUST);
		this.put(TWIN_BLADE);

//		this.put(tomahawk);
		this.put(FUJI_VIEW);
		this.put(SKY_DRIVE);
		this.put(WOOD_CHOPPER);
//		this.put(crossing);
		this.put(YOYO);
		this.put(FIREWOOD_CHOPPER);

		this.put(AIMING);
		this.put(ACUPUNCTURE);
		this.put(SWING);
		this.put(GRASSHOPPER);
		this.put(ARM_OF_LIGHT);
		this.put(TRIPLE_SUPREMACY);
		this.put(JAVELIN);
		this.put(WIND_MILL);

		this.put(EARTH_DRAGON);
		this.put(SKULL_CRASH);
		this.put(PULVERIZER);
		this.put(GRANDSLAM);
		this.put(GONGER);
		this.put(ROCK_CRASHER);

//		this.put(doubleShot);
//		this.put(tripleShot);
		this.put(ZAPPER);
		this.put(EXORCIST);
		this.put(SHADOW_STITCHING);
		this.put(PHOENIX);
//		this.put(arrowRain);
		this.put(QUICK_CHECKER);

		this.put(KNIFE_THROW);
		this.put(STUNNER);
		this.put(BLOODMARY);
		this.put(CUTIN);
		this.put(BLITZ);
		this.put(LIGHTNING_THRUST);

		this.put(HAWK_BLADE);

		this.put(AIR_THROW);
		this.put(CYCLONE);
		this.put(CALLBACK);
		this.put(SINKER);
		this.put(WATERFALL);
		this.put(RAKSHA);
		this.put(THUNDER_KICK);
		this.put(TRIANGLE_KICK);
		this.put(FLYING_KNEE);
		this.put(THREE_DRAGON);
	}

}
