        // Variables globales
        let currentPage = 0;
        const pageSize = 5;
        let loading = false;
        let currentFilter = 'all';
        let currentSearch = '';
        let currentUser = null;


        // Obtener usuario autenticado y mostrar botón de crear evento si es admin
        async function loadCurrentUser() {
            try {
                const response = await fetch('/api/usuario/autenticado');
                if (!response.ok) throw new Error('No autenticado');
                window.currentUser = await response.json();
                if (window.currentUser && window.currentUser.roles && window.currentUser.roles.includes('ROLE_ADMIN')) {
                    document.getElementById('newEventBtn').style.display = 'block';
                    document.getElementById('newEventBtn').addEventListener('click', () => {
                        window.location.href = '/admin/eventos';
                    });
                }
            } catch (error) {
                console.log('Usuario no autenticado o sin permisos admin');
            }
        }

        // Función para cargar publicaciones desde el backend
        async function loadPosts(reset = false) {
            if (loading) return;
            loading = true;
            if (reset) {
                currentPage = 0;
                document.getElementById('publicacionesContainer').innerHTML = '';
            }
            let url = `/api/publicaciones?page=${currentPage}&size=${pageSize}`;
            if (window.currentUser && window.currentUser.id) {
                url += `&usuarioId=${window.currentUser.id}`;
            }
            if (currentFilter === 'popular') {
                url += '&sort=totalLikes,desc';
            } else if (currentFilter === 'recent') {
                url += '&sort=fechaCreacion,desc';
            } else if (currentFilter === 'mine') {
            }
            if (currentSearch) {
                url += `&search=${encodeURIComponent(currentSearch)}`;
            }
            try {
                const response = await fetch(url);
                if (!response.ok) throw new Error('Error al cargar publicaciones');
                const data = await response.json();
                console.log('currentPage before render:', currentPage);
                data.content.forEach(post => {
                    document.getElementById('publicacionesContainer').insertAdjacentHTML('beforeend', renderPost(post));
                });
                console.log('data.last:', data.last);
                if (data.last) {
                    // window.removeEventListener('scroll', handleScroll);
                    // Instead of removing event listener, set loading to true to prevent further loads
                    loading = true;
                } else {
                    currentPage++;
                    console.log('currentPage incremented to:', currentPage);
                }
            } catch (error) {
                console.error(error);
            } finally {
                loading = false;
            }
        }

        // Función para editar publicación
        function editPost(postId) {
            const postElement = document.querySelector(`.post-card[data-post-id="${postId}"]`);
            if (!postElement) return;
            
            // Obtener datos actuales del post
            const titulo = postElement.querySelector('.post-content h5').textContent;
            const contenido = postElement.querySelector('.post-content p').textContent;
            
            // Llenar el formulario de edición
            document.getElementById('editPostId').value = postId;
            document.getElementById('editPostTitle').value = titulo;
            document.getElementById('editPostContent').value = contenido;
            
            // Mostrar el modal
            new bootstrap.Modal(document.getElementById('editPostModal')).show();
        }

        // Función para eliminar publicación
        async function deletePost(postId) {
            if (!confirm('¿Estás seguro de que deseas eliminar esta publicación?')) {
                return;
            }

            try {
                const response = await fetch(`/api/publicaciones/${postId}`, {
                    method: 'DELETE'
                });
                if (!response.ok) throw new Error('Error al eliminar la publicación');
                
                // Eliminar la publicación del DOM
                const postElement = document.querySelector(`.post-card[data-post-id="${postId}"]`);
                if (postElement) {
                    postElement.remove();
                }
            } catch (error) {
                console.error(error);
                alert('Error al eliminar la publicación');
            }
        }

        // Función para renderizar una publicación
        function renderPost(post) {
            console.log('Render post:', post.id, 'usuarioHaDadoLike:', post.usuarioHaDadoLike, 'totalLikes:', post.totalLikes);
            const likedClass = post.usuarioHaDadoLike === true ? 'liked' : '';
            const totalLikes = post.totalLikes !== undefined && post.totalLikes !== null ? post.totalLikes : 0;
            const tags = post.etiquetas ? post.etiquetas.split(',').map(tag => `<span class="badge" style="background-color: var(--secondary-color); color: var(--text-dark); margin-right: 0.25rem;">#${tag.trim()}</span>`).join('') : '';
            return `
                <div class="post-card" data-post-id="${post.id}">
                    <div class="post-header">
                        <div class="d-flex align-items-center">
                            <div class="user-avatar me-3">${getInitials(post.nombreUsuario)}</div>
                            <div class="flex-grow-1">
                                <h6 class="mb-0 fw-bold">${post.nombreUsuario}</h6>
                                <small class="text-muted">${post.tiempoTranscurrido}</small>
                            </div>
                            ${(post.puedeEditar || post.puedeEliminar) ? `
                            <div class="dropdown">
                                <button class="btn btn-sm" data-bs-toggle="dropdown">
                                    <i class="bi bi-three-dots"></i>
                                </button>
                                <ul class="dropdown-menu">
                                    ${post.puedeEditar ? `<li><a class="dropdown-item edit-post" href="#" data-post-id="${post.id}"><i class="bi bi-pencil-square me-2"></i>Editar</a></li>` : ''}
                                    ${post.puedeEliminar ? `<li><a class="dropdown-item delete-post" href="#" data-post-id="${post.id}"><i class="bi bi-trash me-2"></i>Eliminar</a></li>` : ''}
                                </ul>
                            </div>
                            ` : ''}
                        </div>
                    </div>  
                    <div class="post-content">
                        <h5 class="mb-3">${post.titulo}</h5>
                        <p>${post.contenido}</p>
                        ${post.imagen ? `<img src="${post.imagen}" alt="Imagen de la publicación" class="post-image">` : ''}
                        <div class="mt-3">${tags}</div>
                    </div>
                    <div class="post-actions">
                        <div class="d-flex align-items-center justify-content-between flex-wrap">
                            <button class="action-btn ${likedClass}" data-post-id="${post.id}" onclick="toggleLike(this)" title="Me gusta">
                                <i class="bi bi-heart${post.usuarioHaDadoLike ? '-fill' : ''} me-1"></i>${totalLikes}
                            </button>
                            <button class="action-btn" onclick="toggleComments(this)" title="Comentarios">
                                <i class="bi bi-chat me-1"></i>${post.totalComentarios} comentarios
                            </button>
                            <button class="action-btn" onclick="reportPost(${post.id}, this)" title="Reportar">
                                <i class="bi bi-flag"></i>
                            </button>
                            <button class="action-btn" onclick="savePost(${post.id}, this)" title="Guardar">
                                <i class="bi bi-bookmark"></i>
                            </button>
                            <button class="action-btn" onclick="sharePost(${post.id}, this)" title="Compartir">
                                <i class="bi bi-share"></i>
                            </button>
                        </div>
                    </div>
                    <div class="comment-section" style="display: none;">
                        <div class="comments-container"></div>
                        <div class="mt-3">
                            <textarea class="form-control comment-input" rows="2" placeholder="Escribe un comentario..."></textarea>
                            <button class="btn btn-sm btn-primary mt-2" onclick="submitComment(this)">Comentar</button>
                            <div class="text-danger comment-error mt-2" style="display:none;"></div>
                        </div>
                    </div>
                </div>
            `;
        }

        // Función para obtener iniciales del nombre
        function getInitials(name) {
            if (!name) return '';
            const parts = name.trim().split(' ');
            if (parts.length === 1) return parts[0].charAt(0).toUpperCase();
            return parts[0].charAt(0).toUpperCase() + parts[1].charAt(0).toUpperCase();
        }

        // Función para alternar like
        async function toggleLike(button) {
            const postId = button.getAttribute('data-post-id');
            const liked = button.classList.contains('liked');
            // Obtener usuarioId del usuario autenticado (simulado aquí, debe obtenerse dinámicamente)
            const usuarioId = window.currentUser ? window.currentUser.id : null;
            if (!usuarioId) {
                alert('Debes iniciar sesión para dar like.');
                return;
            }
            try {
                const response = await fetch(`/api/publicaciones/${postId}/${liked ? 'unlike' : 'like'}?usuarioId=${usuarioId}`, {
                    method: 'POST',
                });
                if (!response.ok) throw new Error('Error al actualizar like');
                const data = await response.json();
                button.classList.toggle('liked');
                button.innerHTML = `<i class="bi bi-heart${button.classList.contains('liked') ? '-fill' : ''} me-1"></i>${data.totalLikes}`;
            } catch (error) {
                console.error(error);
            }
        }

        // Función para reportar publicación
        async function reportPost(postId, button) {
            try {
                const response = await fetch(`/api/publicaciones/${postId}/reportar`, {
                    method: 'POST',
                });
                if (!response.ok) throw new Error('Error al reportar publicación');
                alert('Publicación reportada correctamente');
            } catch (error) {
                console.error(error);
                alert('Error al reportar publicación');
            }
        }

        // Función para guardar publicación
        async function savePost(postId, button) {
            try {
                const response = await fetch(`/api/publicaciones/${postId}/guardar`, {
                    method: 'POST',
                });
                if (!response.ok) throw new Error('Error al guardar publicación');
                alert('Publicación guardada correctamente');
            } catch (error) {
                console.error(error);
                alert('Error al guardar publicación');
            }
        }

        // Función para compartir publicación
        async function sharePost(postId, button) {
            try {
                const response = await fetch(`/api/publicaciones/${postId}/compartir`, {
                    method: 'POST',
                });
                if (!response.ok) throw new Error('Error al compartir publicación');
                alert('Publicación compartida correctamente');
            } catch (error) {
                console.error(error);
                alert('Error al compartir publicación');
            }
        }

        // Función para alternar comentarios
        function toggleComments(button) {
            const postCard = button.closest('.post-card');
            const commentSection = postCard.querySelector('.comment-section');
            if (commentSection.style.display === 'block') {
                commentSection.style.display = 'none';
            } else {
                commentSection.style.display = 'block';
                loadComments(postCard.getAttribute('data-post-id'), commentSection.querySelector('.comments-container'));
            }
        }

        // Función para cargar comentarios
        async function loadComments(postId, container) {
            try {
                const response = await fetch(`/api/publicaciones/${postId}/comentarios`);
                if (!response.ok) throw new Error('Error al cargar comentarios');
                const comentarios = await response.json();
                container.innerHTML = '';
                comentarios.forEach(comentario => {
                    container.insertAdjacentHTML('beforeend', renderComment(comentario));
                });
            } catch (error) {
                console.error(error);
            }
        }

        // Función para renderizar un comentario
        function renderComment(comentario) {
            return `
                <div class="comment-item">
                    <div class="d-flex">
                        <div class="user-avatar me-2" style="width: 30px; height: 30px; font-size: 0.8rem;">${getInitials(comentario.nombreUsuario)}</div>
                        <div class="flex-grow-1">
                            <strong>${comentario.nombreUsuario}</strong>
                            <p class="mb-1">${comentario.contenido}</p>
                            <small class="text-muted">${comentario.tiempoTranscurrido}</small>
                        </div>
                    </div>
                </div>
            `;
        }

        // Función para enviar un comentario
        async function submitComment(button) {
            const commentSection = button.closest('.comment-section');
            const postCard = button.closest('.post-card');
            const postId = postCard.getAttribute('data-post-id');
            const textarea = commentSection.querySelector('.comment-input');
            const errorDiv = commentSection.querySelector('.comment-error');
            const contenido = textarea.value.trim();

        // Verificar permisos antes de comentar
            const rawPermisos = document.body.getAttribute("data-permisos") || "";
            const permisos = rawPermisos.replace(/[\[\]\s]/g, "").split(",");

            if (!permisos.includes("USUARIO_CREAR_COMENTARIO")) {
                mostrarModalAccesoDenegado();
                return;
            }

            if (!contenido) {
                errorDiv.textContent = 'El comentario no puede estar vacío.';
                errorDiv.style.display = 'block';
                return;
            }

            try {
                const response = await fetch(`/api/publicaciones/${postId}/comentarios`, {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify({ contenido })
                });
                if (!response.ok) {
                    const errorData = await response.json();
                    errorDiv.textContent = errorData.message || 'Error al enviar el comentario.';
                    errorDiv.style.display = 'block';
                    return;
                }
                const nuevoComentario = await response.json();
                commentSection.querySelector('.comments-container').insertAdjacentHTML('beforeend', renderComment(nuevoComentario));
                textarea.value = '';
                errorDiv.style.display = 'none';
                // Actualizar contador de comentarios
                const commentBtn = postCard.querySelector('.action-btn[onclick^="toggleComments"]');
                commentBtn.innerHTML = `<i class="bi bi-chat me-1"></i>${parseInt(commentBtn.textContent) + 1} comentarios`;
            } catch (error) {
                console.error(error);
                errorDiv.textContent = 'Error al enviar el comentario.';
                errorDiv.style.display = 'block';
            }
        }

        // Enviar nueva publicación
        document.getElementById('submitPostBtn').addEventListener('click', async () => {
            const form = document.getElementById('newPostForm');
            const titulo = form.titulo.value.trim();
            const contenido = form.contenido.value.trim();
            const etiquetas = form.etiquetas.value.trim();
            const errorDiv = document.getElementById('postError');

            if (!titulo || !contenido) {
                errorDiv.textContent = 'El título y contenido son obligatorios.';
                errorDiv.style.display = 'block';
                return;
            }

            // Validación de palabras restringidas (ejemplo simple)
            const palabrasRestringidas = ['feo', 'xd'];
            const textoParaValidar = titulo.toLowerCase() + ' ' + contenido.toLowerCase();
            for (const palabra of palabrasRestringidas) {
                if (textoParaValidar.includes(palabra)) {
                    errorDiv.textContent = 'El texto contiene palabras no permitidas.';
                    errorDiv.style.display = 'block';
                    return;
                }
            }

            errorDiv.style.display = 'none';

            try {
                const response = await fetch('/api/publicaciones', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify({ titulo, contenido, etiquetas })
                });

                const text = await response.text();

                if (!response.ok) {
                    try {
                        const errorData = JSON.parse(text);
                        if (response.status >= 400) {
                            errorDiv.textContent = errorData.message || 'Error al crear la publicación.';
                            errorDiv.style.display = 'block';
                            return;
                        }
                    } catch (e) {
                        errorDiv.textContent = 'Error inesperado al crear la publicación.';
                        errorDiv.style.display = 'block';
                        return;
                    }
                }

                // Si el cuerpo está vacío
                if (!text) {
                    errorDiv.textContent = 'Publicación creada, pero no se pudo obtener respuesta.';
                    errorDiv.style.display = 'block';
                    return;
                }

                // Intentar parsear la respuesta
                let nuevaPublicacion;
                try {
                    nuevaPublicacion = JSON.parse(text);
                } catch (parseErr) {
                    errorDiv.textContent = 'Publicación creada, pero no se pudo interpretar la respuesta.';
                    errorDiv.style.display = 'block';
                    return;
                }

                document.getElementById('publicacionesContainer').insertAdjacentHTML('afterbegin', renderPost(nuevaPublicacion));
                var modal = bootstrap.Modal.getInstance(document.getElementById('newPostModal'));
                modal.hide();
                form.reset();

            } catch (error) {
                console.error(error);
                errorDiv.textContent = 'Error al crear la publicación.';
                errorDiv.style.display = 'block';
            }
        });

        // Filtros
        document.querySelectorAll('.filter-chip').forEach(chip => {
            chip.addEventListener('click', () => {
                document.querySelectorAll('.filter-chip').forEach(c => c.classList.remove('active'));
                chip.classList.add('active');
                currentFilter = chip.getAttribute('data-filter');
                loadPostsForPage(0);  // <-- usa esta función
            });
        });

        // Búsqueda
        document.getElementById('searchInput').addEventListener('input', (e) => {
            currentSearch = e.target.value;
            loadPosts(true);
        });

        // Función para renderizar paginación
        function renderPagination(totalPages, currentPage) {
            const pagination = document.getElementById('pagination');
            if (!pagination) return;

            // 🔁 Oculta si solo hay una página
            if (totalPages <= 1) {
                pagination.innerHTML = '';
                return;
            }

            pagination.innerHTML = '';

            // Botón Anterior
            const prevLi = document.createElement('li');
            prevLi.className = 'page-item' + (currentPage === 0 ? ' disabled' : '');
            const prevLink = document.createElement('a');
            prevLink.className = 'page-link';
            prevLink.href = '#';
            prevLink.textContent = 'Anterior';
            prevLink.addEventListener('click', (e) => {
                e.preventDefault();
                if (currentPage > 0) {
                    loadPostsForPage(currentPage - 1);
                }
            });
            prevLi.appendChild(prevLink);
            pagination.appendChild(prevLi);

            // Botones numéricos
            for (let i = 0; i < totalPages; i++) {
                const li = document.createElement('li');
                li.className = 'page-item' + (i === currentPage ? ' active' : '');
                const link = document.createElement('a');
                link.className = 'page-link';
                link.href = '#';
                link.textContent = (i + 1).toString();
                link.addEventListener('click', (e) => {
                    e.preventDefault();
                    loadPostsForPage(i);
                });
                li.appendChild(link);
                pagination.appendChild(li);
            }

            // Botón Siguiente
            const nextLi = document.createElement('li');
            nextLi.className = 'page-item' + (currentPage === totalPages - 1 ? ' disabled' : '');
            const nextLink = document.createElement('a');
            nextLink.className = 'page-link';
            nextLink.href = '#';
            nextLink.textContent = 'Siguiente';
            nextLink.addEventListener('click', (e) => {
                e.preventDefault();
                if (currentPage < totalPages - 1) {
                    loadPostsForPage(currentPage + 1);
                }
            });
            nextLi.appendChild(nextLink);
            pagination.appendChild(nextLi);
        }

                // Función para cargar publicaciones para una página específica
                async function loadPostsForPage(page) {
                    if (loading) return;
                    loading = true;
                    currentPage = page;

                    const container = document.getElementById('publicacionesContainer');
                    container.innerHTML = '';

                    let url = `/api/publicaciones?page=${currentPage}&size=${pageSize}`;

                    // 🔁 Filtros por tipo
                    if (currentFilter === 'popular') {
                        url += '&sort=popular';
                    } else if (currentFilter === 'recent') {
                        url += '&sort=recent';
                    } else if (currentFilter === 'mine') {
                        // Solo incluir usuarioId si es "mis publicaciones"
                        if (window.currentUser && window.currentUser.id) {
                            url += `&usuarioId=${window.currentUser.id}`;
                        }
                    }

                    // 🔍 Búsqueda por texto
                    if (currentSearch) {
                        url += `&search=${encodeURIComponent(currentSearch)}`;
                    }

                    try {
                        const response = await fetch(url);
                        if (!response.ok) throw new Error('Error al cargar publicaciones');
                        const data = await response.json();

                        // Renderizar publicaciones
                        data.content.forEach(post => {
                            container.insertAdjacentHTML('beforeend', renderPost(post));
                        });

                        // Renderizar paginación si hay más de una página
                        renderPagination(data.totalPages, currentPage);
                    } catch (error) {
                        console.error(error);
                    } finally {
                        loading = false;
                    }
                }

            // Cargar publicaciones iniciales con paginación
            loadCurrentUser();
            loadPostsForPage(0);

            // Eliminar div postsContainer para evitar confusión
            const postsContainer = document.getElementById('postsContainer');
            if (postsContainer) {
                postsContainer.remove();
            }

        // Event listener para el formulario de edición
        document.getElementById('submitEditBtn').addEventListener('click', async () => {
            const form = document.getElementById('editPostForm');
            const postId = document.getElementById('editPostId').value;
            const titulo = form.titulo.value.trim();
            const contenido = form.contenido.value.trim();
            const errorDiv = document.getElementById('editPostError');

            if (!titulo || !contenido) {
                errorDiv.textContent = 'El título y contenido son obligatorios.';
                errorDiv.style.display = 'block';
                return;
            }

            errorDiv.style.display = 'none';

            try {
                // Obtener el ID del usuario actual
                const userResponse = await fetch('/api/usuario/autenticado');
                if (!userResponse.ok) throw new Error('Error al obtener usuario');
                const userData = await userResponse.json();

                const response = await fetch(`/api/publicaciones/${postId}`, {
                    method: 'PUT',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify({
                        id: postId,
                        usuarioId: userData.id,
                        titulo,
                        contenido
                    })
                });

                if (!response.ok) {
                    const errorData = await response.json();
                    errorDiv.textContent = errorData.message || 'Error al actualizar la publicación.';
                    errorDiv.style.display = 'block';
                    return;
                }

                const publicacionActualizada = await response.json();
                
                // Actualizar la publicación en el DOM
                const postElement = document.querySelector(`.post-card[data-post-id="${postId}"]`);
                if (postElement) {
                    postElement.outerHTML = renderPost(publicacionActualizada);
                }

                // Cerrar el modal
                var modal = bootstrap.Modal.getInstance(document.getElementById('editPostModal'));
                modal.hide();
            } catch (error) {
                console.error(error);
                errorDiv.textContent = 'Error al actualizar la publicación.';
                errorDiv.style.display = 'block';
            }
        });

        // Event listeners para los botones de editar y eliminar
        document.addEventListener('click', function(e) {
            if (e.target.classList.contains('edit-post')) {
                e.preventDefault();
                const postId = e.target.getAttribute('data-post-id');
                editPost(postId);
            } else if (e.target.classList.contains('delete-post')) {
                e.preventDefault();
                const postId = e.target.getAttribute('data-post-id');
                deletePost(postId);
            }
        });

        function mostrarModalAccesoDenegado() {
            const modal = new bootstrap.Modal(document.getElementById('modalAccesoDenegado'));
            modal.show();
        }

