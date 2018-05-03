package mods.hinasch.unsaga.core.item.weapon;

import java.util.List;

import javax.annotation.Nullable;

import com.google.common.collect.Sets;

import mods.hinasch.unsaga.common.tool.ItemToolUnsaga;
import mods.hinasch.unsaga.core.client.event.UnsagaTooltips;
import mods.hinasch.unsaga.util.ToolCategory;
import net.minecraft.block.BlockDispenser;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumAction;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemShieldUnsaga extends ItemToolUnsaga{

	public ItemShieldUnsaga() {
		super(0, -2.8F, Sets.newHashSet(),ToolCategory.SHIELD);


        this.addPropertyOverride(new ResourceLocation("blocking"), new IItemPropertyGetter()
        {
            @SideOnly(Side.CLIENT)
            public float apply(ItemStack stack, @Nullable World worldIn, @Nullable EntityLivingBase entityIn)
            {
                return entityIn != null && entityIn.isHandActive() && entityIn.getActiveItemStack() == stack ? 1.0F : 0.0F;
            }
        });

        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(this, ItemArmor.DISPENSER_BEHAVIOR);
	}
	@Override
	@SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
    {
		this.getComponent().addInformations(stack, tooltip, flagIn,UnsagaTooltips.Type.SHIELD);
	}
    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        return super.onItemUse(player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
    }

    @Override
    public boolean isShield(ItemStack stack, @Nullable EntityLivingBase entity)
    {
        return true;
    }
    @Override
    public EnumAction getItemUseAction(ItemStack stack)
    {
        return EnumAction.BLOCK;
    }

	@Override
	public ToolCategory getCategory() {
		// TODO 自動生成されたメソッド・スタブ
		return ToolCategory.SHIELD;
	}


    @Override
    public int getMaxItemUseDuration(ItemStack stack)
    {
        return 72000;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
    {
    	ItemStack itemStackIn = playerIn.getHeldItem(handIn);
        playerIn.setActiveHand(handIn);
        return new ActionResult(EnumActionResult.SUCCESS, itemStackIn);
    }

    public static boolean canBlockDamageSource(EntityPlayer ep,DamageSource damageSourceIn)
    {
        if (!damageSourceIn.isUnblockable() && ep.isActiveItemStackBlocking())
        {
            Vec3d vec3d = damageSourceIn.getDamageLocation();

            if (vec3d != null)
            {
                Vec3d vec3d1 = ep.getLook(1.0F);
                Vec3d vec3d2 = vec3d.subtractReverse(new Vec3d(ep.posX, ep.posY, ep.posZ)).normalize();
                vec3d2 = new Vec3d(vec3d2.x, 0.0D, vec3d2.z);

                if (vec3d2.dotProduct(vec3d1) < 0.0D)
                {
                    return true;
                }
            }
        }

        return false;
    }

    public static void damageShield(EntityPlayer ep,float damage)
    {
        if (damage > 3.0F && ep.getActiveItemStack().getItem().isShield(ep.getActiveItemStack(), ep))
        {
            ItemStack copyBeforeUse = ep.getActiveItemStack().copy();
            int i = 1 + MathHelper.floor(damage);
            ep.getActiveItemStack().damageItem(i, ep);

            if (ep.getActiveItemStack().isEmpty())
            {
                EnumHand enumhand = ep.getActiveHand();
                net.minecraftforge.event.ForgeEventFactory.onPlayerDestroyItem(ep, copyBeforeUse, enumhand);

                if (enumhand == EnumHand.MAIN_HAND)
                {
                	ep.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, ItemStack.EMPTY);
                }
                else
                {
                	ep.setItemStackToSlot(EntityEquipmentSlot.OFFHAND, ItemStack.EMPTY);
                }

                ep.resetActiveHand();
                ep.playSound(SoundEvents.ITEM_SHIELD_BREAK, 0.8F, 0.8F + ep.world.rand.nextFloat() * 0.4F);
            }
        }
    }
}
