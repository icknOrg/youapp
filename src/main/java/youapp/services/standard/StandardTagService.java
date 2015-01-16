package youapp.services.standard;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import youapp.dataaccess.dao.ITagDao;
import youapp.dataaccess.dto.TagDto;
import youapp.exception.dataaccess.DataAccessLayerException;
import youapp.exception.dataaccess.InconsistentStateException;
import youapp.exception.model.InconsistentModelException;
import youapp.exception.model.ModelException;
import youapp.model.Tag;
import youapp.model.Tag.Category;
import youapp.model.TagSet;
import youapp.services.TagService;

public class StandardTagService
    implements TagService
{

    /**
     * Logger.
     */
    static final Log log = LogFactory.getLog(StandardTagService.class);

    private ITagDao tagDao;

    @Autowired
    public void setTagDao(ITagDao tagDao)
    {
        this.tagDao = tagDao;
    }

    @Override
    public Tag getById(Long tagId) throws DataAccessLayerException
    {
        if (tagId == null)
        {
            throw new IllegalArgumentException("Id must not be null.");
        }
        if (log.isDebugEnabled())
        {
            log.debug("Retrieving tag with id: " + tagId);
        }
        TagDto tagDto = tagDao.getById(tagId);
        return reassembleTag(tagDto);
    }

    @Override
    public Tag getByNameAndCategory(String name, Tag.Category category) throws DataAccessLayerException
    {
        if (name == null)
        {
            throw new IllegalArgumentException("Name must not be null.");
        }
        if (category == null)
        {
            throw new IllegalArgumentException("Category must not be null.");
        }
        if (log.isDebugEnabled())
        {
            log.debug("Retrieving tag with name and category: " + name + " " + category);
        }
        List<TagDto> tagDtos = tagDao.getByNameAndCategory(name, category.id());
        if (tagDtos != null && tagDtos.size() > 0)
            return reassembleTag(tagDtos.get(0));
        
        return null;
    }

    @Override
    public TagSet getByPerson(Long personId) throws DataAccessLayerException
    {
        if (personId == null)
        {
            throw new IllegalArgumentException("Person id must not be null.");
        }
        if (log.isDebugEnabled())
        {
            log.debug("Retrieving all tag of person with id: " + personId);
        }
        List<TagDto> tagDtos = tagDao.getByPerson(personId);
        TagSet tags = new TagSet();
        for (TagDto t : tagDtos)
        {
            if (log.isDebugEnabled())
            {
                log.debug(">>> Found tag: " + t.toString());
            }
            tags.add(reassembleTag(t));
        }
        return tags;
    }

    @Override
    public TagSet getByCategory(Category category) throws DataAccessLayerException
    {
        if (category == null)
        {
            throw new IllegalArgumentException("Category must not be null.");
        }
        if (log.isDebugEnabled())
        {
            log.debug("Retrieving tag with category: " + category);
        }
        List<TagDto> tagDtos = tagDao.getByCategory(category.id());
        TagSet tags = new TagSet();
        for (TagDto t : tagDtos)
        {
            if (log.isDebugEnabled())
            {
                log.debug(">>> Found tag: " + t.toString());
            }
            tags.add(reassembleTag(t));
        }
        return tags;
    }

    @Override
    public TagSet getAll() throws DataAccessLayerException
    {
        if (log.isDebugEnabled())
        {
            log.debug("Retrieving all tags.");
        }
        List<TagDto> tagDtos = tagDao.getAll();
        TagSet tags = new TagSet();
        for (TagDto t : tagDtos)
        {
            if (log.isDebugEnabled())
            {
                log.debug(">>> Found tag: " + t.toString());
            }
            tags.add(reassembleTag(t));
        }
        return tags;
    }

    @Override
    public boolean exists(String name, Tag.Category category) throws DataAccessLayerException
    {
        if (name == null)
        {
            throw new IllegalArgumentException("Name must not be null.");
        }
        if (category == null)
        {
            throw new IllegalArgumentException("Category must not be null.");
        }
        if (log.isDebugEnabled())
        {
            log.debug("Check whether tags with given name and category exists: " + name + " " + category);
        }
        return tagDao.exists(name, category.id());
    }

    @Override
    public Long create(Tag tag) throws DataAccessLayerException, ModelException
    {
        if (tag == null)
        {
            throw new IllegalArgumentException("Tag must not be null.");
        }
        if (tag.getId() != null)
        {
            throw new IllegalArgumentException("Tag id must not be set.");
        }

        // Perform validation checks.
        validateTag(tag);

        // Create new symptom in the database.
        TagDto tagDto = disassembleTag(tag);
        if (log.isDebugEnabled())
        {
            log.debug("Creating tag: " + tagDto);
        }
        if (exists(tag.getName(), tag.getCategory()))
        {
            if (log.isDebugEnabled())
            {
                log.debug(">>> Tag already exists! Stopped creation.");
            }
            return getByNameAndCategory(tag.getName(), tag.getCategory()).getId();
        }
        Long generatedId = tagDao.create(tagDto);
        if (log.isDebugEnabled())
        {
            log.debug(">>> Created tag with id: " + generatedId);
        }
        tag.setId(generatedId);
        return generatedId;
    }

    @Override
    public void delete(Long tagId) throws DataAccessLayerException
    {
        if (tagId == null)
        {
            throw new IllegalArgumentException("Tag id must not be null.");
        }
        if (log.isDebugEnabled())
        {
            log.debug("Deleting tag: " + tagId);
        }
        tagDao.delete(tagId);
    }

    @Override
    public void addAssociation(Long personId, Long tagId) throws DataAccessLayerException
    {
        if (personId == null)
        {
            throw new IllegalArgumentException("Person id must not be null");
        }
        if (tagId == null)
        {
            throw new IllegalArgumentException("Tag id must not be null.");
        }
        if (log.isDebugEnabled())
        {
            log.debug("Adding association between person with id " + personId + " and tag with id " + tagId + ".");
        }
        if (!existsAssociation(personId, tagId))
        {
            tagDao.addAssociation(personId, tagId);
        }
    }

    @Override
    public void createAndAssociate(Long personId, TagSet tags) throws DataAccessLayerException, ModelException
    {
        if (personId == null)
        {
            throw new IllegalArgumentException("Person id must not be null.");
        }
        if (tags == null)
        {
            throw new IllegalArgumentException("Tags must not be null.");
        }
        if (log.isDebugEnabled())
        {
            log.debug("Creating tags and corresponding associations if not present.");
        }
        Long id = null;
        for (Tag t : tags)
        {
            if (log.isDebugEnabled())
            {
                log.debug("Handling tag: " + t.toString());
            }
            // Validate.
            validateTag(t);
            // Create tag if not yet present.
            if (t.getId() == null)
            {
                if (log.isTraceEnabled())
                {
                    log.trace(">>> Found new tag. Creating: " + t.toString());
                }
                id = create(t);
                t.setId(id);
            }
            // Make association if not yet present.
            if (log.isTraceEnabled())
            {
                log.trace(">>> Adding association if not yet present.");
            }
            addAssociation(personId, t.getId());
        }
    }

    @Override
    public boolean existsAssociation(Long personId, Long tagId) throws DataAccessLayerException
    {
        if (personId == null)
        {
            throw new IllegalArgumentException("Person id must not be null.");
        }
        if (tagId == null)
        {
            throw new IllegalArgumentException("Tag id must not be null.");
        }
        if (log.isDebugEnabled())
        {
            log.debug("Check whether an association between a person with id " + personId + " and a tag with id "
                + tagId + " exists.");
        }
        return tagDao.existsAssociation(personId, tagId);
    }

    @Override
    public void removeAssociation(Long personId, Long tagId) throws DataAccessLayerException
    {
        if (personId == null)
        {
            throw new IllegalArgumentException("Person id must not be null.");
        }
        if (tagId == null)
        {
            throw new IllegalArgumentException("Tag id must not be null");
        }
        if (log.isDebugEnabled())
        {
            log.debug("Removing association between person with id " + personId + " and tag with id " + tagId + ".");
        }
        if (existsAssociation(personId, tagId))
        {
            tagDao.removeAssociation(personId, tagId);
        }
    }

    @Override
    public void removeAssociations(Long personId, TagSet tags) throws DataAccessLayerException
    {
        if (personId == null)
        {
            throw new IllegalArgumentException("Person id must not be null.");
        }
        if (tags == null)
        {
            throw new IllegalArgumentException("Tags must not be null");
        }
        if (log.isDebugEnabled())
        {
            log.debug("Removing association between person with id " + personId + " and some tags.");
        }
        for (Tag t : tags)
        {
            if (t.getId() == null)
            {
                if (log.isTraceEnabled())
                {
                    log.trace(">>> Skipped tag because of missing tag id.");
                }
            }
            else
            {
                if (log.isTraceEnabled())
                {
                    log.trace(">>> Removing association with tag with id " + t.getId() + ".");
                }
                removeAssociation(personId, t.getId());
            }
        }
    }

    @Override
    public void removeAllAssociations(Long personId) throws DataAccessLayerException
    {
        if (personId == null)
        {
            throw new IllegalArgumentException("Person id must not be null.");
        }
        tagDao.removeAllAssociations(personId);
    }

    public static void validateTag(TagDto tag) throws InconsistentStateException
    {
        if (tag == null)
        {
            throw new IllegalArgumentException("Tag must not be null.");
        }
        if (tag.getName() == null)
        {
            throw new InconsistentStateException("Name must not be null.");
        }
        if (tag.getCategory() == null)
        {
            throw new InconsistentStateException("Category must not be null.");
        }
    }

    public static void validateTag(Tag tag) throws InconsistentModelException
    {
        if (tag == null)
        {
            throw new IllegalArgumentException("Tag must not be null.");
        }
        if (tag.getName() == null)
        {
            throw new InconsistentModelException("Name must not be null.");
        }
        if (tag.getCategory() == null)
        {
            throw new InconsistentModelException("Category must not be null.");
        }
    }

    public static TagDto disassembleTag(Tag tag)
    {
        if (tag == null)
        {
            throw new IllegalArgumentException("Parameter must not be null.");
        }
        TagDto tagDto = new TagDto();
        tagDto.setId(tag.getId());
        tagDto.setCategory(tag.getCategory().id());
        tagDto.setName(tag.getName());
        return tagDto;
    }

    public static Tag reassembleTag(TagDto tagDto) throws InconsistentStateException
    {
        // Check parameter null pointers.
        if (tagDto == null)
        {
            throw new IllegalArgumentException("Parameter must not be null.");
        }

        // Validate.
        validateTag(tagDto);

        // Reassemble.
        if (log.isDebugEnabled())
        {
            log.debug("Reassembling tag: " + tagDto.toString());
        }
        Tag tag = new Tag();
        tag.setId(tagDto.getId());
        if (log.isDebugEnabled())
        {
            log.debug(">>> Id: " + tagDto.getId());
        }
        tag.setCategory(Tag.Category.getCategory(tagDto.getCategory()));
        if (log.isDebugEnabled())
        {
            log.debug(">>> Cateogry: " + tagDto.getCategory());
        }
        tag.setName(tagDto.getName());
        if (log.isDebugEnabled())
        {
            log.debug(">>> Name: " + tagDto.getName());
        }
        return tag;
    }
    
    @Override
    public Integer getNumberOfCommonTagsByCategory(Long personIdA, Long personIdB,Category category){
    	
    	if (personIdA == null)
         {
             throw new IllegalArgumentException("Person A id must not be null.");
         }
    	if (personIdB == null)
        {
            throw new IllegalArgumentException("Person B id must not be null.");
        }
    	if (category == null)
        {
            throw new IllegalArgumentException("Tag category must not be null.");
        }
    	
    	return tagDao.getNumberOfCommonTagsByCategory(personIdA, personIdB, category);
    	
    }
    
    @Override
    public Integer getNumberOfTagsByCategory(Long personId, Category category){
    	
    	if (personId == null)
        {
            throw new IllegalArgumentException("Person id must not be null.");
        }  
    	if (category == null)
    	{
           throw new IllegalArgumentException("Tag category must not be null.");
    	}
    	
    	return tagDao.getNumberOfTagsByCategory(personId, category);    	
    }

}
