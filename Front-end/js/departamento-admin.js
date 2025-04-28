const API_URL = "http://localhost:8085/api/v1/departamento";

/* ==========================
   Funciones de Utilidad
========================== */
function showAlert(message, type = "info") {
  alert(type === "error" ? `Error: ${message}` : message);
}

/* ==========================
   Listado y Filtrado de Departamentos
========================== */
async function loadDepartamentoTable() {
  try {
    const response = await fetch(API_URL);
    if (!response.ok) throw new Error("Error al cargar departamentos");
    const departamentos = await response.json();
    populateDepartamentoTable(departamentos);
  } catch (error) {
    console.error(error);
    showAlert("Error al cargar departamentos", "error");
  }
}

function populateDepartamentoTable(departamentos) {
  const tableBody = document.getElementById("departamentoTableBody");
  tableBody.innerHTML = "";
  if (!departamentos || departamentos.length === 0) {
    tableBody.innerHTML = `<tr><td colspan="3" class="text-center">No hay departamentos registrados.</td></tr>`;
    return;
  }
  departamentos.forEach(departamento => {
    const tr = document.createElement("tr");
    tr.innerHTML = `
      <td>${departamento.nombre}</td>
      <td>${departamento.ubicacion}</td>
      <td>
        <button class="btn btn-sm btn-primary" onclick="openEditDepartamentoModal(${departamento.id})">Editar</button>
        <button class="btn btn-sm btn-danger" onclick="deleteDepartamento(${departamento.id})">Eliminar</button>
      </td>
    `;
    tableBody.appendChild(tr);
  });
}

async function filterDepartamentos() {
  const filterNombre = document.getElementById("filterDepartamentoNombre").value.trim().toLowerCase();
  const filterUbicacion = document.getElementById("filterDepartamentoUbicacion").value.trim().toLowerCase();
  try {
    const response = await fetch(API_URL);
    if (!response.ok) throw new Error("Error al cargar departamentos");
    const departamentos = await response.json();
    const filtered = departamentos.filter(departamento => {
      let matchNombre = true;
      let matchUbicacion = true;
      if (filterNombre) {
        matchNombre = departamento.nombre.toLowerCase().includes(filterNombre);
      }
      if (filterUbicacion) {
        matchUbicacion = departamento.ubicacion.toLowerCase().includes(filterUbicacion);
      }
      return matchNombre && matchUbicacion;
    });
    populateDepartamentoTable(filtered);
  } catch (error) {
    console.error(error);
    showAlert("Error al filtrar departamentos", "error");
  }
}

function clearDepartamentoFilters() {
  document.getElementById("filterDepartamentoNombre").value = "";
  document.getElementById("filterDepartamentoUbicacion").value = "";
  loadDepartamentoTable();
}

/* ==========================
   Registro de Departamento
========================== */
async function createDepartamento(event) {
  event.preventDefault();
  const nombre = document.getElementById("registerDepartamentoNombre").value.trim();
  const ubicacion = document.getElementById("registerDepartamentoUbicacion").value.trim();
  
  if (!nombre || !ubicacion) {
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
        ubicacion: ubicacion
      })
    });
    if (!response.ok) {
      const errMsg = await response.text();
      showAlert(errMsg, "error");
      return;
    }
    showAlert("Departamento registrado exitosamente");
    document.getElementById("registerDepartamentoForm").reset();
    loadDepartamentoTable();
  } catch (error) {
    console.error(error);
    showAlert("Error al registrar departamento", "error");
  }
}

/* ==========================
   Eliminación de Departamento
========================== */
async function deleteDepartamento(id) {
  if (!confirm("¿Está seguro de eliminar este departamento?")) return;
  try {
    const response = await fetch(`${API_URL}/${id}`, { method: "DELETE" });
    if (!response.ok) {
      const errMsg = await response.text();
      showAlert(errMsg, "error");
      return;
    }
    showAlert("Departamento eliminado correctamente");
    loadDepartamentoTable();
  } catch (error) {
    console.error(error);
    showAlert("Error al eliminar departamento", "error");
  }
}

/* ==========================
   Edición de Departamento (Modal)
========================== */
async function openEditDepartamentoModal(id) {
  try {
    const response = await fetch(`${API_URL}/${id}`);
    if (!response.ok) {
      showAlert("Error al obtener datos del departamento", "error");
      return;
    }
    const departamento = await response.json();
    document.getElementById("editDepartamentoId").value = departamento.id;
    document.getElementById("editDepartamentoNombre").value = departamento.nombre;
    document.getElementById("editDepartamentoUbicacion").value = departamento.ubicacion;
    const modalEl = document.getElementById("editDepartamentoModal");
    const modal = new bootstrap.Modal(modalEl);
    modal.show();
  } catch (error) {
    console.error(error);
    showAlert("Error al cargar el departamento", "error");
  }
}

async function updateDepartamento(event) {
  event.preventDefault();
  const id = document.getElementById("editDepartamentoId").value;
  const nombre = document.getElementById("editDepartamentoNombre").value.trim();
  const ubicacion = document.getElementById("editDepartamentoUbicacion").value.trim();
  
  if (!nombre || !ubicacion) {
    showAlert("Todos los campos son obligatorios", "error");
    return;
  }
  
  try {
    const response = await fetch(`${API_URL}/${id}`, {
      method: "PUT",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({
        nombre: nombre,
        ubicacion: ubicacion
      })
    });
    if (!response.ok) {
      const errMsg = await response.text();
      showAlert(errMsg, "error");
      return;
    }
    showAlert("Departamento actualizado correctamente");
    const modalEl = document.getElementById("editDepartamentoModal");
    const modal = bootstrap.Modal.getInstance(modalEl);
    modal.hide();
    loadDepartamentoTable();
  } catch (error) {
    console.error(error);
    showAlert("Error al actualizar departamento", "error");
  }
}

/* ==========================
   Eventos al Cargar el DOM
========================== */
document.addEventListener("DOMContentLoaded", () => {
  loadDepartamentoTable();
  document.getElementById("filterDepartamentoNombre").addEventListener("keyup", filterDepartamentos);
  document.getElementById("filterDepartamentoUbicacion").addEventListener("keyup", filterDepartamentos);
  document.getElementById("registerDepartamentoForm").addEventListener("submit", createDepartamento);
  document.getElementById("editDepartamentoForm").addEventListener("submit", updateDepartamento);
});
