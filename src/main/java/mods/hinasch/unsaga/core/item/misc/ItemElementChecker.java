package mods.hinasch.unsaga.core.item.misc;

import mods.hinasch.lib.util.ChatHandler;
import mods.hinasch.lib.world.WorldHelper;
import mods.hinasch.unsaga.core.world.chunk.UnsagaChunkCapability;
import mods.hinasch.unsaga.element.AspectGetter;
import mods.hinasch.unsaga.element.BiomeElements;
import mods.hinasch.unsaga.element.newele.ElementTable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

public class ItemElementChecker extends Item{


	public ItemElementChecker(){
		super();
		this.setMaxStackSize(1);
		this.setMaxDamage(0);


	}

	@Override
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
		IBlockState state = worldIn.getBlockState(pos);
		if(state!=null && WorldHelper.isClient(worldIn)){
			ElementTable base =  BiomeElements.getBiomeElements(worldIn.getBiome(pos));
			Chunk chunk = worldIn.getChunkFromBlockCoords(pos);
			if(UnsagaChunkCapability.ADAPTER.hasCapability(chunk)){
				ChatHandler.sendChatToPlayer(player, AspectGetter.getCurrentAspect(worldIn, pos).getAmountAsFloatLocalized());
				return EnumActionResult.SUCCESS;
			}


		}
        return EnumActionResult.PASS;
    }



}
