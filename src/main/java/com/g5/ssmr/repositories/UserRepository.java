package com.g5.ssmr.repositories;

import com.g5.ssmr.models.User;
import com.g5.ssmr.projections.UserDetailProjection;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends CrudRepository<User, String> {

    /**
     * Este metodo se encarga de realizar una consulta a la base de datos y retornar todos los usuarios registrados en
     * la misma.
     *
     * @return Lista de {@link User} registrados en la base de datos.
     * @author Carlos Ramos (cramosl3@miumg.edu.gt)
     */
    @Override
    List<User> findAll();

    @Query(value = "select\n" +
            "\tus.id_user as username, (us.name || ' ' || us.last_name) as name, us.email, cc.name state\n" +
            "from g5_ssmr.users us\n" +
            "inner join g5_ssmr.catalogue_child cc on us.state = cc.id_catalogue_child", nativeQuery = true)
    List<UserDetailProjection> findAllPopulated();

    Optional<User> findByEmailOrIdUser(String email, String idUser);
}
