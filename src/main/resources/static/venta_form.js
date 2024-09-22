document.addEventListener("DOMContentLoaded", function () {
    const ventaForm = document.getElementById("ventaForm");
    const formularioVenta = document.getElementById("formularioVenta");
    const totalVentaInput = document.getElementById("totalVenta");
    let barcodeInput = "";
    let barcodeTimer;

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
        verificarFilaCompleta(row);
        // Solo agregar una nueva fila si la fila actual está completa y aún no se ha agregado una fila
        if (filaCompleta(row) && !row.classList.contains('fila-agregada')) {
            agregarFila(); // Agregar una nueva fila solo si la actual está completa
            row.classList.add('fila-agregada'); // Marca esta fila como completa para evitar agregar filas adicionales
        }
    }

    // Función para agregar una nueva fila al formulario
    function agregarFila(hacerFoco = false) {
        const newRow = document.createElement("div");
        newRow.classList.add("row", "mt-3");

        newRow.innerHTML = `
            <div class="col-md-2">
                <input type="text" class="form-control codigo-input" name="codigo" placeholder="Código" readonly>
            </div>
            <div class="col-md-3">
                <select class="form-control producto-select" name="producto">
                    ${formularioVenta.querySelector('.producto-select').innerHTML}
                </select>
            </div>
            <div class="col-md-2">
                <input type="number" class="form-control cantidad-input" name="cantidad" value="1" step="0.01" min="0">
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
            if (agregarFilaBtn.classList.contains('btn-primary')) {
                agregarFila(true);
            } else {
                eliminarFila(newRow);
            }
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

    // Función para verificar si una fila está completa y cambiar el botón a "-"
    function verificarFilaCompleta(row) {
        if (filaCompleta(row)) {
            const boton = row.querySelector('.btn-primary');
            console.log(boton);
            boton.textContent = '-';
            boton.classList.remove('btn-primary');
            boton.classList.add('btn-danger');
        }
    }

    // Función para verificar si una fila está completa
    function filaCompleta(row) {
        const codigo = row.querySelector('.codigo-input').value;
        const producto = row.querySelector('.producto-select').value;
        const cantidad = parseFloat(row.querySelector('.cantidad-input').value);
        const precio = parseFloat(row.querySelector('.precio-input').value);
        const total = parseFloat(row.querySelector('.total-input').value);

        return codigo && producto && cantidad && precio && total;
    }

    // Función para limpiar una fila (primaria) en lugar de eliminarla
    function limpiarFila(row) {
        row.querySelector('.codigo-input').value = '';
        row.querySelector('.producto-select').value = '';
        row.querySelector('.cantidad-input').value = '';
        row.querySelector('.precio-input').value = '';
        row.querySelector('.total-input').value = '';
        actualizarTotalVenta(); // Asegurarse de que el total se actualiza
    }

    // Función para eliminar una fila
    function eliminarFila(row) {
        if (row === formularioVenta.querySelector('.row')) {
            limpiarFila(row); // Limpiar la fila primaria
        } else {
            row.remove(); // Eliminar filas dinámicas
        }
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
            // Solo agregar una nueva fila si la fila actual está completa y aún no se ha agregado una fila
            if (filaCompleta(row) && !row.classList.contains('fila-agregada')) {
                agregarFila(); // Agregar una nueva fila tras encontrar y completar el producto
                row.classList.add('fila-agregada'); // Marca esta fila como completa para evitar agregar filas adicionales
            }
        }
    }

    // Manejar la entrada del código de barras desde cualquier parte de la vista
    document.addEventListener("keydown", function (e) {
        if (barcodeTimer) clearTimeout(barcodeTimer);

        if (e.key !== "Enter") {
            barcodeInput += e.key;
        } else {
            e.preventDefault(); // Evitar que la tecla Enter provoque el envío del formulario

            const currentRow = formularioVenta.lastElementChild;
            filtrarPorCodigoDeBarra(barcodeInput, currentRow);
            barcodeInput = "";

            // No realizar el envío automático del formulario
        }

        barcodeTimer = setTimeout(() => {
            barcodeInput = "";
        }, 200); // Limpiar el input después de un breve tiempo de inactividad
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

    // Manejar la selección del cliente desde el select
    const clienteSelect = document.getElementById("rubroSelect");

    // Función para enviar los datos al servidor
    function enviarDatos() {
        const formData = new FormData(ventaForm);

        const venta = {
            clienteId: formData.get("cliente"), // Cambiado para obtener el cliente desde el select
            observaciones: formData.get("observaciones"),
            totalVenta: parseFloat(formData.get("totalVenta")),
            detalles: []
        };

        formularioVenta.querySelectorAll(".row").forEach(row => {
            const codigo = row.querySelector('.codigo-input').value;
            const producto = row.querySelector('.producto-select').value;
            const cantidad = parseFloat(row.querySelector('.cantidad-input').value);
            const precioDeVenta = parseFloat(row.querySelector('.precio-input').value);
            const total = parseFloat(row.querySelector('.total-input').value);

            if (codigo && producto && cantidad && precioDeVenta && total) {
                venta.detalles.push({
                    producto: codigo,
                    //productoId: producto,
                    cantidad: cantidad,
                    precioDeVenta: precioDeVenta,
                    total: total
                });
            }
        });

        console.log("Venta:", venta);

       fetch("/venta/registro", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(venta)
        })
        .then(response => response.json())
        .then(data => {
            if (data.message) {
                alert(data.message);
                window.location.href = "/venta/registrar";
            } else if (data.error) {
                alert(data.error);
                window.location.href = "/venta/registrar";
            }
        })
        .catch((error) => console.error("Error en fetch:", error));
    }

    ventaForm.addEventListener("submit", function (e) {
        e.preventDefault(); // Prevenir el envío normal del formulario
        enviarDatos(); // Llamar a la función para enviar los datos
    });
});
