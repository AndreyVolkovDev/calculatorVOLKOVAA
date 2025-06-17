package model;

// Используем record для неизменяемого класса-хранилища данных
public record CalculationEntry(String expression, String result) {
    @Override
    public String toString() {
        return "Выражение: " + expression + " -> Результат: " + result;
    }
}