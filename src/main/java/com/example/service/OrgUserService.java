package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.bean.OrgUserPO;
import com.example.dto.authentication.UserResponse;
import com.example.dto.org.AddOrEditUserResponse;
import com.example.dto.grid.Pager;
import com.example.dto.org.EditPasswordRequest;
import com.example.exception.BaseBusinessException;


/**
 * <p>
 *  服务类
 * </p>
 *
 * @author pxh
 * @since 2023-04-14 15:58:36
 */
public interface OrgUserService extends IService<OrgUserPO> {
    String createToken(UserResponse userResponse);
    UserResponse getUserByToken(String token);
    void removeToken(String token);
    void removeTokenByLoginName(String loginName);
    OrgUserPO getUserByLoginName(String loginName);
    boolean isExist(String loginName, Long id);
    boolean isExistCode(String code, Long id);
    void saveOrEdit(AddOrEditUserResponse user) throws BaseBusinessException;
    void editPassword(EditPasswordRequest editPassword) throws BaseBusinessException;
    Pager<OrgUserPO> getPage() throws BaseBusinessException;
    Pager<AddOrEditUserResponse> getPage(Pager<OrgUserPO> pager) throws BaseBusinessException;
}
