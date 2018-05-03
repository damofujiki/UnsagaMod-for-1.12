package mods.hinasch.unsaga.core.inventory.container;

import java.util.List;
import java.util.UUID;

import mods.hinasch.lib.container.ContainerBase;
import mods.hinasch.lib.network.PacketGuiButtonBaseNew;
import mods.hinasch.lib.util.Statics;
import mods.hinasch.lib.world.WorldHelper;
import mods.hinasch.lib.world.XYZPos;
import mods.hinasch.unsaga.UnsagaMod;
import mods.hinasch.unsaga.common.tool.ComponentUnsagaWeapon;
import mods.hinasch.unsaga.core.client.gui.GuiToolCustomizeMinsaga;
import mods.hinasch.unsaga.core.inventory.InventorySmithMinsaga;
import mods.hinasch.unsaga.core.inventory.slot.SlotMinsagaSmith;
import mods.hinasch.unsaga.core.net.packet.PacketGuiButtonUnsaga;
import mods.hinasch.unsaga.init.UnsagaGui;
import mods.hinasch.unsaga.material.UnsagaMaterialCapability;
import mods.hinasch.unsaga.minsaga.MinsagaForgeCapability;
import mods.hinasch.unsaga.minsaga.MinsagaForgeCapability.IMinsagaForge;
import mods.hinasch.unsaga.minsaga.MinsagaForging;
import mods.hinasch.unsaga.minsaga.MinsagaMaterial;
import net.minecraft.block.BlockAnvil;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;

public class ContainerToolCustomizeMinsaga extends ContainerBase{


    protected static final UUID UUID_MINSAGA = UUID.fromString("848258b3-a37c-4619-9b2b-b9e94526c906");

	protected InventorySmithMinsaga inventorySmith;
	public ContainerToolCustomizeMinsaga(EntityPlayer ep) {
		super(ep, new InventorySmithMinsaga());
		this.inventorySmith = (InventorySmithMinsaga) this.inv;

		UnsagaMod.logger.trace(this.getClass().getName(), "called");
//		this.addSlotToContainer(new SlotSmith(this.inventorySmith, inventorySmith, 28, 53)); //Emerald
		this.addSlotToContainer(new SlotMinsagaSmith.Base(this.inv, inventorySmith.INV_BASE, 28, 53-(18*2))); //Base Material
		this.addSlotToContainer(new SlotMinsagaSmith.Material(this.inv,inventorySmith.INV_MATERIAL, 28+(18*2)-8, 53-(18*2))); //Material2
		this.addSlotToContainer(new SlotMinsagaSmith.Forged(this.inv, inventorySmith.INV_FORGED, 28+(18*6)+1, 52));
	}

	@Override
	public PacketGuiButtonBaseNew getPacketGuiButton(int guiID, int buttonID, NBTTagCompound args) {
		// TODO 自動生成されたメソッド・スタブ
		return PacketGuiButtonUnsaga.create(UnsagaGui.Type.SMITH_MINSAGA,buttonID,args);
	}

	@Override
	public SimpleNetworkWrapper getPacketPipeline() {
		// TODO 自動生成されたメソッド・スタブ
		return UnsagaMod.packetDispatcher;
	}

	@Override
	public int getGuiID() {
		// TODO 自動生成されたメソッド・スタブ
		return UnsagaGui.Type.SMITH_MINSAGA.getMeta();
	}

	@Override
	public void onPacketData() {
//		UnsagaMod.logger.trace("id", this.buttonID);
		if(this.buttonID==GuiToolCustomizeMinsaga.ID_FORGE){

			List<XYZPos> list = WorldHelper.findNear(ep.world, XYZPos.createFrom(ep), 5, 3, (w,p,scanner)->scanner.getBlock()==Blocks.ANVIL);
			if(list.isEmpty()){
				return;
			}
			XYZPos pos = list.get(0);
			IBlockState anvil = ep.getEntityWorld().getBlockState(pos);

			if(this.hasBaseTool() && this.hasMaterialStack() && this.hasExp(ep)){
				ItemStack newTool = this.inventorySmith.getBaseItem().copy();
				ItemStack materialStack = this.inventorySmith.getMaterial();
				if(MinsagaForgeCapability.ADAPTER.hasCapability(newTool) && MinsagaForging.instance().getMaterialFromItemStack(materialStack).isPresent()){
					MinsagaMaterial material = MinsagaForging.instance().getMaterialFromItemStack(materialStack).get();
					MinsagaForgeCapability.ADAPTER.getCapability(newTool).addMaterialLayer(material);
					this.inventorySmith.setForged(newTool);
					this.inventorySmith.consumerItems();
					this.consumeExp(ep);
					this.updateWeaponModifier(newTool);
					this.updateAnvilState(ep.world, ep, pos, anvil);
				}
			}
		}
	}

	public void updateWeaponModifier(ItemStack forged){

		if(MinsagaForgeCapability.ADAPTER.hasCapability(forged)){
			IMinsagaForge capa = MinsagaForgeCapability.ADAPTER.getCapability(forged);
//			ModifierHelper.refleshModifier(forged, SharedMonsterAttributes.ATTACK_DAMAGE, this.getModifier("Weapon", capa.getAttackModifier()), EntityEquipmentSlot.MAINHAND);
//			if(forged.getItem() instanceof ItemArmor){
//				ItemArmor armor = (ItemArmor) forged.getItem();
//				ModifierHelper.refleshModifier(forged, SharedMonsterAttributes.ARMOR, this.getModifier("Armor", capa.getAttackModifier()), armor.getEquipmentSlot());
//			}

			int weight = 0;
			if(UnsagaMaterialCapability.adapter.hasCapability(forged)){
				weight = UnsagaMaterialCapability.adapter.getCapability(forged).getWeight();
			}
			weight += capa.getWeightModifier();
			ComponentUnsagaWeapon.refleshWeightModifier(forged, weight);
		}
	}

	public AttributeModifier getModifier(String type,float f){
		return new AttributeModifier(UUID_MINSAGA,"Minsaga Modifier"+type,f,Statics.OPERATION_INCREMENT);
	}


	public void updateAnvilState(World worldIn,EntityPlayer playerIn,BlockPos blockPosIn,IBlockState iblockstate){
		float breakChance = 0.13F;
        if (!playerIn.capabilities.isCreativeMode && !worldIn.isRemote && iblockstate.getBlock() == Blocks.ANVIL && playerIn.getRNG().nextFloat() < breakChance)
        {
            int l = ((Integer)iblockstate.getValue(BlockAnvil.DAMAGE)).intValue();
            ++l;

            if (l > 2)
            {
                worldIn.setBlockToAir(blockPosIn);
                worldIn.playEvent(1029, blockPosIn, 0);
            }
            else
            {
                worldIn.setBlockState(blockPosIn, iblockstate.withProperty(BlockAnvil.DAMAGE, Integer.valueOf(l)), 2);
                worldIn.playEvent(1030, blockPosIn, 0);
            }
        }
        else if (!worldIn.isRemote)
        {
            worldIn.playEvent(1030, blockPosIn, 0);
        }
	}
	public boolean hasBaseTool(){

		return this.inventorySmith.getBaseItem()!=null && this.inventorySmith.getBaseItem().getItem().isRepairable();
	}

	public boolean hasMaterialStack(){
		return this.inventorySmith.getMaterial()!=null && MinsagaForging.instance().getMaterialFromItemStack(this.inventorySmith.getMaterial())!=null;
	}

	public void consumeExp(EntityPlayer ep){
		if(!ep.capabilities.isCreativeMode){
			ep.experienceLevel -= 5;
		}
	}

	public boolean hasExp(EntityPlayer ep){
		if(ep.capabilities.isCreativeMode){
			return true;
		}
		if(ep.experienceLevel>5){
			return true;
		}
		return false;
	}
}
