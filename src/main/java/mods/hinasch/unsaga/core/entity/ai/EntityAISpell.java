package mods.hinasch.unsaga.core.entity.ai;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import mods.hinasch.lib.entity.RangedHelper;
import mods.hinasch.lib.util.HSLibs;
import mods.hinasch.unsaga.UnsagaMod;
import mods.hinasch.unsaga.common.specialaction.IActionPerformer.TargetType;
import mods.hinasch.unsaga.core.potion.StateCasting;
import mods.hinasch.unsaga.status.TargetHolderCapability;
import mods.hinasch.unsagamagic.spell.Spell;
import mods.hinasch.unsagamagic.spell.SpellComponent;
import mods.hinasch.unsagamagic.spell.action.SpellCaster;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

@Deprecated
public class EntityAISpell extends EntityAIBase{

	protected final List<SpellAIData> spellList;
	protected List<SpellAIData> rangedList;
	protected EntityLivingBase attackTarget;
	protected final EntityLiving entityHost;
	protected final ISpellAI entityInvoker;
	protected double entityMoveSpeed;
	protected int rangedAttackTime;
	protected int maxRangedAttackTime;
    //索敵範囲
	protected float search;
	@Deprecated
    protected float attackPowerFromRange;
	protected int cooling;

	//謎
    private int field_96561_g = 60;

    final int chance;

	public EntityAISpell(ISpellAI host,List<SpellAIData> spellList,double moveSpeed,int interval,float search,int chance){
		this.rangedAttackTime = -1;
		this.spellList = spellList;
		this.rangedList = new ArrayList();
		this.entityHost = (EntityLiving) host;
		this.entityInvoker = (ISpellAI)host;
		this.entityMoveSpeed = moveSpeed;
		this.maxRangedAttackTime = interval;
		this.attackPowerFromRange = search;
		this.search = search * search;
		this.chance = chance;
		this.setMutexBits(4);
	}
	@Override
	public boolean shouldExecute() {
//		int rnd = this.entityHost.getRNG().nextInt(chance);
//		UnsagaMod.logger.trace(this.getClass().getName(), rnd);
//		if(rnd==0){
//			return false;
//		}

        EntityLivingBase entitylivingbase = this.entityHost.getAttackTarget();
        if(this.entityInvoker.canCastSpell()){
            if (entitylivingbase == null)
            {
                return false;
            }
            else
            {

                    this.attackTarget = entitylivingbase;
                    return true;


            }
        }

        return false;
	}

	@Override
    public void resetTask()
    {
        this.attackTarget = null;
        this.cooling = 0;
        this.rangedAttackTime = -1;
    }

    @Override
    public boolean shouldContinueExecuting()
    {
        return this.shouldExecute() || !this.entityHost.getNavigator().noPath();
    }

    @Override
    public void updateTask()
    {
//    	if(this.isReadyCast()){
//    		SpellCaster caster = this.getCastingState().getCastingSpell().get();
//    		caster.broadCastMessage(HSLibs.translateKey("msg.unsaga.enemy.cast.end", this.entityHost.getName(),caster.getActionProperty().getLocalized()));
//    		caster.cast();
//    		this.resetCast();
//    	}

//    	if(this.isCasting()){
//
//    		if(this.entityHost.ticksExisted % 5 ==0){
////        		UnsagaMod.logger.trace("casttime", this.getCastingState().getCastingTime(),this.getCastingState().getCastingSpell().get());
//    			this.getCastingState().decrCastingTime();
//    		}
//    		if(this.entityHost.ticksExisted % 10 ==0){
//    			HSLib.core().getPacketDispatcher().sendToAllAround(PacketParticle.create(XYZPos.createFrom(entityHost), EnumParticleTypes.SPELL, 10), PacketUtil.getTargetPointNear(entityHost));
//    		}
//    	}


        double d0 = this.entityHost.getDistanceSq(this.attackTarget.posX, this.attackTarget.getEntityBoundingBox().minY, this.attackTarget.posZ);
        boolean flag = this.entityHost.getEntitySenses().canSee(this.attackTarget);

        if (flag)
        {
            ++this.cooling;
        }
        else
        {
            this.cooling = 0;
        }

//        if (d0 <= (double)this.search && this.cooling >= 20)
//        {
//            this.entityHost.getNavigator().clearPathEntity();
//        }
//        else
//        {
//            this.entityHost.getNavigator().tryMoveToEntityLiving(this.attackTarget, this.entityMoveSpeed);
//        }

        this.entityHost.getLookHelper().setLookPositionWithEntity(this.attackTarget, 30.0F, 30.0F);
//        float f;
//    	UnsagaMod.logger.trace(this.getClass().getName(), this.rangedAttackTime);
        if (--this.rangedAttackTime == 0)
        {

            if (d0 > (double)this.search || !flag)
            {
                return;
            }


            this.executeSpell(this.attackTarget);
            this.rangedAttackTime = this.maxRangedAttackTime;


        }
        else if (this.rangedAttackTime < 0)
        {
//            f = MathHelper.sqrt_double(d0) / this.attackPowerFromRange;
            this.rangedAttackTime = this.maxRangedAttackTime;
        }
    }


//    private boolean isCasting(){
//    	return this.getCastingState().getCastingTime()>0;
//    }
//
//    private boolean isReadyCast(){
//    	return this.getCastingState().getCastingTime()<=0 && this.getCastingState().getCastingSpell().isPresent();
//    }
//    private void resetCast(){
//    	this.getCastingState().setCastingSpell(null);
//    	this.getCastingState().setCastingTime(0);
//    }
//    private StateCast getCastingState(){
//    	return (StateCast) EntityStateCapability.adapter.getCapability(entityHost).getState(StateRegistry.instance().stateSpell);
//    }

    public List<SpellAIData> getSpellList(){
    	return this.spellList;
    }

    public void executeSpell(EntityLivingBase target){
    	UnsagaMod.logger.trace(this.getClass().getName(), "called");
//    	if(StatePropertySpellCast.getStateCast(entityHost).getCastingTime()>0){
//    		return;
//    	}

    	final double rangeToTarget = this.entityHost.getDistance(target);
    	this.rangedList = this.getSpellList().stream().filter(p ->p.maxDistance>=rangeToTarget && p.minDistance<=rangeToTarget)
    			.collect(Collectors.toList());

    	UnsagaMod.logger.trace("spellList",this.spellList,"distance:",rangeToTarget,"Spell Ranged:",this.rangedList);
    	if(!this.rangedList.isEmpty()){

    		int rn = this.rangedList.size()<=1? 0: this.entityHost.getRNG().nextInt(this.rangedList.size());
    		Spell spell = this.rangedList.get(rn).spell;
    		int castingTime = this.rangedList.get(rn).castTime;
    		if(spell!=null && target!=null){
    			UnsagaMod.logger.trace("distance:", rangeToTarget);

        		this.cast(target, spell,castingTime);
    		}

//    		this.entityInvoker.setReadyCast(spell, target);

//    		TargetHolderCapability
//    		DebuffHelper.addLivingDebuff(entityHost, ((LivingStateCast)UnsagaMod.debuffs.casting.createLivingDebuff(3)).setSpell(spell).setEntityid(target.getEntityId()));
//    		DebuffHelper.addLivingDebuff(entityHost, new LivingStateCast(Unsaga.debuffs.cast,3,spell,target.getEntityId()));
//    		this.invoker = new InvokeSpell(spell, this.entityHost.worldObj, this.entityHost, null);
//    		this.invoker.setTarget(target);
//    		if(!this.entityHost.worldObj.isRemote){
//        		this.invoker.run();
//    		}


    	}


    }

	public void cast(EntityLivingBase target,Spell spell,int castingTime){

		if(spell!=null){
			World world = this.entityHost.getEntityWorld();
			SpellComponent spellComponent = new SpellComponent(spell, 1.0F, 1.0F, false);
			SpellCaster caster = SpellCaster.ofEnemy(world, this.entityHost, spellComponent);
			EntityLivingBase beneficalTarget = this.entityHost;
			if(caster.isBenefical()){

				List<EntityLivingBase> list = world.getEntitiesWithinAABB(EntityLivingBase.class, this.entityHost.getEntityBoundingBox().grow(20.0D),RangedHelper.getTargetSelectorFromEntityType(entityHost,true));
				if(!list.isEmpty()){
					Collections.shuffle(list);
					beneficalTarget = list.get(0);
				}

			}

			caster.setTargetType(TargetType.TARGET);
			TargetHolderCapability.adapter.getCapability(entityHost).updateTarget(caster.isBenefical() ? beneficalTarget : target);
//			StateCast state = (StateCast) EntityStateCapability.adapter.getCapability(this.entityHost).getState(StateRegistry.instance().stateSpell);
			PotionEffect casting = new StateCasting.Effect(castingTime, caster);
//			state.setCastingSpell(caster);
//			state.setCastingTime(castingTime);
			this.entityHost.addPotionEffect(casting);
			UnsagaMod.logger.trace("isBenefical:", caster.isBenefical());
			caster.broadCastMessage(HSLibs.translateKey("msg.unsaga.enemy.cast.start",entityHost.getName(),spell.getLocalized()));
//			StatePropertySpellCast.sendSyncMobCastToClient(this.entityHost, target, spell, castingTime);
		}

	}

    public static class SpellAIData{
    	public final Spell spell;
    	public final double maxDistance;
    	public final double minDistance;
    	public final int castTime;
    	public SpellAIData(Spell spell,double max,double min,int castTime){
    		this.spell = spell;
    		this.maxDistance =max;
    		this.minDistance = min;
    		this.castTime = castTime;
    	}
    }

    /**
     * これを実装したあとonupdataからchestCastと
     */
    public static interface ISpellAI{
//    	public int getDataWatcherTargetID();
//    	public void setCastingSpellTarget(EntityLivingBase target);
//    	public void setCastingSpell(Spell spell);
//    	public String getDataWatcherSpellKey();
//    	public void setDataWatcherSpellKey(String key);
//    	public boolean isDataWatcherRequireSync();
//    	public void setDataWatcherRequireSync(boolean par1);
//    	public void setDataWathcerTargetID(int id);
    	public boolean canCastSpell();
    	public default int getCastingTime(Spell spell,boolean isBenefical){
    		if(isBenefical){
    			return (int)(spell.getBaseCastingTime() * 0.6F);
    		}
    		return spell.getBaseCastingTime();
    	}
//    	public boolean isReadyCast();
//    	public void setIsReadyCast(boolean par1);
//    	public Spell getCastingSpell();
//    	public EntityLivingBase getCastingTarget();

//    	public default void onUpdateSpell(World worldObj,EntityLivingBase host){
//        	this.syncClient(worldObj);
//        	this.checkCast(worldObj, host);
//    	}
//    	public default void setReadyCast(Spell spell, EntityLivingBase target) {
//    		UnsagaMod.logger.trace(this.getClass().getName(), "キャスト同期を開始",spell,target);
//
//    		if(target!=null && !target.isDead){
//
////        		this.setDataWatcherRequireSync(true);
////        		this.setDataWatcherSpellKey(spell.getKey().getResourcePath());
////        		this.setDataWathcerTargetID((int)target.getEntityId());
////        		this.setCastingSpell(spell);
////        		this.setCastingSpellTarget(target);
////        		this.setIsReadyCast(true);
//    		}
//
//
//    	}
//
//    	public default void checkCast(World world,EntityLivingBase host) {
//    		if(getCastingTarget()!=null && getCastingSpell()!=null && this.isReadyCast() && !this.getCastingTarget().isDead){
//    			UnsagaMod.logger.trace(this.getClass().getName(), "キャストを開始",this.getCastingSpell(),this.getCastingTarget());
//    			SpellCaster caster = SpellCaster.ofEnemy(world, host, this.getCastingSpell());
//    			caster.setTargetType(caster.isBenefical() ? TargetType.OWNER : TargetType.TARGET);
//    			TargetHolderCapability.adapter.getCapability(host).updateTarget(this.getCastingTarget());
//    			StateCast state = (StateCast) EntityStateCapability.adapter.getCapability(host).getState(StateRegistry.instance().stateSpell);
//    			state.setCastingSpell(caster);
//    			state.setCastingTime(caster.getActionProperty().getBaseCastingTime());
//    			caster.broadCastMessage(HSLibs.translateKey("msg.unsaga.enemy.cast.start",host.getName(),this.getCastingSpell().getLocalized()));
//    			this.setIsReadyCast(false);
//    			StatePropertySpellCast.getStateCast(host).setCastingSpell(caster);
//    			StatePropertySpellCast.getStateCast(host).setCastingTime(this.getCastingSpell().getBaseCastingTime());
//
//    		}
//
//    	}
//    	public default void syncClient(World worldObj){
//    		if(WorldHelper.isClient(worldObj) && this.isDataWatcherRequireSync()){
//
//    			String key = this.getDataWatcherSpellKey();
//    			this.setCastingSpell(SpellRegistry.instance().get(key));
//    			this.setCastingSpellTarget((EntityLivingBase) worldObj.getEntityByID(this.getDataWatcherTargetID()));
//    			this.setDataWatcherRequireSync(false);
//    			this.setIsReadyCast(true);
//    			UnsagaMod.logger.trace(this.getClass().getName(), "クライアント同期",this.getCastingSpell(),this.getCastingTarget());
//    		}
//
//    	}

    }
}
