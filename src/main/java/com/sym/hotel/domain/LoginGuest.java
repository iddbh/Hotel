package com.sym.hotel.domain;

import com.alibaba.fastjson.annotation.JSONField;
import com.sym.hotel.pojo.Guest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginGuest implements UserDetails {
    private Guest guest;
    private List<String> permissions;
    @JSONField(serialize = false)
    private List<SimpleGrantedAuthority> authorities;
    public LoginGuest(Guest guest, List<String> permissions){
        this.guest=guest;
        this.permissions=permissions;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if(authorities!=null){
            return authorities;
        }
        //把permissions中的权限信息封装SimpleGrantedAuthority对象
        authorities= permissions.stream().map(SimpleGrantedAuthority::new).toList();
        return authorities;
    }


    @Override
    public String getPassword() {
        return guest.getPassword();
    }

    @Override
    public String getUsername() {
        return guest.getName();
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
        return true;
    }
}
