package edu.ifmg.produtos.services;

import edu.ifmg.produtos.dto.RoleDTO;
import edu.ifmg.produtos.entities.Role;
import edu.ifmg.produtos.repository.RoleRepository;
import edu.ifmg.produtos.services.exceptions.DatabaseException;
import edu.ifmg.produtos.services.exceptions.ResourceNotFound;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import java.util.stream.Collectors;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Transactional(readOnly = true)
    public Page<RoleDTO> findAll(Pageable pageable) {
        Page<Role> list = roleRepository.findAll(pageable);
        return list.map(RoleDTO::new);
    }

    @Transactional(readOnly = true)
    public RoleDTO findById(Long id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFound("Role not found"));
        return new RoleDTO(role);
    }
}
