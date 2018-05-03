package mods.hinasch.unsaga;

import java.security.SecureRandom;

import mods.hinasch.lib.capability.CapabilityAdapterFactory;
import mods.hinasch.lib.client.IGuiAttribute;
import mods.hinasch.lib.core.HSGuis;
import mods.hinasch.lib.core.HSLib;
import mods.hinasch.lib.iface.IModBase;
import mods.hinasch.lib.misc.LogWrapper;
import mods.hinasch.lib.network.PacketSyncCapability;
import mods.hinasch.unsaga.ability.AbilityRegistry;
import mods.hinasch.unsaga.ability.specialmove.SparklingPointRegistry;
import mods.hinasch.unsaga.core.advancement.UnsagaTriggerRegisterer;
import mods.hinasch.unsaga.core.command.CommandSetAbility;
import mods.hinasch.unsaga.core.command.CommandTestDebuff;
import mods.hinasch.unsaga.core.command.CommandUnsaga;
import mods.hinasch.unsaga.core.entity.UnsagaEntityRegistry;
import mods.hinasch.unsaga.core.item.UnsagaCreativeTabs;
import mods.hinasch.unsaga.core.net.CommonProxy;
import mods.hinasch.unsaga.core.net.packet.PacketAddExhaution;
import mods.hinasch.unsaga.core.net.packet.PacketClientScanner;
import mods.hinasch.unsaga.core.net.packet.PacketClientThunder;
import mods.hinasch.unsaga.core.net.packet.PacketDebugPos;
import mods.hinasch.unsaga.core.net.packet.PacketGuiButtonUnsaga;
import mods.hinasch.unsaga.core.net.packet.PacketLP;
import mods.hinasch.unsaga.core.net.packet.PacketLP.PacketLPHandler;
import mods.hinasch.unsaga.core.net.packet.PacketSyncActionPerform;
import mods.hinasch.unsaga.core.net.packet.PacketSyncFieldChest;
import mods.hinasch.unsaga.core.net.packet.PacketSyncServerTargetHolder;
import mods.hinasch.unsaga.core.net.packet.PacketSyncSkillPanel;
import mods.hinasch.unsaga.core.potion.UnsagaPotions;
import mods.hinasch.unsaga.core.world.WorldGeneratorUnsaga;
import mods.hinasch.unsaga.element.BiomeElements;
import mods.hinasch.unsaga.init.BlockOrePropertyRegistry;
import mods.hinasch.unsaga.init.UnsagaBlockRegistry;
import mods.hinasch.unsaga.init.UnsagaCapabilities;
import mods.hinasch.unsaga.init.UnsagaConfigHandler;
import mods.hinasch.unsaga.init.UnsagaGui;
import mods.hinasch.unsaga.init.UnsagaItemRegistry;
import mods.hinasch.unsaga.init.UnsagaItems;
import mods.hinasch.unsaga.init.UnsagaLibrary;
import mods.hinasch.unsaga.material.MaterialItemAssociatings;
import mods.hinasch.unsaga.material.RawMaterialRegistry;
import mods.hinasch.unsaga.material.UnsagaMaterialRegistry;
import mods.hinasch.unsaga.material.UnsagaMaterials;
import mods.hinasch.unsaga.minsaga.MinsagaForging;
import mods.hinasch.unsaga.plugin.UnsagaPluginIntegration;
import mods.hinasch.unsaga.skillpanel.SkillPanelRegistry;
import mods.hinasch.unsaga.skillpanel.SkillPanels;
import mods.hinasch.unsaga.util.ToolCategory;
import mods.hinasch.unsaga.villager.UnsagaVillagers;
import mods.hinasch.unsagamagic.UnsagaMagic;
import net.minecraft.command.ICommandManager;
import net.minecraft.command.ServerCommandManager;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;

@Mod(modid = UnsagaMod.MODID  ,name = UnsagaMod.NAME , version= UnsagaMod.VERSION,dependencies="required-after:hinasch.lib",guiFactory="mods.hinasch.unsaga.core.client.ModGuiFactoryUnsaga")
public class UnsagaMod implements IModBase{


	@SidedProxy(modId= UnsagaMod.MODID,clientSide = "mods.hinasch.unsaga.core.client.ClientProxy", serverSide = "mods.hinasch.unsaga.core.net.CommonProxy")
	public static CommonProxy proxy;
	@Instance(UnsagaMod.MODID)
	public static UnsagaMod instance;
	public static final String MODID = "hinasch.unsaga";
	public static final String NAME = "Unsaga Mod";
	public static final String VERSION = "1.12.2-0.0.0.1";

	public static Configuration configFile;
	public static final UnsagaConfigHandler configs = new UnsagaConfigHandler();

	public static SecureRandom secureRandom = new SecureRandom();
	public static LogWrapper logger = LogWrapper.newLogger(MODID);


	public static final CapabilityAdapterFactory capabilityAdapterFactory = new CapabilityAdapterFactory(UnsagaMod.MODID);
	public static final SimpleNetworkWrapper packetDispatcher = NetworkRegistry.INSTANCE.newSimpleChannel(MODID);

	public static UnsagaPluginIntegration pluginHandler = new UnsagaPluginIntegration();

	@EventHandler
	public void registerCommands(FMLServerStartingEvent  e){
		MinecraftServer server = e.getServer().getServer();
		ICommandManager manager = server.getCommandManager();
		ServerCommandManager smanager = (ServerCommandManager)manager;
		smanager.registerCommand(new CommandSetAbility());
		smanager.registerCommand(new CommandTestDebuff());
		smanager.registerCommand(new CommandUnsaga());
		if(HSLib.configHandler.isDebug()){
//			smanager.registerCommand(new CommandCheckRegisteredEvents());
		}
	}

	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{

		this.configFile = new Configuration(event.getSuggestedConfigurationFile());

		this.configFile.load();
		configs.setConfigFile(configFile);
		configs.init();
		configs.syncConfig();

		this.preInitGameRegistration();
		proxy.registerEntityRenderers();
//		core.preInit();
//
//		proxy.registerEntityRenderers();
//
//		configHandler.checkValidation();
		this.configFile.save();

//		ModelLoaderRegistry.registerLoader(ModelLoaderUnsaga.INSTANCE);
//		HSLibs.registerEvent(new EventModelBake());
	}

	private void preInitGameRegistration(){
		UnsagaCapabilities.registerCapabilities();
		UnsagaMaterialRegistry.instance().preInit();
		RawMaterialRegistry.instance().preInit();
		AbilityRegistry.instance().preInit();
		BlockOrePropertyRegistry.instance().preInit();
		UnsagaItemRegistry.instance().register();
		UnsagaBlockRegistry.instance().register();
		UnsagaVillagers.instance().preInit();
		UnsagaEntityRegistry.instance().preInit();
		UnsagaMagic.instance().preInit();
		SkillPanelRegistry.instance().preInit();
		UnsagaPotions.instance().preInit();
		BiomeElements.register();
//		StateRegistry.instance().preInit();

	}

	private void initGameRegistration(){
		UnsagaItemRegistry.instance().init();
		UnsagaMaterialRegistry.instance().init();
		AbilityRegistry.instance().init();
		UnsagaCapabilities.registerCapabilityAttachEvents();
		UnsagaModEvents.instance().regiser();
		UnsagaLibrary.instance().init();

		UnsagaPotions.registerEvent();
		BlockOrePropertyRegistry.instance().init();
		UnsagaBlockRegistry.instance().registerRecipesAndOres();
		RawMaterialRegistry.instance().init();
		MaterialItemAssociatings.instance().init();
		UnsagaVillagers.instance().init();
		ToolCategory.registerAssociation();
		SparklingPointRegistry.instance().register();
		MinsagaForging.instance().init();

		UnsagaMagic.instance().init();

		UnsagaEntityRegistry.instance().init();
		SkillPanelRegistry.instance().init();
		GameRegistry.registerWorldGenerator(WorldGeneratorUnsaga.instance(), 8);
		UnsagaTriggerRegisterer.register();


		BiomeElements.registerBiomes();

		UnsagaCreativeTabs.TOOLS.setIconItemStack(UnsagaItemRegistry.createStack(UnsagaItems.AXE,UnsagaMaterials.DAMASCUS, 0));
		UnsagaCreativeTabs.PANEL_GROWTH.setIconItemStack(SkillPanels.LOCKSMITH.getItemStack(1));
		UnsagaCreativeTabs.MISC.setIconItemStack(RawMaterialRegistry.instance().carnelian.getItemStack(1));
	}

	@EventHandler
	public void init(FMLInitializationEvent e)
	{

		this.initGameRegistration();

		proxy.registerKeyHandlers();
		proxy.registerRenderers();

		proxy.registerEvents();
//
//
//		core.init(e);
//		//HSLib.eventLivingHurt.getEventsPre().sort(null);
//
//		packetDispatcher.registerMessage(PacketSkillHandler.class, PacketSkill.class, 1, Side.SERVER);
		packetDispatcher.registerMessage(PacketLPHandler.class, PacketLP.class, 1, Side.CLIENT);
		packetDispatcher.registerMessage(PacketSyncServerTargetHolder.Handler.class, PacketSyncServerTargetHolder.class, 2, Side.SERVER);
		packetDispatcher.registerMessage(PacketGuiButtonUnsaga.Handler.class, PacketGuiButtonUnsaga.class, 3, Side.SERVER);
		packetDispatcher.registerMessage(PacketSyncCapability.Handler.class, PacketSyncCapability.class, 4, Side.CLIENT);
		packetDispatcher.registerMessage(PacketLPHandler.class, PacketLP.class, 5, Side.SERVER);
		packetDispatcher.registerMessage(PacketSyncSkillPanel.Handler.class, PacketSyncSkillPanel.class, 6, Side.CLIENT);
		packetDispatcher.registerMessage(PacketDebugPos.Handler.class, PacketDebugPos.class, 7, Side.CLIENT);
		packetDispatcher.registerMessage(PacketClientScanner.Handler.class, PacketClientScanner.class, 8, Side.CLIENT);
		packetDispatcher.registerMessage(PacketClientThunder.Handler.class, PacketClientThunder.class, 9, Side.CLIENT);
		packetDispatcher.registerMessage(PacketAddExhaution.Handler.class, PacketAddExhaution.class, 10, Side.SERVER);
		packetDispatcher.registerMessage(PacketSyncActionPerform.Handler.class, PacketSyncActionPerform.class, 11, Side.SERVER);
		packetDispatcher.registerMessage(PacketSyncFieldChest.Handler.class, PacketSyncFieldChest.class, 12, Side.CLIENT);
		packetDispatcher.registerMessage(PacketSyncActionPerform.Handler.class, PacketSyncActionPerform.class, 13, Side.CLIENT);
//		packetDispatcher.registerMessage(PacketSyncAbiltyHeldItem.Handler.class, PacketSyncAbiltyHeldItem.class, 3, Side.CLIENT);

////		packetDispatcher.registerMessage(PacketSyncEntityInfo.Handler.class, PacketSyncEntityInfo.class, 5, Side.CLIENT);
////		packetDispatcher.registerMessage(PacketSyncExp.Handler.class, PacketSyncExp.class, 5, Side.CLIENT);
//
//		packetDispatcher.registerMessage(PacketSyncSkillPanel.Handler.class, PacketSyncSkillPanel.class, 6, Side.CLIENT);
//		packetDispatcher.registerMessage(PacketSyncGui.Handler.class, PacketSyncGui.class, 7, Side.SERVER);
//		packetDispatcher.registerMessage(PacketSyncGui.Handler.class, PacketSyncGui.class, 8, Side.CLIENT);

//		packetDispatcher.registerMessage(PacketOpenGui.Handler.class, PacketOpenGui.class, 10, Side.SERVER);

//		packetDispatcher.registerMessage(PacketClientScanner.Handler.class, PacketClientScanner.class, 12, Side.CLIENT);
//		packetDispatcher.registerMessage(PacketChangeGuiMessage.Handler.class, PacketChangeGuiMessage.class, 13, Side.CLIENT);
//		packetDispatcher.registerMessage(PacketSyncSkillPanel.Handler.class, PacketSyncSkillPanel.class, 14, Side.SERVER);
//		packetDispatcher.registerMessage(PacketEntityInteractionWithItem.Handler.class, PacketEntityInteractionWithItem.class, 15, Side.SERVER);



//		PacketSyncCapability.syncCapabilityMap.put(XPHelper.DefaultImpl.ID_SYNC, XPHelper.CAPA);
//		PacketSyncCapability.syncCapabilityMap.put(DefaultIUnsagaPropertyItem.ID_SYNC, UnsagaCapability.unsagaPropertyItem());
//

		NetworkRegistry.INSTANCE.registerGuiHandler(UnsagaMod.instance, proxy);

		HSLib.registerGuiPacketHandler(this.getModGuiID(), this);

		pluginHandler.checkLoadedModsInit();
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event){

		pluginHandler.checkLoadedMods();


	}


	@Override
	public Class<? extends IGuiAttribute> getGuiClass() {
		// TODO 自動生成されたメソッド・スタブ
		return UnsagaGui.Type.class;
	}

	@Override
	public Object getModInstance() {
		// TODO 自動生成されたメソッド・スタブ
		return this.instance;
	}

	@Override
	public int getModGuiID() {
		// TODO 自動生成されたメソッド・スタブ
		return HSGuis.UNSAGA;
	}

}
