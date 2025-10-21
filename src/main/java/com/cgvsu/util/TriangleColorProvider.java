package com.cgvsu.util;

import javafx.scene.paint.Color;

public class TriangleColorProvider implements ColorProvider {
    private final Color firstVertexColor;
    private final Color secondVertexColor;
    private final Color thirdVertexColor;

    public TriangleColorProvider(Color firstVertexColor, Color secondVertexColor, Color thirdVertexColor) {
        this.firstVertexColor = firstVertexColor;
        this.secondVertexColor = secondVertexColor;
        this.thirdVertexColor = thirdVertexColor;
    }

    @Override
    public Color getVertexColor(int vertexIndex) {
        return switch (vertexIndex) {
            case 0 -> firstVertexColor;
            case 1 -> secondVertexColor;
            case 2 -> thirdVertexColor;
            default -> throw new IllegalArgumentException();
        };
    }
}
