map.on('moveend', function() {
    var xhr = new XMLHttpRequest();
    xhr.open('GET', '/chiador/chios/mapa/refrescar', false);

    xhr.onload = function () {
        // Request finished. Do processing here.
    };

    xhr.send(map.getBounds().toJSON());
});