package com.djcri.shieldfix;

import java.awt.Desktop.Action;
import java.awt.event.ItemEvent;

import org.jline.utils.Log;

import com.djcri.shieldfix.util.Reflector;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;


public class ShieldFixer {
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void shieldFix(LivingAttackEvent event)
	{
		EntityLivingBase entity=event.getEntityLiving();
		DamageSource damageSourceIn=event.getSource();

        if (!damageSourceIn.isUnblockable() && entity.isHandActive() && !entity.getActiveItemStack().isEmpty())
        {
            Item item = entity.getActiveItemStack().getItem();

            if (item.getItemUseAction(entity.getActiveItemStack()) == EnumAction.BLOCK)
            {
            	if(item.getMaxItemUseDuration(entity.getActiveItemStack()) - entity.getItemInUseCount() < 5)
            	{
            		//entity.activeItemStackUseCount = item.getMaxItemUseDuration(entity.getActiveItemStack()) - 5;
            		new Reflector<>(entity, EntityLivingBase.class, "activeItemStackUseCount", "field_184628_bn", item.getMaxItemUseDuration(entity.getActiveItemStack()) - 5).setField();
            	}
            }
        }
	}
}
