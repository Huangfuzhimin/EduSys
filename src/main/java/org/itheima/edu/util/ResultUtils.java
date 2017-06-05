package org.itheima.edu.util;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import org.itheima.edu.bean.ExamItemResult;
import org.itheima.edu.bean.ExamResult;
import org.itheima.edu.bean.RunInfo;
import org.itheima.edu.entity.UserEntity;

public class ResultUtils {
	public static final int CACHE_DURATION = 10 * 60 * 1000;	// 缓存10分钟
	static Gson gson = new Gson();

	public static ExamResult getResult(String dataPath, String cachePath, UserEntity entry) {
		return getResult(dataPath, cachePath, entry, null, null);
	}
	
	// 获取指定用户的测试结果
	public static ExamResult getResult(String dataPath, String cachePath, UserEntity entry, String chapter, String type) {
		// 获得用户根目录 (eg. /result/aaa)
		File dir = new File(dataPath, entry.getAccount());
		if (!dir.exists()) {
			return null;
		}
		
		// 获取章节根目录 (eg. /result/aaa/java基础和变量)
		if(chapter != null){
			dir = new File(dir, chapter);
			if(!dir.exists()){
				return null;
			}
		}
		
		// 获取指定用户/章节/类型根目录 (eg. /result/aaa/java基础和变量/lesson)
		if(type != null){
			dir = new File(dir, type);
			if(!dir.exists()){
				return null;
			}
		}

		ExamResult result = null;
		try {
			// 1. 尝试读取目录中的缓存文件 (eg. /cache/aaa/java基础和变量/lesson_result.json)
			File cacheFile = new File(cachePath, String.format("/%1$s/%2$s/%3$s.json",
					entry.getAccount(), chapter, (type + "_result")));

			result = readResultCache(cacheFile); // 读取结果缓存数据
			if(result != null){
				try {
					System.out.println("读取到进度缓存: " + cacheFile.getCanonicalPath());
				} catch (IOException e) {
					e.printStackTrace();
				}
				return result;
			}

			// 2. 缓存不存在或缓存失效, 生成结果文件
			result = generateExamResult(entry, dir);

			// 3. 保存结果文件
			saveResultCache(cacheFile, result);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		return result;
	}
	private static ExamResult readResultCache(File cacheFile) throws FileNotFoundException {
		ExamResult result = null;
		if(cacheFile.exists() && cacheFile.isFile()){
			// 文件存在
			long lastModified = cacheFile.lastModified();
			if(System.currentTimeMillis() - lastModified < CACHE_DURATION){
				// 最后一次修改时间小于10分钟, 使用缓存
				result = JsonUtils.fromJson(cacheFile, ExamResult.class);
			}else {
				// 缓存文件已经失效, 删除文件
				cacheFile.delete();
			}
		}
		return result;
	}

	/**
	 * 保存缓存文件
	 * @param cacheFile
	 * @param result
	 */
	private static void saveResultCache(File cacheFile, ExamResult result) {
		FileUtils.writeFile(cacheFile, JsonUtils.toJson(result));
	}

	/**
	 * 生成测试结果
	 * @param entry	用户对象
	 * @param dir	要解析的目录
	 * @return
	 */
	public static ExamResult generateExamResult(UserEntity entry, File dir) {
		ExamResult result = new ExamResult();
		result.setAccount(entry.getAccount());
		result.setName(entry.getName());

		// 题目的集合
		File[] files = dir.listFiles(new FileFilter() {

			@Override
			public boolean accept(File file) {
				if (file.isDirectory()) {
					return true;
				}
				return false;
			}
		});

		if (files == null) {
			return null;
		}

		int totalScore = 0; 		// 总分
		long lastCommitTime = 0;	// 最后提交时间
		int totalExecuteCount = 0;	// 总执行次数
		int totalExecuteSuccessCount = 0;	// 总成功执行次数
		int totalCompleteCount = 0; // 满分完成的题目数量
		int totalTryCount = 0; 		// 尝试执行的题目数量

		// 遍历题目的集合
		List<ExamItemResult> items = new ArrayList<>();
		for (File file : files) {
			// 保存试题信息汇总
			final String name = file.getName();

			Object[] objs = getInfo(file);
			int[] executeInfo = getExecuteInfo(file);

			ExamItemResult item = new ExamItemResult();
			item.setName(name);

			long commitTime = 0;
			if (objs != null && objs.length == 2) {
				int score = (int) objs[0];
				item.setScore(score);		// 最终得分

				commitTime = (long) objs[1];
				item.setCommitTime(commitTime);	// 交卷时间
				if(lastCommitTime < commitTime){
					lastCommitTime = commitTime; // 记录最后一个提交的时间
				}
			}

			if (executeInfo != null && executeInfo.length == 4) {
				int executeCount = executeInfo[0];
				int compileSuccessCount = executeInfo[1];
				int compileFailedCount = executeInfo[2];
				int topScore = executeInfo[3];

				item.setCompileFailedCount(compileFailedCount);
				item.setCompileSuccessCount(compileSuccessCount);
				item.setExecuteCount(executeCount);
				item.setTopScore(topScore);
				totalScore += topScore;	// 累加最高分

				totalExecuteCount += executeCount; // 累加执行次数

				totalExecuteSuccessCount += compileSuccessCount; // 累加成功执行次数


				if(item.getScore() == 100){	// 累加满分完成的题目数量
					totalCompleteCount++;
				}
				if(executeCount > 0){	// 累加尝试执行的题目数量
					totalTryCount++;
				}
			}

			items.add(item);
		}
		result.setResults(items);

		result.setTotalScore(totalScore);
		result.setLastCommitTime(lastCommitTime);
		result.setTotalExecuteCount(totalExecuteCount);
		result.setTotalExecuteSuccessCount(totalExecuteSuccessCount);
		result.setTotalExecuteFailedCount(totalExecuteCount - totalExecuteSuccessCount);
		result.setTotalExamCount(items.size());
		result.setTotalCompleteCount(totalCompleteCount);
		result.setTotalTryCount(totalTryCount);

		result.setLastModified(System.currentTimeMillis());

		return result;
	}


	private static Object[] getInfo(File dir) {
		File resultInfo = new File(dir, "submit.data");
		if (!resultInfo.exists()) {
			return null;
		}
		try {
			String content = StreamTools.readStream(new FileInputStream(resultInfo));

			RunInfo info = gson.fromJson(content, RunInfo.class);

			return new Object[] { info.getScore(), info.getEndTime() };
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private static int[] getExecuteInfo(File dir) {
		File runDir = new File(dir, "storage/run_info");
		if (!runDir.exists()) {
			return null;
		}

		int excuteCount = 0;// 执行次数
		int compileFailed = 0;// 编译失败次数
		int compileSuccess = 0;// 编译成功次数

		File[] listFiles = runDir.listFiles();
		excuteCount = listFiles.length;
		int topScore = 0;
		for (File file : listFiles) {
			try {
				String content = StreamTools.readStream(new FileInputStream(file));
				RunInfo info = gson.fromJson(content, RunInfo.class);
				// 是否有编译错误
				boolean compile = info.getCompile().isPass();
				if (compile) {
					compileSuccess++;
				} else {
					compileFailed++;
				}

				// 找到当前分数
				int score = info.getScore();
				if (score > topScore) {
					topScore = score;
				}
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println(file.getAbsolutePath());
			}
		}

		return new int[] { excuteCount, compileSuccess, compileFailed, topScore };
	}
}
