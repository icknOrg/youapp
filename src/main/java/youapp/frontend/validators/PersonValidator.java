package youapp.frontend.validators;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import youapp.model.Location;
import youapp.model.Person;
import youapp.model.Tag;
import youapp.model.TagSet;

public class PersonValidator
    implements Validator
{
    /**
     * Logger.
     */
    private static final Log log = LogFactory.getLog(PersonValidator.class);

    @Override
    public boolean supports(Class<?> clazz)
    {
        return Person.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors)
    {
        // Perform general checks.
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "firstName", "error.firstName.empty", "First name is required.");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "lastName", "error.lastName.empty", "Last name is required.");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "nickName", "error.nickName.empty", "Nick name is required.");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "gender", "error.gender.empty", "Gender is required");
        ValidationUtils.rejectIfEmpty(errors, "birthday", "error.birthday.empty");

        // Perform specific checks.
        Person person = (Person) target;
        if (person == null)
        {
            if (log.isDebugEnabled())
            {
                log.debug("Person is null.");
            }
            // Global error.
            if (!errors.hasGlobalErrors())
            {
                errors.reject("error.global.emptyForm", "An error occurred while processing the form data. Please try again later.");
            }
        }
        else
        {
            // Check first name.
            String firstName = person.getFirstName();
            if (firstName != null)
            {
                if (!errors.hasFieldErrors("firstName") && (firstName.length() > 30))
                {
                    errors.rejectValue("firstName", "error.firstName.length", "Max. 30 characters allowed.");
                }
                if (!errors.hasFieldErrors("firstName") && !firstName.matches("^\\p{Lu}.*$"))
                {
                    errors.rejectValue("firstName", "error.firstName.capital", "Fist name must start with a capital letter.");
                }
                if (!errors.hasFieldErrors("firstName") && !firstName.matches("^[\\p{Lu}][\\p{Lu}\\p{Ll}]*$"))
                {
                    errors.rejectValue("firstName", "error.firstName.letters", "Only uppercase and lowercase letters allowed.");
                }
            }
            else
            {
                if (log.isDebugEnabled())
                {
                    log.debug("First name is null.");
                }
                if (!errors.hasFieldErrors("firstName"))
                {
                    errors.rejectValue("firstName", "error.firstName.empty", "First name is required.");
                }
            }

            // Check last name.
            String lastName = person.getLastName();
            if (lastName != null)
            {
                if (!errors.hasFieldErrors("lastName") && (lastName.length() > 40))
                {
                    errors.rejectValue("lastName", "error.lastName.length", "Max. 40 characters allowed.");
                }
                if (!errors.hasFieldErrors("lastName") && !lastName.matches("([\\p{Lu}\\p{Ll}][ -]?)*([\\p{Lu}\\p{Ll}])+"))
                {
                    errors.rejectValue("lastName", "error.lastName.letters", "Only uppercase and lowercase letters, optionally separated by one space or dash allowed.");
                }
            }
            else
            {
                if (log.isDebugEnabled())
                {
                    log.debug("Last name is null.");
                }
                if (!errors.hasFieldErrors("lastName"))
                {
                    errors.rejectValue("lastName", "error.lastName.empty", "Last name is required.");
                }
                errors.rejectValue("lastName", "error.lastName.empty", "Last name is required.");
            }

            // Check nick name.
            String nickName = person.getNickName();
            if (nickName != null)
            {
                if (!errors.hasFieldErrors("nickName") && (nickName.length() > 20))
                {
                    errors.rejectValue("nickName", "error.nickName.length", "Max. 20 characters allowed.");
                }
                if (!errors.hasFieldErrors("nickName") && !nickName.matches("^[\\p{Lu}\\p{Ll}].*$"))
                {
                    errors.rejectValue("nickName", "error.nickName.alpha", "Nick name must start with a letter.");
                }
                if (!errors.hasFieldErrors("nickName") && !nickName.matches("[\\p{Lu}\\p{Ll}]([\\p{Lu}\\p{Ll}\\p{Nd}][ -]?)*([\\p{Lu}\\p{Ll}\\p{Nd}])+"))
                {
                    errors.rejectValue("nickName", "error.nickName.letters", "Only uppercase and lowercase letters and numbers, optionally separated by one space or dash allowed.");
                }
            }
            else
            {
                if (log.isDebugEnabled())
                {
                    log.debug("Nick name is null.");
                }
                if (!errors.hasFieldErrors("nickName"))
                {
                    errors.rejectValue("nickName", "error.nickName.empty", "Nick name is required.");
                }
            }

            // Check gender.
            String gender = person.getGender();
            if (gender != null)
            {
                if (!(("M".equals(gender)) || ("F".equals(gender))))
                {
                    errors.rejectValue("gender", "error.gender.values", "Gender must be either male or female.");
                }
            }
            else
            {
                if (log.isDebugEnabled())
                {
                    log.debug("Gender is null.");
                }
                if (!errors.hasFieldErrors("gender"))
                {
                    errors.rejectValue("gender", "error.gender.empty", "Gender is required.");
                }
            }

            // Check description.
            String description = person.getDescription();
            if (description != null)
            {
                if (!errors.hasFieldErrors("description") && (firstName.length() > 160))
                {
                    errors.rejectValue("firstName", "error.descrption.length", "Max. 160 characters allowed.");
                }
            }

            // Check tags.
            TagSet tags = person.getTags();
            String tagName;
            String field = null;
            String messageCode = null;
            if (tags != null)
            {
                for (Tag tag : tags)
                {
                    if (tag.getId() == null)
                    {
                        // User defined tag!
                        tagName = tag.getName();

                        switch (tag.getCategory())
                        {
                        case Medication:
                            field = "medications";
                            messageCode = "error.medication";
                            break;
                        case Symptom:
                            field = "symptoms";
                            messageCode = "error.symptom";
                            break;
                        case ProfileTag:
                            field = "profileTags";
                            messageCode = "error.profileTag";
                            break;
                        }

                        if (tagName == null)
                        {
                            if (!errors.hasFieldErrors(field))
                            {
                                errors.rejectValue(field, messageCode + ".empty", "Specific tag must not be empty.");
                            }
                        }
                        else
                        {
                            if (!errors.hasFieldErrors(field) && (tagName.length() == 0))
                            {
                                errors.rejectValue(field, messageCode + ".empty", "Specific tag must not be empty.");
                            }
                            if (!errors.hasFieldErrors(field) && (tagName.matches("[\\p{Z}]*")))
                            {
                                errors.rejectValue(field, messageCode + ".empty", "Specific tag must not be empty.");
                            }
                            if (!errors.hasFieldErrors(field) && (tagName.length() > 50))
                            {
                                errors.rejectValue(field, messageCode + ".length", "Max. 50 characters allowed.");
                            }
                        }
                    }
                }
            }

            // Check location
            Location location = person.getLocation();
            if (location != null)
            {
                if (!errors.hasFieldErrors("location") && StringUtils.isBlank(location.getName()))
                {
                    errors.rejectValue("location", "error.location.name.empty", "Location is required.");
                }
                if (!errors.hasFieldErrors("location") && location.getId() == null)
                {
                    errors.rejectValue("location", "error.location.other.empty", "Try again.");
                }
                if (!errors.hasFieldErrors("location") && location.getLatitude() == null)
                {
                    errors.rejectValue("location", "error.location.other.empty", "Try again.");
                }
                if (!errors.hasFieldErrors("location") && location.getLongitude() == null)
                {
                    errors.rejectValue("location", "error.location.other.empty", "Try again.");
                }
                if (!errors.hasFieldErrors("location") && (!location.getName().matches("([^,;]+,[^,;]+){1,2}")))
                {
                    errors.rejectValue("location", "error.location.other.empty", "Try again.");
                }
            }
            else
            {
                if (log.isDebugEnabled())
                {
                    log.debug("Location is null.");
                }
                if (!errors.hasFieldErrors("location"))
                {
                    errors.rejectValue("location", "error.location.name.empty", "Location is required.");
                }
            }
        }

    }
}
