package com.atm;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/balance")
public class BalanceServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        String accountNo = "100001";

        try {

            Class.forName("com.mysql.cj.jdbc.Driver");

            Connection con = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/atm_db",
                "root",
                "312006"
            );

            String sql = "SELECT balance FROM users WHERE account_no=?";

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, accountNo);

            ResultSet rs = ps.executeQuery();

            out.println("<html>");

            out.println("<head>");
            out.println("<title>Balance Enquiry</title>");
            out.println("<link rel='stylesheet' href='style.css'>");
            out.println("</head>");

            out.println("<body>");

            out.println("<div class='atm-container'>");

            out.println("<div class='atm-card'>");

            out.println("<h2>ATM MANAGEMENT SYSTEM</h2>");

            out.println("<h3>Balance Enquiry</h3>");

            if (rs.next()) {

                out.println("<p>Account Number</p>");

                out.println("<h3>" + accountNo + "</h3>");

                out.println("<p>Available Balance</p>");

                out.println("<h2>₹"
                        + rs.getDouble("balance")
                        + "</h2>");

            } else {

                out.println("<h3>Account not found</h3>");
            }

            out.println("<br>");

            out.println("<a href='home.html'>← Back to ATM Menu</a>");

            out.println("</div>");

            out.println("</div>");

            out.println("</body>");

            out.println("</html>");

            con.close();

        } catch (Exception e) {

            out.println("<h3>Error: "
                    + e.getMessage()
                    + "</h3>");
        }
    }
}