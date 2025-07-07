package com.piuraexpressa.config;

import jakarta.annotation.PostConstruct;
import org.locationtech.jts.io.WKTReader;
import org.locationtech.jts.geom.Point;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public class StringToPointConverter implements Converter<String, Point> {

    private WKTReader reader;

    @PostConstruct
    public void init() {
        reader = new WKTReader();
    }

    @Override
    public Point convert(@NonNull String source) {
        try {
            return (Point) reader.read(source);
        } catch (Exception e) {
            throw new IllegalArgumentException("Coordenadas inválidas. Formato esperado: POINT(lon lat)", e);
        }
    }
}
