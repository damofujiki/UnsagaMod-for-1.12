package mods.hinasch.unsaga.skillpanel;

import java.util.Set;

import com.google.common.collect.Sets;

import mods.hinasch.lib.util.HSLibs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;

public class EventSkillPanel {

	public static void register(){
//		EventShield.registerEvents();
		HSLibs.registerEvent(new EventSaveDamage());


	}

	public static void onFall(LivingFallEvent e){
		if(SkillPanelAPI.hasPanel(e.getEntityLiving(),SkillPanels.TOUGHNESS)){
			float damage = (1.0F - (0.2F + 0.05F * SkillPanelAPI.getHighestPanelLevel(e.getEntityLiving(), SkillPanels.TOUGHNESS).getAsInt()));
			e.setDamageMultiplier(MathHelper.clamp(damage, 0.1F, damage));
		}
	}

	public static Set<DamageSource> AGAINST_IRON_WILL = Sets.newHashSet(DamageSource.STARVE,DamageSource.WITHER,DamageSource.LAVA);
	public static void onLivingDeath(LivingDeathEvent e){
		if(e.getSource().getTrueSource() instanceof EntityPlayer){
			EntityPlayer ep = (EntityPlayer) e.getSource().getTrueSource();
			SkillPanelAPI.getAllPanels(in -> in instanceof SkillPanelNegative)
			.forEach(in ->{
				SkillPanelNegative negative = (SkillPanelNegative) in;
				if(negative.getNegativeType()==SkillPanelNegative.Type.DAMAGE && SkillPanelAPI.hasPanel(ep, in)){
					if(negative.getTargetEntityPredicate().test(e.getEntityLiving())){
						ep.attackEntityFrom(DamageSource.GENERIC, 5.0F);
					}
				}
			});
		}
	}
	public static void onLivingDamage(LivingDamageEvent e){
		if(SkillPanelAPI.hasPanel(e.getEntityLiving(), SkillPanels.IRON_WILL)){
			if(AGAINST_IRON_WILL.contains(e.getSource())){
				float damage = e.getAmount() - (0.15F * SkillPanelAPI.getHighestPanelLevel(e.getEntityLiving(), SkillPanels.IRON_WILL).getAsInt());
				e.setAmount(MathHelper.clamp(damage, 0.01F, damage));
			}
		}
		if(e.getSource().getTrueSource() instanceof EntityPlayer){
			EntityPlayer ep = (EntityPlayer) e.getSource().getTrueSource();
			SkillPanelAPI.getAllPanels(in -> in instanceof SkillPanelNegative)
			.forEach(in ->{
				SkillPanelNegative negative = (SkillPanelNegative) in;
				if(negative.getNegativeType()==SkillPanelNegative.Type.WEAKNESS && SkillPanelAPI.hasPanel(ep, in)){
					if(negative.getTargetEntityPredicate().test(e.getEntityLiving())){
						e.setAmount(e.getAmount() * 0.5F);
					}
				}
			});
		}

	}
}
