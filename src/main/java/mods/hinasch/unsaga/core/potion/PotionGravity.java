package mods.hinasch.unsaga.core.potion;

import mods.hinasch.lib.item.ItemUtil;
import mods.hinasch.lib.util.VecUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingFallEvent;

public class PotionGravity extends PotionUnsaga{

	protected PotionGravity(String name,boolean isBadEffect, int u, int v) {
		super(name, isBadEffect, isBadEffect ? 0x00ff00 : 0xff0000, u, v);
		// TODO 自動生成されたコンストラクター・スタブ
	}

	@Override
	public void performEffect(EntityLivingBase entityLivingBaseIn, int amplifier)
	{
		super.performEffect(entityLivingBaseIn, amplifier);
		World world = entityLivingBaseIn.world;
		if(this==UnsagaPotions.instance().STUN){
			if(!entityLivingBaseIn.isPotionActive(MobEffects.SLOWNESS)){
				entityLivingBaseIn.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS,ItemUtil.getPotionTime(3),10));
			}

		}
		if(this==UnsagaPotions.instance().GRAVITY){
			if(entityLivingBaseIn.posY>0){
				entityLivingBaseIn.motionY -= 1.0D;
			}
		}
		if(this==UnsagaPotions.instance().SPELL_MAGNET){
			int amp = 1 + amplifier;
			world.getEntitiesWithinAABB(Entity.class, entityLivingBaseIn.getEntityBoundingBox().grow(8.0D * amp)
					,in -> in!=entityLivingBaseIn &&(in instanceof EntityLivingBase || in instanceof EntityThrowable || in instanceof EntityArrow || in instanceof EntityXPOrb || in instanceof EntityItem))
			.forEach(in ->{
				Vec3d vec = VecUtil.getHeadingToEntityVec(in,entityLivingBaseIn).normalize().scale(0.2D);
				in.setVelocity(vec.x, vec.y , vec.z);
			});
		}
	}


	public static void onFall(LivingFallEvent e){
		if(e.getEntityLiving().isPotionActive(UnsagaPotions.GRAVITY)){
			e.setDamageMultiplier(1.5F);
		}
	}
}
