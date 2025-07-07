document.addEventListener('DOMContentLoaded', () => {
  const mainContent = document.querySelector('.main-content');

  if (mainContent) {
    mainContent.addEventListener('mousemove', (e) => {
      const rect = mainContent.getBoundingClientRect();
      const x = e.clientX - rect.left; // x position within the element.
      const y = e.clientY - rect.top;  // y position within the element.

      const centerX = rect.width / 2;
      const centerY = rect.height / 2;

      const rotateX = ((y - centerY) / centerY) * 5; // max 5 degrees rotation
      const rotateY = ((x - centerX) / centerX) * 5;

      mainContent.style.transform = `rotateX(${-rotateX}deg) rotateY(${rotateY}deg)`;
    });

    mainContent.addEventListener('mouseleave', () => {
      mainContent.style.transform = 'rotateX(0deg) rotateY(0deg)';
    });
  }
});
