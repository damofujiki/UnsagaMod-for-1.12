package mods.hinasch.unsaga.core.item.misc.skillpanel;

import java.util.Collections;
import java.util.List;

import javax.annotation.Nullable;

import com.google.common.collect.Lists;

import mods.hinasch.lib.util.ChatHandler;
import mods.hinasch.lib.util.HSLibs;
import mods.hinasch.lib.util.Statics;
import mods.hinasch.unsaga.core.world.UnsagaWorldCapability;
import mods.hinasch.unsaga.skillpanel.SkillPanel;
import mods.hinasch.unsaga.skillpanel.SkillPanelRegistry;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemSkillPanel extends Item implements IItemColor{

	protected final int[] iconColors = {0x7cfc00,0x1e90ff,0xffc0cb,0xff8c00,0xdc143c};

	public ItemSkillPanel(){

		for(SkillPanel.IconType type:SkillPanel.IconType.values()){
			ResourceLocation res = new ResourceLocation(type.getJsonName());
			this.addPropertyOverride(res, new IconGetter(type));
		}
	}

	@Override
    @SideOnly(Side.CLIENT)
    public boolean hasEffect(ItemStack stack)
    {
		if(SkillPanelCapability.adapter.hasCapability(stack)){
			return SkillPanelCapability.adapter.getCapability(stack).hasJointed();
		}
        return stack.isItemEnchanted();
    }
	@Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
    {
		ItemStack itemStackIn = playerIn.getHeldItem(handIn);
		if(playerIn.isSneaking() && playerIn.isCreative()){
//			WorldSaveDataSkillPanel data = WorldSaveDataSkillPanel.get(worldIn);
//			data.clearData();
			UnsagaWorldCapability.ADAPTER.getCapability(worldIn);
			ChatHandler.sendChatToPlayer(playerIn, "cleared skill panel data!");
			return new ActionResult(EnumActionResult.SUCCESS,itemStackIn);
		}
		return new ActionResult(EnumActionResult.PASS, itemStackIn);
	}
	@Override
	@SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
    {
		if(SkillPanelCapability.adapter.hasCapability(stack)){
			SkillPanel panel = SkillPanelCapability.adapter.getCapability(stack).getPanel();
			int lv = SkillPanelCapability.adapter.getCapability(stack).getLevel();
			tooltip.add(HSLibs.translateKey("skillPanel."+panel.getPropertyName()+".name"));
			tooltip.add("Level "+lv);
		}
	}
	@Override
	public int colorMultiplier(ItemStack stack, int tintIndex) {
		if(tintIndex==0){
			if(SkillPanelCapability.adapter.hasCapability(stack)){
				int lv = SkillPanelCapability.adapter.getCapability(stack).getLevel();
				return iconColors[MathHelper.clamp(lv-1, 0, 4)];
			}
		}

		return Statics.COLOR_NONE;
	}

	@Override
	@SideOnly(Side.CLIENT)
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items)
    {
		if(!this.isInCreativeTab(tab)){
			return;
		}
		List<SkillPanel> panels = Lists.newArrayList(SkillPanelRegistry.instance().getProperties());
		Collections.sort(panels);
		for(int i=0;i<5;i++){
			for(SkillPanel panel:panels){
				ItemStack stack = SkillPanelRegistry.instance().getItemStack(panel, i+1);
				items.add(stack);
			}
		}

	}

	@Override
    public int getEntityLifespan(ItemStack itemStack, World world)
    {
        return 0;
    }
	public static class IconGetter implements IItemPropertyGetter{

		final SkillPanel.IconType type;
		public IconGetter(SkillPanel.IconType type){
			this.type = type;
		}
		@Override
		public float apply(ItemStack stack, World worldIn, EntityLivingBase entityIn) {

			if(SkillPanelCapability.adapter.hasCapability(stack)){
				SkillPanel panel = SkillPanelCapability.adapter.getCapability(stack).getPanel();
				if(panel.getIconType()==this.type){
					return 1.0F;
				}
			}
			return 0;
		}

	}
}
