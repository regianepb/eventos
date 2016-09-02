$(function(){
    $.get('menu.html', function(data) {
        $('#topo').html(data);
    });
});