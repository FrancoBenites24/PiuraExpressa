# Plan para actualizar detalle-provincia.html

## Análisis de entidades actuales:
1. **Provincia**: id, nombre, subtitulo, descripcion, imagenPrincipal, altitudPromedio, climaDescripcion, mejorEpocaVisita, activo, coordenadas
2. **EstadisticaProvincia**: ano, poblacionTotal, densidadPoblacional, indiceAlfabetizacion, tasaDesempleo, porcentajeUrbano, fuente
3. **HistoriaProvincia**: ano, titulo, descripcion, imagenUrl, ordenCronologico, activo
4. **PuntoInteres**: nombre, descripcion, categoria, coordenadas, activo

## Cambios necesarios en el template:
1. **Actualizar campos de estadísticas** para usar los campos reales de EstadisticaProvincia
2. **Agregar campos nuevos** como altitudPromedio, climaDescripcion, mejorEpocaVisita
3. **Actualizar puntos de interés** para usar categoria en lugar de tipo
4. **Agregar el perrito guía** del index.html
5. **Mantener** la misma paleta de colores, Bootstrap, Bootstrap icons, elementos 3D y animaciones

## Estructura del template actualizado:
1. **Hero Section**: usar imagenPrincipal, nombre, subtitulo, descripcion
2. **Stats Section**: poblacionTotal, densidadPoblacional, indiceAlfabetizacion, tasaDesempleo, porcentajeUrbano
3. **Info adicional**: altitudPromedio, climaDescripcion, mejorEpocaVisita
4. **Mapa interactivo**: usar coordenadas de provincia y puntos de interés
5. **Historia**: usar HistoriaProvincia con imagenUrl y ordenCronologico
6. **Puntos de interés**: usar categoria en lugar de tipo
7. **Perrito guía**: elemento animado del index.html

## Archivos a actualizar:
1. detalle-provincia.html - template principal
2. detalle-provincia.css - estilos (ya actualizado)
3. detalle-provincia.js - funcionalidad JavaScript
