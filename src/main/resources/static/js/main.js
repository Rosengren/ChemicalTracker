$(".clickableContainer").click(function() {
    window.location = $(this).find("a").attr("href"); 
    return false;
});

$(".button").popup({
	variation: 'inverted',
	position: 'top center',

});

// $('.ui.sidebar')
//   .sidebar('toggle');
