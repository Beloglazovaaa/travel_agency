document.addEventListener('DOMContentLoaded', function() {
    const myCarouselElement = document.querySelector('#carouselExampleIndicators');

    const carousel = new bootstrap.Carousel(myCarouselElement, {
        interval: 5000,
        touch: false
    });
});
