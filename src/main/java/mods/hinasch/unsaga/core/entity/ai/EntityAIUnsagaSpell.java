package mods.hinasch.unsaga.core.entity.ai;

import java.util.List;

import javax.annotation.Nullable;

import mods.hinasch.lib.entity.RangedHelper;
import mods.hinasch.lib.network.PacketUtil;
import mods.hinasch.lib.util.HSLibs;
import mods.hinasch.unsaga.UnsagaMod;
import mods.hinasch.unsaga.common.specialaction.IActionPerformer.TargetType;
import mods.hinasch.unsaga.core.entity.ai.EntityAIUnsagaSpell.ISpellCaster;
import mods.hinasch.unsaga.core.net.packet.PacketSyncActionPerform;
import mods.hinasch.unsaga.status.TargetHolderCapability;
import mods.hinasch.unsagamagic.spell.Spell;
import mods.hinasch.unsagamagic.spell.SpellComponent;
import mods.hinasch.unsagamagic.spell.action.SpellCaster;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.monster.IMob;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundEvent;

/**
 *
 * AIUseSpellを元にだいぶ雑に改造したAI
 *
 * @param <T>
 */
public class EntityAIUnsagaSpell<T extends EntityLiving & ISpellCaster> extends EntityAIBase{

	protected T caster;
//	protected Range<Double> spellRange;
	protected Spell spell;
	protected int spellWarmup;
	protected int spellCooldown;
	protected int spellCastingTimeMultiply;

	//    protected RangeMap<Double,Spell> castableSpells = TreeRangeMap.create();

	public EntityAIUnsagaSpell(T caster,int spellCastingTimeMultiply){
		this.caster = caster;
		this.spell = this.caster.selectSpell(null);
		this.spellCastingTimeMultiply = spellCastingTimeMultiply;
//		this.spellRange = range;
	}

	public EntityAIUnsagaSpell(T caster){
		this(caster, 10);
//		this.spellRange = range;
	}
	/**
	 * Returns whether the EntityAIBase should begin execution.
	 */
	public boolean shouldExecute()
	{

		if(!caster.canCastSpell(spell)){
			return false;
		}
		if (caster.getAttackTarget() == null)
		{
			return false;
		}
		else if (caster.isSpellCasting())
		{
			return false;
		}
		else
		{
			return caster.ticksExisted >= this.spellCooldown && this.caster.selectSpell(caster.getAttackTarget())!=null;
		}
	}

//	protected boolean existCastableSpell(EntityLivingBase target){
//		double distance = this.caster.getDistance(target);
//		return this.spellRange.contains(distance);
//	}
	/**
	 * Returns whether an in-progress EntityAIBase should continue executing
	 */
	public boolean shouldContinueExecuting()
	{
		return caster.getAttackTarget() != null && this.spellWarmup > 0;
	}

	/**
	 * Execute a one shot task or start executing a continuous task
	 */
	public void startExecuting()
	{
		if(!this.caster.isSpellCasting()){
			this.spell = this.caster.selectSpell(caster.getAttackTarget());
			RangedHelper.createChatBroadcastHelper(caster, 10.0D,HSLibs.translateKey("chat.unsaga.enemy.cast.start",this.caster.getName(),this.spell.getPropertyName())).invoke();
			this.spellWarmup = this.getCastWarmupTime();
			caster.setSpellTicks(this.getCastingTime());
			this.spellCooldown = caster.ticksExisted + this.getCastingInterval();
			SoundEvent soundevent = this.getSpellPrepareSound();

			if (soundevent != null)
			{
				caster.playSound(soundevent, 1.0F, 1.0F);
			}
		}


//		     caster.setCurrentSpell(this.spell);
	}

	/**
	 * Keep ticking a continuous task that has already been started
	 */
	public void updateTask()
	{
		--this.spellWarmup;

		if (this.spellWarmup == 0)
		{
			if(caster.getAttackTarget()!=null){
				this.castSpell(caster.getAttackTarget());
//				caster.playSound(SoundEvents.ENTITY_ILLAGER_CAST_SPELL, 1.0F, 1.0F);
			}

		}
	}

	protected void castSpell(EntityLivingBase target){
		SpellComponent component = new SpellComponent(this.spell,1.0F,1.0F,false);
		SpellCaster spellcaster = SpellCaster.ofEnemy(caster.world, caster, component);
		if(spellcaster.isBenefical()){

			List<EntityLivingBase> list = caster.world.getEntitiesWithinAABB(EntityLivingBase.class, caster.getEntityBoundingBox().grow(10.0D),IMob.MOB_SELECTOR);
			if(!list.isEmpty()){
				EntityLivingBase living = HSLibs.randomPick(caster.world.rand, list);
				spellcaster.setTarget(living);
				spellcaster.setTargetType(living==caster? TargetType.OWNER:TargetType.TARGET);
			}else{
				spellcaster.setTarget(caster);
				spellcaster.setTargetType(TargetType.OWNER);
			}
//			if(caster.world.rand.nextInt(2)==0){
//				spellcaster.setTarget(caster);
//				spellcaster.setTargetType(TargetType.OWNER);
//			}else{
//
//
//			}

		}else{
			spellcaster.setTarget(target);
			spellcaster.setTargetType(TargetType.TARGET);
			TargetHolderCapability.adapter.getCapability(caster).updateTarget(target);
		}


		spellcaster.cast();
		UnsagaMod.packetDispatcher.sendToAllAround(PacketSyncActionPerform.createSyncSpellCastPacket(this.caster.getEntityId(),this.spell),PacketUtil.getTargetPointNear(caster));

	}

	protected int getCastWarmupTime()
	{
		return 20;
	}

	public int getCastingTime(){
		return this.spell.getBaseCastingTime() * spellCastingTimeMultiply;
	}

	public int getCastingInterval(){
		return this.spell.getBaseCastingTime() * (spellCastingTimeMultiply/2);
	}


	protected SoundEvent getSpellPrepareSound(){
		return SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE;
	}


	/**
	 *
	 * updateAIあたりでspellticksを減らしていくこと
	 *
	 */
	public static interface ISpellCaster{
		public boolean isSpellCasting();
		public boolean canCastSpell(Spell spell);
		public int getSpellTicks();
		public void setSpellTicks(int ticks);
		/**
		 * ここで使う術を選択する、コンストラクタ時だけtargetがnullで来る
		 * @param target
		 * @return
		 */
		public Spell selectSpell(@Nullable EntityLivingBase target);
	}


}
