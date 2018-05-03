package mods.hinasch.unsaga.init;

import java.util.function.Function;

import com.google.common.collect.Lists;

import mods.hinasch.lib.config.ConfigBase;
import mods.hinasch.lib.config.PropertyCustomNew;
import mods.hinasch.lib.util.HSLibs;
import mods.hinasch.lib.world.XYZPos;
import mods.hinasch.unsaga.lp.LPDifficulty;

public class UnsagaConfigHandler extends ConfigBase{

	public static class OreSetting{
		public boolean enableCopper = true;
		public boolean enableLead = true;
		public boolean enableSilver = true;
		public boolean enableSapphire = true;
		public boolean enableRuby = true;
		public boolean enableSerpentine = true;
	}
//	boolean enableAlwaysSparkling = false;
	PropertyCustomNew prop;
//	boolean enableOreGeneration = true;

	boolean isDisplayToolDurability = false;
	boolean enableLP = true;
	boolean enableRenderNearMonsterLP = false;
	boolean enableChestGeneration =  true;
	boolean enableHighProbBirthUnsagaVillagers = false;
	boolean enableSpawnMerhchantHouse = true;
	boolean isForceVillagerToChangeProfessionUnsaga = false;
	OreSetting oreSetting = new OreSetting();

	int chestGenerationWeight = 3;
	int defaultPlayerLP;
	int skillXPMultiply = 1;
	int decipheringXPMultiply = 1;
	int spawnDensityUnsagaMonster = 10;
//	Map<String,Integer> materialRenderColorOverrides = Maps.newHashMap();


	double defaultTargettingRange = 20.0D;
	double defaultTargettingRangeVertical = 10.0D;


	XYZPos renderLPOffset = new XYZPos(0,0,0);
	XYZPos renderDebuffOffset = new XYZPos(0,0,0);

	Function<int[],XYZPos> parseXYZ = new Function<int[],XYZPos>(){

		@Override
		public XYZPos apply(int[] in) {
			if(in.length>=3){
				return new XYZPos(in[0],in[1],in[2]);

			}
			return new XYZPos(0,0,0);
		}
	};

//	public void enableAlwaysSparkling(boolean par1){
//		this.enableAlwaysSparkling = par1;
//	}


	public int getDensityMonsterSpawn(){
		return this.spawnDensityUnsagaMonster;
	}
	public int getDecipheringXPMultiply() {
		return decipheringXPMultiply;
	}



//	public int getChestGenerationWeight() {
//		return chestGenerationWeight;
//	}


	public boolean isEnabledDisplayToolDurability(){
		return this.isDisplayToolDurability;
	}
	public int getDefaultPlayerLifePoint(){
		return this.defaultPlayerLP;
	}

	public double getDefaultTargettingRange() {
		return defaultTargettingRange;
	}
	public double getDefaultTargettingRangeVertical() {
		return defaultTargettingRangeVertical;
	}

//	@SideOnly(Side.CLIENT)
//	public Map<String, Integer> getMaterialRenderColorOverrides() {
//		return materialRenderColorOverrides;
//	}
	public OreSetting getOreSetting() {
		return oreSetting;
	}

	public boolean isForceToSpawnMerchantHouse(){
		return this.isForceVillagerToChangeProfessionUnsaga;
	}
	public boolean isEnabledHighProbBirthUnsagaVillagers(){
		return this.enableHighProbBirthUnsagaVillagers;
	}
	public XYZPos getRenderLPOffset() {
		return renderLPOffset;
	}

	public LPDifficulty.Type getDifficulty(){
		return LPDifficulty.Type.NORMAL;
	}
//	public XYZPos getRenderDebuffOffset() {
//		return renderDebuffOffset;
//	}

	public int getSkillXPMultiply() {
		return skillXPMultiply;
	}

	@Override
	public void init() {
		// TODO 自動生成されたメソッド・スタブ

		prop = PropertyCustomNew.create();
		prop.add(0,"enable.LPSystem", HSLibs.translateKey("unsaga.config.enable.LPSystem.info"), true);
		prop.add(1,"default.LP.player", HSLibs.translateKey("unsaga.config.default.LP.player.info"), 8);
		prop.add(2,"offset.playerLP",HSLibs.translateKey("unsaga.config.offset.playerLP.info"), Lists.newArrayList(0,0),Integer.class);
//		prop.add(3,"offset.playerDebuffs", "offset of rendering debuff icons.(x,y)", Lists.newArrayList(0,0),Integer.class);
		prop.add(3,"default.targettingRange", HSLibs.translateKey("unsaga.config.default.targettingRange.info"),Lists.newArrayList(20.0D,10.0D),Double.class);
		prop.add(4,"enable.render.nearMonsterLP", HSLibs.translateKey("unsaga.config.enable.render.nearMonsterLP.info"), false);
//		prop.add(6, "overrides.materialRenderColor", "custom material render colors.(name:color)", Lists.newArrayList("damascus:0x726250"),String.class);
//		prop.add(7,"enable.generation.ores", "set true to allow generation of ores.", true);
//		prop.add(8, "enable.generation.chests", "set true to allow generation of chets.", true);
//		prop.add(9, "weight.generation.chest", "chest generation weight.(1-10)",3 );
//		prop.add(10, "enable.generation.copper", "set true to allow generation of copper ores.", true);
//		prop.add(11, "enable.generation.lead", "set true to allow generation of lead ores.", true);
//		prop.add(12, "enable.generation.silver", "set true to allow generation of silver ores.", true);
//		prop.add(13, "enable.generation.sapphire", "set true to allow generation of sapphire ores.", true);
//		prop.add(14, "enable.generation.ruby", "set true to allow generation of ruby ores.", true);
//		prop.add(15, "enable.generation.serpentine", "set true to allow generation of serpentine stones.", true);
		prop.add(5, "xp.skill.multiply", HSLibs.translateKey("unsaga.config.xp.skill.multiply.info"), 1);
		prop.add(6, "xp.deciphering.multiply", HSLibs.translateKey("unsaga.config.xp.deciphering.multiply.info"), 1);
		prop.add(7, "enable.villager.unsagaVillagersBirthHighProb", HSLibs.translateKey("unsaga.config.enable.villager.unsagaVillagersBirthHighProb.info"), false);
		prop.add(8, "force.spawn.merchantHouse", HSLibs.translateKey("unsaga.config.forge.spawn.merchantHouse.info"), false);
		prop.add(9, "density.spawn.additionalMonster", HSLibs.translateKey("unsaga.config.density.spawn.additionalMonster.info"), 10);
		prop.add(10, "enable.spawn.merchantHouse", HSLibs.translateKey("unsaga.config.enable.spawn.merchantHouse.info"), false);
		prop.add(11, "enable.display.tool.durability", HSLibs.translateKey("unsaga.config.enable.display.tool.durability.info"), false);

		prop.adapt(this.configFile);


	}

//	public boolean isAlwaysSparkling(){
//		return this.enableAlwaysSparkling;
//	}

//	public boolean isEnableChestGeneration() {
//		return enableChestGeneration;
//	}

	public boolean isEnabledLifePointSystem(){
		return this.enableLP;
	}

//	public boolean isEnableOreGeneration() {
//		return enableOreGeneration;
//	}
//
	public boolean isEnabledRenderNearMonsterLP() {
		return enableRenderNearMonsterLP;
	}


	public boolean isEnabledSpawnMerchantHouse() {
		return this.enableSpawnMerhchantHouse;
	}

	@Override
	public void syncConfig() {



		enableLP = prop.getAdaptedProperties().get(0).getBoolean();
		defaultPlayerLP = prop.getAdaptedProperties().get(1).getInt();
		this.renderLPOffset = parseXYZ.apply(prop.getAdaptedProperties().get(2).getIntList());


		defaultTargettingRange = prop.getAdaptedProperties().get(3).getDoubleList()[0];
		defaultTargettingRangeVertical = prop.getAdaptedProperties().get(3).getDoubleList()[1];
		this.enableRenderNearMonsterLP = prop.getAdaptedProperties().get(4).getBoolean();
		this.skillXPMultiply = prop.getAdaptedProperties().get(5).getInt();
		this.decipheringXPMultiply = prop.getAdaptedProperties().get(6).getInt();
		this.enableHighProbBirthUnsagaVillagers = prop.getAdaptedProperties().get(7).getBoolean();
		this.isForceVillagerToChangeProfessionUnsaga = prop.getAdaptedProperties().get(8).getBoolean();
		this.spawnDensityUnsagaMonster = prop.getAdaptedProperties().get(9).getInt();
		this.enableSpawnMerhchantHouse = prop.getAdaptedProperties().get(10).getBoolean();
		this.isDisplayToolDurability = prop.getAdaptedProperties().get(11).getBoolean();
//		this.materialRenderColorOverrides = splitters.parse().get();

//
//		this.oreSetting.enableCopper = prop.getAdaptedProperties().get(10).getBoolean();
//		this.oreSetting.enableLead = prop.getAdaptedProperties().get(11).getBoolean();
//		this.oreSetting.enableSilver = prop.getAdaptedProperties().get(12).getBoolean();
//		this.oreSetting.enableSapphire = prop.getAdaptedProperties().get(13).getBoolean();
//		this.oreSetting.enableRuby = prop.getAdaptedProperties().get(14).getBoolean();
//		this.oreSetting.enableSerpentine = prop.getAdaptedProperties().get(15).getBoolean();


//		if(UnsagaMod.materials.isMaterialLoaded()){
//			this.checkValidation();
//		}
		super.syncConfig();

	}
}
