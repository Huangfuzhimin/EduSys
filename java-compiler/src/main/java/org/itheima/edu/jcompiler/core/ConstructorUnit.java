package org.itheima.edu.jcompiler.core;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class ConstructorUnit {

	protected Set<String> modifiers;// 方法的修饰符号
	protected int paramCount;// 参数个数
	protected List<ParamUnit> params;// 参数类型
	protected Set<String> exceptions;// 异常
	protected Set<String> annotations;// 注解
	protected String name;

	private Set<String> signatures;

	public Set<String> getModifiers() {
		return modifiers;
	}

	public int getParamCount() {
		return paramCount;
	}

	public List<ParamUnit> getParams() {
		return params;
	}

	public Set<String> getExceptions() {
		return exceptions;
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

		// 拼接注解和修饰符号
		List<String> mix = Utils.generateMix(annotations, modifiers);

		List<String> prefixs = null;
		if (mix != null && mix.size() != 0) {
			Set<String> set = new HashSet<>();
			for (String str : mix) {
				set.add(str.concat(" "));
			}

			prefixs = new ArrayList<>(set);
		} else {
			String prefix = "";
			prefixs = new ArrayList<>();
			prefixs.add(prefix);
		}

		List<String> params = paramsCom();

		// 拼接参数部分
		prefixs = Utils.generateMix("", prefixs, params);

		// 拼接异常部分
		List<String> exceptions = exceptionCom();
		prefixs = Utils.generateMix("", prefixs, exceptions);

		signatures = new HashSet<>(prefixs);

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

	@SuppressWarnings("unchecked")
	private List<String> paramsCom() {
		if (params == null || params.size() == 0) {
			List<String> result = new ArrayList<>();
			result.add(name.concat("()"));
			return result;
		}

		List<List<String>> list = new ArrayList<>();
		for (ParamUnit unit : params) {
			Set<String> sigs = unit.toSignatures();
			list.add(new ArrayList<>(sigs));
		}
		return Utils.generateMix(",", name.concat("("), ")", list.toArray(new ArrayList[list.size()]));
	}

	private List<String> exceptionCom() {
		if (exceptions == null || exceptions.size() == 0) {
			return null;
		}

		StringBuilder builder = new StringBuilder();
		List<String> dest = new ArrayList<>();
		List<String> src = new ArrayList<>(exceptions);
		Utils.generatePerCom(src, dest, ",", " throws ", null, builder);
		return dest;
	}
}
