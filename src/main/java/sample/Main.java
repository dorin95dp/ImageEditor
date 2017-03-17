package sample;

import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;

public class Main extends Application {

    VBox vBox;
    VBox settings;
    Button makeGray;
    Button rotate;
    Button openImage;
    Button invertColorButton;
    Image greyImage;
    Image simpleImage;
    Slider saturationSlider;
    double rotateDegrees = 0;
    StackPane pane;
    Canvas canvas;
    ColorPicker colorPicker;
    GraphicsContext gc;
    Button saveButton;



    @Override
    public void start(Stage primaryStage) throws Exception{
        primaryStage.setTitle("Image editor Dorin Popa FAF-141");
        //Parent layout
        BorderPane mainLayout = new BorderPane();

        //Settings Layout
        vBox = new VBox();
        vBox.setId("border");
        vBox.setSpacing(10);
        openImage = new Button("Open");
        openImage.setOnAction(loadImage);
        vBox.getChildren().addAll(openImage);


        settings = new VBox();
        settings.setPrefWidth(250);
        makeGray = new Button("Make grey");
        rotate = new Button("Rotate right");
        invertColorButton = new Button("Contrast invert");
        saveButton = new Button("Save");
        colorPicker = new ColorPicker();
        saturationSlider = new Slider(0, 1, 1);

        settings.getChildren().addAll(makeGray, saturationSlider);
        settings.setVisible(Boolean.FALSE);
        rotate.setVisible(Boolean.FALSE);
        saturationSlider.setVisible(Boolean.FALSE);
        invertColorButton.setVisible(Boolean.FALSE);
        colorPicker.setVisible(Boolean.FALSE);
        saveButton.setVisible(Boolean.FALSE);
        rotate.setId("rotate");

        vBox.getChildren().addAll(settings, invertColorButton, rotate, saturationSlider, colorPicker, saveButton);
        mainLayout.setLeft(vBox);



        //Image layout
        pane = new StackPane();
        canvas = new Canvas(800, 600);
        gc = canvas.getGraphicsContext2D();
        pane.getChildren().addAll(canvas);
        mainLayout.setCenter(pane);
        addListeners(primaryStage);

        Scene scene = new Scene(mainLayout, 1100, 600);
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    private void addListeners(Stage primaryStage) {

        saturationSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            canvas.setOpacity(newValue.doubleValue());

        });

        makeGray.setOnAction((actionEvent) -> {
            if(makeGray.getText().equals("Make grey")) {
                gc.drawImage(greyImage, 0, 0);
                makeGray.setText("Make colorful");
            } else {
                gc.drawImage(simpleImage, 0, 0);
                makeGray.setText("Make grey");
            }
        });

        invertColorButton.setOnAction((event) ->{
            gc.drawImage(Controller.invertImage(simpleImage), 0,0);
        });

        rotate.setOnAction((actionEvent) -> {
            rotateDegrees += 90;
            if(rotateDegrees == 360) rotateDegrees = 0;
            canvas.setRotate(rotateDegrees);
        });

        canvas.addEventHandler(MouseEvent.MOUSE_DRAGGED,
                e -> gc.fillRect(e.getX() - 2, e.getY() - 2, 10, 10));

        colorPicker.setOnAction((event) -> gc.setFill(colorPicker.getValue()));

        saveButton.setOnAction(t -> {
            FileChooser fileChooser = new FileChooser();

            //Set extension filter
            FileChooser.ExtensionFilter extFilter =
                    new FileChooser.ExtensionFilter("png files (*.png)", "*.png");
            fileChooser.getExtensionFilters().add(extFilter);

            //Show save file dialog
            File file = fileChooser.showSaveDialog(primaryStage);

            if(file != null){
                try {
                    WritableImage writableImage = new WritableImage(800, 600);
                    canvas.snapshot(null, writableImage);
                    RenderedImage renderedImage = SwingFXUtils.fromFXImage(writableImage, null);
                    ImageIO.write(renderedImage, "png", file);
                } catch (IOException ex) {
                }
            }
        });

    }

    private EventHandler<ActionEvent> loadImage = new EventHandler<ActionEvent>() {

        @Override
        public void handle(ActionEvent event) {
            FileChooser fileChooser = new FileChooser();

            File file = fileChooser.showOpenDialog(null);

            try {
                BufferedImage bufferedImage = ImageIO.read(file);
                simpleImage = SwingFXUtils.toFXImage(bufferedImage, null);
                gc.drawImage(simpleImage, 0, 0);
                greyImage = Controller.makePicturegrey(255, simpleImage);
                settings.setVisible(Boolean.TRUE);
                rotate.setVisible(Boolean.TRUE);
                saturationSlider.setVisible(Boolean.TRUE);
                colorPicker.setVisible(Boolean.TRUE);
                invertColorButton.setVisible(Boolean.TRUE);
                saveButton.setVisible(Boolean.TRUE);

            } catch (IOException ex) {
            }
        }
    };

    public static void main(String[] args) {
        launch(args);
    }

}
