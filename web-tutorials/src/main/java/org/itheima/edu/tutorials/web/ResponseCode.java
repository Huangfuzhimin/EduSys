package org.itheima.edu.tutorials.web;

/**
 * 
 * 全局的响应码：根据后续业务不断往里面添加
 * 
 */
public interface ResponseCode {

	/**
	 * 成功的响应码:暂定0--100(>=0,<100)间的100个状态码
	 *
	 */
	public interface Success {
		int DEFAULT = 0;// 默认成功为0
	}

	/**
	 * 
	 * 全局的错误码:100--300(>=100,<300)
	 *
	 */
	public interface Error {
		int e404 = 100;
		int e400 = 101;
		int e500 = 102;
		int METHOD_NOT_SUPPORT = 103;// 不支持的请求方法:本来要get，用了post
	}

	/**
	 * 登录错误:300--500间
	 *
	 */
	public interface LoginError {
		int LOST_USER = 300;
		int LOST_PWD = 301;
		int USER_NOT_EXSIT = 302;
		int PWD_ERROR = 303;
		int USER_LOGINED = 304;
		int LOGIN_NEEDED = 305; // 用户未登录或登录已超时, 需要重新登录
	}

	/**
	 * 考试错误 500--600
	 *
	 */
	public interface ExamError {
		int EXAM_COMMITED = 500;// 试卷已经提交
		int EXAM_WATTING = 501;// 考试还未开始
		int DATA_LOAD_FAILD = 502;// 考试数据加载失败
		int RUN_EXEC_FAILD = 503;// 考试数据运行失败
		int SUBMIT_FAILD = 504;// 提交考试数据失败
	}


	/**
	 * 字段校验错误码300--500
	 *
	 */
	public interface Validate {
		int ADMIN_ACCOUNT_EMPTY = 300;
		int ADMIN_PASSWORD_EMPTY = 301;
		int ADMIN_MATCH_ERROR = 302;// 用户名和密码不匹配的error
		int CAMPUS_EMPTY = 303;
		int CAMPUS_EXIST = 304;
		int CAMPUS_NOT_EXIST = 305;
		int USER_NAME_EMPTY = 306;
		int USER_ACCOUNT_EMPTY = 307;
		int USER_PASSWORD_EMPTY = 308;
		int USER_PASSWORD_ERROR = 309;
		int USER_EXIST = 310;
		int USER_NOT_EXIST = 311;
		int TOKEN_DURATION_EMPTY = 312;
		int TOKEN_DURATION_ERROR = 313;
		int TOKEN_REPEAT_ACTIVE = 314;
		int TOKEN_NO_ACTIVE = 315;
		int TOKEN_EMPTY = 316;
		int CLASS_EMPTY = 317;
		int CLASS_EXIST = 318;
		int CLASS_NOT_EXIST = 319;
		int STUDENT_NAME_EMPTY = 320;
		int STUDENT_ACCOUNT_EMPTY = 321;
		int STUDENT_IDCARD_EMPTY = 322;
		int STUDENT_IDCARD_REPEAT = 323;
		int STUDENT_IDCARD_FORMAT = 324;
		int STAGE_EMPTY = 325;
		int STAGE_EXIST = 326;
		int STAGE_NOT_EXIST = 327;
	}

	/**
	 * token错误码500--700
	 */
	public interface Token {
		int NOT_SETUP = 500;
		int NOT_EXIST = 501;
		int MATCH_ERROR = 502;
		int DISABLE = 503;
	}

}