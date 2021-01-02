

$( document ).ready(function() {

    console.log( "ready!" );

    $('form').submit(function(ev) {


        ev.preventDefault();

        var form = $(this);
    
        $.ajax({
                type: "POST",
                url: form.attr('action'),
                data: form.serialize(),
                headers : {
                    Authorization : 'Bearer ' + 'limited-guest-access'
                },
                success : function(response) {
                    console.log(response);

                    $('#result').empty();



                },
                error : function(xhr, status, error) {
                    var err = eval("(" + xhr.responseText + ")");
                    console.log(err);                   
                }
        }); 

    });


});