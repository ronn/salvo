const ships = {
    AircraftCarrier: {
        type: "AircraftCarrier",
        length: 5
    },
    Battleship: {
        type: "Battleship",
        length: 4
    },
    Submarine: {
        type: "Submarine",
        length: 3
    },
    Destroyer: {
        type: "Destroyer",
        length: 3
    },
    PatrolBoat: {
        type: "PatrolBoat",
        length: 2
    }
}

class Ship {
    constructor(type, locations){
        this.type = type
        this.locations = locations
    }
}

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

const setCellBehavior = (cell, color, onClickFunc) => {
    if (cell.classList.contains("hova")) {
        cell.style.backgroundColor = color
        cell.onclick = onClickFunc
    }
}

const changeBackGroundColor = (cell, color) => setCellBehavior(cell, color, null)

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

    hideElements([shipType.toLowerCase()])

    stopOnClickBehavior();
    if (shipsToPlace.length > 4){
        showElements(["place-ships"])
        hideElements(["orientation-choose"])
    }
}

const stopOnClickBehavior = () => getElementsByClass(getShipsGrid(), "hova")
    .forEach(value => setNotOverAndOut(value))

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










const barcos = [
    {
        type: "Submarina",
        locations: ["A1", "B2", "C3"]
    }
]
JSON.stringify()


















