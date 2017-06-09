package org.itheima.edu.jcompiler.core;

import java.util.HashSet;
import java.util.Set;

import org.itheima.edu.jcompiler.tools.classfile.Annotation;
import org.itheima.edu.jcompiler.tools.classfile.ClassFile;
import org.itheima.edu.jcompiler.tools.classfile.ConstantPoolException;
import org.itheima.edu.jcompiler.tools.classfile.RuntimeVisibleAnnotations_attribute;
import org.itheima.edu.jcompiler.tools.classfile.Signature;
import org.itheima.edu.jcompiler.tools.classfile.Signature_attribute;
import org.itheima.edu.jcompiler.tools.classfile.Type;

final class FieldAttr {
	private final static boolean DEBUG = false;

	private static void log(String log) {
		if (DEBUG) {
			System.out.println("[FieldAttr]-" + log);
		}
	}

	protected static class FiledSignatureVistor extends SimpleAttrVisitor<String, Void> {
		public FiledSignatureVistor(ClassFile cf) {
			super(cf);
		}

		@Override
		public String visitSignature(Signature_attribute attr, Void p) {
			try {
				Signature signature = attr.getParsedSignature();
				Type type = signature.getType(cf.constant_pool);
				String value = ClassParser.replace(type.toString());
				value = ClassParser.filterSignature(value);
				return value;
			} catch (ConstantPoolException e) {
				e.printStackTrace();
			}
			return null;
		}
	}

	protected static class FieldRuntimeVisibleAnnotationsVistor extends SimpleAttrVisitor<Set<String>, Void> {
		public FieldRuntimeVisibleAnnotationsVistor(ClassFile cf) {
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
