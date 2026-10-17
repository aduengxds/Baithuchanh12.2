
package com.codegym.controller;

import com.codegym.dao.UserDAO;
import com.codegym.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet(name = "UserServlet", urlPatterns = "/users")
public class UserServlet extends HttpServlet {

    private UserDAO userDAO;

    @Override
    public void init() {
        userDAO = new UserDAO();
    }

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");

        try {
            if (action == null || action.isBlank()
                    || "list".equals(action)) {
                listUsers(request, response);
            } else {
                switch (action) {
                    case "search":
                        searchUsers(request, response);
                        break;
                    case "sort":
                        sortUsers(request, response);
                        break;
                    case "create":
                        showCreateForm(request, response);
                        break;
                    case "edit":
                        showEditForm(request, response);
                        break;
                    case "delete":
                        showDeleteForm(request, response);
                        break;
                    default:
                        response.sendError(
                                HttpServletResponse.SC_NOT_FOUND
                        );
                }
            }
        } catch (SQLException e) {
            throw new ServletException(
                    "Lỗi khi truy cập cơ sở dữ liệu", e
            );
        }
    }

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");

        try {
            if (action == null) {
                response.sendError(
                        HttpServletResponse.SC_BAD_REQUEST
                );
                return;
            }

            switch (action) {
                case "create":
                    insertUser(request, response);
                    break;
                case "edit":
                    updateUser(request, response);
                    break;
                case "delete":
                    deleteUser(request, response);
                    break;
                default:
                    response.sendError(
                            HttpServletResponse.SC_BAD_REQUEST
                    );
            }
        } catch (SQLException e) {
            throw new ServletException(
                    "Lỗi khi thao tác với cơ sở dữ liệu", e
            );
        }
    }

    // Hiển thị toàn bộ danh sách người dùng
    private void listUsers(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws SQLException, ServletException, IOException {

        List<User> users = userDAO.selectAllUsers();
        request.setAttribute("users", users);

        request.getRequestDispatcher("/user/list.jsp")
                .forward(request, response);
    }

    // Tìm kiếm người dùng theo quốc gia
    private void searchUsers(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws SQLException, ServletException, IOException {

        String country = request.getParameter("country");

        if (country == null) {
            country = "";
        }

        List<User> users = userDAO.searchByCountry(country);

        request.setAttribute("country", country);
        request.setAttribute("users", users);

        request.getRequestDispatcher("/user/list.jsp")
                .forward(request, response);
    }

    // Sắp xếp người dùng theo tên A-Z
    private void sortUsers(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws SQLException, ServletException, IOException {

        List<User> users = userDAO.sortByName();
        request.setAttribute("users", users);

        request.getRequestDispatcher("/user/list.jsp")
                .forward(request, response);
    }

    // Hiển thị form thêm người dùng
    private void showCreateForm(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {

        request.getRequestDispatcher("/user/create.jsp")
                .forward(request, response);
    }

    // Hiển thị form sửa người dùng
    private void showEditForm(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws SQLException, ServletException, IOException {

        int id = parseId(request, response);
        if (id == -1) {
            return;
        }

        User user = userDAO.selectUser(id);

        if (user == null) {
            response.sendError(
                    HttpServletResponse.SC_NOT_FOUND,
                    "Không tìm thấy người dùng."
            );
            return;
        }

        request.setAttribute("user", user);
        request.getRequestDispatcher("/user/edit.jsp")
                .forward(request, response);
    }

    // Hiển thị form xác nhận xóa
    private void showDeleteForm(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws SQLException, ServletException, IOException {

        int id = parseId(request, response);
        if (id == -1) {
            return;
        }

        User user = userDAO.selectUser(id);

        if (user == null) {
            response.sendError(
                    HttpServletResponse.SC_NOT_FOUND,
                    "Không tìm thấy người dùng."
            );
            return;
        }

        request.setAttribute("user", user);
        request.getRequestDispatcher("/user/delete.jsp")
                .forward(request, response);
    }

    // Thêm người dùng
    private void insertUser(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws SQLException, IOException {

        User user = new User(
                request.getParameter("name"),
                request.getParameter("email"),
                request.getParameter("country")
        );

        userDAO.insertUser(user);
        redirectToList(request, response);
    }

    // Cập nhật người dùng
    private void updateUser(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws SQLException, IOException {

        int id = parseId(request, response);
        if (id == -1) {
            return;
        }

        User user = new User(
                id,
                request.getParameter("name"),
                request.getParameter("email"),
                request.getParameter("country")
        );

        userDAO.updateUser(user);
        redirectToList(request, response);
    }

    // Xóa người dùng
    private void deleteUser(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws SQLException, IOException {

        int id = parseId(request, response);
        if (id == -1) {
            return;
        }

        userDAO.deleteUser(id);
        redirectToList(request, response);
    }

    // Kiểm tra ID đầu vào
    private int parseId(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {

        try {
            int id = Integer.parseInt(request.getParameter("id"));

            if (id <= 0) {
                response.sendError(
                        HttpServletResponse.SC_BAD_REQUEST,
                        "ID không hợp lệ."
                );
                return -1;
            }

            return id;
        } catch (NumberFormatException | NullPointerException e) {
            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "ID không hợp lệ."
            );
            return -1;
        }
    }

    // Quay về danh sách sau khi thêm, sửa hoặc xóa
    private void redirectToList(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {

        response.sendRedirect(
                request.getContextPath() + "/users"
        );
    }
}
