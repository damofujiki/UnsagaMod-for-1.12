package mods.hinasch.unsaga.core.item.weapon;

import java.util.List;
import java.util.Optional;

import javax.annotation.Nullable;

import mods.hinasch.lib.container.inventory.InventoryHandler;
import mods.hinasch.lib.item.ItemUtil;
import mods.hinasch.lib.world.WorldHelper;
import mods.hinasch.unsaga.UnsagaMod;
import mods.hinasch.unsaga.ability.AbilityAPI;
import mods.hinasch.unsaga.ability.IAbilitySelector;
import mods.hinasch.unsaga.ability.specialmove.Tech;
import mods.hinasch.unsaga.ability.specialmove.TechInvoker;
import mods.hinasch.unsaga.ability.specialmove.TechInvoker.InvokeType;
import mods.hinasch.unsaga.common.tool.ComponentUnsagaWeapon;
import mods.hinasch.unsaga.common.tool.UnsagaToolUtil;
import mods.hinasch.unsaga.core.client.event.UnsagaTooltips;
import mods.hinasch.unsaga.material.IUnsagaMaterialSelector;
import mods.hinasch.unsaga.util.ToolCategory;
import mods.hinasch.unsagamagic.enchant.UnsagaEnchantmentRegistry;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemArrow;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemBowUnsaga extends ItemBow implements IItemColor,IAbilitySelector,IUnsagaMaterialSelector{


	ComponentUnsagaWeapon component;
	public ItemBowUnsaga(){

		super();
		this.component = new ComponentUnsagaWeapon(ToolCategory.BOW);
		this.setCreativeTab(CreativeTabs.COMBAT);
		this.component.addPropertyOverrides(this);
	}

	@Override
	public int colorMultiplier(ItemStack stack, int tintIndex) {
		return this.component.getColorFromItemstack(stack, tintIndex);
	}

	@Override
	public ComponentUnsagaWeapon getComponent(){
		return this.component;
	}
	@Override
	@SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
    {
		this.getComponent().addInformations(stack, tooltip, flagIn, UnsagaTooltips.Type.BOW);
	}

    private ItemStack findAmmo(EntityPlayer player)
    {
        if (this.isArrow(player.getHeldItem(EnumHand.OFF_HAND)))
        {
            return player.getHeldItem(EnumHand.OFF_HAND);
        }
        else if (this.isArrow(player.getHeldItem(EnumHand.MAIN_HAND)))
        {
            return player.getHeldItem(EnumHand.MAIN_HAND);
        }
        else
        {
        	InventoryHandler inv = new InventoryHandler(player.inventory);
        	Optional<ItemStack> arrow = inv.toStream(0,player.inventory.getSizeInventory())
        			.map(in -> in.getStack()).filter(is -> ItemUtil.isItemStackPresent(is) && this.isArrow(is)).findFirst();
            return arrow.isPresent() ? arrow.get() : null;
        }
    }

    public static float getDamageModifier(ItemStack is){

    	//エンチャントを付与
        int j = EnchantmentHelper.getEnchantmentLevel(Enchantments.POWER, is);
        j += EnchantmentHelper.getEnchantmentLevel(UnsagaEnchantmentRegistry.WEAPON_BLESS.getEnchantment(), is);
        j += EnchantmentHelper.getEnchantmentLevel(UnsagaEnchantmentRegistry.SHARPNESS.getEnchantment(), is);
        double d = 0;
        if (j > 0)
        {
        	d = (double)j * 0.5D + 0.5D;

        }

        //最後に素材による追加攻撃力を付与
        d += UnsagaToolUtil.getMaterial(is).getBowModifier();
    	return (float)d;
    }
	@Override
	public int getItemEnchantability(ItemStack is)
	{
		return UnsagaToolUtil.getItemEnchantability(is);
	}

	@Override
	public int getMaxDamage(ItemStack stack)
	{
		return (int)((float)this.component.getMaxDamage(stack) * 0.8F);
	}

	@Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items)
    {
		if(!this.isInCreativeTab(tab)){
			return;
		}
		this.component.getSubItems(this, tab, items);
	}

	@Override
	public String getUnlocalizedName(ItemStack par1ItemStack)
	{
		return this.component.getUnlocalizedName(par1ItemStack);
	}

//	@Override
//	public net.minecraftforge.common.capabilities.ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt)
//	{
//		return this.component.initCapabilities(stack, nbt);
//	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean isFull3D()
	{
		return true;
	}

	protected boolean hasSpecialMoveType(ItemStack is,InvokeType type){
		if(AbilityAPI.getLearnedSpecialMove(is).isPresent()){
			if(AbilityAPI.getLearnedSpecialMove(is).get().getAction().getInvokeTypes().contains(type)){
				return true;
			}
		}
		return false;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand hand)
	{
		ItemStack itemStackIn = playerIn.getHeldItem(hand);
		if(this.hasSpecialMoveType(itemStackIn, InvokeType.RIGHTCLICK) && playerIn.isSneaking()){
			Tech move = AbilityAPI.getLearnedSpecialMove(itemStackIn).get();
			TechInvoker invoker = new TechInvoker(worldIn, playerIn, move);
			invoker.setArtifact(itemStackIn);
			invoker.setInvokeType(InvokeType.RIGHTCLICK);
			invoker.invoke();
			return new ActionResult(EnumActionResult.SUCCESS, itemStackIn);
		}
		return super.onItemRightClick(worldIn, playerIn, hand);
	}

	public static void setArrowProperties(EntityLivingBase entityplayer,ItemStack bowStack,EntityArrow entityarrow,float charge,boolean isInfinity){
        if (charge == 1.0F)
        {
            entityarrow.setIsCritical(true);
        }


        entityarrow.setDamage(entityarrow.getDamage() + getDamageModifier(bowStack));

        int k = EnchantmentHelper.getEnchantmentLevel(Enchantments.PUNCH, bowStack);

        if (k > 0)
        {
            entityarrow.setKnockbackStrength(k);
        }

        if (EnchantmentHelper.getEnchantmentLevel(Enchantments.FLAME, bowStack) > 0)
        {
            entityarrow.setFire(100);
        }

        bowStack.damageItem(1, entityplayer);

        if (isInfinity)
        {
            entityarrow.pickupStatus = EntityArrow.PickupStatus.CREATIVE_ONLY;
        }
	}
	@Override
    public void onPlayerStoppedUsing(ItemStack bowStack, World worldIn, EntityLivingBase entityLiving, int timeLeft)
    {
        if (entityLiving instanceof EntityPlayer)
        {
            EntityPlayer entityplayer = (EntityPlayer)entityLiving;
            boolean isInfinite = entityplayer.capabilities.isCreativeMode || EnchantmentHelper.getEnchantmentLevel(Enchantments.INFINITY, bowStack) > 0;
            ItemStack arrowStack = this.findAmmo(entityplayer);
            UnsagaMod.logger.trace("aarrow", timeLeft);
            int i = this.getMaxItemUseDuration(bowStack) - timeLeft;
            i = net.minecraftforge.event.ForgeEventFactory.onArrowLoose(bowStack, worldIn, (EntityPlayer)entityLiving, i, arrowStack != null || isInfinite);
            if (i < 0) return;

            if (arrowStack != null || isInfinite)
            {
                if (arrowStack == null)
                {
                    arrowStack = new ItemStack(Items.ARROW);
                }

                float f = getArrowVelocity(i);

                if ((double)f >= 0.1D)
                {
                    boolean infiniteFlag = entityplayer.capabilities.isCreativeMode || (arrowStack.getItem() instanceof ItemArrow ? ((ItemArrow)arrowStack.getItem()).isInfinite(arrowStack, bowStack, entityplayer) : false);

//                    if (WorldHelper.isServer(worldIn))
//                    {
                        ItemArrow itemarrow = (ItemArrow)((ItemArrow)(arrowStack.getItem() instanceof ItemArrow ? arrowStack.getItem() : Items.ARROW));
                        EntityArrow entityarrow = itemarrow.createArrow(worldIn, arrowStack, entityplayer);
                        entityarrow.shoot(entityplayer, entityplayer.rotationPitch, entityplayer.rotationYaw, 0.0F, f * 3.0F, 1.0F);

                        //エンチャント等
                        this.setArrowProperties(entityplayer, bowStack, entityarrow, f, infiniteFlag);

                        //スニーク＆必殺があるなら矢のスポーンを取り消して技動作へ切り替え
                		if(this.hasSpecialMoveType(bowStack, InvokeType.BOW) && entityLiving.isSneaking()){
                			Tech move = AbilityAPI.getLearnedSpecialMove(bowStack).get();
                			TechInvoker invoker = new TechInvoker(worldIn, entityLiving, move);
                			invoker.setArtifact(bowStack);
                			invoker.setArrowComponent(entityarrow,arrowStack);
                			invoker.setChargedTime((int) (f*100));
                			invoker.setInvokeType(InvokeType.BOW);
                			invoker.invoke();
                		}else{
                			if(WorldHelper.isServer(worldIn)){
                				worldIn.spawnEntity(entityarrow);
                			}

                		}

//                    }

                    worldIn.playSound((EntityPlayer)null, entityplayer.posX, entityplayer.posY, entityplayer.posZ, SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.NEUTRAL, 1.0F, 1.0F / (itemRand.nextFloat() * 0.4F + 1.2F) + f * 0.5F);

                    processDecreaseArrow(entityplayer, arrowStack, infiniteFlag);

                    entityplayer.addStat(StatList.getObjectUseStats(this));
                }
            }
        }
    }

	public static void processDecreaseArrow(EntityPlayer entityplayer,ItemStack arrowStack,boolean flag1){
        if (!flag1)
        {
           arrowStack.shrink(1);

            if (arrowStack.isEmpty())
            {
                entityplayer.inventory.deleteStack(arrowStack);
            }
        }
	}
	@Override
	public int getMaxAbilitySize(){
		return 4;
	}

	@Override
	public ToolCategory getCategory() {
		// TODO 自動生成されたメソッド・スタブ
		return ToolCategory.BOW;
	}
}
