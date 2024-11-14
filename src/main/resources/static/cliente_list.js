document.addEventListener("DOMContentLoaded", function() {
    document.getElementById("buscador").focus();
});


function buscarCliente() {
    // Obtén el valor del campo de búsqueda
    const filtro = document.getElementById("buscador").value.toUpperCase();
    const tabla = document.getElementById("tablaClientes");
    const filas = tabla.getElementsByTagName("tr");

    // Recorre todas las filas de la tabla (excepto la primera, que es el encabezado)
    for (let i = 1; i < filas.length; i++) {
        const celdas = filas[i].getElementsByTagName("td");
        let encontrado = false;

        // Recorre cada celda de la fila
        for (let j = 0; j < celdas.length; j++) {
            const textoCelda = celdas[j].textContent || celdas[j].innerText;
            if (textoCelda.toUpperCase().indexOf(filtro) > -1) {
                encontrado = true;
                break;
            }
        }

        // Muestra la fila si se encontró una coincidencia; de lo contrario, ocúltala
        filas[i].style.display = encontrado ? "" : "none";
    }
}


