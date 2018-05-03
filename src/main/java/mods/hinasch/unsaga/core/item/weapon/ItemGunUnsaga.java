package mods.hinasch.unsaga.core.item.weapon;


import java.util.OptionalInt;

import com.google.common.base.Predicate;

import mods.hinasch.lib.capability.CapabilityAdapterFactory.CapabilityAdapterPlanImpl;
import mods.hinasch.lib.capability.CapabilityAdapterFrame;
import mods.hinasch.lib.capability.CapabilityStorage;
import mods.hinasch.lib.capability.ComponentCapabilityAdapters;
import mods.hinasch.lib.iface.IIntSerializable;
import mods.hinasch.lib.network.PacketSound;
import mods.hinasch.lib.network.PacketUtil;
import mods.hinasch.lib.network.SoundPacket;
import mods.hinasch.lib.util.HSLibs;
import mods.hinasch.lib.world.XYZPos;
import mods.hinasch.unsaga.UnsagaMod;
import mods.hinasch.unsaga.core.entity.projectile.EntityBullet;
import mods.hinasch.unsaga.init.UnsagaItems;
import mods.hinasch.unsaga.skillpanel.SkillPanelAPI;
import mods.hinasch.unsaga.skillpanel.SkillPanels;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.event.AttachCapabilitiesEvent;


public class ItemGunUnsaga extends ItemBow {

	private String iconname;

	@CapabilityInject(IMusket.class)
	public static Capability<IMusket> CAPA;
	//MMM氏のFN5728Gunsをもとに
	//	protected final int RELOAD_END = 10;
	//	protected final int RELOAD_START = 8;
	//	protected final int FIRE = 0x0000;

	//	protected MaterialAnalyzer info;

	public ItemGunUnsaga() {
		super();
		//		this.iconname = par2;
		this.setMaxDamage(384);
		//ClientHelper.registerModelMeser(this, 0, Unsaga.modelHelper.getNewModelResource("musket", "inventory"));
		//Unsaga.proxy.registerItemRenderer(this,new RenderItemMusket());
	}





	@Override
	public void onPlayerStoppedUsing(ItemStack par1ItemStack, World par2World, EntityLivingBase par3EntityPlayer, int par4)
	{

		//		Unsaga.debug("playerstopped",this.getClass());
		if(par1ItemStack.isEmpty()){
			return;
		}


		if(this.getReload(par1ItemStack)==Status.RELOAD_START){

			this.setReload(par1ItemStack, Status.RELOAD_END);
			par3EntityPlayer.playSound(SoundEvents.ITEM_FLINTANDSTEEL_USE, 1.0F, 1.0F / (par3EntityPlayer.getRNG().nextFloat() * 0.4F + 1.2F) * 0.5F);

			return;
		}
		if(this.getReload(par1ItemStack).getMeta()<Status.RELOAD_END.getMeta()){
			return;
		}



		int j = this.getMaxItemUseDuration(par1ItemStack) - par4;

		boolean flag = true;

		if (flag)
		{
			float f = (float)j / 20.0F;
			f = (f * f + f * 2.0F) / 3.0F;

			if ((double)f < 0.1D)
			{
				return;
			}

			if (f > 1.0F)
			{
				f = 1.0F;
			}

			SoundPacket.sendToAllAround(PacketSound.atPos(SoundEvents.ENTITY_GENERIC_EXPLODE, XYZPos.createFrom(par3EntityPlayer)), PacketUtil.getTargetPointNear(par3EntityPlayer));
//			par3EntityPlayer.playSound(SoundEvents.entity_generic_explode, 1.0F, 1.0F / (itemRand.nextFloat() * 0.4F + 1.2F) + f * 0.5F);
			EntityBullet entityarrow = new EntityBullet(par2World, par3EntityPlayer, 1.0F * 2.0F);

			if (f == 1.0F)
			{
				entityarrow.setIsCritical(true);
			}

			int k = EnchantmentHelper.getEnchantmentLevel(Enchantments.POWER, par1ItemStack);

			if (k > 0)
			{
				OptionalInt skill = SkillPanelAPI.getHighestPanelLevel(par3EntityPlayer, SkillPanels.GUN);
				entityarrow.setDamage(entityarrow.getDamage() + (double)k * 0.5D + 1.0D * (skill.isPresent() ? 0 : skill.getAsInt()));
			}

			int l = EnchantmentHelper.getEnchantmentLevel(Enchantments.PUNCH, par1ItemStack);

			if (l > 0)
			{
				entityarrow.setKnockbackStrength(l);
			}

			if (EnchantmentHelper.getEnchantmentLevel(Enchantments.FLAME, par1ItemStack) > 0)
			{
				entityarrow.setFire(100);
			}

			par1ItemStack.damageItem(1, par3EntityPlayer);
			//			par3EntityPlayer.playSound(SoundEvents.entity_generic_explode, 1.0F, 1.0F / (par3EntityPlayer.getRNG().nextFloat() * 0.4F + 1.2F) * 0.5F);


			if (flag)
			{
				entityarrow.canBePickedUp = 2;
			}
			else
			{
				//				par3EntityPlayer.inventory.consumeInventoryItem(Items.arrow);
			}

			if (!par2World.isRemote)
			{
				par2World.spawnEntity(entityarrow);
			}
			this.setReload(par1ItemStack, Status.FIRE);

			par3EntityPlayer.rotationPitch += (itemRand.nextFloat() * -3F) * 1.0F;
		}
	}


	//	public void setBarrett(ItemStack is){
	//		UtilSkill.setStateNBT(is, RELOAD_END);
	//	}
	//
	//	public void setReady(ItemStack is){
	//		UtilSkill.setStateNBT(is, FIRE);
	//	}

	public boolean isWeaponReload(ItemStack itemstack, EntityPlayer entityplayer){

		cancelReload(itemstack,Status.RELOAD_END);


		return canReload(itemstack,entityplayer);
	}

	//	public boolean hasSetBarrett(ItemStack is){
	//		return UtilSkill.readStateNBT(is)!=0;
	//	}
	//
	//	public boolean isReadyToFire(ItemStack is){
	//		return UtilSkill.readStateNBT(is)==this.FIRE;
	//	}
	//
	//	public void setFinish(ItemStack is){
	//		UtilSkill.setStateNBT(is, 0);
	//	}


	public Status getReload(ItemStack is){

		if(adapter.hasCapability(is)){
			return adapter.getCapability(is).getReloadStatus();
		}
		return null;
	}

	protected void setReload(ItemStack is,Status val){
		//		Unsaga.debug(val);
		if(adapter.hasCapability(is)){
			adapter.getCapability(is).setReloadStatus(val);
		}
	}

	protected void cancelReload(ItemStack itemstack, Status force) {
		if (getReload(itemstack).getMeta() >= force.getMeta()) {
			setReload(itemstack, Status.FIRE);
		}
	}

	@Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
    {
		ItemStack itemStackIn = playerIn.getHeldItem(handIn);
		//		Unsaga.debug("itemclick",this.getClass());
		//		if(HSLibs.isEntityLittleMaidAvatar(par3EntityPlayer)){
		//			par3EntityPlayer.setItemInUse(par1ItemStack, this.getMaxItemUseDuration(par1ItemStack));
		//			return par1ItemStack;
		//		}
		if(this.getReload(itemStackIn)==Status.FIRE && !this.canReload(itemStackIn, playerIn)){
			return new ActionResult(EnumActionResult.FAIL,itemStackIn);
		}
		//
		//		if(this.getReload(par1ItemStack)==RELOAD_START){
		//			this.setReload(par1ItemStack, RELOAD_END);
		//			par3EntityPlayer.worldObj.playSoundAtEntity(par3EntityPlayer, "fire.ignite", 1.0F, 1.0F / (par3EntityPlayer.getRNG().nextFloat() * 0.4F + 1.2F) * 0.5F);
		//			return par1ItemStack;
		//		}

		//		int li = this.getReload(par1ItemStack);
		//		if(li <= FIRE){
		//			if(this.canReload(par1ItemStack, par3EntityPlayer)){
		//
		//			}
		//		}
		//		ArrowNockEvent event = new ArrowNockEvent(par3EntityPlayer, par1ItemStack);
		//		MinecraftForge.EVENT_BUS.post(event);
		//		if (event.isCanceled())
		//		{
		//			return event.result;
		//		}
		//
		//		if (par3EntityPlayer.capabilities.isCreativeMode || par3EntityPlayer.inventory.hasItem(InitUnsagaTools.itemBarrett.itemID))
		//		{
		//			par3EntityPlayer.setItemInUse(par1ItemStack, this.getMaxItemUseDuration(par1ItemStack));
		//		}
		playerIn.setActiveHand(EnumHand.MAIN_HAND);
		return new ActionResult(EnumActionResult.SUCCESS,itemStackIn);
	}
	//
	//	@Override
	//	public void onUsingItemTick(ItemStack stack, EntityPlayer player, int count)
	//	{
	//
	//
	//		if(stack!=){
	//			//System.out.println(count);
	//			boolean flag = player.capabilities.isCreativeMode || EnchantmentHelper.getEnchantmentLevel(Enchantment.infinity.effectId, stack) > 0
	//					|| player.inventory.hasItem(InitUnsagaTools.itemBarrett.itemID);
	//
	//			if(!this.hasSetBarrett(stack) && this.getMaxItemUseDuration(stack)-count>=20 && flag){
	//				if(!player.capabilities.isCreativeMode  || EnchantmentHelper.getEnchantmentLevel(Enchantment.infinity.effectId, stack) > 0){
	//					player.inventory.consumeInventoryItem(InitUnsagaTools.itemBarrett.itemID);
	//				}
	//				this.setBarrett(stack);
	//				player.worldObj.playSoundAtEntity(player, "fire.ignite", 1.0F, 1.0F / (player.getRNG().nextFloat() * 0.4F + 1.2F) * 0.5F);
	//
	//
	//			}
	//		}
	//	}

	protected void reloadBarrett(ItemStack itemstack, World world, EntityLivingBase entityplayer){
		boolean isCreative = false;
		if(entityplayer instanceof EntityPlayer){
			isCreative = ((EntityPlayer) entityplayer).capabilities.isCreativeMode;
		}
		if(!world.isRemote){
			if (entityplayer == null || isCreative) {
				itemstack.setItemDamage(0);
			} else {
				boolean linfinity = EnchantmentHelper.getEnchantmentLevel(Enchantments.INFINITY, itemstack) > 0;
				Status lk = getReload(itemstack);
				if(!linfinity && entityplayer instanceof EntityPlayer){

					((EntityPlayer)entityplayer).inventory.clearMatchingItems(UnsagaItems.AMMO,-1,1,null);
				}
				itemstack.setItemDamage(itemstack.getItemDamage() - 1);
			}
			this.setReload(itemstack, Status.RELOAD_START);
		}
		entityplayer.playSound(SoundEvents.ITEM_FLINTANDSTEEL_USE, 1.0F, 1.0F / (entityplayer.getRNG().nextFloat() * 0.4F + 1.2F) * 0.5F);
	}

	@Override
	public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityLivingBase entityLiving)
	{
		// リロード完了


		if(this.getReload(stack).getMeta()<=Status.FIRE.getMeta()){
			this.reloadBarrett(stack, worldIn, entityLiving);
		}
		return stack;
	}



	@Override
	public int getMaxItemUseDuration(ItemStack par1ItemStack)
	{
		return this.getReload(par1ItemStack)==Status.FIRE ? 32 : super.getMaxItemUseDuration(par1ItemStack);
	}

	//	@Override
	//    public EnumAction getItemUseAction(ItemStack par1ItemStack)
	//    {
	//        return getReload(par1ItemStack)==RELOAD_START ? EnumAction.none : EnumAction.bow;
	//    }

	protected boolean canReload(ItemStack is,EntityPlayer ep){
		if (ep.capabilities.isCreativeMode) return true;
		if(ep.inventory.hasItemStack(new ItemStack(UnsagaItems.AMMO))) return true;
		return false;
	}

	@Override
	public void onUpdate(ItemStack par1ItemStack, World par2World, Entity par3Entity, int par4, boolean par5) {
		if(par1ItemStack.isEmpty())return;

		//		if(par3Entity instanceof EntityLivingBase && HSLibs.isEntityLittleMaidAvatar((EntityLivingBase) par3Entity)){
		//			if(ModuleLMM.getLMMTarget((EntityLivingBase) par3Entity)!=null){
		//				EntityLivingBase target = ModuleLMM.getLMMTarget((EntityLivingBase) par3Entity);
		//				Unsaga.debug(this.getReload(par1ItemStack));
		//				if(this.getReload(par1ItemStack)==8){
		//					this.onPlayerStoppedUsing(par1ItemStack, par3Entity.worldObj, (EntityPlayer) par3Entity, 1000);
		//				}
		//			}
		//		}
		//				if(par3Entity instanceof EntityLivingBase){
		//					EntityPlayer ep = (EntityPlayer)par3Entity;
		//					if(ep.getHeldItem()!=null){
		//						if(ep.getHeldItem().getItem().itemID==this.itemID){
		//							ItemStack is = ep.getHeldItem();
		//							//ニュートラル時
		//							if(this.getReload(is)==RELOAD_START){
		//								this.setReload(is,RELOAD_END);
		//								ep.worldObj.playSoundAtEntity(ep, "fire.ignite", 1.0F, 1.0F / (ep.getRNG().nextFloat() * 0.4F + 1.2F) * 0.5F);
		//
		//							}
		//
		//						}
		//					}
		//
		//				}

	}

	//	@Override
	//	public ToolCategory getCategory() {
	//		// TODO 自動生成されたメソッド・スタブ
	//		return ToolCategory.GUN;
	//	}
	//
	//
	//	@Override
	//	public UnsagaMaterial getMaterial() {
	//		// TODO 自動生成されたメソッド・スタブ
	//		return Unsaga.materials.iron;
	//	}

	public static enum Status implements IIntSerializable{
		RELOAD_START(8),RELOAD_END(10),FIRE(0);

		int meta;
		public static Status fromMeta(int meta){
			return HSLibs.fromMeta(Status.values(), meta);
		}
		private Status(int par1){
			meta = par1;
		}
		@Override
		public int getMeta() {
			// TODO 自動生成されたメソッド・スタブ
			return meta;
		}
	}
	public static interface IMusket{
		public void setReloadStatus(Status st);
		public Status getReloadStatus();
	}

	public static class DefaultImpl implements IMusket{

		Status status = Status.FIRE;
		@Override
		public void setReloadStatus(Status st) {
			// TODO 自動生成されたメソッド・スタブ
			status = st;
		}

		@Override
		public Status getReloadStatus() {
			// TODO 自動生成されたメソッド・スタブ
			return status;
		}

	}

	public static class Storage extends CapabilityStorage<IMusket>{

		@Override
		public void writeNBT(NBTTagCompound comp, Capability<IMusket> capability, IMusket instance,
				EnumFacing side) {
			comp.setInteger("status", instance.getReloadStatus().getMeta());
		}

		@Override
		public void readNBT(NBTTagCompound comp, Capability<IMusket> capability, IMusket instance, EnumFacing side) {
			// TODO 自動生成されたメソッド・スタブ
			instance.setReloadStatus(Status.fromMeta(comp.getInteger("status")));
		}



	}

	public static Predicate<AttachCapabilitiesEvent> predicate = ev -> HSLibs.cast(ev, in -> in.getItem() instanceof ItemGunUnsaga);

	public static CapabilityAdapterFrame<IMusket> adapterBase = UnsagaMod.capabilityAdapterFactory.create(new CapabilityAdapterPlanImpl(()->CAPA,()->IMusket.class,()->DefaultImpl.class,Storage::new));
	public static ComponentCapabilityAdapters.ItemStack<IMusket> adapter = (ComponentCapabilityAdapters.ItemStack<IMusket>) adapterBase.createChildItem("musket")
			.setRequireSerialize(true).setPredicate(predicate);
}
