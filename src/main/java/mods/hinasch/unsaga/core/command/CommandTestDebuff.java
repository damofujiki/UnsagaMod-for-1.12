package mods.hinasch.unsaga.core.command;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

public class CommandTestDebuff extends CommandBase{

	@Override
	public String getName() {
		// TODO 自動生成されたメソッド・スタブ
		return "testdebuff";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		// TODO 自動生成されたメソッド・スタブ
		return "/testdebuff <Debuff ID> <Debuff Time>";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
//		if(args.length<1){
//			throw new NumberInvalidException("commands.generic.num.invalid", new Object[] {});
//		}
//		String id = args[0];
//		PotionUnsaga potion = UnsagaRegistries.POTIONS.get(id);
//		if(potion!=null){
//			if(sender instanceof EntityPlayer){
//				EntityPlayer ep = (EntityPlayer) sender;
//				if(potion==UnsagaPotions.FEAR){
//					ep.addPotionEffect(new PotionEffectFear(UnsagaPotions.instance().FEAR,ItemUtil.getPotionTime(10),0));
//				}else{
//					ep.addPotionEffect(potion.getPotionType().getEffects().get(0));
//				}
//
//				this.notifyCommandListener(sender, this, "command.testdebuff.success", new Object[]{potion.getPropertyName()});
//			}
//		}else{
//			UnsagaMod.logger.trace("potion","ポーションが見つからない");
//		}
//
//
//
	}


}
