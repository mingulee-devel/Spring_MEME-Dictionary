<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<h1>퀴즈 결과</h1>
	<c:forEach var="i" items="${userAnswer}" varStatus="vs">
    	문제 : ${quizQuest[vs.index]} <br>
    	<c:if test="${not empty quizCh1[vs.index]}">
    	보기 <br>
	    	${quizCh1[vs.index]} <br>
	    	${quizCh2[vs.index]} <br>
	    	${quizCh3[vs.index]} <br>
	    	${quizCh4[vs.index]} <br>
    	</c:if>
    	정답 : ${quizAnswer[vs.index]} <br>
    	나의 답 : ${i } <br>
    	신고 : ${quizNo[vs.index]}
    	<br><br><br>
	</c:forEach>
	최고 기록 : ${score }
</body>
</html>