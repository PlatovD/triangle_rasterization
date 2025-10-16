package com.cgvsu;

import com.cgvsu.rasterization.Rasterization;
import javafx.scene.canvas.Canvas;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.Random;

public class LoadTest {
    private final int TRIANGLE_CORD_BOUND = 300;
    private final Canvas canvas = new Canvas();

    @Test
    public void draw1000PolygonsBresenham() {
        Random random = new Random();
        Assertions.assertTimeout(Duration.ofSeconds(4), () -> {
            for (int i = 0; i < 1000; i++) {
                Rasterization.drawTriangle(
                        canvas.getGraphicsContext2D(),
                        random.nextInt(TRIANGLE_CORD_BOUND), random.nextInt(TRIANGLE_CORD_BOUND),
                        random.nextInt(TRIANGLE_CORD_BOUND), random.nextInt(TRIANGLE_CORD_BOUND),
                        random.nextInt(TRIANGLE_CORD_BOUND), random.nextInt(TRIANGLE_CORD_BOUND)
                );
            }
        });
    }

    @Test
    public void draw1000Polygons() {
        Random random = new Random();
        Assertions.assertTimeout(Duration.ofSeconds(4), () -> {
            for (int i = 0; i < 1000; i++) {
                Rasterization.drawTriangleNotBresenham(
                        canvas.getGraphicsContext2D(),
                        random.nextInt(TRIANGLE_CORD_BOUND), random.nextInt(TRIANGLE_CORD_BOUND),
                        random.nextInt(TRIANGLE_CORD_BOUND), random.nextInt(TRIANGLE_CORD_BOUND),
                        random.nextInt(TRIANGLE_CORD_BOUND), random.nextInt(TRIANGLE_CORD_BOUND)
                );
            }
        });
    }
}
