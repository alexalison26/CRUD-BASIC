function toggleForms() {
    const loginForm = document.getElementById('login-form');
    const registerForm = document.getElementById('register-form');
    const title = document.getElementById('form-title');
    const container = document.getElementById('form-container');

    if (loginForm.classList.contains('d-none')) {
      loginForm.classList.remove('d-none');
      registerForm.classList.add('d-none');
      title.textContent = 'Iniciar Sesión';
    } else {
      loginForm.classList.add('d-none');
      registerForm.classList.remove('d-none');
      title.textContent = 'Registro';
    }

    container.classList.remove('fade-in');
    void container.offsetWidth; // Reinicia la animación
    container.classList.add('fade-in');
  }