package com.atm;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/changepin")
public class ChangePinServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request,
                           HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        String newPin = request.getParameter("newpin");

        try {

            Class.forName("com.mysql.cj.jdbc.Driver");

            Connection con = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/atm_db",
                "root",
                "312006"
            );

            String sql =
                "UPDATE users SET pin=? WHERE account_no=?";

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, newPin);
            ps.setString(2, "100001");

            int result = ps.executeUpdate();

            if (result > 0) {

                out.println("<h2>PIN Changed Successfully!</h2>");
                out.println("<h3>Your new PIN has been saved.</h3>");
                out.println("<br><a href='home.html'>Back to ATM Menu</a>");

            } else {

                out.println("<h3>Account not found</h3>");
            }

            con.close();

        } catch (Exception e) {

            out.println("<h3>Error: " + e.getMessage() + "</h3>");
        }
    }
}