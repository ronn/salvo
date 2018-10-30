window.onload = () => {
    document.getElementById("table-headers").innerHTML = getHeaders()
    document.getElementById("table-rows").innerHTML = getRows()

    fetch("http://localhost:8080/api/game_view/" + gamePlayerId)
        .then(response => response.json())
        .then(gameView => {
            showInfo(gameView)
            changeBackground(gameView.ships)
        })
        .catch(err => alert("Ocurrió un error: " + err))
}

const gamePlayerId = new URLSearchParams(window.location.search).get("gp")

const showInfo = gameview => gameview.gamePlayers
    .forEach(gp =>
         document.getElementById(
             gp.id.toString() === gamePlayerId ? "viewer" : "player"
         ).innerText = gp.player.email
    )

const changeBackground = ships => ships.forEach(s =>
    s.locations.forEach(loc => document.getElementsByClassName(loc)[0].style.backgroundColor="RED")
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

