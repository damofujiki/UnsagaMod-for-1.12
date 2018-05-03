package mods.hinasch.unsaga.core.item.wearable;

import java.util.List;

import javax.annotation.Nullable;

import com.google.common.collect.Sets;

import mods.hinasch.unsaga.common.tool.ItemToolUnsaga;
import mods.hinasch.unsaga.core.client.event.UnsagaTooltips;
import mods.hinasch.unsaga.util.ToolCategory;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemAccessoryUnsaga extends ItemToolUnsaga{

	public ItemAccessoryUnsaga() {
		super(0, -2.8F, Sets.newHashSet(),ToolCategory.ACCESSORY);
		// TODO 自動生成されたコンストラクター・スタブ
	}

	@Override
	public ToolCategory getCategory() {
		// TODO 自動生成されたメソッド・スタブ
		return ToolCategory.ACCESSORY;
	}

	@Override
	@SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
    {
		this.getComponent().addInformations(stack, tooltip, flagIn,UnsagaTooltips.Type.ACCESSORY_ARMOR);
	}
}
