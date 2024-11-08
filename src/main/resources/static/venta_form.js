
document.addEventListener("DOMContentLoaded", function () {
    const ventaForm = document.getElementById("ventaForm");
    const formularioVenta = document.getElementById("formularioVenta");
    const totalVentaInput = document.getElementById("totalVenta");
    const importeInput = document.querySelector('.importe-input');
    const formularioPago = document.getElementById("formularioPago"); // contenedor para formas de pago
    
    function sincronizarImporte() {
        const primerImporte = formularioPago.querySelector('.importe-input');
        if (totalVentaInput && primerImporte) {
            primerImporte.value = totalVentaInput.value;
        }

    }

    sincronizarImporte();
        
// Función para agregar una fila de forma de pago
    function agregarFilaPago() {
        // Obtener los selects existentes
        const tipoFormaPagoSelects = document.querySelectorAll(".tipoFormaPago");
        const formaDePagoSelects = document.querySelectorAll(".formaDePago");

        // Verificar si todos los campos "tipo" y "forma de pago" están completos
        for (let i = 0; i < tipoFormaPagoSelects.length; i++) {
            const tipoFormaPago = tipoFormaPagoSelects[i].value;
            const formaDePago = formaDePagoSelects[i].value;

            if (!tipoFormaPago || !formaDePago) {
                alert("Por favor, complete todos los campos de tipo y forma de pago antes de agregar una nueva fila.");
                return; // No se agrega la fila si no están completos
            }
        }

        const newRow = document.createElement("div");
        newRow.classList.add("row", "mb-3");

        // Obtener opciones de los elementos ocultos
        const tiposPago = document.querySelectorAll('#tiposFormaPagoData option');
        const formasPago = document.querySelectorAll('#formasDePagoData option');

        document.querySelectorAll('#formasDePagoData option').forEach(opt => {
            console.log(opt.value, opt.dataset.tipo);
        });

        // Crear los select dinámicamente
        const tiposPagoOptions = Array.from(tiposPago).map(option =>
                `<option value="${option.value}">${option.text}</option>`
        ).join('');

        const formasPagoOptions = Array.from(formasPago).map(option =>
                `<option value="${option.value}" data-tipo="${option.dataset.tipo}">${option.text}</option>`
        ).join('');

        console.log("formasPagoOptions:", formasPagoOptions);

        newRow.innerHTML = `
        <div class="col-md-3">
            <select class="form-control tipoFormaPago">
                <option value="">Seleccionar Tipo de Pago</option>
                ${tiposPagoOptions}  <!-- Usando opciones generadas dinámicamente -->
            </select>
        </div>
        <div class="col-md-3">
            <select class="form-control formaDePago">
                <option value="">Seleccionar Forma de Pago</option>
                ${formasPagoOptions}  <!-- Opciones generadas dinámicamente de formas de pago -->
            </select>
        </div>
        <div class="col-md-2">
            <input type="number" class="form-control importe-input" name="importe"
            placeholder="Importe" step="0.01" min="0">
        </div>
    `;

        // Añadir la nueva fila al contenedor de formulario de pago
        const formularioPago = document.getElementById("formularioPago");
        formularioPago.appendChild(newRow);

        // Añadir event listeners a los selects
        const tipoFormaPagoSelect = newRow.querySelector(".tipoFormaPago");
        const formaDePagoSelect = newRow.querySelector(".formaDePago");

        tipoFormaPagoSelect.addEventListener("change", function () {
            actualizarFormasPago(tipoFormaPagoSelect, formaDePagoSelect);
        });

        // Añadir event listener al nuevo campo importe usando blur
        const importeInput = newRow.querySelector(".importe-input");
        importeInput.addEventListener("blur", validarTotalPago);
    }

    function actualizarFormasPago(tipoFormaPagoSelect, formaDePagoSelect) {
        const selectedTipo = tipoFormaPagoSelect.value;

        // Filtrar y mostrar solo las formas de pago correspondientes al tipo seleccionado
        Array.from(formaDePagoSelect.options).forEach(option => {
            option.hidden = option.getAttribute('data-tipo') !== selectedTipo && option.value !== "";
        });
        formaDePagoSelect.value = ""; // Restablecer la selección de forma de pago
    }


// Función para validar si el total de pagos alcanza el total de venta
    function validarTotalPago() {
        const importeInputs = document.querySelectorAll(".importe-input");
        let totalPagado = 0;

        importeInputs.forEach(input => {
            totalPagado += parseFloat(input.value) || 0;
        });

        // Si el total pagado alcanza el total de la venta, evitar agregar más filas
        if (totalPagado >= parseFloat(totalVentaInput.value)) {
            if (totalPagado > parseFloat(totalVentaInput.value)) {
                alert("El total de los importes no puede exceder el total de la venta.");
                this.value = ""; // Limpia el último valor ingresado si excede el total
            }
            return; // Evita agregar filas adicionales
        }

        // Solo agregar fila si el total pagado es menor al total de la venta
        if (totalPagado < parseFloat(totalVentaInput.value)) {
            agregarFilaPago();
        }
    }

// Añadir listener al primer campo de importe al iniciar, usando blur
    const primerImporteInput = formularioPago.querySelector(".importe-input");
    if (primerImporteInput) {
        primerImporteInput.addEventListener("blur", validarTotalPago);
    }


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
        const precioVenta = productoSeleccionado.getAttribute('data-precioVenta');
        row.querySelector('.precio-input').value = precioVenta ? parseFloat(precioVenta).toFixed(2) : '';
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
    // Obtener la última fila añadida para verificar si está completa
    const ultimaFila = formularioVenta.lastElementChild;

    // Si la última fila existe y no está completa, mostrar un mensaje y no agregar una nueva fila
    if (ultimaFila && !filaCompleta(ultimaFila)) {
        alert("Por favor, complete todos los campos de la fila actual antes de agregar una nueva.");
        return;
    }

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
            <input type="number" class="form-control precio-input" name="precioVenta">
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

        // Llamar a agregarFila nuevamente para intentar agregar una nueva fila si se cumplen las condiciones
        agregarFila(true);
    });
}

// Función para actualizar el total de la venta sumando los totales de cada fila
function actualizarTotalVenta() {
    let totalVenta = 0;
    const totalInputs = document.querySelectorAll('.total-input');
    totalInputs.forEach(function (input) {
        totalVenta += parseFloat(input.value) || 0;
    });
    totalVentaInput.value = totalVenta.toFixed(2); // Mostrar el total con 2 decimales
    
    // Llamamos a sincronizarImporte aquí para asegurar la actualización
    sincronizarImporte();
}

// Función para verificar si una fila está completa y cambiar el botón a "-"
function verificarFilaCompleta(row) {
    if (filaCompleta(row)) {
        const boton = row.querySelector('.btn-primary');
        boton.textContent = '-';
        boton.classList.remove('btn-primary');
        boton.classList.add('btn-danger');
    }
}

// Función para verificar si una fila está completa
function filaCompleta(row) {
    const codigo = row.querySelector('.codigo-input').value.trim(); // Asegura que no esté vacío ni tenga solo espacios
    const producto = row.querySelector('.producto-select').value;
    const cantidad = parseFloat(row.querySelector('.cantidad-input').value);
    const precio = parseFloat(row.querySelector('.precio-input').value);
    const total = parseFloat(row.querySelector('.total-input').value);

    // Verificar que 'codigo' y 'producto' no estén vacíos, y que 'cantidad', 'precio', y 'total' sean mayores a 0
    return codigo !== "" && producto !== "" && cantidad > 0 && precio > 0 && total > 0;
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

    // Función para enviar los datos al servidor
    function enviarDatos() {
        const formData = new FormData(ventaForm);

        const venta = {
            id: parseInt(formData.get("ventaId"), 10), // Convertir el ID de String a Long
            clienteId: parseInt(formData.get("cliente"), 10), // Convertir clienteId a Long
            observaciones: formData.get("observaciones"),
            totalVenta: parseFloat(formData.get("totalVenta")),
            detalles: [],
            formasPago: []
        };

// Recorrer las filas del formulario de venta
        formularioVenta.querySelectorAll(".row").forEach(row => {
            const codigo = row.querySelector('.codigo-input').value;
            const producto = row.querySelector('.producto-select').value;
            const cantidad = parseFloat(row.querySelector('.cantidad-input').value);
            const precioVenta = parseFloat(row.querySelector('.precio-input').value);
            const total = parseFloat(row.querySelector('.total-input').value);

            // Agregar detalle solo si todos los campos están completos
            if (codigo && producto && cantidad && precioVenta && total) {
                venta.detalles.push({
                    producto: codigo,
                    cantidad: cantidad,
                    precioVenta: precioVenta,
                    total: total
                });
            }
        });

// Recorrer las filas del formulario de pago
        formularioPago.querySelectorAll(".row").forEach(row => {
            const tipoPago = row.querySelector('.tipoFormaPago').value;
            const formaDePago = row.querySelector('.formaDePago').value;
            const importe = parseFloat(row.querySelector('.importe-input').value); // Convierte importe a número decimal

            // Agregar forma de pago solo si todos los campos están completos
            if (tipoPago && formaDePago && importe) {
                venta.formasPago.push({
                    tipoPago: tipoPago,
                    formaDePago: formaDePago,
                    importe: importe
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

    ventaForm.addEventListener("submit", function (event) {
        event.preventDefault(); // Prevenir el envío por defecto del formulario
        enviarDatos(); // Llamar a la función para enviar los datos
    });
    
    
    const tipoFormaPagoSelect = document.getElementById('tipoFormaPago');
    const formaDePagoSelect = document.getElementById('formaDePago');
//    const formasDePagoData = document.getElementById('formasDePagoData');

// Guardar todas las opciones de forma de pago al cargar la página
const todasLasOpcionesFormasPago = Array.from(formaDePagoSelect.querySelectorAll('option'));

    // Evento para actualizar las formas de pago cuando se selecciona un tipo
    tipoFormaPagoSelect.addEventListener('change', function () {
        const tipoSeleccionado = this.value;

        // Limpiar el select de formas de pago
        formaDePagoSelect.innerHTML = '<option value="">Seleccionar la forma de pago</option>';

       if (tipoSeleccionado) {
        // Filtrar y mostrar las opciones que coincidan con el tipo seleccionado
        const formasFiltradas = todasLasOpcionesFormasPago.filter(forma => {
            return forma.getAttribute('data-tipo') === tipoSeleccionado;
        });

        // Agregar las opciones filtradas al select de formaDePago
        formasFiltradas.forEach(forma => {
            formaDePagoSelect.appendChild(forma);
        });
    } else {
        // Si no hay tipo seleccionado, mostrar todas las opciones
        todasLasOpcionesFormasPago.forEach(forma => {
        formaDePagoSelect.appendChild(forma);
        });
    }
});
    
});

