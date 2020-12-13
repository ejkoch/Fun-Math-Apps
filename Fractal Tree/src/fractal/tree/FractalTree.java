package fractal.tree;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

/**
 *
 * @author Emerich Koch
 */
public class FractalTree extends Application {

    private int displayWidth = 800;
    private int sceneHeight = 600;
    private int panelWidth = 200;

    private double startX = 400;
    private double startY = 550;
    private double angle = 90;
    private double length = 150;
    private double leftRatio = 0.65;
    private double rightRatio = 0.65;
    private double leftTilt = 30;
    private double rightTilt = 30;

    private HBox root = new HBox();
    private Pane display = new Pane();
    private VBox panel = new VBox();
    Slider lengthSlider = new Slider(0, 1, 0.5);
    Slider lTiltSlider = new Slider(0, 90, leftTilt);
    Slider lRatioSlider = new Slider(0, 1, leftRatio);
    Slider rTiltSlider = new Slider(0, 90, rightTilt);
    Slider rRatioSlider = new Slider(0, 1, rightRatio);

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

        lengthSlider.setShowTickMarks(true);
        lengthSlider.setShowTickLabels(true);
        lengthSlider.setMajorTickUnit(0.25f);
        lengthSlider.setBlockIncrement(0.1f);
        panel.getChildren().add(new Label("Base Length"));
        panel.getChildren().add(lengthSlider);

        lTiltSlider.setShowTickMarks(true);
        lTiltSlider.setShowTickLabels(true);
        lTiltSlider.setMajorTickUnit(15f);
        lTiltSlider.setBlockIncrement(5f);
        panel.getChildren().add(new Label("Left Branch Angle"));
        panel.getChildren().add(lTiltSlider);

        lRatioSlider.setShowTickMarks(true);
        lRatioSlider.setShowTickLabels(true);
        lRatioSlider.setMajorTickUnit(0.25f);
        lRatioSlider.setBlockIncrement(0.1f);
        panel.getChildren().add(new Label("Left Branch Ratio"));
        panel.getChildren().add(lRatioSlider);

        rTiltSlider.setShowTickMarks(true);
        rTiltSlider.setShowTickLabels(true);
        rTiltSlider.setMajorTickUnit(15f);
        rTiltSlider.setBlockIncrement(5f);
        panel.getChildren().add(new Label("Right Branch Angle"));
        panel.getChildren().add(rTiltSlider);

        rRatioSlider.setShowTickMarks(true);
        rRatioSlider.setShowTickLabels(true);
        rRatioSlider.setMajorTickUnit(0.25f);
        rRatioSlider.setBlockIncrement(0.1f);
        panel.getChildren().add(new Label("Right Branch Ratio"));
        panel.getChildren().add(rRatioSlider);

        Button btDisplay = new Button("Display");
        btDisplay.setOnAction(new DisplayHandler());
        panel.getChildren().add(btDisplay);

        drawTree(startX, startY, angle, length);

        Scene scene = new Scene(root, displayWidth + panelWidth, sceneHeight);

        primaryStage.setTitle("Fractal Tree");
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    public void drawTree(double startX, double startY, double angle, double length) {
        if (length <= 1) {
            return;
        }
        double endX = startX + Math.cos(angle * Math.PI / 180) * length;
        double endY = startY - Math.sin(angle * Math.PI / 180) * length;
        Line line = new Line(startX, startY, endX, endY);
        display.getChildren().add(line);
        drawTree(endX, endY, angle + leftTilt, length * leftRatio);
        drawTree(endX, endY, angle - rightTilt, length * rightRatio);

    }

    private class DisplayHandler implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent e) {
            display.getChildren().clear();
            length = lengthSlider.getValue() * 300;
            leftTilt = lTiltSlider.getValue();
            leftRatio = lRatioSlider.getValue() > 0.95 ? 0.95 : lRatioSlider.getValue();
            rightTilt = rTiltSlider.getValue();
            rightRatio = rRatioSlider.getValue() > 0.95 ? 0.95 : rRatioSlider.getValue();
            drawTree(startX, startY, angle, length);
        }
    }

    private class DefaultHandler implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent e) {
            display.getChildren().clear();
            length = 150;
            lengthSlider.setValue(0.5);
            leftTilt = 30;
            lTiltSlider.setValue(30);
            leftRatio = 0.65;
            lRatioSlider.setValue(0.65);
            rightTilt = 30;
            rTiltSlider.setValue(30);
            rightRatio = 0.65;
            rRatioSlider.setValue(0.65);
            drawTree(startX, startY, angle, length);
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
