package org.itheima.edu.controller;

import org.itheima.edu.bean.ExamBean;
import org.itheima.edu.bean.RunInfoDTO;
import org.itheima.edu.dao.UserEntityDao;
import org.itheima.edu.entity.UserEntity;
import org.itheima.edu.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.*;

/**
 * 题库包管理器
 *
 */
@Controller
public class PackageController {

//	public static final String SEPARATOR = ">>><<<";

	@Value("${config.suffix}")
	String suffix;

	// 远程数据源目录
	@Value("${config.dir.source}")
	String sourceFolder;	// /mnt/disk/source

	// 远程服务数据结果根目录
	@Value("${config.dir.result}")
	String resultFolder; 	// /mnt/disk/result

	// 本地json数据缓存根目录
	@Value("${config.dir.cache}")
	String cacheFolder;		// /bootstrap/cache

	@Autowired
	UserEntityDao userDao;

	/**
	 * 获取指定目录及类型的题目列表
	 * @param folder	章节名称
	 * @param type		题库类型
	 * @return
	 */
	@RequestMapping("/api/list")
	@ResponseBody
	public String list(HttpServletRequest request, String folder, String type) {

		String token = request.getHeader("token");
		UserEntity entry = userDao.findByToken(token);
		if(entry == null){
			return ResponseUtils.error(ResponseCode.LoginError.LOGIN_NEEDED, "请重新登录");
		}

		String accountName = entry.getAccount();

		// 获取指定目录所有package文件路径
//		List<String> allPackages = getAllPackages(sourceFolder + "/" + folder + "/" + type);
		System.out.println(String.format("开始获取 [%1$s] 章节 [%2$s] 类型题库", folder, type));

		// 读取指定目录的缓存数据 账户名/章节/类型.json (aaa/02_Computer_think/lesson.json)
		File localCacheFile = new File(cacheFolder, String.format("/%1$s/%2$s/%3$s.json", accountName, folder, type));
		if(localCacheFile.exists() && localCacheFile.length() > 0){
			// 缓存存在
			// /root  E:/cms/data/json/Java开发入门/lesson.json
			// /root  /bootstap/cache/json/Java开发入门/lesson.json
			String str = FileUtils.readFile(localCacheFile);
			if(str != null){
//				List<String> jsons = Arrays.asList(str.split(SEPARATOR));
				List<String> jsons = JsonUtils.fromJson(str, List.class);
				System.out.println("从缓存获取到数据, 直接返回");
				return ResponseUtils.success(jsons);
			}
		}

		// 数据不存在, 获取指定 章节/分类下的题目结构, 调用远程服务, 获取json结果
//		List<String> allPackages = getSourceExams(folder, type);

		List<ExamBean.SourceItem> sourceItems = getSourceItems(folder, type);

		if (sourceItems == null || sourceItems.size() == 0) {
			return ResponseUtils.error(ResponseCode.ExamError.EXAM_WATTING, "考试未开始，稍后重试");
		}

		try {
			List<String> jsons = new ArrayList<>();
			for (ExamBean.SourceItem item : sourceItems) {
				String rootDir = getPkgRootDir(accountName, folder, type, item.alias);
				String pkg = sourceFolder + item.file;
				System.out.println("source_pkg: " + pkg + " ==> result_rootDir: " + rootDir);
				String json = Connector.load(pkg, rootDir);

				if (json == null) {
					return ResponseUtils.error(-2, "考试数据加载失败");
				}
				jsons.add(json);

			}

			String content = JsonUtils.toJson(jsons);
			System.out.println("首次读取, 缓存数据到本地");
			// 缓存到本地
			FileUtils.writeFile(localCacheFile, content);

			return ResponseUtils.success(jsons);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// 返回给调用者
		return ResponseUtils.error(-2, "考试数据加载失败");
	}

	public List<ExamBean.SourceItem> getSourceItems(String folder, String type) {
		List<ExamBean.SourceItem> items = new ArrayList<>();
		List<ExamBean> exams = SourceReader.readSource(new File(sourceFolder));
		for (ExamBean exam : exams) {
			if(exam.getExamName().equals(folder)){
				Map<String, List<ExamBean.SourceItem>> maps = exam.getMaps();
				List<ExamBean.SourceItem> sourceItems = maps.get(type);

				if(sourceItems == null) break;

				for (ExamBean.SourceItem sourceItem : sourceItems) {
					items.add(sourceItem);
					System.out.println("path: " + sourceItem.file);
				}
				break;
			}
		}
		return items;
	}

	/**
	 * 上传并编译指定题目
	 *
	 * 格式:/.../result/{accountName}/{folder}/{type}/{pkgName}
	 * 例如:/.../result/bbb/Java开发入门/exercise/01_HelloJava
	 * @param request
//	 * @param folder	章节目录
//	 * @param type		练习类型
//	 * @param pkgName	题目文件夹名称
//	 * @param fileName	文件名称, e.g.  SamplePlugin
//	 * @param content	答题内容		   itg8gyaousdtf76
	 * @return	编译结果
	 */
	@RequestMapping(value = "/api/run", method = RequestMethod.POST)
	@ResponseBody
	public String run(HttpServletRequest request, @RequestBody RunInfoDTO dto) {

		String folder = dto.getFolder();
		String type = dto.getType();
		String pkgName = dto.getPkgName();

		String token = request.getHeader("token");
		UserEntity entry = userDao.findByToken(token);
		if(entry == null) {
			return ResponseUtils.error(ResponseCode.LoginError.LOGIN_NEEDED, "请重新登录");
		}
		String accountName = entry.getAccount();
		String rootDir = String.format(resultFolder + "/%s/%s/%s/%s", accountName, folder, type, pkgName);

		List<RunInfoDTO.RunFile> files = dto.getFiles();
		String json = null;
		System.out.println("执行编译请求: " + rootDir + " fileSize: " + (files == null ? 0 : files.size()));
		Map<String, String> map = new HashMap<>();

		if(files != null){
			for (RunInfoDTO.RunFile file : files) {
				map.put(file.getFileName(), file.getContent());
			}
		}

		try {
			json = Connector.run(rootDir, map);

			if (json != null) {
				return json;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}
		return ResponseUtils.error(-2, "执行失败");
	}


	/**
	 * 运行代码
	 * @param request
	 * @param rootDir 题库根目录
	 *                格式:/root/data/result/{accountName}/{folder}/{type}
	 *                例如:/root/data/result/bbb/Java开发入门/exercise/01_HelloJava
	 * @param fileName	题目名称, e.g.  SamplePlugin
	 * @param content	答题内容
	 * @return
	 */
	@RequestMapping("/api/run1")
	@ResponseBody
	public String run1(HttpServletRequest request, String rootDir, String fileName, String content) {
		System.out.println("执行请求: ");
		Map<String, String> map = new HashMap<>();
		map.put(fileName, content);

		String json = null;
		try {
			json = Connector.run(rootDir, map);

			if (json != null) {
				return json;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}
		return ResponseUtils.error(-2, "执行失败");
	}

	/**
	 * 上传并编译指定题目
	 *
	 * 格式:/root/data/result/{accountName}/{folder}/{type}/{pkgName}
	 * 例如:/root/data/result/bbb/Java开发入门/exercise/01_HelloJava
	 * @param request
	 * @param folder	章节目录
	 * @param type		练习类型
	 * @param pkgName	题目文件夹名称
	 * @param fileName	文件名称, e.g.  SamplePlugin
	 * @param content	答题内容		   itg8gyaousdtf76
	 * @return	编译结果
	 */
	@RequestMapping("/api/run")
	@ResponseBody
	public String run(HttpServletRequest request, String folder, String type, String pkgName, String fileName, String content) {

		String token = request.getHeader("token");
		UserEntity entry = userDao.findByToken(token);
		if(entry == null) {
			return ResponseUtils.error(ResponseCode.LoginError.LOGIN_NEEDED, "请重新登录");
		}
		String accountName = entry.getAccount();
		String rootDir = String.format(resultFolder + "/%s/%s/%s/%s", accountName, folder, type, pkgName);

		String json = null;
		System.out.println("执行编译请求: " + rootDir + " fileName: " + fileName);
		Map<String, String> map = new HashMap<>();
		map.put(fileName, content);
		try {
			json = Connector.run(rootDir, map);

			if (json != null) {
				return json;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			if(json != null){
				// 记录执行结果, 方便客户端获取当前进度
//				saveResult(rootDir, json);
			}
		}
		return ResponseUtils.error(-2, "执行失败");
	}

	@RequestMapping("/api/submit")
	@ResponseBody
	public String submit(HttpServletRequest request, String rootDir, String fileName, String content) {

		Map<String, String> map = new HashMap<>();
		map.put(fileName, content);

		try {
			String json = Connector.submit(rootDir, map);
			if (json != null) {
				return json;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ResponseUtils.error(-2, "提交失败");
	}

	private List<String> getAllPackages(String path) {
		File dir = new File(path);
		try {
			dir = dir.getCanonicalFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (!dir.exists() || !dir.isDirectory()) {
			return null;
		}

		File[] files = dir.listFiles(new FileFilter() {
			@Override
			public boolean accept(File file) {
				if (file.getName().endsWith(suffix)) {
					return true;
				}
				return false;
			}
		});

		if (files == null || files.length == 0) {
			return null;
		}
		List<String> list = new ArrayList<>();

		for (File file : files) {
			String absolutePath = file.getAbsolutePath();
			list.add(absolutePath);
		}
		return list;
	}

	private String getPkgRootDir(String accountName, String folder, String type, String pkgName) {
//		File pkg = new File(pkgPath);
//		String pkgName = pkg.getName();
//		pkgName = pkgName.substring(0, pkgName.lastIndexOf(suffix));
		// /root/data/result			/bbb				/Java开发入门		/lesson	  /01_Welcome
		String path = resultFolder + "/" + accountName + "/" + folder + "/" + type + "/" + pkgName;
		return path;
	}

}
