package org.itheima.edu.jcompiler.core;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class ParamUnit {

	protected String type;// 参数的类型
	protected Set<String> annotations;// 参数对应的注解

	private Set<String> signatures;

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
		
		if (annotations != null && annotations.size() != 0) {
			signatures = new HashSet<>();
			for (String annotation : annotations) {
				signatures.add(annotation.concat(" ").concat(type));
			}
		} else {
			signatures = new HashSet<>();
			signatures.add(type);
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

}
