package edu.ifmg.produtos.dto;

import edu.ifmg.produtos.entities.User;

public class UserDTO {

    private Long id;
    private String name;
    @NotBlank(message = "Campo obrigatório")
    private String firstName;
    private String lastName;

    @Email(message = "Favor informar um email válido")
    private String email;

    private Set<Role> roles = new HashSet<>();

    public UserDTO() { }

    public User(long id, String firstName, String lastName, String email) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public User(User user) {
        this.id = user.getId();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.email = user.getEmail();

        user.getRoles()
            .forEach(role -> {
                roles.add(new RoleDTO(role))
            });
    }
}
