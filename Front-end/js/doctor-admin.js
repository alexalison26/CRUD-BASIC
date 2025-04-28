const API_URL = "http://localhost:8085/api/v1/doctor";

/* ==========================
   Funciones de Utilidad
========================== */
function showAlert(message, type = "info") {
  alert(type === "error" ? `Error: ${message}` : message);
}

/* ==========================
   Listado y Filtrado de Doctores
========================== */
async function loadDoctorTable() {
  try {
    const response = await fetch(API_URL);
    if (!response.ok) throw new Error("Error al cargar doctores");
    const doctors = await response.json();
    populateDoctorTable(doctors);
  } catch (error) {
    console.error(error);
    showAlert("Error al cargar doctores", "error");
  }
}

function populateDoctorTable(doctors) {
  const tableBody = document.getElementById("doctorTableBody");
  tableBody.innerHTML = "";
  if (!doctors || doctors.length === 0) {
    tableBody.innerHTML = `<tr><td colspan="4" class="text-center">No hay doctores registrados.</td></tr>`;
    return;
  }
  doctors.forEach(doctor => {
    const tr = document.createElement("tr");
    tr.innerHTML = `
      <td>${doctor.nombre}</td>
      <td>${doctor.especialidad}</td>
      <td>${doctor.telefono}</td>
      <td>
        <button class="btn btn-sm btn-primary" onclick="openEditDoctorModal(${doctor.id})">Editar</button>
        <button class="btn btn-sm btn-danger" onclick="deleteDoctor(${doctor.id})">Eliminar</button>
      </td>
    `;
    tableBody.appendChild(tr);
  });
}

async function filterDoctores() {
  const filterNombre = document.getElementById("filterDoctorNombre").value.trim().toLowerCase();
  const filterEspecialidad = document.getElementById("filterDoctorEspecialidad").value.trim().toLowerCase();
  const filterTelefono = document.getElementById("filterDoctorTelefono").value.trim().toLowerCase();
  
  try {
    const response = await fetch(API_URL);
    if (!response.ok) throw new Error("Error al cargar doctores");
    const doctors = await response.json();
    const filtered = doctors.filter(doctor => {
      let matchNombre = true, matchEspecialidad = true, matchTelefono = true;
      if (filterNombre) {
        matchNombre = doctor.nombre.toLowerCase().includes(filterNombre);
      }
      if (filterEspecialidad) {
        matchEspecialidad = doctor.especialidad.toLowerCase().includes(filterEspecialidad);
      }
      if (filterTelefono) {
        matchTelefono = doctor.telefono.toLowerCase().includes(filterTelefono);
      }
      return matchNombre && matchEspecialidad && matchTelefono;
    });
    populateDoctorTable(filtered);
  } catch (error) {
    console.error(error);
    showAlert("Error al filtrar doctores", "error");
  }
}

/* ==========================
   Registro de Doctor
========================== */
async function createDoctor(event) {
  event.preventDefault();
  const nombre = document.getElementById("registerDoctorNombre").value.trim();
  const especialidad = document.getElementById("registerDoctorEspecialidad").value.trim();
  const telefono = document.getElementById("registerDoctorTelefono").value.trim();
  
  if (!nombre || !especialidad || !telefono) {
    showAlert("Todos los campos son obligatorios", "error");
    return;
  }
  try {
    const response = await fetch(API_URL, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({
        id: 0,
        nombre: nombre,
        especialidad: especialidad,
        telefono: telefono
      })
    });
    if (!response.ok) {
      const errMsg = await response.text();
      showAlert(errMsg, "error");
      return;
    }
    showAlert("Doctor registrado exitosamente");
    document.getElementById("registerDoctorForm").reset();
    loadDoctorTable();
  } catch (error) {
    console.error(error);
    showAlert("Error al registrar doctor", "error");
  }
}

/* ==========================
   Eliminación de Doctor
========================== */
async function deleteDoctor(id) {
  // Muestra un mensaje de confirmación extendido
  if (!confirm("¿Está seguro de eliminar este doctor? Al hacerlo, se eliminarán todas las citas asignadas a este doctor.")) return;
  try {
    const response = await fetch(`${API_URL}/${id}`, { method: "DELETE" });
    if (!response.ok) {
      const errMsg = await response.text();
      showAlert(errMsg, "error");
      return;
    }
    showAlert("Doctor eliminado correctamente");
    loadDoctorTable();
  } catch (error) {
    console.error(error);
    showAlert("Error al eliminar doctor", "error");
  }
}

/* ==========================
   Edición de Doctor (Modal)
========================== */
async function openEditDoctorModal(id) {
  try {
    const response = await fetch(`${API_URL}/${id}`);
    if (!response.ok) {
      showAlert("Error al obtener datos del doctor", "error");
      return;
    }
    const doctor = await response.json();
    document.getElementById("editDoctorId").value = doctor.id;
    document.getElementById("editDoctorNombre").value = doctor.nombre;
    document.getElementById("editDoctorEspecialidad").value = doctor.especialidad;
    document.getElementById("editDoctorTelefono").value = doctor.telefono;
    const modalEl = document.getElementById("editDoctorModal");
    const modal = new bootstrap.Modal(modalEl);
    modal.show();
  } catch (error) {
    console.error(error);
    showAlert("Error al cargar el doctor", "error");
  }
}

async function updateDoctor(event) {
  event.preventDefault();
  const id = document.getElementById("editDoctorId").value;
  const nombre = document.getElementById("editDoctorNombre").value.trim();
  const especialidad = document.getElementById("editDoctorEspecialidad").value.trim();
  const telefono = document.getElementById("editDoctorTelefono").value.trim();
  
  if (!nombre || !especialidad || !telefono) {
    showAlert("Todos los campos son obligatorios", "error");
    return;
  }
  try {
    const response = await fetch(`${API_URL}/${id}`, {
      method: "PUT",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({
        nombre: nombre,
        especialidad: especialidad,
        telefono: telefono
      })
    });
    if (!response.ok) {
      const errMsg = await response.text();
      showAlert(errMsg, "error");
      return;
    }
    showAlert("Doctor actualizado correctamente");
    const modalEl = document.getElementById("editDoctorModal");
    const modal = bootstrap.Modal.getInstance(modalEl);
    modal.hide();
    loadDoctorTable();
  } catch (error) {
    console.error(error);
    showAlert("Error al actualizar doctor", "error");
  }
}

/* ==========================
   Eventos al cargar el DOM
========================== */
document.addEventListener("DOMContentLoaded", () => {
  loadDoctorTable();
  document.getElementById("filterDoctorNombre").addEventListener("keyup", filterDoctores);
  document.getElementById("filterDoctorEspecialidad").addEventListener("keyup", filterDoctores);
  document.getElementById("filterDoctorTelefono").addEventListener("keyup", filterDoctores);
  document.getElementById("registerDoctorForm").addEventListener("submit", createDoctor);
  document.getElementById("editDoctorForm").addEventListener("submit", updateDoctor);
});
