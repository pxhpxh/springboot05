package com.example.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.bean.User;
import com.example.dto.grid.Filter;
import com.example.dto.grid.Pager;
import com.example.dto.org.UserVo;
import com.example.mapper.UserMapper;
import com.example.service.UserService;
import com.github.yulichang.toolkit.JoinWrappers;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import org.springframework.stereotype.Service;


import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author pxh
 * @since 2024-01-19
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Override
    public Pager<User> getPage(Pager pager) {
        MPJLambdaWrapper<User> lqw= JoinWrappers.lambda(User.class);
        lqw.selectAll(User.class)
                .selectAs(User::getUsername, UserVo::getUsername);
                //.leftJoin(BankTypePO.class,BankTypePO::getId,BankPO::getType)

        List<Filter> filters = pager.getFilters();
        for (Filter filter : filters) {
            if ("username".equals(filter.getFieldName())) {
                lqw.like(User::getUsername, filter.getParValue1());
            }
        }

      /*  lqw.and((wrapper)->{
            wrapper.eq(BankPO::getGroupId,0L).or().eq(BankPO::getGroupId, loginService.getCurrentGroupId());
        });*/
        //lqw.orderByDesc(BankPO::getCreateTime);
        this.baseMapper.selectJoinPage(pager,UserVo.class,lqw);
        return pager;
    }


}
