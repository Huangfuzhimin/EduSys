package org.itheima.edu.jcompiler.core;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class FieldUnit {

	protected String name;// 属性的名称
	protected Set<String> modifiers;// 属性的修饰符号
	protected String type;// 属性的修饰符号
	protected Set<String> annotations;// 属性的注解

	private Set<String> signatures;

	public String getName() {
		return name;
	}

	public Set<String> getModifiers() {
		return modifiers;
	}

	public String getType() {
		return type;
	}

	public Set<String> getAnnotations() {
		return annotations;
	}

	public Set<String> toSignatures() {
		if (signatures != null) {
			return signatures;
		}

		// 获得所有annotation的组合方式
		List<String> annotations = annotationCom();

		// 获得所有的修饰符的组合方式
		List<String> modifiers = modifierCom();

		List<String> mix = Utils.generateMix(annotations, modifiers);

		if (mix != null && mix.size() != 0) {
			signatures = new HashSet<>();
			for (String str : mix) {
				signatures.add(str.concat(" ").concat(type).concat(" ").concat(name));
			}
		} else {
			signatures = new HashSet<>();

			signatures.add(type.concat(" ").concat(name));
		}
		return signatures;
	}

	private List<String> annotationCom() {
		if (annotations == null || annotations.size() == 0) {
			return null;
		}
		List<String> src = new ArrayList<>();
		for (String annotation : annotations) {
			src.add("@".concat(annotation));
		}

		StringBuilder builder = new StringBuilder();
		List<String> dest = new ArrayList<>();
		Utils.generatePerCom(src, dest, builder);
		return dest;
	}

	private List<String> modifierCom() {
		if (modifiers == null || modifiers.size() == 0) {
			return null;
		}

		List<String> src = new ArrayList<>(modifiers);
		StringBuilder builder = new StringBuilder();
		List<String> dest = new ArrayList<>();
		Utils.generatePerCom(src, dest, builder);
		return dest;
	}
}
