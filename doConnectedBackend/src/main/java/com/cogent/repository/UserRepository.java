package com.cogent.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.cogent.model.UserDao;
import com.cogent.model.UserDto;
public interface UserRepository extends JpaRepository<UserDao, Integer> {

	UserDao findByUsername(String username);

	UserDao findById(int id);

	List<UserDao> findByEmail(String email);

	List<UserDao> findByName(String name);

	boolean existsByUsername(String username);
	
	@Query(value = "select email from user where username=?1",nativeQuery = true)
	String findbyUsername(String username);
	
	
	@Query(value = "select * from user where role=?1",nativeQuery = true)
	List<UserDao> findAllByRole(String role);

	@Query(value = "select * from user where username!=?1",nativeQuery = true)
	List<UserDao> getAllExceptMe(String username);

	//UserDao update(UserDao u);
}