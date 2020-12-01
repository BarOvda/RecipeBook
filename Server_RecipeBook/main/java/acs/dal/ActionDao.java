package acs.dal;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import acs.data.ActionEntity;

public interface ActionDao extends  PagingAndSortingRepository<ActionEntity, Long> {
	
	public List<ActionEntity> findAllByTypeAndInvokedBy_email(
			@Param("type") String type,
			@Param("invokedBy") String invokedBy,
			Pageable pageable
			);
	
}
