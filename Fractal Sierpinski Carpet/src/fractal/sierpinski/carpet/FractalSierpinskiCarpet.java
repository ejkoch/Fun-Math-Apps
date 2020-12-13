package fractal.sierpinski.carpet;

import javafx.application.Application;
import static javafx.application.Application.launch;
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
public class FractalSierpinskiCarpet extends Application {

    private int displayWidth = 600;
    private int sceneHeight = 600;
    private int panelWidth = 200;

    private double x1 = 50;
    private double y1 = 50;
    private double x4 = 550;
    private double y4 = 550;

    private HBox root = new HBox();
    private Pane display = new Pane();
    private VBox panel = new VBox();

    TextField x1Disp = new TextField();
    TextField y1Disp = new TextField();
    TextField x4Disp = new TextField();
    TextField y4Disp = new TextField();

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

        HBox point4Box = new HBox();
        point4Box.getChildren().add(new Label("Point 2: ( "));
        x4Disp.setPrefColumnCount(3);
        x4Disp.setText(Double.toString(x4));
        point4Box.getChildren().add(x4Disp);
        point4Box.getChildren().add(new Label(", "));
        y4Disp.setPrefColumnCount(3);
        y4Disp.setText(Double.toString(sceneHeight - y4));
        point4Box.getChildren().add(y4Disp);
        point4Box.getChildren().add(new Label(")"));
        panel.getChildren().add(point4Box);

        Button btDisplay = new Button("Display");
        btDisplay.setOnAction(new DisplayHandler());
        panel.getChildren().add(btDisplay);

        Scene scene = new Scene(root, displayWidth + panelWidth, sceneHeight);
        primaryStage.setTitle("Sierpinski Carpet");
        primaryStage.setScene(scene);
        primaryStage.show();
        display.getChildren().add(new Polyline(x1, y1, x4, y1, x4, y4, x1, y4, x1, y1));
        recursive(x1, y1, x4, y4);
    }

    public void recursive(double x1, double y1, double x4, double y4) {
        if (Math.abs(x4 - x1) * Math.abs(y4 - y1) <= 2) {
            return;
        }
        double x2 = (2.0 / 3) * Math.min(x1, x4) + (1.0 / 3) * Math.max(x1, x4);
        double y2 = (2.0 / 3) * Math.min(y1, y4) + (1.0 / 3) * Math.max(y1, y4);
        double x3 = (1.0 / 3) * Math.min(x1, x4) + (2.0 / 3) * Math.max(x1, x4);
        double y3 = (1.0 / 3) * Math.min(y1, y4) + (2.0 / 3) * Math.max(y1, y4);
        Polyline rec = new Polyline(x2, y2, x3, y2, x3, y3, x2, y3, x2, y2);
        display.getChildren().add(rec);
        recursive(x1, y1, x2, y2);
        recursive(x2, y1, x3, y2);
        recursive(x3, y1, x4, y2);
        recursive(x1, y2, x2, y3);
        recursive(x3, y2, x4, y3);
        recursive(x1, y3, x2, y4);
        recursive(x2, y3, x3, y4);
        recursive(x3, y3, x4, y4);
    }

    private class DefaultHandler implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent e) {
            display.getChildren().clear();
            x1 = 50;
            x1Disp.setText(Double.toString(x1));
            y1 = 50;
            y1Disp.setText(Double.toString(sceneHeight - y1));
            x4 = 550;
            x4Disp.setText(Double.toString(x4));
            y4 = 550;
            y4Disp.setText(Double.toString(sceneHeight - y4));

            display.getChildren().add(new Polyline(x1, y1, x4, y1, x4, y4, x1, y4, x1, y1));
            recursive(x1, y1, x4, y4);
        }
    }

    private class DisplayHandler implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent e) {
            display.getChildren().clear();
            x1 = Double.parseDouble(x1Disp.getText());
            y1 = sceneHeight - Double.parseDouble(y1Disp.getText());
            x4 = Double.parseDouble(x4Disp.getText());
            y4 = sceneHeight - Double.parseDouble(y4Disp.getText());

            display.getChildren().add(new Polyline(x1, y1, x4, y1, x4, y4, x1, y4, x1, y1));
            recursive(x1, y1, x4, y4);
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
