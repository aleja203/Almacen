
    // Obtener los elementos de proveedor y de fechas
    var proveedorSelect = document.getElementById('proveedorSelect');
    var fechaDesdeInput = document.getElementById('fechaDesde');
    var fechaHastaInput = document.getElementById('fechaHasta');

    // Escuchar cambios en el selector de proveedor y en los inputs de fecha
    proveedorSelect.addEventListener('change', filtrarCompras);
    fechaDesdeInput.addEventListener('change', filtrarCompras);
    fechaHastaInput.addEventListener('change', filtrarCompras);

    function filtrarCompras() {
        var idProveedor = proveedorSelect.value; // Obtener el id del proveedor seleccionado
        var fechaDesde = new Date(fechaDesdeInput.value); // Convertir fecha desde a objeto Date
        var fechaHasta = new Date(fechaHastaInput.value); // Convertir fecha hasta a objeto Date

        // Obtener todas las filas de la tabla
        var rows = document.querySelectorAll('table tbody tr');

        rows.forEach(row => {
            var proveedorId = row.getAttribute('data-id'); // Obtener el id del proveedor en esa fila
            var fechaVenta = new Date(row.cells[0].innerText.split('/').reverse().join('-')); // Obtener la fecha de la venta en formato dd/MM/yyyy

            // Condiciones para mostrar la fila:
            // 1. El proveedor seleccionado coincide o no hay selección.
            // 2. La fecha de la venta está dentro del rango de fechas seleccionado o no hay fechas seleccionadas.
            var mostrarPorProveedor = (idProveedor === "" || idProveedor === proveedorId);
            var mostrarPorFecha = (!fechaDesdeInput.value || fechaVenta >= fechaDesde) &&
                                  (!fechaHastaInput.value || fechaVenta <= fechaHasta);

            // Mostrar la fila si cumple con ambas condiciones
            if (mostrarPorProveedor && mostrarPorFecha) {
                row.style.display = ""; // Mostrar la fila
            } else {
                row.style.display = "none"; // Ocultar la fila
            }
        });
    }

    function confirmDelete() {
        return confirm("¿Estás seguro de que deseas eliminar esta compra y el detalle de compra asociado?");
    }



