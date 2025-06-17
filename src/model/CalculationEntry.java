package model;

/**
 * Класс-запись для хранения неизменяемой пары "выражение-результат".
 */
public record CalculationEntry(String expression, String result) {
    @Override
    public String toString() {
        return "Выражение: " + expression + " -> Результат: " + result;
    }
}