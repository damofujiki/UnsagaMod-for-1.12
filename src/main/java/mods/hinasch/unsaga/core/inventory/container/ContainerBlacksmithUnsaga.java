package mods.hinasch.unsaga.core.inventory.container;

import java.util.List;
import java.util.Optional;

import com.google.common.collect.Lists;
import com.mojang.realmsclient.util.Pair;

import mods.hinasch.lib.container.ContainerBase;
import mods.hinasch.lib.container.inventory.InventoryHandler;
import mods.hinasch.lib.core.HSLib;
import mods.hinasch.lib.item.ItemUtil;
import mods.hinasch.lib.network.PacketGuiButtonBaseNew;
import mods.hinasch.lib.network.PacketSendGuiInfoToClient;
import mods.hinasch.lib.network.PacketSound;
import mods.hinasch.lib.util.UtilNBT;
import mods.hinasch.lib.world.WorldHelper;
import mods.hinasch.lib.world.XYZPos;
import mods.hinasch.unsaga.UnsagaMod;
import mods.hinasch.unsaga.core.advancement.UnsagaTriggers;
import mods.hinasch.unsaga.core.client.gui.GuiBlacksmithUnsaga;
import mods.hinasch.unsaga.core.inventory.InventorySmithUnsaga;
import mods.hinasch.unsaga.core.inventory.slot.SlotSmith;
import mods.hinasch.unsaga.core.net.packet.PacketGuiButtonUnsaga;
import mods.hinasch.unsaga.init.UnsagaGui;
import mods.hinasch.unsaga.material.MaterialItemAssociatings;
import mods.hinasch.unsaga.material.SuitableLists;
import mods.hinasch.unsaga.material.UnsagaMaterial;
import mods.hinasch.unsaga.material.UnsagaMaterialCapability;
import mods.hinasch.unsaga.util.ToolCategory;
import mods.hinasch.unsaga.villager.UnsagaVillagerCapability;
import mods.hinasch.unsaga.villager.smith.BlackSmithType;
import mods.hinasch.unsaga.villager.smith.ByproductAbilityRegistry;
import mods.hinasch.unsaga.villager.smith.ItemForgeFactory;
import mods.hinasch.unsaga.villager.smith.ItemForgeFactory.ForgeResult;
import mods.hinasch.unsaga.villager.smith.ItemForgeFactory.ForgingProcess;
import mods.hinasch.unsaga.villager.smith.SmithMaterialRegistry;
import mods.hinasch.unsaga.villager.smith.ValidPaymentRegistry;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IMerchant;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;

public class ContainerBlacksmithUnsaga extends ContainerBase{

//	protected ForgingFactory forgingFactory;
	final protected IMerchant theMerchant;
	public final EntityPlayer ep;
	final protected World worldobj;
	final protected InventorySmithUnsaga inventorySmith;
	final protected IInventory invPlayer;
	protected static final int PAYMENT = 0;
	protected static final int BASE = 1;
	protected static final int MATERIAL = 2;
	protected static final int FORGED = 3;
	protected byte currentCategory = 0; //GUI側と同期される
//	protected DebugUnsaga debug;

	/**
	 * 鍛冶屋の特性。未完成
	 */
	BlackSmithType type = BlackSmithType.NONE;


	ItemForgeFactory factory;


	boolean isForgeable = false;
	Optional<ForgingProcess> process = Optional.empty();

	protected int id = 0;



	public ContainerBlacksmithUnsaga(final IMerchant par1,World par2,EntityPlayer ep){

		super(ep, new InventorySmithUnsaga(ep,par1));
		this.ep = ep;
		this.invPlayer = ep.inventory;
		this.worldobj = par2;
//		if(debug==null){
//			debug = new DebugUnsaga();
//		}


		//this.helper = new SmithHelper();
		this.theMerchant = par1;
		this.theMerchant.setCustomer(ep);

		UnsagaMod.logger.trace("inv", ep.getEntityWorld().isRemote);
//		if(this.theMerchant!=null){
//
//		}




		if(this.theMerchant instanceof EntityVillager){
			EntityVillager villager = (EntityVillager) this.theMerchant;
			if(UnsagaVillagerCapability.ADAPTER.hasCapability(villager)){
				this.type = UnsagaVillagerCapability.ADAPTER.getCapability(villager).getBlackSmithType();
				UnsagaMod.logger.trace(this.getClass().getName(), this.type);
			}
		}

		this.factory = new ItemForgeFactory(this.worldobj.rand,this);
//		//server only
//		this.smithType = new Supplier<SmithProfessionality>(){
//
//			@Override
//			public SmithProfessionality get() {
//				if(par1 instanceof EntityVillager && UnsagaVillager.hasCapability((EntityVillager) par1)){
//					return UnsagaVillager.getCapability((EntityVillager) par1).getSmithProfessionality();
//				}
//
//				return SmithProfessionality.NORMAL;
//			}
//		}.get();
//




//		Unsaga.debug("Container:"+this.theMerchant);


		this.inventorySmith = (InventorySmithUnsaga) this.inv;
		this.addSlotToContainer(new SlotSmith.SlotPayment(this.inventorySmith, this.PAYMENT, 28, 53)); //Emerald
		this.addSlotToContainer(new SlotSmith.SlotMaterial(this.inventorySmith, this.BASE, 28, 53-(18*2))); //Base Material
		this.addSlotToContainer(new SlotSmith.SlotMaterial(this.inventorySmith, this.MATERIAL, 28+(18*2)-8, 53-(18*2))); //Material2
		this.addSlotToContainer(new SlotSmith.SlotMaterial(this.inventorySmith, this.FORGED, 28+(18*6)+1, 52));

//		forgingFactory = new ForgingFactory(ep,this.smithType);


//		this.ep.addStat(UnsagaModCore.instance().achievements.openVillager);
	}


	public BlackSmithType getSmithType(){
		return this.type;
	}

	public ToolCategory getCurrentCategory(){
		return ToolCategory.toolArray.get(this.currentCategory);
	}
//	@Override
//	public boolean canInteractWith(EntityPlayer entityplayer) {
//		if(this.theMerchant==null){
//			return false;
//		}
//		return this.theMerchant.getCustomer() == entityplayer;
//	}

	@Override
	public int getGuiID(){
		return UnsagaGui.Type.SMITH.getMeta();
	}

	@Override
	public SimpleNetworkWrapper getPacketPipeline() {
		// TODO 自動生成されたメソッド・スタブ
		return UnsagaMod.packetDispatcher;
	}


	@Override
	public PacketGuiButtonBaseNew getPacketGuiButton(int guiID,int buttonID,NBTTagCompound args){
		return PacketGuiButtonUnsaga.create(UnsagaGui.Type.fromMeta(guiID),buttonID,args);
	}

	@Deprecated
	public void onClientGuiOpened(){
//		UnsagaMod.logger.trace("smith", par2);
//		if(WorldHelper.isServer(this.worldobj)){
//			NBTTagCompound args = UtilNBT.createCompound();
//			args.setInteger("type", this.smithType.getMeta());
//			UnsagaMod.packetDispatcher.sendTo(PacketSyncGui.create(Type.SMITH_PROFESSIONALITY, args), (EntityPlayerMP) ep);
//		}
	}

	public List<ToolCategory> getForgeableToolCategory(){
		return ToolCategory.toolArray;
	}

	/**
	 * 素材として使えるかどうかのメソッド
	 * @param base
	 * @return
	 */
	public Optional<UnsagaMaterial> getMaterialOrNot(ItemStack base){
		UnsagaMaterial mate = null;
		if(UnsagaMaterialCapability.adapter.hasCapability(base)){
			mate = UnsagaMaterialCapability.adapter.getCapability(base).getMaterial();
		}
		if(MaterialItemAssociatings.instance().getMaterialFromStack(base).isPresent()){
			mate =  MaterialItemAssociatings.instance().getMaterialFromStack(base).get();
		}
		if(SmithMaterialRegistry.instance().find(base).isPresent()){
			mate = SmithMaterialRegistry.instance().find(base).get().getMaterial();
		}
		if(mate==null){
			return Optional.empty();
		}
		return Optional.of(mate);
	}
	public boolean isValidSubItem(){
		ItemStack sub = this.getSubStack();
		if(sub!=null){
			return this.getMaterialOrNot(sub).isPresent();
		}
		return false;
	}

	public boolean isValidPayment(){
		ItemStack pay = this.inventorySmith.getPayment();
		if(pay!=null){
			return ValidPaymentRegistry.getValue(pay).isPresent();
		}
		return false;
	}
	public boolean hasSubMaterialByproductAbility(){
		if(this.isValidSubItem()){
			return ByproductAbilityRegistry.instance().hasByproductAbility(this.getMaterialOrNot(getSubStack()).get());
		}
		return false;
	}

	/**
	 * ベースアイテムとして使えるか（サブ素材として使え、なおかつ適合素材）
	 * @return
	 */
	public boolean isValidBaseItem(){
		if(this.getBaseStack()!=null){
			ItemStack base = this.getBaseStack();
			if(this.getMaterialOrNot(base).isPresent()){
//				UnsagaMod.logger.trace("ddd", this.getMaterialOrNot(base).get());
				UnsagaMaterial mate = this.getMaterialOrNot(base).get();
				ToolCategory cate = this.getForgeableToolCategory().get(currentCategory);
//				UnsagaMod.logger.trace("zzz", SuitableLists.getSuitableList(cate).values());
				if(SuitableLists.getSuitables(cate).contains(mate)){

					return true;
				}
			}


		}
		return false;
	}
	@Override
	public void onPacketData() {
//		Unsaga.debug(this.id);
		this.currentCategory = this.argsSent.getByte("category");
		final ToolCategory category = this.getForgeableToolCategory().get(currentCategory);


		if(this.buttonID==GuiBlacksmithUnsaga.DOFORGE){
//			final IMaterialAnalyzer analyzerBase = MaterialAnalyzer.hasCapability(this.inventorySmith.getBaseItem()) ? MaterialAnalyzer.getCapability(this.inventorySmith.getBaseItem()) : null;
//			final IMaterialAnalyzer analyzerSub = MaterialAnalyzer.hasCapability(this.inventorySmith.getMaterial()) ? MaterialAnalyzer.getCapability(this.inventorySmith.getMaterial()) : null;
//			final MaterialAnalyzer baseItemInfo = this.inventorySmith.getBaseItem()!=null ? new MaterialAnalyzer(this.inventorySmith.getBaseItem()) : null;
//			final MaterialAnalyzer subItemInfo = this.inventorySmith.getMaterial()!=null ? new MaterialAnalyzer(this.inventorySmith.getMaterial()) : null;



			if(this.isValidBaseItem() && this.isValidPayment() && this.isValidSubItem() && ItemUtil.isItemStackNull(this.inventorySmith.getForged())){
				ForgingProcess forging = this.factory.create(this.getMaterialOrNot(getBaseStack()).get(), this.getMaterialOrNot(getSubStack()).get(), getBaseStack(), getSubStack());
				ForgeResult result =  forging.decideForgedDurability().decideForgedMaterial()
						.decideForgedWeight().bakeForgedStack();
				if(forging.getForgedStack().isPresent()){
//					ep.addStat(UnsagaAchievementRegistry.instance().firstSmith);
					if(WorldHelper.isServer(worldobj)){
						if(ep instanceof EntityPlayerMP){
							UnsagaTriggers.FIRST_FORGE.trigger((EntityPlayerMP) ep);
							HSLib.getPacketDispatcher().sendTo(PacketSound.atPos(result.getResultSound(), XYZPos.createFrom(ep)), (EntityPlayerMP) this.ep);
						}
						ItemStack forged = forging.getForgedStack().get();
						this.inventorySmith.setInventorySlotContents(BASE, null);
						this.inventorySmith.setInventorySlotContents(MATERIAL, null);
						this.inventorySmith.decrStackSize(PAYMENT, 1);
						this.inventorySmith.setInventorySlotContents(FORGED, forged);
					}

				}
			}

		}

	}

	public ItemStack getForgedInv(){
		return this.inventorySmith.getStackInSlot(FORGED);
	}

//	public EnumPayValues getPaymentValue(){
//		if(this.inventorySmith.getPayment()!=null){
//			return ValidPayments.getValueFromItemStack(this.inventorySmith.getPayment());
//		}
//		return null;
//	}


	@Override
	public void onContainerClosed(EntityPlayer par1EntityPlayer)
	{
		super.onContainerClosed(par1EntityPlayer);
		if(this.theMerchant!=null){
			this.theMerchant.setCustomer(null);
		}

	}


	public EntityLivingBase getSmith(){
		if(this.theMerchant==null){
			return this.ep;
		}
		return (EntityLivingBase) this.theMerchant;
	}

	public ItemStack getBaseStack(){
		return this.inventorySmith.getBaseItem();
	}

	public ItemStack getSubStack(){
		return this.inventorySmith.getMaterial();
	}

	public ItemStack getPaymentStack(){
		return this.inventorySmith.getPayment();
	}

	/** クライアント側*/
	public List<UnsagaMaterial> getForgeableMaterials(){
		List<UnsagaMaterial> list = Lists.newArrayList();
//		UnsagaMod.logger.trace("fff", this.isValidBaseItem(),this.isValidSubItem());
		if(this.isValidBaseItem() && this.isValidSubItem()){
//			list.add(this.getMaterialOrNot(getBaseStack()).get());
			list.addAll(ForgingProcess.getForgeableMaterials(this.getMaterialOrNot(getBaseStack()).get(),this.getMaterialOrNot(getSubStack()).get()));
		}
		return list;
	}

	public void setCurrentCategory(byte cate){
		this.currentCategory = cate;
	}
	public Pair<Integer,Integer> getRepairAmount(){
		if(this.isValidBaseItem() && this.isValidSubItem()){
			int a =  ForgingProcess.getDurability(this.getMaterialOrNot(getBaseStack()).get(), this.getBaseStack());
			int b = ForgingProcess.getRepairAmount(this.getMaterialOrNot(getBaseStack()).get(), this.getMaterialOrNot(this.getSubStack()).get(), this.getSubStack());
			return Pair.of(a, b);
		}
		return Pair.of(0, 0);
	}


	@Override
	public PacketSendGuiInfoToClient getSyncPacketToClient(EntityPlayer ep){
		NBTTagCompound comp = UtilNBT.compound();
		comp.setByte("smithType", (byte)this.type.getMeta());
//		comp.setBoolean("canForge", this.isForgeable);
//		if(this.process.isPresent()){
//			if(this.process.get().getForgeableMaterials().isEmpty()){
//				comp.setString("material", this.process.get().getBaseMaterial().getKey().getResourcePath());
//			}else{
//				List<UnsagaMaterial> list = this.process.get().getForgeableMaterials();
//				Collections.shuffle(list);
//				comp.setString("material", list.get(0).getKey().getResourcePath());
//			}
//			comp.setInteger("repair", this.process.get().getRepair());
//		}
		return PacketSendGuiInfoToClient.create(comp);
	}

	@Override
	public void onSlotClick(int rawSlotNumber,int containerSlotNumber,int clickButton,ClickType clickTypeIn,EntityPlayer ep){
		super.onSlotClick(rawSlotNumber, containerSlotNumber, clickButton, clickTypeIn, ep);


//		this.isForgeable = this.isValidBaseItem() && this.isValidSubItem();
//
		if(this.isValidBaseItem() && this.isValidSubItem()){
			this.process = Optional.of(this.factory.create(this.getMaterialOrNot(this.inventorySmith.getBaseItem()).get(), this.getMaterialOrNot(this.inventorySmith.getMaterial()).get()
					, this.inventorySmith.getBaseItem(), this.inventorySmith.getMaterial()));
		}else{
			this.process = Optional.empty();
		}

//		if(this.ep instanceof EntityPlayerMP){
//			HSLib.core().getPacketDispatcher().sendTo(this.getSyncPacketToClient(ep), (EntityPlayerMP) ep);
//		}
//

	}
	@Override
    public ItemStack transferStackInSlot(Slot slot)
    {
		InventoryHandler hinvEp = InventoryHandler.create(this.invPlayer);
		InventoryHandler hinvSmith = InventoryHandler.create(this.inventorySmith);

		if(slot instanceof SlotSmith.SlotMaterial || slot instanceof SlotSmith.SlotPayment){
			if(slot.getStack()!=null && hinvEp.getFirstMergeableOrEmptySlot(slot.getStack()).isPresent()){
				hinvEp.merge(hinvEp.getFirstMergeableOrEmptySlot(slot.getStack()).getAsInt(), slot);
			}
		}
        return super.transferStackInSlot(slot);
    }
}
