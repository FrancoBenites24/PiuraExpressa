package com.piuraexpressa.repositorio.seguridad;

import com.piuraexpressa.model.seguridad.Usuario;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@Primary
@Qualifier("usuarioRepositorio")
public interface UsuarioRepositorio extends JpaRepository<Usuario, Long> {

    @Query("SELECT u FROM Usuario u LEFT JOIN FETCH u.roles r LEFT JOIN FETCH r.permisos WHERE u.username = :username")
    Optional<Usuario> findByUsernameConRolesYPermisos(@Param("username") String username);

    Optional<Usuario> findByUsername(String username);

    Optional<Usuario> findByEmail(String email);

    Optional<Usuario> findByNumeroDocumento(String numeroDocumento);

    boolean existsByUsernameIgnoreCase(String username);

    boolean existsByEmailIgnoreCase(String email);

    boolean existsByNumeroDocumento(String numeroDocumento);

    @Query(value = """
                SELECT * FROM usuarios u
                WHERE
                    (:filtro IS NULL OR LOWER(CAST(u.username AS text)) LIKE LOWER(CONCAT('%', :filtro, '%'))
                        OR LOWER(CAST(u.nombres AS text)) LIKE LOWER(CONCAT('%', :filtro, '%'))
                        OR LOWER(CAST(u.apellidos AS text)) LIKE LOWER(CONCAT('%', :filtro, '%')))
                    AND (:provincia IS NULL OR LOWER(CAST(u.provincia AS text)) LIKE LOWER(CONCAT('%', :provincia, '%')))
                """, nativeQuery = true)
    Page<Usuario> buscarUsuariosFiltrados(
            @Param("filtro") String filtro,
            @Param("provincia") String provincia,
            Pageable pageable);

}
