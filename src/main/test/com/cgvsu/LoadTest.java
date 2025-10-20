package com.cgvsu;

import com.cgvsu.rasterization.Rasterization;
import com.cgvsu.testutil.MockPixelWriter;
import javafx.scene.image.PixelWriter;
import javafx.scene.paint.Color;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.Random;


public class LoadTest {
    private final int TRIANGLE_CORD_BOUND = 10000;
    private final int CNT_TRIANGLES = 2000;
    private final PixelWriter pixelWriter = new MockPixelWriter();

    @Test
    public void draw1000Polygons() {
        Random random = new Random();
        Assertions.assertTimeout(Duration.ofSeconds(5), () -> {
            for (int i = 0; i < CNT_TRIANGLES; i++) {
                Rasterization.drawTriangleNotBresenham(
                        pixelWriter,
                        random.nextInt(TRIANGLE_CORD_BOUND), random.nextInt(TRIANGLE_CORD_BOUND),
                        random.nextInt(TRIANGLE_CORD_BOUND), random.nextInt(TRIANGLE_CORD_BOUND),
                        random.nextInt(TRIANGLE_CORD_BOUND), random.nextInt(TRIANGLE_CORD_BOUND)
                );
            }
        });
    }

    @Test
    public void draw1000PolygonsBresenham() {
        Random random = new Random();
        Assertions.assertTimeout(Duration.ofSeconds(5), () -> {
            for (int i = 0; i < CNT_TRIANGLES; i++) {
                Rasterization.drawTriangleByIterator(
                        pixelWriter,
                        random.nextInt(TRIANGLE_CORD_BOUND), random.nextInt(TRIANGLE_CORD_BOUND),
                        random.nextInt(TRIANGLE_CORD_BOUND), random.nextInt(TRIANGLE_CORD_BOUND),
                        random.nextInt(TRIANGLE_CORD_BOUND), random.nextInt(TRIANGLE_CORD_BOUND),
                        Color.PINK
                );
            }
        });
    }
}
