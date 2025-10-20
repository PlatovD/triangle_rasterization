package com.cgvsu.rasterizationfxapp;

import com.cgvsu.rasterization.Rasterization;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;

import java.util.Random;

public class RasterizationController {

    @FXML
    AnchorPane anchorPane;
    @FXML
    private Canvas canvas;

    @FXML
    private void initialize() {
        anchorPane.prefWidthProperty().addListener((ov, oldValue, newValue) -> canvas.setWidth(newValue.doubleValue()));
        anchorPane.prefHeightProperty().addListener((ov, oldValue, newValue) -> canvas.setHeight(newValue.doubleValue()));

        Random random = new Random();
        Color[] colors = {Color.RED, Color.GREEN, Color.BLUE};
        for (int i = 0; i < 1; i++)
            Rasterization.drawInterpolatedTriangleByIterator(
                    canvas.getGraphicsContext2D().getPixelWriter(),
                    random.nextInt(700), random.nextInt(600),
                    random.nextInt(700), random.nextInt(600),
                    random.nextInt(700), random.nextInt(600),
                    colors[0], colors[1], colors[2]
            );
    }

}