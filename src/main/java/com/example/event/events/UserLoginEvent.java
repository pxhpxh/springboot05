package com.example.event.events;


import com.example.dto.authentication.LoginRequest;
import com.example.event.BaseEvent;
import org.springframework.stereotype.Service;

@Service("UserLoginEvent")
public class UserLoginEvent implements BaseEvent {
    private LoginRequest loginRequest;

    public LoginRequest getLoginRequest() {
        return loginRequest;
    }

    public void setLoginRequest(LoginRequest loginRequest) {
        this.loginRequest = loginRequest;
    }

    @Override
    public String getEventName() {
        return "UserLoginEvent";
    }
}
