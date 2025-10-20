package com.cgvsu.testutil;

import javafx.scene.image.PixelFormat;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.paint.Color;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

public class MockPixelWriter implements PixelWriter {
    @Override
    public PixelFormat getPixelFormat() {
        return null;
    }

    @Override
    public void setArgb(int i, int i1, int i2) {

    }

    @Override
    public void setColor(int i, int i1, Color color) {

    }

    @Override
    public <T extends Buffer> void setPixels(int i, int i1, int i2, int i3, PixelFormat<T> pixelFormat, T t, int i4) {

    }

    @Override
    public void setPixels(int i, int i1, int i2, int i3, PixelFormat<ByteBuffer> pixelFormat, byte[] bytes, int i4, int i5) {

    }

    @Override
    public void setPixels(int i, int i1, int i2, int i3, PixelFormat<IntBuffer> pixelFormat, int[] ints, int i4, int i5) {

    }

    @Override
    public void setPixels(int i, int i1, int i2, int i3, PixelReader pixelReader, int i4, int i5) {

    }
}
