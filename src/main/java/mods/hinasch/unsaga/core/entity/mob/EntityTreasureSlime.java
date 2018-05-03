//package mods.hinasch.unsaga.core.entity.mob;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.stream.Collectors;
//
//import org.apache.commons.lang3.tuple.Pair;
//
//import com.google.common.base.Function;
//
//import mods.hinasch.lib.util.VecUtil;
//import mods.hinasch.lib.world.WorldHelper;
//import mods.hinasch.unsaga.core.entity.ai.EntityAIArrowAttack;
//import mods.hinasch.unsaga.core.entity.ai.EntityAISpell;
//import mods.hinasch.unsaga.core.entity.ai.EntityAISpell.ISpellAI;
//import mods.hinasch.unsaga.core.entity.ai.EntityAISpell.SpellAIData;
//import mods.hinasch.unsaga.core.entity.ai.EnumRangedAttackSelector;
//import mods.hinasch.unsaga.core.entity.projectile.EntitySolutionLiquid;
//import mods.hinasch.unsaga.core.potion.UnsagaPotions;
//import mods.hinasch.unsagamagic.spell.Spell;
//import mods.hinasch.unsagamagic.spell.SpellRegistry;
//import net.minecraft.enchantment.EnchantmentHelper;
//import net.minecraft.entity.Entity;
//import net.minecraft.entity.EntityLivingBase;
//import net.minecraft.entity.IRangedAttackMob;
//import net.minecraft.entity.SharedMonsterAttributes;
//import net.minecraft.entity.ai.attributes.AttributeModifier;
//import net.minecraft.entity.monster.EntitySlime;
//import net.minecraft.entity.player.EntityPlayer;
//import net.minecraft.entity.projectile.EntityArrow;
//import net.minecraft.entity.projectile.EntityTippedArrow;
//import net.minecraft.init.Enchantments;
//import net.minecraft.init.SoundEvents;
//import net.minecraft.nbt.NBTTagCompound;
//import net.minecraft.util.DamageSource;
//import net.minecraft.util.math.MathHelper;
//import net.minecraft.world.World;
//
//public class EntityTreasureSlime extends EntitySlime implements IRangedAttackMob,ISpellAI{
//
////	private static final DataParameter<String> AI_SPELL = EntityDataManager.<String>createKey(EntityTreasureSlime.class, DataSerializers.STRING);
////	private static final DataParameter<Integer> AI_TARGET = EntityDataManager.<Integer>createKey(EntityTreasureSlime.class, DataSerializers.VARINT);
////	private static final DataParameter<Boolean> REQUIRE_SYNC = EntityDataManager.<Boolean>createKey(EntityTreasureSlime.class, DataSerializers.BOOLEAN);
//	public final List<SpellAIData> spellList;
//	public final List<AttackTypeRangePair> projectileList;
//
//	//	public final String FIREBALL = "fireball";
//	//	public final String LIQUID = "liquid";
//	public int slimeLevel = -1;
//	Spell spell;
//	EntityLivingBase target;
//	boolean isReadyCast = false;
//
//	public EntityTreasureSlime(World worldIn) {
//		super(worldIn);
//		this.setSlimeSize(3,true);
//		this.spellList = new ArrayList();
//		this.projectileList = new ArrayList();
//
//
//
//		this.targetTasks.addTask(1, new EntityAIArrowAttack(this,1.0D,60,15.0F));
//		this.targetTasks.addTask(2, new EntityAISpell(this,this.spellList,1.0D,200,15.0F,10));
//		if(this.slimeLevel==-1){
//			this.slimeLevel = this.rand.nextInt(99)+1;
//		}
//
//		this.buildSpellListFromLevel();
//
//
//		this.experienceValue = 8;
//
//		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).applyModifier(new AttributeModifier("Slime Level Scaled HP",this.slimeLevel/3,0));
//		this.getAttributeMap().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE);
//		this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).applyModifier(new AttributeModifier("Slime Level Scaled Attack Damage",this.slimeLevel/5,0));
//		//		Unsaga.debug(this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).getAttributeValue(),this.getClass());
//		//		Unsaga.debug(this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue(),this.getClass());
//		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.10000000149011612D);
//	}
//
//	public EntityTreasureSlime(World worldIn,int level) {
//		this(worldIn);
//		this.slimeLevel = level;
//	}
//
//
//
//	@Override
//	protected void entityInit()
//	{
//		super.entityInit();
////		this.dataManager.register(AI_SPELL, new String(""));
////		this.dataManager.register(AI_TARGET, Integer.valueOf(-1));
////		this.dataManager.register(REQUIRE_SYNC, Boolean.valueOf(false));
//	}
//	//	public void setRangedList(List<AttackTypeRangePair> list){
//	//		this.rangedList = list;
//	//	}
//	//
//	//	public List<AttackTypeRangePair> getRangedList(){
//	//		return this.rangedList;
//	//	}
//	public void buildSpellListFromLevel(){
//
//
////		SpellRegistry spells = SpellRegistry.instance();
//		this.spellList.add(new SpellAIData(SpellRegistry.PURIFY,120.0D,0.0D,15));
//		if(this.slimeLevel>25){
//			//this.projectileList.add(Pair.of(FIREBALL, 28.0D));
//			this.spellList.add(new SpellAIData(SpellRegistry.FIRE_ARROW,120.0D,0.0D,10));
//			this.spellList.add(new SpellAIData(SpellRegistry.FIRE_VEIL,120.0D,0.0D,10));
//			this.spellList.add(new SpellAIData(SpellRegistry.HEROISM,120.0D,0.0D,20));
//
//		}
//		this.projectileList.add(new AttackTypeRangePair(EnumRangedAttackSelector.LIQUID, 10.0D));
//		this.projectileList.add(new AttackTypeRangePair(EnumRangedAttackSelector.ARROW, 20.0D));
//		if(this.slimeLevel>45){
//			this.spellList.add(new SpellAIData(SpellRegistry.CALL_THUNDER,120.0D,20.0D,20));
//			this.spellList.add(new SpellAIData(SpellRegistry.FIRE_STORM,120.0D,20.0D,45));
//
//		}
//	}
//
//	public List<AttackTypeRangePair> getPossibleRangeList(final double rangeIn){
//		List<AttackTypeRangePair> ranged = new ArrayList();
//		ranged = this.projectileList.stream().filter(input ->input.getRange()>=rangeIn)
//				.collect(Collectors.toList());
//		return ranged;
//	}
//
//	@Override
//    public void onUpdate()
//    {
////    	this.onUpdateSpell(worldObj, this);
//		super.onUpdate();
//
//    }
//	@Override
//	public void attackEntityWithRangedAttack(final EntityLivingBase target,
//			final float f) {
//		if(this.isPotionActive(UnsagaPotions.instance().LOCK_SLIME)){
//			return;
//		}
//
//		final float damage = (float) this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue();
//
//		final List<AttackTypeRangePair> rangedList = this.getPossibleRangeList(this.getDistance(target));
//		if(rangedList.isEmpty()){
//			return;
//		}
//		final EnumRangedAttackSelector sl = new Function<List<AttackTypeRangePair>,EnumRangedAttackSelector>(){
//
//
//			@Override
//			public EnumRangedAttackSelector apply(
//					List<AttackTypeRangePair> input) {
//				if(input.size()<=1){
//					return EnumRangedAttackSelector.LIQUID;
//				}else{
//					return input.get(rand.nextInt(input.size())).getType();
//				}
//			}
//		}.apply(rangedList);
//
//
//
//		Entity throwable = this.getThrowableBySelector(sl, target, damage, f);
//
//
//		if(throwable!=null){
//
//			if(WorldHelper.isServer(world)){
//				this.world.spawnEntity(throwable);
//			}
//		}
//
//
//
//	}
//
//	public Entity getThrowableBySelector(EnumRangedAttackSelector sl,EntityLivingBase target,float damage,float f){
//		//		if(sl==EnumRangedAttackSelector.FIREBALL){
//		//			EntityCustomArrow entityFireArrow = new EntityFireArrow(worldObj, this);
//		//			VecUtil.setThrowableToTarget(this, target, entityFireArrow);
//		//
//		//			entityFireArrow.setDamage((float) ((f * 2.0F) + rand.nextGaussian() * 0.25D + (double)((float)worldObj.getDifficulty().getDifficultyId() * 0.11F)));
//		//
//		//			this.playSound(SoundEvents.ENTITY_ARROW_SHOOT, 1.0F, 1.0F / (this.getRNG().nextFloat() * 0.4F + 0.8F));
//		//			return entityFireArrow;
//		//
//		//		}
//		if(sl==EnumRangedAttackSelector.LIQUID){
//			EntitySolutionLiquid liquid = new EntitySolutionLiquid(world,this);
//			VecUtil.setThrowableToTarget(this, target, liquid);
//			this.playSound(this.getFallSound(0), this.getSoundVolume(), ((rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F) / 0.3F);
//			liquid.setDamage(damage*0.6F, 0);
//			return liquid;
//		}
//
//		if(sl==EnumRangedAttackSelector.ARROW){
//			return this.getArrowObject(target, f);
//		}
//
//		return null;
//	}
//
//	public Entity getArrowObject(EntityLivingBase target, float p_82196_2_)
//	{
//		EntityArrow entityarrow = new EntityTippedArrow(this.world, this);
//		double d0 = target.posX - this.posX;
//		double d1 = target.getEntityBoundingBox().minY + (double)(target.height / 3.0F) - entityarrow.posY;
//		double d2 = target.posZ - this.posZ;
//		double d3 = (double)MathHelper.sqrt(d0 * d0 + d2 * d2);
//		entityarrow.shoot(d0, d1 + d3 * 0.20000000298023224D, d2, 1.6F, (float)(14 - this.world.getDifficulty().getDifficultyId() * 4));
//		int i = EnchantmentHelper.getMaxEnchantmentLevel(Enchantments.POWER, this);
//		int j = EnchantmentHelper.getMaxEnchantmentLevel(Enchantments.PUNCH, this);
//		entityarrow.setDamage((double)(p_82196_2_ * 2.0F) + this.rand.nextGaussian() * 0.25D + (double)((float)this.world.getDifficulty().getDifficultyId() * 0.11F));
//
//		this.playSound(SoundEvents.ENTITY_ARROW_SHOOT, 1.0F, 1.0F / (this.getRNG().nextFloat() * 0.4F + 0.8F));
//		return entityarrow;
//	}
//
//	@Override
//	protected void applyEntityAttributes()
//	{
//		super.applyEntityAttributes();
//		//		this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(7.0F);
//		//		this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(1);
//	}
//
//
//	@Override
//	public boolean canCastSpell(){
//
//		return !this.isPotionActive(UnsagaPotions.instance().LOCK_SLIME);
//	}
//
//	@Override
//	public void setDead()
//	{
//		this.isDead = true;
//	}
//
//	@Override
//	public void onCollideWithPlayer(EntityPlayer par1EntityPlayer)
//	{
//		if (this.canDamagePlayer())
//		{
//			int i = this.getSlimeSize();
//
//			if (this.canEntityBeSeen(par1EntityPlayer) && this.getDistance(par1EntityPlayer) < 0.6D * (double)i * 0.6D * (double)i && par1EntityPlayer.attackEntityFrom(DamageSource.causeMobDamage(this), (float)this.getTreasureSlimeStrength()))
//			{
//				this.playSound(SoundEvents.ENTITY_SLIME_ATTACK, 1.0F, (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
//			}
//		}
//	}
//
//	private float getTreasureSlimeStrength() {
//		float modifier = 1 + (this.slimeLevel /5);
//		return modifier;
//	}
//
//	@Override
//	public void writeEntityToNBT(NBTTagCompound par1NBTTagCompound)
//	{
//		super.writeEntityToNBT(par1NBTTagCompound);
//		par1NBTTagCompound.setInteger("SlimeLevel", this.slimeLevel);
//
//	}
//
//	@Override
//	public void readEntityFromNBT(NBTTagCompound par1NBTTagCompound)
//	{
//		super.readEntityFromNBT(par1NBTTagCompound);
//		this.slimeLevel = par1NBTTagCompound.getInteger("SlimeLevel");
//
//	}
//
//	protected class AttackTypeRangePair{
//
//		final Pair<EnumRangedAttackSelector,Double> value;
//
//		public AttackTypeRangePair(EnumRangedAttackSelector left,double right){
//			this.value = Pair.of(left, right);
//		}
//
//		public Pair<EnumRangedAttackSelector,Double> getValue(){
//			return this.value;
//		}
//
//		public EnumRangedAttackSelector getType(){
//			return this.getValue().getLeft();
//		}
//
//		public double getRange(){
//			return this.getValue().getRight();
//		}
//
//
//
//	}
//
//	@Override
//	public void setSwingingArms(boolean swingingArms) {
//		// TODO 自動生成されたメソッド・スタブ
//
//	}
//
//
//
//}
