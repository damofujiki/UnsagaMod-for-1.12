package mods.hinasch.unsaga.core.event.old;

import java.util.Set;

import mods.hinasch.lib.world.EnvironmentalManager;
import mods.hinasch.lib.world.XYZPos;
import mods.hinasch.unsaga.skillpanel.SkillPanelAPI;
import mods.hinasch.unsaga.skillpanel.SkillPanels;
import mods.hinasch.unsaga.status.UnsagaStatus;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;

public class HealTimerCalculator {



	public static int calcHealTimer(EntityLivingBase playerIn){

		final XYZPos pos = XYZPos.createFrom(playerIn);
		final World world = playerIn.world;
		Biome biome =world.getBiome(pos);
		Set<BiomeDictionary.Type> types  = BiomeDictionary.getTypes(biome);
		int base = (int) playerIn.getEntityAttribute(UnsagaStatus.HEAL_SPEED).getAttributeValue();

		if(playerIn instanceof EntityPlayer){
			if(!SkillPanelAPI.hasPanel((EntityPlayer) playerIn, SkillPanels.ADAPTABILITY)){
				if(EnvironmentalManager.getCondition(world,pos,playerIn.world.getBiome(pos),playerIn).isHarsh){
					base += 50;
				}
			}
		}

		base = MathHelper.clamp(base, 10, 200);
		return base;
	}
}
