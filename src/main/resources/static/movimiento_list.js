// Función para convertir fecha de formato dd/MM/yyyy a objeto Date
function convertirFecha(fechaStr) {
    var partes = fechaStr.split('/');
    if (partes.length === 3) {
        return new Date(partes[2], partes[1] - 1, partes[0]);
    }
    return null;
}

// Función principal para aplicar los filtros
function aplicarFiltros() {
    // Obtener los valores de los filtros
    var tipoSeleccionado = document.getElementById('tipoSelect').value;
    var provCliSeleccionado = document.getElementById('provCliSelect').value;
    var productoSeleccionado = document.getElementById('productoSelect').value;
    var fechaDesde = document.getElementById('fechaDesde').value;
    var fechaHasta = document.getElementById('fechaHasta').value;

    // Convertir las fechas seleccionadas a objetos Date, si existen
    var fechaDesdeDate = fechaDesde ? new Date(fechaDesde + "T00:00:00") : null;
    var fechaHastaDate = fechaHasta ? new Date(fechaHasta + "T23:59:59") : null;

    // Obtener todas las filas de la tabla
    var rows = document.querySelectorAll('table tbody tr');

    rows.forEach(row => {
        // Obtener los datos de la fila actual
        var fechaMovimientoStr = row.cells[0].innerText; // Fecha en la primera columna
        var tipoMovimiento = row.cells[1].innerText;     // Tipo en la segunda columna
        var provCliMovimiento = row.cells[2].innerText;  // Proveedor/Cliente en la tercera columna
        var productoMovimiento = row.cells[4].innerText; // Producto en la quinta columna

        // Convertir la fecha del movimiento a objeto Date
        var fechaMovimiento = convertirFecha(fechaMovimientoStr);

        // Verificar si cada filtro coincide
        var tipoCoincide = (tipoSeleccionado === "" || tipoMovimiento === tipoSeleccionado);
        var provCliCoincide = (provCliSeleccionado === "" || provCliMovimiento === provCliSeleccionado);
        var productoCoincide = (productoSeleccionado === "" || productoMovimiento === productoSeleccionado);

        // Verificar si la fecha del movimiento está dentro del rango seleccionado
        var fechaCoincide = true;
        if (fechaDesdeDate && fechaMovimiento) {
            fechaCoincide = fechaMovimiento >= fechaDesdeDate;
        }
        if (fechaHastaDate && fechaMovimiento) {
            fechaCoincide = fechaCoincide && (fechaMovimiento <= fechaHastaDate);
        }

        // Mostrar u ocultar la fila según los resultados de los filtros
        if (tipoCoincide && provCliCoincide && productoCoincide && fechaCoincide) {
            row.style.display = ""; // Mostrar la fila
        } else {
            row.style.display = "none"; // Ocultar la fila
        }
    });
}

// Escuchar los cambios en los selectores de tipo, proveedor, producto y fechas
document.getElementById('tipoSelect').addEventListener('change', aplicarFiltros);
document.getElementById('provCliSelect').addEventListener('change', aplicarFiltros);
document.getElementById('productoSelect').addEventListener('change', aplicarFiltros);
document.getElementById('fechaDesde').addEventListener('change', aplicarFiltros);
document.getElementById('fechaHasta').addEventListener('change', aplicarFiltros);
