package youapp.dataaccess.dao;

import java.util.List;

import youapp.dataaccess.dto.TagDto;
import youapp.model.Tag.Category;

public interface ITagDao {
	public TagDto getById(Long tagId);

	public List<TagDto> getByNameAndCategory(String name, Integer category);

	public List<TagDto> getByPerson(Long personId);

	public List<TagDto> getByCategory(Integer category);

	public List<TagDto> getAll();

	public boolean exists(String name, Integer category);

	public void addAssociation(Long personId, Long tagId);

	public boolean existsAssociation(Long personId, Long tagId);

	public void removeAssociation(Long personId, Long tagId);

	public void removeAllAssociations(Long personId);

	public Long create(TagDto tag);

	public void delete(Long tagId);

	public Integer getNumberOfCommonTagsByCategory(Long personIdA, Long personIdB,
			Category category);

	public Integer getNumberOfTagsByCategory(Long personId, Category category);
}
