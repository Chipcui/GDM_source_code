package org.gobiiproject.gobiiprocess.machine;

import org.apache.commons.lang3.StringUtils;
import org.gobiiproject.gobiiprocess.machine.builder.Component;
import org.gobiiproject.gobiiprocess.machine.components.*;
import org.gobiiproject.gobiiprocess.machine.exceptions.DependencyException;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Util {

	public static <A extends T,B extends T, T> B mapSubTypes(A a, Class<B> bClass)
			throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
		Class<?> t = mostSpecificCommonSuperclass(a.getClass(), bClass);

		B b = bClass.newInstance();

		for (Field f : t.getDeclaredFields()) {
			setField(b, f, getField(a, f));
		}

		return b;
	}

	public static Object getField(Object object, Field field) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
		return object.getClass().getMethod("get" + StringUtils.capitalize(field.getName())).invoke(object);
	}

	public static Object setField(Object object, Field field, Object value)
			throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
		String fieldName = field.getName();
		String setterName = "set" + StringUtils.capitalize(fieldName);

		Object castedValue = field.getType().cast(value);
		Method m = object.getClass().getMethod(setterName, field.getType());
		m.invoke(object, castedValue);

		return object;
	}

	static Class<?> mostSpecificCommonSuperclass(Class<?> a, Class<?> b) {
		Class<?> s = a;
		while (!s.isAssignableFrom(b)) {
			s = s.getSuperclass();
		}
		return s;
	}

}
