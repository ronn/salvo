window.onload = () => {
    Array.from(document.getElementsByClassName("table-headers"))
        .forEach(value => value.innerHTML = getHeaders())

    Array.from(document.getElementsByClassName("table-rows"))
        .forEach(value => value.innerHTML = getRows())

    fetch("http://localhost:8080/api/game_view/" + getGPId)
        .then(response => response.json())
        .then(gameView => {
            showInfo(gameView.gamePlayers)
            changeBackground(gameView.ships)
            changeBackgroundSalvoes(gameView.salvoes)
            paintShipHit(gameView)
        })
        .catch(err => console.log("Ocurrió un error: " + err))
}

const getGPId = new URLSearchParams(window.location.search).get("gp")

const loggear = dato => console.log(JSON.stringify(dato))

const changeBackgroundSalvoes = salvoes => salvoes
    .filter(s => s.player.toString() === getGPId)
    .forEach(s => s.locations
        .forEach(loc => {
            document.getElementsByClassName(loc)[1].style.backgroundColor="GREEN"
            document.getElementsByClassName(loc)[1].innerHTML="<span style='color: white; font-weight: bold'>" + s.turn + "</pan>"
        })
    )

const showInfo = gamePlayers => gamePlayers
    .forEach(gp =>
         document.getElementById(
             gp.id.toString() === getGPId ? "viewer" : "player"
         ).innerText = gp.player.email
    )

const changeBackground = ships => ships.forEach(s =>
    s.locations.forEach(loc => document.getElementsByClassName(loc)[0].style.backgroundColor="BLUE")
)

const paintShipHit = gamePlayer => gamePlayer.ships
    .forEach(ship => gamePlayer.salvoes
        .forEach(salvo => ship.locations
            .forEach(location => salvo.locations
                .filter(loc => loc === location)
                .forEach(salvoHit => {
                    document.getElementsByClassName(salvoHit)[0].style.backgroundColor="RED"
                    document.getElementsByClassName(salvoHit)[0].innerHTML="<span style='color: white; font-weight: bold'>" + salvo.turn + "</span>"
                })
            )
        )
    )

const cols = ["A", "B","C", "D", "E", "F", "G", "H", "I", "J"]
const rows = ["1", "2", "3", "4", "5", "6", "7", "8", "9", "10"]

const getHeaders = () =>
    "<tr><th></th>" + cols
        .map(col =>"<th>" + col + "</th>")
        .join("")
    + "</tr>"

const getRows = () =>
    rows.map(row => "<tr><td>" + row + "</td>"
        + cols.map(col => "<td class=" + col + row + "></td>")
            .join("")
        + "</tr>"
    ).join("")

const placeShips = () => fetch(`http://localhost:8080/api/games/players/${getGPId}/ships`, {
    method: 'POST',
    credentials: 'include',
    headers: {
        'Accept': 'application/json',
        'Content-Type': 'application/json',
    },
    body: buildShipsToPlace()
}).then(response => response.json())
    .then(json => {
        if ("CREATED" === json.status){
            window.location.reload()
        }else {
            alert(JSON.stringify(json.msj))
        }
    })
    .catch(error => console.log("An error has ocurred: ", error))

const buildShipsToPlace = () => "[ { \"type\": \"Destroyer\", \"locations\": [\"A1\", \"B1\", \"C1\"] },\n" +
"  { \"type\": \"PatrolBoat\", \"locations\": [\"H5\", \"H6\"] }\n" +
"]"