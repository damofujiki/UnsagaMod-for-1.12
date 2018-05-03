package mods.hinasch.unsaga.ability.specialmove;

import java.util.Optional;

import mods.hinasch.lib.container.inventory.InventoryHandler;
import mods.hinasch.lib.container.inventory.InventoryHandler.InventoryStatus;
import mods.hinasch.lib.iface.IExtendedReach;
import mods.hinasch.lib.item.ItemUtil;
import mods.hinasch.lib.util.HSLibs;
import mods.hinasch.lib.world.WorldHelper;
import mods.hinasch.lib.world.XYZPos;
import mods.hinasch.unsaga.ability.AbilityCapability;
import mods.hinasch.unsaga.ability.specialmove.action.TechActionBase;
import mods.hinasch.unsaga.ability.specialmove.action.TechArrow;
import mods.hinasch.unsaga.common.specialaction.ActionPerformerBase;
import mods.hinasch.unsaga.core.item.weapon.ItemBowUnsaga;
import mods.hinasch.unsaga.core.potion.UnsagaPotions;
import mods.hinasch.unsaga.damage.DamageComponent;
import mods.hinasch.unsaga.init.UnsagaItems;
import mods.hinasch.unsaga.skillpanel.SkillPanelAPI;
import mods.hinasch.unsaga.skillpanel.SkillPanels;
import mods.hinasch.unsaga.util.LivingHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class TechInvoker extends ActionPerformerBase<Tech,TechActionBase>{

	public static enum InvokeType{
		NONE,USE,CHARGE,RIGHTCLICK,BOW,SPRINT_RIGHTCLICK;
	}

	int usingTime = 0;
	InvokeType invokeType = InvokeType.NONE;
	Optional<ComponentArrow> arrowEntity = Optional.empty();
	public TechInvoker(World world, EntityLivingBase performer,Tech move) {
		super(world, performer,move);
		// TODO 自動生成されたコンストラクター・スタブ
	}

	public InvokeType getInvokeType(){
		return this.invokeType;
	}

	public void setArrowComponent(EntityArrow arrow,ItemStack arrowStack){
		this.arrowEntity = Optional.of(new ComponentArrow(arrowStack,arrow));
	}

	public Optional<ComponentArrow> getArrowComponent(){
		return this.arrowEntity;
	}
	public void swingMainHand(boolean swingSound,boolean sweepParticle){
		this.getPerformer().swingArm(EnumHand.MAIN_HAND);
		if(swingSound){
			this.playSound(XYZPos.createFrom(getPerformer()), SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, false);

		}
		if(sweepParticle){
			this.spawnSweepParticle();
		}
	}
	public void spawnSweepParticle(){
		if(this.getPerformer() instanceof EntityPlayer){
			((EntityPlayer)this.getPerformer()).spawnSweepParticles();
		}
	}
	public TechInvoker setInvokeType(InvokeType type){
		this.invokeType = type;
		return this;
	}

	public float getReach(){
		if(this.article.isPresent()){
			if(this.article.get().getItem() instanceof IExtendedReach){
				return ((IExtendedReach)this.article.get().getItem()).getReach();
			}
		}
		return 4.0F;
	}

	public int getChargedTime(){
		return this.usingTime;
	}

	public DamageComponent getActionStrength(){
		return this.getActionProperty().getStrength();
	}
	public TechInvoker setChargedTime(int time){
		this.usingTime = time;
		return this;
	}
	@Override
	public TechActionBase getAction() {
		// TODO 自動生成されたメソッド・スタブ
		return TechRegistry.instance().getAssociatedAction(getActionProperty());
	}


	public void invoke(){
		if(this.canInvoke()){
			if(LivingHelper.ADAPTER.hasCapability(getPerformer())){ //技発動中状態にする
				LivingHelper.ADAPTER.getCapability(getPerformer()).addState(new PotionEffect(UnsagaPotions.ACTION_PROGRESS,3000,0));
			}
			//			this.getAction().getPrePerform().accept(this);
			EnumActionResult result = this.getAction().perform(this);

			if(result!=EnumActionResult.PASS){
				//クールタイムの付与
				if(this.getActionProperty().getCoolingTime().isPresent()){
					int cooling = this.getActionProperty().getCoolingTime().getAsInt();
					this.getPerformer().addPotionEffect(new PotionEffect(UnsagaPotions.instance().COOLDOWN,ItemUtil.getPotionTime(cooling),0));
				}
				this.consumeCost();
				if(WorldHelper.isServer(getWorld()) && this.getWorld().rand.nextFloat()<=0.2F){
					this.masterTech();
				}
			}
		}

		if(LivingHelper.ADAPTER.hasCapability(getPerformer())){
			LivingHelper.ADAPTER.getCapability(getPerformer()).removeState(UnsagaPotions.ACTION_PROGRESS);
		}
	}

	public float getInvokerStrength(){
		if(this.getAction() instanceof TechArrow && this.getArtifact().isPresent()){
			return ItemBowUnsaga.getDamageModifier(this.getArtifact().get());
		}
		if(this.getPerformer().getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE)!=null){
			return (float) this.getPerformer().getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue();
		}
		return 1.0F;
	}

	/**
	 * 攻撃者の攻撃力＋技の攻撃力を足した値を返す。
	 * {@link #getStrength()}から参照される
	 * @return
	 */
	public float getStrengthHP(){
		float at = this.getInvokerStrength() + this.getActionProperty().getStrength().hp();
		return MathHelper.clamp(at, 0.1F, at);
	}
	public boolean canInvoke(){
		//
		//		if(this.getTargetType()==TargetType.TARGET){
		//			if(TargetHolderCapability.adapter.hasCapability(getPerformer())){
		//				if(!TargetHolderCapability.adapter.getCapability(getPerformer()).getTarget().isPresent()){
		//					return false;
		//				}
		//			}
		//		}
		if(!this.getPerformer().isPotionActive(UnsagaPotions.instance().COOLDOWN)){
			if(this.getPerformer() instanceof EntityPlayer){
				return this.checkItemCost();
			}

		}

		return false;
	}

	public float getCostReduction(){
		if(SkillPanelAPI.hasPanel(getPerformer(), SkillPanels.WEAPON_MASTER)){
			return 0.13F * SkillPanelAPI.getHighestPanelLevel(getPerformer(), SkillPanels.WEAPON_MASTER).getAsInt();
		}
		return 0.0F;
	}

	@Override
	public int getCost(){
		int cost = this.getActionProperty().getCost();
		cost  -= (int) ((float)this.getActionProperty().getCost() * this.getCostReduction());
		return MathHelper.clamp(cost, 1, cost);
	}

	private void masterTech(){
		if(this.getPerformer() instanceof EntityPlayer){
			EntityPlayer ep = (EntityPlayer) this.getPerformer();
			if(SkillPanelAPI.hasPanel(ep, SkillPanels.WEAPON_MASTER)){
				Optional<InventoryStatus> opt = InventoryHandler.create(ep.inventory).toStream(0, ep.inventory.getSizeInventory())
						.filter(in -> ItemUtil.isItemStackPresent(in.getStack())).filter(in -> in.getStack().getItem()==UnsagaItems.WAZA_BOOK)
						.filter(in -> AbilityCapability.adapter.getCapability(in.getStack()).isAbilityEmpty()).findFirst();
				if(opt.isPresent()){
					ItemStack book = opt.get().getStack();
					AbilityCapability.adapter.getCapability(book).setAbility(0,this.getActionProperty());
					this.playSound(XYZPos.createFrom(ep), SoundEvents.BLOCK_ANVIL_PLACE, false);
					this.broadCastMessage(HSLibs.translateKey("msg.mastered.specialMove",this.getActionProperty().getLocalized()));

				}
			}

		}
	}
	private void consumeCost(){
		if(this.getPerformer() instanceof EntityPlayer){
			if(this.getArtifact().isPresent()){
				this.getArtifact().get().damageItem(this.getCost(), this.getPerformer());
			}else{
				EntityPlayer ep = (EntityPlayer) this.getPerformer();
				float foodCost = (float)this.getCost() * 0.01F * 2.0F;
				if(ep.getFoodStats().getFoodLevel()>=1){
					ep.getFoodStats().addExhaustion(foodCost);
				}else{
					ep.attackEntityFrom(DamageSource.STARVE, foodCost * 10.0F);
				}
			}
		}

	}


	@Override
	public DamageComponent getStrength() {
		// TODO 自動生成されたメソッド・スタブ
		return DamageComponent.of(this.getStrengthHP(), this.getActionStrength().lp());
	}

	public static class ComponentArrow{
		public final ItemStack arrowStack;
		public final EntityArrow arrowEntity;

		public ComponentArrow(ItemStack stack,EntityArrow e){
			this.arrowEntity = e;
			this.arrowStack = stack;
		}
	}
}
