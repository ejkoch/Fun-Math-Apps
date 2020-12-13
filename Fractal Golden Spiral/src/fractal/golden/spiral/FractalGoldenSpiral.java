
package fractal.golden.spiral;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.stage.Stage;

/**
 *
 * @author Emerich Koch
 */
public class FractalGoldenSpiral extends Application {

    private int displayWidth = 600;
    private int sceneHeight = 600;
    private int panelWidth = 200;

    private final int defaultIterations = 5;
    private int iterations = defaultIterations;
    private double sizeValue = 0.5;

    private HBox root = new HBox();
    private Pane display = new Pane();
    private VBox panel = new VBox();

    TextField iterationField = new TextField();

    Slider sizeSlider = new Slider(0, 1, 0.5);

    @Override
    public void start(Stage primaryStage) {

        display.setPrefWidth(displayWidth);
        display.setPrefHeight(sceneHeight);
        root.getChildren().add(display);
        panel.setPrefWidth(panelWidth);
        panel.setPrefHeight(sceneHeight);
        panel.setStyle("-fx-background-color: lightgray");
        root.getChildren().add(panel);

        Button btDefault = new Button("Default");
        btDefault.setOnAction(new DefaultHandler());
        panel.getChildren().add(btDefault);

        HBox iterationBox = new HBox();
        iterationBox.getChildren().add(new Label("Iterations: "));

        Button btMinus = new Button("-");
        btMinus.setOnAction(new MinusHandler());
        iterationBox.getChildren().add(btMinus);

        iterationField.setPrefColumnCount(3);
        iterationField.setText(Double.toString(iterations));
        iterationBox.getChildren().add(iterationField);
        panel.getChildren().add(iterationBox);

        Button btPlus = new Button("+");
        btPlus.setOnAction(new PlusHandler());
        iterationBox.getChildren().add(btPlus);

        sizeSlider.setShowTickMarks(true);
        sizeSlider.setShowTickLabels(true);
        sizeSlider.setMajorTickUnit(0.25f);
        sizeSlider.setBlockIncrement(0.1f);
        panel.getChildren().add(new Label("Size"));
        panel.getChildren().add(sizeSlider);

        Button btDisplay = new Button("Display");
        btDisplay.setOnAction(new DisplayHandler());
        panel.getChildren().add(btDisplay);

        Scene scene = new Scene(root, displayWidth + panelWidth, sceneHeight);

        print();

        primaryStage.setTitle("Golden Spiral");
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    public void print() {

        int[] fib = new int[iterations];
        fib[0] = 1;
        if (iterations > 1) {
            fib[1] = 1;
        }
        if (iterations > 2) {
            for (int i = 2; i < iterations; i++) {
                fib[i] = fib[i - 1] + fib[i - 2];
            }
        }

        int centerX = displayWidth / 2;
        int centerY = sceneHeight / 2;
        int gap = (int) Math.ceil((-1 - 1 / (sizeValue - 1)) * 0.4 * displayWidth / fib[iterations - 1]);
        int radius = gap;
        int startAngle = 0;
        Arc arc = new Arc(centerX, centerY, radius, radius, startAngle, 90);
        arc.setFill(Color.TRANSPARENT);
        arc.setStroke(Color.BLACK);
        display.getChildren().add(arc);

        for (int j = 1; j < iterations; j++) {
            int temp = radius;
            radius = gap * fib[j];
            startAngle = (startAngle + 90) % 360;
            arc = new Arc(centerX, centerY, radius, radius, startAngle, 90);
            arc.setFill(Color.TRANSPARENT);
            arc.setStroke(Color.BLACK);
            display.getChildren().add(arc);
            switch (j % 4) {
                case 1:
                    centerX += temp;
                    break;
                case 2:
                    centerY -= temp;
                    break;
                case 3:
                    centerX -= temp;
                    break;
                case 0:
                    centerY += temp;
                    break;
            }
        }

    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    private class DefaultHandler implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent e) {
            display.getChildren().clear();
            iterations = defaultIterations;
            iterationField.setText(Double.toString(iterations));
            sizeValue = 0.5;
            sizeSlider.setValue(sizeValue);
            print();
        }
    }

    private class MinusHandler implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent e) {
            display.getChildren().clear();
            if (iterations > 0) {
                iterations--;

                iterationField.setText(Double.toString(iterations));
                sizeValue = sizeSlider.getValue();
            }
            print();
        }
    }

    private class PlusHandler implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent e) {
            display.getChildren().clear();
            iterations++;
            iterationField.setText(Double.toString(iterations));
            sizeValue = sizeSlider.getValue();
            print();
        }
    }

    private class DisplayHandler implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent e) {
            display.getChildren().clear();
            iterations = (int) Math.floor(Double.parseDouble(iterationField.getText()));
            sizeValue = sizeSlider.getValue();
            print();
        }
    }

}
