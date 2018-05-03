package mods.hinasch.unsaga.plugin.hac;

import java.util.Map;
import java.util.function.Predicate;

import defeatedcrow.hac.api.climate.ClimateAPI;
import defeatedcrow.hac.api.climate.DCHeatTier;
import defeatedcrow.hac.api.climate.DCHumidity;
import defeatedcrow.hac.api.climate.IClimate;
import defeatedcrow.hac.api.damage.DamageSourceClimate;
import mods.hinasch.lib.util.HSLibs;
import mods.hinasch.unsaga.ability.AbilityRegistry;
import mods.hinasch.unsaga.damage.DamageTypeAssociation;
import mods.hinasch.unsaga.damage.DamageTypeUnsaga.General;
import mods.hinasch.unsaga.damage.DamageTypeUnsaga.Sub;
import mods.hinasch.unsaga.element.FiveElements;
import mods.hinasch.unsaga.element.newele.ElementTable;
import net.minecraft.block.Block;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class UnsagaPluginHAC {

	static AbilityRegistry abilities = AbilityRegistry.instance();
	public void registerEvents(){
		HSLibs.registerEvent(new UnsagaClimateDamageEvent());
//		HSLib.core().events.livingHurt.getEventsPost().add(new LivingHurtEventBase(){
//
//			@Override
//			public boolean apply(LivingHurtEvent e, DamageSource dsu) {
//				// TODO 自動生成されたメソッド・スタブ
//				return true;
//			}
//
//			@Override
//			public String getName() {
//				// TODO 自動生成されたメソッド・スタブ
//				return "hac_climate_damage";
//			}
//
//			@Override
//			public DamageSource process(LivingHurtEvent e, DamageSource dsu) {
//				float reduce = 0.0F;
//				if(dsu==DamageSourceClimate.climateColdDamage){
//					UnsagaMod.logger.trace("called", "kiteru");
//					reduce += (0.5F*AbilityAPI.getAbilityAmount(e.getEntityLiving(),abilities.armorCold));
//					reduce += (1F*AbilityAPI.getAbilityAmount(e.getEntityLiving(),abilities.armorColdEx));
//					if(e.getEntityLiving() instanceof EntityPlayer && SkillPanelAPI.hasPanel((EntityPlayer) e.getEntityLiving(), SkillPanelRegistry.instance().adaptability)){
//						reduce += (0.5F*SkillPanelAPI.getHighestPanelLevel((EntityPlayer) e.getEntityLiving(),SkillPanelRegistry.instance().adaptability).getAsInt());
//					}
//					if(e.getEntityLiving().getActivePotionEffect(UnsagaPotions.instance().selfBurning)!=null){
//						reduce += 0.5F * (1 + e.getEntityLiving().getActivePotionEffect(UnsagaPotions.instance().selfBurning).getAmplifier());
//					}
//
//					if(e.getEntityLiving().isPotionActive(UnsagaPotions.instance().selfBurning)){
//						reduce += 100F;
//					}
//				}
//				if(dsu==DamageSourceClimate.climateHeatDamage){
//					reduce += (0.5F*AbilityAPI.getAbilityAmount(e.getEntityLiving(),abilities.armorFire));
//					reduce += (1F*AbilityAPI.getAbilityAmount(e.getEntityLiving(),abilities.armorFireEx));
//					if(e.getEntityLiving() instanceof EntityPlayer && SkillPanelAPI.hasPanel((EntityPlayer) e.getEntityLiving(), SkillPanelRegistry.instance().adaptability)){
//						reduce += (0.5F*SkillPanelAPI.getHighestPanelLevel((EntityPlayer) e.getEntityLiving(),SkillPanelRegistry.instance().adaptability).getAsInt());
//					}
//					if(e.getEntityLiving().getActivePotionEffect(UnsagaPotions.instance().selfBurning)!=null){
//						reduce += 0.5F * (1 + e.getEntityLiving().getActivePotionEffect(UnsagaPotions.instance().selfBurning).getAmplifier());
//					}
//					if(e.getEntityLiving().isPotionActive(UnsagaPotions.instance().waterShield)){
//						reduce += 100F;
//					}
//				}
//				float amount = MathHelper.clamp_float(e.getAmount()-reduce, 0, 65535F);
//				e.setAmount(amount);
//				return dsu;
//			}}
//		);

	}

	public void registerDamageSources(Map<Predicate<DamageSource>, DamageTypeAssociation.Attribute>  registerer){
		registerer.put(in -> in==DamageSourceClimate.climateHeatDamage, new DamageTypeAssociation.Attribute(0,General.MAGIC,Sub.FIRE));
		registerer.put(in -> in==DamageSourceClimate.climateColdDamage, new DamageTypeAssociation.Attribute(0,General.MAGIC,Sub.FREEZE));
	}
	public void registerBlocks(){
//		ClimateAPI.registerBlock.registerHeatBlock(UnsagaMagic.instance().blocks.fireWall, 32767, DCHeatTier.KILN);
	}
	public static ElementTable getElementsTable(World world,BlockPos pos){
		IClimate heat = ClimateAPI.calculator.getClimate(world, pos);
		if(getElementsFromHeatTier(heat.getHeat())!=null){
			return getElementsFromHeatTier(heat.getHeat());
		}

		DCHumidity humid = heat.getHumidity();
		if(getElementsFromHumid(humid)!=null){
			return getElementsFromHumid(humid);
		}
		return null;
	}

	public static ElementTable getElementsFromHumid(DCHumidity humid){
		if(humid==DCHumidity.WET){
			return new ElementTable(FiveElements.Type.WATER,0.3F);
		}
		if(humid==DCHumidity.UNDERWATER){
			return new ElementTable(FiveElements.Type.WATER,0.8F);
		}
		return null;
	}
	public static ElementTable getElementsFromHeat(DCHeatTier tier){
		if(tier.getTemp()>=DCHeatTier.HOT.getTemp()){
			return new ElementTable(FiveElements.Type.FIRE,0.3F);
		}
		if(tier.getTemp()>=DCHeatTier.OVEN.getTemp()){
			return new ElementTable(FiveElements.Type.FIRE,0.6F);
		}
		if(tier.getTemp()>=DCHeatTier.KILN.getTemp()){
			return new ElementTable(FiveElements.Type.FIRE,1.0F);
		}
		if(tier.getTemp()>=DCHeatTier.SMELTING.getTemp()){
			return new ElementTable(FiveElements.Type.FIRE,1.2F);
		}
		if(tier.getTemp()>=DCHeatTier.UHT.getTemp()){
			return new ElementTable(FiveElements.Type.FIRE,1.5F);

		}
		if(tier.getTemp()>=DCHeatTier.INFERNO.getTemp()){
			return new ElementTable(FiveElements.Type.FIRE,2.0F);
		}

		return null;
	}
	public static ElementTable getElementsFromHumidity(DCHumidity humid){
		if(humid==DCHumidity.WET){
			return new ElementTable(FiveElements.Type.WATER,0.3F);
		}
		if(humid==DCHumidity.UNDERWATER){
			return new ElementTable(FiveElements.Type.WATER,0.8F);
		}
		return null;
	}

	public static ElementTable getElementsFromHeatTier(DCHeatTier tier){
		if(tier.getTemp()>=DCHeatTier.HOT.getTemp()){
			return new ElementTable(FiveElements.Type.FIRE,0.3F);
		}
		if(tier.getTemp()>=DCHeatTier.OVEN.getTemp()){
			return new ElementTable(FiveElements.Type.FIRE,0.6F);
		}
		if(tier.getTemp()>=DCHeatTier.KILN.getTemp()){
			return new ElementTable(FiveElements.Type.FIRE,1.0F);
		}
		if(tier.getTemp()>=DCHeatTier.SMELTING.getTemp()){
			return new ElementTable(FiveElements.Type.FIRE,1.2F);
		}
		if(tier.getTemp()>=DCHeatTier.UHT.getTemp()){
			return new ElementTable(FiveElements.Type.FIRE,1.5F);

		}
		if(tier.getTemp()>=DCHeatTier.INFERNO.getTemp()){
			return new ElementTable(FiveElements.Type.FIRE,2.0F);
		}

		return null;
	}
	public static ElementTable getElements(Block block,int meta){
		DCHeatTier heat = ClimateAPI.registerBlock.getHeatTier(block, meta);
		if(getElementsFromHeatTier(heat)!=null){
			return getElementsFromHeat(heat);
		}

		DCHumidity humid = ClimateAPI.registerBlock.getHumidity(block, meta);
		if(getElementsFromHumid(humid)!=null){
			return getElementsFromHumidity(humid);
		}
		return null;
	}
	public static ElementTable getElementsTable(Block block,int meta){
		DCHeatTier heat = ClimateAPI.registerBlock.getHeatTier(block, meta);
		if(getElementsFromHeatTier(heat)!=null){
			return getElementsFromHeatTier(heat);
		}

		DCHumidity humid = ClimateAPI.registerBlock.getHumidity(block, meta);
		if(getElementsFromHumid(humid)!=null){
			return getElementsFromHumid(humid);
		}
		return null;
	}
}
