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

// Enhanced smooth scrolling for internal links
document.querySelectorAll('a[href^="#"]').forEach((anchor) => {
  anchor.addEventListener("click", function (e) {
    e.preventDefault();
    const target = document.querySelector(this.getAttribute("href"));
    if (target) {
      const navHeight = document.querySelector(".navbar").offsetHeight;
      const targetPosition = target.offsetTop - navHeight;

      window.scrollTo({
        top: targetPosition,
        behavior: "smooth",
      });

      // Add ripple effect
      this.style.position = 'relative';
      this.style.overflow = 'hidden';
      
      const ripple = document.createElement('span');
      ripple.style.position = 'absolute';
      ripple.style.borderRadius = '50%';
      ripple.style.background = 'rgba(160, 200, 120, 0.3)';
      ripple.style.transform = 'scale(0)';
      ripple.style.animation = 'ripple 0.6s linear';
      ripple.style.left = '50%';
      ripple.style.top = '50%';
      ripple.style.width = ripple.style.height = '100px';
      ripple.style.marginLeft = ripple.style.marginTop = '-50px';
      
      this.appendChild(ripple);
      
      setTimeout(() => {
        ripple.remove();
      }, 600);
    }
  });
});

// Enhanced counter animation with easing
function animateCounters() {
  const counters = document.querySelectorAll(".stat-number[data-count]");

  counters.forEach((counter) => {
    const target = parseInt(counter.getAttribute("data-count"));
    const duration = 2000;
    const start = performance.now();

    const updateCounter = (currentTime) => {
      const elapsed = currentTime - start;
      const progress = Math.min(elapsed / duration, 1);
      
      // Easing function for smooth animation
      const easeOutQuart = 1 - Math.pow(1 - progress, 4);
      const current = Math.floor(target * easeOutQuart);
      
      counter.textContent = current;
      
      if (progress < 1) {
        requestAnimationFrame(updateCounter);
      } else {
        counter.textContent = target;
        // Add completion effect
        counter.style.transform = 'scale(1.1)';
        setTimeout(() => {
          counter.style.transform = 'scale(1)';
        }, 200);
      }
    };

    // Observe counter
    const counterObserver = new IntersectionObserver(
      (entries) => {
        entries.forEach((entry) => {
          if (entry.isIntersecting) {
            requestAnimationFrame(updateCounter);
            counterObserver.unobserve(counter);
          }
        });
      },
      { threshold: 0.5 }
    );

    counterObserver.observe(counter);
  });
}

// Initialize counter animation
animateCounters();

// Enhanced Leaflet Map with custom styling
function initMap() {
  try {
    // Map centered on Piura region
    const map = L.map("map", {
      center: [-5.1945, -80.6328],
      zoom: 8,
      zoomControl: true,
      attributionControl: true,
    });

    // Custom map tiles with enhanced styling
    L.tileLayer("https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png", {
      maxZoom: 18,
      attribution: "© OpenStreetMap contributors",
    }).addTo(map);

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

    // Province markers with enhanced information
    const provinces = [
      {
        name: "Piura",
        coords: [-5.1945, -80.6328],
        description: "Capital de la región, centro administrativo y cultural",
        color: "#A0C878",
        attractions: ["Plaza de Armas", "Catedral de Piura", "Casa Grau"]
      },
      {
        name: "Sullana",
        coords: [-4.9037, -80.6849],
        description: "Importante centro agrícola y comercial",
        color: "#DDEB9D",
        attractions: ["Represa de Poechos", "Mercado Central", "Iglesia Matriz"]
      },
      {
        name: "Talara",
        coords: [-4.5772, -81.2719],
        description: "Principal centro petrolero del país",
        color: "#A0C878",
        attractions: ["Refinería de Talara", "Playa Lobitos", "Cabo Blanco"]
      },
      {
        name: "Paita",
        coords: [-5.0892, -81.1144],
        description: "Puerto pesquero e industrial más importante",
        color: "#DDEB9D",
        attractions: ["Puerto de Paita", "Iglesia de la Merced", "Malecón"]
      },
      {
        name: "Sechura",
        coords: [-5.5569, -80.8222],
        description: "Conocida por el desierto y la producción de fosfatos",
        color: "#A0C878",
        attractions: ["Desierto de Sechura", "Laguna Ñapique", "Illescas"]
      },
      {
        name: "Morropón",
        coords: [-5.1781, -79.9703],
        description: "Rica tradición musical y artesanal",
        color: "#DDEB9D",
        attractions: ["Chulucanas", "Cerámica de Chulucanas", "Tondero"]
      },
      {
        name: "Huancabamba",
        coords: [-5.2372, -79.4525],
        description: "Famosa por las Lagunas de las Huaringas",
        color: "#A0C878",
        attractions: ["Lagunas de las Huaringas", "Curanderos", "Medicina tradicional"]
      },
      {
        name: "Ayabaca",
        coords: [-4.6378, -79.7111],
        description: "Tierra del Señor Cautivo y tradiciones ancestrales",
        color: "#DDEB9D",
        attractions: ["Señor Cautivo", "Aypate", "Bosques de neblina"]
      },
    ];

    // Add enhanced markers to map
    provinces.forEach((province, index) => {
      const marker = L.marker(province.coords, {
        icon: createCustomIcon(province.color),
      }).addTo(map);

      // Enhanced popup content
      const popupContent = `
        <div class="custom-popup" style="text-align: center; padding: 15px; min-width: 250px;">
          <h6 style="color: #A0C878; margin-bottom: 10px; font-size: 18px; font-weight: 600;">
            <i class="bi bi-geo-alt"></i> ${province.name}
          </h6>
          <p style="margin: 10px 0; font-size: 14px; line-height: 1.4; color: #4a5568;">
            ${province.description}
          </p>
          <div style="margin: 15px 0;">
            <h7 style="color: #A0C878; font-weight: 500; font-size: 14px;">Principales Atractivos:</h7>
            <ul style="list-style: none; padding: 5px 0; margin: 0; font-size: 12px;">
              ${province.attractions.map(attraction => `<li style="padding: 2px 0;">• ${attraction}</li>`).join('')}
            </ul>
          </div>
          <a href="/provincia/${province.name.toLowerCase()}" 
             style="display: inline-block; background: linear-gradient(135deg, #A0C878 0%, #8bb564 100%); 
                    color: white; padding: 8px 16px; border-radius: 8px; text-decoration: none; 
                    font-weight: 500; font-size: 13px; margin-top: 10px; transition: all 0.3s ease;">
            <i class="bi bi-arrow-right"></i> Explorar ${province.name}
          </a>
        </div>
      `;

      marker.bindPopup(popupContent, {
        maxWidth: 300,
        className: 'custom-popup-container'
      });

      // Add hover effect with animation
      marker.on("mouseover", function () {
        this.openPopup();
        // Add bounce animation
        const markerElement = this.getElement();
        if (markerElement) {
          markerElement.style.animation = 'markerBounce 0.6s ease-in-out';
        }
      });

      // Add click tracking
      marker.on("click", function () {
        console.log(`Clicked on ${province.name}`);
        // Add analytics tracking here if needed
      });

      // Delayed marker appearance for visual effect
      setTimeout(() => {
        marker.setOpacity(1);
      }, index * 200);
    });

    // Add scale control
    L.control.scale().addTo(map);

    // Add custom map controls
    const customControl = L.Control.extend({
      options: {
        position: 'topright'
      },
      onAdd: function (map) {
        const container = L.DomUtil.create('div', 'custom-map-control');
        container.innerHTML = `
          <button onclick="resetMapView()" style="background: #A0C878; color: white; border: none; 
                  padding: 8px 12px; border-radius: 6px; cursor: pointer; font-size: 12px;">
            <i class="bi bi-house"></i> Vista General
          </button>
        `;
        return container;
      }
    });

    map.addControl(new customControl());

    // Global function to reset map view
    window.resetMapView = function() {
      map.setView([-5.1945, -80.6328], 8);
    };

    console.log("Enhanced map initialized successfully");
  } catch (error) {
    console.error("Error initializing map:", error);
    // Show fallback message
    const mapContainer = document.getElementById("map");
    if (mapContainer) {
      mapContainer.innerHTML = `
        <div class="d-flex align-items-center justify-content-center h-100 text-center">
          <div>
            <i class="bi bi-map" style="font-size: 3rem; color: var(--color-primary); opacity: 0.5;"></i>
            <h5 style="color: var(--color-text-muted); margin-top: 1rem;">Mapa no disponible</h5>
            <p style="color: var(--color-text-muted); margin: 0;">Estamos trabajando para restaurar la funcionalidad del mapa.</p>
          </div>
        </div>
      `;
    }
  }
}

// Animated Dog Interactions
function initAnimatedDog() {
  const dog = document.getElementById('animatedDog');
  if (!dog) return;

  let clickCount = 0;
  const dogMessages = [
    "¡Guau! 🐕",
    "¡Hola! Soy tu guía",
    "¿Exploramos Piura?",
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
  
  @keyframes markerBounce {
    0%, 100% { transform: translateY(0); }
    50% { transform: translateY(-10px); }
  }
`;

// Inject dog animations
const styleSheet = document.createElement('style');
styleSheet.textContent = dogAnimations;
document.head.appendChild(styleSheet);

// Lazy loading for images with enhanced effects
function initLazyLoading() {
  if ("loading" in HTMLImageElement.prototype) {
    // Browser supports native lazy loading
    const lazyImages = document.querySelectorAll('img[loading="lazy"]');
    lazyImages.forEach((img) => {
      img.loading = "lazy";
      
      // Add loading effect
      img.addEventListener('load', function() {
        this.style.opacity = '0';
        this.style.transition = 'opacity 0.5s ease-in-out';
        setTimeout(() => {
          this.style.opacity = '1';
        }, 100);
      });
    });
  } else {
    // Fallback for browsers that don't support lazy loading
    const script = document.createElement("script");
    script.src = "https://cdn.jsdelivr.net/npm/vanilla-lazyload@17.8.3/dist/lazyload.min.js";
    document.body.appendChild(script);

    script.onload = function () {
      const lazyLoadInstance = new LazyLoad({
        elements_selector: "img[loading='lazy']",
        callback_loaded: function(element) {
          element.style.opacity = '0';
          element.style.transition = 'opacity 0.5s ease-in-out';
          setTimeout(() => {
            element.style.opacity = '1';
          }, 100);
        }
      });
    };
  }
}

// Performance optimization: Preload critical resources
function preloadCriticalResources() {
  const criticalImages = [
    "https://storage.googleapis.com/workspace-0f70711f-8b4e-4d94-86f1-2a93ccde5887/image/917778ca-0f78-4862-9368-70fd4d35819e.png",
    "https://storage.googleapis.com/workspace-0f70711f-8b4e-4d94-86f1-2a93ccde5887/image/8b8aa9ca-4cb9-433c-8293-698909baca42.png",
    "https://storage.googleapis.com/workspace-0f70711f-8b4e-4d94-86f1-2a93ccde5887/image/11fa5d85-cb0a-40ec-a51c-23b8605d1d45.png"
  ];

  criticalImages.forEach((src) => {
    const link = document.createElement("link");
    link.rel = "preload";
    link.href = src;
    link.as = "image";
    document.head.appendChild(link);
  });
}

// Enhanced button interactions
function initButtonEffects() {
  document.querySelectorAll(".btn").forEach((btn) => {
    btn.addEventListener("click", function (e) {
      if (this.href && !this.href.startsWith("#")) {
        const loadingHtml = this.innerHTML;
        this.innerHTML = '<span class="loading me-2"></span>Cargando...';
        this.disabled = true;

        // Reset after 2 seconds if still on page
        setTimeout(() => {
          this.innerHTML = loadingHtml;
          this.disabled = false;
        }, 2000);
      }
    });

    // Add hover sound effect (optional)
    btn.addEventListener('mouseenter', function() {
      // You can add subtle sound effects here
      this.style.transform = 'translateY(-2px)';
    });

    btn.addEventListener('mouseleave', function() {
      this.style.transform = 'translateY(0)';
    });
  });
}

// Intersection Observer for advanced animations
function initAdvancedAnimations() {
  const observerOptions = {
    threshold: 0.1,
    rootMargin: "0px 0px -50px 0px",
  };

  const observer = new IntersectionObserver(function (entries) {
    entries.forEach((entry) => {
      if (entry.isIntersecting) {
        entry.target.classList.add("visible");
        
        // Add staggered animation for cards
        if (entry.target.classList.contains('province-card') || 
            entry.target.classList.contains('feature-card') || 
            entry.target.classList.contains('news-card')) {
          
          const cards = entry.target.parentElement.querySelectorAll('.province-card, .feature-card, .news-card');
          cards.forEach((card, index) => {
            setTimeout(() => {
              card.style.transform = 'translateY(0)';
              card.style.opacity = '1';
            }, index * 100);
          });
        }
      }
    });
  }, observerOptions);

  // Observe all animation elements
  document.querySelectorAll(".fade-in, .slide-in-left, .slide-in-right").forEach((el) => {
    observer.observe(el);
  });
}

// Initialize everything when DOM is loaded
document.addEventListener("DOMContentLoaded", function () {
  // Initialize all components
  initAnimatedDog();
  initLazyLoading();
  preloadCriticalResources();
  initButtonEffects();
  initAdvancedAnimations();
  
  // Delay map initialization to ensure container is ready
  setTimeout(initMap, 500);
  
  // Add page load animation
  document.body.style.opacity = '0';
  document.body.style.transition = 'opacity 0.5s ease-in-out';
  
  setTimeout(() => {
    document.body.style.opacity = '1';
  }, 100);
  
  console.log("🎉 Piura Expressa Enhanced - All systems loaded!");
});

// Add some Easter eggs
let konamiCode = [];
const konamiSequence = [38, 38, 40, 40, 37, 39, 37, 39, 66, 65]; // ↑↑↓↓←→←→BA

document.addEventListener('keydown', function(e) {
  konamiCode.push(e.keyCode);
  
  if (konamiCode.length > konamiSequence.length) {
    konamiCode.shift();
  }
  
  if (konamiCode.join(',') === konamiSequence.join(',')) {
    // Easter egg activated!
    const dog = document.getElementById('animatedDog');
    if (dog) {
      dog.style.animation = 'dogCelebration 2s ease-in-out infinite';
      showDogMessage('¡Código secreto activado! 🎉');
      
      // Add rainbow effect
      document.body.style.filter = 'hue-rotate(0deg)';
      let hue = 0;
      const rainbowInterval = setInterval(() => {
        hue += 10;
        document.body.style.filter = `hue-rotate(${hue}deg)`;
        if (hue >= 360) {
          clearInterval(rainbowInterval);
          document.body.style.filter = 'none';
          dog.style.animation = 'dogBounce 3s ease-in-out infinite';
        }
      }, 100);
    }
    konamiCode = [];
  }
});

// Export functions for global access
window.PiuraExpressaEnhanced = {
  initMap,
  showDogMessage,
  resetMapView: window.resetMapView
};
