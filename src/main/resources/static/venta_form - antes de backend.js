document.addEventListener("DOMContentLoaded", function () {
    const formularioVenta = document.getElementById("formularioVenta");

    // Función para actualizar el total al cambiar cantidad o precio
    function actualizarTotal(fila) {
        const cantidadInput = fila.querySelector('.cantidad-input');
        const precioInput = fila.querySelector('.precio-input');
        const totalInput = fila.querySelector('.total-input');

        const cantidad = parseFloat(cantidadInput.value) || 0;
        const precio = parseFloat(precioInput.value) || 0;
        const resultado = cantidad * precio;

        totalInput.value = resultado.toFixed(2); // Mostrar con 2 decimales
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
        const productoSelect = newRow.querySelector('.producto-select');
        productoSelect.addEventListener("change", function () {
            actualizarPrecio(newRow);
        });

        if (hacerFoco) {
            newRow.querySelector('input[name="codigo"]').focus();
        }

        const agregarFilaBtn = newRow.querySelector('.agregar-fila-btn');
        agregarFilaBtn.addEventListener("click", function () {
            agregarFila(true);
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
            row.querySelector('.codigo-input').value = codigo;
            productoSelect.value = codigo;
            actualizarPrecio(row);
            agregarFila(true);
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
            const currentRow = formularioVenta.lastElementChild;
            filtrarPorCodigoDeBarra(barcodeInput, currentRow);
            barcodeInput = "";
        }

        barcodeTimer = setTimeout(() => {
            barcodeInput = "";
        }, 200);
    });

    const agregarFilaBtn = formularioVenta.querySelector('#agregarFila');
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

    // Envío del formulario
    document.getElementById('submitVenta').addEventListener('click', function () {
        const filas = document.querySelectorAll('#formularioVenta .row');
        let ventas = [];
        
        const cliente = document.getElementById('cliente').value;
        const observaciones = document.getElementById('observaciones').value;

        filas.forEach(fila => {
            const codigo = fila.querySelector('.codigo-input').value;
            const cantidad = fila.querySelector('.cantidad-input').value;
            const precioDeVenta = fila.querySelector('.precio-input').value;
            const total = fila.querySelector('.total-input').value;

            if (codigo && cantidad && precioDeVenta) {
                ventas.push({
                    codigo: codigo,
                    cantidad: cantidad,
                    precioDeVenta: precioDeVenta,
                    total: total
                });
            }
        });

        if (ventas.length > 0) {
            fetch('/ventas', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({
                    cliente: cliente,
                    observaciones: observaciones,
                    ventas: ventas
                }),
            })
            .then(response => response.json())
            .then(data => {
                console.log('Venta realizada:', data);
            })
            .catch((error) => {
                console.error('Error:', error);
            });
        } else {
            alert('No se ha ingresado ninguna venta.');
        }
    });
});
