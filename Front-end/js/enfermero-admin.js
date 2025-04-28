const API_URL = "http://localhost:8085/api/v1/enfermero";

// Función debounce para optimizar la ejecución de funciones al teclear
function debounce(func, delay) {
  let timer;
  return function(...args) {
    clearTimeout(timer);
    timer = setTimeout(() => func.apply(this, args), delay);
  };
}

/* ==========================
   Funciones de Utilidad
========================== */
function showAlert(message, type = "info") {
  alert(type === "error" ? `Error: ${message}` : message);
}

/* ==========================
   Listado y Filtrado de Enfermeros
========================== */
async function loadEnfermeroTable() {
  try {
    const response = await fetch(API_URL);
    if (!response.ok) throw new Error("Error al cargar enfermeros");
    const enfermeros = await response.json();
    populateEnfermeroTable(enfermeros);
  } catch (error) {
    console.error(error);
    showAlert("Error al cargar enfermeros", "error");
  }
}

function populateEnfermeroTable(enfermeros) {
  const tableBody = document.getElementById("enfermeroTableBody");
  tableBody.innerHTML = "";
  if (!enfermeros || enfermeros.length === 0) {
    tableBody.innerHTML = `<tr><td colspan="4" class="text-center">No hay enfermeros registrados.</td></tr>`;
    return;
  }
  enfermeros.forEach(enfermero => {
    const tr = document.createElement("tr");
    tr.innerHTML = `
      <td>${enfermero.nombre}</td>
      <td>${enfermero.turno}</td>
      <td>${enfermero.aniosExperiencia}</td>
      <td>
        <button class="btn btn-sm btn-primary" onclick="openEditEnfermeroModal(${enfermero.id})">Editar</button>
        <button class="btn btn-sm btn-danger" onclick="deleteEnfermero(${enfermero.id})">Eliminar</button>
      </td>
    `;
    tableBody.appendChild(tr);
  });
}

function filterEnfermeros() {
  const filterNombre = document.getElementById("filterEnfermeroNombre").value.trim().toLowerCase();
  const filterTurno = document.getElementById("filterEnfermeroTurno").value.trim().toLowerCase();
  const filterAnios = document.getElementById("filterEnfermeroAnios").value.trim();

  // Primero cargamos la lista completa desde el API para filtrar de forma local
  fetch(API_URL)
    .then(response => {
      if (!response.ok) throw new Error("Error al cargar enfermeros");
      return response.json();
    })
    .then(enfermeros => {
      const filtered = enfermeros.filter(enfermero => {
        let nameMatch = true;
        let turnoMatch = true;
        let experienceMatch = true;
        if (filterNombre) {
          nameMatch = enfermero.nombre.toLowerCase().includes(filterNombre);
        }
        if (filterTurno) {
          turnoMatch = enfermero.turno.toLowerCase().includes(filterTurno);
        }
        if (filterAnios) {
          // Comparamos de forma exacta el número de años de experiencia
          experienceMatch = enfermero.aniosExperiencia === parseInt(filterAnios);
        }
        return nameMatch && turnoMatch && experienceMatch;
      });
      populateEnfermeroTable(filtered);
    })
    .catch(error => {
      console.error(error);
      showAlert("Error al filtrar enfermeros", "error");
    });
}

function clearEnfermeroFilters() {
  document.getElementById("filterEnfermeroNombre").value = "";
  document.getElementById("filterEnfermeroTurno").value = "";
  document.getElementById("filterEnfermeroAnios").value = "";
  loadEnfermeroTable();
}

/* ==========================
   Registro de Enfermero
========================== */
async function createEnfermero(event) {
  event.preventDefault();
  const nombre = document.getElementById("registerEnfermeroNombre").value.trim();
  const turno = document.getElementById("registerEnfermeroTurno").value.trim();
  const aniosExperiencia = document.getElementById("registerEnfermeroAnios").value.trim();

  if (!nombre || !turno || aniosExperiencia === "") {
    showAlert("Todos los campos son obligatorios", "error");
    return;
  }
  if (parseInt(aniosExperiencia) < 0) {
    showAlert("Los años de experiencia deben ser 0 o mayores", "error");
    return;
  }
  try {
    const response = await fetch(API_URL, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({
        id: 0,
        nombre: nombre,
        turno: turno,
        aniosExperiencia: parseInt(aniosExperiencia)
      })
    });
    if (!response.ok) {
      const errorMsg = await response.text();
      showAlert(errorMsg, "error");
      return;
    }
    showAlert("Enfermero registrado exitosamente");
    document.getElementById("registerEnfermeroForm").reset();
    loadEnfermeroTable();
  } catch (error) {
    console.error(error);
    showAlert("Error al registrar enfermero", "error");
  }
}

/* ==========================
   Eliminación de Enfermero
========================== */
async function deleteEnfermero(id) {
  if (!confirm("¿Está seguro de eliminar este enfermero?")) return;
  try {
    const response = await fetch(`${API_URL}/${id}`, { method: "DELETE" });
    if (!response.ok) {
      const errorMsg = await response.text();
      showAlert(errorMsg, "error");
      return;
    }
    showAlert("Enfermero eliminado correctamente");
    loadEnfermeroTable();
  } catch (error) {
    console.error(error);
    showAlert("Error al eliminar enfermero", "error");
  }
}

/* ==========================
   Edición de Enfermero (Modal)
========================== */
async function openEditEnfermeroModal(id) {
  try {
    const response = await fetch(`${API_URL}/${id}`);
    if (!response.ok) {
      showAlert("Error al obtener datos del enfermero", "error");
      return;
    }
    const enfermero = await response.json();
    document.getElementById("editEnfermeroId").value = enfermero.id;
    document.getElementById("editEnfermeroNombre").value = enfermero.nombre;
    document.getElementById("editEnfermeroTurno").value = enfermero.turno;
    document.getElementById("editEnfermeroAnios").value = enfermero.aniosExperiencia;

    const modalEl = document.getElementById("editEnfermeroModal");
    const modal = new bootstrap.Modal(modalEl);
    modal.show();
  } catch (error) {
    console.error(error);
    showAlert("Error al cargar el enfermero", "error");
  }
}

async function updateEnfermero(event) {
  event.preventDefault();
  const id = document.getElementById("editEnfermeroId").value;
  const nombre = document.getElementById("editEnfermeroNombre").value.trim();
  const turno = document.getElementById("editEnfermeroTurno").value.trim();
  const aniosExperiencia = document.getElementById("editEnfermeroAnios").value.trim();

  if (!nombre || !turno || aniosExperiencia === "") {
    showAlert("Todos los campos son obligatorios", "error");
    return;
  }
  if (parseInt(aniosExperiencia) < 0) {
    showAlert("Los años de experiencia deben ser 0 o mayores", "error");
    return;
  }
  try {
    const response = await fetch(`${API_URL}/${id}`, {
      method: "PUT",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({
        nombre: nombre,
        turno: turno,
        aniosExperiencia: parseInt(aniosExperiencia)
      })
    });
    if (!response.ok) {
      const errorMsg = await response.text();
      showAlert(errorMsg, "error");
      return;
    }
    showAlert("Enfermero actualizado correctamente");
    const modalEl = document.getElementById("editEnfermeroModal");
    const modal = bootstrap.Modal.getInstance(modalEl);
    modal.hide();
    loadEnfermeroTable();
  } catch (error) {
    console.error(error);
    showAlert("Error al actualizar enfermero", "error");
  }
}

/* ==========================
   Eventos al Cargar el DOM
========================== */
document.addEventListener("DOMContentLoaded", () => {
  loadEnfermeroTable();
  
  // Configuramos un debounce de 300ms para el filtrado
  const debouncedFilter = debounce(filterEnfermeros, 300);
  
  document.getElementById("filterEnfermeroNombre").addEventListener("keyup", debouncedFilter);
  document.getElementById("filterEnfermeroTurno").addEventListener("keyup", debouncedFilter);
  // Usamos el evento "input" para el campo numérico para filtrar sin esperar Enter
  document.getElementById("filterEnfermeroAnios").addEventListener("input", debouncedFilter);
  
  document.getElementById("registerEnfermeroForm").addEventListener("submit", createEnfermero);
  document.getElementById("editEnfermeroForm").addEventListener("submit", updateEnfermero);
});
