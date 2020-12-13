
package fractal.dragon.curve;

import java.util.LinkedList;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polyline;
import javafx.stage.Stage;

/**
 *
 * @author Emerich Koch
 */
public class FractalDragonCurve extends Application {

    private int displayWidth = 600;
    private int sceneHeight = 600;
    private int panelWidth = 200;

    private final int defaultIterations = 8;
    private int iterations = defaultIterations;
    private int counter = 0;
    private double sizeValue = 0.5;
    private double colorValue = 0.5;
    private boolean dispStart = false;
    private boolean dispColor = false;
    private boolean dispLine = true;

    private HBox root = new HBox();
    private Pane display = new Pane();
    private VBox panel = new VBox();

    TextField iterationField = new TextField();

    Slider sizeSlider = new Slider(0, 1, 0.5);

    ToggleGroup group = new ToggleGroup();
    RadioButton rb1 = new RadioButton("Line");
    RadioButton rb2 = new RadioButton("Circles");
    RadioButton rb3 = new RadioButton("Line + Circles");

    Slider colorSlider = new Slider(0, 1, 0.5);

    @Override
    public void start(Stage primaryStage) {

        display.setPrefWidth(displayWidth * 1.1);
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

        rb1.setToggleGroup(group);
        rb1.setSelected(true);
        rb2.setToggleGroup(group);
        rb3.setToggleGroup(group);

        panel.getChildren().add(rb1);
        panel.getChildren().add(rb2);
        panel.getChildren().add(rb3);

        sizeSlider.setShowTickMarks(true);
        sizeSlider.setShowTickLabels(true);
        sizeSlider.setMajorTickUnit(0.25f);
        sizeSlider.setBlockIncrement(0.1f);
        panel.getChildren().add(new Label("Size"));
        panel.getChildren().add(sizeSlider);

        colorSlider.setShowTickMarks(true);
        colorSlider.setShowTickLabels(true);
        colorSlider.setMajorTickUnit(0.25f);
        colorSlider.setBlockIncrement(0.1f);
        panel.getChildren().add(new Label("Color"));
        panel.getChildren().add(colorSlider);

        Button btDisplay = new Button("Display");
        btDisplay.setOnAction(new DisplayHandler());
        panel.getChildren().add(btDisplay);

        Scene scene = new Scene(root, displayWidth * 1.1 + panelWidth, sceneHeight * 1.1);

        print();

        primaryStage.setTitle("Dragon Curve");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void print() {
        double length;
        int startX1;
        int startY1;
        double gap = Math.pow(2, (double) iterations / 2);
        /*
        switch (iterations % 8) {
            case 1:
            case 2:
                length = sceneHeight / (2 * gap);
                startX1 = displayWidth * 2 / 3;
                startY1 = sceneHeight / 3;
                break;
            case 3:
            case 4:
                length = displayWidth / (2 * gap);
                startX1 = displayWidth / 3;
                startY1 = sceneHeight / 3;
                break;
            case 5:
            case 6:
                length = sceneHeight / (2 * gap);
                startX1 = displayWidth / 3;
                startY1 = sceneHeight * 2 / 3;
                break;
            case 7:
            case 0:
                length = displayWidth / (2 * gap);
                startX1 = displayWidth * 2 / 3;
                startY1 = sceneHeight * 2 / 3;
                break;
            default:
                length = 1;
                startX1 = displayWidth / 2;
                startY1 = sceneHeight / 2;
        }
         */
        length = (Math.pow(4, sizeValue) - 1) * sceneHeight / (2.5 * gap);
        startX1 = displayWidth / 2;
        startY1 = sceneHeight / 2;
        if (length < 1) {
            length = 1;
        }
        int startX2 = startX1 - (int) length;
        int startY2 = startY1;

        LinkedList stack = new LinkedList<Point>();
        stack.push(new Point(startX1, startY1));
        stack.push(new Point(startX2, startY2));
        recursive(stack);
        Double[] array = new Double[stack.size() * 2];
        for (int i = 0; i < array.length; i += 2) {
            Point p = (Point) stack.pop();
            array[i] = (double) p.X();
            array[i + 1] = (double) p.Y();
            if (dispColor) {
                display.getChildren().add(new Circle(
                        (double) p.X(),
                        (double) p.Y(),
                        length * 0.65,
                        Color.hsb((300 - i * 300 / (array.length - 2)) * 0.5 / (1 - colorValue),
                                1,
                                1)));
            }
        }

        if (dispLine) {
            Polyline curve = new Polyline();
            curve.getPoints().addAll(array);
            display.getChildren().add(curve);
        }
        if (dispStart) {
            display.getChildren().add(new Circle(startX1, startY1, 5, new Color(0, 0, 1, 1)));
        }
    }

    public void recursive(LinkedList oStack) {
        if (counter >= iterations) {
            return;
        }
        LinkedList cStack = (LinkedList) oStack.clone();
        Point end = (Point) cStack.pop();
        Point p1 = end;
        Point p2;
        while (!cStack.isEmpty()) {
            p2 = p1;
            p1 = (Point) cStack.pop();
            end = new Point(end.X() + p2.Y() - p1.Y(), end.Y() + p1.X() - p2.X());
            oStack.push(end);
        }
        counter++;
        recursive(oStack);
    }

    private class DefaultHandler implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent e) {
            display.getChildren().clear();
            iterations = defaultIterations;
            iterationField.setText(Double.toString(iterations));
            counter = 0;
            dispStart = false;
            dispColor = false;
            dispLine = true;
            sizeSlider.setValue(0.5);
            sizeValue = 0.5;
            rb1.setSelected(true);
            colorSlider.setValue(0.5);
            colorValue = 0.5;

            print();
        }
    }

    private class MinusHandler implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent e) {
            display.getChildren().clear();
            if (iterations > 0) {
                iterations--;
            }
            iterationField.setText(Double.toString(iterations));
            counter = 0;
            sizeValue = sizeSlider.getValue();
            colorValue = colorSlider.getValue();
            if (rb1.isSelected()) {
                dispLine = true;
                dispColor = false;
            } else if (rb2.isSelected()) {
                dispLine = false;
                dispColor = true;
            } else if (rb3.isSelected()) {
                dispLine = true;
                dispColor = true;
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
            counter = 0;
            sizeValue = sizeSlider.getValue();
            colorValue = colorSlider.getValue();
            if (rb1.isSelected()) {
                dispLine = true;
                dispColor = false;
            } else if (rb2.isSelected()) {
                dispLine = false;
                dispColor = true;
            } else if (rb3.isSelected()) {
                dispLine = true;
                dispColor = true;
            }
            print();
        }
    }

    private class DisplayHandler implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent e) {
            display.getChildren().clear();
            iterations = (int) Math.floor(Double.parseDouble(iterationField.getText()));
            counter = 0;
            sizeValue = sizeSlider.getValue();
            colorValue = colorSlider.getValue();
            if (rb1.isSelected()) {
                dispLine = true;
                dispColor = false;
            } else if (rb2.isSelected()) {
                dispLine = false;
                dispColor = true;
            } else if (rb3.isSelected()) {
                dispLine = true;
                dispColor = true;
            }
            print();
        }
    }

    class Point {

        private int x;
        private int y;

        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int X() {
            return x;
        }

        public int Y() {
            return y;
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
