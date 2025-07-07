// Initialize AOS
AOS.init({
  duration: 800,
  easing: 'ease-in-out',
  once: true,
  mirror: false
});

// Initialize VanillaTilt for 3D card effects
VanillaTilt.init(document.querySelectorAll(".tilt-card"), {
  max: 15,
  speed: 400,
  glare: true,
  "max-glare": 0.2,
});

document.addEventListener('DOMContentLoaded', function() {
  // Variables de la provincia
  const provinciaData = {
    nombre: /*[[${provincia.nombre}]]*/ 'Piura',
    latitud: /*[[${provincia.coordenadas != null ? provincia.coordenadas.y : -5.1945}]]*/ -5.1945,
    longitud: /*[[${provincia.coordenadas != null ? provincia.coordenadas.x : -80.6328}]]*/ -80.6328,
    puntosInteres: /*[[${puntosInteres}]]*/ []
  };

  // Inicializar mapa
  let map;
  let currentLayer = 'street';
  let markers = [];

  function initMap() {
    try {
      // Crear el mapa
      map = L.map('provinciaMap').setView([provinciaData.latitud, provinciaData.longitud], 12);

      // Capa de mapa base
      const streetLayer = L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
        attribution: '© OpenStreetMap contributors'
      });

      const satelliteLayer = L.tileLayer('https://server.arcgisonline.com/ArcGIS/rest/services/World_Imagery/MapServer/tile/{z}/{y}/{x}', {
        attribution: '© Esri'
      });

      streetLayer.addTo(map);

      // Custom animated icon for markers
      const createCustomIcon = (color = '#A0C878') => {
        return L.divIcon({
          html: `
            <div class="custom-marker" style="background: linear-gradient(135deg, ${color} 0%, #8bb564 100%);">
              <i class="bi bi-geo-alt-fill" style="color: white; font-size: 14px;"></i>
            </div>
          `,
          iconSize: [30, 30],
          className: "custom-div-icon",
        });
      };

      // Marcador principal de la provincia
      const mainMarker = L.marker([provinciaData.latitud, provinciaData.longitud], {
        icon: createCustomIcon('#A0C878')
      })
        .addTo(map)
        .bindPopup(`
          <div class="custom-popup">
            <div class="popup-title">${provinciaData.nombre}</div>
            <div class="popup-address">Capital de la provincia</div>
          </div>
        `);

      // Agregar puntos de interés si existen
      if (provinciaData.puntosInteres && provinciaData.puntosInteres.length > 0) {
        provinciaData.puntosInteres.forEach(punto => {
          if (punto.coordenadas) {
            const lat = punto.coordenadas.y || punto.latitud;
            const lng = punto.coordenadas.x || punto.longitud;
            
            if (lat && lng) {
              const marker = L.marker([lat, lng], {
                icon: createCustomIcon('#DDEB9D')
              })
                .addTo(map)
                .bindPopup(`
                  <div class="custom-popup">
                    <div class="popup-title">${punto.nombre}</div>
                    <div class="popup-address">${punto.categoria ? punto.categoria.nombre : 'Punto de interés'}</div>
                    <p style="margin: 10px 0; font-size: 14px;">${punto.descripcion || ''}</p>
                  </div>
                `);
              markers.push(marker);
            }
          }
        });
      }

      // Guardar referencias de capas
      map.streetLayer = streetLayer;
      map.satelliteLayer = satelliteLayer;

      console.log("Mapa inicializado correctamente");
    } catch (error) {
      console.error("Error inicializando mapa:", error);
    }
  }

  // Navbar scroll effect with enhanced animations
  let lastScrollTop = 0;
  window.addEventListener("scroll", function () {
    const navbar = document.querySelector(".navbar");
    const scrollTop = window.pageYOffset || document.documentElement.scrollTop;
    
    if (scrollTop > 50) {
      navbar.classList.add("scrolled");
    } else {
      navbar.classList.remove("scrolled");
    }

    // Animate dog on scroll
    const dog = document.getElementById('animatedDog');
    if (dog && Math.abs(scrollTop - lastScrollTop) > 5) {
      dog.classList.add('scrolling');
      setTimeout(() => {
        dog.classList.remove('scrolling');
      }, 500);
    }
    lastScrollTop = scrollTop;
  });

  function scrollToSection(id) {
    const el = document.getElementById(id);
    if (el) {
      const navHeight = document.querySelector(".navbar").offsetHeight;
      const targetPosition = el.offsetTop - navHeight;

      window.scrollTo({
        top: targetPosition,
        behavior: "smooth",
      });
    }
  }

  // Botón de volver arriba
  const backToTop = document.getElementById("backToTop");
  window.addEventListener("scroll", () => {
    backToTop.classList.toggle("d-none", window.scrollY < 300);
  });
  backToTop.addEventListener("click", () =>
    window.scrollTo({ top: 0, behavior: "smooth" })
  );

  // Funciones del mapa
  window.resetMapView = function() {
    if (map) {
      map.setView([provinciaData.latitud, provinciaData.longitud], 12);
    }
  }

  window.toggleSatellite = function() {
    if (map) {
      if (currentLayer === 'street') {
        map.removeLayer(map.streetLayer);
        map.addLayer(map.satelliteLayer);
        currentLayer = 'satellite';
      } else {
        map.removeLayer(map.satelliteLayer);
        map.addLayer(map.streetLayer);
        currentLayer = 'street';
      }
    }
  }

  window.findMyLocation = function() {
    if (navigator.geolocation) {
      navigator.geolocation.getCurrentPosition((pos) => {
        const lat = pos.coords.latitude;
        const lng = pos.coords.longitude;
        if (map) {
          map.setView([lat, lng], 15);
          L.marker([lat, lng])
            .addTo(map)
            .bindPopup('Tu ubicación actual')
            .openPopup();
        }
      }, (error) => {
        alert('No se pudo obtener tu ubicación: ' + error.message);
      });
    } else {
      alert('Tu navegador no soporta geolocalización');
    }
  }

  // Filtros de puntos de interés
  const filterButtons = document.querySelectorAll('[data-filter]');
  filterButtons.forEach(button => {
    button.addEventListener('click', function() {
      const filter = this.getAttribute('data-filter');
      
      // Actualizar botones activos
      filterButtons.forEach(btn => btn.classList.remove('active'));
      this.classList.add('active');

      // Filtrar marcadores
      markers.forEach(marker => {
        if (filter === 'all') {
          marker.addTo(map);
        } else {
          // Implementar lógica de filtrado basada en categoría
          marker.addTo(map);
        }
      });
    });
  });

  // Animated Dog Interactions
  function initAnimatedDog() {
    const dog = document.getElementById('animatedDog');
    if (!dog) return;

    let clickCount = 0;
    const dogMessages = [
      "¡Guau! 🐕",
      "¡Hola! Soy tu guía",
      "¿Exploramos esta provincia?",
      "¡Sígueme! 🦴",
      "¡Woof woof! 🎾"
    ];

    dog.addEventListener('click', function() {
      clickCount++;
      
      // Show message
      const message = dogMessages[clickCount % dogMessages.length];
      showDogMessage(message);
      
      // Special animations based on clicks
      if (clickCount % 5 === 0) {
        // Special celebration animation
        dog.style.animation = 'dogCelebration 1s ease-in-out';
        setTimeout(() => {
          dog.style.animation = 'dogBounce 3s ease-in-out infinite';
        }, 1000);
      }
      
      // Change dog emoji occasionally
      if (clickCount % 3 === 0) {
        const dogEmojis = ['🐕', '🐶', '🦮', '🐕‍🦺'];
        dog.textContent = dogEmojis[Math.floor(Math.random() * dogEmojis.length)];
        setTimeout(() => {
          dog.textContent = '🐕';
        }, 2000);
      }
    });

    // Dog follows scroll with smooth movement
    let ticking = false;
    window.addEventListener('scroll', function() {
      if (!ticking) {
        requestAnimationFrame(function() {
          const scrollPercent = window.scrollY / (document.documentElement.scrollHeight - window.innerHeight);
          const maxMove = 50;
          const moveY = scrollPercent * maxMove;
          
          dog.style.transform = `translateY(-${moveY}px)`;
          ticking = false;
        });
        ticking = true;
      }
    });
  }

  // Show dog message function
  function showDogMessage(message) {
    const existingMessage = document.querySelector('.dog-message');
    if (existingMessage) {
      existingMessage.remove();
    }

    const messageDiv = document.createElement('div');
    messageDiv.className = 'dog-message';
    messageDiv.textContent = message;
    messageDiv.style.cssText = `
      position: fixed;
      bottom: 120px;
      right: 20px;
      background: linear-gradient(135deg, #A0C878 0%, #8bb564 100%);
      color: white;
      padding: 10px 15px;
      border-radius: 20px;
      font-size: 14px;
      font-weight: 500;
      z-index: 1001;
      animation: dogMessageAppear 0.3s ease-out;
      box-shadow: 0 4px 15px rgba(160, 200, 120, 0.3);
    `;

    document.body.appendChild(messageDiv);

    setTimeout(() => {
      messageDiv.style.animation = 'dogMessageDisappear 0.3s ease-in';
      setTimeout(() => {
        messageDiv.remove();
      }, 300);
    }, 2000);
  }

  // Add CSS animations for dog
  const dogAnimations = `
    @keyframes dogCelebration {
      0%, 100% { transform: scale(1) rotate(0deg); }
      25% { transform: scale(1.2) rotate(-10deg); }
      50% { transform: scale(1.3) rotate(10deg); }
      75% { transform: scale(1.2) rotate(-5deg); }
    }
    
    @keyframes dogMessageAppear {
      0% { opacity: 0; transform: translateY(20px) scale(0.8); }
      100% { opacity: 1; transform: translateY(0) scale(1); }
    }
    
    @keyframes dogMessageDisappear {
      0% { opacity: 1; transform: translateY(0) scale(1); }
      100% { opacity: 0; transform: translateY(-20px) scale(0.8); }
    }
    
    .custom-marker {
      width: 30px;
      height: 30px;
      border-radius: 50%;
      display: flex;
      align-items: center;
      justify-content: center;
      border: 3px solid white;
      box-shadow: 0 4px 15px rgba(160, 200, 120, 0.3);
      animation: markerPulse 2s ease-in-out infinite;
    }
    
    @keyframes markerPulse {
      0%, 100% { transform: scale(1); }
      50% { transform: scale(1.1); }
    }
  `;

  // Inject dog animations
  const styleSheet = document.createElement('style');
  styleSheet.textContent = dogAnimations;
  document.head.appendChild(styleSheet);

  // Initialize all components
  initAnimatedDog();
  
  // Delay map initialization to ensure container is ready
  setTimeout(initMap, 500);

  // Make functions globally available
  window.scrollToSection = scrollToSection;
  
  console.log("🎉 Piura Expressa Detalle Provincia - All systems loaded!");
});
