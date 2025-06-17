package model;

import java.util.Stack;
import java.util.function.DoubleBinaryOperator;

public class ExpressionEvaluator {

    // Метод для вычисления значения выражения
    public double evaluate(String expression) {
        // Заменяем пользовательские операторы на внутренние односимвольные для упрощения парсинга
        expression = expression.replace(" ", "")
                .replace("**", "^")
                .replace("//", "#");

        Stack<Double> values = new Stack<>();
        Stack<Character> ops = new Stack<>();

        for (int i = 0; i < expression.length(); i++) {
            char c = expression.charAt(i);

            if (Character.isDigit(c)) {
                StringBuilder sb = new StringBuilder();
                // Читаем всё число, включая десятичную часть
                while (i < expression.length() && (Character.isDigit(expression.charAt(i)) || expression.charAt(i) == '.')) {
                    sb.append(expression.charAt(i++));
                }
                i--; // Корректируем индекс после цикла
                values.push(Double.parseDouble(sb.toString()));
            } else if (c == '(') {
                ops.push(c);
            } else if (c == ')') {
                while (ops.peek() != '(') {
                    values.push(applyOp(ops.pop(), values.pop(), values.pop()));
                }
                ops.pop(); // убираем '('
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

        return values.pop();
    }

    private boolean isOperator(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/' || c == '^' || c == '%' || c == '#';
    }

    // Проверка приоритета операторов
    private boolean hasPrecedence(char op1, char op2) {
        if (op2 == '(' || op2 == ')') {
            return false;
        }
        // У оператора '^' (возведение в степень) самый высокий приоритет
        if ((op1 == '^') && (op2 == '*' || op2 == '/' || op2 == '%' || op2 == '#'|| op2 == '+' || op2 == '-')) {
            return false;
        }
        if ((op1 == '*' || op1 == '/' || op1 == '%' || op1 == '#') && (op2 == '+' || op2 == '-')) {
            return false;
        }
        return true;
    }

    // Выполнение операции
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