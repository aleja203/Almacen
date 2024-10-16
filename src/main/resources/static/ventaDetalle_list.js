//document.getElementById('clienteSelect').addEventListener('change', function () {
//    var dniCliente = this.value; // Obtener el dni del cliente seleccionado
//
//    // Obtener todas las filas de la tabla
//    var rows = document.querySelectorAll('table tbody tr');
//
//    rows.forEach(row => {
//        var clienteDni = row.getAttribute('data-dni'); // Obtener el dni del cliente en esa fila
//
//        // Mostrar la fila solo si el dni del cliente coincide o si no se ha seleccionado ningún cliente
//        if (dniCliente === "" || dniCliente === clienteDni) {
//            row.style.display = ""; // Mostrar la fila
//        } else {
//            row.style.display = "none"; // Ocultar la fila
//        }
//       
//    });
//});

// Función para aplicar los filtros de cliente y producto
function aplicarFiltros() {
    var dniCliente = document.getElementById('clienteSelect').value; // Obtener el dni del cliente seleccionado
    var codigoProducto = document.getElementById('productoSelect').value; // Obtener el código del producto seleccionado
    var fechaDesde = document.getElementById('fechaDesde').value; // Obtener la fecha desde seleccionada
    var fechaHasta = document.getElementById('fechaHasta').value; // Obtener la fecha hasta seleccionada

    // Convertir las fechas seleccionadas a objetos Date para facilitar la comparación
    var fechaDesdeDate = fechaDesde ? new Date(fechaDesde) : null;
    var fechaHastaDate = fechaHasta ? new Date(fechaHasta) : null;

    // Obtener todas las filas de la tabla
    var rows = document.querySelectorAll('table tbody tr');

    rows.forEach(row => {
        var clienteDni = row.getAttribute('data-dni'); // Obtener el dni del cliente en esa fila
        var productoCodigo = row.getAttribute('data-producto'); // Obtener el código del producto en esa fila
        var fechaVenta = new Date(row.cells[0].innerText); // Obtener la fecha de la venta en esa fila (suponiendo que la fecha está en la primera columna)

        // Mostrar la fila si el dni del cliente coincide, el código del producto coincide, o si no se ha seleccionado ningún cliente/producto
        var clienteCoincide = (dniCliente === "" || dniCliente === clienteDni);
        var productoCoincide = (codigoProducto === "" || codigoProducto === productoCodigo);
        var fechaCoincide = true; // Por defecto, las filas coinciden si no se han seleccionado fechas

        // Si se selecciona una fecha desde, comprobar que la fecha de la venta sea mayor o igual
        if (fechaDesdeDate) {
            fechaCoincide = fechaCoincide && (fechaVenta >= fechaDesdeDate);
        }
        // Si se selecciona una fecha hasta, comprobar que la fecha de la venta sea menor o igual
        if (fechaHastaDate) {
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
