package project2;

import java.util.Random;
import java.util.Scanner;

public class Test {

	public static void main(String[] args) {

		Scanner sc = new Scanner(System.in);

		while (true) {

			// List of Banking options
			System.out.println("=== WELCOME TO BANKING MENU ===");
			System.out.println("1. Create an account.");
			System.out.println("2. Check account details.");
			System.out.println("3. Withdrawl amount.");
			System.out.println("4. Deposite amount.");
			System.out.println("5. Exit banking.");
			System.out.println("\n\nEnter your choice.....");

			int choice = sc.nextInt();
			sc.nextLine();

			switch (choice) {

			case 1:

				System.out.println("Enter the acc holder's name: ");
				String name = sc.next();
				System.out.println("Enter the acc. holder's age: ");
				int age = sc.nextInt();

				// Generate random number to be used as acc. number.
				Random num = new Random();
				int randomNum = num.nextInt(1000000000);
				final String random10DigitNumber = String.format("%010d", randomNum);

				// set the accNum and tableName
				BankApplication.setAccNumber(random10DigitNumber);
				BankApplication.setTableName(name);

				// Call the create account method.
				BankApplication.createAcc(age);
				break;

			case 2:

				System.out.println("Enter the acc. user name");
				String name1 = sc.next();
				BankApplication.setTableName(name1);
				
				System.out.println("==== Account Details ====");
				BankApplication.checkBalance();
				System.out.println();
				System.out.println();
				break;

			case 3:
				
				System.out.println("Enter the acc. user name");
				String name2 = sc.next();
				BankApplication.setTableName(name2);

				System.out.println("Enter the paymenet details.");
				String details = sc.next();
				System.out.println("Enter the amount to withdraw.");
				double amount = sc.nextDouble();
				sc.nextLine();

				// call the method
				BankApplication.withdraw(details, amount);
				break;

			case 4:
				
				System.out.println("Enter the acc. user name");
				String name3 = sc.next();
				BankApplication.setTableName(name3);

				System.out.println("Enter the paymenet details.");
				String detail = sc.next();
				System.out.println("Enter the amount to deposit");
				double amt = sc.nextDouble();
				sc.nextLine();

				// call the method
				BankApplication.deposit(detail, amt);
				break;

			case 5:
				sc.close();
				System.exit(0);
				break;

			default:
				System.out.println("Invalid choice...");
			}
		}
	}
}
