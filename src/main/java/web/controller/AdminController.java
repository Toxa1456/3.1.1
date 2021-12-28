package web.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import web.model.Role;
import web.model.User;
import web.service.RoleService;
import web.service.UserService;
import javax.validation.Valid;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public AdminController(UserService userRepository, RoleService roleRepository) {
        this.roleService = roleRepository;
        this.userService = userRepository;
    }

    @RequestMapping("/")
    public String getUsers(ModelMap model) {
        model.addAttribute("Users", userService.findAllUsers());
        return "admin";
    }

    @RequestMapping("/adduser")
    public String addUser(@RequestParam Optional<String> role, @Valid User user, BindingResult result) {
        if (result.hasErrors()) {
            return "addUser";
        }
        if (role.get().equalsIgnoreCase("1,2")){
            Set<Role> roles = roleService.findAllRoles();
            user.setRoles(roles);
        } else {
            if (role.get().equals("1")) {
                user.setRoles(Collections.singleton(roleService.findRoleById(2L)));
            } else {
                if (role.get().equals("2")) {
                    user.setRoles(Collections.singleton(roleService.findRoleById(1L)));
                } else {
                    return "addUser";
                }
            }
        }
        userService.save(user);
        return "redirect:/admin/";
    }

    @RequestMapping("/update/{id}")
    public String updateUser(@RequestParam Optional<String> role, @PathVariable("id") long id, @Valid User user, BindingResult result) {
        if (result.hasErrors()) {
            User original = userService.findById(id);
            user.setName(original.getName());
            user.setPassword(original.getPassword());
            user.setEmail(original.getEmail());
            user.setRoles(original.getRoles());
            user.setId(id);
            return "changeUser";
        }
        if (role.get().equalsIgnoreCase("1,2")) {
            Set<Role> roles = roleService.findAllRoles();
            user.setRoles(roles);
        } else {
            if (role.get().equals("1")) {
                user.setRoles(Collections.singleton(roleService.findRoleById(2L)));
            } else {
                if (role.get().equals("2")) {
                    user.setRoles(Collections.singleton(roleService.findRoleById(1L)));
                } else {
                    return "changeUser";
                }
            }
        }
        userService.save(user);

        return "redirect:/admin/";
    }

    @RequestMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") long id) {
        userService.deleteById(id);
        return "redirect:/admin/";
    }
}
