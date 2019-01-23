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
            changeBackgroundSalvoes(gameView.salvoes)
            paintShipHit(gameView)
        })
        .catch(err => console.log("Ocurrió un error: " + err))

    document.getElementById("place-ships").style.visibility = "hidden"
}

const getGPId = new URLSearchParams(window.location.search).get("gp")

const changeBackgroundSalvoes = salvoes => salvoes
    .filter(s => s.player.toString() === getGPId)
    .forEach(s => s.locations
        .forEach(loc => {
            document.getElementsByClassName(loc)[1].style.backgroundColor="GREEN"
            document.getElementsByClassName(loc)[1].innerHTML="<span style='color: white; font-weight: bold'>" + s.turn + "</pan>"
        })
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

const getCols = () => ["A", "B","C", "D", "E", "F", "G", "H", "I", "J"]
const getRows = () => [1, 2, 3, 4, 5, 6, 7, 8, 9, 10]

const buildHeaders = () =>
    "<tr><th></th>" + getCols()
        .map(col =>"<th width='80px'>" + col + "</th>")
        .join("")
    + "</tr>"

const buildRows = () =>
    getRows().map(row => "<tr><td height='40px'>" + row + "</td>"
        + getCols().map(col => "<td width='80px' class='hova " + col + row + "'></td>")
            .join("")
        + "</tr>"
    ).join("")

const placeShipsInServer = () => fetch(`http://localhost:8080/api/games/players/${getGPId}/ships`, {
    method: 'POST',
    credentials: 'include',
    headers: {
        'Accept': 'application/json',
        'Content-Type': 'application/json',
    },
    body: JSON.stringify(shipsToPlace)
}).then(response => response.json())
    .then(json => {
        if ("CREATED" === json.status){
            window.location.reload()
        }else {
            alert(JSON.stringify(json.msj))
        }
    })
    .catch(error => console.log("An error has ocurred: ", error))

const getElementsByClass = (element, className) => Array.from(element.getElementsByClassName(className))

const setHover = ship => getElementsByClass(getShipsGrid(), "hova")
    .forEach(value => {
        value.onmouseover = () => getMouseBehavior(ship, value, "over")
        value.onmouseout = () => getMouseBehavior(ship, value, "out")
    })

const getSelectedLocations = shipLocs => Array.from(getShipsGrid().querySelectorAll("." + shipLocs.join(", .")))

const getMouseBehavior = (ship, value, behavior) => {
    const col = value.className.substr(4, 5).substr(1, 1)
    const row = Number(value.className.substr(6, 8))

    const hori = getCols().indexOf(col)

    const endShip = "vertical" === getOrientation()
        ? (row + (ship.length - 1))
        : (getCols()[hori + ship.length - 1])

    const shipLocs = "vertical" === getOrientation()
        ? getRows().slice(row -1, endShip).map(n => col + n)
        : getCols().slice(hori, hori + ship.length).map(n => n + row)

    getSelectedLocations(shipLocs)
        .forEach(cell =>
            "over" === behavior ? suggestLocations(cell, endShip, shipLocs, ship.type) : changeBackGroundColor(cell, "WHITE"))
}

const suggestLocations = (cell, endShip, shipLocs, shipType) => {
    const letterIndex = endShip !== undefined ? getCols().indexOf(endShip) : 11
    const lastCell = "vertical" === getOrientation() ? endShip : letterIndex

    10 >= lastCell
        ? (
            isOverALocatedShip(shipLocs)
                ? changeBackGroundColor(cell, "RED")
                : setCellBehavior(cell, "BLACK", placeAShip(shipType, shipLocs))
        )
        : changeBackGroundColor(cell, "RED")
}

const changeBackGroundColor = (cell, color) => {
    if (cell.classList.contains("hova")){
        setCellBehavior(cell, color, null)
    }
}

const setCellBehavior = (cell, color, onClickFunc) => {
    cell.style.backgroundColor = color
    cell.onclick = onClickFunc
}

const isOverALocatedShip = locs => shipsToPlace
    .map(ship => ship.locations)
    .flatMap(shipLocs => shipLocs
        .flatMap(shipLoc => locs
            .filter(loc => loc === shipLoc)
        )
    ).length > 0

const shipsToPlace = []

const getShipsGrid = () => document.getElementById("ships-grid")

const placeAShip = (shipType, shipLocs) => () => {
    const shipToPlace = new Ship(shipType, shipLocs)
    shipsToPlace.push(shipToPlace)

    getSelectedLocations(shipLocs)
        .forEach(cell => {
            setCellBehavior(cell, "BLACK", null)
            setNotOverAndOut(cell);
            cell.classList.remove("hova")
        })

    document.getElementById(shipType.toLowerCase()).style.visibility = "hidden"

    stopOnClickBehavior();
    if (shipsToPlace.length > 4){
        document.getElementById("place-ships").style.visibility = "visible"
        document.getElementById("orientation-choose").style.visibility = "hidden"
    }
}

const stopOnClickBehavior = () => getElementsByClass(getShipsGrid(), "hova")
    .forEach(value => setNotOverAndOut(value))

const setNotOverAndOut = cell => {
    cell.onmouseout = null;
    cell.onmouseover = null;
}

const buildButtons = playerShips => {
    if (playerShips.length > 0) {
        document.getElementById("ship-butons").style.visibility = "hidden"
        document.getElementById("orientation-choose").style.visibility = "hidden"
    }
}

const getOrientation = () => document.querySelector('input[name="orientation"]:checked').value


- Ejército de Liberación Nacional (ELN)
- Fuerzas Armadas Revolucionarias de Colombia (FARC)
- Movimiento 19 de abril (M19)
- Ejército de Popular de Liberación (EPL)
- Ejército Revolucionario del Pueblo (ERP)
- Movimiento Armado Quintín Lame (MAQL)
- Movimiento Jaime Bateman Cayón (Bateman Cayon)
- Ejército Revolucionario Guevarista (ERG)