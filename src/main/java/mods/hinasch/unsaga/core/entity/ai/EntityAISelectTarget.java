package mods.hinasch.unsaga.core.entity.ai;

import mods.hinasch.unsaga.status.TargetHolderCapability;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAITarget;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;

@Deprecated
public class EntityAISelectTarget extends EntityAITarget{

    EntityTameable tameable;
    EntityLivingBase attacker;
    private int timestamp;

    public EntityAISelectTarget(EntityTameable theEntityTameableIn)
    {
        super(theEntityTameableIn, false);
        this.tameable = theEntityTameableIn;
        this.setMutexBits(1);
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute()
    {
        if (!this.tameable.isTamed())
        {
            return false;
        }
        else
        {
            EntityLivingBase owner = this.tameable.getOwner();

            if (owner == null)
            {
                return false;
            }
            else
            {
            	if(!TargetHolderCapability.adapter.getCapability(owner).getTarget().isPresent()){
            		return false;
            	}
            	EntityLivingBase target = TargetHolderCapability.adapter.getCapability(owner).getTarget().get();

            	if(owner instanceof EntityPlayer){
                	if(!(target instanceof IMob)){
                		return false;
                	}
            	}

                this.attacker = TargetHolderCapability.adapter.getCapability(owner).getTarget().get();
                int i = owner.getLastAttackedEntityTime();
                return this.isSuitableTarget(this.attacker, false) && this.tameable.shouldAttackEntity(this.attacker, owner);
            }
        }
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting()
    {
        this.taskOwner.setAttackTarget(this.attacker);
        EntityLivingBase entitylivingbase = this.tameable.getOwner();

        if (entitylivingbase != null)
        {
            this.timestamp = entitylivingbase.getLastAttackedEntityTime();
        }

        super.startExecuting();
    }

}
