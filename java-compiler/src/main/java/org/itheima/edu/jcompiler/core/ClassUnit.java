package org.itheima.edu.jcompiler.core;

import java.util.Set;

public final class ClassUnit {

	protected Set<String> modifiers;// 类的修饰符号
	protected Set<String> annotations;// 类的注解
	protected String extend;// 继承的类
	protected Set<String> impls;// 实现的接口
	protected String name;// 类名，含包名
	protected boolean isClass;// 是否是类
	protected boolean isInterface;// 是否是接口

	public Set<String> getModifiers() {
		return modifiers;
	}

	public Set<String> getAnnotations() {
		return annotations;
	}

	public String getExtend() {
		return extend;
	}

	public Set<String> getImpls() {
		return impls;
	}

	public String getName() {
		return name;
	}

	public boolean isClass() {
		return isClass;
	}

	public boolean isInterface() {
		return isInterface;
	}

}
