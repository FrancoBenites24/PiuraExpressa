package com.piuraexpressa.util;

import com.piuraexpressa.model.dominio.Archivo;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;

@Component
public class ArchivoUtil {

    /**
     * Guarda un archivo en disco y retorna una entidad Archivo lista para
     * persistir.
     *
     * @param file           Archivo subido
     * @param subdirectorio  Carpeta relativa dentro de /uploads/ donde se guardará
     * @param referenciaTipo Tipo de entidad asociada (Ej: "Provincia", "Noticia",
     *                       etc.)
     * @param referenciaId   ID de la entidad asociada
     * @return Entidad Archivo con metadatos y URL relativa
     * @throws IOException si ocurre un error al guardar el archivo
     */
    public static Archivo guardarArchivo(MultipartFile archivoMultipart, String subdirectorio,
            String referenciaTipo, Long referenciaId) throws IOException {

        if (archivoMultipart == null || archivoMultipart.isEmpty()) {
            throw new IllegalArgumentException("El archivo está vacío o es nulo.");
        }

        String baseUploadDir = "uploads/" + subdirectorio + "/";
        Path uploadPath = Paths.get(baseUploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        String originalFilename = archivoMultipart.getOriginalFilename();
        String extension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }

        String nuevoNombre = subdirectorio.toLowerCase() + "_" + System.currentTimeMillis() + extension;
        Path rutaDestino = uploadPath.resolve(nuevoNombre);
        archivoMultipart.transferTo(rutaDestino.toFile());

        return Archivo.builder()
                .nombreArchivo(nuevoNombre)
                .url("/" + baseUploadDir + nuevoNombre)
                .tipo(archivoMultipart.getContentType())
                .fechaSubida(LocalDateTime.now())
                .referenciaTipo(referenciaTipo)
                .referenciaId(referenciaId)
                .build();
    }

}
