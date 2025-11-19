package com.cgvsu.rasterizationfxapp;

import com.cgvsu.rasterization.Rasterization;
import com.cgvsu.util.Point;
import com.cgvsu.util.Triangle;
import com.cgvsu.util.TriangleColorProvider;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.cgvsu.util.Constants.EPS;

public class RasterizationController {
    private List<Triangle> triangles;
    private Triangle activeTriangle = null;
    private int activePointIndex = -1;


    @FXML
    AnchorPane anchorPane;
    @FXML
    private Canvas canvas;

    @FXML
    private void initialize() {
        anchorPane.prefWidthProperty().addListener((ov, oldValue, newValue) -> canvas.setWidth(newValue.doubleValue()));
        anchorPane.prefHeightProperty().addListener((ov, oldValue, newValue) -> canvas.setHeight(newValue.doubleValue()));
        triangles = new ArrayList<>();
        Random random = new Random();

        Point point1 = new Point(random.nextInt(700), random.nextInt(600));
        Point point2 = new Point(random.nextInt(700), random.nextInt(600));
        Point point3 = new Point(random.nextInt(700), random.nextInt(600));
        Triangle triangle = new Triangle(
                point1,
                point2,
                point3,
                new TriangleColorProvider(Color.RED, Color.GREEN, Color.BLUE)
        );
        triangles.add(triangle);
        triangle.draw(canvas.getGraphicsContext2D());
    }

    @FXML
    private void handleMouseDown(MouseEvent mouseEvent) {
        if (activeTriangle != null) {
            activeTriangle = null;
            activePointIndex = -1;
            return;
        }
        for (int i = 0; i < triangles.size(); i++) {
            for (int j = 0; j < triangles.get(i).pointCnt(); j++) {
                if (
                        Math.abs(triangles.get(i).getPointByIndex(j).getX() - mouseEvent.getX()) < EPS
                                && Math.abs(triangles.get(i).getPointByIndex(j).getY() - mouseEvent.getY()) < EPS
                ) {
                    activeTriangle = triangles.get(i);
                    activePointIndex = j;
                    System.out.println(activePointIndex);
                    return;
                }
            }

        }
    }

    @FXML
    private void handleMouseMove(MouseEvent mouseEvent) {
//        canvas.getGraphicsContext2D().clearRect(0, 0, 1000, 1000);
//        Rasterization.drawLineBresenham(canvas.getGraphicsContext2D().getPixelWriter(), 200, 200, (int) mouseEvent.getX(), (int) mouseEvent.getY());
        if (activeTriangle == null) return;
        canvas.getGraphicsContext2D().clearRect(0, 0, 1000, 1000);
        activeTriangle.setPointByIndex(activePointIndex, new Point((int) mouseEvent.getX(), (int) mouseEvent.getY()));
        for (Triangle triangle : triangles) {
            triangle.draw(canvas.getGraphicsContext2D());
        }
    }
}