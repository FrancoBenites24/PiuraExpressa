// Main JavaScript file for PiuraExpressa
$(document).ready(function() {
    // Initialize tooltips
    var tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'));
    var tooltipList = tooltipTriggerList.map(function (tooltipTriggerEl) {
        return new bootstrap.Tooltip(tooltipTriggerEl);
    });

    // Initialize popovers
    var popoverTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="popover"]'));
    var popoverList = popoverTriggerList.map(function (popoverTriggerEl) {
        return new bootstrap.Popover(popoverTriggerEl);
    });

    // Smooth scrolling for anchor links
    $('a[href*="#"]:not([href="#"])').click(function() {
        if (location.pathname.replace(/^\//, '') == this.pathname.replace(/^\//, '') && location.hostname == this.hostname) {
            var target = $(this.hash);
            target = target.length ? target : $('[name=' + this.hash.slice(1) + ']');
            if (target.length) {
                $('html, body').animate({
                    scrollTop: target.offset().top - 70
                }, 1000);
                return false;
            }
        }
    });

    // Add fade-in animation to cards
    $('.card').addClass('fade-in');

    // Loading spinner utility
    window.showLoader = function() {
        $('body').append('<div id="loader-overlay" class="position-fixed top-0 start-0 w-100 h-100 d-flex justify-content-center align-items-center" style="background: rgba(0,0,0,0.5); z-index: 9999;"><div class="loader"></div></div>');
    };

    window.hideLoader = function() {
        $('#loader-overlay').remove();
    };
});

// Utility functions
function loadFeaturedEvents() {
    // Placeholder for loading featured events
    console.log('Loading featured events...');
}

function loadPopularDishes() {
    // Placeholder for loading popular dishes
    console.log('Loading popular dishes...');
}

function loadRecentPosts() {
    // Placeholder for loading recent posts
    console.log('Loading recent posts...');
}

function loadStatistics() {
    // Placeholder for loading statistics
    console.log('Loading statistics...');
}

// Client-side filtering for posts
function filterPostsByUser(userId) {
    $('.card').each(function() {
        var postUserId = $(this).data('user-id'); // Assuming each post card has data-user-id attribute
        if (!userId || postUserId == userId) {
            $(this).show();
        } else {
            $(this).hide();
        }
    });
}

// Example: Event listener for filter dropdown or buttons
$(document).ready(function() {
    $('#filterUserSelect').on('change', function() {
        var selectedUserId = $(this).val();
        filterPostsByUser(selectedUserId);
    });
});
