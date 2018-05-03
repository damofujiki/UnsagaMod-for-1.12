package mods.hinasch.unsaga.core.entity.mob;

import mods.hinasch.lib.util.VecUtil;
import mods.hinasch.lib.world.WorldHelper;
import mods.hinasch.unsaga.core.entity.ai.EntityAIArrowAttack;
import mods.hinasch.unsaga.core.entity.ai.EntityAIUnsagaSpell;
import mods.hinasch.unsaga.core.entity.ai.EntityAIUnsagaSpell.ISpellCaster;
import mods.hinasch.unsaga.core.entity.projectile.EntitySolutionLiquid;
import mods.hinasch.unsagamagic.spell.Spell;
import mods.hinasch.unsagamagic.spell.SpellRegistry;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.world.World;

public class EntityRuffleTree extends EntityMob implements IRangedAttackMob,ISpellCaster{


//	private static final DataParameter<String> AI_SPELL = EntityDataManager.<String>createKey(EntityTreasureSlime.class, DataSerializers.STRING);
//	private static final DataParameter<Integer> AI_TARGET = EntityDataManager.<Integer>createKey(EntityTreasureSlime.class, DataSerializers.VARINT);
//	private static final DataParameter<Boolean> REQUIRE_SYNC = EntityDataManager.<Boolean>createKey(EntityTreasureSlime.class, DataSerializers.BOOLEAN);
	EntityLivingBase target;
	Spell spell;
	int spellTicks;
	boolean isReadyCast = false;

	public static EnumCreatureAttribute PLANT;

	public EntityRuffleTree(World worldIn) {
		super(worldIn);
		this.experienceValue = 5;
		this.width = 1.0F;
		this.height = 3.0F;

	}


    @Override
    protected void initEntityAI()
    {

//    	SpellRegistry spells = SpellRegistry.instance();
//    	List<SpellAIData> spellList = Lists.newArrayList();
//    	spellList.add(new SpellAIData(spells.SLEEP,30.0F,0.0F,60));
//    	spellList.add(new SpellAIData(spells.SUPER_SONIC,50.0F,10.0F,30));
//    	spellList.add(new SpellAIData(spells.SPOIL,50.0F,10.0F,30));
        this.tasks.addTask(1, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.tasks.addTask(4, new EntityAIUnsagaSpell(this));
        this.tasks.addTask(6, new EntityAILookIdle(this));
		this.targetTasks.addTask(3, new EntityAIArrowAttack(this,0.01D,60,15.0F));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, true));
//		this.targetTasks.addTask(4, new EntityAISpell(this,spellList,1.0D,100,15.0F,10));
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false, new Class[0]));
    }

	@Override
    public EnumCreatureAttribute getCreatureAttribute()
    {
        return PLANT;
    }
	@Override
    protected void applyEntityAttributes()
    {
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(70.0F);
		this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(5.0F);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.01D);
    }

	@Override
	public void attackEntityWithRangedAttack(EntityLivingBase target, float drive) {
//		UnsagaMod.logger.trace(this.getClass().getName(),this.getEntityWorld());
		EntitySolutionLiquid liquid = new EntitySolutionLiquid(this.getEntityWorld(), this);
		if(this.getEntityWorld().rand.nextInt(3)==0){
			liquid.setPoison();
		}
		liquid.setDamage((float)this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue(),0.3F);
		VecUtil.setThrowableToTarget(this, target, liquid);
		this.playSound(SoundEvents.ENTITY_GENERIC_SWIM, 1.0F, 1.0F);
		if(WorldHelper.isServer(getEntityWorld())){
			this.getEntityWorld().spawnEntity(liquid);
		}
	}


	@Override
    public void onUpdate()
    {
//    	this.onUpdateSpell(worldObj, this);
		super.onUpdate();

    }






	@Override
	public void setSwingingArms(boolean swingingArms) {
		// TODO 自動生成されたメソッド・スタブ

	}


	@Override
	public boolean isSpellCasting() {
		// TODO 自動生成されたメソッド・スタブ
		return spellTicks > 0;
	}


	@Override
	public boolean canCastSpell(Spell spell) {
		// TODO 自動生成されたメソッド・スタブ
		return true;
	}


	@Override
	public int getSpellTicks() {
		// TODO 自動生成されたメソッド・スタブ
		return spellTicks;
	}


	@Override
	public void setSpellTicks(int ticks) {
		// TODO 自動生成されたメソッド・スタブ
		this.spellTicks = ticks;
	}


	@Override
	public Spell selectSpell(EntityLivingBase target) {
		if(rand.nextInt(3)==0){
			return SpellRegistry.SLEEP;
		}
		if(rand.nextInt(3)==0){
			return SpellRegistry.SPOIL;
		}
		return SpellRegistry.SUPER_SONIC;
	}
}
