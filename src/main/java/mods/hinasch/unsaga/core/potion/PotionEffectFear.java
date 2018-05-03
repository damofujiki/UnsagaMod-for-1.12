package mods.hinasch.unsaga.core.potion;

import mods.hinasch.unsaga.UnsagaMod;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

public class PotionEffectFear extends PotionEffect{


	boolean hasTaskRemoved = false;
	public PotionEffectFear(Potion potionIn, int durationIn, int amplifierIn) {
		super(potionIn, durationIn, amplifierIn);
		UnsagaMod.logger.trace("potion", "created");
	}

	@Override
    public boolean onUpdate(EntityLivingBase entityIn)
    {
		UnsagaMod.logger.trace("potion", "ok");
    	if(!this.hasTaskRemoved){
    		if(entityIn instanceof EntityCreature){
    			UnsagaMod.logger.trace("potion", "removed");
    			EntityCreature creature = (EntityCreature) entityIn;
    			creature.tasks.addTask(0, new EntityAIAvoidEntity(creature, EntityPlayer.class, 10.0F, 1.0D, 1.2D));
    			this.hasTaskRemoved = true;
    		}
    	}
    	return super.onUpdate(entityIn);
    }


}
