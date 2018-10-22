console.log("Qué pasa, putos?!!")

let games = [];
fetch('http://localhost:8080/api/games')
    .then(response => response.json())
    .catch(error => console.log('There was a problem fetching the games data:' + error.message))
    .then(g => games = g)

window.onload = () => {
    games.forEach(g => {
        const row = document.createElement("tr")
        const idCell = document.createElement("td")

        idCell.appendChild(document.createTextNode(g.id))
        row.appendChild(idCell)

        const createdCell = document.createElement("td");
        createdCell.appendChild(document.createTextNode(new Date(Date.parse(g.created)).toDateString()))
        row.appendChild(createdCell)

        const gamePlayersCell = document.createElement("td");
        g.gamePlayers.forEach(gp => {
            gamePlayersCell.appendChild(document.createTextNode(gp.player.emal + ", "))
            row.appendChild(gamePlayersCell)
        })

        document.getElementById("tabla").appendChild(row)
    })
}