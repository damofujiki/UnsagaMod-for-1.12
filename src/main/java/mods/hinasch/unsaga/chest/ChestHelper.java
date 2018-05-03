package mods.hinasch.unsaga.chest;

import java.util.List;
import java.util.Random;

import com.google.common.collect.Lists;

import mods.hinasch.unsaga.skillpanel.SkillPanel;
import mods.hinasch.unsaga.skillpanel.SkillPanelAPI;
import mods.hinasch.unsaga.skillpanel.SkillPanels;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.math.MathHelper;

public class ChestHelper {

	public static BaseDifficulty DEFUSE = new BaseDifficulty(80,-0.89F,0.02F);
	public static BaseDifficulty UNLOCK = new BaseDifficulty(80,-0.89F,0.02F);
	public static BaseDifficulty DIVINE = new BaseDifficulty(80,-0.89F,0.02F);
	public static BaseDifficulty PENETRATE = new BaseDifficulty(90,-0.69F,0.02F);
	public static float getInteractionSuccessProb(BaseDifficulty base,IChestCapability chest,int panelLevel){
		int a = base.base+(int)(base.slope*(float)chest.getLevel());
		a = MathHelper.clamp(a, 10, 100);
		a += (panelLevel * 15);
		return a * 0.01F;
	}

	public static float getBaddestInteractionProb(BaseDifficulty base,IChestCapability chest,int panelLevel){
		float f = base.badslope * chest.getLevel();
		f -= (0.08F*panelLevel);
		f = MathHelper.clamp(f, 0, 100);
//		UnsagaMod.logger.trace("adgjosadgads", f);
		return f;
	}
	public static List<ChestTrap> getInitializedTraps(IChestCapability inst,Random rand){
		List<ChestTrap> traps = Lists.newArrayList();
		if(rand.nextFloat()<0.3F+0.0055F*inst.getLevel()){
			traps.add(ChestTraps.NEEDLE);
		}
		if(inst.getLevel()>15){
			if(rand.nextFloat()<0.3F+0.0055F*inst.getLevel()){
				traps.add(ChestTraps.POISON);
			}
		}
		if(inst.getLevel()>25){
			if(rand.nextFloat()<0.3F+0.0035F*inst.getLevel()){
				traps.add(ChestTraps.EXPLODE);
			}
			if(rand.nextFloat()<0.3F+0.0040F*inst.getLevel()){
				traps.add(ChestTraps.SLIME);
			}
		}
		return traps;
	}

	public static BaseDifficulty getBaseDifficulty(SkillPanel skill){
		if(skill==SkillPanels.DEFUSE){
			return ChestHelper.DEFUSE;
		}
		if(skill==SkillPanels.LOCKSMITH){
			return ChestHelper.UNLOCK;
		}
		if(skill==SkillPanels.FORTUNE){
			return ChestHelper.DIVINE;
		}
		if(skill==SkillPanels.SHARP_EYE){
			return ChestHelper.PENETRATE;
		}
		return ChestHelper.PENETRATE;
	}

	public static EnumActionResult tryInteraction(EntityPlayer ep,SkillPanel skill,Random rand,IChestCapability chest,int skillLevel){
		BaseDifficulty dif = getBaseDifficulty(skill);
		SkillPanel smartMove = SkillPanels.SMART_MOVE;
		int smartLevel = SkillPanelAPI.hasPanel(ep, smartMove)?SkillPanelAPI.getHighestPanelLevel(ep,smartMove).getAsInt() : 0;

		if(ChestHelper.getInteractionSuccessProb(dif,chest,skillLevel+smartLevel)>rand.nextFloat()){
			return EnumActionResult.SUCCESS;
		}
		if(ChestHelper.getBaddestInteractionProb(dif, chest,skillLevel+smartLevel)>rand.nextFloat()){
			return EnumActionResult.FAIL;
		}
		return EnumActionResult.PASS;

	}
	public static class BaseDifficulty{
		final int base;
		final float slope;
		final float badslope;
		public BaseDifficulty(int base,float slope,float badslope){
			this.base = base;
			this.slope = slope;
			this.badslope = badslope;
		}
	}
}
