package com.units.backend.repository.user;

import com.units.backend.model.user.EnumRole;
import com.units.backend.model.user.Role;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface RoleRepository extends MongoRepository<Role, String> {
    Optional<Role> findByName(EnumRole name);
}
