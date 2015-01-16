package youapp.dataaccess.dao.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.stereotype.Repository;

import youapp.dataaccess.dao.IPersonDao;
import youapp.dataaccess.dao.filter.IFilterTranslator;
import youapp.dataaccess.dto.NameDto;
import youapp.dataaccess.dto.PersonDto;
import youapp.model.filter.IFilter;

@Repository
public class JdbcPersonDao
    implements IPersonDao
{

    /**
     * Logger.
     */
    static final Log log = LogFactory.getLog(JdbcPersonDao.class);

    /**
     * Used for accessing the database. JdbcTemplate is thread safe once
     * configured.
     */
    private SimpleJdbcTemplate jdbcTemplate;

    /**
     * Used for simple inserts. JdbcTemplate is thread safe once configured.
     */
    private SimpleJdbcInsert jdbcPersonInsert;

    @Autowired
    public void setDataSource(DataSource dataSource)
    {
        this.jdbcTemplate = new SimpleJdbcTemplate(dataSource);
        this.jdbcPersonInsert =
            new SimpleJdbcInsert(dataSource).withTableName("person").usingGeneratedKeyColumns("id_person");
    }

    private IFilterTranslator filterTranslator;

    @Autowired
    public void setFilterTranslator(IFilterTranslator filterTranslator)
    {
        this.filterTranslator = filterTranslator;
    }

    @Override
    public PersonDto getById(Long id)
    {
        if (id == null)
        {
            throw new IllegalArgumentException("Parameter must not be null.");
        }
        String sql = "select * from person where id_person = ?";
        return this.jdbcTemplate.queryForObject(sql, new PersonMapper(), id);
    }

    @Override
    public PersonDto getByFbId(Long fbId)
    {
        if (fbId == null)
        {
            throw new IllegalArgumentException("Parameter must not be null.");
        }
        String sql = "select * from person where fb_id = ?";
        return this.jdbcTemplate.queryForObject(sql, new PersonMapper(), fbId);
    }

    @Override
    public List<PersonDto> getByFilters(IFilter[] filters)
    {
        if (filters == null)
        {
            throw new IllegalArgumentException("Parameter must not be null.");
        }
        String filtersString = filterTranslator.getComposedFilterStrings(filters);
        String sql = "select * from person where " + filtersString;
        return this.jdbcTemplate.query(sql, new PersonMapper());
    }
    
    @Override
    public List<PersonDto> getByFilters(IFilter[] filters, Integer offset, Integer resultSize)
    {
        if (filters == null)
        {
            throw new IllegalArgumentException("Parameter must not be null.");
        }
        String filtersString = filterTranslator.getComposedFilterStrings(filters);
        String sql = "select * from person where " + filtersString + " limit " + offset + ", " + resultSize;
        return this.jdbcTemplate.query(sql, new PersonMapper());
    }

    @Override
    public List<PersonDto> getByFirstName(String firstName)
    {
        if (firstName == null)
        {
            throw new IllegalArgumentException("Parameter must not be null.");
        }
        String sql = "select * from person where first_name = ?";
        return this.jdbcTemplate.query(sql, new PersonMapper(), firstName);
    }

    @Override
    public List<PersonDto> getByLastName(String lastName)
    {
        if (lastName == null)
        {
            throw new IllegalArgumentException("Parameter must not be null.");
        }
        String sql = "select * from person where last_name = ?";
        return this.jdbcTemplate.query(sql, new PersonMapper(), lastName);
    }

    @Override
    public List<PersonDto> getByName(String firstName, String lastName)
    {
        if (firstName == null || lastName == null)
        {
            throw new IllegalArgumentException("Parameters must not be null.");
        }
        String sql = "select * from person where first_name = ? and last_name = ?";
        return this.jdbcTemplate.query(sql, new PersonMapper(), firstName, lastName);
    }

    @Override
    public List<PersonDto> getByNick(String nick)
    {
        if (nick == null)
        {
            throw new IllegalArgumentException("Parameter must not be null.");
        }
        String sql = "select * from person where nick = ?";
        return this.jdbcTemplate.query(sql, new PersonMapper(), nick);
    }

    @Override
    public NameDto getName(Long personId)
    {
        if (personId == null)
        {
            throw new IllegalArgumentException("person id must not be null.");
        }
        String sql = "select id_person, first_name, last_name, nick from person where id_person = ?";
        return this.jdbcTemplate.queryForObject(sql, new NameMapper(), personId);
    }

    @Override
    public String getNickName(Long personId)
    {
        if (personId == null)
        {
            throw new IllegalArgumentException("person id must not be null.");
        }
        String sql = "select id_person, first_name, last_name, nick from person where id_person = ?";
        NameDto name = this.jdbcTemplate.queryForObject(sql, new NameMapper(), personId);
        return name.getNickName();
    }

    @Override
    public Long getAlternativeId(Long personId, Boolean fbId)
    {
        if (personId == null)
        {
            throw new IllegalArgumentException("person id must not be null.");
        }
        if (fbId == null)
        {
            throw new IllegalArgumentException("Facebook id indicator must not be null.");
        }
        if (!exists(personId, fbId))
        {
            return null;
        }

        String sql = null;
        if (fbId)
        {
            sql = "select id_person from person where fb_id = ?";
        }
        else
        {
            sql = "select fb_id from person where id_person = ?";
        }
        return this.jdbcTemplate.queryForLong(sql, personId);
    }

    // TODO soulmates: really needed anymore?
    @Override
    public List<PersonDto> getAllFriends(Long personId, Integer offset, Integer resultSize, Boolean includeProvisional)
    {
        if (personId == null)
        {
            throw new IllegalArgumentException("person id must not be null.");
        }
        if (offset == null)
        {
            throw new IllegalArgumentException("Offset must not be null.");
        }
        if (resultSize == null)
        {
            throw new IllegalArgumentException("Result size must not be null.");
        }
        if (includeProvisional == null)
        {
            throw new IllegalArgumentException("Include provisional information must not be null.");
        }

        String sql = null;
        List<PersonDto> friends = new LinkedList<PersonDto>();
        if (includeProvisional)
        {
            sql =
                "select * from person where id_person in (select id_target from friendship where id_source = ?) limit ?,?";
            friends = jdbcTemplate.query(sql, new PersonMapper(), personId, offset, resultSize);
        }
        else
        {
            sql =
                "select * from person where id_person in (select id_target from friendship where id_source = ? and provisional = ?) limit ?,?";
            friends = jdbcTemplate.query(sql, new PersonMapper(), personId, false, offset, resultSize);
        }
        return friends;
    }

    @Override
    public List<PersonDto> getAll()
    {
        String sql = "select * from person";
        return this.jdbcTemplate.query(sql, new PersonMapper());
    }

    @Override
    public List<PersonDto> getRecentFriends(Long personId, Integer resultSize, Boolean includeProvisional)
    {
        if (personId == null)
        {
            throw new IllegalArgumentException("person id must not be null.");
        }
        if (resultSize == null)
        {
            throw new IllegalArgumentException("Result size must not be null.");
        }
        if (includeProvisional == null)
        {
            throw new IllegalArgumentException("Include provisional information must not be null.");
        }

        String sql = null;
        List<PersonDto> friends = new LinkedList<PersonDto>();
        if (includeProvisional)
        {
            sql =
                "select * from person where id_person in (select id_target from friendship where id_source = ? order by since desc) limit ?,?";
            friends = jdbcTemplate.query(sql, new PersonMapper(), personId, 0, resultSize);
        }
        else
        {
            sql =
                "select * from person where id_person in (select id_target from friendship where id_source = ? and provisional = ? order by since desc) limit ?,?";
            friends = jdbcTemplate.query(sql, new PersonMapper(), personId, false, 0, resultSize);
        }
        return friends;
    }

    @Override
    public Boolean exists(Long id, boolean fbId)
    {
        if (id == null)
        {
            throw new IllegalArgumentException("Id must be set.");
        }
        String sql = null;
        if (fbId)
        {
            sql = "select id_person from person where fb_id = ?";
        }
        else
        {
            sql = "select id_person from person where id_person = ?";
        }
        List<Long> ids = this.jdbcTemplate.query(sql, new MyLongMapper(), id);
        return (!(ids.isEmpty() || (ids.size() > 1)));
    }

    @Override
    public Boolean isActive(Long id, boolean fbId)
    {
        if (id == null)
        {
            throw new IllegalArgumentException("Id must be set.");
        }
        String sql = null;
        if (fbId)
        {
            sql = "select activated from person where fb_id = ?";
        }
        else
        {
            sql = "select activated from person where id_person = ?";
        }
        List<Boolean> resultList = this.jdbcTemplate.query(sql, new MyBooleanMapper(), id);
        if (!(resultList.isEmpty() || (resultList.size() > 1)))
        {
            return resultList.get(0);
        }
        else
        {
            return false;
        }
    }

    @Override
    public Boolean nickAvailable(String nick)
    {
        if (nick == null)
        {
            throw new IllegalArgumentException("Parameter must be set.");
        }
        String sql = "select id_person from person where nick = ?";
        List<Long> ids = this.jdbcTemplate.query(sql, new MyLongMapper(), nick);
        return ids.isEmpty();
    }

    @Override
    public Long create(PersonDto person)
    {
        if (person == null)
        {
            throw new IllegalArgumentException("Parameter must not be null.");
        }
        if (person.getId() != null)
        {
            throw new IllegalArgumentException("Id must be null.");
        }
        if (log.isDebugEnabled())
        {
            log.debug("Creating new person: " + person.toString());
        }
        // Prepare parameters.
        Map<String, Object> parameters = new HashMap<String, Object>(18);
        parameters.put("access_level", person.getAccessLevel());
        parameters.put("fb_id", person.getFbId());
        parameters.put("first_name", person.getFirstName());
        parameters.put("last_name", person.getLastName());
        parameters.put("gender", person.getGender());
        parameters.put("activated", person.getActivated());
        parameters.put("nick", person.getNickName());
        parameters.put("member_since", person.getMemberSince());
        parameters.put("last_online", person.getLastOnline());
        parameters.put("birthday", person.getBirthday());
        parameters.put("location", person.getLocation());
        parameters.put("location_name", person.getLocationName());
        parameters.put("education", person.getEducation());
        parameters.put("job", person.getJob());
        parameters.put("diet", person.getDiet());
        parameters.put("diet_strict", person.getDietStrict());
        parameters.put("religion", person.getReligion());
        parameters.put("religion_serious", person.getReligionSerious());
        parameters.put("sign", person.getSign());
        parameters.put("sign_serious", person.getSignSerious());
        parameters.put("description", person.getDescription());
        parameters.put("useFBMatching", person.getUseFBMatching());
        parameters.put("useQuestionMatching", person.getUseQuestionMatching());
        parameters.put("useDistanceMatching", person.getUseDistanceMatching());
        parameters.put("useSymptomsMatching", person.getUseSymptomsMatching());
        parameters.put("useMedicationMatching", person.getUseMedicationMatching());

        // Execute and set id.
        Number newId = this.jdbcPersonInsert.executeAndReturnKey(parameters);
        person.setId(newId.longValue());
        return person.getId();
    }

    @Override
    public void update(PersonDto person)
    {
        if (person == null)
        {
            throw new IllegalArgumentException("Parameter must not be null.");
        }
        if (person.getId() == null)
        {
            throw new IllegalArgumentException("Id must not be null.");
        }

        String sql =
            "update person set access_level = ?, fb_id = ?, first_name = ?, last_name = ?,"
                + "gender = ?, activated = ?, nick = ?, member_since = ?, last_online = ?, birthday = ?, "
                + "location = ?, location_name = ?, education = ?, job = ?, diet = ?, "
                + "diet_strict = ?, religion = ?, religion_serious = ?, sign = ?, sign_serious = ?, description = ?,"
                + "useFBMatching = ?, useQuestionMatching = ?, useDistanceMatching = ?, useSymptomsMatching = ?, useMedicationMatching = ? "
                + "where id_person = ?";

        this.jdbcTemplate.update(sql, person.getAccessLevel(), person.getFbId(), person.getFirstName(),
            person.getLastName(), person.getGender(), person.getActivated(), person.getNickName(),
            person.getMemberSince(), person.getLastOnline(), person.getBirthday(), person.getLocation(),person.getLocationName(),
            person.getEducation(), person.getJob(), person.getDiet(), person.getDietStrict(), person.getReligion(),
            person.getReligionSerious(), person.getSign(), person.getSignSerious(), person.getDescription(),
            person.getUseFBMatching(),person.getUseQuestionMatching(),person.getUseDistanceMatching(),
            person.getUseSymptomsMatching(), person.getUseMedicationMatching(),
            person.getId());
    }

    @Override
    public void updateLastOnline(Long personId, Date lastOnline)
    {
        if (personId == null)
        {
            throw new IllegalArgumentException("Person id must not be null.");
        }
        if (lastOnline == null)
        {
            throw new IllegalArgumentException("Last login date must not be null.");
        }

        String sql = "update person set last_online = ? where id_person = ?";

        this.jdbcTemplate.update(sql, lastOnline, personId);
    }

    @Override
    public void deactivate(Long personId)
    {
        if (personId == null)
        {
            throw new IllegalArgumentException("person id must not be null.");
        }
        String sql =
            "update person set activated = ?, nick = ?, member_since = ?, last_online = ?, "
                + "birthday = ?, location = ?, education = ?, job = ?, diet = ?, "
                + "diet_strict = ?, religion = ?, religion_serious = ?, sign = ?, sign_serious = ?, description = ? "
                + "where id_person = ?";
        this.jdbcTemplate.update(sql, false, null, null, null, null, null, null, null, null, null, null, null, null,
            null, null, null, null, personId);
    }

    @Override
    public void delete(Long personId)
    {
        if (personId == null)
        {
            throw new IllegalArgumentException("person id must not be null.");
        }
        this.jdbcTemplate.update("delete from person where id_person = ?", personId);
    }

    /**
     * Maps a database row to a person data transfer object.
     * 
     * @author neme
     * 
     */
    private static final class PersonMapper
        implements RowMapper<PersonDto>
    {

        public PersonDto mapRow(ResultSet rs, int rowNum) throws SQLException
        {
            PersonDto person = new PersonDto();
            person.setId(rs.getLong("id_person"));
            if (rs.wasNull())
            {
                person.setId(null);
            }
            person.setAccessLevel(rs.getInt("access_level"));
            if (rs.wasNull())
            {
                person.setAccessLevel(null);
            }
            person.setFbId(rs.getLong("fb_id"));
            if (rs.wasNull())
            {
                person.setFbId(null);
            }
            person.setFirstName(rs.getString("first_name"));
            if (rs.wasNull())
            {
                person.setFirstName(null);
            }
            person.setLastName(rs.getString("last_name"));
            if (rs.wasNull())
            {
                person.setLastName(null);
            }
            person.setGender(rs.getString("gender"));
            if (rs.wasNull())
            {
                person.setGender(null);
            }
            person.setActivated(rs.getBoolean("activated"));
            if (rs.wasNull())
            {
                person.setActivated(null);
            }
            person.setNickName(rs.getString("nick"));
            if (rs.wasNull())
            {
                person.setNickName(null);
            }
            person.setMemberSince(rs.getDate("member_since"));
            if (rs.wasNull())
            {
                person.setMemberSince(null);
            }
            person.setLastOnline(rs.getDate("last_online"));
            if (rs.wasNull())
            {
                person.setLastOnline(null);
            }
            person.setBirthday(rs.getDate("birthday"));
            if (rs.wasNull())
            {
                person.setBirthday(null);
            }
            person.setLocation(rs.getInt("location"));
            if (rs.wasNull())
            {
                person.setLocation(null);
            }
            person.setLocationName(rs.getString("location_name"));
            if (rs.wasNull())
            {
                person.setLocationName(null);
            }
            person.setEducation(rs.getInt("education"));
            if (rs.wasNull())
            {
                person.setEducation(null);
            }
            person.setJob(rs.getInt("job"));
            if (rs.wasNull())
            {
                person.setJob(null);
            }
            person.setDiet(rs.getInt("diet"));
            if (rs.wasNull())
            {
                person.setDiet(null);
            }
            person.setDietStrict(rs.getBoolean("diet_strict"));
            if (rs.wasNull())
            {
                person.setDietStrict(null);
            }
            person.setReligion(rs.getInt("religion"));
            if (rs.wasNull())
            {
                person.setReligion(null);
            }
            person.setReligionSerious(rs.getInt("religion_serious"));
            if (rs.wasNull())
            {
                person.setReligionSerious(null);
            }
            person.setSign(rs.getByte("sign"));
            if (rs.wasNull())
            {
                person.setSign(null);
            }
            person.setSignSerious(rs.getInt("sign_serious"));
            if (rs.wasNull())
            {
                person.setSignSerious(null);
            }
            person.setDescription(rs.getString("description"));
            if (rs.wasNull())
            {
                person.setDescription(null);
            }
            person.setUseFBMatching(rs.getBoolean("useFBMatching"));
            if (rs.wasNull())
            {
                person.setUseFBMatching(null);
            }
            person.setUseQuestionMatching(rs.getBoolean("useQuestionMatching"));
            if (rs.wasNull())
            {
                person.setUseQuestionMatching(null);
            }
            person.setUseDistanceMatching(rs.getBoolean("useDistanceMatching"));
            if (rs.wasNull())
            {
                person.setUseDistanceMatching(null);
            }
            person.setUseSymptomsMatching(rs.getBoolean("useSymptomsMatching"));
            if (rs.wasNull())
            {
                person.setUseSymptomsMatching(null);
            }
            person.setUseMedicationMatching(rs.getBoolean("useMedicationMatching"));
            if (rs.wasNull())
            {
                person.setUseMedicationMatching(null);
            }
            return person;
        }
    }

    /**
     * Maps a row containing an id to a Long.
     * 
     * @author neme
     * 
     */
    private static final class MyLongMapper
        implements RowMapper<Long>
    {

        @Override
        public Long mapRow(ResultSet rs, int rowNum) throws SQLException
        {
            Long id = rs.getLong(1);
            if (rs.wasNull())
            {
                id = null;
            }
            return id;
        }
    }

    /**
     * Maps a row containing a boolean to a Bong.
     * 
     * @author neme
     * 
     */
    private static final class MyBooleanMapper
        implements RowMapper<Boolean>
    {

        @Override
        public Boolean mapRow(ResultSet rs, int rowNum) throws SQLException
        {
            Boolean isActive = rs.getBoolean(1);
            if (rs.wasNull())
            {
                isActive = null;
            }
            return isActive;
        }

    }

    private static final class NameMapper
        implements RowMapper<NameDto>
    {

        @Override
        public NameDto mapRow(ResultSet rs, int rowNum) throws SQLException
        {
            NameDto name = new NameDto();
            name.setPersonId(rs.getLong("id_person"));
            if (rs.wasNull())
            {
                name.setPersonId(null);
            }
            name.setFirstName(rs.getString("first_name"));
            if (rs.wasNull())
            {
                name.setFirstName(null);
            }
            name.setLastName(rs.getString("last_name"));
            if (rs.wasNull())
            {
                name.setLastName(null);
            }
            name.setNickName(rs.getString("nick"));
            if (rs.wasNull())
            {
                name.setNickName(null);
            }
            return name;
        }

    }

}
