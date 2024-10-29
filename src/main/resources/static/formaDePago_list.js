document.addEventListener("DOMContentLoaded", function () {
    const tipoFormaPagoSelect = document.getElementById("tipoFormaPago");
    const formaDePagoRows = document.querySelectorAll("#formaDePagoTableBody tr");

    const filtrarFormasDePagoPorTipo = () => {
        const selectedTipoFormaPago = tipoFormaPagoSelect.value;
        console.log("Tipo de Forma de Pago seleccionado:", selectedTipoFormaPago);

        formaDePagoRows.forEach(row => {
            // Accedemos al atributo data-tipo en cada fila
            const tipo = row.querySelector("td[data-attr]")?.getAttribute("data-attr")?.trim();
            console.log("Tipo de Forma de Pago de la fila:", tipo);

            // Mostrar/Ocultar filas según el tipo seleccionado
            if (!selectedTipoFormaPago || tipo === selectedTipoFormaPago) {
                row.style.display = "";  // Mostrar fila
            } else {
                row.style.display = "none";  // Ocultar fila
            }
        });
    };

    tipoFormaPagoSelect.addEventListener("change", filtrarFormasDePagoPorTipo);

    // Llamada inicial para mostrar las filas según el valor predeterminado del select
    filtrarFormasDePagoPorTipo();
});
