package com.jc.sgtasec.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.googlecode.jmapper.JMapper;
import com.jc.sgtasec.EmailSenderService;
import com.jc.sgtasec.model.Rol;
import com.jc.sgtasec.model.Usuario;
import com.jc.sgtasec.repository.IUsuarioRepository;
import com.jc.sgtasec.web.dto.UsuarioDto;

@Service
public class UsuarioServiceImpl implements IUsuarioService {

	private Logger logger = LogManager.getLogger(getClass());
	private Authentication auth;
	private IUsuarioRepository usuarioRepository;
	private JMapper<Usuario, UsuarioDto> mapperToEntity;
	private JMapper<UsuarioDto, Usuario> mapperToDTO;

	@Autowired
	private EmailSenderService senderService;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	public UsuarioServiceImpl(IUsuarioRepository usuarioRepository) {
		super();
		this.usuarioRepository = usuarioRepository;
		this.mapperToEntity = new JMapper<>(Usuario.class, UsuarioDto.class);
		this.mapperToDTO = new JMapper<>(UsuarioDto.class, Usuario.class);
	}

	@Override
	public Usuario save(UsuarioDto usuarioDto) {
		
		Usuario usuario = new Usuario(usuarioDto.getNombre(), usuarioDto.getApellidoPaterno(),
				usuarioDto.getApellidoMaterno(), usuarioDto.getEmail(),
				passwordEncoder.encode(usuarioDto.getPassword()), usuarioDto.getRut(),
				Arrays.asList(new Rol("USER_ROLE")));
		
		this.auth = SecurityContextHolder.getContext().getAuthentication();
		logger.info("Usuario: " + auth.getName());
		logger.info("save(UsuarioDto usuarioDto): " + usuarioDto.toString());	

		return usuarioRepository.save(usuario);
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Usuario usuario = usuarioRepository.findByEmail(username);
		if (usuario == null) {
			throw new UsernameNotFoundException("Invalid username or password.");
		}
		return new org.springframework.security.core.userdetails.User(usuario.getEmail(), usuario.getPassword(),
				mapRolesToAuthorities(usuario.getRoles()));
	}

	private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Rol> roles) {
		return roles.stream().map(rol -> new SimpleGrantedAuthority(rol.getName())).collect(Collectors.toList());
	}

	@Override
	public boolean recoveryAcces(UsuarioDto usuarioDto) {

		Usuario usuario = usuarioRepository.findByEmail(usuarioDto.getEmail());

		if (usuario == null) {
			return false;
		}

		PasswordGenerator passwordGenerator = new PasswordGenerator();
		String password = passwordGenerator.nextPassword();
		String mensaje = "El usuario " + usuarioDto.getEmail()
				+ " a solicitado la recuperación de acceso al sistema SGTA.\r\n";
		mensaje += "Puede ingresar con la siguiente clave: " + password;
		senderService.sendEmail(usuarioDto.getEmail(), "SGTA solicitud de recuperación de contraseña", mensaje);

		usuario.setPassword(passwordEncoder.encode(password));
		resetPassword(usuario);
		
		this.auth = SecurityContextHolder.getContext().getAuthentication();
		logger.info("Usuario: " + auth.getName());
		logger.info("recoveryAcces(UsuarioDto usuarioDto): " + usuarioDto.toString());	

		return true;
	}

	@Override
	public Usuario resetPassword(Usuario usuario) {
		return usuarioRepository.save(usuario);
	}

	// método para saber si un usuario a eliminar o editar es ADMINISTRADOR, para
	// evitar la modificación

	@Override
	public boolean esUsuarioAdministrador(Long id) {

		Usuario usuario = usuarioRepository.findById(id).get();
		boolean esAdmin = false;

		for (Rol rol : usuario.getRoles()) {

			if (rol.getName().equalsIgnoreCase("ADMIN_ROLE")) {
				esAdmin = true;
			}
		}

		return esAdmin;
	}

	// crud

	@Override
	public List<Usuario> getAllUsuarios() {
		return usuarioRepository.findAll();
	}

	@Override
	public Usuario saveUsuario(Usuario usuario) {
		this.auth = SecurityContextHolder.getContext().getAuthentication();
		logger.info("Usuario: " + auth.getName());
		logger.info("saveUsuario(Usuario usuario): " + usuario.toString());	
		return usuarioRepository.save(usuario);
	}

	@Override
	public Usuario getUsuarioById(Long id) {
		return usuarioRepository.findById(id).get();
	}

	@Override
	public Usuario updateUsuario(Usuario usuario) {
		this.auth = SecurityContextHolder.getContext().getAuthentication();
		logger.info("Usuario: " + auth.getName());
		logger.info("updateUsuario(Usuario usuario): " + usuario.toString());	
		return usuarioRepository.save(usuario);
	}

	@Override
	public void deleteUsuarioById(Long id) {
		this.auth = SecurityContextHolder.getContext().getAuthentication();
		logger.info("Usuario: " + auth.getName());
		logger.info("deleteUsuarioById(Long id): " + id);
		usuarioRepository.deleteById(id);
	}

	@Override
	public Usuario mapperToEntity(UsuarioDto source) {
		Usuario usuario = new Usuario();
		usuario = mapperToEntity.getDestination(source);
		return usuario;
	}

	@Override
	public UsuarioDto mapperToDTO(Usuario source) {
		UsuarioDto usuarioDto = new UsuarioDto();
		usuarioDto = mapperToDTO.getDestination(source);
		return usuarioDto;
	}

	@Override
	public List<UsuarioDto> getListDTO(List<Usuario> lista) {
		List<UsuarioDto> listDTO = new ArrayList<UsuarioDto>();

		for (Usuario usuario : getAllUsuarios()) {
			listDTO.add(mapperToDTO(usuario));
		}
		return listDTO;
	}

	@Override
	public Usuario getUsuarioByEmail(String email) {
		return usuarioRepository.findByEmail(email);
	}

}
