package com.cgvsu.rasterizationfxapp;

import com.cgvsu.rasterization.Rasterization;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.util.Random;

public class RasterizationController {

    @FXML
    AnchorPane anchorPane;
    @FXML
    private Canvas canvas;

    @FXML
    private void initialize() throws IOException {
        anchorPane.prefWidthProperty().addListener((ov, oldValue, newValue) -> canvas.setWidth(newValue.doubleValue()));
        anchorPane.prefHeightProperty().addListener((ov, oldValue, newValue) -> canvas.setHeight(newValue.doubleValue()));

        Random random = new Random();
        Color[] colors = {Color.RED, Color.BLACK, Color.BLUE};
        for (int i = 0; i < 10; i++)
            Rasterization.drawTriangleByIterator(
                    canvas.getGraphicsContext2D(),
                    random.nextInt(700), random.nextInt(600),
                    random.nextInt(700), random.nextInt(600),
                    random.nextInt(700), random.nextInt(600),
                    colors[i % 3]
            );
    }

}