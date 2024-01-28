package com.example.dto.authentication;

import com.alibaba.fastjson.annotation.JSONField;
import com.example.bean.OrgUserPO;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class LoginUser implements UserDetails {
    private String uid;
    private OrgUserPO orgUserPO;
    private List<String> permissions;
    @JSONField(serialize = false)
    private List<SimpleGrantedAuthority> authorities;

    public LoginUser(OrgUserPO orgUserPO, List<String> permissions, String uid){
        this.orgUserPO=orgUserPO;
        this.permissions=permissions;
        this.uid=uid;
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {//返回权限信息
        //把authorities中String类型的权限信息封装成SimpleGrantedAuthority对象
        if(authorities==null && permissions!=null) {
            authorities = permissions
                    .stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
        }
        return authorities;
    }

    @Override
    @JSONField(serialize = false)
    public String getPassword() {//获取密码
        return orgUserPO.getPassword();
    }

    @Override
    @JSONField(serialize = false)
    public String getUsername() {//获取用户名
        return orgUserPO.getLoginName();
    }

    @Override
    @JSONField(serialize = false)
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    @JSONField(serialize = false)
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    @JSONField(serialize = false)
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    @JSONField(serialize = false)
    public boolean isEnabled() {
        return orgUserPO.getState()==0 && !orgUserPO.getDeleted();
    }
}
