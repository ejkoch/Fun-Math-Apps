package goldbach.conjecture.graph;

import java.util.Arrays;
import java.util.Stack;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

/**
 *
 * @author Emerich Koch
 */
public class GoldbachConjectureGraph extends Application {

    private int displayWidth = 600;
    private int sceneHeight = 600;
    private int panelWidth = 200;
    private int leftMargin = 50;
    private int bottomMargin = 50;
    private int topMargin = 50;
    private int rightMargin = 50;
    private int tickLength = 20;
    private int graphWidth = displayWidth - (leftMargin + rightMargin);
    private int graphHeight = sceneHeight - (topMargin + bottomMargin);

    private HBox root = new HBox();
    private Pane display = new Pane();
    private VBox panel = new VBox();

    TextField maxField = new TextField();
    TextField selectionField = new TextField();
    Label selectionCountLabel = new Label("");

    private final int defaultMax = 100;
    private int max = defaultMax;
    private int maxLeadDigit;
    private int maxNumDigits;
    private int countMax;
    private int countMaxLeadDigit;
    private int countMaxNumDigits;
    private final int defaultSelection = 4;
    private int selection = defaultSelection;
    private int[] primeArray;
    private int[] countArray;

    private Circle selectionDot = new Circle();

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

        HBox maxBox = new HBox();
        maxBox.getChildren().add(new Label("Max Value: "));

        maxField.setPrefColumnCount(4);
        maxField.setText(Double.toString(max));
        maxBox.getChildren().add(maxField);

        Button btDisplay = new Button("Display");
        btDisplay.setOnAction(new DisplayHandler());
        maxBox.getChildren().add(btDisplay);

        panel.getChildren().add(maxBox);

        panel.getChildren().add(new Label("Select an even number:"));

        HBox selectionBox = new HBox();

        Button btMinus = new Button("-");
        btMinus.setOnAction(new MinusHandler());
        selectionBox.getChildren().add(btMinus);

        selectionField.setPrefColumnCount(3);
        selectionField.setText(Double.toString(4));
        selectionBox.getChildren().add(selectionField);

        display.getChildren().add(selectionDot);

        selectionField.textProperty().addListener((observable, oldValue, newValue) -> {
            selection = ((int) Double.parseDouble(newValue) / 2) * 2;
            selection();
        });

        primeArrayGenerator();
        countArrayGenerator();
        findMaxDigits();
        findCountMaxDigits();
        print();
        selection();

        Button btPlus = new Button("+");
        btPlus.setOnAction(new PlusHandler());
        selectionBox.getChildren().add(btPlus);

        panel.getChildren().add(selectionBox);

        HBox selectionButtonBox = new HBox();

        Button btSelect = new Button("Select Point");
        btSelect.setOnAction(new SelectPointHandler());
        selectionButtonBox.getChildren().add(btSelect);

        Button btClear = new Button("Clear Point");
        btClear.setOnAction(new ClearPointHandler());
        selectionButtonBox.getChildren().add(btClear);

        panel.getChildren().add(selectionButtonBox);

        Label selectionLabel = new Label("Number of representations of __:");
        panel.getChildren().add(selectionLabel);
        panel.getChildren().add(selectionCountLabel);

        Scene scene = new Scene(root, displayWidth + panelWidth, sceneHeight);

        primaryStage.setTitle("Goldbach Conjecture");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void primeArrayGenerator() {
        Stack primeStack = new Stack();
        primeStack.push(2);
        primeStack.push(3);
        primeStack.push(5);
        boolean isPrime;
        for (int i = 7; i <= max; i += 2) {
            if (i % 10 != 5) {
                isPrime = true;
                for (Object value : primeStack) {
                    if (i % (int) value == 0) {
                        isPrime = false;
                        break;
                    }
                }
                if (isPrime) {
                    primeStack.push(i);
                }
            }
        }
        primeArray = new int[primeStack.size()];
        int j = 0;
        for (Object value : primeStack) {
            primeArray[j] = (int) value;
            j++;
        }
    }

    public void countArrayGenerator() {
        countMax = 0;
        countArray = new int[(max - 2) / 2];
        for (int i = 0; i < countArray.length; i++) {
            int count = 0;
            int value = (i + 2) * 2;
            for (int j = 0; primeArray[j] <= value / 2; j++) {
                if (Arrays.binarySearch(primeArray, value - primeArray[j]) >= 0) {
                    count++;
                }
            }
            countArray[i] = count;
            if (count > countMax) {
                countMax = count;
            }
        }
    }

    public void findMaxDigits() {
        int exp = 0;
        int num = max;
        while (num != 0) {
            exp++;
            maxLeadDigit = num;
            num -= (num % Math.pow(10, exp));
        }
        exp--;
        maxLeadDigit /= Math.pow(10, exp);
        maxNumDigits = exp;
    }

    public void findCountMaxDigits() {
        int exp = 0;
        int num = countMax;
        while (num != 0) {
            exp++;
            countMaxLeadDigit = num;
            num -= (num % Math.pow(10, exp));
        }
        exp--;
        countMaxLeadDigit /= Math.pow(10, exp);
        countMaxNumDigits = exp;
    }

    public double toHPixel(int value) {
        return leftMargin + value * graphWidth / (double) max;
    }

    public double toVPixel(int value) {
        return bottomMargin + value * graphWidth / (double) countMax;
    }

    public void selection() {
        double radius = graphWidth / Math.pow(Math.log(max), 3);
        if (radius < 2) {
            radius = 2;
        }
        int index = (selection / 2) - 2;
        selectionDot.setCenterX(toHPixel(selection));
        selectionDot.setCenterY(sceneHeight - toVPixel(countArray[index]));
        selectionDot.setRadius(radius);
        selectionDot.setFill(Color.RED);

        selectionCountLabel.setText(Double.toString(countArray[index]));

    }

    public void print() {

        display.getChildren().add(new Line(leftMargin,
                bottomMargin,
                leftMargin,
                sceneHeight - bottomMargin));
        display.getChildren().add(new Line(leftMargin,
                sceneHeight - bottomMargin,
                displayWidth - leftMargin,
                sceneHeight - bottomMargin));
        display.getChildren().add(new Line(toHPixel(max),
                sceneHeight - bottomMargin + tickLength / 2,
                toHPixel(max),
                sceneHeight - bottomMargin - tickLength / 2));
        Label labelXMax = new Label(Integer.toString(max));
        labelXMax.setLayoutX(toHPixel(max));
        labelXMax.setLayoutY(sceneHeight - bottomMargin + tickLength / 2);
        display.getChildren().add(labelXMax);

        int hTicks = 0;
        switch (maxLeadDigit) {
            case 1:
                maxNumDigits--;
                while (hTicks <= 0.9 * max) {
                    display.getChildren().add(new Line(toHPixel(hTicks),
                            sceneHeight - bottomMargin + tickLength / 2,
                            toHPixel(hTicks),
                            sceneHeight - bottomMargin - tickLength / 2));
                    Label labelX = new Label(Integer.toString(hTicks));
                    labelX.setLayoutX(toHPixel(hTicks));
                    labelX.setLayoutY(sceneHeight - bottomMargin + tickLength / 2);
                    display.getChildren().add(labelX);
                    hTicks += 2 * Math.pow(10, maxNumDigits);
                }
                break;
            case 2:
                maxNumDigits--;
                while (hTicks <= 0.9 * max) {
                    display.getChildren().add(new Line(toHPixel(hTicks),
                            sceneHeight - bottomMargin + tickLength / 2,
                            toHPixel(hTicks),
                            sceneHeight - bottomMargin - tickLength / 2));
                    Label labelX = new Label(Integer.toString(hTicks));
                    labelX.setLayoutX(toHPixel(hTicks));
                    labelX.setLayoutY(sceneHeight - bottomMargin + tickLength / 2);
                    display.getChildren().add(labelX);
                    hTicks += 4 * Math.pow(10, maxNumDigits);
                }
                break;
            default:
                while (hTicks < 0.9 * max) {
                    display.getChildren().add(new Line(toHPixel(hTicks),
                            sceneHeight - bottomMargin + tickLength / 2,
                            toHPixel(hTicks),
                            sceneHeight - bottomMargin - tickLength / 2));
                    Label labelX = new Label(Integer.toString(hTicks));
                    labelX.setLayoutX(toHPixel(hTicks));
                    labelX.setLayoutY(sceneHeight - bottomMargin + tickLength / 2);
                    display.getChildren().add(labelX);
                    hTicks += Math.pow(10, maxNumDigits);
                }
        }

        display.getChildren().add(new Line(leftMargin - tickLength / 2,
                sceneHeight - toVPixel(countMax),
                leftMargin + tickLength / 2,
                sceneHeight - toVPixel(countMax)));
        Label labelYMax = new Label(Integer.toString(countMax));
        labelYMax.setLayoutX(leftMargin - tickLength);
        labelYMax.setLayoutY(sceneHeight - toVPixel(countMax));
        display.getChildren().add(labelYMax);

        int vTicks = 0;
        switch (countMaxLeadDigit) {
            case 1:
                countMaxNumDigits--;
                while (vTicks <= 0.9 * countMax) {
                    display.getChildren().add(new Line(leftMargin - tickLength / 2,
                            sceneHeight - toVPixel(vTicks),
                            leftMargin + tickLength / 2,
                            sceneHeight - toVPixel(vTicks)));
                    Label labelY = new Label(Integer.toString(vTicks));
                    labelY.setLayoutX(leftMargin - tickLength);
                    labelY.setLayoutY(sceneHeight - toVPixel(vTicks));
                    display.getChildren().add(labelY);
                    vTicks += 2 * Math.pow(10, countMaxNumDigits);
                }
                break;
            case 2:
                countMaxNumDigits--;
                while (vTicks <= 0.9 * countMax) {
                    display.getChildren().add(new Line(leftMargin - tickLength / 2,
                            sceneHeight - toVPixel(vTicks),
                            leftMargin + tickLength / 2,
                            sceneHeight - toVPixel(vTicks)));
                    Label labelY = new Label(Integer.toString(vTicks));
                    labelY.setLayoutX(leftMargin - tickLength);
                    labelY.setLayoutY(sceneHeight - toVPixel(vTicks));
                    display.getChildren().add(labelY);
                    vTicks += 4 * Math.pow(10, countMaxNumDigits);
                }
                break;
            default:
                while (vTicks <= 0.9 * countMax) {
                    display.getChildren().add(new Line(leftMargin - tickLength / 2,
                            sceneHeight - toVPixel(vTicks),
                            leftMargin + tickLength / 2,
                            sceneHeight - toVPixel(vTicks)));
                    Label labelY = new Label(Integer.toString(vTicks));
                    labelY.setLayoutX(leftMargin - tickLength);
                    labelY.setLayoutY(sceneHeight - toVPixel(vTicks));
                    display.getChildren().add(labelY);
                    vTicks += Math.pow(10, countMaxNumDigits);
                }
        }

        /*for (int i = 0; i <= 5; i++) {
            display.getChildren().add(new Line(leftMargin - tickLength / 2,
                                                bottomMargin + i * graphHeight / 5,
                                                leftMargin + tickLength / 2,
                                                bottomMargin + i * graphHeight / 5));
        }*/
        double radius = 0.5 * graphWidth / Math.pow(Math.log(max), 3);
        if (radius < 0.5) {
            radius = 0.5;
        }
        for (int i = 0; i < countArray.length; i++) {
            /*switch (((i + 2) * 2) % 4) {
                case 0:
                    display.getChildren().add(new Circle(toHPixel((i + 2) * 2), sceneHeight - toVPixel(countArray[i]), radius, Color.BLUE));
                    break;
                case 2:
                    display.getChildren().add(new Circle(toHPixel((i + 2) * 2), sceneHeight - toVPixel(countArray[i]), radius, Color.RED));
                    break;
            }*/
            Circle circ = new Circle(toHPixel((i + 2) * 2),
                    sceneHeight - toVPixel(countArray[i]),
                    radius,
                    Color.BLUE);
            //circ.getOnMouseClicked(new EventHandler<ActionEvent> )
            display.getChildren().add(circ);
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
            max = defaultMax;
            selection = defaultSelection;
            maxField.setText(Double.toString(max));
            primeArrayGenerator();
            countArrayGenerator();
            findMaxDigits();
            findCountMaxDigits();
            print();
            selection();
        }

    }

    private class DisplayHandler implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent e) {
            display.getChildren().clear();
            max = (int) Math.floor(Double.parseDouble(maxField.getText()));
            primeArrayGenerator();
            countArrayGenerator();
            findMaxDigits();
            findCountMaxDigits();
            print();
            selection();
        }

    }

    private class MinusHandler implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent e) {
            selection -= 2;
            selectionField.setText(Double.toString(selection));
            selection();
        }
    }

    private class PlusHandler implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent e) {
            selection += 2;
            selectionField.setText(Double.toString(selection));
            selection();
        }
    }

    private class SelectPointHandler implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent e) {

        }
    }

    private class ClearPointHandler implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent e) {

        }
    }

}
