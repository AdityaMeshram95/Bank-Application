package project2;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class BankApplication {

	/*
	 * @ AUTHOR: Aditya Meshram
	 * 
	 * @ Objective: Bank Application Project
	 */

	private static String jdbcUrl = "jdbc:mysql://localhost:3306/unisoft";
	private static String username = "root";
	private static String password = "root";
	private static String accNumber;
	private static String tableName;

	// Setter method.
	public static void setAccNumber(String accNumber) {
		BankApplication.accNumber = accNumber;
	}

	// Setter method.
	public static void setTableName(String name) { /*
													 * This method is use to ask the table/acc. name from the user since
													 * we hare going to have many acc holders inside the same database
													 * having their own table.
													 */
		tableName = name.toLowerCase().replaceAll(" ", "_");
	}

	// Create a method to create a bank account from user's name
	public static void createAcc(int age) {

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			try (Connection con = DriverManager.getConnection(jdbcUrl, username, password)) {

				if (age < 18) {
					throw new ArithmeticException("Age is less than 18, Acc. can't be created...");
				} else {

					// Create a table
					String query1 = "CREATE TABLE IF NOT EXISTS " + tableName
							+ " (Trans_Date timestamp, accNum varchar(10), Description TEXT, Amt_dect double, new_bal double)";

					Statement smt = con.createStatement();
					smt.execute(query1);

					/*
					 * First insert with 0 balance so that we can create an account number, which we
					 * will going to save inside the static variable and use it every time
					 */
					String query2 = "INSERT INTO " + tableName
							+ "(Trans_date, accNum, Description, new_bal) VALUES(CURRENT_TIMESTAMP, " + accNumber
							+ ", 'Deposit_Amount', 0)";

					smt.executeUpdate(query2);

					System.out.println("Account created successfully...");
					System.out.println("Your account number is: " + accNumber);

					smt.close();
					con.close();
				}
			} catch (SQLException s) {
				s.printStackTrace();
			}
		} catch (ClassNotFoundException c) {
			c.printStackTrace();
		}
	}

	// Create a method to check the account balance
	public static void checkBalance() {

		try (Connection con = DriverManager.getConnection(jdbcUrl, username, password)) {
			String query3 = "SELECT * FROM " + tableName;
			Statement smt = con.createStatement();
			ResultSet rs = smt.executeQuery(query3);

			while (rs.next()) {
				System.out.println(rs.getString("Trans_date") + " " + rs.getString("accNum") + " "
						+ rs.getString("Description") + " " + rs.getDouble("Amt_dect") + " " + rs.getDouble("new_bal"));
			}

			rs.close();
			smt.close();
			con.close();

		} catch (SQLException s) {
			s.printStackTrace();
		}
	}

	// Create a method to WITHDRAW the money
	public static void withdraw(String desc, double amount) {
		try (Connection con = DriverManager.getConnection(jdbcUrl, username, password)) {

			String query4 = "SELECT * FROM " + tableName + " ORDER BY new_bal DESC LIMIT 1";
			Statement smt = con.createStatement();
			ResultSet rs = smt.executeQuery(query4);

			double existingValue = 0;
			String acc_num = "";
			if (rs.next()) {
				existingValue = rs.getDouble("new_bal");
				acc_num = rs
						.getString("accNum"); /*
												 * Since we have created account number by using a class Random which
												 * will going to process random number every time so to avoid changing
												 * of acc. num with every insert, here we took the last inserted value
												 * and save it inside a variable and use it as a new insert again.
												 */
			}

			double newAmount = existingValue - amount;

			if (newAmount < 0) { /* Here we use a conditional statement to avoid the balance to get - minus */
				System.out.println("Low balance....");
				return;
			} else {
				String query5 = "INSERT INTO " + tableName
						+ "(Trans_date, accNum, Description, Amt_dect, new_bal) VALUES(CURRENT_TIMESTAMP, ?, ?, ?, ?)";

				PreparedStatement ps = con.prepareStatement(query5);
				ps.setString(1, acc_num);
				ps.setString(2, desc);
				ps.setDouble(3, amount);
				ps.setDouble(4, newAmount);
				ps.executeUpdate();

				System.out.println("Transaction successful...");

				ps.close();
				smt.close();
				con.close();
			}
		} catch (SQLException s) {
			s.printStackTrace();
		}
	}

	// Create a method to DEPOSIT the money.
	public static void deposit(String desc, double amount) {
		try (Connection con = DriverManager.getConnection(jdbcUrl, username, password)) {

			String query6 = "SELECT * FROM " + tableName + " ORDER BY new_bal DESC LIMIT 1";
			Statement smt = con.createStatement();
			ResultSet rs = smt.executeQuery(query6);

			double existingValue = 0;
			String acc_num = "";
			if (rs.next()) {
				existingValue = rs.getDouble("new_bal");
				acc_num = rs.getString("accNum");
			}

			double newAmount = existingValue + amount;

			String query7 = "INSERT INTO " + tableName
					+ "(Trans_date, accNum, Description, Amt_dect, new_bal) VALUES(CURRENT_TIMESTAMP, ?, ?, ?, ?)";

			PreparedStatement ps = con.prepareStatement(query7);
			ps.setString(1, acc_num);
			ps.setString(2, desc);
			ps.setDouble(3, amount);
			ps.setDouble(4, newAmount);
			ps.executeUpdate();

			System.out.println("Transaction successful...");

			ps.close();
			smt.close();
			con.close();

		} catch (SQLException s) {
			s.printStackTrace();
		}
	}
}
