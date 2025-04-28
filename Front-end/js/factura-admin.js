// URL del endpoint de facturas y pacientes
const API_URL = "http://localhost:8085/api/v1/factura";
const PATIENT_API_URL = "http://localhost:8085/api/v1/paciente";

// Formateador de moneda en pesos colombianos (COP)
const currencyFormatter = new Intl.NumberFormat('es-CO', {
  style: 'currency',
  currency: 'COP',
  minimumFractionDigits: 0
});

// Caché para facturas y pacientes (para filtrar localmente y llenar select)
let facturasCache = [];
let pacientesCache = [];

/* ==========================
   Funciones de Utilidad
========================== */
function showAlert(message, type = "info") {
  alert(type === "error" ? `Error: ${message}` : message);
}

/* ==========================
   Listado y Filtrado de Facturas
========================== */
async function loadFacturaTable() {
  try {
    const response = await fetch(API_URL);
    if (!response.ok) throw new Error("Error al cargar facturas");
    facturasCache = await response.json();
    populateFacturaTable(facturasCache);
  } catch (error) {
    console.error(error);
    showAlert("Error al cargar facturas", "error");
  }
}

function populateFacturaTable(facturas) {
  const tableBody = document.getElementById("facturaTableBody");
  tableBody.innerHTML = "";
  if (!facturas || facturas.length === 0) {
    tableBody.innerHTML = `<tr><td colspan="5" class="text-center">No hay facturas registradas.</td></tr>`;
    return;
  }
  facturas.forEach(factura => {
    const totalFormateado = currencyFormatter.format(factura.total);
    const tr = document.createElement("tr");
    tr.innerHTML = `
      <td>${factura.nombrePaciente}</td>
      <td>${totalFormateado}</td>
      <td>${factura.fechaPago}</td>
      <td>
        <button class="btn btn-sm btn-primary" onclick="openEditFacturaModal(${factura.id})">Editar</button>
        <button class="btn btn-sm btn-danger" onclick="deleteFactura(${factura.id})">Eliminar</button>
      </td>
    `;
    tableBody.appendChild(tr);
  });
}

function filterFacturas() {
  const filterPaciente = document.getElementById("filterFacturaPaciente").value.trim().toLowerCase();
  const filterTotal = document.getElementById("filterFacturaTotal").value.trim();
  const filterFecha = document.getElementById("filterFacturaFechaPago").value.trim();

  // Filtramos sobre la lista cache (ya cargada)
  const filtered = facturasCache.filter(factura => {
    let matchPaciente = true, matchTotal = true, matchFecha = true;
    if (filterPaciente) {
      // Se filtra por coincidencias en el nombre del paciente
      matchPaciente = factura.nombrePaciente.toLowerCase().includes(filterPaciente);
    }
    if (filterTotal) {
      // Se compara el total; si se desea aproximar se puede modificar
      matchTotal = factura.total === parseFloat(filterTotal);
    }
    if (filterFecha) {
      // Se asume que la fecha están en formato "YYYY-MM-DD"
      matchFecha = factura.fechaPago.includes(filterFecha);
    }
    return matchPaciente && matchTotal && matchFecha;
  });
  populateFacturaTable(filtered);
}

function clearFacturaFilters() {
  document.getElementById("filterFacturaPaciente").value = "";
  document.getElementById("filterFacturaTotal").value = "";
  document.getElementById("filterFacturaFechaPago").value = "";
  loadFacturaTable();
}

/* ==========================
   Funciones para Cargar Opciones de Pacientes
========================== */
async function loadPacienteOptions() {
  try {
    const response = await fetch(PATIENT_API_URL);
    if (!response.ok) throw new Error("Error al cargar pacientes");
    pacientesCache = await response.json();
    populatePacienteSelect("registerFacturaPacienteSelect");
    populatePacienteSelect("editFacturaPacienteSelect");
  } catch (error) {
    console.error(error);
    showAlert("Error al cargar opciones de pacientes", "error");
  }
}

function populatePacienteSelect(selectId) {
  const select = document.getElementById(selectId);
  select.innerHTML = '<option value="">Seleccione un paciente</option>';
  if (pacientesCache && pacientesCache.length > 0) {
    pacientesCache.forEach(paciente => {
      const option = document.createElement("option");
      option.value = paciente.id;
      option.text = paciente.nombre;
      select.appendChild(option);
    });
  }
}

/* ==========================
   Registro de Factura
========================== */
async function createFactura(event) {
  event.preventDefault();
  // Leer los valores del formulario de registro
  const pacienteSelect = document.getElementById("registerFacturaPacienteSelect");
  const idPaciente = pacienteSelect.value;
  const total = document.getElementById("registerFacturaTotal").value.trim();
  const fechaPago = document.getElementById("registerFacturaFechaPago").value.trim();

  if (!idPaciente || !total || !fechaPago) {
    showAlert("Todos los campos son obligatorios", "error");
    return;
  }
  if (parseFloat(total) < 0) {
    showAlert("El total no puede ser negativo", "error");
    return;
  }

  try {
    const response = await fetch(API_URL, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({
        id: 0,
        idPaciente: parseInt(idPaciente),
        total: parseFloat(total),
        fechaPago: fechaPago
      })
    });
    if (!response.ok) {
      const errorMsg = await response.text();
      showAlert(errorMsg, "error");
      return;
    }
    showAlert("Factura registrada exitosamente");
    document.getElementById("registerFacturaForm").reset();
    loadFacturaTable();
  } catch (error) {
    console.error(error);
    showAlert("Error al registrar factura", "error");
  }
}

/* ==========================
   Eliminación de Factura
========================== */
async function deleteFactura(id) {
  if (!confirm("¿Está seguro de eliminar esta factura?")) return;
  try {
    const response = await fetch(`${API_URL}/${id}`, { method: "DELETE" });
    if (!response.ok) {
      const errorMsg = await response.text();
      showAlert(errorMsg, "error");
      return;
    }
    showAlert("Factura eliminada correctamente");
    loadFacturaTable();
  } catch (error) {
    console.error(error);
    showAlert("Error al eliminar factura", "error");
  }
}

/* ==========================
   Edición de Factura (Modal)
========================== */
async function openEditFacturaModal(id) {
  try {
    const response = await fetch(`${API_URL}/${id}`);
    if (!response.ok) {
      showAlert("Error al obtener datos de la factura", "error");
      return;
    }
    const factura = await response.json();
    document.getElementById("editFacturaId").value = factura.id;
    document.getElementById("editFacturaTotal").value = factura.total;
    document.getElementById("editFacturaFechaPago").value = factura.fechaPago;
    
    // Cargar las opciones de pacientes si no se han cargado aún
    if (!pacientesCache || pacientesCache.length === 0) {
      await loadPacienteOptions();
    } else {
      populatePacienteSelect("editFacturaPacienteSelect");
    }
    // Seleccionar el paciente actual en el desplegable
    document.getElementById("editFacturaPacienteSelect").value = factura.idPaciente;
    
    const modalEl = document.getElementById("editFacturaModal");
    const modal = new bootstrap.Modal(modalEl);
    modal.show();
  } catch (error) {
    console.error(error);
    showAlert("Error al cargar la factura", "error");
  }
}

async function updateFactura(event) {
  event.preventDefault();
  const id = document.getElementById("editFacturaId").value;
  const pacienteSelect = document.getElementById("editFacturaPacienteSelect");
  const idPaciente = pacienteSelect.value;
  const total = document.getElementById("editFacturaTotal").value.trim();
  const fechaPago = document.getElementById("editFacturaFechaPago").value.trim();

  if (!idPaciente || !total || !fechaPago) {
    showAlert("Todos los campos son obligatorios", "error");
    return;
  }
  if (parseFloat(total) < 0) {
    showAlert("El total no puede ser negativo", "error");
    return;
  }

  try {
    const response = await fetch(`${API_URL}/${id}`, {
      method: "PUT",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({
        idPaciente: parseInt(idPaciente),
        total: parseFloat(total),
        fechaPago: fechaPago
      })
    });
    if (!response.ok) {
      const errorMsg = await response.text();
      showAlert(errorMsg, "error");
      return;
    }
    showAlert("Factura actualizada correctamente");
    const modalEl = document.getElementById("editFacturaModal");
    const modal = bootstrap.Modal.getInstance(modalEl);
    modal.hide();
    loadFacturaTable();
  } catch (error) {
    console.error(error);
    showAlert("Error al actualizar factura", "error");
  }
}

/* ==========================
   Eventos al Cargar el DOM
========================== */
document.addEventListener("DOMContentLoaded", () => {
  loadFacturaTable();
  loadPacienteOptions(); // llenar los select de pacientes

  // Eventos de filtrado
  document.getElementById("filterFacturaPaciente").addEventListener("keyup", filterFacturas);
  document.getElementById("filterFacturaTotal").addEventListener("keyup", filterFacturas);
  document.getElementById("filterFacturaFechaPago").addEventListener("change", filterFacturas);

  // Eventos de formularios
  document.getElementById("registerFacturaForm").addEventListener("submit", createFactura);
  document.getElementById("editFacturaForm").addEventListener("submit", updateFactura);
});
