 package mods.hinasch.unsaga.skillpanel;

import java.util.Collection;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.google.common.base.Preconditions;

import mods.hinasch.unsaga.core.item.misc.skillpanel.ISkillPanel;
import mods.hinasch.unsaga.core.item.misc.skillpanel.SkillPanelCapability;
import mods.hinasch.unsaga.core.world.UnsagaWorldCapability;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public class SkillPanelAPI {


	public static NonNullList<ItemStack> getPanelStacks(EntityPlayer ep){
		Preconditions.checkNotNull(UnsagaWorldCapability.ADAPTER.getCapability(ep.getEntityWorld()));
		return UnsagaWorldCapability.ADAPTER.getCapability(ep.getEntityWorld()).getPanels(ep.getGameProfile().getId());
	}

	public static Collection<SkillPanel> getAllPanels(Predicate<SkillPanel> filter){
		return SkillPanelRegistry.instance().getProperties().stream().filter(filter).collect(Collectors.toList());
	}

	public static Collection<ISkillPanel> getAllPlayerPanels(EntityPlayer ep,Predicate<ISkillPanel> filter){
		return getPanelStacks(ep).stream().filter(in -> !in.isEmpty()).map(in -> SkillPanelCapability.adapter.getCapability(in)).collect(Collectors.toList());
	}
	public static OptionalInt getLevel(ItemStack panel){
		if(SkillPanelCapability.adapter.hasCapability(panel)){
			return OptionalInt.of(SkillPanelCapability.adapter.getCapability(panel).getLevel());
		}
		return OptionalInt.empty();
	}

	public static Optional<SkillPanel> getSkillPanel(ItemStack panel){
		if(SkillPanelCapability.adapter.hasCapability(panel)){
			return Optional.of(SkillPanelCapability.adapter.getCapability(panel).getPanel());
		}
		return Optional.empty();
	}



	public static boolean hasFamiliar(EntityPlayer ep){
		return SkillPanelRegistry.instance().familiars.stream().allMatch(in ->hasPanel(ep,in));
	}
	public static boolean hasPanel(EntityLivingBase ep,SkillPanel panel){
		if(ep instanceof EntityPlayer){
			return getHighestPanelLevel((EntityPlayer) ep,panel).isPresent();
		}
		return false;
	}
	public static OptionalInt getHighestPanelLevel(EntityLivingBase ep,SkillPanel panel){
		if(ep instanceof EntityPlayer){
			if(getPanelStacks((EntityPlayer) ep)==null){
				return OptionalInt.empty();
			}
			return getPanelStacks((EntityPlayer) ep).stream().filter(in ->{
				if(SkillPanelCapability.adapter.hasCapability(in)){
					return SkillPanelCapability.adapter.getCapability(in).getPanel()==panel;
				}
				return false;
			}).mapToInt(in -> SkillPanelCapability.adapter.getCapability(in).getLevel()).max();
		}
		return OptionalInt.empty();
//		if(found.isPresent()){
//			return OptionalInt.of(SkillPanelCapability.adapter.getCapability(found.get()).getLevel());
//		}
//		return OptionalInt.empty();
	}
}
