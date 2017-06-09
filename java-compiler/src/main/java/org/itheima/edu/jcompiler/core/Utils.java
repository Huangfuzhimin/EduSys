package org.itheima.edu.jcompiler.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

final class Utils {

	private Utils() {
	}

	public static void generatePerCom(List<String> src, List<String> dest, StringBuilder builder) {
		generatePerCom(src, dest, " ", builder);
	}

	public static void generatePerCom(List<String> src, List<String> dest, String separator, StringBuilder builder) {
		generatePerCom(src, dest, separator, null, null, builder);
	}

	public static void generatePerCom(List<String> src, List<String> dest, String separator, String start, String end,
			StringBuilder builder) {
		if (src.isEmpty()) {
			String result = builder.toString();
			if (result.endsWith(separator)) {
				result = result.substring(0, result.length() - separator.length());
			}
			if (start == null) {
				start = "";
			}
			if (end == null) {
				end = "";
			}
			dest.add(start.concat(result).concat(end));
			return;
		}
		int size = src.size();
		List<String> temp;
		StringBuilder tempBuilder;
		for (int i = 0; i < size; i++) {
			tempBuilder = new StringBuilder();
			tempBuilder.append(builder);
			tempBuilder.append(src.get(i));
			tempBuilder.append(separator);

			temp = new ArrayList<String>();
			temp.addAll(src);
			temp.remove(i);
			generatePerCom(temp, dest, separator, tempBuilder);
		}
	}

	private static List<String> generateMix(List<String> src1, List<String> src2, String separator, String start,
			String end, boolean last) {
		if (src1 == null && src2 != null) {
			if (start == null) {
				start = "";
			}
			if (end == null) {
				end = "";
			}
			if (last) {
				for (int i = 0; i < src2.size(); i++) {
					String s = src2.get(i);
					src2.set(i, start.concat(s).concat(end));
				}
			}
			return src2;
		}

		if (src1 != null && src2 == null) {
			if (start == null) {
				start = "";
			}
			if (end == null) {
				end = "";
			}
			if (last) {
				for (int i = 0; i < src1.size(); i++) {
					String s = src1.get(i);
					src1.set(i, start.concat(s).concat(end));
				}
			}
			return src1;
		}

		if (src1 == null && src2 == null) {
			return null;
		}

		List<String> dest = new ArrayList<>();
		for (String s1 : src1) {
			for (String s2 : src2) {
				if (last) {
					if (start == null) {
						start = "";
					}
					if (end == null) {
						end = "";
					}

					dest.add(start.concat(s1).concat(separator).concat(s2).concat(end));
				} else {
					dest.add(s1.concat(separator).concat(s2));
				}
			}
		}

		return dest;
	}

	@SafeVarargs
	public static List<String> generateMix(List<String>... srcs) {
		return generateMix(" ", null, null, srcs);
	}

	@SafeVarargs
	public static List<String> generateMix(String separator, List<String>... srcs) {
		return generateMix(separator, null, null, srcs);
	}

	@SafeVarargs
	public static List<String> generateMix(String separator, String start, String end, List<String>... srcs) {
		if (srcs == null || srcs.length == 0) {
			return null;
		}
		// if (srcs.length == 1) {
		// return srcs[0];
		// }

		List<List<String>> copys = new ArrayList<>(Arrays.asList(srcs));
		// 临时
		List<String> temps = null;
		while (copys.size() > 0) {
			int size = copys.size();

			List<String> list = copys.remove(size - 1);
			boolean last = false;
			if (copys.size() == 0) {
				last = true;
			}
			temps = generateMix(list, temps, separator, start, end, last);
		}

		return temps;
	}
	
}
