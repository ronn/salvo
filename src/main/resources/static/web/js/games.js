fetch('http://localhost:8080/api/games')
    .then(response => response.json())
    .then(g => createTable(g))
    .catch(error => console.log('There was a problem fetching the games data:' + error.message))

fetch('http://localhost:8080/api/leaderboard')
    .then(response => response.json())
    .then(leaderRanking => createLeaderboard(leaderRanking))
    .catch(error => console.log('There was a problem fetching the leaderboard: ' + error.message))

const createTable = games =>
    games.map(g => `<tr>
                <td> ${g.id}</td>
                <td> ${new Date(g.created)}</td>
                <td>${g.gamePlayers.map(gp => gp.player.email)}</td>
            </tr>`)
        .forEach(row => document.getElementById('tabla').innerHTML += row)

const createLeaderboard = ranking =>
    ranking.sort((a, b) => b.total - a.total)
        .map(r => `<tr>
                <td> ${r.name}</td>
                <td> ${r.total}</td>
                <td> ${r.won}</td>
                <td> ${r.lost}</td>
                <td> ${r.tied}</td>
            </tr>`)
        .forEach(row => document.getElementById('leaderboard-table').innerHTML += row)

const loggear = algo => console.log("Logeando: " + JSON.stringify(algo))