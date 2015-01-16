package youapp.model;

public class Tag
{
    private Long id;

    private Category category;

    private String name;

    /**
     * @return the id
     */
    public Long getId()
    {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Long id)
    {
        this.id = id;
    }

    /**
     * @return the category
     */
    public Category getCategory()
    {
        return category;
    }

    /**
     * @param category the category to set
     */
    public void setCategory(Category category)
    {
        this.category = category;
    }

    /**
     * @return the name
     */
    public String getName()
    {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name)
    {
        this.name = name;
    }

    public enum Category
    {
        Medication(0), Symptom(1), ProfileTag(2);

        private final int id;

        Category(int id)
        {
            this.id = id;
        }

        public int id()
        {
            return id;
        }

        public static Category getCategory(int id)
        {
            switch (id)
            {
            case 0:
                return Category.Medication;
            case 1:
                return Category.Symptom;
            case 2:
                return Category.ProfileTag;
            default:
                return null;
            }
        }

    }

    @Override
    public String toString()
    {
        return id + " - " + category + " " + name;
    }

    @Override
    public boolean equals(Object obj)
    {
        if ((obj == this) || obj instanceof Tag)
            return true;

        Tag compareTag = (Tag) obj;
        return this.getCategory() == compareTag.getCategory()
            && (this.getName() == compareTag.getName() || (this.getName() != null)
                && this.getName().equals(compareTag.getName()));

    }

    @Override
    public int hashCode()
    {
        int hashCode = 0;
        if (this.name != null)
            hashCode += this.name.hashCode();
        if (this.category != null)
            hashCode += this.category.id();
        return hashCode;
    }
}
