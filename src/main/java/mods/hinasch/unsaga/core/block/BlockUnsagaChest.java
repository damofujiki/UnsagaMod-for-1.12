//package mods.hinasch.unsaga.core.block;
//
//import java.util.Random;
//
//import mods.hinasch.unsaga.core.tileentity.TileEntityUnsagaChest;
//import net.minecraft.block.BlockContainer;
//import net.minecraft.block.material.Material;
//import net.minecraft.block.state.IBlockState;
//import net.minecraft.entity.EntityLivingBase;
//import net.minecraft.entity.player.EntityPlayer;
//import net.minecraft.item.Item;
//import net.minecraft.item.ItemStack;
//import net.minecraft.tileentity.TileEntity;
//import net.minecraft.util.EnumFacing;
//import net.minecraft.util.EnumHand;
//import net.minecraft.util.math.BlockPos;
//import net.minecraft.world.World;
//
//public class BlockUnsagaChest extends BlockContainer{
//
//	public BlockUnsagaChest() {
//		super(Material.WOOD);
//		// TODO 自動生成されたコンストラクター・スタブ
//	}
//
//	@Override
//	public TileEntity createNewTileEntity(World worldIn, int meta) {
//		TileEntityUnsagaChest te = new TileEntityUnsagaChest();
////		if(worldIn!=null){
////			te.init(worldIn);
////		}
//		return te;
//	}
//
//	@Override
//    public Item getItemDropped(IBlockState state, Random rand, int fortune)
//    {
//        return null;
//    }
//	@Override
//    public boolean isOpaqueCube(IBlockState state)
//    {
//        return false;
//    }
//
//	@Override
//    public boolean isFullCube(IBlockState state)
//    {
//        return false;
//    }
//
//	@Override
//    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ)
//    {
//		if(worldIn.getTileEntity(pos) instanceof TileEntityUnsagaChest){
//			TileEntityUnsagaChest chest = (TileEntityUnsagaChest) worldIn.getTileEntity(pos);
////			if(ChestBehavior.hasCapability(chest) && !ChestBehavior.getCapability(chest).hasInitialized()){
////				ChestBehavior.getCapability(chest).initChest(worldIn);
////				worldIn.markChunkDirty(pos, chest);
////			}
//			chest.interact(playerIn);
//			return true;
//		}
//        return false;
//    }
//	@Override
//    public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
//    {
//		if(worldIn.getTileEntity(pos) instanceof TileEntityUnsagaChest){
//			TileEntityUnsagaChest chest = (TileEntityUnsagaChest) worldIn.getTileEntity(pos);
//
//			chest.onDeath();
//		}
//		super.breakBlock(worldIn, pos, state);
//    }
//	@Override
//    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
//    {
//		if(worldIn.getTileEntity(pos) instanceof TileEntityUnsagaChest){
//			TileEntityUnsagaChest chest = (TileEntityUnsagaChest) worldIn.getTileEntity(pos);
//			chest.init(worldIn);
//			worldIn.markChunkDirty(pos, chest);
//		}
//		super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
//    }
//}
