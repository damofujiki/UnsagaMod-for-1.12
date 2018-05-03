package mods.hinasch.unsaga.minsaga;

import java.util.List;
import java.util.stream.Collectors;

import mods.hinasch.lib.item.ItemUtil;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemFood;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EventItemUseFinished {


	@SubscribeEvent
	public void onFoodEaten(LivingEntityUseItemEvent.Finish ev){
		if(ev.getItem().getItem() instanceof ItemFood){
			List<MinsagaForging.Ability> list = MinsagaUtil.getForgedArmors(ev.getEntityLiving()).stream().flatMap(in -> MinsagaForgeCapability.ADAPTER.getCapability(in).getAbilities().stream()).collect(Collectors.toList());
			if(list.contains(MinsagaForging.Ability.SEA)){
				int amount = (int) list.stream().filter(in -> in==MinsagaForging.Ability.SEA).count();
				ev.getEntityLiving().addPotionEffect(new PotionEffect(MobEffects.WATER_BREATHING,ItemUtil.getPotionTime(10*amount),0));
			}
			if(list.contains(MinsagaForging.Ability.FAERIE)){
				int amount = (int) list.stream().filter(in -> in==MinsagaForging.Ability.FAERIE).count();
				ev.getEntityLiving().addPotionEffect(new PotionEffect(MobEffects.JUMP_BOOST,ItemUtil.getPotionTime(10*amount),0));
				ev.getEntityLiving().addPotionEffect(new PotionEffect(MobEffects.SPEED,ItemUtil.getPotionTime(10*amount),0));
			}
		}
	}
}
