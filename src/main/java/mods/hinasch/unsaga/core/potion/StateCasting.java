package mods.hinasch.unsaga.core.potion;

import mods.hinasch.lib.particle.ParticleHelper;
import mods.hinasch.lib.world.WorldHelper;
import mods.hinasch.lib.world.XYZPos;
import mods.hinasch.unsagamagic.spell.action.SpellCaster;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;

public class StateCasting extends PotionUnsaga{

	protected StateCasting(String name) {
		super(name, false, 0, 0,0);
		// TODO 自動生成されたコンストラクター・スタブ
	}

	@Override
	public void performEffect(EntityLivingBase entityLivingBaseIn, int p_76394_2_)
	{
		super.performEffect(entityLivingBaseIn, p_76394_2_);
		World world = entityLivingBaseIn.world;
		PotionEffect effect = entityLivingBaseIn.getActivePotionEffect(this);
		if(effect instanceof Effect){
			Effect casting = (Effect) effect;
			if(WorldHelper.isClient(entityLivingBaseIn.getEntityWorld())){
				ParticleHelper.MovingType.FLOATING.spawnParticle(world, XYZPos.createFrom(entityLivingBaseIn)
						, EnumParticleTypes.ENCHANTMENT_TABLE, world.rand, 10, 0.02D);
			}
			if(effect.getDuration()<=1){
				casting.getCaster().cast();
				entityLivingBaseIn.removeActivePotionEffect(this);
			}
		}


	}

	public static class Effect extends PotionEffect{

		final SpellCaster caster;
		public Effect(int durationIn,SpellCaster caster) {
			super(UnsagaPotions.CASTING, durationIn);
			// TODO 自動生成されたコンストラクター・スタブ
			this.caster = caster;
		}

		public SpellCaster getCaster(){
			return this.caster;
		}
	}
}
