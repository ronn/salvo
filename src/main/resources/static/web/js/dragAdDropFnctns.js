const drag = ev => ev.dataTransfer.setData("text", ev.target.id)

const drop = ev => {
    ev.preventDefault()
    const data = ev.dataTransfer.getData("text");
    ev.target.appendChild(document.getElementById(data));
    alert(ev.target.className)
}