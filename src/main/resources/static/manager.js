$(function() {

    // display text in the output area
    function showOutput(text) {
        $("#output").text(text);
    }

    // load and display JSON sent by server for /players

    function loadData() {
        $.get("/players")
            .done(function(data) {
                showOutput(JSON.stringify(data, null, 2));
            })
            .fail(function( jqXHR, textStatus ) {
                showOutput( "Failed: " + textStatus );
            });
    }

    // handler for when user clicks add person

    function addPlayer() {
        var name = $("#email").val();
        if (name) {
            postPlayer(name);
        }
    }

    // code to post a new player using AJAX
    // on success, reload and display the updated data from the server

    function postPlayer(userName) {
        $.post({
            headers: {
                'Content-Type': 'application/json'
            },
            dataType: "text",
            url: "/players",
            data: JSON.stringify({ "userName": userName })
        })
            .done(function( ) {
                showOutput( "Saved -- reloading");
                loadData();
            })
            .fail(function( jqXHR, textStatus ) {
                showOutput( "Failed: " + textStatus );
            });
    }

    $("#add_player").on("click", addPlayer);

    loadData();
});


fetch('http://localhost:8080/api/login', {
    method: 'POST',
    credentials: 'include',
    headers: {
        'Accept': 'application/json',
        'Content-Type': 'application/x-www-form-urlencoded',
    },
    body: "username=j.bauer@ctu.gov&password=24"
}).then(response => {
        if (response.status === 200) {
            console.log("Logged In!!");
        } else if (response.status === 401) {
            console.log("Can't login: " + response.status);
        } else {
            console.log("A problem has occurred: " + response.status);
        }
    }).catch(error => console.log("An error has ocurred: ", error))