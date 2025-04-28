const API_URL = "http://localhost:8085/api/v1/usuario";

/* ==========================
   Funciones de Utilidad
========================== */
function showAlert(message, type = "info") {
  alert(type === "error" ? `Error: ${message}` : message);
}

/* ==========================
   Listado y Filtrado de Usuarios
========================== */
async function loadUserTable() {
  try {
    const response = await fetch(API_URL);
    if (!response.ok) throw new Error("Error al cargar usuarios");
    const users = await response.json();
    populateUserTable(users);
  } catch (error) {
    console.error(error);
    showAlert("Error al cargar usuarios", "error");
  }
}

function populateUserTable(users) {
  const tableBody = document.getElementById("userTableBody");
  tableBody.innerHTML = "";
  if (!users || users.length === 0) {
    tableBody.innerHTML = `<tr><td colspan="3" class="text-center">No hay usuarios registrados.</td></tr>`;
    return;
  }
  users.forEach(user => {
    const tr = document.createElement("tr");
    tr.innerHTML = `
      <td>${user.nombre}</td>
      <td>${user.correo}</td>
      <td>
        <button class="btn btn-sm btn-primary" onclick="openEditModal(${user.id})">Editar</button>
        <button class="btn btn-sm btn-danger" onclick="deleteUser(${user.id})">Eliminar</button>
      </td>
    `;
    tableBody.appendChild(tr);
  });
}

// Filtrado: se obtienen todos los usuarios y se filtra en memoria por coincidencia (substring)
async function filterUsers() {
  const nameFilter = document.getElementById("filterName").value.trim().toLowerCase();
  const emailFilter = document.getElementById("filterEmail").value.trim().toLowerCase();
  try {
    const response = await fetch(API_URL);
    if (!response.ok) throw new Error("Error al cargar usuarios");
    const users = await response.json();
    const filtered = users.filter(user => {
      let nameMatch = true, emailMatch = true;
      if (nameFilter) {
        // Filtra por coincidencias en cualquier parte
        nameMatch = user.nombre.toLowerCase().includes(nameFilter);
      }
      if (emailFilter) {
        emailMatch = user.correo.toLowerCase().includes(emailFilter);
      }
      return nameMatch && emailMatch;
    });
    populateUserTable(filtered);
  } catch (error) {
    console.error(error);
    showAlert("Error al filtrar usuarios", "error");
  }
}

/* ==========================
   Registro de Usuario
========================== */
async function createUser(event) {
  event.preventDefault();
  const name = document.getElementById("registerName").value.trim();
  const email = document.getElementById("registerEmail").value.trim();
  const password = document.getElementById("registerPassword").value.trim();
  
  if (!name || !email || !password) {
    showAlert("Todos los campos son obligatorios", "error");
    return;
  }
  const emailRegex = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
  if (!emailRegex.test(email)) {
    showAlert("El correo ingresado es inválido", "error");
    return;
  }
  if (password.length < 8 || password.length > 20) {
    showAlert("La contraseña debe tener entre 8 y 20 caracteres", "error");
    return;
  }
  try {
    const response = await fetch(API_URL, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({
        id: 0,
        nombre: name,
        correo: email,
        contrasena: password
      }),
    });
    if (!response.ok) {
      const errMsg = await response.text();
      showAlert(errMsg, "error");
      return;
    }
    showAlert("Usuario registrado exitosamente");
    document.getElementById("registerForm").reset();
    loadUserTable();
  } catch (error) {
    console.error(error);
    showAlert("Error al registrar usuario", "error");
  }
}

/* ==========================
   Eliminación de Usuario
========================== */
async function deleteUser(id) {
  if (!confirm("¿Estás seguro de eliminar este usuario?")) return;
  try {
    const response = await fetch(`${API_URL}/${id}`, {
      method: "DELETE"
    });
    if (!response.ok) {
      const errMsg = await response.text();
      showAlert(errMsg, "error");
      return;
    }
    showAlert("Usuario eliminado correctamente");
    loadUserTable();
  } catch (error) {
    console.error(error);
    showAlert("Error al eliminar usuario", "error");
  }
}

/* ==========================
   Edición de Usuario (Modal)
========================== */
async function openEditModal(id) {
  try {
    const response = await fetch(`${API_URL}/${id}`);
    if (!response.ok) {
      showAlert("Error al obtener datos del usuario", "error");
      return;
    }
    const user = await response.json();
    // Asigna los valores actuales al modal, incluyendo la contraseña para poder editarla
    document.getElementById("editUserId").value = user.id;
    document.getElementById("editName").value = user.nombre;
    document.getElementById("editEmail").value = user.correo;
    document.getElementById("editPassword").value = user.contrasena;
    const modalEl = document.getElementById("editModal");
    const modal = new bootstrap.Modal(modalEl);
    modal.show();
  } catch (error) {
    console.error(error);
    showAlert("Error al cargar el usuario", "error");
  }
}

async function updateUser(event) {
  event.preventDefault();
  const id = document.getElementById("editUserId").value;
  const name = document.getElementById("editName").value.trim();
  const email = document.getElementById("editEmail").value.trim();
  const password = document.getElementById("editPassword").value.trim();
  
  if (!name || !email || !password) {
    showAlert("Todos los campos son obligatorios", "error");
    return;
  }
  const emailRegex = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
  if (!emailRegex.test(email)) {
    showAlert("El correo ingresado es inválido", "error");
    return;
  }
  if (password.length < 8 || password.length > 20) {
    showAlert("La contraseña debe tener entre 8 y 20 caracteres", "error");
    return;
  }
  try {
    const response = await fetch(`${API_URL}/${id}`, {
      method: "PUT",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({
        nombre: name,
        correo: email,
        contrasena: password
      }),
    });
    if (!response.ok) {
      const errMsg = await response.text();
      showAlert(errMsg, "error");
      return;
    }
    showAlert("Usuario actualizado correctamente");
    const modalEl = document.getElementById("editModal");
    const modal = bootstrap.Modal.getInstance(modalEl);
    modal.hide();
    loadUserTable();
  } catch (error) {
    console.error(error);
    showAlert("Error al actualizar usuario", "error");
  }
}

/* ==========================
   Eventos al Cargar el DOM
========================== */
document.addEventListener("DOMContentLoaded", () => {
  loadUserTable();
  document.getElementById("filterName").addEventListener("keyup", filterUsers);
  document.getElementById("filterEmail").addEventListener("keyup", filterUsers);
  document.getElementById("registerForm").addEventListener("submit", createUser);
  document.getElementById("editUserForm").addEventListener("submit", updateUser);
});
