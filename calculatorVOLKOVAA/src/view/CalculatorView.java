package view;

import model.CalculationEntry;

import java.util.List;
import java.util.Scanner;

public class CalculatorView {
    private final Scanner scanner = new Scanner(System.in);

    public void displayMenu() {
        System.out.println("\n===== Меню Калькулятора =====");
        System.out.println("1. Ввести уравнение для расчета");
        System.out.println("2. Посмотреть историю расчетов");
        System.out.println("3. Сохранить всю историю в файл");
        System.out.println("4. Выбрать и сохранить записи из истории в файл");
        System.out.println("5. Выход");
        System.out.print("Выберите опцию: ");
    }

    public String getInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    public void displayMessage(String message) {
        System.out.println(message);
    }

    public void displayResult(String result) {
        System.out.println("Результат: " + result);
    }

    public void displayHistory(List<CalculationEntry> history) {
        if (history.isEmpty()) {
            System.out.println("История расчетов пуста.");
            return;
        }
        System.out.println("\n--- История расчетов ---");
        for (int i = 0; i < history.size(); i++) {
            System.out.println((i + 1) + ". " + history.get(i));
        }
        System.out.println("------------------------");
    }

    public void close() {
        scanner.close();
    }
}