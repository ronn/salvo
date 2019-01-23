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