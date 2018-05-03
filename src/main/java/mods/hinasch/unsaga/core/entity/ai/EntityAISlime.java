//package mods.hinasch.unsaga.core.entity.ai;
//
//import net.minecraft.entity.EntityLiving;
//import net.minecraft.entity.ai.EntityAIBase;
//import net.minecraft.entity.player.EntityPlayer;
//import net.minecraft.util.SoundEvent;
//
//
//public class EntityAISlime extends EntityAIBase{
//
//	//~baseにはfaceentityが無いのでentitylivingから継承
//	public final EntityLiving entityHost;
//	public final ISlimeAI hostSlime;
//
//
//	public EntityAISlime(EntityLiving living){
//		this.entityHost = living;
//		this.hostSlime =(ISlimeAI) living;
//
//	}
//	//AIを実行すべきか
//	@Override
//	public boolean shouldExecute() {
//		// TODO 自動生成されたメソッド・スタブ
//		if(this.entityHost instanceof ISlimeAI){
//			return true;
//		}
//		return false;
//	}
//
//	//AIを遂行するか中断するか
//    public boolean continueExecuting()
//    {
//        return true;
//    }
//    //AIIが消される前に実行
//    public void resetTask()
//    {
//
//    	entityHost.setJumping(false);
//    }
//
//    //AIの本体、continueExecutingがtrueなら実行される
//    public void updateTask()
//    {
//    	//スライムをそのままもってきた
//       //this.entityHost.des();
//    	hostSlime.despawn();
//        EntityPlayer entityplayer = this.entityHost.world.getClosestPlayerToEntity(this.entityHost, 16.0D);
//
//
//        if (entityplayer != null)
//        {
//        	this.entityHost.faceEntity(entityplayer, 10.0F, 20.0F);
//        	this.entityHost.setAttackTarget(entityplayer);
//        }
//
//        if(this.entityHost.onGround){
//            this.decrSlimeJumpDelay();
//        }
//
//        if (entityHost.onGround && hostSlime.getSlimeJumpDelay() <= 0)
//        {
//
//        	//Unsaga.logger.log("jump",hostSlime.getSlimeJumpDelay());
//        	hostSlime.setSlimeJumpDelay(hostSlime.getJumpDelay());
//
//            if (entityplayer != null)
//            {
//                this.hostSlime.setSlimeJumpDelay(this.hostSlime.getSlimeJumpDelay()/3);
//            }
//
//            entityHost.getJumpHelper().setJumping();
//
//            if (hostSlime.makesSoundOnJump())
//            {
//            	entityHost.playSound(hostSlime.getJumpSound(), hostSlime.getSlimeSoundVolume(), ((entityHost.getRNG().nextFloat() - entityHost.getRNG().nextFloat()) * 0.2F + 1.0F) * 0.8F);
//            }
//
//            EntitySlime
//           // entityHost.setmo
//            entityHost.moveRelative(1.0F - entityHost.getRNG().nextFloat() * 2.0F, (float)(1 * hostSlime.getSlimeSize()));
////            entityHost.moveStrafing = 1.0F - entityHost.getRNG().nextFloat() * 2.0F;
////            entityHost.moveForward = (float)(1 * hostSlime.getSlimeSize());
//        }
//        else
//        {
//
//        	//Unsaga.logger.log("not jump",hostSlime.getSlimeJumpDelay());
//        	//entityHost.getJumpHelper().setJumping();
//
//            if (entityHost.onGround)
//            {
//            	entityHost.moveEntityWithHeading(0, 0);
//            }
//        }
//        //entityHost.rotationYawHead = entityHost.rotationYaw;
//    }
//
//    public void decrSlimeJumpDelay(){
//    	hostSlime.setSlimeJumpDelay(hostSlime.getSlimeJumpDelay()-1);
//    }
//    public static interface ISlimeAI{
//    	public int getJumpDelay();
//    	public int getSlimeJumpDelay();
//    	public void setSlimeJumpDelay(int par1);
//    	public SoundEvent getJumpSound();
//    	public float getSlimeSoundVolume();
//    	public boolean makesSoundOnJump();
//    	public int getSlimeSize();
//    	public void despawn();
//    }
//}
