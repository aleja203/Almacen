function convertirFecha(fechaStr) {
    var partes = fechaStr.split('/');
    // Asegurarse de que la fecha tiene el formato correcto (dd/mm/yyyy)
    if (partes.length === 3) {
        // Crear un objeto Date usando las partes: año, mes (restar 1 porque los meses empiezan en 0 en JS), día
        return new Date(partes[2], partes[1] - 1, partes[0]);
    }
    return null; // Retornar null si la fecha no es válida
}

function aplicarFiltros() {
    var dniCliente = document.getElementById('clienteSelect').value; // Obtener el id del cliente seleccionado
    var codigoProducto = document.getElementById('productoSelect').value; // Obtener el código del producto seleccionado
    var fechaDesde = document.getElementById('fechaDesde').value; // Obtener la fecha desde seleccionada
    var fechaHasta = document.getElementById('fechaHasta').value; // Obtener la fecha hasta seleccionada

    // Convertir las fechas seleccionadas a objetos Date para facilitar la comparación
    var fechaDesdeDate = fechaDesde ? new Date(fechaDesde + "T00:00:00") : null;
    var fechaHastaDate = fechaHasta ? new Date(fechaHasta + "T23:59:59") : null;

    // Obtener todas las filas de la tabla
    var rows = document.querySelectorAll('table tbody tr');

    rows.forEach(row => {
        var clienteDni = row.getAttribute('data-id'); // Obtener el id del cliente en esa fila
        var productoCodigo = row.getAttribute('data-producto'); // Obtener el código del producto en esa fila
        var fechaVentaStr = row.cells[0].innerText; // Obtener la fecha de la compra en esa fila (suponiendo que la fecha está en la primera columna)
        var fechaVenta = convertirFecha(fechaVentaStr); // Convertir la fecha de la fila a un objeto Date

        // Mostrar la fila si el id del proveedor coincide, el código del producto coincide, o si no se ha seleccionado ningún proveedor/producto
        var clienteCoincide = (dniCliente === "" || dniCliente === clienteDni);
        var productoCoincide = (codigoProducto === "" || codigoProducto === productoCodigo);
        var fechaCoincide = true; // Por defecto, las filas coinciden si no se han seleccionado fechas

        // Si se selecciona una fecha desde, comprobar que la fecha de la compra sea mayor o igual
        if (fechaDesdeDate && fechaVenta) {
            fechaCoincide = fechaCoincide && (fechaVenta >= fechaDesdeDate);
        }
        // Si se selecciona una fecha hasta, comprobar que la fecha de la compra sea menor o igual
        if (fechaHastaDate && fechaVenta) {
            fechaCoincide = fechaCoincide && (fechaVenta <= fechaHastaDate);
        }

        // Mostrar u ocultar la fila según todos los filtros
        if (clienteCoincide && productoCoincide && fechaCoincide) {
            row.style.display = ""; // Mostrar la fila
        } else {
            row.style.display = "none"; // Ocultar la fila
        }
    });
}

// Escuchar los cambios en los selectores de cliente, producto y fechas
document.getElementById('clienteSelect').addEventListener('change', aplicarFiltros);
document.getElementById('productoSelect').addEventListener('change', aplicarFiltros);
document.getElementById('fechaDesde').addEventListener('change', aplicarFiltros);
document.getElementById('fechaHasta').addEventListener('change', aplicarFiltros);
