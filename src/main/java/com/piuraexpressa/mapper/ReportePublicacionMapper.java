package com.piuraexpressa.mapper;

import com.piuraexpressa.dto.ReportePublicacionDTO;
import com.piuraexpressa.model.dominio.ReportePublicacion;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ReportePublicacionMapper {

    ReportePublicacionDTO toDto(ReportePublicacion entidad);

    ReportePublicacion toEntidad(ReportePublicacionDTO dto);

    List<ReportePublicacionDTO> toDtoLista(List<ReportePublicacion> entidades);

    List<ReportePublicacion> toEntidadLista(List<ReportePublicacionDTO> dtos);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void actualizarEntidadDesdeDto(ReportePublicacionDTO dto, @MappingTarget ReportePublicacion entidad);
}
