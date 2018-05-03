package mods.hinasch.unsaga.lp;

import mods.hinasch.unsaga.UnsagaMod;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.MathHelper;

public class LPInitializeHelper {

	public static int getLPFromEntity(EntityLivingBase entity){


		if(entity instanceof EntityPlayer){
			return UnsagaMod.configs.getDefaultPlayerLifePoint();
		}



		float f1 = MathHelper.clamp(entity.getMaxHealth()/5.0F,1.0F, 256.0F);


		if(entity.getMaxHealth()<=1.0F){
			f1 = 0.0F;
		}

		if(entity instanceof EntityTameable){
			f1 += 3.0F;
		}
//		UnsagaMod.logger.trace(entity.getName(), f1);
		return (int)f1;
	}
}
