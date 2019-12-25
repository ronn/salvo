window.onload = () => {
    getElementsByClass(document, "table-headers")
        .forEach(value => value.innerHTML = buildHeaders())

    getElementsByClass(document, "table-rows")
        .forEach(value => value.innerHTML = buildRows())

    fetch("http://localhost:8080/api/game_view/" + getGPId)
        .then(response => response.json())
        .then(gameView => {
            showInfo(gameView.gamePlayers)
            changeBackground(gameView.ships)
            buildButtons(gameView.ships)
            changeBackgroundSalvoes(gameView.salvoes, gameView.gamePlayers)
            paintShipHit(gameView)
            canShootSalvos(gameView)
        })
        .catch(err => console.log("Ocurrió un error: " + err))

    hideElements(["salvoes-table", "place-ships", "fire-salvo"])
}

const hideElements = elementIds1 => elementIds1.forEach(id => constChangeVisibility(id, "hidden"))
const showElements = elementIds2 => elementIds2.forEach(id => constChangeVisibility(id, "visible"))

const constChangeVisibility = (elementId, visibility) => document.getElementById(elementId).style.visibility = visibility

const canShootSalvos = game => {
    if (game.ships.length > 0){
        if (game.oponent){
            if (game.oponent.hasPlacedShips){
                document.getElementById("fire-spot")
                    .innerHTML = showSalvoesTable()
            } else {
                document.getElementById("fire-spot")
                    .innerHTML = "Waiting for oponent's Ships"
                reloadWithTimeOut()
            }
        } else {
            document.getElementById("fire-spot")
                .innerHTML = "Waiting for an oponent"
            reloadWithTimeOut()
        }
    }
}

const reloadWithTimeOut = () => setTimeout(() => window.location.reload(), 8000)

const showSalvoesTable = () => {
    showElements(["salvoes-table"])
    setHoverSalvo()
    return "Shoot your salvoes!"
}

const getGPId = new URLSearchParams(window.location.search).get("gp")

const changeBackgroundSalvoes = (salvoes, gamePlayers) =>
    gamePlayers.filter(gp => gp.id.toString() !== getGPId)
        .forEach(gp => salvoes
                .filter(s => s.player !== gp.id)
                .forEach(s => s.locations
                    .forEach(loc => {
                        document.getElementsByClassName(loc)[1].style.backgroundColor = "GREEN"
                        document.getElementsByClassName(loc)[1].innerHTML = "<span style='color: white; font-weight: bold'>" + s.turn + "</pan>"
                    })
                )
        )

const showInfo = gamePlayers =>
    gamePlayers.forEach(gp =>
        document.getElementById(
             gp.id.toString() === getGPId ? "viewer" : "player"
         ).innerText = gp.player.email
    )

const changeBackground = ships => ships.forEach(s =>
    s.locations.forEach(loc => document.getElementsByClassName(loc)[0].style.backgroundColor="BLUE")
)

const paintShipHit = game => game.ships
    .filter(ship => {
        const re = game.gamePlayers.filter(gp => gp.player.id !== ship.player)
        return ship.player !== re[0]
    })
    .forEach(ship => game.salvoes
        .filter(salvo => salvo.player.toString() !== getGPId)
        .forEach(salvo => ship.locations
            .forEach(shipLoc => salvo.locations
                .filter(salvoLoc => salvoLoc === shipLoc)
                .forEach(salvoHit => {
                    document.getElementsByClassName(salvoHit)[0].style.backgroundColor="RED"
                    document.getElementsByClassName(salvoHit)[0].innerHTML="<span style='color: white; font-weight: bold'>" + salvo.turn + "</span>"
                })
            )
        )
    )

const getCols = () => ["A", "B","C", "D", "E", "F", "G", "H", "I", "J"]
const getRows = () => [1, 2, 3, 4, 5, 6, 7, 8, 9, 10]

const buildHeaders = () =>
    "<tr><th></th>" + getCols()
        .map(col =>"<th width='80px'>" + col + "</th>")
        .join("")
    + "</tr>"

const buildRows = () =>
    getRows().map(row => "<tr><td width='80px' height='40px'>" + row + "</td>"
        + getCols().map(col => "<td width='80px' class='hova " + col + row + "'></td>")
            .join("")
        + "</tr>"
    ).join("")

const getElementsByClass = (element, className) => Array.from(element.getElementsByClassName(className))

const setNotOverAndOut = cell => {
    cell.onmouseout = null;
    cell.onmouseover = null;
}

const buildButtons = playerShips => {
    if (playerShips.length > 0) {
        hideElements(["ship-butons", "orientation-choose"])
    }
}

const getOrientation = () => document.querySelector('input[name="orientation"]:checked').value