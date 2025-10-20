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

    /**
     * Метод рисования линии. Позволяет рисовать ее из точки 1 в точку 2. Использует округление и
     * линейную интерполяцию для того, чтобы получить промежуточные значения и построить путь из 1 в 2.
     *
     * @param pixelWriter объект для рисования
     * @param x1          точка начала
     * @param y1          точка начала
     * @param x2          точка конца
     * @param y2          точка конца
     */
    public static void drawLine(PixelWriter pixelWriter, double x1, double y1, double x2, double y2) {
        double dx = x2 - x1;
        double dy = y2 - y1;
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

    /**
     * Вычисляет значения функции d = f(i) от i=i0 до i=i1
     * Использует числа с плавающей точкой и их округление.
     *
     * @param d0 значение функции в начальной координате
     * @param i0 аргумент функции в начальной координате
     * @param d1 значение функции в конечной координате
     * @param i1 аргумент функции в конечной координате
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


    /**
     * Классический алгоритм Брезенхейма. Для каждого y из отрезка на входе находит для него x.
     *
     * @param x0 координата точки начала
     * @param y0 координата точки начала
     * @param x1 координата точки конца
     * @param y1 координата точки конца
     * @return массив из x для данных y
     */
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
     * С помощью линий рисует треугольник, однако не заполняет его, а ставит пиксели только на стороны.
     */
    public static void drawWireFrameTriangle(PixelWriter pixelWriter, double x0, double y0, double x1, double y1, double x2, double y2) {
        drawLine(pixelWriter, x0, y0, x1, y1);
        drawLine(pixelWriter, x0, y0, x2, y2);
        drawLine(pixelWriter, x2, y2, x1, y1);
    }

    /**
     * Метод для растеризации треугольника с использованием идеи scanline и нахождения границ через алгоритм
     * Брезенхейма.
     */
    public static void drawTriangle(
            final PixelWriter pixelWriter,
            int x0, int y0,
            int x1, int y1,
            int x2, int y2

    ) {
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

    private static int findThirdOrderDeterminant(
            int a00, int a01, int a02,
            int a10, int a11, int a12,
            int a20, int a21, int a22
    ) {
        return ((a00 * a11 * a22) + (a10 * a21 * a02) + (a01 * a12 * a20)) - ((a02 * a11 * a20) + (a01 * a10 * a22) + (a12 * a21 * a00));
    }

    private static double[] findBarycentricCords(int xCur, int yCur, int x0, int y0, int x1, int y1, int x2, int y2) {
        int mainDet = findThirdOrderDeterminant(
                x0, x1, x2,
                y0, y1, y2,
                1, 1, 1
        );
        if (mainDet == 0) return new double[]{0, 0, 0};

        int detForAlpha = findThirdOrderDeterminant(
                xCur, x1, x2,
                yCur, y1, y2,
                1, 1, 1
        );
        int detForBeta = findThirdOrderDeterminant(
                x0, xCur, x2,
                y0, yCur, y2,
                1, 1, 1
        );
        int detForLambda = findThirdOrderDeterminant(
                x0, x1, xCur,
                y0, y1, yCur,
                1, 1, 1
        );
        return new double[]{(double) detForAlpha / mainDet, (double) detForBeta / mainDet, (double) detForLambda / mainDet};
    }

    /**
     * Метод для растеризации треугольника через scanline и алгоритм Брезенхейма для нахождения границ,
     * реализованный в виде итератора по границам. Позволяет закрашивать треугольник тремя цветами с
     * интерполяцией между вершинами. Использует барицентрические координаты. Работает медленнее заполнения
     * одним цветом.
     */
    public static void drawInterpolatedTriangleByIterator(
            final PixelWriter pixelWriter,
            int x0, int y0,
            int x1, int y1,
            int x2, int y2,
            Color color1,
            Color color2,
            Color color3
    ) {
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
        BorderIterator borderIterator1 = new BresenhamBorderIterator(x0, y0, x1, y1);
        BorderIterator borderIterator2 = new BresenhamBorderIterator(x0, y0, x2, y2);
        BorderIterator borderIterator3 = new BresenhamBorderIterator(x1, y1, x2, y2);

        while (borderIterator1.hasNext() && borderIterator2.hasNext()) {
            int y = borderIterator1.getY();
            int leftX = Math.min(borderIterator1.getX(), borderIterator2.getX());
            int rightX = Math.max(borderIterator1.getX(), borderIterator2.getX());
            for (int x = leftX; x <= rightX; x++) {
                double[] barycentric = findBarycentricCords(x, y, x0, y0, x1, y1, x2, y2);
                pixelWriter.setColor(x, y, createColorFromBarycentric(barycentric, color1, color2, color3));
            }
            borderIterator1.next();
            borderIterator2.next();
        }

        while (borderIterator3.hasNext() && borderIterator2.hasNext()) {
            int y = borderIterator2.getY();
            int leftX = Math.min(borderIterator2.getX(), borderIterator3.getX());
            int rightX = Math.max(borderIterator2.getX(), borderIterator3.getX());
            for (int x = leftX; x <= rightX; x++) {
                double[] barycentric = findBarycentricCords(x, y, x0, y0, x1, y1, x2, y2);
                pixelWriter.setColor(x, y, createColorFromBarycentric(barycentric, color1, color2, color3));
            }
            borderIterator2.next();
            borderIterator3.next();
        }
    }

    private static Color createColorFromBarycentric(double[] barycentric, Color color1, Color color2, Color color3) {
        double a = Math.max(0, Math.min(1, barycentric[0]));
        double b = Math.max(0, Math.min(1, barycentric[1]));
        double c = Math.max(0, Math.min(1, barycentric[2]));

        return new Color(
                color1.getRed() * a + color2.getRed() * b + color3.getRed() * c,
                color1.getGreen() * a + color2.getGreen() * b + color3.getGreen() * c,
                color1.getBlue() * a + color2.getBlue() * b + color3.getBlue() * c,
                1);

    }

    /**
     * Метод для растеризации треугольника с использованием идеи scanline и нахождения границ через алгоритм
     * Брезенхейма, реализованный в виде итератора по границам треугольника.
     */
    public static void drawTriangleByIterator(
            final PixelWriter pixelWriter,
            int x0, int y0,
            int x1, int y1,
            int x2, int y2,
            Color color
    ) {
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
        BorderIterator borderIterator1 = new BresenhamBorderIterator(x0, y0, x1, y1);
        BorderIterator borderIterator2 = new BresenhamBorderIterator(x0, y0, x2, y2);
        BorderIterator borderIterator3 = new BresenhamBorderIterator(x1, y1, x2, y2);

        while (borderIterator1.hasNext() && borderIterator2.hasNext()) {
            int y = borderIterator1.getY();
            int leftX = Math.min(borderIterator1.getX(), borderIterator2.getX());
            int rightX = Math.max(borderIterator1.getX(), borderIterator2.getX());
            for (int x = leftX; x <= rightX; x++) {
                pixelWriter.setColor(x, y, color);
            }
            borderIterator1.next();
            borderIterator2.next();
        }

        while (borderIterator3.hasNext() && borderIterator2.hasNext()) {
            int y = borderIterator2.getY();
            int leftX = Math.min(borderIterator2.getX(), borderIterator3.getX());
            int rightX = Math.max(borderIterator2.getX(), borderIterator3.getX());
            for (int x = leftX; x <= rightX; x++) {
                pixelWriter.setColor(x, y, color);
            }
            borderIterator2.next();
            borderIterator3.next();
        }
    }

    /**
     * Метод рестеризации треугольника через нахождение границ при помощи линейной интерполяции. Использует
     * операции с плавающей точкой.
     */
    public static void drawTriangleNotBresenham(
            final PixelWriter pixelWriter,
            int x0, int y0,
            int x1, int y1,
            int x2, int y2

    ) {
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
