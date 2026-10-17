<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Thêm người dùng</title>
</head>
<body>
    <h1>Thêm người dùng</h1>

    <form action="${pageContext.request.contextPath}/users" method="post">
        <input type="hidden" name="action" value="create">

        <p>
            <label for="name">Tên:</label><br>
            <input type="text" id="name" name="name"
                   maxlength="120" required>
        </p>

        <p>
            <label for="email">Email:</label><br>
            <input type="email" id="email" name="email"
                   maxlength="220" required>
        </p>

        <p>
            <label for="country">Quốc gia:</label><br>
            <input type="text" id="country" name="country"
                   maxlength="120">
        </p>

        <button type="submit">Lưu</button>
        <a href="${pageContext.request.contextPath}/users">Quay lại</a>
    </form>
</body>
</html>