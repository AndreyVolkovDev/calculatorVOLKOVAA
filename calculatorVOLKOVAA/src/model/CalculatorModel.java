package model;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CalculatorModel {
    private final List<CalculationEntry> history = new ArrayList<>();
    private final ExpressionEvaluator evaluator = new ExpressionEvaluator();
    private static final String HISTORY_FILE = "calculator_history.log";
    private static final String HISTORY_SEPARATOR = ";;;";

    public CalculatorModel() {
        loadHistory();
    }

    public String calculateAndStore(String expression) {
        try {
            double result = evaluator.evaluate(expression);
            String resultString = (result == Math.floor(result)) ? String.valueOf((long)result) : String.valueOf(result);
            history.add(new CalculationEntry(expression, resultString));
            saveHistory();
            return resultString;
        } catch (Exception e) {
            return "Ошибка в выражении: " + e.getMessage();
        }
    }

    public List<CalculationEntry> getHistory() {
        return new ArrayList<>(history); // Возвращаем копию, чтобы избежать изменений извне
    }

    private void loadHistory() {
        File file = new File(HISTORY_FILE);
        if (!file.exists()) {
            return;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(HISTORY_SEPARATOR);
                if (parts.length == 2) {
                    history.add(new CalculationEntry(parts[0], parts[1]));
                }
            }
        } catch (IOException e) {
            System.err.println("Ошибка загрузки истории: " + e.getMessage());
        }
    }

    private void saveHistory() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(HISTORY_FILE))) {
            for (CalculationEntry entry : history) {
                writer.write(entry.expression() + HISTORY_SEPARATOR + entry.result());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Ошибка сохранения истории: " + e.getMessage());
        }
    }

    public String exportHistory(String userInput) {
        return saveToFile(userInput, history);
    }

    public String exportSelectedHistory(String userInput, List<Integer> indices) {
        List<CalculationEntry> selectedEntries = indices.stream()
                .filter(i -> i > 0 && i <= history.size())
                .map(i -> history.get(i - 1))
                .collect(Collectors.toList());

        if (selectedEntries.isEmpty()) {
            return "Не выбрано ни одной корректной записи для экспорта.";
        }

        return saveToFile(userInput, selectedEntries);
    }

    private String saveToFile(String userInput, List<CalculationEntry> entriesToSave) {
        try {
            Path path = resolvePath(userInput);
            Files.createDirectories(path.getParent()); // Создаем родительские папки, если их нет

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(path.toFile()))) {
                for (CalculationEntry entry : entriesToSave) {
                    writer.write(entry.toString());
                    writer.newLine();
                }
            }
            return "История успешно сохранена в файл: " + path.toAbsolutePath();
        } catch (IOException e) {
            return "Ошибка при сохранении файла: " + e.getMessage();
        }
    }

    private Path resolvePath(String userInput) {
        if (userInput == null || userInput.isBlank()) {
            // Случай 1: Пользователь ничего не ввел
            return Paths.get(HISTORY_FILE).toAbsolutePath();
        }

        Path inputPath = Paths.get(userInput);

        // Случай 4: Указан абсолютный путь с именем файла
        if (inputPath.isAbsolute()) {
            return inputPath;
        }

        // Случай 3: Указан путь (считаем, что это директория)
        if (userInput.endsWith("/") || userInput.endsWith("\\")) {
            return Paths.get(userInput, "log.log");
        }

        // Случай 2: Указано имя файла и расширение
        String lowerCaseInput = userInput.toLowerCase();
        if (lowerCaseInput.endsWith(".txt") || lowerCaseInput.endsWith(".log") || lowerCaseInput.endsWith(".md")) {
            return Paths.get(userInput); // Сохраняем в текущей директории
        }

        // По умолчанию, если не подошло ни одно из явных правил,
        // но пользователь что-то ввел, считаем это путем к директории
        return Paths.get(userInput, "log.log");
    }
}