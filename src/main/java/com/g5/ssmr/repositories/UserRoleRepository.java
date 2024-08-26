package com.g5.ssmr.repositories;

import com.g5.ssmr.models.User;
import com.g5.ssmr.models.UserRole;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRoleRepository extends CrudRepository<UserRole, Integer> {

    /**
     * Este metodo se encarga de realizar una consulta a la base de datos y retornar todos los roles de usuarios
     * registrados en la misma.
     *
     * @author Carlos Ramos (cramosl3@miumg.edu.gt)
     * @return Lista de {@link User} registrados en la base de datos.
     * */
    @Override
    List<UserRole> findAll();

    boolean existsByIdUserAndIdRoleIsIn(String idUser, List<Integer> idRole);

    void deleteByIdUser(String idUser);

    @Query(value = "select ur.id_role as \"idRole\", cc.\"name\" from g5_ssmr.user_roles ur\n" +
            "inner join g5_ssmr.catalogue_child cc on ur.id_role = cc.id_catalogue_child\n" +
            "where ur.id_user = :username", nativeQuery = true)
    List<com.g5.ssmr.projections.UserRole> getRolesDetail(@Param("username") String username);
}
