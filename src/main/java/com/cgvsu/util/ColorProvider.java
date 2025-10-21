package com.cgvsu.util;

import javafx.scene.paint.Color;

public interface ColorProvider {
    default Color getVertexColor(int vertexIndex) {
        return Color.BLACK;
    }
}
