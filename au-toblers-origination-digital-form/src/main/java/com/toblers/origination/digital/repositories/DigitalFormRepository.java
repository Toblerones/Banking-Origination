package com.toblers.origination.digital.repositories;

import com.toblers.origination.digital.repositories.model.DigitalForm;
import com.toblers.origination.digital.repositories.model.DigitalFormId;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DigitalFormRepository extends CrudRepository<DigitalForm, DigitalFormId> {
//
//    Optional<DigitalForm> findById(DigitalFormId id);
}
