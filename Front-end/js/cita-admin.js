const API_URL = "http://localhost:8085/api/v1/cita";
const PACIENTE_API = "http://localhost:8085/api/v1/paciente";
const DOCTOR_API = "http://localhost:8085/api/v1/doctor";

/* ==========================
   Funciones de Utilidad
========================== */
function showAlert(message, type = "info") {
  alert(type === "error" ? `Error: ${message}` : message);
}

/* ==========================
   Cargar opciones para selects (Pacientes y Doctores)
========================== */
function loadPacienteOptions(selectId) {
  fetch(PACIENTE_API)
    .then(response => response.json())
    .then(data => {
      let select = document.getElementById(selectId);
      if (data.length === 0) {
        select.innerHTML = "<option value=''>No hay pacientes</option>";
        select.disabled = true;
      } else {
        select.disabled = false;
        select.innerHTML = '<option value="">Seleccione Paciente</option>';
        data.forEach(paciente => {
          let option = document.createElement("option");
          option.value = paciente.id;
          option.textContent = paciente.nombre;
          select.appendChild(option);
        });
      }
    })
    .catch(err => console.error("Error al cargar pacientes: ", err));
}

function loadDoctorOptions(selectId) {
  fetch(DOCTOR_API)
    .then(response => response.json())
    .then(data => {
      let select = document.getElementById(selectId);
      if (data.length === 0) {
        select.innerHTML = "<option value=''>No hay doctores</option>";
        select.disabled = true;
      } else {
        select.disabled = false;
        select.innerHTML = '<option value="">Seleccione Doctor</option>';
        data.forEach(doctor => {
          let option = document.createElement("option");
          option.value = doctor.id;
          option.textContent = doctor.nombre;
          select.appendChild(option);
        });
      }
    })
    .catch(err => console.error("Error al cargar doctores: ", err));
}

/* ==========================
   Listado y Filtrado de Citas
========================== */
async function loadCitaTable() {
  try {
    const response = await fetch(API_URL);
    if (!response.ok) throw new Error("Error al cargar citas");
    const citas = await response.json();
    populateCitaTable(citas);
  } catch (error) {
    console.error(error);
    showAlert("Error al cargar citas", "error");
  }
}

function populateCitaTable(citas) {
  const tableBody = document.getElementById("citaTableBody");
  tableBody.innerHTML = "";
  if (!citas || citas.length === 0) {
    tableBody.innerHTML = `<tr><td colspan="5" class="text-center">No hay citas registradas.</td></tr>`;
    return;
  }
  citas.forEach(cita => {
    // Convertir fecha a formato local
    const fechaStr = new Date(cita.fecha).toLocaleString();
    const tr = document.createElement("tr");
    tr.innerHTML = `
      <td>${cita.nombrePaciente || cita.idPaciente}</td>
      <td>${cita.nombreDoctor || cita.idDoctor}</td>
      <td>${fechaStr}</td>
      <td>${cita.descripcion || ""}</td>
      <td>
        <button class="btn btn-sm btn-primary" onclick="openEditCitaModal(${cita.id})">Editar</button>
        <button class="btn btn-sm btn-danger" onclick="deleteCita(${cita.id})">Eliminar</button>
      </td>
    `;
    tableBody.appendChild(tr);
  });
}

async function filterCitas() {
  const filterDescripcion = document.getElementById("filterDescripcion").value.trim().toLowerCase();
  const filterPaciente = document.getElementById("filterPacienteSelect") ? 
                         document.getElementById("filterPacienteSelect").value.trim() : "";
  const filterDoctor = document.getElementById("filterDoctorSelect") ? 
                       document.getElementById("filterDoctorSelect").value.trim() : "";
  
  try {
    const response = await fetch(API_URL);
    if (!response.ok) throw new Error("Error al cargar citas");
    const citas = await response.json();
    const filtered = citas.filter(cita => {
      let descMatch = true, pacienteMatch = true, doctorMatch = true;
      if (filterDescripcion) {
        descMatch = (cita.descripcion || "").toLowerCase().includes(filterDescripcion);
      }
      if (filterPaciente) {
        pacienteMatch = (cita.idPaciente == filterPaciente);
      }
      if (filterDoctor) {
        doctorMatch = (cita.idDoctor == filterDoctor);
      }
      return descMatch && pacienteMatch && doctorMatch;
    });
    populateCitaTable(filtered);
  } catch (error) {
    console.error(error);
    showAlert("Error al filtrar citas", "error");
  }
}

// Función para borrar filtros: limpia los inputs y selects, y recarga la tabla
function clearCitaFilters() {
  document.getElementById("filterDescripcion").value = "";
  if (document.getElementById("filterPacienteSelect")) {
    document.getElementById("filterPacienteSelect").value = "";
  }
  if (document.getElementById("filterDoctorSelect")) {
    document.getElementById("filterDoctorSelect").value = "";
  }
  loadCitaTable();
}

/* ==========================
   Registro de Cita
========================== */
async function createCita(event) {
  event.preventDefault();
  const pacienteId = document.getElementById("registerCitaPacienteSelect").value.trim();
  const doctorId = document.getElementById("registerCitaDoctorSelect").value.trim();
  const fecha = document.getElementById("registerCitaFecha").value.trim();
  const descripcion = document.getElementById("registerCitaDescripcion").value.trim();
  
  if (!pacienteId || !doctorId || !fecha) {
    showAlert("Paciente, Doctor y Fecha son obligatorios", "error");
    return;
  }
  if (parseInt(pacienteId) <= 0 || parseInt(doctorId) <= 0) {
    showAlert("Los IDs deben ser positivos", "error");
    return;
  }
  const citaDate = new Date(fecha);
  const now = new Date();
  if (citaDate < now) {
    showAlert("La fecha de la cita no puede estar en el pasado", "error");
    return;
  }
  try {
    const response = await fetch(API_URL, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({
        id: 0,
        idPaciente: parseInt(pacienteId),
        idDoctor: parseInt(doctorId),
        fecha: fecha,
        descripcion: descripcion
      }),
    });
    if (!response.ok) {
      const errMsg = await response.text();
      showAlert(errMsg, "error");
      return;
    }
    showAlert("Cita registrada exitosamente");
    document.getElementById("registerCitaForm").reset();
    loadCitaTable();
  } catch (error) {
    console.error(error);
    showAlert("Error al registrar cita", "error");
  }
}

/* ==========================
   Eliminación de Cita
========================== */
async function deleteCita(id) {
  if (!confirm("¿Estás seguro de eliminar esta cita?")) return;
  try {
    const response = await fetch(`${API_URL}/${id}`, { method: "DELETE" });
    if (!response.ok) {
      const errMsg = await response.text();
      showAlert(errMsg, "error");
      return;
    }
    showAlert("Cita eliminada correctamente");
    loadCitaTable();
  } catch (error) {
    console.error(error);
    showAlert("Error al eliminar cita", "error");
  }
}

/* ==========================
   Edición de Cita (Modal)
========================== */
async function openEditCitaModal(id) {
  try {
    const response = await fetch(`${API_URL}/${id}`);
    if (!response.ok) {
      showAlert("Error al obtener datos de la cita", "error");
      return;
    }
    const cita = await response.json();
    document.getElementById("editCitaId").value = cita.id;
    document.getElementById("editCitaPacienteSelect").value = cita.idPaciente;
    document.getElementById("editCitaDoctorSelect").value = cita.idDoctor;
    const fechaLocal = cita.fecha.substring(0, 16);
    document.getElementById("editCitaFecha").value = fechaLocal;
    document.getElementById("editCitaDescripcion").value = cita.descripcion || "";
    const modalEl = document.getElementById("editCitaModal");
    const modal = new bootstrap.Modal(modalEl);
    modal.show();
  } catch (error) {
    console.error(error);
    showAlert("Error al cargar la cita", "error");
  }
}

async function updateCita(event) {
  event.preventDefault();
  const id = document.getElementById("editCitaId").value;
  const pacienteId = document.getElementById("editCitaPacienteSelect").value.trim();
  const doctorId = document.getElementById("editCitaDoctorSelect").value.trim();
  const fecha = document.getElementById("editCitaFecha").value.trim();
  const descripcion = document.getElementById("editCitaDescripcion").value.trim();
  
  if (!pacienteId || !doctorId || !fecha) {
    showAlert("Paciente, Doctor y Fecha son obligatorios", "error");
    return;
  }
  if (parseInt(pacienteId) <= 0 || parseInt(doctorId) <= 0) {
    showAlert("Los IDs deben ser positivos", "error");
    return;
  }
  const citaDate = new Date(fecha);
  const now = new Date();
  if (citaDate < now) {
    showAlert("La fecha de la cita no puede estar en el pasado", "error");
    return;
  }
  try {
    const response = await fetch(`${API_URL}/${id}`, {
      method: "PUT",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({
        idPaciente: parseInt(pacienteId),
        idDoctor: parseInt(doctorId),
        fecha: fecha,
        descripcion: descripcion
      }),
    });
    if (!response.ok) {
      const errMsg = await response.text();
      showAlert(errMsg, "error");
      return;
    }
    showAlert("Cita actualizada correctamente");
    const modalEl = document.getElementById("editCitaModal");
    const modal = bootstrap.Modal.getInstance(modalEl);
    modal.hide();
    loadCitaTable();
  } catch (error) {
    console.error(error);
    showAlert("Error al actualizar la cita", "error");
  }
}

/* ==========================
   Eventos al cargar el DOM
========================== */
document.addEventListener("DOMContentLoaded", () => {
  loadCitaTable();
  document.getElementById("filterDescripcion").addEventListener("keyup", filterCitas);
  if(document.getElementById("filterPacienteSelect")){
    document.getElementById("filterPacienteSelect").addEventListener("change", filterCitas);
  }
  if(document.getElementById("filterDoctorSelect")){
    document.getElementById("filterDoctorSelect").addEventListener("change", filterCitas);
  }
  document.getElementById("registerCitaForm").addEventListener("submit", createCita);
  document.getElementById("editCitaForm").addEventListener("submit", updateCita);
  
  // Cargar opciones en selects para registro, filtro y edición
  loadPacienteOptions("registerCitaPacienteSelect");
  loadDoctorOptions("registerCitaDoctorSelect");
  loadPacienteOptions("filterPacienteSelect");
  loadDoctorOptions("filterDoctorSelect");
  loadPacienteOptions("editCitaPacienteSelect");
  loadDoctorOptions("editCitaDoctorSelect");
});
