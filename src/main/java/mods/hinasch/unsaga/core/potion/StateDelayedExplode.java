package mods.hinasch.unsaga.core.potion;

import mods.hinasch.lib.world.WorldHelper;
import mods.hinasch.lib.world.XYZPos;
import mods.hinasch.unsaga.util.LivingHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.util.math.BlockPos;

public class StateDelayedExplode extends LivingState{

	protected StateDelayedExplode(String name) {
		super(name);
		// TODO 自動生成されたコンストラクター・スタブ
	}


	@Override
	public void performEffect(EntityLivingBase entityLivingBaseIn, int p_76394_2_)
	{
		super.performEffect(entityLivingBaseIn, p_76394_2_);
		Potion delayExplode = UnsagaPotions.instance().DELAYED_EXPLODE;
		if(LivingHelper.isStateActive(entityLivingBaseIn,delayExplode)){
			if(LivingHelper.getState(entityLivingBaseIn,delayExplode).getDuration()<=1){
				LivingHelper.ADAPTER.getCapability(entityLivingBaseIn).removeState(delayExplode);
				if(entityLivingBaseIn.onGround){
					XYZPos pos = XYZPos.createFrom(entityLivingBaseIn);
					WorldHelper.createExplosionSafe(entityLivingBaseIn.getEntityWorld(),null,pos, 3, true);
				}else{
					BlockPos pos = entityLivingBaseIn.getEntityWorld().getHeight(entityLivingBaseIn.getPosition());
					if(pos.getY()>0){
						WorldHelper.createExplosionSafe(entityLivingBaseIn.getEntityWorld(),null,new XYZPos(pos), 3, true);
					}

				}
			}
		}
	}
}
