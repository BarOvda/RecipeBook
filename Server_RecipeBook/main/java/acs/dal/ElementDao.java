package acs.dal;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;

import acs.data.ElementEntity;

public interface ElementDao extends PagingAndSortingRepository<ElementEntity, Long> {
	
	public List<ElementEntity> findAllByParents_elementId(
			@Param("parentId") Long parentId, 
			Pageable pageable
			);
	
	public List<ElementEntity> findAllByParents_elementIdAndActive(
			@Param("parentId") Long parentId,
			@Param("active") Boolean active,
			Pageable pageable
			);
	
	public List<ElementEntity> findAllByChildren_elementId(
			@Param("childrenId") Long childId, 
			Pageable pageable
			);
	
	public List<ElementEntity> findAllByChildren_elementIdAndActive(
			@Param("childrenId") Long childId,
			@Param("active") Boolean active,
			Pageable pageable
			);
	
	public List<ElementEntity> findAllByName(
			@Param("name") String name,
			Pageable pageable
			); 
	
	public List<ElementEntity> findAllByNameAndActive(
			@Param("name") String name,
			@Param("active") Boolean active,
			Pageable pageable
			); 
	
	public List<ElementEntity> findAllByType(
			@Param("type") String type,
			Pageable pageable
			);
	
	public List<ElementEntity> findAllByTypeAndActive(
			@Param("type") String type,
			@Param("active") Boolean active,
			Pageable pageable
			);
	
	public Optional<ElementEntity> findByElementIdAndActive(
			@Param("elementId") Long id,
			@Param("active") Boolean active
			);
	
	public List<ElementEntity> findAllByActive(
			@Param("active") Boolean active,
			Pageable pageable
			);
	
	public List<ElementEntity> findAllByTypeAndCreatedBy_email(
			@Param("type") String type,@Param("email") String email,
			Pageable pageable
			);
	
	public List<ElementEntity> findAllByTypeAndNameLike(
			@Param("type") String type,
			@Param("name") String name,
			Pageable pageable
			);
}
