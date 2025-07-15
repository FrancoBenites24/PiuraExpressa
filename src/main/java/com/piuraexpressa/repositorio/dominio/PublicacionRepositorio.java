package com.piuraexpressa.repositorio.dominio;

import com.piuraexpressa.model.dominio.Publicacion;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PublicacionRepositorio
        extends JpaRepository<Publicacion, Long>, JpaSpecificationExecutor<Publicacion> {
    long countByAutor(Long autorId);

    List<Publicacion> findByAutor(Long autorId);

    Page<Publicacion> findByAutor(Long autorId, Pageable pageable);

    Optional<Publicacion> findBySlug(String slug);

    @Query(value = """
            SELECT p.id
            FROM publicaciones p
            LEFT JOIN usuario_like_publicacion ulp ON p.id = ulp.publicacion_id
            LEFT JOIN comentarios c ON p.id = c.publicacion_id
            WHERE p.activa = true
            GROUP BY p.id
            ORDER BY COUNT(DISTINCT ulp.usuario_id) + COUNT(DISTINCT c.id) DESC
            LIMIT :limit OFFSET :offset
            """, nativeQuery = true)
    List<Long> findPublicacionesPopulares(@Param("limit") int limit, @Param("offset") int offset);

    @Query("SELECT COUNT(p) FROM Publicacion p WHERE p.activo = true")
    long contarPublicacionesActivas();

    // filtros admin
    @Query(value = """
            SELECT p.id
            FROM publicaciones p
            LEFT JOIN comentarios c ON p.id = c.publicacion_id
            WHERE p.activa = true
            GROUP BY p.id
            ORDER BY COUNT(c.id) DESC
            LIMIT :limit OFFSET :offset
            """, nativeQuery = true)
    List<Long> findMasComentadas(@Param("limit") int limit, @Param("offset") int offset);

    @Query(value = """
            SELECT p.id
            FROM publicaciones p
            LEFT JOIN usuario_like_publicacion ulp ON p.id = ulp.publicacion_id
            WHERE p.activa = true
            GROUP BY p.id
            ORDER BY COUNT(ulp.usuario_id) DESC
            LIMIT :limit OFFSET :offset
            """, nativeQuery = true)
    List<Long> findMasLikeadas(@Param("limit") int limit, @Param("offset") int offset);

    @Query(value = """
            SELECT p.id
            FROM publicaciones p
            LEFT JOIN reporte_publicacion r ON p.id = r.publicacion_id
            WHERE p.activa = true
            GROUP BY p.id
            ORDER BY COUNT(r.id) DESC
            LIMIT :limit OFFSET :offset
            """, nativeQuery = true)
    List<Long> findMasReportadas(@Param("limit") int limit, @Param("offset") int offset);

}
