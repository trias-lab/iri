package com.trias.resouce.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.trias.resouce.body.request.QueryUserRequest;
import com.trias.resouce.model.Resource;
import com.trias.resouce.model.User;

public interface UserInfoMapper {

	List<Resource> getResourceByUserRole(@Param("roleList") List<String> roleList);

	User getUserByName(@Param("username") String username);

	void insertUser(@Param("user")User user);

	void updateUserByName(@Param("user")User user);

	String getUserRoleByName(@Param("username") String username);

	List<User> getUserList(QueryUserRequest request);

	int getUserCount(QueryUserRequest request);
}
