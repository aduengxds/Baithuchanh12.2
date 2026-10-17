<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Quản lý người dùng</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 40px;
        }

        table {
            border-collapse: collapse;
            width: 100%;
            margin-top: 20px;
        }

        th, td {
            border: 1px solid #ccc;
            padding: 10px;
            text-align: left;
        }

        th {
            background: #eee;
        }

        a, button {
            margin-right: 8px;
        }
    </style>
</head>
<body>
    <h1>Danh sách người dùng</h1>

    <a href="${pageContext.request.contextPath}/users?action=create">
        Thêm người dùng
    </a>

    <table>
        <thead>
            <tr>
                <th>ID</th>
                <th>Tên</th>
                <th>Email</th>
                <th>Quốc gia</th>
                <th>Thao tác</th>
            </tr>
        </thead>

        <tbody>
            <c:choose>
                <c:when test="${empty users}">
                    <tr>
                        <td colspan="5">Chưa có người dùng nào.</td>
                    </tr>
                </c:when>

                <c:otherwise>
                    <c:forEach var="user" items="${users}">
                        <tr>
                            <td><c:out value="${user.id}"/></td>
                            <td><c:out value="${user.name}"/></td>
                            <td><c:out value="${user.email}"/></td>
                            <td><c:out value="${user.country}"/></td>
                            <td>
                                <a href="${pageContext.request.contextPath}/users?action=edit&id=${user.id}">
                                    Sửa
                                </a>

                                <a href="${pageContext.request.contextPath}/users?action=delete&id=${user.id}">
                                    Xóa
                                </a>
                            </td>
                        </tr>
                    </c:forEach>
                </c:otherwise>
            </c:choose>
        </tbody>
    </table>
</body>
</html>