package ir.maktab.arf.quiz.security;

import ir.maktab.arf.quiz.entities.*;
import ir.maktab.arf.quiz.utilities.StatusTitle;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@NoArgsConstructor
public class MyUserDetail implements UserDetails {

    private String username;
    private String password;
    private List<Role> roles;
    private Status status;
    private PersonalInfo personalInfo;

    public MyUserDetail(Account account) {
        this.username = account.getUsername();
        this.password = account.getPassword();
        this.status = account.getStatus();
        this.roles = account.getRoles();
        this.personalInfo = account.getPersonalInfo();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        for (Role roleItem : roles){
            for (Privilege privilegeItem : roleItem.getPrivileges())
                grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_" + privilegeItem.getTitle().name()));
        }
        return grantedAuthorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
//        return (status.getTitle().equals(StatusTitle.ACTIVE)?true:false);
        return true;
    }
}
