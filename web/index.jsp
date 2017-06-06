<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<script src="js/jquery-2.1.0.js">
</script>

<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Insert title here</title>
</head>
<body>

	<form action="/edu/run" method="post">
		<p>
			username:<br /> <input type="text" name="username" />
		</p>
		
		<p id="questiondesc">
			
		</p>
		
		<p>
			Itheima code:<br />
			<textarea rows="15" cols="100" name="code">
public class Itheima{
					
}
			</textarea>
		</p>
		<script>
			function show_sub(v) {
				 $.get("http://localhost:8080/edu/desc",{"questionid":v}, function(result){
						$("#questiondesc").html(result);
				 });
			}
		</script>
		<p>
			<select name="questionid" 
				onchange="show_sub(this.options[this.options.selectedIndex].value)">
				<option value="" selected="true" disabled="true">选择题目</option>
				<option value="makeChocolate">makeChocolate</option>
				<option value="minCat">minCat</option>
				<option value="has22">has22</option>
				<option value="exam04">题目4</option>
			</select>
		</p>
		<input type="submit" value="Submit" />
	</form>

</body>
</html>