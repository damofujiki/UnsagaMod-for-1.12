package mods.hinasch.unsaga.core.potion;

import mods.hinasch.lib.particle.ParticleHelper;
import mods.hinasch.lib.world.WorldHelper;
import mods.hinasch.lib.world.XYZPos;
import mods.hinasch.unsaga.util.LivingHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

public class LivingState extends PotionUnsaga{

	protected LivingState(String name) {
		super(name, false, 0, 0,0);
		// TODO 自動生成されたコンストラクター・スタブ
	}


	@Override
	public void performEffect(EntityLivingBase entityLivingBaseIn, int p_76394_2_)
	{
		super.performEffect(entityLivingBaseIn, p_76394_2_);
		World world = entityLivingBaseIn.world;
		if(LivingHelper.isStateActive(entityLivingBaseIn, UnsagaPotions.CANCEL_FALL)){
			entityLivingBaseIn.fallDistance = 0;
			if(entityLivingBaseIn.onGround){
				LivingHelper.getCapability(entityLivingBaseIn).removeState(UnsagaPotions.CANCEL_FALL);
			}
			if(WorldHelper.isClient(world)){
				ParticleHelper.MovingType.FLOATING.spawnParticle(world, XYZPos.createFrom(entityLivingBaseIn), EnumParticleTypes.CRIT, world.rand	, 10, 0.05D);
			}
		}
//		if(entityLivingBaseIn.isPotionActive(UnsagaPotions.CANCEL_HURT)){
//			if(!entityLivingBaseIn.isPotionActive(UnsagaPotions.MOVING_STATE)){
//				entityLivingBaseIn.removeActivePotionEffect(UnsagaPotions.CANCEL_HURT);
//			}
//		}
	}
	@Override
	public boolean hasStatusIcon() {
		return false;
	}


	@Override
	public void affectOnHurt(LivingHurtEvent e,int amplifier){

		if(!LivingHelper.ADAPTER.hasCapability(e.getEntityLiving())){
			return;
		}
		if(LivingHelper.isStateActive(e.getEntityLiving(),UnsagaPotions.CANCEL_HURT)){
			e.setAmount(0);
			e.setCanceled(true);
		}
	}


	public static void onFall(LivingFallEvent e){
		if(!LivingHelper.ADAPTER.hasCapability(e.getEntityLiving())){
			return;
		}
		if(LivingHelper.isStateActive(e.getEntityLiving(),UnsagaPotions.CANCEL_FALL)){
			e.setDistance(0);
			e.setCanceled(true);
		}
	}

	public static void setCancelHurt(EntityLivingBase living,int time){
		LivingHelper.getCapability(living).addState(new PotionEffect(UnsagaPotions.CANCEL_HURT,time,0,false,false));
	}

	public static void setCancelFall(EntityLivingBase living,int time){
		LivingHelper.getCapability(living).addState(new PotionEffect(UnsagaPotions.CANCEL_FALL,time,0,false,false));
	}
}
