package mods.hinasch.unsaga.common.tool;

import java.util.List;
import java.util.stream.Collectors;

import mods.hinasch.lib.util.HSLibs;
import mods.hinasch.unsaga.ability.AbilityCapability;
import mods.hinasch.unsaga.ability.IAbilityAttachable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

/** アビリティの表示*/
public class ComponentDisplayAbility extends ComponentDisplayInfo{

	public ComponentDisplayAbility() {
		super(4,(is,ep,list,sw)->is!=null &&AbilityCapability.adapter.hasCapability(is));
	}

	@Override
	public void addInfo(ItemStack is, EntityPlayer ep, List dispList, boolean par4) {
		if(AbilityCapability.adapter.hasCapability(is)){
			IAbilityAttachable instance = AbilityCapability.adapter.getCapability(is);
			String abilityNames  = instance.getLearnedAbilities().stream().map(in -> in.getLocalized())
					.collect(Collectors.joining("/"));

			dispList.add(HSLibs.translateKey("tooltip.unsaga.ability")+":"+abilityNames);
		}


	}

}
