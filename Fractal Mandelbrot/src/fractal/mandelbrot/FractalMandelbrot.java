
package fractal.mandelbrot;

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
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

/**
 *
 * @author Emerich Koch
 */
public class FractalMandelbrot extends Application {

    private int displayWidth = 800;
    private int panelWidth = 200;
    private int sceneHeight = displayWidth * 3 / 4;
    private double xMin = -2.5;
    private double xMax = 0.75;
    private double yMin = -1.22;
    private double yMax = yMin + (3.0 / 4) * (xMax - xMin);
    private double resolution = 1;
    private double partition = (xMax - xMin) / (resolution * displayWidth);
    private double radius = 1.5 / resolution;
    private double bound = 20;
    private double edgeResolution = 0.03;

    private HBox root = new HBox();
    private Pane display = new Pane();
    private VBox panel = new VBox();

    Slider resolutionSlider = new Slider(0, 100, 100);
    TextField xMinDisp = new TextField();
    TextField xMaxDisp = new TextField();
    TextField yMinDisp = new TextField();
    Label point2Label = new Label();

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
        xMinDisp.setPrefColumnCount(3);
        xMinDisp.setText(Double.toString(xMin));
        point1Box.getChildren().add(xMinDisp);
        point1Box.getChildren().add(new Label(", "));
        yMinDisp.setPrefColumnCount(3);
        yMinDisp.setText(Double.toString(-yMin));
        point1Box.getChildren().add(yMinDisp);
        point1Box.getChildren().add(new Label(")"));
        panel.getChildren().add(point1Box);

        HBox point2Box = new HBox();
        point2Box.getChildren().add(new Label("Point 2: ( "));
        xMaxDisp.setPrefColumnCount(3);
        xMaxDisp.setText(Double.toString(xMax));
        point2Box.getChildren().add(xMaxDisp);
        point2Label.setText(", " + -yMax + ")");
        point2Box.getChildren().add(point2Label);
        panel.getChildren().add(point2Box);

        resolutionSlider.setShowTickMarks(true);
        resolutionSlider.setShowTickLabels(true);
        resolutionSlider.setMajorTickUnit(25f);
        resolutionSlider.setBlockIncrement(10f);
        panel.getChildren().add(new Label("Resolution:"));
        panel.getChildren().add(resolutionSlider);

        Button btDisplay = new Button("Display");
        btDisplay.setOnAction(new DisplayHandler());
        panel.getChildren().add(btDisplay);

        Scene scene = new Scene(root, displayWidth + panelWidth, sceneHeight);

        primaryStage.setTitle("Mandelbrot Set");
        primaryStage.setScene(scene);
        primaryStage.show();

        printSet();

    }

    public void printSet() {
        for (double a = xMin; a <= xMax; a += partition) {
            for (double b = yMin; b <= yMax; b += partition) {
                ComplexNumber c = new ComplexNumber(a, b);
                ComplexNumber z = new ComplexNumber(0, 0);
                double zMag = 0;
                double i;
                for (i = 2; i < 20 && zMag < bound; i += edgeResolution) {
                    z.set(ComplexNumber.add(ComplexNumber.multiply(z, z), c));
                    zMag = Math.sqrt(z.getRe() * z.getRe() + z.getIm() * z.getIm());
                }
                if (zMag <= bound) {
                    display.getChildren().add(new Circle((a - xMin) * displayWidth / (xMax - xMin),
                            (b - yMin) * displayWidth / (xMax - xMin),
                            radius));
                } else {
                    //System.out.println(i);
                    display.getChildren().add(new Circle((a - xMin) * displayWidth / (xMax - xMin),
                            (b - yMin) * displayWidth / (xMax - xMin),
                            //radius, Color.hsb(i * 100, 1, 1 - 0.75 / i)));
                            radius, new Color(1 - Math.sqrt(2) / Math.sqrt(i), 1 - Math.log(2) / Math.log(i), 1 - 1.0 / i, 0.75)));
                }
            }
        }
    }

    private class DefaultHandler implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent e) {
            display.getChildren().clear();
            xMin = -2.5;
            xMinDisp.setText(Double.toString(xMin));
            xMax = 0.75;
            xMaxDisp.setText(Double.toString(xMax));
            yMin = -1.22;
            yMinDisp.setText(Double.toString(-yMin));
            yMax = yMin + (3.0 / 4) * (xMax - xMin);
            resolution = 1;
            partition = (xMax - xMin) / (resolution * displayWidth);
            radius = 1.5 / resolution;
            edgeResolution = 0.03;
            resolutionSlider.setValue(100);

            printSet();
        }
    }

    private class DisplayHandler implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent e) {
            display.getChildren().clear();
            xMin = Double.parseDouble(xMinDisp.getText());
            xMax = Double.parseDouble(xMaxDisp.getText());
            yMin = -Double.parseDouble(yMinDisp.getText());
            yMax = yMin + (3.0 / 4) * (xMax - xMin);
            point2Label.setText(", " + -yMax + ")");
            resolution = resolutionSlider.getValue() / 100;
            partition = (xMax - xMin) / (resolution * displayWidth);
            radius = 1.5 / resolution;
            edgeResolution = 0.03;

            printSet();
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
