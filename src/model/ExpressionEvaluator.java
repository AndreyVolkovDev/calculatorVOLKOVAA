package model;

import java.util.Stack;

/**
 * Вычислитель математических выражений.
 * Использует алгоритм на основе двух стеков (вариация "сортировочной станции").
 */
public class ExpressionEvaluator {

    public double evaluate(String expression) {
        // Заменяем текстовые операторы на односимвольные для удобства парсинга
        expression = expression.replace(" ", "")
                .replace("**", "^")
                .replace("//", "#");

        Stack<Double> values = new Stack<>(); // Стек для чисел
        Stack<Character> ops = new Stack<>(); // Стек для операторов

        for (int i = 0; i < expression.length(); i++) {
            char c = expression.charAt(i);

            if (Character.isDigit(c)) {
                StringBuilder sb = new StringBuilder();
                // Читаем всё число, включая десятичную часть
                while (i < expression.length() && (Character.isDigit(expression.charAt(i)) || expression.charAt(i) == '.')) {
                    sb.append(expression.charAt(i++));
                }
                i--; // Коррекция индекса после выхода из цикла
                values.push(Double.parseDouble(sb.toString()));
            } else if (c == '(') {
                ops.push(c);
            } else if (c == ')') {
                // Вычисляем всё до открывающей скобки
                while (ops.peek() != '(') {
                    values.push(applyOp(ops.pop(), values.pop(), values.pop()));
                }
                ops.pop(); // Удаляем открывающую скобку
            } else if (isOperator(c)) {
                // Пока на вершине стека оператор с большим или равным приоритетом, вычисляем его
                while (!ops.empty() && hasPrecedence(c, ops.peek())) {
                    values.push(applyOp(ops.pop(), values.pop(), values.pop()));
                }
                ops.push(c);
            }
        }

        // Вычисляем оставшиеся операции
        while (!ops.empty()) {
            values.push(applyOp(ops.pop(), values.pop(), values.pop()));
        }

        // На вершине стека значений остается финальный результат
        return values.pop();
    }

    private boolean isOperator(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/' || c == '^' || c == '%' || c == '#';
    }

    // Проверка приоритета операторов. Возвращает true, если op2 имеет больший или равный приоритет, чем op1.
    private boolean hasPrecedence(char op1, char op2) {
        if (op2 == '(' || op2 == ')') {
            return false;
        }
        if ((op1 == '^') && (op2 != '^')) {
            return false; // Возведение в степень имеет наивысший приоритет
        }
        if ((op1 == '*' || op1 == '/' || op1 == '%' || op1 == '#') && (op2 == '+' || op2 == '-')) {
            return false;
        }
        return true;
    }

    // Применение операции
    private double applyOp(char op, double b, double a) {
        switch (op) {
            case '+': return a + b;
            case '-': return a - b;
            case '*': return a * b;
            case '/':
                if (b == 0) throw new UnsupportedOperationException("Деление на ноль невозможно");
                return a / b;
            case '^': return Math.pow(a, b);
            case '%': return a % b;
            case '#': // Целочисленное деление
                if (b == 0) throw new UnsupportedOperationException("Деление на ноль невозможно");
                return Math.floor(a / b);
        }
        return 0;
    }
}