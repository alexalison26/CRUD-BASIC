package com.hospital.pacientes.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.hospital.pacientes.model.usuario;
import com.hospital.pacientes.DTO.usuarioDTO;
import com.hospital.pacientes.repository.Iusuario;

@Service
public class usuarioService {

    @Autowired
    private Iusuario iusuario;

    // Conversión de entidad a DTO
    private usuarioDTO convertToDTO(usuario entity) {
        return new usuarioDTO(entity.getId(), entity.getNombre(), entity.getCorreo(), entity.getContrasena());
    }

    // Conversión de DTO a entidad
    private usuario convertToEntity(usuarioDTO dto) {
        usuario entity = new usuario();
        entity.setId(dto.getId());
        entity.setNombre(dto.getNombre());
        entity.setCorreo(dto.getCorreo());
        entity.setContrasena(dto.getContrasena());
        return entity;
    }

    // Listar todos los usuarios
    public List<usuarioDTO> getAllUsuarios() {
        List<usuario> lista = iusuario.findAll();
        return lista.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // Obtener usuario por ID
    public usuarioDTO getUsuarioById(int id) {
        Optional<usuario> opt = iusuario.findById(id);
        if(opt.isPresent()) {
            return convertToDTO(opt.get());
        } else {
            throw new IllegalArgumentException("Usuario no encontrado con id: " + id);
        }
    }

    // Crear un nuevo usuario con validaciones
    public usuarioDTO createUsuario(usuarioDTO usuDTO) {
        if(usuDTO.getNombre() == null || usuDTO.getNombre().trim().isEmpty()){
            throw new IllegalArgumentException("El nombre es obligatorio.");
        }
        if(usuDTO.getCorreo() == null || usuDTO.getCorreo().trim().isEmpty()){
            throw new IllegalArgumentException("El correo es obligatorio.");
        }
        if(usuDTO.getContrasena() == null || usuDTO.getContrasena().trim().isEmpty()){
            throw new IllegalArgumentException("La contraseña es obligatoria.");
        }
        if(usuDTO.getContrasena().length() < 8 || usuDTO.getContrasena().length() > 20){
            throw new IllegalArgumentException("La contraseña debe tener entre 8 y 20 caracteres.");
        }
        usuario entity = convertToEntity(usuDTO);
        usuario created = iusuario.save(entity);
        return convertToDTO(created);
    }

    // Actualizar un usuario existente con validaciones
    public usuarioDTO updateUsuario(int id, usuarioDTO usuDTO) {
        if(usuDTO.getNombre() == null || usuDTO.getNombre().trim().isEmpty()){
            throw new IllegalArgumentException("El nombre es obligatorio.");
        }
        if(usuDTO.getCorreo() == null || usuDTO.getCorreo().trim().isEmpty()){
            throw new IllegalArgumentException("El correo es obligatorio.");
        }
        if(usuDTO.getContrasena() == null || usuDTO.getContrasena().trim().isEmpty()){
            throw new IllegalArgumentException("La contraseña es obligatoria.");
        }
        if(usuDTO.getContrasena().length() < 8 || usuDTO.getContrasena().length() > 20){
            throw new IllegalArgumentException("La contraseña debe tener entre 8 y 20 caracteres.");
        }
        Optional<usuario> optional = iusuario.findById(id);
        if(!optional.isPresent()){
            throw new IllegalArgumentException("Usuario no encontrado con id: " + id);
        }
        usuario entity = optional.get();
        entity.setNombre(usuDTO.getNombre());
        entity.setCorreo(usuDTO.getCorreo());
        entity.setContrasena(usuDTO.getContrasena());
        usuario updated = iusuario.save(entity);
        return convertToDTO(updated);
    }

    // Eliminar usuario por ID (si no existe, lanza excepción)
    public void deleteUsuario(int id) {
        if(!iusuario.existsById(id)) {
            throw new IllegalArgumentException("Usuario no encontrado con id: " + id);
        }
        iusuario.deleteById(id);
    }

    // Filtrar usuarios por nombre
    public List<usuarioDTO> filterUsuarios(String nombre) {
        if(nombre == null){
            nombre = "";
        }
        List<usuario> lista = iusuario.findByNombreContainingIgnoreCase(nombre);
        return lista.stream().map(this::convertToDTO).collect(Collectors.toList());
    }
}
