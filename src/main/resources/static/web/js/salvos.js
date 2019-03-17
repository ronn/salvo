class Salvo {
    constructor(locations){
        this.locations = locations
    }
}

const setHoverSalvo = () => getElementsByClass(getSalvoesGrid(), "hova")
    .forEach(shoot => {
        shoot.onmouseover = () => setCellBehavior(shoot, "YELLOW", addShoot(shoot))
        shoot.onmouseout = () => changeBackGroundColor(shoot, "WHITE")
    })

const addShoot = shoot => () => {
    changeBackGroundColor(shoot, "ORANGE")
    shoot.classList.remove("hova")
    salvoLocations.push(shoot.className)
    checkSalvoLocsSize()
}

const checkSalvoLocsSize = () => {
    if (salvoLocations.length > 4){
        getElementsByClass(getSalvoesGrid(), "hova")
            .forEach(shoot => shoot.onmouseover = null)
        showElements(["fire-salvo"])
    }
}

const shootSalvo = () => {
    const salvoToShoot = new Salvo(salvoLocations)
    console.log("Disparando desde aquí, putos!", JSON.stringify(salvoToShoot))
    placeSalvoInServer(salvoToShoot)
}

const salvoLocations = []

const getSalvoesGrid = () => document.getElementById("salvoes-grid")

const placeSalvoInServer = salvo => fetch(`http://localhost:8080/api/games/players/${getGPId}/salvos`, {
    method: 'POST',
    credentials: 'include',
    headers: {
        'Accept': 'application/json',
        'Content-Type': 'application/json',
    },
    body: JSON.stringify(salvo)
}).then(response => response.json())
    .then(json => {
        if ("CREATED" === json.status){
            window.location.reload()
        }else {
            alert(JSON.stringify(json.msj))
        }
    })
    .catch(error => console.log("An error has ocurred: ", error))