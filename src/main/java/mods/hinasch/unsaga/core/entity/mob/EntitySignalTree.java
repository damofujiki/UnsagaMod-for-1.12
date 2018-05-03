package mods.hinasch.unsaga.core.entity.mob;

import java.util.List;

import com.google.common.collect.Lists;

import mods.hinasch.unsaga.core.entity.ai.EntityAISpell.SpellAIData;
import mods.hinasch.unsaga.core.entity.ai.EntityAIUnsagaSpell;
import mods.hinasch.unsaga.core.entity.ai.EntityAIUnsagaSpell.ISpellCaster;
import mods.hinasch.unsaga.core.potion.UnsagaPotions;
import mods.hinasch.unsagamagic.spell.Spell;
import mods.hinasch.unsagamagic.spell.SpellRegistry;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class EntitySignalTree extends EntityMob implements ISpellCaster{

	int spellTicks = 0;
	public EntitySignalTree(World worldIn) {
		super(worldIn);
		// TODO 自動生成されたコンストラクター・スタブ
	}

	@Override
    protected void applyEntityAttributes()
    {
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(20.0F);
		this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(2.0F);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.25D);
    }

	@Override
    public EnumCreatureAttribute getCreatureAttribute()
    {
        return EntityRuffleTree.PLANT;
    }

    protected void initEntityAI()
    {
//    	SpellRegistry spells = SpellRegistry.instance();
    	List<SpellAIData> spellList = Lists.newArrayList();
    	spellList.add(new SpellAIData(SpellRegistry.CALL_THUNDER,40.0F,10.0F,20));
    	spellList.add(new SpellAIData(SpellRegistry.BUBBLE_BLOW,40.0F,10.0F,10));
    	spellList.add(new SpellAIData(SpellRegistry.WATER_SHIELD,50.0F,0.0F,18));

        this.tasks.addTask(1, new EntityAISwimming(this));
        this.tasks.addTask(4, new EntityAIAttackMelee(this, 1.0D, false));
        this.tasks.addTask(5, new EntityAIWander(this, 0.8D));
        this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.tasks.addTask(6, new EntityAILookIdle(this));
        this.tasks.addTask(4, new EntityAIUnsagaSpell(this));
        this.targetTasks.addTask(1, new EntityAINearestAttackableTarget(this, EntityPlayer.class, true));
        this.targetTasks.addTask(2, new EntityAIHurtByTarget(this, false, new Class[0]));
//		this.targetTasks.addTask(3, new EntityAISpell(this,spellList,1.0D,200,20.0F,10));
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
    protected void updateAITasks()
    {
        super.updateAITasks();

        if (this.spellTicks > 0)
        {
            --this.spellTicks;
        }

//		UnsagaMod.logger.trace(this.getClass().getName(), spellTicks);
    }

	@Override
	public Spell selectSpell(EntityLivingBase target) {
		if(rand.nextInt(3)==0){
			if(!this.isPotionActive(UnsagaPotions.WATER_SHIELD)){
				return SpellRegistry.WATER_SHIELD;
			}
		}
		if(rand.nextInt(4)==0){
			return SpellRegistry.CALL_THUNDER;
		}
		return SpellRegistry.BUBBLE_BLOW;
	}

}
