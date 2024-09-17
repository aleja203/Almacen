document.addEventListener("DOMContentLoaded", function () {
    const ventaForm = document.getElementById("ventaForm");
    const formularioVenta = document.getElementById("formularioVenta");
    const totalVentaInput = document.getElementById("totalVenta");

    // Función para actualizar el total al cambiar cantidad o precio
    function actualizarTotal(fila) {
        const cantidadInput = fila.querySelector('.cantidad-input');
        const precioInput = fila.querySelector('.precio-input');
        const totalInput = fila.querySelector('.total-input');

        const cantidad = parseFloat(cantidadInput.value) || 0;
        const precio = parseFloat(precioInput.value) || 0;
        const resultado = cantidad * precio;

        totalInput.value = resultado.toFixed(2); // Mostrar con 2 decimales
        actualizarTotalVenta(); // Actualizar el total general de la venta
    }

    // Función para actualizar el precio según el producto seleccionado
    function actualizarPrecio(row) {
        const productoSelect = row.querySelector('.producto-select');
        const productoSeleccionado = productoSelect.options[productoSelect.selectedIndex];
        const precioDeVenta = productoSeleccionado.getAttribute('data-preciodeventa');
        row.querySelector('.precio-input').value = precioDeVenta ? parseFloat(precioDeVenta).toFixed(2) : '';
        const codigoProducto = productoSeleccionado.value;
        row.querySelector('.codigo-input').value = codigoProducto;
        actualizarTotal(row);
    }

    // Función para agregar una nueva fila al formulario
    function agregarFila(hacerFoco = false) {
        const newRow = document.createElement("div");
        newRow.classList.add("row", "mt-3");

        newRow.innerHTML = `
            <div class="col-md-2">
                <input type="text" class="form-control codigo-input" name="codigo" placeholder="Código">
            </div>
            <div class="col-md-3">
                <select class="form-control producto-select" name="producto">
                    ${formularioVenta.querySelector('.producto-select').innerHTML}
                </select>
            </div>
            <div class="col-md-2">
                <input type="number" class="form-control cantidad-input" name="cantidad" value="1">
            </div>
            <div class="col-md-2">
                <input type="number" class="form-control precio-input" name="precioDeVenta" readonly>
            </div>
            <div class="col-md-2">
                <input type="number" class="form-control total-input" name="total" readonly>
            </div>
            <div class="col-md-1 d-flex align-items-end">
                <button type="button" class="btn btn-primary mt-4 agregar-fila-btn">+</button>
            </div>
        `;

        formularioVenta.appendChild(newRow);
        const productoSelect = newRow.querySelector('.producto-select');
        productoSelect.addEventListener("change", function () {
            actualizarPrecio(newRow);
        });

        if (hacerFoco) {
            newRow.querySelector('input[name="codigo"]').focus();
        }

        const agregarFilaBtn = newRow.querySelector('.agregar-fila-btn');
        agregarFilaBtn.addEventListener("click", function (event) {
            event.preventDefault(); // Prevenir el comportamiento por defecto del botón
            agregarFila(true);
        });
    }

    // Función para actualizar el total general de la venta
    function actualizarTotalVenta() {
        let totalVenta = 0;
        const totalInputs = document.querySelectorAll('.total-input');
        totalInputs.forEach(function (input) {
            totalVenta += parseFloat(input.value) || 0;
        });
        totalVentaInput.value = totalVenta.toFixed(2); // Mostrar el total con 2 decimales
    }

    // Función para filtrar por código de barra y seleccionar el producto
    function filtrarPorCodigoDeBarra(barcode, row) {
        const productoSelect = row.querySelector('.producto-select');
        const opciones = productoSelect.options;
        let productoEncontrado = false;

        for (let i = 0; i < opciones.length; i++) {
            if (opciones[i].value === barcode) {
                productoSelect.selectedIndex = i;
                actualizarPrecio(row);
                productoEncontrado = true;
                break;
            }
        }

        if (!productoEncontrado) {
            alert("Producto no encontrado para el código de barra: " + barcode);
        } else {
            agregarFila(); // Agregar una nueva fila tras encontrar el producto
        }
    }

    // Manejar la entrada del código de barras
    let barcodeInput = "";
    let barcodeTimer;

    document.addEventListener("keydown", function (e) {
        if (barcodeTimer) clearTimeout(barcodeTimer);

        if (e.key !== "Enter") {
            barcodeInput += e.key;
        } else {
            // Evitar que la tecla Enter provoque el envío del formulario
            e.preventDefault();
            const currentRow = formularioVenta.lastElementChild;
            filtrarPorCodigoDeBarra(barcodeInput, currentRow);
            barcodeInput = "";
        }

        barcodeTimer = setTimeout(() => {
            barcodeInput = "";
        }, 200);
    });

    const agregarFilaBtn = ventaForm.querySelector('#agregarFila');
    agregarFilaBtn.addEventListener("click", function () {
        agregarFila(true);
    });

    const firstRow = formularioVenta.querySelector('.row');
    const productoSelect = firstRow.querySelector('.producto-select');
    productoSelect.addEventListener("change", function () {
        actualizarPrecio(firstRow);
    });

    // Escuchar cambios en los inputs de cantidad o precio
    document.addEventListener('input', function (event) {
        if (event.target.matches('.cantidad-input, .precio-input')) {
            const fila = event.target.closest('.row');
            actualizarTotal(fila);
        }
    });

    // Función para enviar los datos al servidor
    function enviarDatos() {
        const formData = new FormData(ventaForm);

        const venta = {
            cliente: formData.get("cliente"),
            observaciones: formData.get("observaciones"),
            totalVenta: parseFloat(formData.get("totalVenta")),
            detalles: [] // Aquí deberás agregar la lógica para convertir los detalles en un array de objetos
        };

        // Añadir detalles al objeto venta
        formularioVenta.querySelectorAll(".row").forEach(row => {
            const codigo = row.querySelector('.codigo-input').value;
            const producto = row.querySelector('.producto-select').value;
            const cantidad = parseFloat(row.querySelector('.cantidad-input').value);
            const precioDeVenta = parseFloat(row.querySelector('.precio-input').value);
            const total = parseFloat(row.querySelector('.total-input').value);

            if (codigo || producto || cantidad || precioDeVenta || total) {
            venta.detalles.push({
                producto: codigo,
                cantidad: cantidad,
                precioVenta: precioDeVenta,
                total: total
            });
        }
        });

        fetch("/venta/registro", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(venta)
        })
        .then(response => response.json())
        .then(data => console.log("Success:", data))
        .catch((error) => console.error("Error:", error));
    }
    
//function enviarDatos() {
//    fetch("/venta/registro", {
//        method: "POST"
//    })
//    .then(response => response.text()) // Procesar la respuesta como texto
//    .then(data => console.log("Success:", data)) // Imprimir la respuesta
//    .catch(error => console.error("Error:", error)); // Manejar errores
//}
    
    // Agregar el manejador de eventos al formulario
    ventaForm.addEventListener("submit", function (event) {
        event.preventDefault(); // Evitar el envío por defecto del formulario
        enviarDatos(); // Llamar a la función para enviar los datos
    });
    
});
