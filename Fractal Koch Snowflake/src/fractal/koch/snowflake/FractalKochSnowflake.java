
package fractal.koch.snowflake;

import javafx.application.Application;
import static javafx.application.Application.launch;
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
import javafx.scene.shape.Polygon;
//import javafx.scene.shape.Polyline;
import javafx.stage.Stage;

/**
 *
 * @author Emerich Koch
 */
public class FractalKochSnowflake extends Application {

    private int displayWidth = 600;
    private int sceneHeight = 600;
    private int panelWidth = 200;

    private HBox root = new HBox();
    private Pane display = new Pane();
    private VBox panel = new VBox();
    Slider sizeSlider = new Slider(0, 1, 0.5);
    TextField iterationField = new TextField();

    private double length = 400;
    private final int iterationDefault = 5;
    private int iterations = iterationDefault;
    private double minDist = 1.1 * length / Math.pow(3, iterations);

    private double x1 = (displayWidth - length) / 2;
    private double y1 = (sceneHeight + length * Math.sqrt(3) / 3) / 2;
    private double x5 = (displayWidth + length) / 2;
    private double y5 = y1;

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
        iterationField.setText(Double.toString(iterationDefault));
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

        primaryStage.setTitle("Koch Snowflake");
        primaryStage.setScene(scene);
        primaryStage.show();

        double dist = Math.sqrt((x5 - x1) * (x5 - x1) + (y5 - y1) * (y5 - y1));
        double angle = Math.atan((y5 - y1) / (x5 - x1)) + Math.PI / 2;
        double x3 = (x1 + x5) / 2 + dist * Math.cos(angle) * Math.sqrt(3) / 2;
        double y3 = (y1 + y5) / 2 - dist * Math.sin(angle) * Math.sqrt(3) / 2;
        display.getChildren().add(new Polygon(x1, y1, x3, y3, x5, y5));
        recursive(x1, y1, x5, y5);
        recursive(x3, y3, x1, y1);
        recursive(x5, y5, x3, y3);
    }

    public void recursive(double x1, double y1, double x5, double y5) {
        double dist = Math.sqrt((x5 - x1) * (x5 - x1) + (y5 - y1) * (y5 - y1)) / 3;
        if (dist <= minDist) {
            return;
        }
        double angle = Math.atan((y5 - y1) / (x5 - x1));
        if (x5 < x1) {
            angle += Math.PI;
        }
        double x2 = x1 + dist * Math.cos(angle);
        double y2 = y1 + dist * Math.sin(angle);
        double x4 = x1 + 2 * dist * Math.cos(angle);
        double y4 = y1 + 2 * dist * Math.sin(angle);
        double x3 = (x2 + x4) / 2 + dist * Math.cos(Math.PI / 2 + angle) * Math.sqrt(3) / 2;
        double y3 = (y2 + y4) / 2 + dist * Math.sin(Math.PI / 2 + angle) * Math.sqrt(3) / 2;
        Polygon tri = new Polygon(x2, y2, x3, y3, x4, y4);
        display.getChildren().add(tri);
        recursive(x1, y1, x2, y2);
        recursive(x2, y2, x3, y3);
        recursive(x3, y3, x4, y4);
        recursive(x4, y4, x5, y5);

    }

    private class DefaultHandler implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent e) {
            display.getChildren().clear();
            sizeSlider.setValue(0.5);
            iterationField.setText(Double.toString(iterationDefault));
            iterations = iterationDefault;
            length = 400;
            minDist = 1.1 * length / Math.pow(3, iterations);

            x1 = (displayWidth - length) / 2;
            y1 = (sceneHeight + length * Math.sqrt(3) / 3) / 2;
            x5 = (displayWidth + length) / 2;
            y5 = y1;
            double dist = Math.sqrt((x5 - x1) * (x5 - x1) + (y5 - y1) * (y5 - y1));
            double angle = Math.atan((y5 - y1) / (x5 - x1)) + Math.PI / 2;
            double x3 = (x1 + x5) / 2 + dist * Math.cos(angle) * Math.sqrt(3) / 2;
            double y3 = (y1 + y5) / 2 - dist * Math.sin(angle) * Math.sqrt(3) / 2;
            display.getChildren().add(new Polygon(x1, y1, x3, y3, x5, y5));
            recursive(x1, y1, x5, y5);
            recursive(x3, y3, x1, y1);
            recursive(x5, y5, x3, y3);
        }
    }

    private class MinusHandler implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent e) {
            display.getChildren().clear();
            length = sizeSlider.getValue() * 800;
            if (iterations > 1) {
                iterations--;
            }
            iterationField.setText(Double.toString(iterations));
            minDist = 1.1 * length / Math.pow(3, iterations);
            x1 = (displayWidth - length) / 2;
            y1 = (sceneHeight + length * Math.sqrt(3) / 3) / 2;
            x5 = (displayWidth + length) / 2;
            y5 = y1;
            double dist = Math.sqrt((x5 - x1) * (x5 - x1) + (y5 - y1) * (y5 - y1));
            double angle = Math.atan((y5 - y1) / (x5 - x1)) + Math.PI / 2;
            double x3 = (x1 + x5) / 2 + dist * Math.cos(angle) * Math.sqrt(3) / 2;
            double y3 = (y1 + y5) / 2 - dist * Math.sin(angle) * Math.sqrt(3) / 2;
            display.getChildren().add(new Polygon(x1, y1, x3, y3, x5, y5));
            recursive(x1, y1, x5, y5);
            recursive(x3, y3, x1, y1);
            recursive(x5, y5, x3, y3);

        }
    }

    private class PlusHandler implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent e) {
            display.getChildren().clear();
            length = sizeSlider.getValue() * 800;

            iterations++;
            iterationField.setText(Double.toString(iterations));
            minDist = 1.1 * length / Math.pow(3, iterations);
            x1 = (displayWidth - length) / 2;
            y1 = (sceneHeight + length * Math.sqrt(3) / 3) / 2;
            x5 = (displayWidth + length) / 2;
            y5 = y1;
            double dist = Math.sqrt((x5 - x1) * (x5 - x1) + (y5 - y1) * (y5 - y1));
            double angle = Math.atan((y5 - y1) / (x5 - x1)) + Math.PI / 2;
            double x3 = (x1 + x5) / 2 + dist * Math.cos(angle) * Math.sqrt(3) / 2;
            double y3 = (y1 + y5) / 2 - dist * Math.sin(angle) * Math.sqrt(3) / 2;
            display.getChildren().add(new Polygon(x1, y1, x3, y3, x5, y5));
            recursive(x1, y1, x5, y5);
            recursive(x3, y3, x1, y1);
            recursive(x5, y5, x3, y3);
        }
    }

    private class DisplayHandler implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent e) {
            display.getChildren().clear();
            length = sizeSlider.getValue() * 800;
            iterations = (int) Math.floor(Double.parseDouble(iterationField.getText()));
            minDist = 1.1 * length / Math.pow(3, iterations);
            x1 = (displayWidth - length) / 2;
            y1 = (sceneHeight + length * Math.sqrt(3) / 3) / 2;
            x5 = (displayWidth + length) / 2;
            y5 = y1;
            double dist = Math.sqrt((x5 - x1) * (x5 - x1) + (y5 - y1) * (y5 - y1));
            double angle = Math.atan((y5 - y1) / (x5 - x1)) + Math.PI / 2;
            double x3 = (x1 + x5) / 2 + dist * Math.cos(angle) * Math.sqrt(3) / 2;
            double y3 = (y1 + y5) / 2 - dist * Math.sin(angle) * Math.sqrt(3) / 2;
            display.getChildren().add(new Polygon(x1, y1, x3, y3, x5, y5));
            recursive(x1, y1, x5, y5);
            recursive(x3, y3, x1, y1);
            recursive(x5, y5, x3, y3);
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
