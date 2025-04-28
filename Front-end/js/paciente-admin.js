const API_URL = "http://localhost:8085/api/v1/paciente";

/* ==========================
   Funciones de Utilidad
========================== */
function showAlert(message, type = "info") {
  alert(type === "error" ? `Error: ${message}` : message);
}

/* ==========================
   Listado y Filtrado de Pacientes
========================== */
async function loadPacienteTable() {
  try {
    const response = await fetch(API_URL);
    if (!response.ok) throw new Error("Error al cargar pacientes");
    const pacientes = await response.json();
    populatePacienteTable(pacientes);
  } catch (error) {
    console.error(error);
    showAlert("Error al cargar pacientes", "error");
  }
}

function populatePacienteTable(pacientes) {
  const tableBody = document.getElementById("pacienteTableBody");
  tableBody.innerHTML = "";
  if (!pacientes || pacientes.length === 0) {
    tableBody.innerHTML = `<tr><td colspan="4" class="text-center">No hay pacientes registrados.</td></tr>`;
    return;
  }
  pacientes.forEach(paciente => {
    const tr = document.createElement("tr");
    tr.innerHTML = `
      <td>${paciente.nombre}</td>
      <td>${paciente.fechaNacimiento}</td>
      <td>${paciente.direccion}</td>
      <td>
        <button class="btn btn-sm btn-primary" onclick="openEditPacienteModal(${paciente.id})">Editar</button>
        <button class="btn btn-sm btn-danger" onclick="deletePaciente(${paciente.id})">Eliminar</button>
      </td>
    `;
    tableBody.appendChild(tr);
  });
}

async function filterPacientes() {
  // Obtiene los filtros por nombre, dirección y fecha
  const nameFilter = document.getElementById("filterName").value.trim().toLowerCase();
  const direccionFilter = document.getElementById("filterDireccion").value.trim().toLowerCase();
  const fechaFilter = document.getElementById("filterFechaNacimiento").value.trim();
  
  try {
    const response = await fetch(API_URL);
    if (!response.ok) throw new Error("Error al cargar pacientes");
    const pacientes = await response.json();
    
    // Filtrar pacientes:
    const filtered = pacientes.filter(paciente => {
      let nameMatch = true, direccionMatch = true, fechaMatch = true;
      if (nameFilter) {
        nameMatch = paciente.nombre.toLowerCase().includes(nameFilter);
      }
      if (direccionFilter) {
        direccionMatch = paciente.direccion.toLowerCase().includes(direccionFilter);
      }
      if (fechaFilter) {
        // Se espera que la fecha esté en formato "YYYY-MM-DD"
        fechaMatch = paciente.fechaNacimiento.includes(fechaFilter);
      }
      return nameMatch && direccionMatch && fechaMatch;
    });
    
    populatePacienteTable(filtered);
  } catch (error) {
    console.error(error);
    showAlert("Error al filtrar pacientes", "error");
  }
}

/* ==========================
   Registro de Paciente
========================== */
async function createPaciente(event) {
  event.preventDefault();
  const nombre = document.getElementById("registerPacienteNombre").value.trim();
  const fechaNacimiento = document.getElementById("registerPacienteFechaNacimiento").value.trim();
  const direccion = document.getElementById("registerPacienteDireccion").value.trim();

  if (!nombre || !fechaNacimiento || !direccion) {
    showAlert("Todos los campos son obligatorios", "error");
    return;
  }
  
  const nacimientoDate = new Date(fechaNacimiento);
  const today = new Date();
  if (nacimientoDate > today) {
    showAlert("La fecha de nacimiento no puede ser futura", "error");
    return;
  }
  
  try {
    const response = await fetch(API_URL, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({
        id: 0,
        nombre: nombre,
        fechaNacimiento: fechaNacimiento,
        direccion: direccion
      })
    });
    if (!response.ok) {
      const errMsg = await response.text();
      showAlert(errMsg, "error");
      return;
    }
    showAlert("Paciente registrado exitosamente");
    document.getElementById("registerPacienteForm").reset();
    loadPacienteTable();
  } catch (error) {
    console.error(error);
    showAlert("Error al registrar paciente", "error");
  }
}

/* ==========================
   Eliminación de Paciente
========================== */
async function deletePaciente(id) {
  // Muestra mensaje de confirmación extendido: se eliminarán también las citas asignadas
  if (!confirm("¿Está seguro de eliminar este paciente? Al hacerlo, se eliminarán todas las citas asignadas a este paciente.")) return;
  try {
    const response = await fetch(`${API_URL}/${id}`, { method: "DELETE" });
    if (!response.ok) {
      const errMsg = await response.text();
      showAlert(errMsg, "error");
      return;
    }
    showAlert("Paciente eliminado correctamente. Todas las citas asignadas serán eliminadas.");
    loadPacienteTable();
  } catch (error) {
    console.error(error);
    showAlert("Error al eliminar paciente", "error");
  }
}

/* ==========================
   Edición de Paciente (Modal)
========================== */
async function openEditPacienteModal(id) {
  try {
    const response = await fetch(`${API_URL}/${id}`);
    if (!response.ok) {
      showAlert("Error al obtener datos del paciente", "error");
      return;
    }
    const paciente = await response.json();
    // Asigna los valores actuales al modal
    document.getElementById("editPacienteId").value = paciente.id;
    document.getElementById("editPacienteNombre").value = paciente.nombre;
    document.getElementById("editPacienteFechaNacimiento").value = paciente.fechaNacimiento;
    document.getElementById("editPacienteDireccion").value = paciente.direccion;
    const modalEl = document.getElementById("editPacienteModal");
    const modal = new bootstrap.Modal(modalEl);
    modal.show();
  } catch (error) {
    console.error(error);
    showAlert("Error al cargar paciente", "error");
  }
}

async function updatePaciente(event) {
  event.preventDefault();
  const id = document.getElementById("editPacienteId").value;
  const nombre = document.getElementById("editPacienteNombre").value.trim();
  const fechaNacimiento = document.getElementById("editPacienteFechaNacimiento").value.trim();
  const direccion = document.getElementById("editPacienteDireccion").value.trim();

  if (!nombre || !fechaNacimiento || !direccion) {
    showAlert("Todos los campos son obligatorios", "error");
    return;
  }
  
  const nacimientoDate = new Date(fechaNacimiento);
  const today = new Date();
  if (nacimientoDate > today) {
    showAlert("La fecha de nacimiento no puede ser futura", "error");
    return;
  }
  
  try {
    const response = await fetch(`${API_URL}/${id}`, {
      method: "PUT",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({
        nombre: nombre,
        fechaNacimiento: fechaNacimiento,
        direccion: direccion
      })
    });
    if (!response.ok) {
      const errMsg = await response.text();
      showAlert(errMsg, "error");
      return;
    }
    showAlert("Paciente actualizado correctamente");
    const modalEl = document.getElementById("editPacienteModal");
    const modal = bootstrap.Modal.getInstance(modalEl);
    modal.hide();
    loadPacienteTable();
  } catch (error) {
    console.error(error);
    showAlert("Error al actualizar paciente", "error");
  }
}

/* ==========================
   Función para Borrar Filtros
========================== */
function clearPacienteFilters() {
  document.getElementById("filterName").value = "";
  document.getElementById("filterDireccion").value = "";
  document.getElementById("filterFechaNacimiento").value = "";
  loadPacienteTable();
}

/* ==========================
   Eventos al Cargar el DOM
========================== */
document.addEventListener("DOMContentLoaded", () => {
  loadPacienteTable();
  document.getElementById("filterName").addEventListener("keyup", filterPacientes);
  document.getElementById("filterDireccion").addEventListener("keyup", filterPacientes);
  document.getElementById("filterFechaNacimiento").addEventListener("change", filterPacientes);
  document.getElementById("registerPacienteForm").addEventListener("submit", createPaciente);
  document.getElementById("editPacienteForm").addEventListener("submit", updatePaciente);
});
