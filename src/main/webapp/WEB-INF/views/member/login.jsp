<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>로그인 페이지</title>
<style>
	.inner {
	position : absolute;
	width : 300px;
	height : 200px;
	top : 50%;
	left : 50%;
	margin-top : -100px;
	margin-left : -150px;
	background-color : lightgray;
	}
</style>
</head>
<body>
	<div class="inner">
		<h3 align="center">로그인</h3>
		<form action="/member/login.oj" method="post">
			<table align="center">
				<tr align="center">
					<td colspan="2"><input type="text" name="user-id" placeholder="아이디"></td>
				</tr>
				<tr align="center">
					<td colspan="2"><input type="password" name="user-pw" placeholder="비밀번호"></td>	
				</tr>
				<tr align="center">
					<td><input type="submit" value="로그인"></td>
					<td><button type="button" onclick="location.href='/member/join.oj'">회원가입</button>
				</tr>
				<tr align="center">
					<td colspan="2"><button type="button" onclick="location.href='#'">아이디/비밀번호 찾기</button></td>
				</tr>
			</table>	
		</form>
	</div>
</body>
</html>