package acs.dal;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import acs.data.UserEntity;

public interface UserDao extends PagingAndSortingRepository<UserEntity, String> {
	
	public List<UserEntity> findAllByUsernameLike(
			@Param("username") String username,
			Pageable pageable
			);
}
