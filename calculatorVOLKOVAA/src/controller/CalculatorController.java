package controller;

import model.CalculatorModel;
import view.CalculatorView;

import java.util.ArrayList;
import java.util.List;

public class CalculatorController {
    private final CalculatorModel model;
    private final CalculatorView view;

    public CalculatorController(CalculatorModel model, CalculatorView view) {
        this.model = model;
        this.view = view;
    }

    public void run() {
        boolean running = true;
        while (running) {
            view.displayMenu();
            String choice = view.getInput("");
            switch (choice) {
                case "1" -> handleCalculation();
                case "2" -> handleViewHistory();
                case "3" -> handleExportHistory();
                case "4" -> handleSelectiveExport();
                case "5" -> {
                    running = false;
                    view.displayMessage("Программа завершена.");
                }
                default -> view.displayMessage("Неверный выбор. Пожалуйста, попробуйте снова.");
            }
        }
        view.close();
    }

    private void handleCalculation() {
        String expression = view.getInput("Введите выражение: ");
        String result = model.calculateAndStore(expression);
        view.displayResult(result);
    }

    private void handleViewHistory() {
        view.displayHistory(model.getHistory());
    }

    private void handleExportHistory() {
        String path = view.getInput("Введите путь для сохранения или имя файла (оставьте пустым для информации о файле истории): ");
        String message = model.exportHistory(path);
        view.displayMessage(message);
    }

    private void handleSelectiveExport() {
        handleViewHistory();
        if (model.getHistory().isEmpty()) {
            return;
        }
        String indicesStr = view.getInput("Введите номера записей для сохранения через запятую (например, 1,3,4): ");
        List<Integer> indices = new ArrayList<>();
        try {
            String[] parts = indicesStr.split(",");
            for (String part : parts) {
                indices.add(Integer.parseInt(part.trim()));
            }
        } catch (NumberFormatException e) {
            view.displayMessage("Ошибка: Введены некорректные номера записей.");
            return;
        }

        String path = view.getInput("Введите путь для сохранения или имя файла: ");
        String message = model.exportSelectedHistory(path, indices);
        view.displayMessage(message);
    }
}