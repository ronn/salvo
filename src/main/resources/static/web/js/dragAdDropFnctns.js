const drag = ev => ev.dataTransfer.setData("text", ev.target.id)

const drop = ev => {
    ev.preventDefault()
    const data = ev.dataTransfer.getData("text");
    //ev.target.style.backgroundColor="YELLOW"
    ev.target.appendChild(document.getElementById(data))
   /* alert(ev.target.className)
    const num = ev.target.className.substr(1, 2)
    const letr = ev.target.className.substr(0,1 )

    alert(letr + (rows[num - 1] + 1))

    Array.from(document.getElementsByClassName(letr + (rows[num - 1] + 1)))
        .forEach(el => {
            el.style.backgroundColor="YELLOW"
        })*/
}