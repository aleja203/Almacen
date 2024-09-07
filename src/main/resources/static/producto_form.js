                // Mostrar subrubros de acuerdo a rubro seleciconado

                    document.addEventListener("DOMContentLoaded", function () {
                        const rubroSelect = document.getElementById("rubroSelect");
                        const subRubroSelect = document.getElementById("subRubroSelect");
                        const subRubroOptions = Array.from(subRubroSelect.options); // Guardamos todas las opciones de subrubro

                        // Evento que se dispara al cambiar el rubro seleccionado
                        rubroSelect.addEventListener("change", function () {
                            const selectedRubroId = this.value;

                            // Limpiamos las opciones del select de subrubro (dejando solo el 'Seleccionar Subrubro')
                            subRubroSelect.innerHTML = '<option value="">Seleccionar Subrubro</option>';

                            // Filtrar y agregar solo las opciones que coincidan con el rubro seleccionado
                            subRubroOptions.forEach(option => {
                                const rubroId = option.getAttribute("data-rubro-id");
                                if (rubroId === selectedRubroId) {
                                    subRubroSelect.appendChild(option); // Mostrar solo los subrubros correspondientes
                                }
                            });
                        });
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
                