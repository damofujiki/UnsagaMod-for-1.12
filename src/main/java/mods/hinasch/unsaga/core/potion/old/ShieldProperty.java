package mods.hinasch.unsaga.core.potion.old;

import java.util.function.Function;
import java.util.function.Predicate;

import mods.hinasch.unsaga.UnsagaMod;
import mods.hinasch.unsaga.core.potion.UnsagaPotions;
import mods.hinasch.unsaga.damage.DamageSourceUnsaga;
import net.minecraft.potion.Potion;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

public class ShieldProperty {

	UnsagaPotions up = UnsagaPotions.instance();


	public Potion getPotion() {
		return potion.apply(UnsagaPotions.instance());
	}

	public Predicate<DamageSourceUnsaga> getBlockableChecker() {
		return blockable;
	}

	public float getReduceDamage() {
		return reduceDamage;
	}

	public float getProtectProb() {
		return protectProb;
	}

	public ResourceLocation getTexture() {
		return texture;
	}

	final Function<UnsagaPotions,Potion> potion;
	Predicate<DamageSourceUnsaga> blockable;
	final float reduceDamage;
	final float protectProb;
	final ResourceLocation texture;

	public ShieldProperty(Function<UnsagaPotions,Potion> potion,float reduce,float protect,String res){
		this.potion = potion;
		this.reduceDamage = reduce;
		this.protectProb = protect;
		this.texture = new ResourceLocation(UnsagaMod.MODID,res);

	}

	public ShieldProperty setBlockableBehavior(Predicate<DamageSourceUnsaga> pre){
		this.blockable = pre;
		return this;
	}

	public static class ShieldEvent extends LivingHurtEventPotion{

		final ShieldProperty prop;
		public ShieldEvent(ShieldProperty s){
			super(s.potion);
			this.prop = s;
		}
		@Override
		public boolean apply(LivingHurtEvent e, DamageSourceUnsaga dsu) {

			return super.apply(e, dsu) && prop.getBlockableChecker().test(dsu);
		}

		@Override
		public String getName() {
			// TODO 自動生成されたメソッド・スタブ
			return prop.getPotion().getName()+".shield";
		}
		@Override
		public DamageSource processPotion(LivingHurtEvent e, DamageSourceUnsaga dsu, int amp) {
			float reduce = e.getAmount() * (prop.getReduceDamage() * amp);
			float amount = e.getAmount() - reduce;
			if(amount<0){
				amount = 0;
			}
			if(this.prop==ShieldPropertyRegistry.instance().aegis && dsu.isExplosion()){
				dsu.setNumberOfLPHurt(1);
				dsu.setStrLPHurt(0.5F);
				amount = 0;
			}
			if((prop.getProtectProb() + (amp * 0.1F))>=e.getEntityLiving().getRNG().nextFloat()){
				amount = 0;
			}
			e.setAmount(amount);

			return dsu;
		}

	}
}
