package main;

import controller.CalculatorController;
import model.CalculatorModel;
import view.CalculatorView;

/**
 * Главный класс для запуска приложения калькулятора.
 */
public class Main {
    public static void main(String[] args) {
        CalculatorModel model = new CalculatorModel();
        CalculatorView view = new CalculatorView();
        CalculatorController controller = new CalculatorController(model, view);

        controller.run();
    }
}