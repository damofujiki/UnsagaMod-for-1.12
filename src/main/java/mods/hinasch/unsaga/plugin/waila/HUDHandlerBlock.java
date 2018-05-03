//package mods.hinasch.unsaga.plugin.waila;
//
//import java.util.List;
//
//import com.google.common.base.Optional;
//
//import mcp.mobius.waila.api.IWailaConfigHandler;
//import mcp.mobius.waila.api.IWailaDataAccessor;
//import mcp.mobius.waila.api.IWailaDataProvider;
//import mcp.mobius.waila.api.IWailaRegistrar;
//import mods.hinasch.lib.client.ClientHelper;
//import mods.hinasch.unsaga.UnsagaMod;
//import mods.hinasch.unsaga.element.newele.ElementTable;
//import net.minecraft.block.Block;
//import net.minecraft.block.state.IBlockState;
//import net.minecraft.client.Minecraft;
//import net.minecraft.client.gui.Gui;
//import net.minecraft.client.gui.ScaledResolution;
//import net.minecraft.entity.player.EntityPlayerMP;
//import net.minecraft.item.ItemStack;
//import net.minecraft.nbt.NBTTagCompound;
//import net.minecraft.tileentity.TileEntity;
//import net.minecraft.util.math.BlockPos;
//import net.minecraft.util.math.Vec3d;
//import net.minecraft.world.World;
//
//public class HUDHandlerBlock extends Gui implements IWailaDataProvider{
//
//	@Override
//	public ItemStack getWailaStack(IWailaDataAccessor paramIWailaDataAccessor,
//			IWailaConfigHandler paramIWailaConfigHandler) {
//		// TODO 自動生成されたメソッド・スタブ
//		return null;
//	}
//
//	@Override
//	public List<String> getWailaHead(ItemStack paramItemStack, List<String> paramList,
//			IWailaDataAccessor paramIWailaDataAccessor, IWailaConfigHandler paramIWailaConfigHandler) {
//		// TODO 自動生成されたメソッド・スタブ
//		return null;
//	}
//
//	@Override
//	public List<String> getWailaBody(ItemStack paramItemStack, List<String> paramList,
//			IWailaDataAccessor accessor, IWailaConfigHandler paramIWailaConfigHandler) {
//		if(accessor.getBlock()!=null){
//			IBlockState block = accessor.getBlockState();
//			Optional<ElementTable> table = UnsagaMod.core.library.getElementPointLib().find(block);
//			if(table.isPresent()){
//
//
//				ClientHelper.bindTextureToTextureManager(ICONS);
//				ScaledResolution res = new ScaledResolution(Minecraft.getMinecraft());
//				int width = res.getScaledWidth();
//				int height = res.getScaledHeight();
//				Vec3d vec = accessor.getRenderingPosition();
//				this.drawTexturedModalRect((int)vec.xCoord, (int)vec.yCoord, 16, 0, 9, 9);
//			}
//		}
//		return paramList;
//	}
//
//	@Override
//	public List<String> getWailaTail(ItemStack paramItemStack, List<String> paramList,
//			IWailaDataAccessor paramIWailaDataAccessor, IWailaConfigHandler paramIWailaConfigHandler) {
//		// TODO 自動生成されたメソッド・スタブ
//		return null;
//	}
//
//	@Override
//	public NBTTagCompound getNBTData(EntityPlayerMP paramEntityPlayerMP, TileEntity paramTileEntity,
//			NBTTagCompound paramNBTTagCompound, World paramWorld, BlockPos paramBlockPos) {
//		// TODO 自動生成されたメソッド・スタブ
//		return null;
//	}
//	public static void register(IWailaRegistrar registrar){
//		registrar.addConfig("UnsagaMod", "hinasch.unsaga.showElement",true);
//
//		registrar.registerBodyProvider(new HUDHandlerBlock(), Block.class);
//	}
//}
