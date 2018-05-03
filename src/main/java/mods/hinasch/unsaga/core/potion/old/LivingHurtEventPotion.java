package mods.hinasch.unsaga.core.potion.old;

import java.util.function.Function;

import mods.hinasch.unsaga.core.potion.UnsagaPotions;
import mods.hinasch.unsaga.damage.DamageSourceUnsaga;
import mods.hinasch.unsaga.util.LivingHurtEventUnsagaBase;
import net.minecraft.potion.Potion;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

public abstract class LivingHurtEventPotion extends  LivingHurtEventUnsagaBase{

	Function<UnsagaPotions,Potion> potion;
	public LivingHurtEventPotion(Function<UnsagaPotions,Potion> potion){
		this.potion = potion;
	}
	@Override
	public boolean apply(LivingHurtEvent e, DamageSourceUnsaga dsu) {
		// TODO 自動生成されたメソッド・スタブ
		return e.getEntityLiving().isPotionActive(potion.apply(UnsagaPotions.instance()));
	}

	@Override
	public DamageSource process(LivingHurtEvent e, DamageSourceUnsaga dsu) {
		int amp = 1;
		if(e.getEntityLiving().getActivePotionEffect(UnsagaPotions.instance().SELF_BURNING)!=null){
			amp += e.getEntityLiving().getActivePotionEffect(UnsagaPotions.instance().SELF_BURNING).getAmplifier();
			return this.processPotion(e, dsu, amp);
		}
		return dsu;
	}

	/**
	 *
	 * @param e
	 * @param dsu
	 * @param amp 1～
	 * @return
	 */
	public abstract DamageSource processPotion(LivingHurtEvent e, DamageSourceUnsaga dsu,int amp);

	@Override
	public String getName() {
		// TODO 自動生成されたメソッド・スタブ
		return potion.apply(UnsagaPotions.instance()).getName();
	}

}
