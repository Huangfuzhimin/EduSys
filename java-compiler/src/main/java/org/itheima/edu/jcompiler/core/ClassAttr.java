package org.itheima.edu.jcompiler.core;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.itheima.edu.jcompiler.tools.classfile.Annotation;
import org.itheima.edu.jcompiler.tools.classfile.ClassFile;
import org.itheima.edu.jcompiler.tools.classfile.ConstantPoolException;
import org.itheima.edu.jcompiler.tools.classfile.RuntimeVisibleAnnotations_attribute;
import org.itheima.edu.jcompiler.tools.classfile.Signature;
import org.itheima.edu.jcompiler.tools.classfile.Signature_attribute;
import org.itheima.edu.jcompiler.tools.classfile.SourceFile_attribute;
import org.itheima.edu.jcompiler.tools.classfile.Type;
import org.itheima.edu.jcompiler.tools.classfile.Type.ClassSigType;
import org.itheima.edu.jcompiler.tools.classfile.Type.ClassType;

final class ClassAttr {
	private final static boolean DEBUG = false;

	private static void log(String log) {
		if (DEBUG) {
			System.out.println("[ClassAttr]-" + log);
		}
	}

	protected static class ClassSourceFileVistor extends SimpleAttrVisitor<String, Void> {
		public ClassSourceFileVistor(ClassFile cf) {
			super(cf);
		}

		@Override
		public String visitSourceFile(SourceFile_attribute attr, Void p) {
			try {
				return attr.getSourceFile(cf.constant_pool);
			} catch (ConstantPoolException e) {
				e.printStackTrace();
			}
			return null;
		}
	}

	protected static class ClassRuntimeVisibleAnnotationsVistor extends SimpleAttrVisitor<Set<String>, Void> {
		public ClassRuntimeVisibleAnnotationsVistor(ClassFile cf) {
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

	protected static class ClassSignatureVistor extends SimpleAttrVisitor<Object[], Void> {
		public ClassSignatureVistor(ClassFile cf) {
			super(cf);
		}

		@Override
		public Object[] visitSignature(Signature_attribute attr, Void p) {
			Object[] objs = new Object[2];
			try {
				Signature signature = attr.getParsedSignature();

				Type type = signature.getType(cf.constant_pool);
				if (type instanceof ClassType) {
					ClassType ct = (ClassType) type;
					objs[0] = ClassParser.replace(ct.toString());
				} else if (type instanceof ClassSigType) {
					ClassSigType cst = (ClassSigType) type;
					// extends
					objs[0] = ClassParser.replace(cst.superclassType.toString());

					// impls
					List<Type> superinterfaceTypes = cst.superinterfaceTypes;
					if (superinterfaceTypes != null && superinterfaceTypes.size() > 0) {
						Set<String> impls = new HashSet<>();
						for (Type t : superinterfaceTypes) {
							impls.add(ClassParser.replace(t.toString()));
						}
						objs[1] = impls;
					}
				} else {
					log("ClassSignatureVistor else : " + type.getClass().getName());
				}
			} catch (ConstantPoolException e) {
				e.printStackTrace();
			}
			return objs;
		}
	}
}
