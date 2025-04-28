const API_URL = "http://localhost:8085/api/v1/tratamiento";

// Caché para almacenar los tratamientos cargados
let tratamientosCache = [];

/* ==========================
   Funciones de Utilidad
========================== */
function showAlert(message, type = "info") {
  // Puedes reemplazar este alert básico por notificaciones más sofisticadas.
  alert(type === "error" ? `Error: ${message}` : message);
}

/* ==========================
   Listado y Filtrado de Tratamientos
========================== */
async function loadTratamientoTable() {
  try {
    const response = await fetch(API_URL);
    if (!response.ok) {
      throw new Error("Error al cargar los tratamientos");
    }
    tratamientosCache = await response.json();
    populateTratamientoTable(tratamientosCache);
  } catch (error) {
    console.error(error);
    showAlert("Error al cargar los tratamientos", "error");
  }
}

function populateTratamientoTable(tratamientos) {
  const tableBody = document.getElementById("tratamientoTableBody");
  if (!tableBody) return;

  tableBody.innerHTML = "";
  if (!tratamientos || tratamientos.length === 0) {
    tableBody.innerHTML = `<tr><td colspan="3" class="text-center">No hay tratamientos registrados.</td></tr>`;
    return;
  }
  tratamientos.forEach(tratamiento => {
    const tr = document.createElement("tr");
    tr.innerHTML = `
      <td>${tratamiento.nombre}</td>
      <td>${tratamiento.descripcion}</td>
      <td>
        <button class="btn btn-sm btn-primary" onclick="openEditTratamientoModal(${tratamiento.id})">Editar</button>
        <button class="btn btn-sm btn-danger" onclick="deleteTratamiento(${tratamiento.id})">Eliminar</button>
      </td>
    `;
    tableBody.appendChild(tr);
  });
}

function filterTratamientos() {
  const filterNombre = document.getElementById("filterTratamientoNombre")?.value.trim().toLowerCase() || "";
  const filterDescripcion = document.getElementById("filterTratamientoDescripcion")?.value.trim().toLowerCase() || "";

  const filtered = tratamientosCache.filter(tratamiento => {
    const nombreMatch = tratamiento.nombre.toLowerCase().includes(filterNombre);
    const descripcionMatch = tratamiento.descripcion.toLowerCase().includes(filterDescripcion);
    return nombreMatch && descripcionMatch;
  });
  populateTratamientoTable(filtered);
}

function clearTratamientoFilters() {
  if (document.getElementById("filterTratamientoNombre"))
    document.getElementById("filterTratamientoNombre").value = "";
  if (document.getElementById("filterTratamientoDescripcion"))
    document.getElementById("filterTratamientoDescripcion").value = "";
  loadTratamientoTable();
}

/* ==========================
   Registro de Tratamiento
========================== */
async function createTratamiento(event) {
  event.preventDefault();
  const nombre = document.getElementById("registerTratamientoNombre")?.value.trim();
  const descripcion = document.getElementById("registerTratamientoDescripcion")?.value.trim();

  if (!nombre || !descripcion) {
    showAlert("Todos los campos son obligatorios", "error");
    return;
  }
  
  try {
    // En el POST enviamos únicamente los campos nombre y descripción.
    const response = await fetch(API_URL, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({
        nombre: nombre,
        descripcion: descripcion
      })
    });
    if (!response.ok) {
      const errorMsg = await response.text();
      showAlert(errorMsg, "error");
      return;
    }
    showAlert("Tratamiento registrado exitosamente");
    document.getElementById("registerTratamientoForm")?.reset();
    loadTratamientoTable();
  } catch (error) {
    console.error(error);
    showAlert("Error al registrar el tratamiento", "error");
  }
}

/* ==========================
   Eliminación de Tratamiento
========================== */
async function deleteTratamiento(id) {
  if (!confirm("¿Está seguro de eliminar este tratamiento?")) return;
  try {
    const response = await fetch(`${API_URL}/${id}`, { method: "DELETE" });
    if (!response.ok) {
      const errorMsg = await response.text();
      showAlert(errorMsg, "error");
      return;
    }
    showAlert("Tratamiento eliminado correctamente");
    loadTratamientoTable();
  } catch (error) {
    console.error(error);
    showAlert("Error al eliminar el tratamiento", "error");
  }
}

/* ==========================
   Edición de Tratamiento (Modal)
========================== */
async function openEditTratamientoModal(id) {
  try {
    const response = await fetch(`${API_URL}/${id}`);
    if (!response.ok) {
      showAlert("Error al obtener datos del tratamiento", "error");
      return;
    }
    const tratamiento = await response.json();
    document.getElementById("editTratamientoId").value = tratamiento.id;
    document.getElementById("editTratamientoNombre").value = tratamiento.nombre;
    document.getElementById("editTratamientoDescripcion").value = tratamiento.descripcion;
    
    const modalEl = document.getElementById("editTratamientoModal");
    const modal = new bootstrap.Modal(modalEl);
    modal.show();
  } catch (error) {
    console.error(error);
    showAlert("Error al cargar el tratamiento", "error");
  }
}

async function updateTratamiento(event) {
  event.preventDefault();
  const id = document.getElementById("editTratamientoId")?.value;
  const nombre = document.getElementById("editTratamientoNombre")?.value.trim();
  const descripcion = document.getElementById("editTratamientoDescripcion")?.value.trim();

  if (!nombre || !descripcion) {
    showAlert("Todos los campos son obligatorios", "error");
    return;
  }
  
  try {
    const response = await fetch(`${API_URL}/${id}`, {
      method: "PUT",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({
        nombre: nombre,
        descripcion: descripcion
      })
    });
    if (!response.ok) {
      const errorMsg = await response.text();
      showAlert(errorMsg, "error");
      return;
    }
    showAlert("Tratamiento actualizado correctamente");
    const modalEl = document.getElementById("editTratamientoModal");
    const modal = bootstrap.Modal.getInstance(modalEl);
    modal.hide();
    loadTratamientoTable();
  } catch (error) {
    console.error(error);
    showAlert("Error al actualizar el tratamiento", "error");
  }
}

/* ==========================
   Eventos al Cargar el DOM
========================== */
document.addEventListener("DOMContentLoaded", () => {
  if (document.getElementById("tratamientoTableBody")) {
    loadTratamientoTable();
  }
  const filterNombreElem = document.getElementById("filterTratamientoNombre");
  if (filterNombreElem) filterNombreElem.addEventListener("keyup", filterTratamientos);

  const filterDescripcionElem = document.getElementById("filterTratamientoDescripcion");
  if (filterDescripcionElem) filterDescripcionElem.addEventListener("keyup", filterTratamientos);

  const regForm = document.getElementById("registerTratamientoForm");
  if (regForm) regForm.addEventListener("submit", createTratamiento);

  const editForm = document.getElementById("editTratamientoForm");
  if (editForm) editForm.addEventListener("submit", updateTratamiento);
});
