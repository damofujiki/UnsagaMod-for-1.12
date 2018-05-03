package mods.hinasch.unsaga.chest;


import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import mods.hinasch.lib.capability.CapabilityAdapterFactory.ICapabilityAdapterPlan;
import mods.hinasch.lib.capability.CapabilityAdapterFrame;
import mods.hinasch.lib.capability.CapabilityStorage;
import mods.hinasch.lib.capability.ComponentCapabilityAdapters;
import mods.hinasch.lib.client.ClientHelper;
import mods.hinasch.lib.item.WeightedRandomItem;
import mods.hinasch.lib.network.PacketSyncCapability;
import mods.hinasch.lib.util.HSLibs;
import mods.hinasch.lib.util.UtilNBT;
import mods.hinasch.unsaga.UnsagaMod;
import mods.hinasch.unsaga.common.tool.ItemFactory;
import mods.hinasch.unsaga.core.client.gui.GuiChest;
import mods.hinasch.unsaga.material.UnsagaMaterialRegistry;
import mods.hinasch.unsaga.util.ToolCategory;
import mods.hinasch.unsagamagic.spell.tablet.TabletRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.WeightedRandom;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class ChestCapability {

	public static class DefaultImpl implements IChestCapability{

		Optional<EntityPlayer> ep = Optional.empty();
		boolean magicLocked = false;
		boolean opened = false;
		boolean analyzed = true;
		Class chestType;
		boolean init = false;
		int level = 0;
		List<ChestTrap> traps = Lists.newArrayList();
		boolean hasLocked = false;
		ChestTreasureType treasureType = ChestTreasureType.MONEY;

		@Override
		public void catchSyncData(NBTTagCompound nbt) {
			CAPA.getStorage().readNBT(CAPA, this, null, nbt);

		}

		@Override
		public Class getChestType() {
			// TODO 自動生成されたメソッド・スタブ
			return this.chestType;
		}

		@Override
		public String getIdentifyName() {
			// TODO 自動生成されたメソッド・スタブ
			return ID_SYNC;
		}

		@Override
		public int getLevel() {
			// TODO 自動生成されたメソッド・スタブ
			return this.level;
		}

		@Override
		public Optional<EntityPlayer> getOpeningPlayer() {
			// TODO 自動生成されたメソッド・スタブ
			return this.ep;
		}

		@Override
		public NBTTagCompound getSendingData() {
			return (NBTTagCompound) CAPA.getStorage().writeNBT(CAPA, this, null);
		}

		@Override
		public List<ChestTrap> getTraps() {
			// TODO 自動生成されたメソッド・スタブ
			return this.traps;
		}

		@Override
		public boolean hasAnalyzed() {
			// TODO 自動生成されたメソッド・スタブ
			return this.analyzed;
		}

		@Override
		public boolean hasDefused() {
			// TODO 自動生成されたメソッド・スタブ
			return this.traps.isEmpty();
		}

		@Override
		public boolean hasInitialized() {
			// TODO 自動生成されたメソッド・スタブ
			return this.init;
		}

		@Override
		public boolean hasLocked() {
			// TODO 自動生成されたメソッド・スタブ
			return this.hasLocked;
		}

		@Override
		public boolean hasMagicLocked() {
			// TODO 自動生成されたメソッド・スタブ
			return this.magicLocked;
		}

		@Override
		public boolean hasOpened() {
			// TODO 自動生成されたメソッド・スタブ
			return this.opened;
		}

		@Override
		public void onPacket(PacketSyncCapability message, MessageContext ctx) {
			int id = message.getArgs().getInteger("entityid");
			Entity ent = ClientHelper.getWorld().getEntityByID(id);
//			UnsagaMod.logger.trace("ent", ent,message.getNbt());
			if(ent!=null && ChestCapability.adapterEntity.hasCapability(ent)){
				ChestCapability.adapterEntity.getCapability(ent).catchSyncData(message.getNbt());
				if(ClientHelper.getCurrentGui() instanceof GuiChest){
					((GuiChest)ClientHelper.getCurrentGui()).updateCapability(ChestCapability.adapterEntity.getCapability(ent));
				}
			}
		}

		@Override
		public void setAnalyzed(boolean par1) {
			// TODO 自動生成されたメソッド・スタブ
			this.analyzed = par1;
		}

		@Override
		public void setChestType(Class clazz) {
			this.chestType = clazz;

		}

		@Override
		public void setDefused() {
			// TODO 自動生成されたメソッド・スタブ
			this.traps.clear();
		}

		@Override
		public void setInitialized(boolean par1) {
			// TODO 自動生成されたメソッド・スタブ
			this.init = par1;
		}

		@Override
		public void setLevel(int par1) {
			// TODO 自動生成されたメソッド・スタブ
			this.level = par1;
		}

		@Override
		public void setLocked(boolean par1) {
			// TODO 自動生成されたメソッド・スタブ
			this.hasLocked = par1;
		}

		@Override
		public void setMagicLocked(boolean par1) {
			// TODO 自動生成されたメソッド・スタブ
			this.magicLocked = par1;
		}

		@Override
		public void setOpened(boolean par1) {
			// TODO 自動生成されたメソッド・スタブ
			this.opened = par1;
		}

		@Override
		public void setOpeningPlayer(EntityPlayer ep) {
			if(ep!=null){
				this.ep = Optional.of(ep);
			}else{
				this.ep = Optional.empty();
			}

		}

		@Override
		public void setTraps(List<ChestTrap> traps) {
			// TODO 自動生成されたメソッド・スタブ
			this.traps = traps;
		}

		@Override
		public ChestTreasureType getTreasureType() {
			// TODO 自動生成されたメソッド・スタブ
			return this.treasureType;
		}

		@Override
		public void setTreasureType(ChestTreasureType par1) {
			// TODO 自動生成されたメソッド・スタブ
			this.treasureType = par1;
		}

	}
	public static class Storage extends CapabilityStorage<IChestCapability>{

		@Override
		public void readNBT(NBTTagCompound comp, Capability<IChestCapability> capability, IChestCapability instance, EnumFacing side) {
//			UnsagaMod.logger.trace(this.getClass().getName(), instance.getTraps());
			if(comp.hasKey("level")){
				instance.setLevel(comp.getByte("level"));
			}
			if(comp.hasKey("traps")){
				instance.setTraps(UtilNBT.readListFromNBT(comp,"traps",ChestTrap.RESTORE));
			}else{
				instance.setTraps(Lists.newArrayList());
			}
			if(comp.hasKey("init")){
				instance.setInitialized(comp.getBoolean("init"));
			}
			if(comp.hasKey("lock")){
				instance.setLocked(comp.getBoolean("lock"));
			}
			if(comp.hasKey("analyzed")){
				instance.setAnalyzed(comp.getBoolean("analyzed"));
			}
			if(comp.hasKey("opened")){
				instance.setOpened(comp.getBoolean("opened"));
			}
			if(comp.hasKey("magicLock")){
				instance.setMagicLocked(comp.getBoolean("magicLock"));
			}
			if(comp.hasKey("treasureType")){
				instance.setTreasureType(ChestTreasureType.fromName(comp.getString("treasureType")));
			}
		}

		@Override
		public void writeNBT(NBTTagCompound comp, Capability<IChestCapability> capability, IChestCapability instance, EnumFacing side) {
			comp.setByte("level", (byte) instance.getLevel());
			if(!instance.getTraps().isEmpty()){

				UtilNBT.writeListToNBT(instance.getTraps(), comp, "traps");
			}
			comp.setBoolean("init", instance.hasInitialized());
			comp.setBoolean("lock",instance.hasLocked());
			comp.setBoolean("analyzed", instance.hasAnalyzed());
			comp.setBoolean("opened", instance.hasOpened());
			comp.setBoolean("magicLock", instance.hasMagicLocked());
			comp.setString("treasureType", instance.getTreasureType().getName());
		}

	}

	@CapabilityInject(IChestCapability.class)
	public static Capability<IChestCapability> CAPA;

	public static final String ID_SYNC = "chest";
	public static ICapabilityAdapterPlan<IChestCapability> capabilityAdapter = new ICapabilityAdapterPlan<IChestCapability>(){


		@Override
		public Capability<IChestCapability> getCapability() {
			// TODO 自動生成されたメソッド・スタブ
			return CAPA;
		}

		@Override
		public Class<IChestCapability> getCapabilityClass() {
			// TODO 自動生成されたメソッド・スタブ
			return IChestCapability.class;
		}

		@Override
		public Class<? extends IChestCapability> getDefault() {
			// TODO 自動生成されたメソッド・スタブ
			return DefaultImpl.class;
		}

		@Override
		public IStorage<IChestCapability> getStorage() {
			// TODO 自動生成されたメソッド・スタブ
			return new Storage();
		}

	};

	public static enum ChestTreasureType{
		ITEM("item",10),MONEY("money",10),TABLET("tablet",1);

		final private String name;
		final private int weight;

		private ChestTreasureType(String name,int weight){
			this.name = name;
			this.weight = weight;
		}

		public String getMessage(){
			switch(this){
			case ITEM:
				return "gui.unsaga.chest.success.divine.item";
			case MONEY:
				return "gui.unsaga.chest.success.divine.money";
			case TABLET:
				return "gui.unsaga.chest.success.divine.tablet";
			default:
				break;

			}
			return "???";
		}
		public int getWeight(){
			return this.weight;
		}
		public String getName(){
			return this.name;
		}

		public ItemStack createTreasure(Random rand,int level,ItemFactory factory){
			switch(this){
			case ITEM:
				int generateLevel = level/10 + 1;
				Set<ToolCategory> cate = Sets.newHashSet();
				cate.addAll(ToolCategory.merchandiseSet);
				cate.add(ToolCategory.GUN);
				List<ItemStack> stacks = factory.createMerchandises(5, generateLevel, cate, Sets.newHashSet(UnsagaMaterialRegistry.instance().merchandiseMaterial));
				Collections.shuffle(stacks);
				ItemStack rt = stacks.get(0);
				return rt;
			case MONEY:
				int num = rand.nextInt(level/10+1);
				return new ItemStack(Items.GOLD_NUGGET,MathHelper.clamp(num, 1, 10));
			case TABLET:
				return TabletRegistry.drawRandomTablet(rand).getStack(1);
			default:
				break;

			}
			return ItemStack.EMPTY;
		}
		public WeightedRandomItem<ChestTreasureType> getWeighted(){
			return new WeightedRandomItem(this.getWeight(),this);
		}

		public static List<WeightedRandomItem<ChestTreasureType>> createList(int level){
			return Lists.newArrayList(ChestTreasureType.values()).stream()
					.filter(in -> {
						if(level<15){
							return in!=ChestTreasureType.TABLET;
						}
						return true;
					}).map(in -> new WeightedRandomItem<ChestTreasureType>(in.getWeight(),in)).collect(Collectors.toList());
		}
		public static ChestTreasureType fromName(String name){
			for(ChestTreasureType type:ChestTreasureType.values()){
				if(type.getName().equals(name)){
					return type;
				}
			}
			return MONEY;
		}
	}
	public static CapabilityAdapterFrame<IChestCapability> adapterBase = UnsagaMod.capabilityAdapterFactory.create(capabilityAdapter);
	public static ComponentCapabilityAdapters.Entity<IChestCapability> adapterEntity =adapterBase.createChildEntity("chest");

	public static ComponentCapabilityAdapters.TileEntity<IChestCapability> adapterTE = adapterBase.createChildTileEntity("techest");

	static{
		adapterEntity.setPredicate(ev -> ev.getObject() instanceof IChestBehavior);
		adapterEntity.setRequireSerialize(true);
	}

	public static void register(){
		Random rand = UnsagaMod.secureRandom;
		adapterEntity.registerAttachEvent((inst,capa,facing,ev)->{
			if(!inst.hasInitialized()){

				inst.setChestType(Entity.class);
				inst.setLevel(rand.nextInt(99)+1);
				if(inst.getLevel()>15){
					inst.setAnalyzed(false);
					if(rand.nextFloat()<0.5F+0.0049F*inst.getLevel()){
						inst.setLocked(true);
					}

					if(inst.getLevel()>30){
						if(rand.nextFloat()<0.5F+0.0049F*inst.getLevel()){
							inst.setMagicLocked(true);
						}
					}

				}

				ChestTreasureType type = WeightedRandom.getRandomItem(rand,ChestTreasureType.createList(inst.getLevel())).getItem();
				inst.setTreasureType(type);
				inst.setTraps(ChestHelper.getInitializedTraps(inst, rand));
				inst.setInitialized(true);
			}
		});

		HSLibs.registerEvent(new EventUnlockMagic());
//		HSLibs.registerEvent(new EventGenerateChest());
//		HSLibs.registerEvent(new EventChestAppear());
//		HSLibs.registerEvent(new EventChestSpawn());
		PacketSyncCapability.registerSyncCapability(ID_SYNC, CAPA);
	}

	public static void registerChunkEvent(){
//		HSLibs.registerEvent(new EventGenerateChest());
	}
}
