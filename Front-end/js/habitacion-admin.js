const API_URL = "http://localhost:8085/api/v1/habitacion";

// Caché para almacenar las habitaciones cargadas
let habitacionesCache = [];

/* ==========================
   Funciones de Utilidad
========================== */
function showAlert(message, type = "info") {
  alert(type === "error" ? `Error: ${message}` : message);
}

/* ==========================
   Listado y Filtrado de Habitaciones
========================== */
async function loadHabitacionTable() {
  try {
    const response = await fetch(API_URL);
    if (!response.ok) {
      throw new Error("Error al cargar las habitaciones");
    }
    habitacionesCache = await response.json();
    populateHabitacionTable(habitacionesCache);
  } catch (error) {
    console.error(error);
    showAlert("Error al cargar las habitaciones", "error");
  }
}

function populateHabitacionTable(habitaciones) {
  const tableBody = document.getElementById("habitacionTableBody");
  if (!tableBody) return;
  
  tableBody.innerHTML = "";
  if (!habitaciones || habitaciones.length === 0) {
    tableBody.innerHTML = `<tr><td colspan="4" class="text-center">No hay habitaciones registradas.</td></tr>`;
    return;
  }
  habitaciones.forEach(habitacion => {
    const tr = document.createElement("tr");
    tr.innerHTML = `
      <td>${habitacion.numero}</td>
      <td>${habitacion.descripcion}</td>
      <td>${habitacion.capacidad}</td>
      <td>
        <button class="btn btn-sm btn-primary" onclick="openEditHabitacionModal(${habitacion.id})">Editar</button>
        <button class="btn btn-sm btn-danger" onclick="deleteHabitacion(${habitacion.id})">Eliminar</button>
      </td>
    `;
    tableBody.appendChild(tr);
  });
}

function filterHabitaciones() {
  const filterNumero = document.getElementById("filterHabitacionNumero")?.value.trim().toLowerCase() || "";
  const filterDescripcion = document.getElementById("filterHabitacionDescripcion")?.value.trim().toLowerCase() || "";
  const filterCapacidad = document.getElementById("filterHabitacionCapacidad")?.value.trim() || "";
  
  const filtered = habitacionesCache.filter(habitacion => {
    const numeroMatch = habitacion.numero.toLowerCase().includes(filterNumero);
    const descripcionMatch = habitacion.descripcion.toLowerCase().includes(filterDescripcion);
    const capacidadMatch = filterCapacidad ? habitacion.capacidad === parseInt(filterCapacidad) : true;
    return numeroMatch && descripcionMatch && capacidadMatch;
  });
  populateHabitacionTable(filtered);
}

function clearHabitacionFilters() {
  if(document.getElementById("filterHabitacionNumero"))
    document.getElementById("filterHabitacionNumero").value = "";
  if(document.getElementById("filterHabitacionDescripcion"))
    document.getElementById("filterHabitacionDescripcion").value = "";
  if(document.getElementById("filterHabitacionCapacidad"))
    document.getElementById("filterHabitacionCapacidad").value = "";
  loadHabitacionTable();
}

/* ==========================
   Registro de Habitación
========================== */
async function createHabitacion(event) {
  event.preventDefault();
  const numero = document.getElementById("registerHabitacionNumero")?.value.trim();
  const descripcion = document.getElementById("registerHabitacionDescripcion")?.value.trim();
  const capacidad = document.getElementById("registerHabitacionCapacidad")?.value.trim();
  
  if (!numero || !descripcion || !capacidad) {
    showAlert("Todos los campos son obligatorios", "error");
    return;
  }
  if (parseInt(capacidad) <= 0) {
    showAlert("La capacidad debe ser un número positivo", "error");
    return;
  }
  
  try {
    const response = await fetch(API_URL, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({
        // Se envían solo los campos requeridos
        numero: numero,
        descripcion: descripcion,
        capacidad: parseInt(capacidad)
      })
    });
    if (!response.ok) {
      const errorMsg = await response.text();
      showAlert(errorMsg, "error");
      return;
    }
    showAlert("Habitación registrada exitosamente");
    document.getElementById("registerHabitacionForm")?.reset();
    loadHabitacionTable();
  } catch (error) {
    console.error(error);
    showAlert("Error al registrar la habitación", "error");
  }
}

/* ==========================
   Eliminación de Habitación
========================== */
async function deleteHabitacion(id) {
  if (!confirm("¿Está seguro de eliminar esta habitación?")) return;
  try {
    const response = await fetch(`${API_URL}/${id}`, { method: "DELETE" });
    if (!response.ok) {
      const errorMsg = await response.text();
      showAlert(errorMsg, "error");
      return;
    }
    showAlert("Habitación eliminada correctamente");
    loadHabitacionTable();
  } catch (error) {
    console.error(error);
    showAlert("Error al eliminar la habitación", "error");
  }
}

/* ==========================
   Edición de Habitación (Modal)
========================== */
async function openEditHabitacionModal(id) {
  try {
    const response = await fetch(`${API_URL}/${id}`);
    if (!response.ok) {
      showAlert("Error al obtener datos de la habitación", "error");
      return;
    }
    const habitacion = await response.json();
    document.getElementById("editHabitacionId").value = habitacion.id;
    document.getElementById("editHabitacionNumero").value = habitacion.numero;
    document.getElementById("editHabitacionDescripcion").value = habitacion.descripcion;
    document.getElementById("editHabitacionCapacidad").value = habitacion.capacidad;
    
    const modalEl = document.getElementById("editHabitacionModal");
    const modal = new bootstrap.Modal(modalEl);
    modal.show();
  } catch (error) {
    console.error(error);
    showAlert("Error al cargar la habitación", "error");
  }
}

async function updateHabitacion(event) {
  event.preventDefault();
  const id = document.getElementById("editHabitacionId")?.value;
  const numero = document.getElementById("editHabitacionNumero")?.value.trim();
  const descripcion = document.getElementById("editHabitacionDescripcion")?.value.trim();
  const capacidad = document.getElementById("editHabitacionCapacidad")?.value.trim();
  
  if (!numero || !descripcion || !capacidad) {
    showAlert("Todos los campos son obligatorios", "error");
    return;
  }
  if (parseInt(capacidad) <= 0) {
    showAlert("La capacidad debe ser un número positivo", "error");
    return;
  }
  try {
    const response = await fetch(`${API_URL}/${id}`, {
      method: "PUT",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({
        numero: numero,
        descripcion: descripcion,
        capacidad: parseInt(capacidad)
      })
    });
    if (!response.ok) {
      const errorMsg = await response.text();
      showAlert(errorMsg, "error");
      return;
    }
    showAlert("Habitación actualizada correctamente");
    const modalEl = document.getElementById("editHabitacionModal");
    const modal = bootstrap.Modal.getInstance(modalEl);
    modal.hide();
    loadHabitacionTable();
  } catch (error) {
    console.error(error);
    showAlert("Error al actualizar la habitación", "error");
  }
}

/* ==========================
   Eventos al Cargar el DOM
========================== */
document.addEventListener("DOMContentLoaded", () => {
  if (document.getElementById("habitacionTableBody")) {
    loadHabitacionTable();
  }
  const filterNumeroElem = document.getElementById("filterHabitacionNumero");
  if (filterNumeroElem) filterNumeroElem.addEventListener("keyup", filterHabitaciones);
  
  const filterDescripcionElem = document.getElementById("filterHabitacionDescripcion");
  if (filterDescripcionElem) filterDescripcionElem.addEventListener("keyup", filterHabitaciones);
  
  const filterCapacidadElem = document.getElementById("filterHabitacionCapacidad");
  if (filterCapacidadElem) filterCapacidadElem.addEventListener("keyup", filterHabitaciones);
  
  const regForm = document.getElementById("registerHabitacionForm");
  if (regForm) regForm.addEventListener("submit", createHabitacion);
  
  const editForm = document.getElementById("editHabitacionForm");
  if (editForm) editForm.addEventListener("submit", updateHabitacion);
});
