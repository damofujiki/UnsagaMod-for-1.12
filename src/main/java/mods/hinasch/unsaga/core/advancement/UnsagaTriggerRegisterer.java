package mods.hinasch.unsaga.core.advancement;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.advancements.ICriterionTrigger;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

public class UnsagaTriggerRegisterer {

	public static void register(){
		Method method;
		try {
		 method = ReflectionHelper.findMethod(CriteriaTriggers.class, "register", "func_192118_a", ICriterionTrigger.class);
		 method.setAccessible(true);
		for (int i=0; i < UnsagaTriggers.TRIGGER_ARRAY.length; i++)
		{
		     method.invoke(null, UnsagaTriggers.TRIGGER_ARRAY[i]);
		} } catch (SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}
	}
}
