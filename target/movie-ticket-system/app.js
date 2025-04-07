
document.addEventListener('DOMContentLoaded', function() {
    const menuToggle = document.createElement('button');
    menuToggle.innerHTML = '☰';
    menuToggle.className = 'menu-toggle';
    menuToggle.style.display = 'none';
    
    document.querySelector('header').appendChild(menuToggle);
    
    function checkScreenSize() {
        if (window.innerWidth < 768) {
            menuToggle.style.display = 'block';
            document.querySelector('nav').style.display = 'none';
        } else {
            menuToggle.style.display = 'none';
            document.querySelector('nav').style.display = 'block';
        }
    }
    
    menuToggle.addEventListener('click', function() {
        const nav = document.querySelector('nav');
        nav.style.display = nav.style.display === 'none' ? 'block' : 'none';
    });
    
    window.addEventListener('resize', checkScreenSize);
    checkScreenSize();
});