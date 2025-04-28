-- Inserción de datos en la tabla Paciente
INSERT INTO Paciente (id_paciente, nombre, fecha_nacimiento, direccion) VALUES 
(1, 'Juan Pérez', '1985-06-15', 'Av. Siempre Viva 123'),
(2, 'Ana Gómez', '1992-08-22', 'Calle Falsa 456'),
(3, 'Carlos López', '1978-11-30', 'Plaza Central 789');

-- Inserción de datos en la tabla Doctor
INSERT INTO Doctor (id_doctor, nombre, especialidad, telefono) VALUES 
(1, 'Dr. Roberto Fernández', 'Cardiología', '555-1234'),
(2, 'Dra. Mariana Ruiz', 'Neurología', '555-5678'),
(3, 'Dr. José Martínez', 'Pediatría', '555-8765');

-- Inserción de datos en la tabla Departamento
INSERT INTO Departamento (id_departamento, nombre, ubicacion) VALUES 
(1, 'Emergencias', 'Edificio A - Piso 1'),
(2, 'Pediatría', 'Edificio B - Piso 2'),
(3, 'Cardiología', 'Edificio C - Piso 3');

-- Inserción de datos en la tabla Cita
INSERT INTO Cita (id_cita, id_paciente, id_doctor, fecha, descripcion) VALUES 
(1, 1, 1, '2025-04-28 23:20:00.000000', 'Cita Tratamiento');

-- Inserción de datos en la tabla Factura
INSERT INTO Factura (id_factura, id_paciente, total, fecha_pago) VALUES 
(1, 1, 250.00, '2025-04-11'),
(2, 2, 320.50, '2025-04-13'),
(3, 3, 150.75, '2025-04-16');

-- Inserción de datos en la tabla Medicamento
INSERT INTO Medicamento (id_medicamento, nombre, tipo, costo) VALUES 
(1, 'Ibuprofeno', 'Analgésico', 8700),
(2, 'Paracetamol', 'Antipirético', 3500),
(3, 'Omeprazol', 'Gastroprotector', 2000);

-- Inserción de datos en la tabla Habitacion
INSERT INTO Habitacion (id_habitacion, numero, capacidad) VALUES 
(1, '101A', 2),
(2, '202B', 1),
(3, '303C', 3);

-- Inserción de datos en la tabla Usuario
INSERT INTO Usuario (id_usuario, nombre, correo, contrasena) VALUES 
(1, 'Admin', 'admin@admin.com','12345678');

-- Inserción de datos en la tabla Enfermero
INSERT INTO `enfermero` (anios_experiencia, id_enfermero, turno, nombre) VALUES
(2, 1, 'Mañana', 'Pepito Perez');

-- Inserción de datos en la tabla Tratamiento
INSERT INTO `tratamiento` (id_tratamiento, nombre, descripcion) VALUES
(1, 'Palo Terapia', 'Sensación de descanso en el cuerpo');