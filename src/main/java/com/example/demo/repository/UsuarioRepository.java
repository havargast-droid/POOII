package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.entities.Usuario;
import com.example.demo.entities.UsuarioPK;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, UsuarioPK> {

    @Query("SELECT u FROM Usuario u WHERE u.id.login = :login")
    Optional<Usuario> findByLogin(@Param("login") String login);

    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM Usuario u WHERE u.id.login = :login")
    boolean existsByLogin(@Param("login") String login);

    Optional<Usuario> findByPersonaId(Long personaId);
}
