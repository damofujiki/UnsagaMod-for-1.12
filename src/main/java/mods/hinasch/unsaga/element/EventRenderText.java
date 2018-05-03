package mods.hinasch.unsaga.element;


import java.util.Optional;

import mods.hinasch.lib.client.ClientHelper;
import mods.hinasch.unsaga.UnsagaMod;
import mods.hinasch.unsaga.core.item.misc.ItemElementChecker;
import mods.hinasch.unsaga.element.newele.ElementAssociationLibrary;
import mods.hinasch.unsaga.element.newele.ElementTable;
import mods.hinasch.unsaga.init.UnsagaLibrary;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.gui.Gui;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EventRenderText extends Gui{


	public static final ResourceLocation ELEMENT_ICONS = new ResourceLocation(UnsagaMod.MODID,"textures/gui/hud_elements.png");
	int offsetx = 5;
	int offsety = 5;
	UnsagaLibrary lib = UnsagaLibrary.instance();
	@SubscribeEvent
	public void onTextDraw(RenderGameOverlayEvent.Post e){
        int top = 2;

        if(e.getType()==ElementType.TEXT){
        	ItemStack is = ClientHelper.getPlayer().getHeldItemMainhand();
        	if(is!=null && is.getItem() instanceof ItemElementChecker){
        		RayTraceResult ray = ClientHelper.getMouseOver();
        		if(ray!=null && ray.typeOfHit == RayTraceResult.Type.BLOCK){
        			BlockPos pos = ray.getBlockPos();
        			IBlockState state = ClientHelper.getWorld().getBlockState(pos);
        			Optional<ElementTable> tableOpt = ElementAssociationLibrary.instance().find(state);

        			if(state!=null && tableOpt.isPresent()){
        				ElementTable table = tableOpt.get();
                		ClientHelper.bindTextureToTextureManager(ELEMENT_ICONS);
            			int margin = 3;
                		for(FiveElements.Type type:FiveElements.VALUES){
                			int x =offsetx+ (16+margin)*type.getMeta();
                			this.drawTexturedModalRect(x, offsety, type.getMeta()*16, 0, 16,16);
                		}
                		for(FiveElements.Type type:FiveElements.VALUES){
                			int x =offsetx+ (16+margin)*type.getMeta();
                			float elm = table.get(type);
                			String str = String.valueOf(elm);
                			this.drawString(ClientHelper.fontRenderer(), str, x, offsety + 16 + 2, 0xffffff);
                		}
        			}
        		}


        	}
        }

	}
}
