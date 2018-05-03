package mods.hinasch.unsaga.common.specialaction;

import java.util.function.Function;

import mods.hinasch.lib.world.WorldHelper;
import mods.hinasch.lib.world.XYZPos;
import mods.hinasch.unsaga.common.specialaction.ActionBase.IAction;
import net.minecraft.entity.Entity;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.math.BlockPos;

public class ActionSummon<T extends IActionPerformer> implements IAction<T>{

	Function<T,Entity> summonFunction;
	public ActionSummon(Function<T,Entity> func){
		this.summonFunction = func;
	}
	@Override
	public EnumActionResult apply(T context) {
		BlockPos pos = context.getPerformer().getPosition();
		if(context.getTargetCoordinate().isPresent()){
			pos = (BlockPos) context.getTargetCoordinate().get();
		}
		Entity ent = this.summonFunction.apply(context);

		if(ent!=null){
			context.playSound(new XYZPos(pos), SoundEvents.ENTITY_ENDERMEN_TELEPORT, false);
			ent.setPosition(pos.getX(),pos.getY() + 1,pos.getZ());
			WorldHelper.safeSpawn(context.getWorld(), ent);
			return EnumActionResult.SUCCESS;
		}

		return EnumActionResult.PASS;
	}


}
