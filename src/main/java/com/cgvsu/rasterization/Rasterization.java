package com.cgvsu.rasterization;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.PixelWriter;
import javafx.scene.paint.Color;

public class Rasterization {

    public static void drawRectangle(
            final GraphicsContext graphicsContext,
            final int x, final int y,
            final int width, final int height,
            final Color color) {
        final PixelWriter pixelWriter = graphicsContext.getPixelWriter();

        for (int row = y; row < y + height; ++row)
            for (int col = x; col < x + width; ++col)
                pixelWriter.setColor(col, row, color);
    }

    public static void drawLine(GraphicsContext graphicsContext, double x1, double y1, double x2, double y2) {
        double dx = x2 - x1;
        double dy = y2 - y1;
        final PixelWriter pixelWriter = graphicsContext.getPixelWriter();
        if (Math.abs(dx) > Math.abs(dy)) {
            if (x1 > x2) {
                double tmp = x2;
                x2 = x1;
                x1 = tmp;

                tmp = y2;
                y2 = y1;
                y1 = tmp;
            }
            int[] points = interpolate(y1, x1, y2, x2);
            for (double x = x1; x < x2; x++) {
                pixelWriter.setColor((int) x, points[(int) (x - x1)], Color.RED);
            }
        } else {
            if (y1 > y2) {
                double tmp = x2;
                x2 = x1;
                x1 = tmp;

                tmp = y2;
                y2 = y1;
                y1 = tmp;
            }
            int[] points = interpolate(x1, y1, x2, y2);
            for (double y = y1; y < y2; y++) {
                pixelWriter.setColor(points[(int) (y - y1)], (int) y, Color.RED);
            }
        }
    }

    public static int[] interpolateBresenham(int x0, int y0, int x1, int y1) {
        int sizeOut = Math.abs(y1 - y0) + 1;
        boolean change = Math.abs(y1 - y0) > Math.abs(x1 - x0);
        boolean changeDirection = false;
        if (change) {
            int tmp = y0;
            y0 = x0;
            x0 = tmp;

            tmp = y1;
            y1 = x1;
            x1 = tmp;
        }

        if (x1 < x0) {
            changeDirection = true;
            int tmp = x1;
            x1 = x0;
            x0 = tmp;

            tmp = y1;
            y1 = y0;
            y0 = tmp;
        }

        int[] values = new int[sizeOut];
        int dx = x1 - x0;
        int dy = y1 - y0;
        int step = dy < 0 ? -1 : 1;
        int error = 0;
        int y = y0;
        int index = 0;
        for (int x = x0; x <= x1; x++) {
            if (change)
                values[index++] = y;
            error += Math.abs(2 * dy);
            if (error > dx) {
                if (!change) values[index++] = changeDirection ? x1 - (x - x0) : x;
                y += step;
                error = -(2 * dx - error);
            }
        }
        return values;
    }

    /**
     * Calculates values of function d = f(i) from i=i0 to i=i1
     */
    private static int[] interpolate(double d0, double i0, double d1, double i1) {
        double tmp;
        int[] values = new int[(int) ((i1 - i0) + 1)];
        if (i0 > i1) {
            tmp = i1;
            i1 = i0;
            i0 = tmp;
        }

        double a = (d1 - d0) / (i1 - i0);
        double value = d0;
        for (double i = i0; i <= i1; i++) {
            values[(int) (i - i0)] = (int) Math.round(value);
            value += a;
        }
        return values;
    }

    public static void drawWireFrameTriangle(GraphicsContext graphicsContext, double x0, double y0, double x1, double y1, double x2, double y2) {
        drawLine(graphicsContext, x0, y0, x1, y1);
        drawLine(graphicsContext, x0, y0, x2, y2);
        drawLine(graphicsContext, x2, y2, x1, y1);
    }

    public static void drawTriangle(
            final GraphicsContext graphicsContext,
            int x0, int y0,
            int x1, int y1,
            int x2, int y2

    ) {
        final PixelWriter pixelWriter = graphicsContext.getPixelWriter();
        int tmp;
        if (y0 > y1) {
            tmp = y1;
            y1 = y0;
            y0 = tmp;

            tmp = x1;
            x1 = x0;
            x0 = tmp;
        }
        if (y1 > y2) {
            tmp = y2;
            y2 = y1;
            y1 = tmp;

            tmp = x2;
            x2 = x1;
            x1 = tmp;
        }
        if (y0 > y1) {
            tmp = y1;
            y1 = y0;
            y0 = tmp;

            tmp = x1;
            x1 = x0;
            x0 = tmp;
        }

        int[] top1 = interpolateBresenham(x0, y0, x1, y1);
        int[] top2 = interpolateBresenham(x0, y0, x2, y2);
        int[] bottom1 = interpolateBresenham(x1, y1, x2, y2);
        int[] res = new int[top2.length + 1];
        System.arraycopy(top1, 0, res, 0, top1.length - 1);
        System.arraycopy(bottom1, 0, res, top1.length - 1, bottom1.length);
        int[] left, right;
        int m = (y1 - y0) / 2;
        if (res[m] < top2[m]) {
            left = res;
            right = top2;
        } else {
            left = top2;
            right = res;
        }

        for (int i = y0; i < y2; i++) {
            for (int j = left[i - y0]; j <= right[i - y0]; j++) {
                pixelWriter.setColor(j, i, Color.BLACK);
            }
        }
    }

    public static void drawTriangleNotBresenham(
            final GraphicsContext graphicsContext,
            int x0, int y0,
            int x1, int y1,
            int x2, int y2

    ) {
        final PixelWriter pixelWriter = graphicsContext.getPixelWriter();
        int tmp;
        if (y0 > y1) {
            tmp = y1;
            y1 = y0;
            y0 = tmp;

            tmp = x1;
            x1 = x0;
            x0 = tmp;
        }
        if (y1 > y2) {
            tmp = y2;
            y2 = y1;
            y1 = tmp;

            tmp = x2;
            x2 = x1;
            x1 = tmp;
        }
        if (y0 > y1) {
            tmp = y1;
            y1 = y0;
            y0 = tmp;

            tmp = x1;
            x1 = x0;
            x0 = tmp;
        }

        int[] x01 = interpolate(x0, y0, x1, y1);
        int[] x12 = interpolate(x1, y1, x2, y2);
        int[] x02 = interpolate(x0, y0, x2, y2);
        int[] x012 = new int[x01.length + x12.length - 1];
        System.arraycopy(x01, 0, x012, 0, x01.length - 1);
        System.arraycopy(x12, 0, x012, x01.length - 1, x12.length);

        int m = x02.length / 2;
        int[] xLeft;
        int[] xRight;
        if (x02[m] < x012[m]) {
            xLeft = x02;
            xRight = x012;
        } else {
            xLeft = x012;
            xRight = x02;
        }

        for (int y = y0; y <= y2; y++) {
            for (int x = xLeft[y - y0]; x <= xRight[y - y0]; x++) {
                pixelWriter.setColor(x, y, Color.RED);
            }
        }
    }
}
