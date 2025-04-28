const API_URL = "http://localhost:8085/api/v1/medicamento";

// Caché para almacenar los medicamentos cargados
let medicamentosCache = [];

/* ==========================
   Funciones de Utilidad
========================== */
function showAlert(message, type = "info") {
  // Puedes reemplazar este alert básico por notificaciones más sofisticadas.
  alert(type === "error" ? `Error: ${message}` : message);
}

/* ==========================
   Listado y Filtrado de Medicamentos
========================== */
async function loadMedicamentoTable() {
  try {
    const response = await fetch(API_URL);
    if (!response.ok) {
      throw new Error("Error al cargar los medicamentos");
    }
    medicamentosCache = await response.json();
    populateMedicamentoTable(medicamentosCache);
  } catch (error) {
    console.error(error);
    showAlert("Error al cargar los medicamentos", "error");
  }
}

function populateMedicamentoTable(medicamentos) {
  const tableBody = document.getElementById("medicamentoTableBody");
  if (!tableBody) return;

  tableBody.innerHTML = "";
  if (!medicamentos || medicamentos.length === 0) {
    tableBody.innerHTML = `<tr><td colspan="4" class="text-center">No hay medicamentos registrados.</td></tr>`;
    return;
  }
  medicamentos.forEach(medicamento => {
    const tr = document.createElement("tr");
    tr.innerHTML = `
      <td>${medicamento.nombre}</td>
      <td>${medicamento.tipo}</td>
      <td>${medicamento.costo}</td>
      <td>
        <button class="btn btn-sm btn-primary" onclick="openEditMedicamentoModal(${medicamento.id})">Editar</button>
        <button class="btn btn-sm btn-danger" onclick="deleteMedicamento(${medicamento.id})">Eliminar</button>
      </td>
    `;
    tableBody.appendChild(tr);
  });
}

function filterMedicamentos() {
  const filterNombre = document.getElementById("filterMedicamentoNombre")?.value.trim().toLowerCase() || "";
  const filterTipo = document.getElementById("filterMedicamentoTipo")?.value.trim().toLowerCase() || "";
  const filterCosto = document.getElementById("filterMedicamentoCosto")?.value.trim() || "";

  const filtered = medicamentosCache.filter(medicamento => {
    const nombreMatch = medicamento.nombre.toLowerCase().includes(filterNombre);
    const tipoMatch = medicamento.tipo.toLowerCase().includes(filterTipo);
    const costoMatch = filterCosto ? medicamento.costo === parseFloat(filterCosto) : true;
    return nombreMatch && tipoMatch && costoMatch;
  });
  populateMedicamentoTable(filtered);
}

function clearMedicamentoFilters() {
  if (document.getElementById("filterMedicamentoNombre"))
    document.getElementById("filterMedicamentoNombre").value = "";
  if (document.getElementById("filterMedicamentoTipo"))
    document.getElementById("filterMedicamentoTipo").value = "";
  if (document.getElementById("filterMedicamentoCosto"))
    document.getElementById("filterMedicamentoCosto").value = "";
  loadMedicamentoTable();
}

/* ==========================
   Registro de Medicamento
========================== */
async function createMedicamento(event) {
  event.preventDefault();
  const nombre = document.getElementById("registerMedicamentoNombre")?.value.trim();
  const tipo = document.getElementById("registerMedicamentoTipo")?.value.trim();
  const costo = document.getElementById("registerMedicamentoCosto")?.value.trim();

  if (!nombre || !tipo || !costo) {
    showAlert("Todos los campos son obligatorios", "error");
    return;
  }
  if (parseFloat(costo) < 0) {
    showAlert("El costo no puede ser negativo", "error");
    return;
  }
  
  try {
    // En el POST enviamos únicamente los campos nombre, tipo y costo.
    const response = await fetch(API_URL, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({
        nombre: nombre,
        tipo: tipo,
        costo: parseFloat(costo)
      })
    });
    if (!response.ok) {
      const errorMsg = await response.text();
      showAlert(errorMsg, "error");
      return;
    }
    showAlert("Medicamento registrado exitosamente");
    document.getElementById("registerMedicamentoForm")?.reset();
    loadMedicamentoTable();
  } catch (error) {
    console.error(error);
    showAlert("Error al registrar el medicamento", "error");
  }
}

/* ==========================
   Eliminación de Medicamento
========================== */
async function deleteMedicamento(id) {
  if (!confirm("¿Está seguro de eliminar este medicamento?")) return;
  try {
    const response = await fetch(`${API_URL}/${id}`, { method: "DELETE" });
    if (!response.ok) {
      const errorMsg = await response.text();
      showAlert(errorMsg, "error");
      return;
    }
    showAlert("Medicamento eliminado correctamente");
    loadMedicamentoTable();
  } catch (error) {
    console.error(error);
    showAlert("Error al eliminar el medicamento", "error");
  }
}

/* ==========================
   Edición de Medicamento (Modal)
========================== */
async function openEditMedicamentoModal(id) {
  try {
    const response = await fetch(`${API_URL}/${id}`);
    if (!response.ok) {
      showAlert("Error al obtener datos del medicamento", "error");
      return;
    }
    const medicamento = await response.json();
    document.getElementById("editMedicamentoId").value = medicamento.id;
    document.getElementById("editMedicamentoNombre").value = medicamento.nombre;
    document.getElementById("editMedicamentoTipo").value = medicamento.tipo;
    document.getElementById("editMedicamentoCosto").value = medicamento.costo;
    
    const modalEl = document.getElementById("editMedicamentoModal");
    const modal = new bootstrap.Modal(modalEl);
    modal.show();
  } catch (error) {
    console.error(error);
    showAlert("Error al cargar el medicamento", "error");
  }
}

async function updateMedicamento(event) {
  event.preventDefault();
  const id = document.getElementById("editMedicamentoId")?.value;
  const nombre = document.getElementById("editMedicamentoNombre")?.value.trim();
  const tipo = document.getElementById("editMedicamentoTipo")?.value.trim();
  const costo = document.getElementById("editMedicamentoCosto")?.value.trim();

  if (!nombre || !tipo || !costo) {
    showAlert("Todos los campos son obligatorios", "error");
    return;
  }
  if (parseFloat(costo) < 0) {
    showAlert("El costo no puede ser negativo", "error");
    return;
  }
  
  try {
    const response = await fetch(`${API_URL}/${id}`, {
      method: "PUT",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({
        nombre: nombre,
        tipo: tipo,
        costo: parseFloat(costo)
      })
    });
    if (!response.ok) {
      const errorMsg = await response.text();
      showAlert(errorMsg, "error");
      return;
    }
    showAlert("Medicamento actualizado correctamente");
    const modalEl = document.getElementById("editMedicamentoModal");
    const modal = bootstrap.Modal.getInstance(modalEl);
    modal.hide();
    loadMedicamentoTable();
  } catch (error) {
    console.error(error);
    showAlert("Error al actualizar el medicamento", "error");
  }
}

/* ==========================
   Eventos al Cargar el DOM
========================== */
document.addEventListener("DOMContentLoaded", () => {
  if (document.getElementById("medicamentoTableBody")) {
    loadMedicamentoTable();
  }
  const filterNombreElem = document.getElementById("filterMedicamentoNombre");
  if (filterNombreElem) filterNombreElem.addEventListener("keyup", filterMedicamentos);
  
  const filterTipoElem = document.getElementById("filterMedicamentoTipo");
  if (filterTipoElem) filterTipoElem.addEventListener("keyup", filterMedicamentos);
  
  const filterCostoElem = document.getElementById("filterMedicamentoCosto");
  if (filterCostoElem) filterCostoElem.addEventListener("keyup", filterMedicamentos);
  
  const regForm = document.getElementById("registerMedicamentoForm");
  if (regForm) regForm.addEventListener("submit", createMedicamento);
  
  const editForm = document.getElementById("editMedicamentoForm");
  if (editForm) editForm.addEventListener("submit", updateMedicamento);
});
