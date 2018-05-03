package mods.hinasch.unsaga.core.potion;

import java.util.List;
import java.util.Set;
import java.util.function.Function;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import mods.hinasch.lib.util.VecUtil;
import mods.hinasch.unsaga.UnsagaMod;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAITasks;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;

public class PotionDisturbAI extends PotionUnsaga{

	public static Set<Potion> checkExpiredPotions = Sets.newHashSet();
	public static void checkExpired(LivingUpdateEvent e){
		for(PotionEffect effect:e.getEntityLiving().getActivePotionEffects()){
			if(checkExpiredPotions.contains(effect.getPotion())){
				restoreAI(e.getEntityLiving(),effect.getPotion());
			}
		}
	}

	public static class Effect extends PotionEffect{
		public Set<EntityAITasks.EntityAITaskEntry> backup = Sets.newHashSet();
		public List<Function<EntityCreature,EntityAIBase>> addedTasks;
		public boolean hasAddedTask = false;
		public Effect(Potion potionIn, int durationIn) {
			super(potionIn, durationIn);
			// TODO 自動生成されたコンストラクター・スタブ
		}

	}
	//	public static class Data implements IExtendedPotionData{
	//		public Set<EntityAITasks.EntityAITaskEntry> backup = Sets.newHashSet();
	//		public List<Function<EntityCreature,EntityAIBase>> addedTasks;
	//		public boolean hasAddedTask = false;
	//		@Override
	//		public void readFromNBT(NBTTagCompound nbt) {
	//			// TODO 自動生成されたメソッド・スタブ
	//
	//		}
	//		@Override
	//		public void writeToNBT(NBTTagCompound nbt) {
	//			// TODO 自動生成されたメソッド・スタブ
	//
	//		}
	//
	//	}

	List<Function<EntityCreature,EntityAIBase>> addTasks;

	protected PotionDisturbAI(String name, int u, int v,Function<EntityCreature,EntityAIBase>... ais) {
		super(name, true, 0xff0000, u, v);
		this.addTasks = Lists.newArrayList(ais);
		checkExpiredPotions.add(this);
		// TODO 自動生成されたコンストラクター・スタブ
	}

	public void addTask(EntityCreature creature,List<Function<EntityCreature,EntityAIBase>> ai,Effect d){
		for(Function<EntityCreature,EntityAIBase> a:ai){
			creature.tasks.addTask(0, a.apply(creature));
		}
		d.addedTasks = ai;
	}
	protected void backUpTask(EntityCreature c,Effect d){
		d.backup.addAll(c.tasks.taskEntries);
	}
	//	@Override
	//	public IExtendedPotionData getExtendedPotionData() {
	//		// TODO 自動生成されたメソッド・スタブ
	//		return new Data();
	//	}
	private void modifyAI(EntityLivingBase living){
		if(!(living.getActivePotionEffect(this) instanceof Effect)){
			int duration = living.getActivePotionEffect(this).getDuration();
			living.removePotionEffect(this);
			living.addPotionEffect(new Effect(this,duration));
		}
		if(living instanceof EntityCreature){
			EntityCreature c = (EntityCreature)living;
			//			if(ExtendedPotionCapability.adapter.hasCapability(living)){
			Effect data = (Effect) living.getActivePotionEffect(this);
			//				if(data instanceof Data){
			//					Data d = (Data) data;
			if(!data.hasAddedTask){
				this.removeTasks(c, data);
				this.addTask(c, this.addTasks, data);


				data.hasAddedTask = true;
				UnsagaMod.logger.trace(this.getClass().getName(), "AIを変更しました。");
				//					}
				//				}
			}
		}

		//		if(EntityStateCapability.adapter.hasCapability(living)){
		//			StatePotion state = (StatePotion) EntityStateCapability.adapter.getCapability(living).getState(StateRegistry.instance().statePotion);
		//			//							UnsagaMod.logger.trace(this.getName(),state);
		//			if(!state.isHasAddedFearTask() && living instanceof EntityCreature){
		//				state.removeTasks((EntityCreature) living);
		//				state.addTask((EntityCreature) living,new EntityAIAvoidEntity((EntityCreature) living, EntityPlayer.class, 10.0F, 1.0D, 1.2D));
		//				state.setHasAddedFearTask(true);
		//				UnsagaMod.logger.trace(this.getName(), "AI埋め込み成功です");
		//			}
		//		}
	}

	@Override
	public void performEffect(EntityLivingBase living, int p_76394_2_)
	{
		super.performEffect(living, p_76394_2_);
		World world = living.getEntityWorld();
		if(living instanceof EntityPlayer){

			if(living instanceof EntityPlayer && living.getRNG().nextInt(4)==0){
				world.getEntitiesWithinAABB(EntityLivingBase.class, living.getEntityBoundingBox().grow(3.0D)
						,IMob.MOB_SELECTOR)
				.forEach(in ->{
					Vec3d vec = VecUtil.getHeadingToEntityVec(living,in).normalize().scale(0.2D);
					living.setVelocity(-vec.x, 0 , -vec.z);
				});


			}
		}else{
			this.modifyAI(living);
		}


	}
	//	@Override
	//	public void performEffectOnEnd(LivingUpdateEvent e){
	//		super.performEffectOnEnd(e);
	//		e.getEntityLiving().removeActivePotionEffect(this);
	//		if(e.getEntityLiving().isPotionActive(this)){
	//			this.restoreAI(e.getEntityLiving());
	//		}
	//
	//	}
	public void removeTasks(EntityCreature creature,Effect data){
		this.backUpTask(creature, data);
		List<EntityAIBase> list = Lists.newArrayList();
		for(EntityAITasks.EntityAITaskEntry task:creature.tasks.taskEntries){
			list.add(task.action);
		}
		for(EntityAIBase ai:list){
			creature.tasks.removeTask(ai);
		}
	}

	/**
	 *AIを復元
	 * @param living
	 */
	private static void restoreAI(EntityLivingBase living,Potion potion){
		//		if(ExtendedPotionCapability.adapter.hasCapability(living)){
		PotionDisturbAI.Effect data = (Effect) living.getActivePotionEffect(potion);
		if(data.hasAddedTask && living instanceof EntityCreature){
			restoreTasks((EntityCreature) living, data);
			data.hasAddedTask = false;
			UnsagaMod.logger.trace(PotionDisturbAI.class.getName(), "AIを復元しました。");
		}
		//		}
	}



	private static void restoreTasks(EntityCreature creature,PotionDisturbAI.Effect data){
		for(EntityAITasks.EntityAITaskEntry  entry:data.backup){
			creature.tasks.addTask(entry.priority, entry.action);
		}
		if(data.addedTasks!=null){
			for(Function<EntityCreature,EntityAIBase> func:data.addedTasks){
				creature.tasks.removeTask(func.apply(creature));
			}

		}

	}
}
