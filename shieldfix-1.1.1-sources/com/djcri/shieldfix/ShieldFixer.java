package com.djcri.shieldfix;

import java.awt.Desktop.Action;
import java.awt.event.ItemEvent;

import org.jline.utils.Log;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.event.entity.PlaySoundAtEntityEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;


public class ShieldFixer {
	@SubscribeEvent
	public void shieldFix(LivingHurtEvent event)
	{
		EntityLivingBase entity=event.getEntityLiving();
		DamageSource damageSourceIn=event.getSource();
		float amount=event.getAmount();
        if (!damageSourceIn.func_76363_c() && entity.func_184587_cr() && !entity.func_184607_cu().func_190926_b())
        {
        	Item item = entity.func_184607_cu().func_77973_b();
            Vec3d vec3d = damageSourceIn.func_188404_v();

            if (vec3d != null && item.func_77661_b(entity.func_184607_cu()) == EnumAction.BLOCK && item.func_77626_a(entity.func_184607_cu()) - entity.func_184605_cv() >= 0)
            {
                Vec3d vec3d1 = entity.func_70676_i(1.0F);
                Vec3d vec3d2 = vec3d.func_72444_a(new Vec3d(entity.field_70165_t, entity.field_70163_u, entity.field_70161_v)).func_72432_b();
                vec3d2 = new Vec3d(vec3d2.field_72450_a, 0.0D, vec3d2.field_72449_c);

                if (vec3d2.func_72430_b(vec3d1) < 0.0D)
                {
                    event.setAmount(0.0F);;

                    if (!damageSourceIn.func_76352_a())
                    {
                        EntityLivingBase entitySource =(EntityLivingBase)damageSourceIn.func_76364_f();

                        if (entity instanceof EntityLivingBase)
                        {
                        	entitySource.func_70653_a(entity, 0.5F, entity.field_70165_t - entitySource.field_70165_t, entity.field_70161_v - entitySource.field_70161_v);
                        }
                    }
                }
            }
        }
    }
}
