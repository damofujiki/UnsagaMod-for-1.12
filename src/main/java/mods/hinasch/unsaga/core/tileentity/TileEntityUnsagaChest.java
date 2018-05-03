//package mods.hinasch.unsaga.core.tileentity;
//
//import mods.hinasch.lib.iface.IIntSerializable;
//import mods.hinasch.lib.util.HSLibs;
//import mods.hinasch.lib.world.XYZPos;
//import mods.hinasch.unsaga.UnsagaMod;
//import mods.hinasch.unsaga.chest.ChestBehavior;
//import mods.hinasch.unsaga.chest.ChestBehavior.IUnsagaChest.EnumProperty;
//import mods.hinasch.unsaga.core.client.render.projectile.IChestModel;
//import mods.hinasch.unsaga.init.UnsagaGui;
//import net.minecraft.entity.player.EntityPlayer;
//import net.minecraft.nbt.NBTTagCompound;
//import net.minecraft.network.play.server.SPacketUpdateTileEntity;
//import net.minecraft.tileentity.TileEntity;
//import net.minecraft.util.EnumActionResult;
//import net.minecraft.util.ITickable;
//import net.minecraft.world.World;
//
//public class TileEntityUnsagaChest extends TileEntity implements ITickable,IChestModel{
//
//	public static enum VisibleType implements IIntSerializable{OVERWORLD(0),CAVE(1),VISIBLE(2);
//
//		int meta;
//
//		private VisibleType(int meta){
//			this.meta = meta;
//		}
//		@Override
//		public int getMeta() {
//			// TODO 自動生成されたメソッド・スタブ
//			return 0;
//		}
//
//		public static VisibleType fromMeta(int meta){
//			return HSLibs.fromMeta(VisibleType.values(), meta);
//		}
//
//	};
//
//	public VisibleType type;
//	public VisibleType getType() {
//		return type;
//	}
//
//	public int openedTick = 0;
//	public float prevLidAngle = 0.0F;
//	public float lidAngle = 0.0F;
//
//	public void init(World world){
//		if(ChestBehavior.hasCapability(this)){
//			ChestBehavior.getCapability(this).initChest(world);
//		}
//		if(UnsagaMod.secureRandom.nextInt(3)==0){
//			this.type = VisibleType.VISIBLE;
//		}else{
//			if(world.canBlockSeeSky(getPos())){
//				this.type = VisibleType.OVERWORLD;
//			}else{
//				this.type = VisibleType.CAVE;
//			}
//		}
//
//	}
//
//	@Override
//	public float getPrevLidAngle() {
//		// TODO 自動生成されたメソッド・スタブ
//		return 0;
//	}
//    @Override
//    public void readFromNBT(NBTTagCompound compound)
//    {
//        super.readFromNBT(compound);
//        if(compound.hasKey("type")){
//            this.type = VisibleType.fromMeta(compound.getInteger("type"));
//        }else{
//        	this.type = VisibleType.VISIBLE;
//        }
//
//    }
//
//    @Override
//    public NBTTagCompound writeToNBT(NBTTagCompound compound)
//    {
//
//    	compound.setInteger("type", this.getType().getMeta());
//    	return super.writeToNBT(compound);
//    }
//	@Override
//	public float getLidAngle() {
//		// TODO 自動生成されたメソッド・スタブ
//		return 0;
//	}
//
//	@Override
//	public boolean isOpened() {
//		// TODO 自動生成されたメソッド・スタブ
//		return ChestBehavior.hasCapability(this) && ChestBehavior.getCapability(this).getStatus(EnumProperty.OPENED);
//	}
//
//	public EnumActionResult interact(EntityPlayer player){
//		ChestBehavior.newInteraction(player, null, this);
//		if(ChestBehavior.hasCapability(this) && !this.getWorld().isRemote){
//
//			if(ChestBehavior.getCapability(this).getOpeningPlayer()!=null){
//				return EnumActionResult.FAIL;
//			}
//
//			if(ChestBehavior.getCapability(this).getStatus(EnumProperty.OPENED)){
//				return EnumActionResult.FAIL;
//			}
//
//			this.sync();
//			//			if(WorldHelper.isServer(player.getEntityWorld())){
//			//				UnsagaMod.packetDispatcher.sendTo(new PacketSyncChest(this), (EntityPlayerMP) player);
//			//			}
//
//			HSLibs.openGui(player, UnsagaMod.instance, UnsagaGui.Type.CHEST.getMeta(), this.getWorld(), new XYZPos(this.getPos()));
//			return EnumActionResult.SUCCESS;
//		}
//		return EnumActionResult.PASS;
//	}
//
//	@Override
//	public SPacketUpdateTileEntity getUpdatePacket()
//	{
//		if(ChestBehavior.hasCapability(this)){
//			NBTTagCompound tag = (NBTTagCompound) ChestBehavior.capability().getStorage().writeNBT(ChestBehavior.capability(), ChestBehavior.getCapability(this), null);
//			tag.setInteger("fieldChestType",this.type.getMeta());
//			return new SPacketUpdateTileEntity(this.getPos(),0,tag);
//		}
//		return null;
//
//	}
//
//
//	public void onDeath()
//	{
//		if(ChestBehavior.hasCapability(this) && !this.getWorld().isRemote){
//			if(!ChestBehavior.getCapability(this).getStatus(EnumProperty.OPENED)){
//				ChestBehavior.getCapability(this).reductionChestLevel();
//				ChestBehavior.getCapability(this).obtainItem(this.getWorld());
//			}
//		}
//
//	}
//
//	@Override
//	public void onDataPacket(net.minecraft.network.NetworkManager net, net.minecraft.network.play.server.SPacketUpdateTileEntity pkt)
//	{
//		UnsagaMod.logger.trace(this.getClass().getName(), "sync!");
//		if(ChestBehavior.hasCapability(this)){
//			ChestBehavior.capability().readNBT(ChestBehavior.getCapability(this), null, pkt.getNbtCompound());
//			this.type = VisibleType.fromMeta(pkt.getNbtCompound().getInteger("fieldChestType"));
//		}
//
//	}
//
//	public void sync(){
//		this.getWorld().markChunkDirty(getPos(), this);
//	}
//
//	@Override
//	public void update() {
//		if(ChestBehavior.hasCapability(this)){
//
//
//			if(ChestBehavior.getCapability(this).getStatus(EnumProperty.OPENED)){
//				this.openedTick += 1;
//				//				this.lidAngle += f1;
//				//
//				//				if(this.lidAngle > 1.0F){
//				//					this.lidAngle = 1.0F;
//				//				}
//				//				if(this.lidAngle < 0.0F){
//				//					this.lidAngle = 0.0F;
//				//				}
//			}
//
//			if(this.openedTick>50){
//				this.getWorld().setBlockToAir(this.getPos());
//			}
//		}
//
//	}
//}
