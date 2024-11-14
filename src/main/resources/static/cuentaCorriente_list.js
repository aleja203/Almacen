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

    var fechaDesde = document.getElementById('fechaDesde').value;
    var fechaHasta = document.getElementById('fechaHasta').value;

    // Convertir las fechas seleccionadas a objetos Date, si existen
    var fechaDesdeDate = fechaDesde ? new Date(fechaDesde + "T00:00:00") : null;
    var fechaHastaDate = fechaHasta ? new Date(fechaHasta + "T23:59:59") : null;

    // Obtener todas las filas de la tabla
    var rows = document.querySelectorAll('table tbody tr');

    rows.forEach(row => {
        // Obtener los datos de la fila actual
        var fechaCuentaCorrienteStr = row.cells[0].innerText; // Fecha en la primera columna


        // Convertir la fecha del movimiento a objeto Date
        var fechaCuentaCorriente = convertirFecha(fechaCuentaCorrienteStr);

        // Verificar si la fecha del movimiento está dentro del rango seleccionado
        var fechaCoincide = true;
        if (fechaDesdeDate && fechaCuentaCorriente) {
            fechaCoincide = fechaCuentaCorriente >= fechaDesdeDate;
        }
        if (fechaHastaDate && fechaCuentaCorriente) {
            fechaCoincide = fechaCoincide && (fechaCuentaCorriente <= fechaHastaDate);
        }

        // Mostrar u ocultar la fila según los resultados de los filtros
       row.style.display = fechaCoincide ? "" : "none";
    });
}
document.getElementById('fechaDesde').addEventListener('change', aplicarFiltros);
document.getElementById('fechaHasta').addEventListener('change', aplicarFiltros);

