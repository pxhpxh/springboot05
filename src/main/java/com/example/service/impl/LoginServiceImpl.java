package com.example.service.impl;


import com.example.bean.OrgCorpPO;
import com.example.bean.OrgUserPO;
import com.example.common.ResponseData;
import com.example.config.IAuthenticationFacade;
import com.example.config.RedisConfig;
import com.example.dto.authentication.*;
import com.example.event.EventService;
import com.example.event.EventTriggerMode;
import com.example.event.events.UserLoginEvent;
import com.example.exception.BaseBusinessException;
import com.example.service.LoginService;
import com.example.service.OrgCorpAuthService;
import com.example.service.OrgGroupService;
import com.example.util.JWTUtil;
import com.example.util.Strings;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Service
public class LoginServiceImpl implements LoginService {
    private AuthenticationManager authenticationManagerBean;
    private IAuthenticationFacade authenticationFacade;
    private RedisTemplate redisTemplate;
    private OrgGroupService orgGroupService;
    private OrgCorpAuthService orgCorpAuthService;

    @Autowired
    public void setAuthenticationManagerBean(AuthenticationManager authenticationManagerBean) {
        this.authenticationManagerBean = authenticationManagerBean;
    }

    @Autowired
    public void setAuthenticationFacade(IAuthenticationFacade authenticationFacade) {
        this.authenticationFacade = authenticationFacade;
    }

    @Autowired
    public void setRedisTemplate(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }


    @Autowired
    public void setOrgGroupService(OrgGroupService orgGroupService) {
        this.orgGroupService = orgGroupService;
    }
    @Autowired
    public void setOrgCorpAuthService(OrgCorpAuthService orgCorpAuthService) {
        this.orgCorpAuthService = orgCorpAuthService;
    }

    private static final String REDIS_PRE = "token:";
    @Override
    public ResponseData<LoginResponse> login(LoginRequest login) {
        UserLoginEvent userLoginEvent=new UserLoginEvent();
        userLoginEvent.setLoginRequest(login);
        try {//登录前事件
            EventService.trigger(userLoginEvent, EventTriggerMode.before);
        } catch (Exception e){
            return ResponseData.error(e.getMessage());
        }
        //校验验证码
        if(Strings.isBlank(login.getCId())||Strings.isBlank(login.getVerificationCode())){
            return ResponseData.error("验证码不正确");
        }

        ValueOperations<String, String> verifyCodes=redisTemplate.opsForValue();
        String temKey="verify_code:" + login.getCId();
        String code = verifyCodes.get(temKey);
        //redisTemplate.delete(temKey);
        if(Strings.isBlank(code)){
            return ResponseData.error(10001,"验证码过期");
        }
        if(!code.equalsIgnoreCase(login.getVerificationCode())){
            return ResponseData.error("验证码不正确");
        }
        //获取AuthenticationManager authenticate进行认证
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(login.getUsername(), login.getPassword());
        Authentication authenticate = null;
        try {
            authenticate = authenticationManagerBean.authenticate(authenticationToken);
        } catch (BadCredentialsException e) {//登录失败
            return ResponseData.error("用户名或密码错误!");
        }

        //如果认证没通过，给出对应的提示
        if (Objects.isNull(authenticate)) {
            return ResponseData.error("用户名或密码错误!");
        }
        LoginUser user = (LoginUser) authenticate.getPrincipal();
        //String userId = user.getOrgUserPO().getId().toString();
        //如果认证通过了，使用userid生成一个jwt jwt存入ResponseData中
        //String jwt = JWTUtil.createToken(userId);
        String jwt = JWTUtil.createToken(user.getUid());
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setToken(jwt);
        //把完整的用户信息存入redis userid作为key
        List<OrgCorpPO> curUserCorps = orgCorpAuthService.getUserCorps(user.getOrgUserPO().getId());

        LoginUserRedis loginUserRedis=new LoginUserRedis();
        loginUserRedis.setLoginUser(user);
        loginUserRedis.setCorps(curUserCorps);
        loginUserRedis.setCurrentCorpId(user.getOrgUserPO().getCorpId());
        ValueOperations<String, LoginUserRedis> operations = redisTemplate.opsForValue();
        operations.set(REDIS_PRE + user.getUid(), loginUserRedis, RedisConfig.REDIS_TIMEOUT, TimeUnit.MINUTES);
        try {//登录成功后事件
            EventService.trigger(userLoginEvent, EventTriggerMode.after);
        } catch (Exception e){
            e.printStackTrace();
        }
        return ResponseData.success(loginResponse);
    }

    @Override
    public ResponseData<UserResponse> getUserByToken() {
        Authentication authentication = authenticationFacade.getAuthentication();
        if(Objects.isNull(authentication)){
            return ResponseData.error(50008,"token已过期");
        }
        LoginUser user= (LoginUser) authentication.getPrincipal();

        ValueOperations<String,LoginUserRedis> operations=redisTemplate.opsForValue();
        LoginUserRedis loginUserRedis=operations.get(REDIS_PRE + user.getUid());
        LoginUser loginUser =loginUserRedis.getLoginUser();
        if(Objects.isNull(loginUser)){
            return ResponseData.error(50008,"token已过期");
        }
        UserResponse userResponse=new UserResponse();
        BeanUtils.copyProperties(loginUser.getOrgUserPO(),userResponse);
        userResponse.setRoles(user.getPermissions().toArray(new String[0]));
        userResponse.setCorps(loginUserRedis.getCorps());
        userResponse.setCurrentCorpId(loginUserRedis.getCurrentCorpId());
        return ResponseData.success(userResponse);
    }

    @Override
    public ResponseData<String> logout() {
        Authentication authentication = authenticationFacade.getAuthentication();

        if(Objects.isNull(authentication)){
            return ResponseData.error(50008,"token已过期");
        }
        LoginUser user= (LoginUser) authentication.getPrincipal();
        redisTemplate.delete(REDIS_PRE+user.getUid());
        return ResponseData.success("退出登录");
    }

    @Override
    public OrgUserPO getLoginUser() {
        LoginUser user = getSecurityLoginUser();
        if(user!=null){
            return user.getOrgUserPO();
        }
        return null;
    }

    @Override
    public LoginUser getSecurityLoginUser() {
        Authentication authentication = authenticationFacade.getAuthentication();

        if(Objects.isNull(authentication)){
            return null;
        }
        LoginUser user= (LoginUser) authentication.getPrincipal();
        return user;
    }

    @Override
    public Long getLoginUserId() {
        OrgUserPO loginUser = getLoginUser();
        if(loginUser!=null){
            return loginUser.getId();
        }
        return null;
    }

    /**
     * 当前单位切换
     * @param corpId
     * @throws BaseBusinessException
     */
    public void changeCurrentCorp(Long corpId) throws BaseBusinessException {
        LoginUserRedis loginUserRedis = getCurrentUserRedis();
        if(loginUserRedis!=null){//当前用户已登录
            List<OrgCorpPO> corps = loginUserRedis.getCorps();
            long count=corps.stream().filter(orgCorpPO -> orgCorpPO.getId()==corpId).count();
            if(count>0){//在授权范围内
                loginUserRedis.setCurrentCorpId(corpId);
                ValueOperations<String, LoginUserRedis> operations = redisTemplate.opsForValue();
                operations.set(REDIS_PRE + loginUserRedis.getLoginUser().getUid(), loginUserRedis, RedisConfig.REDIS_TIMEOUT, TimeUnit.MINUTES);
            } else {
                throw new BaseBusinessException("单位切换失败：无该单位访问权限！");
            }
        }
    }

    /**
     * 获取当前集团ID
     * @return
     */
    public Long getCurrentGroupId(){
        OrgUserPO loginUser = getLoginUser();
        if(loginUser!=null){
            return loginUser.getGroupId();
        }
        return null;
    }
    /**
     * 获取用户当前单位
     * @return
     */
    public Long getCurrentCorpId(){
        LoginUserRedis loginUserRedis = getCurrentUserRedis();
        if(loginUserRedis!=null){
            return loginUserRedis.getCurrentCorpId();
        }
        return null;
    }

    /**
     * 获取当前登录用户缓存对象
     * @return
     */
    public LoginUserRedis getCurrentUserRedis(){
        LoginUser securityLoginUser = getSecurityLoginUser();
        if(securityLoginUser==null){
            return null;
        }
        ValueOperations<String,LoginUserRedis> operations=redisTemplate.opsForValue();
        LoginUserRedis loginUserRedis=operations.get(REDIS_PRE + securityLoginUser.getUid());
        return loginUserRedis;
    }
}
