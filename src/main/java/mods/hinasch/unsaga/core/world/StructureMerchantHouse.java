package mods.hinasch.unsaga.core.world;

import mods.hinasch.unsaga.UnsagaMod;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Mirror;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraft.world.gen.structure.template.Template;
import net.minecraft.world.gen.structure.template.TemplateManager;

public class StructureMerchantHouse {

	public void build(World world,BlockPos pos){
		WorldServer worldServer = (WorldServer)world;
		MinecraftServer minecraftServer = worldServer.getMinecraftServer();
		TemplateManager templateManager = worldServer.getStructureTemplateManager();
		ResourceLocation res = new ResourceLocation(UnsagaMod.MODID,"merchanthouse");
		Template template = templateManager.getTemplate(minecraftServer, res);
		UnsagaMod.logger.splitter(pos.toString());
		if(template!=null){
			PlacementSettings placementSettings = (new PlacementSettings()).setMirror(Mirror.NONE)
					.setRotation(Rotation.NONE).setIgnoreEntities(false).setChunk(null)
					.setReplacedBlock(null).setIgnoreStructureBlock(false);
			UnsagaMod.logger.splitter("1");
			template.addBlocksToWorld(world, pos, placementSettings);
			UnsagaMod.logger.splitter("2");
		}
	}
}
