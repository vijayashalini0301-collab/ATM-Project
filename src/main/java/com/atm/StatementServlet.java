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

@WebServlet("/statement")
public class StatementServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        try {

            Class.forName("com.mysql.cj.jdbc.Driver");

            Connection con = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/atm_db",
                "root",
                "312006"
            );

            String sql = "SELECT * FROM transactions " +
                         "WHERE account_no=? " +
                         "ORDER BY transaction_date DESC";

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, "100001");

            ResultSet rs = ps.executeQuery();

            // HTML START
            out.println("<html>");

            out.println("<head>");
            out.println("<title>Mini Statement</title>");
            out.println("<link rel='stylesheet' href='style.css'>");
            out.println("</head>");

            out.println("<body>");

            out.println("<div class='atm-container'>");

            out.println("<div class='atm-card statement-card'>");

            out.println("<h2>ATM MANAGEMENT SYSTEM</h2>");

            out.println("<h3>Mini Statement</h3>");

            out.println("<table class='statement-table'>");

            out.println("<tr>");
            out.println("<th>ID</th>");
            out.println("<th>Account No</th>");
            out.println("<th>Type</th>");
            out.println("<th>Amount</th>");
            out.println("<th>Date & Time</th>");
            out.println("</tr>");

            while (rs.next()) {

                out.println("<tr>");

                out.println("<td>"
                        + rs.getInt("id")
                        + "</td>");

                out.println("<td>"
                        + rs.getString("account_no")
                        + "</td>");

                out.println("<td>"
                        + rs.getString("type")
                        + "</td>");

                out.println("<td>₹"
                        + rs.getDouble("amount")
                        + "</td>");

                out.println("<td>"
                        + rs.getTimestamp("transaction_date")
                        + "</td>");

                out.println("</tr>");
            }

            out.println("</table>");

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