
    // Obtener los elementos de cliente y de fechas
    var clienteSelect = document.getElementById('clienteSelect');
    var fechaDesdeInput = document.getElementById('fechaDesde');
    var fechaHastaInput = document.getElementById('fechaHasta');

    // Escuchar cambios en el selector de cliente y en los inputs de fecha
    clienteSelect.addEventListener('change', filtrarVentas);
    fechaDesdeInput.addEventListener('change', filtrarVentas);
    fechaHastaInput.addEventListener('change', filtrarVentas);

    function filtrarVentas() {
        var dniCliente = clienteSelect.value; // Obtener el dni del cliente seleccionado
        var fechaDesde = new Date(fechaDesdeInput.value); // Convertir fecha desde a objeto Date
        var fechaHasta = new Date(fechaHastaInput.value); // Convertir fecha hasta a objeto Date

        // Obtener todas las filas de la tabla
        var rows = document.querySelectorAll('table tbody tr');

        rows.forEach(row => {
            var clienteDni = row.getAttribute('data-dni'); // Obtener el dni del cliente en esa fila
            var fechaVenta = new Date(row.cells[0].innerText.split('/').reverse().join('-')); // Obtener la fecha de la venta en formato dd/MM/yyyy

            // Condiciones para mostrar la fila:
            // 1. El cliente seleccionado coincide o no hay selección.
            // 2. La fecha de la venta está dentro del rango de fechas seleccionado o no hay fechas seleccionadas.
            var mostrarPorCliente = (dniCliente === "" || dniCliente === clienteDni);
            var mostrarPorFecha = (!fechaDesdeInput.value || fechaVenta >= fechaDesde) &&
                                  (!fechaHastaInput.value || fechaVenta <= fechaHasta);

            // Mostrar la fila si cumple con ambas condiciones
            if (mostrarPorCliente && mostrarPorFecha) {
                row.style.display = ""; // Mostrar la fila
            } else {
                row.style.display = "none"; // Ocultar la fila
            }
        });
    }

    function confirmDelete() {
        return confirm("¿Estás seguro de que deseas eliminar esta venta y el detalle de venta asociado?");
    }
