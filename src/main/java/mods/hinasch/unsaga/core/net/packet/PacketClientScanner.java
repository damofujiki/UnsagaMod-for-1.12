package mods.hinasch.unsaga.core.net.packet;

import java.util.List;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;

import io.netty.buffer.ByteBuf;
import mods.hinasch.lib.client.ClientHelper;
import mods.hinasch.lib.item.ItemUtil;
import mods.hinasch.lib.misc.ObjectCounter;
import mods.hinasch.lib.util.ChatHandler;
import mods.hinasch.lib.util.HSLibs;
import mods.hinasch.lib.world.ScannerNew;
import mods.hinasch.lib.world.WorldHelper;
import mods.hinasch.lib.world.XYZPos;
import mods.hinasch.unsaga.chest.ChunkChestInfo;
import mods.hinasch.unsaga.chest.FieldChestType;
import mods.hinasch.unsaga.core.client.gui.ChestPosCache;
import mods.hinasch.unsaga.core.potion.PotionOreDetector;
import mods.hinasch.unsaga.core.potion.UnsagaPotions;
import net.minecraft.block.BlockChest;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;


public class PacketClientScanner implements IMessage{

	private int range;
	private Type mode;

	public static final int DETECT_TREASURE = 1;
	public static final int DETECT_GOLD = 2;

	public static enum Type{
		DETECT_TREASURE(1),DETECT_GOLD(2),DETECT_GOLD_AMP(3);

		private int meta;

		private Type(int meta) {
			this.meta = meta;
		}

		public int getMeta() {
			return meta;
		}

		public static Type fromMeta(int meta){
			for(Type type:Type.values()){
				if(meta==type.getMeta()){
					return type;
				}
			}
			return null;
		}
	}
	public PacketClientScanner(){


	}

	public PacketClientScanner(int range,Type mode){
		this.range = range;
		this.mode = mode;
	}
	@Override
	public void fromBytes(ByteBuf buf) {
		this.range = buf.readInt();
		this.mode = Type.fromMeta(buf.readInt());

	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(range);
		buf.writeInt(mode.getMeta());

	}

	public int getRange(){
		return this.range;
	}


	public Type getMode(){
		return this.mode;
	}
	public static class Handler implements IMessageHandler<PacketClientScanner,IMessage>{

		//		public final Set<Block> blockClasses = Sets.newHashSet(BlockOre.class,BlockRedstoneOre.class);

		BiPredicate<World,BlockPos> isExistBlockAndValidHeight = new BiPredicate<World,BlockPos>(){

			@Override
			public boolean test(World w,BlockPos input) {
				if(WorldHelper.isValidHeight(input)){
					if(!w.isAirBlock(input) && w.getBlockState(input).getBlock()!=null){
						return true;
					}
				}
				return false;
			}
		};

		//		Predicate<Block> isChest = new Predicate<Block>(){
		//
		//			@Override
		//			public boolean test(Block block) {
		//				return block instanceof BlockChest || block instanceof BlockUnsagaChest;
		//			}
		//
		//		};


		@Override
		public IMessage onMessage(PacketClientScanner message,
				MessageContext ctx) {
			if(ctx.side.isClient()){
				final EntityPlayer clientPlayer = Minecraft.getMinecraft().player;
				switch(message.getMode()){
				case DETECT_TREASURE:
					int range = message.getRange();
					XYZPos originPos = XYZPos.createFrom(clientPlayer);
					List<ChunkChestInfo> posList = ScannerNew.create().base(originPos).range(range, range*4, range).ready().stream().filter(in -> in.getY()>=0 && in.getY()<=255)
							.filter(in ->{
								IBlockState state = ClientHelper.getWorld().getBlockState(in);
								return state.getBlock() instanceof BlockChest;
							}).map(in ->{
								ChunkChestInfo info = new ChunkChestInfo();
								info.setChestPos(in);
								info.setFieldChestType(FieldChestType.BLOCK);
								return info;
							}).collect(Collectors.toList());
					ChestPosCache.setChestPosCache(1, posList);

					String text = posList.stream().map(in -> in.getChestPos().get()).map(in -> String.format("Detected! X:%d,Y:%d,Z:%d",in.getX(),in.getY(),in.getZ()))
							.collect(Collectors.joining("/"));


					//					if(ExtendedPotionCapability.adapter.hasCapability(clientPlayer)){
//					PotionOreDetector.Effect effect = new PotionOreDetector.Effect(UnsagaPotions.DETECT_TREASURE,ItemUtil.getPotionTime(15),ClientHelper.getPlayer().getPosition(),posList);
					//						PotionOreDetect.Data data = (PotionOreDetect.Data) ExtendedPotionCapability.adapter.getCapability(clientPlayer).getData(UnsagaPotions.DETECT_GOLD);
					//						data.setBasePos(ClientHelper.getPlayer().getPosition());
					//						data.setOrePosList(blockPosList);
					ClientHelper.getPlayer().addPotionEffect(new PotionEffect(UnsagaPotions.DETECT_TREASURE,ItemUtil.getPotionTime(20)));
					//					}
					ChatHandler.sendChatToPlayer(clientPlayer, HSLibs.translateKey("msg.spell.chest.detected")+text);
					break;
				case DETECT_GOLD:
				case DETECT_GOLD_AMP:

					World world = clientPlayer.getEntityWorld();
					List<BlockPos> blockPosList = ScannerNew.create().base(clientPlayer).range(message.getRange()).ready().stream()
							.filter(in -> {
								if(WorldHelper.isValidHeight(in) && !world.isAirBlock(in)){
									IBlockState state = world.getBlockState(in);
									int meta = state.getBlock().getMetaFromState(state);
									ItemStack blockStack = new ItemStack(state.getBlock(),1,meta);
									if(state.getBlock()==Blocks.DIAMOND_ORE){
										return message.getMode()==PacketClientScanner.Type.DETECT_GOLD_AMP;
									}
									if(blockStack!=null){
										if(!HSLibs.getOreNames(blockStack).isEmpty()){
											return HSLibs.getOreNames(blockStack).stream().anyMatch(str -> str.startsWith("ore"));
										}
									}
								}
								return false;
							}).collect(Collectors.toList());

					ObjectCounter<String> cnt = blockPosList.stream().map(in -> world.getBlockState(in))
							.map(in -> HSLibs.getItemStackFromState(in))
							.map(in -> in.getDisplayName())
							.collect(ObjectCounter::new,(left,right)->left.add(right),(left,right)->left.merge(right));


					//					if(ExtendedPotionCapability.adapter.hasCapability(clientPlayer)){
					PotionOreDetector.Effect eff = new PotionOreDetector.Effect(UnsagaPotions.DETECT_GOLD,ItemUtil.getPotionTime(15),ClientHelper.getPlayer().getPosition(),blockPosList);
					//						PotionOreDetect.Data data = (PotionOreDetect.Data) ExtendedPotionCapability.adapter.getCapability(clientPlayer).getData(UnsagaPotions.DETECT_GOLD);
					//						data.setBasePos(ClientHelper.getPlayer().getPosition());
					//						data.setOrePosList(blockPosList);
					ClientHelper.getPlayer().addPotionEffect(eff);
					//					}

					if(!cnt.isEmpty()){
						String strMessage = cnt.getKeySet().stream().map(in ->{
							int count = cnt.get(in);
							return String.format("%s:%d ",in,count);
						}).collect(Collectors.joining("/"));


						if(!strMessage.isEmpty()){
							ChatHandler.sendChatToPlayer(clientPlayer, strMessage);
						}
					}else{
						ChatHandler.sendChatToPlayer(clientPlayer, HSLibs.translateKey("msg.spell.metal.notfound"));
					}


				}





			}
			return null;
		}



	}
}
