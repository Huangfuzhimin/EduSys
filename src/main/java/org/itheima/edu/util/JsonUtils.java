package org.itheima.edu.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.itheima.edu.bean.BeanWrapper;
import org.itheima.edu.bean.ListWrapper;

public class JsonUtils {
	static Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

	/**
	 * 将json字符串转换成对象
	 * @param json
	 * @param clazz
	 * @return
	 */
	public static <T> T fromJson(String json, Class<T> clazz) {
		return gson.fromJson(json, clazz);
	}
	public static <T> T fromJson(File jsonFile, Class<T> clazz) throws FileNotFoundException {
		return gson.fromJson(new FileReader(jsonFile), clazz);
	}

	/**
	 * 将对象转成json字符串
	 * @return
	 */
	public static String toJson(Object src) {
		return gson.toJson(src);
	}

	/**
	 * 将json字符串 解析成包裹后的对象 BeanWrapper
	 * 包含 res响应码 和 data数据
	 * @param json
	 * @param clazz
	 * @return
	 */
	public static <T> BeanWrapper<T> fromWrapperJson(String json, Class<T> clazz) {
		Type objectType = type(BeanWrapper.class, clazz);
		return gson.fromJson(json, objectType);
	}

	/**
	 * 将json字符串 解析成包裹后的对象 ListWrapper
	 * 包含 res响应码 和 data数据
	 * 与 fromWrapperJson 的区别在于 如果data的数据直接为集合时, 必须用此方法解析
	 * @param json
	 * @param clazz
	 * @return
	 */
	public static <T> ListWrapper<T> fromListJson(String json, Class<T> clazz) {
		Type objectType = type(ListWrapper.class, clazz);
		return gson.fromJson(json, objectType);
	}

	/**
	 * 将对象进行包裹后, 输出成json字符串
	 * @param bean
	 * @return
	 */
	public static <T> String toWrapperJson(T bean) {
		return toWrapperJson(0, bean);
	}

	/**
	 * 将对象进行包裹后, 输出成json字符串, 并指定响应码
	 * @param resCode
	 * @param bean
	 * @return
	 */
	public static <T> String toWrapperJson(int resCode, T bean) {
		BeanWrapper<T> src = new BeanWrapper<T>();
		src.setCode(resCode);
		src.setMsg("success");
		src.setData(bean);
		String json = gson.toJson(src);
		return json;
	}

	// 此种方式行不通
	// public static <T> BeanWrapper<T> fromJson(String json, Class<T> clazz) {
	// Type objectType = new TypeToken<BeanWrapper<T>>() {
	// }.getType();
	// return gson.fromJson(json, objectType);
	// }

	static ParameterizedType type(final Class<?> raw, final Type... args) {
		return new ParameterizedType() {
			public Type getRawType() {
				return raw;
			}

			public Type[] getActualTypeArguments() {
				return args;
			}

			public Type getOwnerType() {
				return null;
			}
		};
	}
}
