package com.xiaobai.mapping;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xiaobai.pojo.entity.UserBean;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<UserBean> {
}
