<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>사전 상세 페이지</title>
<style>
#memeDetailTable {
	width: 95%;
	background-color: #eeeeee;
	border: 1px solid black;
	text-align: center;
	margin: auto;
}
</style>
</head>
<body>
	<br>
	<br>
	<h1 align="center">MEME DICTIONARY</h1>
	<br>
	<hr>
	<br>
	<table id="memeDetailTable" align="center" border=1>
		<tr>
			<td>유행어</td>
			<td>${meme.memeName}</td>
		</tr>
		<tr>
			<td>유행시기</td>
			<td>${meme.memeDate}</td>
		</tr>
		<tr>
			<td height="300">유행어 설명</td>
			<td width="700">${meme.memeContents}</td>
		</tr>
		<tr>
			<td>첨부파일</td>
			<td><img
				src="/resources/memeUploadFiles/${memeFile.memeFileRename }">
			</td>
		</tr>
		<tr>
			<td>등재요청자</td>
			<td>${meme.memberNickname}</td>
		</tr>
	</table>

	<br>
	<hr>

	<div align="right">
		<a href="/meme/requestView?memeName=${meme.memeName}&memeNo=${meme.memeNo}"> 의견내기 &nbsp;&nbsp;</a>
	</div>


	<br>
	<br>
	<br>

</body>
</html>