package com.toy.component.user.mapper;

import java.util.List;
import java.util.Set;

import org.apache.ibatis.annotations.Param;

import com.toy.component.user.domain.UserVo;

public interface IUserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserVo record);

    int insertSelective(UserVo record);

    UserVo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserVo record);

    int updateByPrimaryKey(UserVo record);

    List<UserVo> getAllOrders();

    UserVo getOrderByPrimaryKey(Integer id);

    void insertGroup(@Param("groupId") String groupId, @Param("addressIds") Set<Integer> addressIds);

}