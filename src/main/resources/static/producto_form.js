
document.addEventListener("DOMContentLoaded", function () {
    const rubroSelect = document.getElementById("rubroSelect");
    const subRubroSelect = document.getElementById("subRubroSelect");

    // Guardamos todas las opciones de subrubro al cargar la página
    const subRubroOptions = Array.from(subRubroSelect.querySelectorAll("option"));

    // Función que filtra los subrubros según el rubro seleccionado
    function filtrarSubRubros() {
        const selectedRubroId = rubroSelect.value;

        // Limpiamos las opciones del select de subrubro (dejando solo el 'Seleccionar Subrubro')
        subRubroSelect.innerHTML = '<option value="">Seleccionar Subrubro</option>';

        // Filtrar y agregar solo las opciones que coincidan con el rubro seleccionado
        subRubroOptions.forEach(option => {
            const rubroId = option.getAttribute("data-rubro-id");
            if (rubroId === selectedRubroId) {
                subRubroSelect.appendChild(option); // Mostrar solo los subrubros correspondientes
            }
        });

        // Si es un formulario de registro (sin subrubro seleccionado), no establecemos el valor
        if (document.querySelector("#subRubroSelect option[selected]")) {
            const currentSubRubroId = document.querySelector("#subRubroSelect option[selected]").getAttribute("value");
            if (currentSubRubroId) {
                subRubroSelect.value = currentSubRubroId;
            }
        }
    }

    // Filtrar subrubros al cargar la página
    filtrarSubRubros();

    // Filtrar subrubros cuando cambie el rubro
    rubroSelect.addEventListener("change", filtrarSubRubros);
});

// Al escanear, mover el foco al siguiente input    
document.getElementById("codigo").addEventListener("keydown", function(event) {
    // Verificamos si la tecla presionada es "Enter" (código 13)
    if (event.key === "Enter" || event.keyCode === 13) {
        event.preventDefault(); // Prevenir que el "Enter" envíe el formulario

        // Mover el foco al siguiente campo de entrada (simulando "Tab")
        const siguienteInput = document.getElementById("descripcion"); // Aquí especificas el siguiente input
        if (siguienteInput) {
            siguienteInput.focus(); // Hacer foco en el siguiente input
        }
    }
});
