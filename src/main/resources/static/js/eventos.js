// JavaScript for Eventos page interactive features

// Show participation modal and populate event details
function showParticipationModal(eventName) {
    const participationModal = new bootstrap.Modal(document.getElementById('participationModal'));
    const eventDetailsDiv = document.getElementById('eventDetails');
    eventDetailsDiv.innerHTML = `<p><strong>Evento:</strong> ${eventName}</p>`;
    participationModal.show();
}

// Clear all filters in the filter form
function clearFilters() {
    document.getElementById('searchInput').value = '';
    document.getElementById('provinciaSelect').value = '';
    document.getElementById('fechaSelect').value = '';
    document.getElementById('precioSelect').value = '';
    filterEvents();
}

// Filter events based on filter inputs (basic client-side filtering)
function filterEvents() {
    const searchText = document.getElementById('searchInput').value.toLowerCase();
    const provincia = document.getElementById('provinciaSelect').value.toLowerCase();
    const fecha = document.getElementById('fechaSelect').value.toLowerCase();
    const precio = document.getElementById('precioSelect').value.toLowerCase();

    const eventsGrid = document.getElementById('eventsGrid');
    const eventCards = eventsGrid.getElementsByClassName('event-card');

    for (let card of eventCards) {
        const title = card.querySelector('.event-title').textContent.toLowerCase();
        const description = card.querySelector('.event-description').textContent.toLowerCase();
        const provinciaMatch = provincia === '' || card.innerHTML.toLowerCase().includes(provincia);
        const fechaMatch = fecha === '' || card.innerHTML.toLowerCase().includes(fecha);
        const precioMatch = precio === '' || card.innerHTML.toLowerCase().includes(precio);
        const searchMatch = title.includes(searchText) || description.includes(searchText);

        if (provinciaMatch && fechaMatch && precioMatch && searchMatch) {
            card.style.display = '';
        } else {
            card.style.display = 'none';
        }
    }
}

// Share event (copy event name to clipboard as example)
function shareEvent(eventName) {
    const shareText = `Mira este evento: ${eventName} en Piura Expressa!`;
    if (navigator.clipboard) {
        navigator.clipboard.writeText(shareText).then(() => {
            alert('Texto del evento copiado al portapapeles para compartir.');
        }, () => {
            alert('No se pudo copiar el texto al portapapeles.');
        });
    } else {
        alert('La función de compartir no está soportada en este navegador.');
    }
}

// Toggle favorite state on the button
function toggleFavorite(button) {
    const icon = button.querySelector('i');
    if (icon.classList.contains('bi-heart')) {
        icon.classList.remove('bi-heart');
        icon.classList.add('bi-heart-fill');
        button.setAttribute('aria-label', 'Quitar de favoritos');
    } else {
        icon.classList.remove('bi-heart-fill');
        icon.classList.add('bi-heart');
        button.setAttribute('aria-label', 'Agregar a favoritos');
    }
}

// Show event details (placeholder)
function showEventDetails(eventName) {
    alert(`Mostrar detalles para el evento: ${eventName}`);
}

// Handle filter form submission
document.getElementById('filtersForm').addEventListener('submit', function(event) {
    event.preventDefault();
    filterEvents();
});

// Handle participation form submission
document.getElementById('participationForm').addEventListener('submit', function(event) {
    event.preventDefault();
    const observaciones = document.getElementById('observaciones').value;
    alert('Participación confirmada. Observaciones: ' + (observaciones || 'Ninguna'));
    const participationModal = bootstrap.Modal.getInstance(document.getElementById('participationModal'));
    participationModal.hide();
    document.getElementById('participationForm').reset();
});
