package fractal.sierpinski.triangle;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Polyline;
import javafx.stage.Stage;

/**
 *
 * @author Emerich Koch
 */
public class FractalSierpinskiTriangle extends Application {

    private int displayWidth = 600;
    private int sceneHeight = 600;
    private int panelWidth = 200;

    private double x1 = 50;
    private double y1 = 500;
    private double x2 = 550;
    private double y2 = 500;
    private double x3 = 300;
    private double y3 = 100;

    private HBox root = new HBox();
    private Pane display = new Pane();
    private VBox panel = new VBox();

    TextField x1Disp = new TextField();
    TextField y1Disp = new TextField();
    TextField x2Disp = new TextField();
    TextField y2Disp = new TextField();
    TextField x3Disp = new TextField();
    TextField y3Disp = new TextField();

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

        HBox point1Box = new HBox();
        point1Box.getChildren().add(new Label("Point 1: ( "));
        x1Disp.setPrefColumnCount(3);
        x1Disp.setText(Double.toString(x1));
        point1Box.getChildren().add(x1Disp);
        point1Box.getChildren().add(new Label(", "));
        y1Disp.setPrefColumnCount(3);
        y1Disp.setText(Double.toString(sceneHeight - y1));
        point1Box.getChildren().add(y1Disp);
        point1Box.getChildren().add(new Label(")"));
        panel.getChildren().add(point1Box);

        HBox point2Box = new HBox();
        point2Box.getChildren().add(new Label("Point 2: ( "));
        x2Disp.setPrefColumnCount(3);
        x2Disp.setText(Double.toString(x2));
        point2Box.getChildren().add(x2Disp);
        point2Box.getChildren().add(new Label(", "));
        y2Disp.setPrefColumnCount(3);
        y2Disp.setText(Double.toString(sceneHeight - y2));
        point2Box.getChildren().add(y2Disp);
        point2Box.getChildren().add(new Label(")"));
        panel.getChildren().add(point2Box);

        HBox point3Box = new HBox();
        point3Box.getChildren().add(new Label("Point 3: ( "));
        x3Disp.setPrefColumnCount(3);
        x3Disp.setText(Double.toString(x3));
        point3Box.getChildren().add(x3Disp);
        point3Box.getChildren().add(new Label(", "));
        y3Disp.setPrefColumnCount(3);
        y3Disp.setText(Double.toString(sceneHeight - y3));
        point3Box.getChildren().add(y3Disp);
        point3Box.getChildren().add(new Label(")"));
        panel.getChildren().add(point3Box);

        Button btDisplay = new Button("Display");
        btDisplay.setOnAction(new DisplayHandler());
        panel.getChildren().add(btDisplay);

        Scene scene = new Scene(root, displayWidth + panelWidth, sceneHeight);
        primaryStage.setTitle("Sierpinski Triangle");
        primaryStage.setScene(scene);
        primaryStage.show();
        display.getChildren().add(new Polyline(x1, y1, x2, y2, x3, y3, x1, y1));
        recursive(x1, y1, x2, y2, x3, y3);
    }

    public void recursive(double x1, double y1, double x2, double y2, double x3, double y3) {
        if (Math.abs(x2 - x1) <= 1) {
            return;
        }
        double x12 = (x1 + x2) / 2;
        double y12 = (y1 + y2) / 2;
        double x13 = (x1 + x3) / 2;
        double y13 = (y1 + y3) / 2;
        double x23 = (x2 + x3) / 2;
        double y23 = (y2 + y3) / 2;
        Polyline tri = new Polyline(x12, y12, x13, y13, x23, y23, x12, y12);
        display.getChildren().add(tri);
        recursive(x1, y1, x12, y12, x13, y13);
        recursive(x12, y12, x2, y2, x23, y23);
        recursive(x13, y13, x23, y23, x3, y3);

    }

    private class DefaultHandler implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent e) {
            display.getChildren().clear();
            x1 = 50;
            x1Disp.setText(Double.toString(x1));
            y1 = 500;
            y1Disp.setText(Double.toString(sceneHeight - y1));
            x2 = 550;
            x2Disp.setText(Double.toString(x2));
            y2 = 500;
            y2Disp.setText(Double.toString(sceneHeight - y2));
            x3 = 300;
            x3Disp.setText(Double.toString(x3));
            y3 = 100;
            y3Disp.setText(Double.toString(sceneHeight - y3));
            display.getChildren().add(new Polyline(x1, y1, x2, y2, x3, y3, x1, y1));
            recursive(x1, y1, x2, y2, x3, y3);
        }
    }

    private class DisplayHandler implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent e) {
            display.getChildren().clear();
            x1 = Double.parseDouble(x1Disp.getText());
            y1 = sceneHeight - Double.parseDouble(y1Disp.getText());
            x2 = Double.parseDouble(x2Disp.getText());
            y2 = sceneHeight - Double.parseDouble(y2Disp.getText());
            x3 = Double.parseDouble(x3Disp.getText());
            y3 = sceneHeight - Double.parseDouble(y3Disp.getText());
            display.getChildren().add(new Polyline(x1, y1, x2, y2, x3, y3, x1, y1));
            recursive(x1, y1, x2, y2, x3, y3);
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
