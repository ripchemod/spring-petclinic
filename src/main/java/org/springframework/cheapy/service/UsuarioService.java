
package org.springframework.cheapy.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cheapy.model.Usuario;
import org.springframework.cheapy.repository.UsuarioRepository;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UsuarioService {

	private UsuarioRepository usuarioRepository;


	@Autowired
	public UsuarioService(final UsuarioRepository usuarioRepository) {
		this.usuarioRepository = usuarioRepository;
	}
	
	@Transactional
	public Usuario getCurrentUsuario() throws DataAccessException {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = authentication.getName();
		return this.usuarioRepository.findByUsername(username);
	}

	@Transactional
	public void saveUsuario(final Usuario usuario) throws DataAccessException {
		this.usuarioRepository.save(usuario);
	}
}
