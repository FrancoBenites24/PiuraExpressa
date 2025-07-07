package com.piuraexpressa.servicio.impl;

import com.piuraexpressa.dto.UsuarioLikePublicacionDTO;
import com.piuraexpressa.mapper.UsuarioLikePublicacionMapper;
import com.piuraexpressa.model.dominio.UsuarioLikePublicacion;

import com.piuraexpressa.repositorio.dominio.UsuarioLikePublicacionRepositorio;
import com.piuraexpressa.servicio.UsuarioLikePublicacionServicio;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UsuarioLikePublicacionServicioImpl implements UsuarioLikePublicacionServicio {

    private final UsuarioLikePublicacionRepositorio likeRepo;
    private final UsuarioLikePublicacionMapper mapper;

    @Override
    public UsuarioLikePublicacionDTO likePublicacion(Long usuarioId, Long publicacionId) {
        UsuarioLikePublicacion.UsuarioLikePublicacionId id = new UsuarioLikePublicacion.UsuarioLikePublicacionId(usuarioId, publicacionId);
        Optional<UsuarioLikePublicacion> existing = likeRepo.findById(id);
        if (existing.isPresent()) {
            // Ya existe, actualiza la fecha
            UsuarioLikePublicacion like = existing.get();
            like.setFechaLike(LocalDateTime.now());
            return mapper.toDTO(likeRepo.save(like));
        } else {
            UsuarioLikePublicacion like = UsuarioLikePublicacion.builder()
                    .id(id)
                    .fechaLike(LocalDateTime.now())
                    .build();
            return mapper.toDTO(likeRepo.save(like));
        }
    }

    @Override
    public void unlikePublicacion(Long usuarioId, Long publicacionId) {
        UsuarioLikePublicacion.UsuarioLikePublicacionId id = new UsuarioLikePublicacion.UsuarioLikePublicacionId(usuarioId, publicacionId);
        likeRepo.deleteById(id);
    }

    @Override
    public long contarLikes(Long publicacionId) {
        return likeRepo.countByIdPublicacionId(publicacionId);
    }

    @Override
    public boolean usuarioHaDadoLike(Long usuarioId, Long publicacionId) {
        return likeRepo.findByIdUsuarioIdAndIdPublicacionId(usuarioId, publicacionId).isPresent();
    }

    @Override
    public List<UsuarioLikePublicacionDTO> obtenerLikesPorPublicacion(Long publicacionId) {
        return likeRepo.findByIdPublicacionId(publicacionId)
                .stream().map(mapper::toDTO).toList();
    }
}
