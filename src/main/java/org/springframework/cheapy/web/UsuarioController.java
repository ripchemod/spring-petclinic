package org.springframework.cheapy.web;

import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.cheapy.model.SpeedOffer;
import org.springframework.cheapy.model.StatusOffer;
import org.springframework.cheapy.model.Usuario;
import org.springframework.cheapy.service.UsuarioService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UsuarioController {

	private static final String VIEWS_USUARIO_CREATE_OR_UPDATE_FORM = "usuarios/createOrUpdateUsuariosForm";

	private final UsuarioService usuarioService;

	public UsuarioController(final UsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}

	@GetMapping("/usuarios/show")
	public String processShowForm(Map<String, Object> model) {
		Usuario usuario = this.usuarioService.getCurrentUsuario();
		model.put("usuario", usuario);
		return "usuarios/usuariosShow"; 
	}

	@GetMapping(value = "/usuarios/edit")
	public String updateUsuario(final ModelMap model, HttpServletRequest request) {
		Usuario usuario = this.usuarioService.getCurrentUsuario();
		model.addAttribute("usuario", usuario);
		return UsuarioController.VIEWS_USUARIO_CREATE_OR_UPDATE_FORM;
	}

	@PostMapping(value = "/usuarios/edit")
	public String updateUsuario(@Valid final Usuario usuarioEdit, final BindingResult result,
			final ModelMap model, HttpServletRequest request) {

		Usuario usuario = this.usuarioService.getCurrentUsuario();
		BeanUtils.copyProperties(usuario, usuarioEdit, "nombre", "apellidos", "dni", "direccion", "telefono", "usuar");
		usuarioEdit.getUsuar().setUsername(usuario.getNombre());
		usuarioEdit.getUsuar().setEnabled(true);
		this.usuarioService.saveUsuario(usuarioEdit);
		return "redirect:/usuarios/show";
	}
	
	@GetMapping(value = "/usuarios/disable")
	public String disableUsuario(final ModelMap model) {

		Usuario usuario = this.usuarioService.getCurrentUsuario();
		model.put("usuario", usuario);
		return "usuarios/usuariosDisable";
	}

	@PostMapping(value = "/usuarios/disable")
	public String disableUsuarioForm(final ModelMap model, final HttpServletRequest request) {

		Usuario usuario = this.usuarioService.getCurrentUsuario();
		usuario.getUsuar().setEnabled(false);
		this.usuarioService.saveUsuario(usuario);
	
		try {
			
			request.logout();
			
		} catch (ServletException e) {
			
			e.printStackTrace();
			
		}

		return "redirect:/login";

	}
}
