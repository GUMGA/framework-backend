package gumga.framework.application;

import gumga.framework.domain.GumgaUserData;
import gumga.framework.domain.repository.GumgaCrudRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface GumgaUserDataRepository extends GumgaCrudRepository<GumgaUserData, Long> {

    @Query(value = "from GumgaUserData gud where gud.userLogin=:userLogin and gud.key=:key")
    public GumgaUserData findByUserLoginAndKey(@Param(value = "userLogin") String userLogin, @Param(value = "key") String key);

}
