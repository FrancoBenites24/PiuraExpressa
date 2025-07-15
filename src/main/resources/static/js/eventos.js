const token = document.querySelector('meta[name="_csrf"]').getAttribute('content');
const header = document.querySelector('meta[name="_csrf_header"]').getAttribute('content');

    let currentReviewEventId = null;
    
    window.participarEnEvento = function(eventoId) {
            fetch('/eventos/' + eventoId + '/participar', {
                method: 'POST',
                cache: 'no-store',
                headers: {
                    [header]: token // ya definidos por Thymeleaf arriba
                }
            })
            .then(response => {
                if (response.ok) {
                    actualizarEventoUI(eventoId); // mejor experiencia sin recarga
                } else {
                    alert("No se pudo registrar tu participación.");
                }
            })
            .catch(error => {
                console.error("Error al participar:", error);
                alert("Hubo un problema.");
            });
        }

    window.cancelarParticipacion = function(id) {
          fetch('/eventos/' + id + '/cancelar', {
              method: 'POST',
              cache: 'no-store',
                headers: {
                [header]: token
                }          
            })
        .then(res => {
            if (res.ok) {
                actualizarEventoUI(id);
            } else {
                alert('Error al cancelar participación');
            }
        })
        .catch(() => alert('Error al cancelar participación'));
      }

    function abrirModalResena(id) {
      currentReviewEventId = id;
      const reviewModal = new bootstrap.Modal(document.getElementById('reviewModal'));
      document.getElementById('reviewRating').value = '';
      document.getElementById('reviewComment').value = '';
      document.getElementById('reviewError').style.display = 'none';
      reviewModal.show();
    }

    function compartirEvento(id) {
        navigator.clipboard.writeText('https://piuraexpressa.com/eventos/' + id).then(() => {
        alert('Enlace copiado al portapapeles.');
        });
    }

function verDetallesEvento(id) {
  fetch('/api/eventos/' + id, { cache: 'no-store' })
    .then(res => res.json())
    .then(evento => {
      const container = document.getElementById('detalleEventoContenido');
      const fechaInicio = evento?.fechaInicio ? formatearFecha(evento.fechaInicio) : 'Fecha no disponible';
      const descripcion = evento?.descripcion || 'Sin descripción';
      const ubicacion = evento?.ubicacion || evento?.provincia || 'Ubicación no especificada';
      const capacidad = evento.capacidad ? `Capacidad: ${evento.capacidad}` : 'Sin límite de capacidad';
      const participantes = evento.participantesActuales || 0;
      const precio = (evento?.precio != null && evento.precio > 0) ? `S/ ${evento.precio}` : 'Gratuito';

        if (!container) return;

      container.innerHTML = `
        <h4 class="mb-2">${evento.titulo}</h4>
        <p>${descripcion}</p>
        <ul class="list-unstyled mb-3">
          <li><i class="bi bi-calendar-event me-1"></i> ${fechaInicio}</li>
          <li><i class="bi bi-geo-alt me-1"></i> ${ubicacion}</li>
          <li><i class="bi bi-people me-1"></i> Participantes: ${participantes}</li>
          <li><i class="bi bi-cash-coin me-1"></i> Precio: ${precio}</li>
          <li><i class="bi bi-bar-chart me-1"></i> ${capacidad}</li>
        </ul>
      `;

      const list = document.getElementById('reviewsList');
      const count = document.getElementById('reviewsCount');
      list.innerHTML = '';
      count.textContent = `${evento.resenas?.length || 0} reseña(s)`;

      if (!evento.resenas || evento.resenas.length === 0) {
        list.innerHTML = `<div class="text-muted">No hay reseñas para este evento.</div>`;
      } else {
        list.innerHTML = evento.resenas.map(resena => {
            const calificacion = resena?.calificacion || 0;
            const estrellas = Array.from({ length: 5 }).map((_, i) =>
            `<i class="bi ${i < calificacion ? 'bi-star-fill text-warning' : 'bi-star text-muted'}"></i>`
            ).join('');

          return `
            <div class="review-item border-bottom pb-2 mb-2">
              <div class="d-flex justify-content-between align-items-start">
                <div>
                  <strong>${resena.nombreUsuario}</strong> 
                  <div class="text-muted small">${formatearFecha(resena.fechaRegistro || resena.fechaResena || '')}</div>
                </div>
                <div class="stars">${estrellas}</div>
              </div>
              <div class="mt-2">${resena.contenido || ''}</div>
            </div>
          `;
        }).join('');
      }

      const modal = new bootstrap.Modal(document.getElementById('eventDetailsModal'));
      modal.show();
    })
    .catch(err => {
      console.error('Error al cargar detalles del evento:', err);
      alert('No se pudieron cargar los detalles del evento.');
    });
}

    function actualizarEventoUI(id) {
          fetch('/api/eventos/' + id, {cache: 'no-store'})
              .then(response => response.json())
              .then(evento => {
                const card = document.querySelector(`.event-card[data-evento-id="${id}"]`);
                if (!card) return;

                // Actualizar botones
                const btnParticipar = card.querySelector('.btn-participate');
                const btnCancelar = card.querySelector('.btn-participate.btn-warning');
                const btnEscribirResena = card.querySelector('.btn-review:not([disabled])');
                const btnYaResenado = card.querySelector('.btn-review[disabled]');

                if (evento.finalizado) {
                    if (evento.yaParticipa && !evento.yaResenado) {
                        if (btnParticipar) btnParticipar.style.display = 'none';
                        if (btnCancelar) btnCancelar.style.display = 'none';
                        if (btnEscribirResena) btnEscribirResena.style.display = 'inline-block';
                        if (btnYaResenado) btnYaResenado.style.display = 'none';
                    } else if (evento.yaResenado) {
                        if (btnParticipar) btnParticipar.style.display = 'none';
                        if (btnCancelar) btnCancelar.style.display = 'none';
                        if (btnEscribirResena) btnEscribirResena.style.display = 'none';
                        if (btnYaResenado) btnYaResenado.style.display = 'inline-block';
                    } else {
                        if (btnParticipar) btnParticipar.style.display = 'inline-block';
                        if (btnCancelar) btnCancelar.style.display = 'none';
                        if (btnEscribirResena) btnEscribirResena.style.display = 'none';
                        if (btnYaResenado) btnYaResenado.style.display = 'none';
                    }
                } else {
                    if (evento.yaParticipa) {
                        if (btnParticipar) btnParticipar.style.display = 'none';
                        if (btnCancelar) btnCancelar.style.display = 'inline-block';
                        if (btnEscribirResena) btnEscribirResena.style.display = 'none';
                        if (btnYaResenado) btnYaResenado.style.display = 'none';
                    } else {
                        if (btnParticipar) btnParticipar.style.display = 'inline-block';
                        if (btnCancelar) btnCancelar.style.display = 'none';
                        if (btnEscribirResena) btnEscribirResena.style.display = 'none';
                        if (btnYaResenado) btnYaResenado.style.display = 'none';
                    }
                }

                // Actualizar participantes y barra
                const participantesInfo = card.querySelector('.capacity-info strong');
                if (participantesInfo) {
                    participantesInfo.textContent = evento.participantesActuales != null ? evento.participantesActuales : 0;
                }
                const capacidadTotal = evento.capacidad || 0;
                const capacidadTexto = card.querySelector('.capacity-info span');
                if (capacidadTexto) {
                    capacidadTexto.textContent = ` de ${capacidadTotal} disponibles`;
                }
                const capacityFill = card.querySelector('.capacity-fill');
                if (capacityFill) {
                    capacityFill.style.width = evento.porcentajeOcupado + '%';
                }
            })
            .catch(error => {
                console.error('Error al actualizar UI del evento:', error);
            });
    }

    function formatearEstado(estado) {
          switch (estado) {
              case 'EN_CURSO': return 'En curso';
              case 'FINALIZADO': return 'Finalizado';
              case 'PROXIMO': return 'Próximo';
              default: return '';
          }
      }
   
    function formatearFecha(fechaISO) {
    if (!fechaISO) return 'Fecha no disponible';
    const fecha = new Date(fechaISO);
    if (isNaN(fecha.getTime())) return 'Fecha inválida';
    return fecha.toLocaleDateString('es-PE', {
        day: 'numeric',
        month: 'long',
        year: 'numeric',
        hour: '2-digit',
        minute: '2-digit'
    });
    }

     
    async function cargarProvincias() {
        try {
            const response = await fetch('/api/provincias/todas');
            const provincias = await response.json();
            const select = document.getElementById('provinciaSelect');

            provincias.forEach(p => {
                const option = document.createElement('option');
                option.value = p.nombre.toLowerCase();
                option.textContent = p.nombre;
                select.appendChild(option);
            });
        } catch (error) {
            console.error('Error al cargar provincias:', error);
        }
    }    
     
    function renderizarPaginacion(data) {
        const paginationContainer = document.querySelector('.pagination');
        if (!paginationContainer) return;

        paginationContainer.innerHTML = '';

        const { totalPages, number, first, last } = data;

        if (totalPages <= 1) return;

        const crearItem = (text, disabled, page, active = false) => `
            <li class="page-item ${disabled ? 'disabled' : ''} ${active ? 'active' : ''}">
                <a class="page-link" href="#" onclick="cargarEventos(${page}); return false;">${text}</a>
            </li>`;

        paginationContainer.innerHTML += crearItem('Anterior', first, number - 1);

        for (let i = 0; i < totalPages; i++) {
            paginationContainer.innerHTML += crearItem(i + 1, false, i, i === number);
        }

        paginationContainer.innerHTML += crearItem('Siguiente', last, number + 1);
    }

    async function cargarEventos() {
        const grid = document.getElementById('eventGrid');
if (!grid) {
    console.error('No se encontró el contenedor #eventGrid');
    return;
}
          const filtros = {
              texto: document.getElementById('searchInput').value,
              provincia: document.getElementById('provinciaSelect').value,
              fecha: document.getElementById('fechaSelect').value,
              precio: document.getElementById('precioSelect').value,
              pagina: 0,
              tamanio: 10
          };

          const response = await fetch('/api/eventos/buscar', {
              method: 'POST',
              headers: { 'Content-Type': 'application/json' },
              body: JSON.stringify(filtros)
          });

          const data = await response.json();
          grid.innerHTML = '';

          if (!data.content || data.content.length === 0) {
              grid.innerHTML = `
                  <div class="empty-state">
                      <div class="empty-state-icon"><i class="bi bi-calendar-x"></i></div>
                      <h3>No se encontraron eventos</h3>
                      <p>Intenta con otros filtros o vuelve más tarde.</p>
                  </div>
              `;
              return;
          }

          data.content.forEach(evento => {
              const statusClass = evento.estado === 'EN_CURSO' ? 'status-ongoing' :
                                  evento.estado === 'FINALIZADO' ? 'status-finished' : 'status-upcoming';

              const precioLabel = evento.precio && evento.precio > 0 ? `S/ ${evento.precio}` : 'Gratuito';
              const precioClass = evento.precio && evento.precio > 0 ? '' : 'price-free';

              const calificacion = evento.calificacionPromedio || 0;
              const estrellas = Array.from({ length: 5 }).map((_, i) => {
                  const icon = i < Math.floor(calificacion) ? 'star-fill'
                              : (i < calificacion ? 'star-half' : 'star');
                  return `<i class="bi bi-${icon} star${icon === 'star' ? ' empty' : ''}"></i>`;
              }).join('');

              const puedeParticipar = evento.puedeParticipar === true;
              const yaParticipa = evento.yaParticipa === true;

              const btnParticipar = puedeParticipar
                ? `<button class="btn-participate" ${evento.finalizado ? 'disabled' : ''} onclick="participarEnEvento('${evento.id}')"><i class="bi bi-calendar-plus"></i> Participar</button>`
                : '';
              const btnCancelar = yaParticipa
                ? `<button class="btn-participate btn-warning" onclick="cancelarParticipacion('${evento.id}')"><i class="bi bi-calendar-x"></i> Cancelar participación</button>`
                : '';

              const card = `
              <article class="event-card fade-in" data-evento-id="${evento.id}">
                  <div class="position-relative">
                      <img src="${evento.imagenUrl || '/img/evento-default.jpg'}" alt="${evento.titulo}" class="event-image">
                      <div class="event-status ${statusClass}">${formatearEstado(evento.estado)}</div>
                      <div class="event-price ${precioClass}">${precioLabel}</div>
                  </div>
                  <div class="event-content">
                      <h3 class="event-title">${evento.titulo}</h3>
                      <p class="event-description">${evento.descripcion || ''}</p>
                      <div class="event-meta">
                          <div class="meta-item"><i class="bi bi-calendar-event meta-icon"></i> ${formatearFecha(evento.fechaInicio)}</div>
                          <div class="meta-item"><i class="bi bi-geo-alt meta-icon"></i> ${evento.ubicacion || evento.provincia}</div>
                      </div>
                      <div class="event-capacity">
                          <div class="capacity-info">
                              <strong>${evento.participantesActuales || 0} participantes</strong>
                              ${evento.capacidad ? ` de ${evento.capacidad} disponibles` : ''}
                          </div>
                          ${evento.capacidad ? `
                          <div class="capacity-bar">
                              <div class="capacity-fill" style="width: ${evento.porcentajeOcupado || 0}%"></div>
                          </div>` : ''}
                      </div>
                      <div class="event-rating">
                          <div class="stars">${estrellas}</div>
                          <span class="rating-text">${calificacion.toFixed(1)} (${evento.cantidadResenas || 0} reseñas)</span>
                      </div>
                      <div class="event-actions">
${btnParticipar}
${btnCancelar}
                          <button class="btn-secondary-action" onclick="shareEvent('${evento.titulo}')" aria-label="Compartir evento">
                              <i class="bi bi-share"></i>
                          </button>
                          <button class="btn-secondary-action" onclick="toggleFavorite(this)" aria-label="Agregar a favoritos">
                              <i class="bi bi-heart"></i>
                          </button>
                          <button class="btn-secondary-action" onclick="showEventDetails('${evento.titulo}')" aria-label="Ver detalles">
                              <i class="bi bi-eye"></i>
                          </button>
                      </div>
                  </div>
              </article>`;

              grid.insertAdjacentHTML('beforeend', card);
          });
        renderizarPaginacion(data);
      }


      document.addEventListener('DOMContentLoaded', () => {
      document.getElementById('reviewForm').addEventListener('submit', function (event) {
        event.preventDefault();
        const rating = document.getElementById('reviewRating').value;
        const comment = document.getElementById('reviewComment').value.trim();

        if (!rating) {
          document.getElementById('reviewError').textContent = 'Por favor seleccione una calificación.';
          document.getElementById('reviewError').style.display = 'block';
          return;
        }

        const ResenaDTO = {
          calificacion: parseInt(rating),
          contenido: comment
        };

        fetch('/api/eventos/' + currentReviewEventId + '/resena', {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
            [header]: token
          },
          body: JSON.stringify(ResenaDTO)
        })
          .then(response => {
            if (response.ok) return response.json();
            else if (response.status === 401) {
              alert('Debe iniciar sesión para enviar una reseña.');
              throw new Error('Unauthorized');
            } else {
              throw new Error('Error al enviar la reseña.');
            }
          })
          .then(() => {
            const reviewModal = bootstrap.Modal.getInstance(document.getElementById('reviewModal'));
            reviewModal.hide();
            alert('Reseña enviada correctamente.');
            actualizarEventoUI(currentReviewEventId);
          })
          .catch(error => {
            document.getElementById('reviewError').textContent = error.message;
            document.getElementById('reviewError').style.display = 'block';
          });
      });

  const form = document.getElementById('filtersForm');
  form?.addEventListener('submit', async (e) => {
    e.preventDefault();
    await cargarEventos();
  });

  cargarEventos();
  cargarProvincias(); 
});
