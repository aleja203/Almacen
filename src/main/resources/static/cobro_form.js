document.addEventListener("DOMContentLoaded", function () {
    const cobroForm = document.getElementById("cobroForm");
    const agregarFilaBtn = document.getElementById("agregarFila");
    const formularioPago = document.getElementById("formularioPago");

    const tipoFormaPagoSelect = document.getElementById('tipoFormaPago');
    const formaDePagoSelect = document.getElementById('formaDePago');

    tipoFormaPagoSelect.addEventListener("change", function () {
        actualizarFormasPago(tipoFormaPagoSelect, formaDePagoSelect);
    });

    actualizarFormasPago(tipoFormaPagoSelect, formaDePagoSelect);

    agregarFilaBtn.addEventListener("click", function () {
        agregarFilaPago();
    });

    cobroForm.addEventListener("submit", function (event) {
        event.preventDefault();
        enviarDatos();
    });

    const primerImporteInput = cobroForm.querySelector('.importe-input');
    if (primerImporteInput) {
        primerImporteInput.addEventListener('input', actualizarTotalCobro);
    }

    // Configurar el botón de la primera fila para que se pueda limpiar
    const primeraFila = formularioPago.querySelector(".row");
    const primerBoton = primeraFila.querySelector(".agregar-fila-btn");
    configurarBotonAgregarOEliminar(primerBoton, primeraFila, true);
});

function agregarFilaPago() {
    const rows = formularioPago.querySelectorAll(".row");
    const lastRow = rows[rows.length - 1];

    if (!filaCompleta(lastRow)) {
        alert("Por favor, complete todos los campos de la fila antes de agregar una nueva.");
        return;
    }

    const newRow = document.createElement("div");
    newRow.classList.add("row", "mb-3");

    const tiposPago = document.querySelectorAll('#tiposFormaPagoData option');
    const formasPago = document.querySelectorAll('#formasDePagoData option');

    const tiposPagoOptions = Array.from(tiposPago).map(option =>
        `<option value="${option.value}">${option.text}</option>`
    ).join('');

    const formasPagoOptions = Array.from(formasPago).map(option =>
        `<option value="${option.value}" data-tipo="${option.dataset.tipo}">${option.text}</option>`
    ).join('');

    newRow.innerHTML = `
        <div class="col-md-3">
            <select class="form-control tipoFormaPago">
                <option value="">Seleccionar Tipo de Pago</option>
                ${tiposPagoOptions}
            </select>
        </div>
        <div class="col-md-3">
            <select class="form-control formaDePago">
                <option value="">Seleccionar Forma de Pago</option>
                ${formasPagoOptions}
            </select>
        </div>
        <div class="col-md-2">
            <input type="number" class="form-control importe-input" name="importe"
            placeholder="Importe" step="0.01" min="0" oninput="actualizarTotalCobro()">
        </div>
        <div class="col-md-1 d-flex align-items-end">
           
            <button type="button" class="btn btn-primary btn-icon agregar-fila-btn">+</button>
        </div>
    `;

    formularioPago.appendChild(newRow);

    const tipoFormaPagoSelect = newRow.querySelector(".tipoFormaPago");
    const formaDePagoSelect = newRow.querySelector(".formaDePago");
    const agregarBtn = newRow.querySelector(".agregar-fila-btn");

    tipoFormaPagoSelect.addEventListener("change", function () {
        actualizarFormasPago(tipoFormaPagoSelect, formaDePagoSelect);
    });

    configurarBotonAgregarOEliminar(agregarBtn, newRow);
}

function configurarBotonAgregarOEliminar(boton, fila, esPrimeraFila = false) {
    boton.addEventListener("click", function () {
        if (boton.classList.contains('btn-primary')) {
            if (filaCompleta(fila)) {
                boton.textContent = '-';
                boton.classList.remove('btn-primary');
                boton.classList.add('btn-danger');
                boton.removeEventListener("click", arguments.callee);
                boton.addEventListener("click", function () {
                    if (esPrimeraFila) {
                        limpiarFila(fila);
                    } else {
                        eliminarFila(fila);
                    }
                });
                agregarFilaPago();
            } else {
                alert("Complete todos los campos antes de agregar o eliminar la fila.");
            }
        }
    });
}

function limpiarFila(row) {
    row.querySelector('.tipoFormaPago').value = '';
    row.querySelector('.formaDePago').value = '';
    row.querySelector('.importe-input').value = '';
    actualizarTotalCobro();
}

function actualizarFormasPago(tipoFormaPagoSelect, formaDePagoSelect) {
    const selectedTipo = tipoFormaPagoSelect.value;

    Array.from(formaDePagoSelect.options).forEach(option => {
        option.hidden = option.getAttribute('data-tipo') !== selectedTipo && option.value !== "";
    });
    formaDePagoSelect.value = "";
}

function filaCompleta(row) {
    const tipoPago = row.querySelector('.tipoFormaPago').value.trim();
    const formaDePago = row.querySelector('.formaDePago').value.trim();
    const importe = row.querySelector('.importe-input').value.trim();

    return tipoPago !== "" && formaDePago !== "" && !isNaN(importe) && importe !== "";
}

function eliminarFila(row) {
    row.remove();
    actualizarTotalCobro();
}

function calcularTotalCobro() {
    let total = 0;
    formularioPago.querySelectorAll(".importe-input").forEach(input => {
        const importe = parseFloat(input.value);
        if (!isNaN(importe)) {
            total += importe;
        }
    });
    return total;
}

function actualizarTotalCobro() {
    const totalCobro = calcularTotalCobro();
    document.getElementById("totalCobro").value = totalCobro.toFixed(2);
}

function enviarDatos() {
    const cobroForm = document.getElementById("cobroForm");
    const formData = new FormData(cobroForm);

    const cobro = {
        id: parseInt(formData.get("cobroId"), 10),
        clienteId: parseInt(formData.get("cliente"), 10),
        totalCobro: parseFloat(document.getElementById("totalCobro").value),
        formasPago: []
    };

    formularioPago.querySelectorAll(".row").forEach(row => {
        const tipoPago = row.querySelector('.tipoFormaPago').value;
        const formaDePago = row.querySelector('.formaDePago').value;
        const importe = parseFloat(row.querySelector('.importe-input').value);

        if (tipoPago && formaDePago && !isNaN(importe)) {
            cobro.formasPago.push({
                tipoPago: tipoPago,
                formaDePago: formaDePago,
                importe: importe
            });
        }
    });

    fetch("/cobro/registro", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(cobro)
    })
    .then(response => response.json())
    .then(data => {
        if (data.message) {
            alert(data.message);
            window.location.href = "/cobro/registrar";
        } else if (data.error) {
            alert(data.error);
            window.location.href = "/cobro/registrar";
        }
    })
    .catch((error) => console.error("Error en fetch:", error));
}
