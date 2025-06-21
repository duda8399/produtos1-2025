package edu.ifmg.produtos.services;

import edu.ifmg.produtos.dto.RoleDTO;
import edu.ifmg.produtos.entities.Role;
import edu.ifmg.produtos.repositories.RoleRepository;
import edu.ifmg.produtos.services.exceptions.ResourceNotFound;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
