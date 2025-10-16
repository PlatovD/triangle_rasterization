package com.cgvsu.rasterization;

public interface BorderIterator {
    int getX();

    int getY();

    void next();

    boolean hasNext();
}
