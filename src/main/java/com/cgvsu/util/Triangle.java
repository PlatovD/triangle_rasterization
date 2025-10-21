package com.cgvsu.util;

import com.cgvsu.rasterization.Rasterization;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Triangle {
    List<Point> points;
    ColorProvider colorProvider;

    public Triangle(Point point1, Point point2, Point point3, ColorProvider cp) {
        points = new ArrayList<>(List.of(point1, point2, point3));
        colorProvider = cp;
    }

    public void setPointByIndex(int i, Point point) {
        points.set(i, point);
    }

    public Point getVertex(int vertexIndex) {
        return points.get(vertexIndex);
    }

    public Color getColor(int vertexIndex) {
        return colorProvider.getVertexColor(vertexIndex);
    }


    public List<Point> getPoints() {
        return Collections.unmodifiableList(points);
    }


    public Point getPointByIndex(int i) {
        return points.get(i);
    }


    public int getPointIndex(Point point) {
        if (point == null) throw new IllegalArgumentException();
        for (int i = 0; i < 3; i++) {
            if (points.get(i).equals(point)) {
                return i;
            }
        }
        return -1;
    }

    public void draw(GraphicsContext gc) {
        Point pointA = points.get(0);
        Point pointB = points.get(1);
        Point pointC = points.get(2);
        Rasterization.drawInterpolatedTriangleByIterator(
                gc.getPixelWriter(),
                pointA.getX(), pointA.getY(),
                pointB.getX(), pointB.getY(),
                pointC.getX(), pointC.getY(),
                colorProvider.getVertexColor(0),
                colorProvider.getVertexColor(1),
                colorProvider.getVertexColor(2)
        );
    }

    public int pointCnt() {
        return points.size();
    }

    @Override
    public String toString() {
        return "Triangle{" +
                "points=" + points +
                ", colorProvider=" + colorProvider +
                '}';
    }
}
