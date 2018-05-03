package mods.hinasch.unsaga.core.client.gui;

import java.util.List;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;
import com.mojang.realmsclient.util.Pair;

import joptsimple.internal.Strings;
import mods.hinasch.lib.client.GuiContainerBase;
import mods.hinasch.lib.core.HSLib;
import mods.hinasch.lib.network.PacketSendGuiInfoToClient;
import mods.hinasch.lib.util.HSLibs;
import mods.hinasch.lib.util.UtilNBT;
import mods.hinasch.unsaga.UnsagaMod;
import mods.hinasch.unsaga.core.inventory.container.ContainerBlacksmithUnsaga;
import mods.hinasch.unsaga.util.ToolCategory;
import mods.hinasch.unsaga.util.UnsagaTextFormatting;
import mods.hinasch.unsaga.villager.smith.BlackSmithType;
import mods.hinasch.unsaga.villager.smith.ItemForgeFactory.ForgingProcess;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.IMerchant;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class GuiBlacksmithUnsaga extends GuiContainerBase{

	public final static int CATEGORY = -2;
	public final static int DOFORGE = -1;
	public final static int ICON_INFO = 4;
	public final static int ICON_INFO_RESULT = 5;
	protected ContainerBlacksmithUnsaga container;



	BlackSmithType type = BlackSmithType.NONE;


	ForgingProcess process;
//
//	boolean canForge = false;
//	UnsagaMaterial transformedMaterial = null;
//	int repair = 0;
	protected FontRenderer font = Minecraft.getMinecraft().fontRenderer;
	//	protected DebugUnsaga debug;
	protected byte currentCategory;
	boolean hasSync = false;
	public GuiBlacksmithUnsaga(IMerchant merchant,World world,EntityPlayer ep) {
		super(new ContainerBlacksmithUnsaga(merchant,world,ep));

		this.currentCategory = 0;

		this.container = (ContainerBlacksmithUnsaga) this.inventorySlots;
		// TODO 自動生成されたコンストラクター・スタブ
		//		ExtendedPlayerData data = ExtendedPlayerData.getData(ep);

		//		if(data.getMerchant().isPresent()){
		//
		//			this.villager = data.getMerchant().get();
		//			Unsaga.debug(this.villager,this.getClass());
		//		}

		this.addIcon(new Icon(ICON_INFO, -32, 0, 32, 168, true));
		this.addIcon(new Icon(ICON_INFO_RESULT, 100, 16, 48, 168, true));
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int par1,int par2)
	{
//		if(this.container.createForgingProcess().isPresent()){
//			UnsagaMod.logger.trace("smith", this.container.createForgingProcess());
//		}

		super.drawGuiContainerForegroundLayer(par1, par2);
		fontRenderer.drawString(ToolCategory.toolArray.get(this.currentCategory).toString(), 8, 42, 0x404040);
		//fontRenderer.drawString("Result:"+getSpellStr(), 8, (ySize - 96) + 2, 0x404040);
	}

	public int getCurrentCategory(){
		return (int)this.currentCategory;
	}
	@Override
	public String getGuiName(){
		return Strings.EMPTY;
	}

	@Override
	public List<String> getIconHoverText(Icon icon){
		List<String> list = Lists.newArrayList();
		if(icon.id==ICON_INFO){
			if(this.hasSync){
				list.add(HSLibs.translateKey("gui.unsaga.smith.type")+":"+this.type.getLocalized());
			}else{
				this.hasSync = true;
				HSLib.getPacketDispatcher().sendToServer(PacketSendGuiInfoToClient.request());
			}
		}
		if(icon.id==ICON_INFO_RESULT){
			if(!this.container.getForgeableMaterials().isEmpty()){
				String materials = this.container.getForgeableMaterials().stream().map(in -> in.getLocalized()).collect(Collectors.joining("/"));
				Pair<Integer,Integer> repair = this.container.getRepairAmount();
				list.add(HSLibs.translateKey("gui.unsaga.smith.forged.materials")+":"+materials);
				list.add(HSLibs.translateKey("gui.unsaga.smith.forged.durability")+":"+repair.first()+"+"+repair.second());
				if(this.container.hasSubMaterialByproductAbility()){
					list.add(HSLibs.translateKey("gui.unsaga.smith.forged.byproductAbility"));
				}
			}else{
				list.add(UnsagaTextFormatting.NEGATIVE+HSLibs.translateKey("gui.unsaga.smith.can.not.forge"));
			}
//			if(this.canForge){
//				list.add(this.transformedMaterial.getLocalized());
//				list.add("Reipair Durability:"+this.repair);
//			}else{
//				list.add(UnsagaTextFormatting.NEGATIVE+HSLibs.translateKey("gui.unsaga.smith.can.not.forge"));
//			}
		}

		return list;

	}
	@Override
	public String getGuiTextureName(){
		return UnsagaMod.MODID + ":textures/gui/container/smith.png";
	}

	@Override
	public NBTTagCompound getSendingArgs(){
		NBTTagCompound args = UtilNBT.compound();
		args.setByte("category", currentCategory);
		//		Byte[] args = new Byte[1];
		//		args[0] =(byte) this.getCurrentCategory();
		////		Unsaga.debug(args[0],this.getClass());
		return args;
	}

	@Override
	public void onPacketFromServer(NBTTagCompound message){
		int id =(int) message.getByte("smithType");
		this.type = BlackSmithType.fromMeta(id);
//		this.canForge = message.getBoolean("canForge");
//		if(this.canForge){
//			String key = message.getString("material");
//			this.transformedMaterial = UnsagaMaterials.instance().get(key);
//			this.repair = message.getInteger("repair");
//		}
//


	}
	@Override
	public void initGui()
	{
		super.initGui();
		int i = width  - xSize >> 1;
		int j = height - ySize >> 1;

		//		UnsagaMod.packetDispatcher.sendToServer(PacketSyncGui.create(Type.SMITH_PROFESSIONALITY	,null));
		// ボタンを追加
		// GuiButton(ボタンID, ボタンの始点X, ボタンの始点Y, ボタンの幅, ボタンの高さ, ボタンに表示する文字列)
		this.addButton(DOFORGE, i + (18*5)+2, j + 16 +(18*2), 30, 19 , "Forge!");
		this.addButton(CATEGORY, i + (18*3)+2, j + 16 +(18*2), 31, 19 , "Change");
	}

	@Override
	public void prePacket(GuiButton par1){
		if(par1.id==CATEGORY){
			this.currentCategory += 1;
			if(this.currentCategory>=ToolCategory.toolArray.size()){
				this.currentCategory = 0;
			}

			this.container.setCurrentCategory(this.currentCategory);
		}
	}



}
