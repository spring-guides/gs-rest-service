package com.example.restservice.entity;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface UserMapper {

    @Select("select USER_AGE from user where USER_ID = #{userId} for update")
    int getAgeByUserIdForUpdate(int userId);

    @Select("select USER_ID,USER_NAME,USER_SEX,USER_AGE from USER where USER_ID=#{userId}")
    User findUserById(int userId);

    @Update("update user set USER_AGE = USER_AGE + 1 where USER_ID=#{userId}")
    int increaseAge(int userId);
}
