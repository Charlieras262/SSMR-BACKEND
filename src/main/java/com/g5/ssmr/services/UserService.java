package com.g5.ssmr.services;

import com.g5.ssmr.config.security.JwtUtil;
import com.g5.ssmr.dto.*;
import com.g5.ssmr.models.User;
import com.g5.ssmr.projections.UserRole;
import com.g5.ssmr.projections.UserDetailProjection;
import com.g5.ssmr.repositories.UserRepository;
import com.g5.ssmr.repositories.UserRoleRepository;
import com.g5.ssmr.utils.Catalog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@Service
@Slf4j
@Transactional
public class UserService {

    @Autowired
    public UserRepository userRepository;

    @Autowired
    public UserRoleRepository userRoleRepository;

    @Value("${spring.security.user.name}")
    private String adminUser;
    @Value("${spring.security.user.password}")
    private String adminPass;

    /**
     * Este metodo es de solo lectura (no se adhiere a una transacción existente) encargado de consultar todos los usuarios
     * registrados en base de datos.
     *
     * @return Lista de {@link User} registrados en la base de datos.
     * @author Carlos Ramos (cramosl3@miumg.edu.gt)
     */
    @Transactional(readOnly = true)
    public List<UserDetailProjection> getAllUsers(HttpHeaders headers) {
        final String username = JwtUtil.parseToken(headers.getFirst("Authorization"));
        if (!userRoleRepository.existsByIdUserAndIdRoleIsIn(username, Arrays.asList(Catalog.UserRole.ROOT, Catalog.UserRole.ADMIN))) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "No tiene los permisos para poder realizar esta acción.");
        }
        return userRepository.findAllPopulated();
    }

    /**
     * Este metodo es de solo lectura (no se adhiere a una transacción existente) encargado de consultar todos los usuarios
     * registrados en base de datos.
     *
     * @return Lista de {@link User} registrados en la base de datos.
     * @author Carlos Ramos (cramosl3@miumg.edu.gt)
     */
    @Transactional(readOnly = true)
    public List<UserDetailProjection> getAllUsersCompanies(HttpHeaders headers) {
        final String username = JwtUtil.parseToken(headers.getFirst("Authorization"));
        if (!userRoleRepository.existsByIdUserAndIdRoleIsIn(username, Arrays.asList(Catalog.UserRole.ROOT, Catalog.UserRole.ADMIN))) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "No tiene los permisos para poder realizar esta acción.");
        }
        return userRepository.findAllCompanies();
    }

    /**
     * Este metodo es de solo lectura (no se adhiere a una transacción existente) encargado de consultar todos los usuarios
     * registrados en base de datos.
     *
     * @return Usuario {@link User} registrado en la base de datos.
     * @author Carlos Ramos (cramosl3@miumg.edu.gt)
     */
    @Transactional(readOnly = true)
    public UserProfileDto getUserProfile(String userId) {
        final User user = userRepository.findById(userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado."));

        return UserProfileDto.builder()
                .username(user.getIdUser())
                .name(user.getName())
                .lastName(user.getLastName())
                .roles(getRolesByUser(user.getIdUser()))
                .email(user.getEmail())
                .state(user.getState())
                .build();
    }

    /**
     * Este metodo es de solo lectura (no se adhiere a una transacción existente) encargado de consultar todos los usuarios
     * registrados en base de datos.
     *
     * @return Usuario {@link User} registrado en la base de datos.
     * @author Carlos Ramos (cramosl3@miumg.edu.gt)
     */
    @Transactional(readOnly = true)
    public User getUser(String userId) {
        return userRepository.findById(userId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado.")
        );
    }

    /**
     * Este metodo es escritura (se adhiere a una transacción existente) encargado de consultar todos los usuarios
     * registrados en base de datos.
     *
     * @return Usuario {@link User} registrado en la base de datos.
     * @author Carlos Ramos (cramosl3@miumg.edu.gt)
     */
    @Transactional
    public UserCreatedDto createUser(HttpHeaders headers, CreateUserDto dto) {
        final String username = JwtUtil.parseToken(headers.getFirst("Authorization"));
        if (!userRoleRepository.existsByIdUserAndIdRoleIsIn(username, Arrays.asList(Catalog.UserRole.ROOT, Catalog.UserRole.ADMIN))) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "No tiene los permisos para poder realizar esta acción.");
        }
        final User user = User.fromDto(dto);
        final String login = generateEmail(dto.getName(), dto.getLastName());
        final UserCreatedDto userCreatedDto = new UserCreatedDto(login, dto.getEmail(), generatePassword());

        user.setIdUser(login);
        user.setEmail(userCreatedDto.getEmail());
        user.setPassword(BCrypt.hashpw(userCreatedDto.getPassword(), BCrypt.gensalt()));

        userRepository.save(user);

        dto.getRoles().forEach(role -> userRoleRepository.save(new com.g5.ssmr.models.UserRole(role, login)));

        return userCreatedDto;
    }

    /**
     * Este metodo es de solo lectura (no se adhiere a una transacción existente) se encarga de consultar todos los roles
     * de un usuario registrados en base de datos.
     *
     * @return Lista de {@link UserRole} registrados en la base de datos.
     * @author Carlos Ramos (cramosl3@miumg.edu.gt)
     */
    @Transactional(readOnly = true)
    public List<UserRole> getRolesByUser(String userId) {
        return userRoleRepository.getRolesDetail(userId);
    }

    /**
     * Este metodo es de solo lectura (no se adhiere a una transacción existente) se encarga de consultar todos los roles
     * de un usuario registrados en base de datos.
     *
     * @return Lista de {@link UserRole} registrados en la base de datos.
     * @author Carlos Ramos (cramosl3@miumg.edu.gt)
     */
    @Transactional
    public boolean changePassword(@Nullable HttpHeaders headers, ChangePassDto dto) {
        if (headers != null) {
            if (!dto.getUsername().equalsIgnoreCase(JwtUtil.parseToken(headers.getFirst("Authorization")))) {
                revokeToken(headers);
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "No tiene los permisos para poder realizar esta accion.");
            }
        }

        final User user = userRepository.findByEmailOrIdUser(dto.getUsername(), dto.getUsername().split("@")[0]).orElseThrow(() -> {
                    revokeToken(headers);
                    return new ResponseStatusException(HttpStatus.UNAUTHORIZED, "No tiene los permisos para poder realizar esta accion.");
                }
        );

        if (!BCrypt.checkpw(dto.getOldPass(), user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "La contraseña ingresada es incorrecta.");
        }

        user.setPassword(BCrypt.hashpw(dto.getNewPass(), BCrypt.gensalt()));

        if(user.getState().equals(Catalog.UserStatus.FIRST_LOGIN)) {
            user.setState(Catalog.UserStatus.ACTIVE);
        }
        return true;
    }

    /**
     * Este metodo es de solo lectura (no se adhiere a una transacción existente) se encarga de consultar todos los roles
     * de un usuario registrados en base de datos.
     *
     * @return Lista de {@link UserRole} registrados en la base de datos.
     * @author Carlos Ramos (cramosl3@miumg.edu.gt)
     */
    @Transactional
    public boolean requestChangePassword(String username) {
        final StringBuilder token = new StringBuilder();

        final User user = userRepository.findByEmailOrIdUser(username, username.split("@")[0]).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.OK, "Si el usuario ingresado es correcto recibirá un correo con el código de recuperación")
        );

        String[] letters = new String[]{"a", "b", "c", "d", "e", "f", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};
        for (int i = 0; i < 6; i++) {
            int pos = (int) Math.floor((Math.random() * 15));
            token.append(letters[pos]);
        }

        user.setPassword(BCrypt.hashpw(token.toString(), BCrypt.gensalt()));
        System.out.printf("New Token: %s\n", token);
        return true;
    }

    @Transactional(rollbackFor = Exception.class)
    public UserLoggedDto authUser(UserDto dto) {
        final String msg = "Credenciales invalidas. Por favor, revise que el usuario y la contraseña sean los correctos.";
        final User user = userRepository.findByEmailOrIdUser(dto.getUsername(), dto.getUsername().split("@")[0]).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, msg)
        );
        if (!BCrypt.checkpw(dto.getPassword(), user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, msg + ", " + dto.getPassword() + ", " + user.getPassword());
        }

        user.setToken(JwtUtil.generateToken(dto.getUsername()));

        return UserLoggedDto.builder()
                .username(user.getIdUser())
                .token(user.getToken())
                .roles(getRolesByUser(user.getIdUser()))
                .build();
    }

    @Transactional(rollbackFor = Exception.class)
    public UserLoggedDto existingLogin(String username) {
        final String msg = "Credenciales invalidas. Por favor, revise que el usuario y la contraseña sean los correctos.";
        final User user = userRepository.findByEmailOrIdUser(username, username.split("@")[0]).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, msg)
        );

        if (JwtUtil.isValidToken(username)) return UserLoggedDto.builder()
                .username(user.getIdUser())
                .token(user.getToken())
                .roles(getRolesByUser(user.getIdUser()))
                .build();

        throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, msg);
    }

    @Transactional
    public UserLoggedDto refreshToken(HttpHeaders headers) {
        final User user = getTokenByHeader(headers);
        user.setToken(JwtUtil.generateToken(user.getIdUser()));
        return UserLoggedDto.builder()
                .username(user.getIdUser())
                .token(user.getToken())
                .roles(getRolesByUser(user.getIdUser()))
                .build();
    }

    @Transactional
    public void revokeToken(HttpHeaders headers) {
        final User user = getTokenByHeader(headers);
        user.setToken(null);
    }

    /**
     * Este metodo es de solo lectura (no se adhiere a una transacción existente) encargado de actualizar un usuario
     * registrado en base de datos.
     *
     * @author Carlos Ramos (cramosl3@miumg.edu.gt)
     */
    @Transactional
    public void updateUser(String userId, EditUserDto dto) {
        final User user = userRepository.findById(userId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado.")
        );

        user.setState(dto.getState());
        userRoleRepository.deleteByIdUser(userId);
        dto.getRoles().forEach(role -> userRoleRepository.save(new com.g5.ssmr.models.UserRole(role, userId)));
    }

    @Transactional(readOnly = true)
    public boolean validateToken(String userId, String token) {
        final User user = userRepository.findByEmailOrIdUser(userId, userId.split("@")[0]).orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "No tiene los permisos para poder realizar esta accion.")
        );

        if (!BCrypt.checkpw(token, user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "El token ingresado no es válido.");
        }
        return true;
    }

    /**
     * Este metodo no es de solo lectura (se adhiere a una transacción existente) encargado de eliminar un usuario
     * registrado en base de datos.
     *
     * @author Carlos Ramos (cramosl3@miumg.edu.gt)
     */
    @Transactional
    public boolean deleteUser(String userId) {
        try {
            userRoleRepository.deleteByIdUser(userId);
            userRepository.deleteById(userId);

            return true;
        } catch (Exception e) {
            log.error("Error al eliminar usuario: {}", e.getMessage());
            return false;
        }
    }

    public void createAdminUser() {
        if (userRepository.existsById(adminUser)) return;

        final User user = new User("Admin", "User", 1);
        final UserCreatedDto userCreatedDto = new UserCreatedDto(adminUser, adminUser + "@ssmr.com", adminPass);

        user.setIdUser(userCreatedDto.getUsername());
        user.setEmail(userCreatedDto.getEmail());
        user.setPassword(BCrypt.hashpw(userCreatedDto.getPassword(), BCrypt.gensalt()));

        userRepository.save(user);

        userRoleRepository.save(new com.g5.ssmr.models.UserRole(Catalog.UserRole.ROOT, userCreatedDto.getUsername()));
    }

    private String generateEmail(String name, String lastName) {
        final String[] nameParts = name.split(" ");
        final String names = nameParts.length == 1 ? String.valueOf(nameParts[0].charAt(0)) : Arrays.stream(nameParts).reduce((s, s2) -> String.valueOf(s.charAt(0)) + s2.charAt(0)).orElse("");
        final String lastNames = Arrays.stream(lastName.split(" ")).reduce((s, s2) -> s + s2.charAt(0)).orElse("");

        return (names + lastNames + getMagicNumber(new Date())).toLowerCase(Locale.ROOT);
    }

    private String generatePassword() {
        return Long.toString(System.currentTimeMillis() * 2, 36) + "$";
    }

    private int getMagicNumber(Date date) {
        final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String dateStr = format.format(date).replaceAll("-", "");

        while (dateStr.length() > 1) {
            dateStr = Arrays.stream(dateStr.split("")).reduce((s, s2) -> String.valueOf(Integer.parseInt(s) + Integer.parseInt(s2))).orElse("");
        }
        return Integer.parseInt(dateStr);
    }

    private User getTokenByHeader(HttpHeaders headers) {
        return userRepository.findById(JwtUtil.parseToken(headers.getFirst("Authorization"))).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado.")
        );
    }
}
