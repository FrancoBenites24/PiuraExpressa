package com.piuraexpressa.repositorio.seguridad;

import com.piuraexpressa.model.seguridad.RolPermiso;
import com.piuraexpressa.model.seguridad.RolPermisoId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RolPermisoRepositorio extends JpaRepository<RolPermiso, RolPermisoId> {
}
