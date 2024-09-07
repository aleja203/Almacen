//
//document.addEventListener("DOMContentLoaded", function () {
//    const rubroSelect = document.getElementById("rubroSelect");
//    const subRubroSelect = document.getElementById("subRubroSelect");
//    const productoSelect = document.getElementById("productoSelect");
//    const productoRows = document.querySelectorAll("#productoTableBody tr");
//
//    // Guardar todas las opciones originales de subrubro en una variable
//    let originalSubRubroOptions = Array.from(subRubroSelect.children);
//
//    // Función para actualizar las opciones de subrubro basadas en el rubro seleccionado
//    function actualizarSubRubroOptions() {
//        const selectedRubroId = rubroSelect.value;
//
//        // Limpiar las opciones actuales del select de subrubro (dejando solo el 'Seleccionar Subrubro')
//        subRubroSelect.innerHTML = '<option value="">Seleccionar Subrubro</option>';
//
//        // Filtrar y agregar solo las opciones que coincidan con el rubro seleccionado
//        originalSubRubroOptions.forEach(option => {
//            const rubroId = option.getAttribute("data-rubro-id");
//            if (rubroId === selectedRubroId || selectedRubroId === "") {
//                subRubroSelect.appendChild(option); // Mostrar solo los subrubros correspondientes
//            }
//        });
//    }
//
//    // Evento que se dispara al cambiar el rubro seleccionado
//    rubroSelect.addEventListener("change", function () {
//        actualizarSubRubroOptions();
//        filtrarProductos();
//    });
//
//    // Evento que se dispara al cambiar el subrubro seleccionado
//    subRubroSelect.addEventListener("change", function () {
//        filtrarProductos();
//    });
//
//    // Evento que se dispara al cambiar el producto seleccionado
//    productoSelect.addEventListener("change", function () {
//        filtrarProductos();
//    });
//
//    // Función para filtrar productos según los filtros seleccionados
//    function filtrarProductos() {
//        const selectedRubroId = rubroSelect.value;
//        const selectedSubRubroId = subRubroSelect.value;
//        const selectedProductoId = productoSelect.value;
//
//        productoRows.forEach(row => {
//            const productoId = row.querySelector("td:nth-child(2)").getAttribute("data-producto-id");
//            const rubroId = row.querySelector("td:nth-child(3)").getAttribute("data-rubro-id");
//            const subRubroId = row.querySelector("td:nth-child(4)").getAttribute("data-subrubro-id");
//
//            // Mostrar/Ocultar la fila según los filtros seleccionados
//            const matchRubro = !selectedRubroId || rubroId === selectedRubroId;
//            const matchSubRubro = !selectedSubRubroId || subRubroId === selectedSubRubroId;
//            const matchProducto = !selectedProductoId || productoId === selectedProductoId;
//
//            if (matchRubro && matchSubRubro && matchProducto) {
//                row.style.display = "";
//            } else {
//                row.style.display = "none";
//            }
//        });
//    }
//});

document.addEventListener("DOMContentLoaded", function () {
    const rubroSelect = document.getElementById("rubroSelect");
    const subRubroSelect = document.getElementById("subRubroSelect");
    const productoSelect = document.getElementById("productoSelect");
    const productoRows = document.querySelectorAll("#productoTableBody tr");

    let originalSubRubroOptions = Array.from(subRubroSelect.children);

    // Variables para manejar la entrada del código de barras
    let barcodeInput = "";
    let barcodeTimer;

    function actualizarSubRubroOptions() {
        const selectedRubroId = rubroSelect.value;
        subRubroSelect.innerHTML = '<option value="">Seleccionar Subrubro</option>';
        originalSubRubroOptions.forEach(option => {
            const rubroId = option.getAttribute("data-rubro-id");
            if (rubroId === selectedRubroId || selectedRubroId === "") {
                subRubroSelect.appendChild(option);
            }
        });
    }

    rubroSelect.addEventListener("change", function () {
        actualizarSubRubroOptions();
        filtrarProductos();
    });

    subRubroSelect.addEventListener("change", function () {
        filtrarProductos();
    });

    productoSelect.addEventListener("change", function () {
        filtrarProductos();
    });

    function filtrarProductos() {
        const selectedRubroId = rubroSelect.value;
        const selectedSubRubroId = subRubroSelect.value;
        const selectedProductoId = productoSelect.value;

        productoRows.forEach(row => {
            const rubroId = row.querySelector("td:nth-child(3)").getAttribute("data-rubro-id");
            const subRubroId = row.querySelector("td:nth-child(4)").getAttribute("data-subrubro-id");
            const productoId = row.querySelector("td:nth-child(2)").getAttribute("data-producto-id");

            const matchRubro = !selectedRubroId || rubroId === selectedRubroId;
            const matchSubRubro = !selectedSubRubroId || subRubroId === selectedSubRubroId;
            const matchProducto = !selectedProductoId || productoId === selectedProductoId;

            if (matchRubro && matchSubRubro && matchProducto) {
                row.style.display = "";
            } else {
                row.style.display = "none";
            }
        });
    }

    function filtrarPorCodigoDeBarra(codigo) {
        let productoEncontrado = false;
        productoRows.forEach(row => {
            const codigoDeBarra = row.querySelector("td:nth-child(7)").getAttribute("data-codigo-barra"); // Índice de la columna con el código de barras

            if (codigoDeBarra === codigo) {
                row.style.display = "";
                productoEncontrado = true;
            } else {
                row.style.display = "none";
            }
        });
        if (!productoEncontrado) {
            alert("No se encontró ningún producto con el código de barras: " + codigo);
        }
    }

    document.addEventListener("keydown", function (e) {
        if (barcodeTimer) {
            clearTimeout(barcodeTimer);
        }

        if (e.key !== "Enter") {
            barcodeInput += e.key;
        }

        if (e.key === "Enter") {
            filtrarPorCodigoDeBarra(barcodeInput);
            barcodeInput = "";
        }

        barcodeTimer = setTimeout(() => {
            barcodeInput = "";
        }, 200);
    });
});
