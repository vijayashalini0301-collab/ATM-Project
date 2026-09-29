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

@WebServlet("/withdraw")
public class WithdrawServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        String amountText = request.getParameter("amount");

        try {
            double amount = Double.parseDouble(amountText);

            if (amount <= 0) {
                out.println("<h3>Enter a valid amount</h3>");
                return;
            }

            Class.forName("com.mysql.cj.jdbc.Driver");

            Connection con = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/atm_db",
                "root",
                "312006"
            );

            String checkSql =
                "SELECT balance FROM users WHERE account_no=?";

            PreparedStatement checkPs = con.prepareStatement(checkSql);
            checkPs.setString(1, "100001");

            ResultSet rs = checkPs.executeQuery();

            if (rs.next()) {

                double balance = rs.getDouble("balance");

                if (amount <= balance) {

                    String updateSql =
                        "UPDATE users SET balance = balance - ? WHERE account_no=?";

                    PreparedStatement updatePs =
                        con.prepareStatement(updateSql);

                    updatePs.setDouble(1, amount);
                    updatePs.setString(2, "100001");

                    updatePs.executeUpdate();

                    // Save withdrawal transaction
                    String transactionSql =
                        "INSERT INTO transactions (account_no, type, amount) VALUES (?, ?, ?)";

                    PreparedStatement transactionPs =
                        con.prepareStatement(transactionSql);

                    transactionPs.setString(1, "100001");
                    transactionPs.setString(2, "WITHDRAW");
                    transactionPs.setDouble(3, amount);

                    transactionPs.executeUpdate();

                    out.println("<h2>Withdrawal Successful!</h2>");
                    out.println("<h3>Amount Withdrawn: ₹" + amount + "</h3>");
                    out.println("<h3>Remaining Balance: ₹"
                                + (balance - amount) + "</h3>");
                    out.println("<br><a href='home.html'>Back to ATM Menu</a>");

                } else {
                    out.println("<h2>Insufficient Balance</h2>");
                    out.println("<h3>Available Balance: ₹"
                                + balance + "</h3>");
                    out.println("<br><a href='home.html'>Back to ATM Menu</a>");
                }

            } else {
                out.println("<h3>Account not found</h3>");
            }

            con.close();

        } catch (Exception e) {
            out.println("<h3>Error: " + e.getMessage() + "</h3>");
        }
    }
}