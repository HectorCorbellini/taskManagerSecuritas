package com.securitas;

import java.util.Scanner;

public class InputHandler {
    private static final Scanner scanner = new Scanner(System.in);

    public void showMenu() {
        System.out.println("\n===== Task Manager Menu =====");
        System.out.println("1. Add New Location");
        System.out.println("2. Add New Shift");
        System.out.println("3. Add New Assignment");
        System.out.println("4. View Tomorrow's Assignment");
        System.out.println("5. View Assignments by Date Range");
        System.out.println("6. Update Assignment");
        System.out.println("7. View All Locations");
        System.out.println("8. View Recurring Shifts");
        System.out.println("9. Check if Date is Rest Day");
        System.out.println("0. Exit");
        System.out.print("Enter your choice: ");
    }

    public String getUserInput() {
        return scanner.nextLine();
    }

    public String getUserInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }
}
