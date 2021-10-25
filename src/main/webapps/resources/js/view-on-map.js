$(document).ready(function() {
    var div = document.getElementById("view-on-map");
    var results = {productUrl: 'http://localhost:8080/chiador/chios/mapa'};
    document.getElementById("view-on-map").addEventListener('click', function() {
        url = results.productUrl + "/buscar?query=" + $('#query-input').val() + '&user=' + $('#user-input').val();
        window.open( url, '_blank');
    });

    document.getElementById("show-tweet-map").addEventListener('click', function() {
            url = results.productUrl;
            window.open( url, '_blank' );
        });


});