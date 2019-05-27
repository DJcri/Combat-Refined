package com.djcri.shieldfix.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;

import org.apache.logging.log4j.LogManager;

import net.minecraft.launchwrapper.Launch;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

public class Reflector<T> {
	private Object owner;
	private Class<?> ownerClass;
	private String name;
	private String nameObf;
	private Object[] params;

	public Reflector(Object owner, Class<?> ownerClass, String name, String nameObf, Object... params)
	{
		this.owner = owner;
		this.ownerClass = ownerClass;
		this.name = name;
		this.nameObf = nameObf;
		this.params = params;
	}

	@SuppressWarnings("unchecked")
	public T invokeMethod()
	{
		Method method = null;
		Class<?>[] paramClasses = paramClasses(params);
		try
		{
			method = ReflectionHelper.findMethod(ownerClass, name, nameObf, paramClasses);
		}
		catch(final ReflectionHelper.UnableToFindMethodException exception)
		{
			throwError(exception, "Can't find method: " + ownerClass.getName() + '#' + name + ' ' + nameObf + '(' + Arrays.toString(paramClasses) + ')');
		}
		
		T value = null;
		try
		{
			value = (T) method.invoke(owner, params);
		}
		catch(final Exception exception)
		{
			throwError(exception, "Can't invoke method: " + ownerClass.getName() + '#' + name + '(' + Arrays.toString(paramClasses) + ')');
		}
		return value;
	}
	
	public Class<?>[] paramClasses(Object... params)
	{
		Class<?>[] classes = new Class<?>[params.length];
		for(int p = 0; p < params.length; p++)
		{
			classes[p] = params[p].getClass();
		}
		return classes;
	}
	
	public Field findField()
	{
		try
		{
			return ReflectionHelper.findField(ownerClass, (boolean)Launch.blackboard.get("fml.deobfuscatedEnvironment")?name:nameObf);
		}
		catch(final RuntimeException exception)
		{
			throwError(exception, "Can't find field: " + ownerClass.getName() + '#' + name);
		}
		return null;
	}

	public Field setField()
	{
		Field field = findField();
		try
		{
			field.set(owner, params[0]);
		}
		catch(final IllegalAccessException exception)
		{
			throwError(exception, "Can't set field: " + ownerClass.getName() + '#' + name);
		}
		return field;
	}
	
	@SuppressWarnings("unchecked")
	public T getField()
	{
		Field field = findField();
		T value = null;
		try
		{
			value = (T) field.get(owner);
		}
		catch(final IllegalAccessException exception)
		{
			throwError(exception, "Can't get field: " + ownerClass.getName() + '#' + name);
		}
		return value;
	}
	
	public void throwError(Exception e, String msg) {
		LogManager.getLogger("REFLECTOR").error(msg);
		throw new RuntimeException(e);
	}
}
