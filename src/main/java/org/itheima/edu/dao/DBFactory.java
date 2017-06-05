package org.itheima.edu.dao;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class DBFactory {

	@Autowired
	private DataSource dataSource;

	@Value("${config.db.path}")
	String dbPath;

	@PostConstruct
	public void initialize() {
		File file = new File(dbPath);
		try {
			file = file.getCanonicalFile();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		boolean exists = file.exists();
		if (!exists || file.length() == 0) {
			System.out.println("初始化数据库: " + file.getAbsolutePath());
			Connection connection = null;
			Statement statement = null;
			try {
				connection = dataSource.getConnection();
				statement = connection.createStatement();

//				// admin 表
//				statement.execute(dropAdminSql());
//				statement.executeUpdate(createAdminSql());
//
//				// campus 表
//				statement.execute(dropCampusSql());
//				statement.executeUpdate(createCampusSql());
//
				// class 表
				statement.execute(dropClassSql());
				statement.executeUpdate(createClassSql());
//
//				// exam 表
//				statement.execute(dropExamSql());
//				statement.executeUpdate(createExamSql());
//
//				// question 表
//				statement.execute(dropQuestionSql());
//				statement.executeUpdate(createQuestionSql());
//
//				// stage 表
//				statement.execute(dropStageSql());
//				statement.executeUpdate(createStageSql());
//
//				// stage_question 表
//				statement.execute(dropStageQuestionSql());
//				statement.executeUpdate(createStageQuestionSql());
//
//				// student 表
//				statement.execute(dropStudentSql());
//				statement.executeUpdate(createStudentSql());

				// token 表
				statement.execute(dropTokenSql());
				statement.executeUpdate(createTokenSql());

				// user 表
				statement.execute(dropUserSql());
				statement.executeUpdate(createUserSql());

			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				try {
					statement.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				statement = null;
				try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				connection = null;
			}
		}
	}

	private String dropAdminSql() {
		return drop("T_ADMIN");
	}

	private String createAdminSql() {
		StringBuilder builder = new StringBuilder();
		builder.append("CREATE TABLE T_ADMIN(");
		builder.append("ID INTEGER Primary key,");
		builder.append("ACCOUNT varchar(64) unique not null,");
		builder.append("PASSWORD varchar(64) not null");
		builder.append(")");
		return builder.toString();
	}

	private String dropUserSql() {
		return drop("T_USER");
	}

	private String createUserSql() {
		StringBuilder builder = new StringBuilder();
		builder.append("CREATE TABLE T_USER(");
		builder.append("ID INTEGER primary key autoincrement,");
		builder.append("NAME varchar(64) not null,");
		builder.append("ACCOUNT varchar(64) unique not null,");
		builder.append("PASSWORD varchar(64) not null,");
		builder.append("LOGIN BOOLEAN default 0,");
		builder.append("SUBMIT BOOLEAN default 0,");
		builder.append("CLASS_ID INTEGER");
		builder.append(")");
		return builder.toString();
	}

	private String dropCampusSql() {
		return drop("T_CAMPUS");
	}

	private String createCampusSql() {
		StringBuilder builder = new StringBuilder();
		builder.append("CREATE TABLE T_CAMPUS(");
		builder.append("ID INTEGER Primary key,");
		builder.append("NAME varchar(64) unique not null");
		builder.append(")");
		return builder.toString();
	}

	private String dropTokenSql() {
		return drop("T_TOKEN");
	}

	private String createTokenSql() {
		StringBuilder builder = new StringBuilder();
		builder.append("CREATE TABLE T_TOKEN(");
		builder.append("ID INTEGER primary key autoincrement,");
		builder.append("USER_ID INTEGER not null,");
		builder.append("TOKEN varchar(64) not null,");
		builder.append("STATE INTEGER default 0,");
		builder.append("CREATE_DATE DATE not null,");
		builder.append("DURATION INTEGER not null");
		builder.append(")");
		return builder.toString();
	}

	private String dropExamSql() {
		return drop("T_EXAM");
	}

	private String createExamSql() {
		StringBuilder builder = new StringBuilder();
		builder.append("CREATE TABLE T_EXAM(");
		builder.append("ID INTEGER Primary key,");
		builder.append("USER_ID INTEGER not null,");
		builder.append("STAGE_ID INTEGER not null,");
		builder.append("CLASS_ID INTEGER not null,");
		builder.append("STATE INTEGER default 0,");
		builder.append("CREATE_TIME DATE not null,");
		builder.append("DURATION INTEGER not null,");
		builder.append("BEGIN_TIME DATE not null,");
		builder.append("END_TIME DATE not null");
		builder.append(")");
		return builder.toString();
	}

	private String dropClassSql() {
		return drop("T_CLASS");
	}

	private String createClassSql() {
		StringBuilder builder = new StringBuilder();
		builder.append("CREATE TABLE T_CLASS(");
		builder.append("ID INTEGER primary key autoincrement,");
		builder.append("NAME varchar(64) not null,");
//		builder.append("CAMPUS_ID INTEGER");
		builder.append("COMPLETE BOOLEAN default 0");
		builder.append(")");
		return builder.toString();
	}

	private String dropQuestionSql() {
		return drop("T_QUESTION");
	}

	private String createQuestionSql() {
		StringBuilder builder = new StringBuilder();
		builder.append("CREATE TABLE T_QUESTION(");
		builder.append("ID INTEGER Primary key,");
		builder.append("TITLE varchar(64) not null,");
		builder.append("DESCRIPTION varchar(256) not null,");
		builder.append("HTML varchar(256) not null,");
		builder.append("PATH varchar(256) not null");
		builder.append(")");
		return builder.toString();
	}

	private String dropStageSql() {
		return drop("T_STAGE");
	}

	private String createStageSql() {
		StringBuilder builder = new StringBuilder();
		builder.append("CREATE TABLE T_STAGE(");
		builder.append("ID INTEGER Primary key,");
		builder.append("NAME varchar(256) not null");
		builder.append(")");
		return builder.toString();
	}

	private String dropStageQuestionSql() {
		return drop("T_STAGE_QUESTION");
	}

	private String createStageQuestionSql() {
		StringBuilder builder = new StringBuilder();
		builder.append("CREATE TABLE T_STAGE_QUESTION(");
		builder.append("ID INTEGER Primary key,");
		builder.append("STAGE_ID INTEGER not null,");
		builder.append("QUESTION_ID INTEGER not null");
		builder.append(")");
		return builder.toString();
	}

	private String dropStudentSql() {
		return drop("T_STUDENT");
	}

	private String createStudentSql() {
		StringBuilder builder = new StringBuilder();
		builder.append("CREATE TABLE T_STUDENT(");
		builder.append("ID INTEGER Primary key,");
		builder.append("NAME varchar(64) not null,");
		builder.append("ACCOUNT varchar(64) not null,");
		builder.append("IDCARD varchar(64) not null,");
		builder.append("CLASS_ID INTEGER not null");
		builder.append(")");
		return builder.toString();
	}

	private String drop(String table) {
		return "DROP TABLE IF EXISTS ".concat(table);
	}
}
