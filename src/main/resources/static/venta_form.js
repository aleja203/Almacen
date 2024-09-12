document.addEventListener("DOMContentLoaded", function () {
    const formularioVenta = document.getElementById("formularioVenta");

    // Función para actualizar el total al cambiar cantidad o precio
    function actualizarTotal(fila) {
        const cantidadInput = fila.querySelector('.cantidad-input');
        const precioInput = fila.querySelector('.precio-input');
        const totalInput = fila.querySelector('.total-input');

        // Obtener los valores de los inputs, convirtiéndolos a número. Si no hay valor, se usa 0.
        const cantidad = parseFloat(cantidadInput.value) || 0;
        const precio = parseFloat(precioInput.value) || 0;

        // Realizar la multiplicación del valor de cantidad con el valor del input de precio
        const resultado = cantidad * precio;

        // Asignar el resultado al input de total
        totalInput.value = resultado.toFixed(2); // Mostrar con 2 decimales

        // Opcional: imprimir los valores y el resultado en la consola para verificación
        console.log('Cantidad:', cantidad);
        console.log('Precio:', precio);
        console.log('Resultado:', resultado);
    }

    // Función para actualizar el precio según el producto seleccionado
    function actualizarPrecio(row) {
        const productoSelect = row.querySelector('.producto-select');
        const productoSeleccionado = productoSelect.options[productoSelect.selectedIndex];
        const precioDeVenta = productoSeleccionado.getAttribute('data-preciodeventa');
        row.querySelector('.precio-input').value = precioDeVenta ? parseFloat(precioDeVenta).toFixed(2) : '';

        // Actualizar el código del producto en el input de código
        const codigoProducto = productoSeleccionado.value;
        row.querySelector('.codigo-input').value = codigoProducto;

        // Llamar a actualizarTotal después de actualizar el precio
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
                <select class="form-control producto-select">
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
                <button class="btn btn-primary mt-4 agregar-fila-btn">
                    <span class="btn-icon">+</span>
                </button>
            </div>
        `;

        formularioVenta.appendChild(newRow);

        // Agregar el evento para el select de productos en la nueva fila
        const productoSelect = newRow.querySelector('.producto-select');
        productoSelect.addEventListener("change", function () {
            actualizarPrecio(newRow);
        });

        if (hacerFoco) {
            newRow.querySelector('input[name="codigo"]').focus(); // Hacer foco en el nuevo campo de código
        }

        // Asignar funcionalidad de "Agregar fila" al botón en la nueva fila
        const agregarFilaBtn = newRow.querySelector('.agregar-fila-btn');
        agregarFilaBtn.addEventListener("click", function () {
            agregarFila(true); // Agregar fila y hacer foco en el nuevo campo
        });
    }

    // Función para manejar el escaneo del código de barras
    function filtrarPorCodigoDeBarra(codigo, row) {
        const productoSelect = row.querySelector('.producto-select');
        let productoSeleccionado = null;

        for (let i = 0; i < productoSelect.options.length; i++) {
            const option = productoSelect.options[i];
            if (option.value === codigo) {
                productoSeleccionado = option;
                break;
            }
        }

        if (productoSeleccionado) {
            // Actualizar inputs con los valores del producto
            row.querySelector('.codigo-input').value = codigo;
            productoSelect.value = codigo;
            actualizarPrecio(row); // Actualiza el precio y el total

            // Agregar nueva fila y hacer foco en el nuevo campo de código
            agregarFila(true); // true para hacer foco en el nuevo campo de código
        } else {
            alert("No se encontró ningún producto con el código de barras: " + codigo);
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
            const currentRow = formularioVenta.lastElementChild; // Última fila añadida
            filtrarPorCodigoDeBarra(barcodeInput, currentRow);
            barcodeInput = "";
        }

        barcodeTimer = setTimeout(() => {
            barcodeInput = "";
        }, 200);
    });

    // Asignar la función de agregar fila manualmente al botón de la primera fila
    const agregarFilaBtn = formularioVenta.querySelector('#agregarFila');
    agregarFilaBtn.addEventListener("click", function () {
        agregarFila(true); // Agregar fila y hacer foco en el nuevo campo de código
    });

    // Asignar la actualización de precio y código en la primera fila
    const firstRow = formularioVenta.querySelector('.row');
    const productoSelect = firstRow.querySelector('.producto-select');
    productoSelect.addEventListener("change", function () {
        actualizarPrecio(firstRow);
    });

    // Escuchar cambios en los inputs de cantidad o precio
    document.addEventListener('input', function (event) {
        if (event.target.matches('.cantidad-input, .precio-input')) {
            const fila = event.target.closest('.row');
            actualizarTotal(fila); // Llama a la función para actualizar el total
        }
    });
});
