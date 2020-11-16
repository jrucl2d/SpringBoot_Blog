<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@include file="../layout/header.jsp"%>
<div class="container">
    <form>
        <input type="hidden" id="updateID" value="${principal.user.id}">
        <div class="form-group">
            <label for="username">Username:</label>
            <input type="text" class="form-control" placeholder="Enter username" id="username" value="${principal.user.username}" readonly>
        </div>
        <div class="form-group">
            <label for="updateEmail">Email:</label>
            <input type="email" class="form-control" placeholder="Enter email" value="${principal.user.email}" id="updateEmail">
        </div>
        <c:if test="${empty principal.user.oauth}">
            <div class="form-group">
                <label for="updatePassword">Password:</label>
                <input type="password" class="form-control" placeholder="Enter password" id="updatePassword">
            </div>
        </c:if>
    </form>
    <button id="btn-update" class="btn btn-primary">회원수정 완료</button>
</div>
<script src="/js/user.js"></script>
<%@include file="../layout/footer.jsp"%>
