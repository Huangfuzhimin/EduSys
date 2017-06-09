package org.itheima.edu.jcompiler.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.itheima.edu.jcompiler.tools.classfile.Annotation;
import org.itheima.edu.jcompiler.tools.classfile.ClassFile;
import org.itheima.edu.jcompiler.tools.classfile.Exceptions_attribute;
import org.itheima.edu.jcompiler.tools.classfile.RuntimeInvisibleParameterAnnotations_attribute;
import org.itheima.edu.jcompiler.tools.classfile.RuntimeVisibleAnnotations_attribute;
import org.itheima.edu.jcompiler.tools.classfile.RuntimeVisibleParameterAnnotations_attribute;
import org.itheima.edu.jcompiler.tools.classfile.Signature;
import org.itheima.edu.jcompiler.tools.classfile.Signature_attribute;
import org.itheima.edu.jcompiler.tools.classfile.Type;
import org.itheima.edu.jcompiler.tools.classfile.Type.MethodType;

final class ConstructorAttr {
	private final static boolean DEBUG = false;

	private static void log(String log) {
		if (DEBUG) {
			System.out.println("[ConstructorAttr]-" + log);
		}
	}

	protected static class ConstructorSignatureVistor extends SimpleAttrVisitor<Object[], Void> {

		public ConstructorSignatureVistor(ClassFile cf) {
			super(cf);
		}

		@Override
		public Object[] visitSignature(Signature_attribute attr, Void p) {
			Object[] objs = new Object[2];
			try {
				Signature signature = attr.getParsedSignature();

				Type type = signature.getType(cf.constant_pool);

				if (type instanceof MethodType) {
					MethodType mt = (MethodType) type;

					List<? extends Type> types = mt.paramTypes;
					if (types != null) {

						List<String> params = new ArrayList<>();
						for (int i = 0; i < types.size(); i++) {
							Type t = types.get(i);
							String value = ClassParser.replace(t.toString());
							value = ClassParser.filterSignature(value);
							params.add(value);
						}
						// 参数个数
						objs[0] = types.size();

						// 参数类型
						objs[1] = params;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return objs;
		}
	}

	protected static class ConstructorRuntimeVisibleParameterAnnotations
			extends SimpleAttrVisitor<Map<Integer, List<String>>, Void> {

		public ConstructorRuntimeVisibleParameterAnnotations(ClassFile cf) {
			super(cf);
		}

		@Override
		public Map<Integer, List<String>> visitRuntimeVisibleParameterAnnotations(
				RuntimeVisibleParameterAnnotations_attribute attr, Void p) {

			Annotation[][] annotations = attr.parameter_annotations;
			if (annotations == null || annotations.length == 0) {
				return null;
			}
			Map<Integer, List<String>> map = new HashMap<>();

			for (int i = 0; i < annotations.length; i++) {
				Annotation[] ans = annotations[i];
				for (int j = 0; j < ans.length; j++) {
					Annotation annotation = ans[j];

					int type_index = annotation.type_index;
					try {
						String value = cf.constant_pool.getUTF8Value(type_index);
						if (value != null) {
							if (value.startsWith("L")) {
								value = value.substring(1);
							}
							if (value.endsWith(";")) {
								value = value.substring(0, value.length() - 1);
							}
							value = ClassParser.replace(value);
							value = ClassParser.filterSignature(value);

							List<String> list = map.get(i);
							if (list == null) {
								list = new ArrayList<>();
								map.put(i, list);
							}
							list.add(value);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			return map;
		}
	}

	protected static class ConstructorRuntimeInvisibleParameterAnnotations
			extends SimpleAttrVisitor<Map<Integer, List<String>>, Void> {

		public ConstructorRuntimeInvisibleParameterAnnotations(ClassFile cf) {
			super(cf);
		}

		@Override
		public Map<Integer, List<String>> visitRuntimeInvisibleParameterAnnotations(
				RuntimeInvisibleParameterAnnotations_attribute attr, Void p) {

			Annotation[][] annotations = attr.parameter_annotations;
			if (annotations == null || annotations.length == 0) {
				return null;
			}
			Map<Integer, List<String>> map = new HashMap<>();

			for (int i = 0; i < annotations.length; i++) {
				Annotation[] ans = annotations[i];
				for (int j = 0; j < ans.length; j++) {
					Annotation annotation = ans[j];

					int type_index = annotation.type_index;
					try {
						String value = cf.constant_pool.getUTF8Value(type_index);
						if (value != null) {
							if (value.startsWith("L")) {
								value = value.substring(1);
							}
							if (value.endsWith(";")) {
								value = value.substring(0, value.length() - 1);
							}
							value = ClassParser.replace(value);
							value = ClassParser.filterSignature(value);

							List<String> list = map.get(i);
							if (list == null) {
								list = new ArrayList<>();
								map.put(i, list);
							}
							list.add(value);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			return map;
		}
	}

	protected static class ConstructorExceptionsVisitor extends SimpleAttrVisitor<Set<String>, Void> {

		public ConstructorExceptionsVisitor(ClassFile cf) {
			super(cf);
		}

		@Override
		public Set<String> visitExceptions(Exceptions_attribute attr, Void p) {
			try {
				int num = attr.number_of_exceptions;
				if (num <= 0) {
					return null;
				}

				Set<String> set = new HashSet<>();
				for (int i = 0; i < num; i++) {
					String exception = attr.getException(i, cf.constant_pool);
					String value = ClassParser.replace(exception);
					value = ClassParser.filterSignature(value);
					set.add(value);
				}
				return set;
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
	}

	protected static class ConstructorRuntimeVisibleAnnotationsVisitor extends SimpleAttrVisitor<Set<String>, Void> {

		public ConstructorRuntimeVisibleAnnotationsVisitor(ClassFile cf) {
			super(cf);
		}

		@Override
		public Set<String> visitRuntimeVisibleAnnotations(RuntimeVisibleAnnotations_attribute attr, Void p) {
			Annotation[] annotations = attr.annotations;
			if (annotations != null && annotations.length > 0) {
				Set<String> set = new HashSet<>();
				for (Annotation annotation : annotations) {
					int type_index = annotation.type_index;
					try {
						String value = cf.constant_pool.getUTF8Value(type_index);
						if (value != null) {
							if (value.startsWith("L")) {
								value = value.substring(1);
							}
							if (value.endsWith(";")) {
								value = value.substring(0, value.length() - 1);
							}
							value = ClassParser.replace(value);
							value = ClassParser.filterSignature(value);
							set.add(value);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				return set;
			}
			return null;
		}

	}

}
