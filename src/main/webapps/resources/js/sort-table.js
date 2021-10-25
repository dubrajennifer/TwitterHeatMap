$(function() {
    $("table").tablesorter({
        headers: {
            // disable sorting of the first & second column - before we would have to had made two entries
            // note that "first-name" is a class on the span INSIDE the first column th cell
            '.tweet-content, .tweet-info' : {
                // disable it by setting the property sorter to false
                sorter: false
            }
        }
    });
});