package org.itheima.edu.jcompiler.core;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.itheima.edu.jcompiler.core.ClassAttr.ClassRuntimeVisibleAnnotationsVistor;
import org.itheima.edu.jcompiler.core.ClassAttr.ClassSignatureVistor;
import org.itheima.edu.jcompiler.core.ClassAttr.ClassSourceFileVistor;
import org.itheima.edu.jcompiler.tools.classfile.Attribute;
import org.itheima.edu.jcompiler.tools.classfile.Attributes;
import org.itheima.edu.jcompiler.tools.classfile.ClassFile;
import org.itheima.edu.jcompiler.tools.classfile.Descriptor;
import org.itheima.edu.jcompiler.tools.classfile.Field;
import org.itheima.edu.jcompiler.tools.classfile.Method;

public class ClassParser {
	private final static boolean DEBUG = false;

	private ClassFile cf;

	private String sourceFileName;// 源文件的文件名，例如 "Test.java"

	private ClassUnit classUnit;
	private Set<FieldUnit> fieldUnits;
	private Set<MethodUnit> methodUnits;
	private Set<ConstructorUnit> constructorUnits;

	private String source;

	public ClassParser(String classFilePath, String source) throws Exception {
		this.source = source;
		cf = ClassFile.read(new File(classFilePath));

		classUnit = new ClassUnit();
		fieldUnits = new HashSet<>();
		methodUnits = new HashSet<>();
		constructorUnits = new HashSet<>();

		// 加载类签名
		loadClassSignature();

		// 加载属性签名
		loadFieldSignature();

		// 加载方法签名
		loadMethodSignature();
	}

	/**
	 * 获得类的源码
	 * 
	 * @return
	 */
	public String getSource() {
		return source;
	}

	/**
	 * 获得类的描述部分
	 * 
	 * @return
	 */
	public ClassUnit getClassUnit() {
		return classUnit;
	}

	/**
	 * 获得所有的属性描述部分
	 * 
	 * @return
	 */
	public Set<FieldUnit> getFieldUnits() {
		return fieldUnits;
	}

	/**
	 * 获得所有的方法描述部分
	 * 
	 * @return
	 */
	public Set<MethodUnit> getMethodUnits() {
		return methodUnits;
	}

	/**
	 * 获得所有的构造函数描述部分
	 * 
	 * @return
	 */
	public Set<ConstructorUnit> getConstructorUnits() {
		return constructorUnits;
	}

	public static String replace(String str) {
		if (str != null) {
			return str.replace("/", ".");
		}
		return null;
	}

	public static String filterSignature(String signature) {
		if (signature == null) {
			return null;
		}
		String sig = signature.trim();

		// 去掉所有的双空格
		while (sig.contains("  ")) {
			sig = sig.replace("  ", " ");
		}

		// 去掉 ', '
		while (sig.contains(", ")) {
			sig = sig.replace(", ", ",");
		}

		// 去掉' ,'
		while (sig.contains(" ,")) {
			sig = sig.replace(" ,", ",");
		}

		return sig;
	}

	private void loadClassSignature() throws Exception {
		classUnit.isClass = cf.isClass();
		classUnit.isInterface = cf.isInterface();

		Attributes attributes = cf.attributes;
		Attribute[] attrs = attributes.attrs;

		if (attrs == null) {
			return;
		}

		// 遍历属性
		for (Attribute attr : attrs) {
			String name = attr.getName(cf.constant_pool);
			log("class : " + name);

			if (Attribute.SourceFile.equals(name)) {
				// sourceFile
				sourceFileName = attr.accept(new ClassSourceFileVistor(cf), null);
			} else if (Attribute.Signature.equals(name)) {
				// signature
				Object[] accept = attr.accept(new ClassSignatureVistor(cf), null);
				classUnit.extend = (String) accept[0];
				classUnit.impls = (Set<String>) accept[1];
			} else if (Attribute.RuntimeVisibleAnnotations.equals(name)) {
				classUnit.annotations = attr.accept(new ClassRuntimeVisibleAnnotationsVistor(cf), null);
			} else {
				// TODO:
				log("class : else : " + name);
			}
		}

		// 扫除判断中没有实现的
		// classExtends
		if (classUnit.extend == null) {
			classUnit.extend = replace(cf.getSuperclassName());
		}
		// classImpls
		if (classUnit.impls == null) {
			int[] interfaces = cf.interfaces;
			if (interfaces != null && interfaces.length > 0) {
				Set<String> set = new HashSet<>();
				for (int i = 0; i < interfaces.length; i++) {
					String interfaceName = cf.getInterfaceName(i);
					set.add(replace(interfaceName));
				}
				classUnit.impls = set;
			}
		}
	}

	private void loadFieldSignature() throws Exception {

		Field[] fields = cf.fields;

		if (fields == null) {
			return;
		}

		for (Field field : fields) {
			FieldUnit unit = new FieldUnit();

			// 1.属性名称
			unit.name = field.getName(cf.constant_pool);

			// 2. 属性的修饰符号
			unit.modifiers = field.access_flags.getFieldModifiers();

			// 3. 属性的类型
			Attributes attributes = field.attributes;
			Attribute[] attrs = attributes.attrs;
			for (Attribute attr : attrs) {
				String name = attr.getName(cf.constant_pool);
				log("filed : " + name);

				if (Attribute.Signature.equals(name)) {
					unit.type = attr.accept(new FieldAttr.FiledSignatureVistor(cf), null);
				} else if (Attribute.RuntimeVisibleAnnotations.equals(name)) {
					unit.annotations = attr.accept(new FieldAttr.FieldRuntimeVisibleAnnotationsVistor(cf), null);
				} else {
					// TODO:
					log("filed : else : " + name);
				}
			}

			if (unit.type == null) {
				Descriptor descriptor = field.descriptor;
				String type = descriptor.getFieldType(cf.constant_pool);
				unit.type = filterSignature(type);
			}

			// 4. 属性的注解

			// 添加到属性描述中
			fieldUnits.add(unit);
		}
	}

	private void loadMethodSignature() throws Exception {
		Method[] methods = cf.methods;

		if (methods == null) {
			return;
		}

		for (Method method : methods) {
			String methodName = method.getName(cf.constant_pool);

			if ("<init>".equals(methodName)) {
				// 构造函数
				ConstructorUnit unit = new ConstructorUnit();
				unit.name = cf.getName();

				// 1. 构造的修饰符号
				unit.modifiers = method.access_flags.getMethodModifiers();

				// 2. 参数
				List<String> params = null;// 参数类型
				int paramCount = 0;// 参数个数
				Map<Integer, Set<String>> map = null;// 注解

				Attributes attributes = method.attributes;
				Attribute[] attrs = attributes.attrs;
				for (Attribute attr : attrs) {
					String name = attr.getName(cf.constant_pool);
					log("Constructor : " + name);

					if (Attribute.Signature.equals(name)) {
						Object[] objs = attr.accept(new ConstructorAttr.ConstructorSignatureVistor(cf), null);
						// 1. 参数个数
						paramCount = (int) objs[0];
						// 2. 参数类型
						params = (List<String>) objs[1];
					} else if (Attribute.RuntimeVisibleParameterAnnotations.equals(name)) {
						Map<Integer, List<String>> accept = attr
								.accept(new ConstructorAttr.ConstructorRuntimeVisibleParameterAnnotations(cf), null);
						if (accept != null && accept.size() > 0) {
							if (map == null) {
								map = new HashMap<>();
							}
							for (Map.Entry<Integer, List<String>> me : accept.entrySet()) {
								Integer key = me.getKey();
								List<String> value = me.getValue();

								Set<String> set = map.get(key);
								if (set == null) {
									set = new HashSet<>();
									map.put(key, set);
								}
								set.addAll(value);
							}
						}
					} else if (Attribute.RuntimeInvisibleParameterAnnotations.equals(name)) {
						Map<Integer, List<String>> accept = attr
								.accept(new ConstructorAttr.ConstructorRuntimeVisibleParameterAnnotations(cf), null);
						if (accept != null && accept.size() > 0) {
							if (map == null) {
								map = new HashMap<>();
							}
							for (Map.Entry<Integer, List<String>> me : accept.entrySet()) {
								Integer key = me.getKey();
								List<String> value = me.getValue();

								Set<String> set = map.get(key);
								if (set == null) {
									set = new HashSet<>();
									map.put(key, set);
								}
								set.addAll(value);
							}
						}
					} else if (Attribute.Exceptions.equals(name)) {
						unit.exceptions = attr.accept(new ConstructorAttr.ConstructorExceptionsVisitor(cf), null);
					} else if (Attribute.RuntimeVisibleAnnotations.equals(name)) {
						unit.annotations = attr
								.accept(new ConstructorAttr.ConstructorRuntimeVisibleAnnotationsVisitor(cf), null);
					} else {
						// TODO:
						log("constructor : else : " + name);
					}
				}

				Descriptor descriptor = method.descriptor;
				if (params == null) {
					unit.paramCount = descriptor.getParameterCount(cf.constant_pool);

					String parameterTypes = descriptor.getParameterTypes(cf.constant_pool);
					parameterTypes = parameterTypes.replace("(", "");
					parameterTypes = parameterTypes.replace(")", "");
					if (parameterTypes != null && parameterTypes.trim().length() != 0) {
						params = new ArrayList<>();
						String[] split = parameterTypes.split(",");
						for (String str : split) {
							params.add(str.trim());
						}
					}
				} else {
					unit.paramCount = paramCount;
				}

				if (params != null && params.size() > 0) {
					List<ParamUnit> paramUnits = new ArrayList<>();

					for (int i = 0; i < params.size(); i++) {
						ParamUnit pu = new ParamUnit();
						pu.type = filterSignature(params.get(i));

						if (map != null) {
							Set<String> set = map.get(i);
							if (set != null) {
								pu.annotations = set;
							}
						}
						paramUnits.add(pu);
					}
					unit.params = paramUnits;
				}

				constructorUnits.add(unit);
			} else {
				// 方法
				MethodUnit unit = new MethodUnit();
				// 1. 方法名
				unit.name = methodName;
				// 2. 方法的修饰符号
				unit.modifiers = method.access_flags.getMethodModifiers();

				List<String> params = null;// 参数类型
				int paramCount = 0;// 参数个数
				String returnType = null;// 返回值
				Map<Integer, Set<String>> map = null;// 注解

				Attributes attributes = method.attributes;
				Attribute[] attrs = attributes.attrs;
				for (Attribute attr : attrs) {
					String name = attr.getName(cf.constant_pool);
					log("method : " + name);

					if (Attribute.Signature.equals(name)) {
						Object[] objs = attr.accept(new MethodAttr.MethodSignatureVistor(cf), null);
						// 1. 参数个数
						paramCount = (int) objs[0];
						// 2. 参数类型
						params = (List<String>) objs[1];
						// 3. 返回值类型
						returnType = (String) objs[2];
					} else if (Attribute.RuntimeVisibleParameterAnnotations.equals(name)) {
						Map<Integer, List<String>> accept = attr
								.accept(new MethodAttr.MethodRuntimeVisibleParameterAnnotations(cf), null);
						if (accept != null && accept.size() > 0) {
							if (map == null) {
								map = new HashMap<>();
							}
							for (Map.Entry<Integer, List<String>> me : accept.entrySet()) {
								Integer key = me.getKey();
								List<String> value = me.getValue();

								Set<String> set = map.get(key);
								if (set == null) {
									set = new HashSet<>();
									map.put(key, set);
								}
								set.addAll(value);
							}
						}
					} else if (Attribute.RuntimeInvisibleParameterAnnotations.equals(name)) {
						Map<Integer, List<String>> accept = attr
								.accept(new MethodAttr.MethodRuntimeInvisibleParameterAnnotations(cf), null);
						if (accept != null && accept.size() > 0) {
							if (map == null) {
								map = new HashMap<>();
							}
							for (Map.Entry<Integer, List<String>> me : accept.entrySet()) {
								Integer key = me.getKey();
								List<String> value = me.getValue();

								Set<String> set = map.get(key);
								if (set == null) {
									set = new HashSet<>();
									map.put(key, set);
								}
								set.addAll(value);
							}
						}
					} else if (Attribute.Exceptions.equals(name)) {
						unit.exceptions = attr.accept(new MethodAttr.MethodExceptionsVisitor(cf), null);
					} else if (Attribute.RuntimeVisibleAnnotations.equals(name)) {
						unit.annotations = attr.accept(new MethodAttr.MethodRuntimeVisibleAnnotationsVisitor(cf), null);
					} else {
						// TODO:
						log("method : else : " + name);
					}
				}

				Descriptor descriptor = method.descriptor;
				// 3. 返回值
				if (returnType == null) {
					returnType = descriptor.getReturnType(cf.constant_pool);
					returnType = filterSignature(returnType);
					unit.returnType = returnType;
				} else {
					unit.returnType = returnType;
				}

				// 4. 参数
				if (params == null) {
					unit.paramCount = descriptor.getParameterCount(cf.constant_pool);

					String parameterTypes = descriptor.getParameterTypes(cf.constant_pool);
					parameterTypes = parameterTypes.replace("(", "");
					parameterTypes = parameterTypes.replace(")", "");
					if (parameterTypes != null && parameterTypes.trim().length() != 0) {
						params = new ArrayList<>();
						String[] split = parameterTypes.split(",");
						for (String str : split) {
							params.add(filterSignature(str.trim()));
						}
					}
				} else {
					unit.paramCount = paramCount;
				}

				if (params != null && params.size() > 0) {
					List<ParamUnit> paramUnits = new ArrayList<>();

					for (int i = 0; i < params.size(); i++) {
						ParamUnit pu = new ParamUnit();
						pu.type = params.get(i);

						if (map != null) {
							Set<String> set = map.get(i);
							if (set != null) {
								pu.annotations = set;
							}
						}
						paramUnits.add(pu);
					}
					unit.params = paramUnits;
				}

				methodUnits.add(unit);
			}

		}
	}

	private static void log(String log) {
		if (DEBUG) {
			System.out.println("[ClassParser]-" + log);
		}
	}
}
