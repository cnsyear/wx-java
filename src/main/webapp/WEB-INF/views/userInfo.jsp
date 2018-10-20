<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%> 
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<p>openid：${objUserInfo.openid}</p>
	<p>nickname：${objUserInfo.nickname}</p>
	<p>sex：  <c:if test="${objUserInfo.sex eq '1'}">男</c:if><c:if test="${objUserInfo.sex eq '2'}">女</c:if>
	  <c:if test="${objUserInfo.sex eq '0'}">未知</c:if>
	 </p>
	<p>province：${objUserInfo.province}</p>
	<p>city：${objUserInfo.city}</p>
	<p>country：${objUserInfo.country}</p>
	<p>headimgurl：${objUserInfo.headimgurl}</p>
	<p>privilege：${objUserInfo.privilege}</p>
	<p>unionid：${objUserInfo.unionid}</p>
</body>
</html>