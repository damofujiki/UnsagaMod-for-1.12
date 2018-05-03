package mods.hinasch.unsaga.core.entity.mob;

import java.util.UUID;

import javax.annotation.Nullable;

import com.google.common.collect.Lists;

import mods.hinasch.lib.iface.IIntSerializable;
import mods.hinasch.lib.util.HSLibs;
import mods.hinasch.lib.util.Statics;
import mods.hinasch.lib.world.WorldHelper;
import mods.hinasch.unsaga.core.entity.ai.EntityAIUnsagaSpell;
import mods.hinasch.unsaga.core.entity.ai.EntityAIUnsagaSpell.ISpellCaster;
import mods.hinasch.unsaga.core.entity.projectile.EntityFlyingAxe;
import mods.hinasch.unsaga.core.entity.projectile.EntityFlyingAxe.AxeMoveType;
import mods.hinasch.unsaga.core.entity.projectile.EntityMagicBall;
import mods.hinasch.unsaga.core.potion.UnsagaPotions;
import mods.hinasch.unsaga.damage.AdditionalDamageData;
import mods.hinasch.unsaga.damage.DamageHelper;
import mods.hinasch.unsaga.damage.DamageTypeUnsaga.General;
import mods.hinasch.unsaga.lp.LPAttribute;
import mods.hinasch.unsagamagic.spell.Spell;
import mods.hinasch.unsagamagic.spell.SpellRegistry;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackRanged;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;

public class EntityTreasureSlimeNew extends EntitySlime implements IRangedAttackMob,ISpellCaster{

	private static final DataParameter<Integer> SLIME_TYPE = EntityDataManager.<Integer>createKey(EntityTreasureSlimeNew.class, DataSerializers.VARINT);

	int spellTicks = 0;
	Spell currentSpell = SpellRegistry.EMPTY;
	public static enum Type implements IIntSerializable{
		YELLOW(0),RED(1),GREEN(2);

		final int meta;
		private Type(int meta){
			this.meta = meta;
		}
		@Override
		public int getMeta() {
			// TODO 自動生成されたメソッド・スタブ
			return meta;
		}

		public void setRenderColor(){
			switch(this){
			case GREEN:
				GlStateManager.color(0, 0.8F, 0);
				break;
			case RED:
				GlStateManager.color(0.8F, 0, 0);
				break;
			case YELLOW:
				GlStateManager.color(0.8F, 0.8F, 0);
				break;
			default:
				break;

			}
		}

		public static Type fromMeta(int meta){
			return HSLibs.fromMeta(Type.values(), meta);
		}
	}
	public EntityTreasureSlimeNew(World worldIn) {
		super(worldIn);

	}
	@Override
	protected void entityInit()
	{
		super.entityInit();
		this.dataManager.register(SLIME_TYPE, Integer.valueOf(0));
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
	public void setSlimeType(Type type){
		this.dataManager.set(SLIME_TYPE, (int)type.getMeta());

		if(this.getSlimeType()==Type.YELLOW){
	        this.tasks.addTask(4, new EntityAIUnsagaSpell(this));
		}

	}

	public Type getSlimeType(){
		int meta = this.dataManager.get(SLIME_TYPE);
		return Type.fromMeta(meta);
	}
	@Override
	protected void applyEntityAttributes()
	{
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).applyModifier(new AttributeModifier(UUID.fromString("789cf3a8-ac89-4248-929d-3fbf2ca55da9"),"slimemodifier", 20.0F, Statics.OPERATION_INCREMENT));
		this.getAttributeMap().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(3.0F);
	}
	protected void initEntityAI()
	{
		super.initEntityAI();
		this.targetTasks.addTask(1, new EntityAIAttackRanged(this,1.0D,60,15.0F));
	}

	@Override
	public void setDead()
	{
		this.isDead = true;
	}

	@Override
	protected int getAttackStrength()
	{
		if(this.getSlimeType()==Type.GREEN){
			return (int) this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue() + 1;
		}
		return (int) this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue();
	}
	@Override
	public void attackEntityWithRangedAttack(EntityLivingBase target, float distanceFactor) {

		if(this.isPotionActive(UnsagaPotions.LOCK_SLIME)){
			return;
		}



		if(this.getSlimeType()==Type.RED){
			if(rand.nextInt(2)==0){
				WorldHelper.safeSpawn(world, this.getRandomThrowing());
				return;
			}
		}
		this.playSound(SoundEvents.ENTITY_EGG_THROW	, 1.0F, 1.0F);
		EntityMagicBall acid = (EntityMagicBall) EntityMagicBall.acid(this, target);
		acid.setAttackDamage(2.0F);
		acid.setLPAttack(1.0F, 1);
		WorldHelper.safeSpawn(world, acid);
	}

	private Entity getRandomThrowing(){
		int r = rand.nextInt(2);
		if(r==0){
			this.playSound(SoundEvents.ENTITY_ARROW_SHOOT	, 1.0F, 1.0F);
			EntityArrow arrow = new EntityTippedArrow(world,this);
			arrow.setDamage(3.0F);
			arrow.shoot(this, rotationPitch, rotationYaw, -60F, 1.5F, 0);
			return arrow;
		}else{

			this.playSound(SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP	, 1.0F, 1.0F);
			EntityFlyingAxe axe = new EntityFlyingAxe(world,this);
			axe.setAttackDamage(3.0F);
			axe.setAxeMoveType(AxeMoveType.NORMAL);
			axe.shoot(this, rotationPitch, rotationYaw, 0F, 1.3F, 1.0F);
			return axe;
		}
	}

	@Override
	protected void dealDamage(EntityLivingBase entityIn)
	{
		int i = this.getSlimeSize();

		General[] attackType = {General.PUNCH};
		LPAttribute damage = new LPAttribute(0.8F,1);
		float damageHP = this.getAttackStrength();
		SoundEvent sound = SoundEvents.BLOCK_SLIME_HIT;
		boolean breakGuard = false;
		//緑スライムは四種の攻撃属性をランダムで
		if(this.getSlimeType()==Type.GREEN){
			int r = rand.nextInt(4);
			if(r==0){
				attackType = new General[]{General.SWORD};
				damage = new LPAttribute(1.0F,1);
				sound = SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP;
			}
			if(r==1){
				attackType = new General[]{General.SPEAR};
				damage = new LPAttribute(1.4F,1);
				damageHP *= 0.7F;
				sound = SoundEvents.ENTITY_ARROW_HIT;
			}
			if(r==2){
				attackType = new General[]{General.SWORD,General.PUNCH};
				damage = new LPAttribute(1.2F,1);
				breakGuard = true;
				sound = SoundEvents.BLOCK_ANVIL_PLACE;
			}
		}
		AdditionalDamageData data = new AdditionalDamageData(DamageSource.causeMobDamage(this),damage,attackType);
		if (this.canEntityBeSeen(entityIn) && this.getDistanceSq(entityIn) < 0.6D * (double)i * 0.6D * (double)i && entityIn.attackEntityFrom(DamageHelper.create(data), damageHP))
		{
//			SoundEvent sound = DamageTypeUnsaga.getAttackSound(EnumSet.copyOf(Sets.newHashSet(attackType)));
			this.playSound(sound, 1.0F, (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);


			if(breakGuard && entityIn instanceof EntityPlayer){
				((EntityPlayer)entityIn).disableShield(true);
			}

			this.applyEnchantments(this, entityIn);
		}
	}
	@Override
	public void setSwingingArms(boolean swingingArms) {
		// TODO 自動生成されたメソッド・スタブ

	}

	@Nullable
	@Override
	public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata)
	{

		Type type = HSLibs.randomPick(rand, Lists.newArrayList(Type.values()));
		this.setSlimeType(type);
		return livingdata;
	}

	public static Entity makeRandomColoredSlime(World world){
		EntityTreasureSlimeNew slime = new EntityTreasureSlimeNew(world);
		slime.onInitialSpawn(world.getDifficultyForLocation(new BlockPos(slime)),null);
		return slime;
	}

	@Override
	public int getSlimeSize()
	{
		return 3;
	}
	@Override
	public void writeEntityToNBT(NBTTagCompound compound)
	{
		super.writeEntityToNBT(compound);
		compound.setInteger("slimeType", this.getSlimeType().getMeta());
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound compound)
	{
		super.readEntityFromNBT(compound);
		this.setSlimeType(Type.fromMeta(compound.getInteger("slimeType")));
	}
	@Override
	public boolean isSpellCasting() {
		// TODO 自動生成されたメソッド・スタブ
		return this.spellTicks > 0;
	}
	@Override
	public boolean canCastSpell(Spell spell) {
		if(!this.isPotionActive(UnsagaPotions.LOCK_SLIME)){
			if(spell==SpellRegistry.FIRE_VEIL){
				return !this.isPotionActive(UnsagaPotions.VEIL_FIRE);
			}else
				return true;
		}
		return false;
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

		if(rand.nextInt(2)==0){
			if(!this.isPotionActive(UnsagaPotions.VEIL_FIRE)){
				return SpellRegistry.FIRE_VEIL;
			}
		}
		if(rand.nextInt(10)==0){
			return SpellRegistry.FIRE_STORM;
		}
		return SpellRegistry.FIRE_ARROW;
	}
}
