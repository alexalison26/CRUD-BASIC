const API_URL = "http://localhost:8085/api/v1/usuario";

// Función: Alternar entre formularios de Login y Registro
function toggleForms() {
    const loginForm = document.getElementById("login-form");
    const registerForm = document.getElementById("register-form");
    
    if (loginForm.classList.contains("d-none")) {
        loginForm.classList.remove("d-none");
        registerForm.classList.add("d-none");
        document.getElementById("form-title").textContent = "Iniciar Sesión";
    } else {
        loginForm.classList.add("d-none");
        registerForm.classList.remove("d-none");
        document.getElementById("form-title").textContent = "Registrarse";
    }
}

// Función: Mostrar mensaje al usuario (se puede optimizar o reemplazar por un modal, etc.)
function showAlert(message, type = "info") {
    // Si type es "error" antepone "Error: " al mensaje.
    alert(type === "error" ? `Error: ${message}` : message);
}

// Función: Validar campos (se utiliza en registro y login)
// Para el registro se valida que el correo tenga formato y la contraseña tenga longitud entre 8 y 20 caracteres.
function validateCredentials(credential, password, isForLogin = false) {
    if (!credential || !password) {
        showAlert("Todos los campos son obligatorios.", "error");
        return false;
    }
    if (!isForLogin) {
        // Validación de correo usando expresión regular
        const emailRegex = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
        if (!emailRegex.test(credential)) {
            showAlert("El correo electrónico debe ser válido.", "error");
            return false;
        }
        if (password.length < 8 || password.length > 20) {
            showAlert("La contraseña debe tener entre 8 y 20 caracteres.", "error");
            return false;
        }
    }
    return true;
}

// Función: Registrar Usuario  
// Se toman: nombre (nombre completo), correo y contraseña.
async function registerUser(event) {
    event.preventDefault();

    const name = document.getElementById("register-name").value.trim();
    const correo = document.getElementById("register-email").value.trim();
    const password = document.getElementById("register-password").value.trim();

    // Validaciones: no vacíos, formato de correo y longitud de contraseña.
    if (!name || !correo || !password) {
        showAlert("Todos los campos son obligatorios.", "error");
        return;
    }
    if (!validateCredentials(correo, password)) return;

    try {
        // En la entidad se espera {id, nombre, correo, contrasena}
        const response = await fetch(API_URL, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ id: 0, nombre: name, correo: correo, contrasena: password })
        });

        if (!response.ok) {
            const errorMessage = await response.text();
            showAlert(errorMessage, "error");
            return;
        }

        showAlert("Registro exitoso");
        toggleForms(); // Cambia automáticamente al formulario de Login
    } catch (error) {
        console.error("Error al registrar usuario:", error);
        showAlert("Ocurrió un error al registrar el usuario.", "error");
    }
}

// Función: Iniciar Sesión (Login)
// Se tomarán el correo (desde login-email) y la contraseña (desde login-password)
async function loginUser(event) {
    event.preventDefault();

    const correo = document.getElementById("login-email").value.trim();
    const password = document.getElementById("login-password").value.trim();

    if (!validateCredentials(correo, password, true)) return;

    try {
        // En el login se envía un JSON con {correo, contrasena}
        const response = await fetch(`${API_URL}/login`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ correo: correo, contrasena: password })
        });

        if (!response.ok) {
            const errorMessage = await response.text();
            showAlert(errorMessage, "error");
            return;
        }

        showAlert("¡Bienvenido! Sesión iniciada correctamente.");
        window.location.href = "panel-admin.html"; // Redirige a la página después del login
    } catch (error) {
        console.error("Error al iniciar sesión:", error);
        showAlert("Ocurrió un error al iniciar sesión.", "error");
    }
}

// Asignar eventos una vez cargado el DOM
document.addEventListener("DOMContentLoaded", () => {
    const loginForm = document.getElementById("login-form");
    const registerForm = document.getElementById("register-form");

    if (loginForm) {
        loginForm.addEventListener("submit", loginUser);
    }
    if (registerForm) {
        registerForm.addEventListener("submit", registerUser);
    }
});
