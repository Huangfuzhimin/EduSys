package org.itheima.edu.jcompiler.core;

import org.itheima.edu.jcompiler.tools.classfile.AnnotationDefault_attribute;
import org.itheima.edu.jcompiler.tools.classfile.Attribute;
import org.itheima.edu.jcompiler.tools.classfile.BootstrapMethods_attribute;
import org.itheima.edu.jcompiler.tools.classfile.CharacterRangeTable_attribute;
import org.itheima.edu.jcompiler.tools.classfile.ClassFile;
import org.itheima.edu.jcompiler.tools.classfile.Code_attribute;
import org.itheima.edu.jcompiler.tools.classfile.CompilationID_attribute;
import org.itheima.edu.jcompiler.tools.classfile.ConstantValue_attribute;
import org.itheima.edu.jcompiler.tools.classfile.DefaultAttribute;
import org.itheima.edu.jcompiler.tools.classfile.Deprecated_attribute;
import org.itheima.edu.jcompiler.tools.classfile.EnclosingMethod_attribute;
import org.itheima.edu.jcompiler.tools.classfile.Exceptions_attribute;
import org.itheima.edu.jcompiler.tools.classfile.InnerClasses_attribute;
import org.itheima.edu.jcompiler.tools.classfile.LineNumberTable_attribute;
import org.itheima.edu.jcompiler.tools.classfile.LocalVariableTable_attribute;
import org.itheima.edu.jcompiler.tools.classfile.LocalVariableTypeTable_attribute;
import org.itheima.edu.jcompiler.tools.classfile.MethodParameters_attribute;
import org.itheima.edu.jcompiler.tools.classfile.RuntimeInvisibleAnnotations_attribute;
import org.itheima.edu.jcompiler.tools.classfile.RuntimeInvisibleParameterAnnotations_attribute;
import org.itheima.edu.jcompiler.tools.classfile.RuntimeInvisibleTypeAnnotations_attribute;
import org.itheima.edu.jcompiler.tools.classfile.RuntimeVisibleAnnotations_attribute;
import org.itheima.edu.jcompiler.tools.classfile.RuntimeVisibleParameterAnnotations_attribute;
import org.itheima.edu.jcompiler.tools.classfile.RuntimeVisibleTypeAnnotations_attribute;
import org.itheima.edu.jcompiler.tools.classfile.Signature_attribute;
import org.itheima.edu.jcompiler.tools.classfile.SourceDebugExtension_attribute;
import org.itheima.edu.jcompiler.tools.classfile.SourceFile_attribute;
import org.itheima.edu.jcompiler.tools.classfile.SourceID_attribute;
import org.itheima.edu.jcompiler.tools.classfile.StackMapTable_attribute;
import org.itheima.edu.jcompiler.tools.classfile.StackMap_attribute;
import org.itheima.edu.jcompiler.tools.classfile.Synthetic_attribute;

class SimpleAttrVisitor<R, P> implements Attribute.Visitor<R, P> {
	protected ClassFile cf;

	public SimpleAttrVisitor(ClassFile cf) {
		this.cf = cf;
	}

	@Override
	public R visitBootstrapMethods(BootstrapMethods_attribute attr, P p) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public R visitDefault(DefaultAttribute attr, P p) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public R visitAnnotationDefault(AnnotationDefault_attribute attr, P p) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public R visitCharacterRangeTable(CharacterRangeTable_attribute attr, P p) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public R visitCode(Code_attribute attr, P p) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public R visitCompilationID(CompilationID_attribute attr, P p) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public R visitConstantValue(ConstantValue_attribute attr, P p) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public R visitDeprecated(Deprecated_attribute attr, P p) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public R visitEnclosingMethod(EnclosingMethod_attribute attr, P p) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public R visitExceptions(Exceptions_attribute attr, P p) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public R visitInnerClasses(InnerClasses_attribute attr, P p) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public R visitLineNumberTable(LineNumberTable_attribute attr, P p) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public R visitLocalVariableTable(LocalVariableTable_attribute attr, P p) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public R visitLocalVariableTypeTable(LocalVariableTypeTable_attribute attr, P p) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public R visitMethodParameters(MethodParameters_attribute attr, P p) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public R visitRuntimeVisibleAnnotations(RuntimeVisibleAnnotations_attribute attr, P p) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public R visitRuntimeInvisibleAnnotations(RuntimeInvisibleAnnotations_attribute attr, P p) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public R visitRuntimeVisibleParameterAnnotations(RuntimeVisibleParameterAnnotations_attribute attr, P p) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public R visitRuntimeInvisibleParameterAnnotations(RuntimeInvisibleParameterAnnotations_attribute attr, P p) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public R visitRuntimeVisibleTypeAnnotations(RuntimeVisibleTypeAnnotations_attribute attr, P p) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public R visitRuntimeInvisibleTypeAnnotations(RuntimeInvisibleTypeAnnotations_attribute attr, P p) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public R visitSignature(Signature_attribute attr, P p) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public R visitSourceDebugExtension(SourceDebugExtension_attribute attr, P p) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public R visitSourceFile(SourceFile_attribute attr, P p) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public R visitSourceID(SourceID_attribute attr, P p) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public R visitStackMap(StackMap_attribute attr, P p) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public R visitStackMapTable(StackMapTable_attribute attr, P p) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public R visitSynthetic(Synthetic_attribute attr, P p) {
		// TODO Auto-generated method stub
		return null;
	}

}
