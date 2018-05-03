package mods.hinasch.unsaga.core.entity.mob;

import java.util.Random;

import mods.hinasch.unsaga.core.entity.ai.EntityAISpell.ISpellAI;
import mods.hinasch.unsaga.core.entity.ai.EntityAIUnsagaSpell;
import mods.hinasch.unsaga.core.entity.ai.EntityAIUnsagaSpell.ISpellCaster;
import mods.hinasch.unsagamagic.spell.Spell;
import mods.hinasch.unsagamagic.spell.SpellRegistry;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.EntityMoveHelper;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class EntityStormEater extends EntityMob implements ISpellAI,ISpellCaster{



    private float heightOffset = 0.5F;
    private int heightOffsetUpdateTime;
	int animationTick = 0;
	int spellTicks = 0;

	public EntityStormEater(World worldIn) {
		super(worldIn);
		this.setNoGravity(true);
//        this.moveHelper = new GhastMoveHelper(this);

	}

    public void fall(float distance, float damageMultiplier)
    {
    }

    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(3.0D);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.53000000417232513D);
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(48.0D);
    }
    protected void initEntityAI()
    {

//    	SpellRegistry spells = SpellRegistry.instance();
//    	List<SpellAIData> spellList = Lists.newArrayList();
//    	spellList.add(new SpellAIData(spells.CALL_THUNDER,40.0F,10.0F,20));
//    	spellList.add(new SpellAIData(spells.ICE_NEEDLE,40.0F,10.0F,20));
//    	spellList.add(new SpellAIData(spells.BUBBLE_BLOW,50.0F,0.0F,10));
//    	spellList.add(new SpellAIData(spells.waterShield,50.0F,0.0F,10));
//        this.tasks.addTask(4, new EntityBlaze.AIFireballAttack(this));
//        this.tasks.addTask(5, new EntityAIMoveTowardsRestriction(this, 1.0D));
        this.tasks.addTask(4, new EntityAIAttackMelee(this, 1.0D, false));
        this.tasks.addTask(4, new EntityAIUnsagaSpell(this));
//        this.tasks.addTask(5, new EntityAIMoveTowardsRestriction(this, 1.0D));
        this.tasks.addTask(7, new EntityAIWander(this, 1.0D));
        this.tasks.addTask(8, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.tasks.addTask(8, new EntityAILookIdle(this));
        this.targetTasks.addTask(2, new EntityAIHurtByTarget(this, true, new Class[0]));
        this.targetTasks.addTask(1, new EntityAINearestAttackableTarget(this, EntityPlayer.class, true));
//		this.targetTasks.addTask(3, new EntityAISpell(this,spellList,1.0D,200,20.0F,10));
//        this.tasks.addTask(5, new AIRandomFly(this));
//        this.targetTasks.addTask(1, new EntityAIFindEntityNearestPlayer(this));
    }

    protected void updateAITasks()
    {
//        if (this.isWet())
//        {
//            this.attackEntityFrom(DamageSource.drown, 1.0F);
//        }
//
        --this.heightOffsetUpdateTime;

        if (this.heightOffsetUpdateTime <= 0)
        {
            this.heightOffsetUpdateTime = 100;
            this.heightOffset = 0.5F + (float)this.rand.nextGaussian() * 3.0F;
        }

        EntityLivingBase entitylivingbase = this.getAttackTarget();

        if (entitylivingbase != null && entitylivingbase.posY + (double)entitylivingbase.getEyeHeight() > this.posY + (double)this.getEyeHeight() )
        {
            this.motionY += (0.30000001192092896D - this.motionY) * 0.30000001192092896D;
            this.isAirBorne = true;
        }

        if (this.spellTicks > 0)
        {
            --this.spellTicks;
        }
        super.updateAITasks();
    }
	public int getAnimationTick(){
		return this.animationTick;
	}
	@Override
	public boolean canCastSpell() {
		// TODO 自動生成されたメソッド・スタブ
		return true;
	}

    public void onLivingUpdate()
    {
//        if (!this.onGround && this.motionY < 0.0D)
//        {
//            this.motionY *= 0.6D;
//        }

        super.onLivingUpdate();
    }
	@Override
    public void onUpdate()
    {
		super.onUpdate();
		this.animationTick += 3;

		if(this.animationTick>100){
			this.animationTick = 0;
		}
    }
    static class AIRandomFly extends EntityAIBase
    {
        private final EntityStormEater parentEntity;

        public AIRandomFly(EntityStormEater ghast)
        {
            this.parentEntity = ghast;
            this.setMutexBits(1);
        }

        /**
         * Returns whether the EntityAIBase should begin execution.
         */
        public boolean shouldExecute()
        {
            EntityMoveHelper entitymovehelper = this.parentEntity.getMoveHelper();

            if (!entitymovehelper.isUpdating())
            {
                return true;
            }
            else
            {
                double d0 = entitymovehelper.getX() - this.parentEntity.posX;
                double d1 = entitymovehelper.getY() - this.parentEntity.posY;
                double d2 = entitymovehelper.getZ() - this.parentEntity.posZ;
                double d3 = d0 * d0 + d1 * d1 + d2 * d2;
                return d3 < 1.0D || d3 > 3600.0D;
            }
        }

        /**
         * Returns whether an in-progress EntityAIBase should continue executing
         */
        public boolean continueExecuting()
        {
            return false;
        }

        /**
         * Execute a one shot task or start executing a continuous task
         */
        public void startExecuting()
        {
            Random random = this.parentEntity.getRNG();
            double d0 = this.parentEntity.posX + (double)((random.nextFloat() * 2.0F - 1.0F) * 16.0F);
            double d1 = this.parentEntity.posY + (double)((random.nextFloat() * 2.0F - 1.0F) * 16.0F);
            double d2 = this.parentEntity.posZ + (double)((random.nextFloat() * 2.0F - 1.0F) * 16.0F);
            this.parentEntity.getMoveHelper().setMoveTo(d0, d1, d2, 1.0D);
        }
    }
    static class GhastMoveHelper extends EntityMoveHelper
    {
        private final EntityStormEater parentEntity;
        private int courseChangeCooldown;

        public GhastMoveHelper(EntityStormEater ghast)
        {
            super(ghast);
            this.parentEntity = ghast;
        }

        public void onUpdateMoveHelper()
        {
            if (this.action == EntityMoveHelper.Action.MOVE_TO)
            {
                double d0 = this.posX - this.parentEntity.posX;
                double d1 = this.posY - this.parentEntity.posY;
                double d2 = this.posZ - this.parentEntity.posZ;
                double d3 = d0 * d0 + d1 * d1 + d2 * d2;

                if (this.courseChangeCooldown-- <= 0)
                {
                    this.courseChangeCooldown += this.parentEntity.getRNG().nextInt(5) + 2;
                    d3 = (double)MathHelper.sqrt(d3);

                    if (this.isNotColliding(this.posX, this.posY, this.posZ, d3))
                    {
                        this.parentEntity.motionX += d0 / d3 * 0.1D;
                        this.parentEntity.motionY += d1 / d3 * 0.1D;
                        this.parentEntity.motionZ += d2 / d3 * 0.1D;
                    }
                    else
                    {
                        this.action = EntityMoveHelper.Action.WAIT;
                    }
                }
            }
        }


        /**
         * Checks if entity bounding box is not colliding with terrain
         */
        private boolean isNotColliding(double x, double y, double z, double p_179926_7_)
        {
            double d0 = (x - this.parentEntity.posX) / p_179926_7_;
            double d1 = (y - this.parentEntity.posY) / p_179926_7_;
            double d2 = (z - this.parentEntity.posZ) / p_179926_7_;
            AxisAlignedBB axisalignedbb = this.parentEntity.getEntityBoundingBox();

            for (int i = 1; (double)i < p_179926_7_; ++i)
            {
                axisalignedbb = axisalignedbb.offset(d0, d1, d2);

                if (!this.parentEntity.world.getCollisionBoxes(this.parentEntity, axisalignedbb).isEmpty())
                {
                    return false;
                }
            }

            return true;
        }
    }
	@Override
	public boolean isSpellCasting() {
		// TODO 自動生成されたメソッド・スタブ
		return this.spellTicks > 0;
	}

	@Override
	public boolean canCastSpell(Spell spell) {
		// TODO 自動生成されたメソッド・スタブ
		return true;
	}

	@Override
	public int getSpellTicks() {
		// TODO 自動生成されたメソッド・スタブ
		return this.spellTicks;
	}

	@Override
	public void setSpellTicks(int ticks) {
		// TODO 自動生成されたメソッド・スタブ
		this.spellTicks = ticks;
	}

	@Override
	public Spell selectSpell(EntityLivingBase target) {
		if(rand.nextInt(4)==0){
			return SpellRegistry.ICE_NEEDLE;
		}
		if(rand.nextInt(4)==0){
			return SpellRegistry.CALL_THUNDER;
		}
		return SpellRegistry.BUBBLE_BLOW;
	}
}
