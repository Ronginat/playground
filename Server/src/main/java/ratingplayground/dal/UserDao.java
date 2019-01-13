package ratingplayground.dal;

import org.springframework.data.repository.CrudRepository;

import ratingplayground.logic.users.UserEntity;


public interface UserDao extends CrudRepository<UserEntity	, String> {

}
