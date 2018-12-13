document.getElementById("newGameButon").style.visibility = "hidden";

fetch('http://localhost:8080/api/games')
    .then(response => response.json())
    .then(g => {
        showLoginOrLogout(g.player)
        showLoggedPlayer(g.player)
        showNewGameButton(g.player)
        createGamesTable(g)
    })
    .catch(error => console.log('There was a problem fetching the games data:' + error.message))

fetch('http://localhost:8080/api/leaderboard')
    .then(response => response.json())
    .then(leaderRanking => createLeaderboard(leaderRanking))
    .catch(error => console.log('There was a problem fetching the leaderboard: ' + error.message))

const createGamesTable = response =>
    response.games.map(g =>
        `<tr>
            <td> ${g.id}</td>
            <td> ${new Date(g.created)}</td>
            <td>${g.gamePlayers.map(gp => gp.player.email)}</td>
            <td>${getGameLinkCell(g.gamePlayers, response.player)}</td>
            </tr>`
        )
        .forEach(row => document.getElementById('tabla').innerHTML += row)

const getGameLinkCell = (gps, player) => null !== player
    ? getLinksPerGP(gps, player)
    : `<span>You should log in!</span>`

const getLinksPerGP = (gps, player) => {
    const tag = gps
        .filter(gp => gp.player.id === player.id)
        .map(gp => `<a href="game.html?gp=${gp.id}">Play game!</a>`)

    return tag.length > 0 ? tag[0] : `<span>Not playing</span>`
}

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

const showLoginOrLogout = player => document.getElementById("logInOut")
    .innerHTML = player ?
    `<input type="submit" value="Log out" onclick="logout()">`
    : getLoginForm()

const getLoginForm = () => `<h1>LOG IN!!</h1>
            <form onsubmit="return false">
                <label for="email-login">User name</label>
                <input id="email-login" type="email" required autocomplete="username">

                <label for="pass-login">Pass word</label>
                <input id="pass-login" type="password" required autocomplete="current-password">

                <input type="submit" value="Log in" onclick="login()">
                <input type="button" value="Sign up" onclick="signup()">
            </form>`

const showLoggedPlayer = player =>
    document.getElementById("loged-player").innerText =
        player ?  `Player logged in: ${player.email}`
        : ""

const showNewGameButton = player => {
    document.getElementById("newGameButon").style.visibility =
        player ?  "visible" : "hidden"
}

const createNewGame = () => fetch('http://localhost:8080/api/games', {
    method: 'POST',
    credentials: 'include'
}).then(response => response.json())
    .then(gpidJson => window.location.replace("http://localhost:8080/web/game.html?gp=" + gpidJson.gpid))
    .catch(error => alert("Couldn't create a new game: " + error))

const loggear = algo => console.log("Logeando: " + JSON.stringify(algo))