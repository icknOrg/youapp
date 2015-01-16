package youapp.model;

import java.util.Date;

public class Reply
{

    private Long personId;

    private Name personName;

    private Long questionId;

    private Question question;

    private Boolean skipped;

    private Boolean inPrivate;

    private Boolean critical;

    private Date lastUpdate;

    private Integer importance;

    private String explanation;

    private Boolean answerA;

    private Boolean answerB;

    private Boolean answerC;

    private Boolean answerD;

    private Boolean answerE;

    /**
     * @return the personId
     */
    public Long getPersonId()
    {
        return personId;
    }

    /**
     * @param personId the personId to set
     */
    public void setPersonId(Long personId)
    {
        this.personId = personId;
    }

    /**
     * @return the questionId
     */
    public Long getQuestionId()
    {
        return questionId;
    }

    /**
     * @param questionId the questionId to set
     */
    public void setQuestionId(Long questionId)
    {
        this.questionId = questionId;
    }

    // /**
    // * @return the person's first name.
    // */
    // public String getPersonFirstName() {
    // return personFirstName;
    // }
    //
    // /**
    // * @param personFirstName the person's first name to set.
    // */
    // public void setPersonFirstName(String personFirstName) {
    // this.personFirstName = personFirstName;
    // }
    //
    // /**
    // * @return the person's last name.
    // */
    // public String getPersonLastName() {
    // return personLastName;
    // }
    //
    // /**
    // * @param personLastName the person's last name to set.
    // */
    // public void setPersonLastName(String personLastName) {
    // this.personLastName = personLastName;
    // }
    //
    // /**
    // * @return the personNickName
    // */
    // public String getPersonNickName() {
    // return personNickName;
    // }
    //
    // /**
    // * @param personNickName the personNickName to set
    // */
    // public void setPersonNickName(String personNickName) {
    // this.personNickName = personNickName;
    // }

    /**
     * @return the skipped
     */
    public Boolean getSkipped()
    {
        return skipped;
    }

    /**
     * Returns the name of the person who has given this reply.
     * 
     * @return the name of the person who has given this reply.
     */
    public Name getPersonName()
    {
        return personName;
    }

    /**
     * Sets the name of the person who has given this reply.
     * 
     * @param personName the name of the person who has given this reply.
     */
    public void setPersonName(Name personName)
    {
        this.personName = personName;
    }

    /**
     * Returns the question to which this reply is given.
     * 
     * @return question to which this reply is given.
     */
    public Question getQuestion()
    {
        return question;
    }

    /**
     * Sets the question to which this reply is given.
     * 
     * @param question the question to which this reply is given.
     */
    public void setQuestion(Question question)
    {
        this.question = question;
    }

    /**
     * @param skipped the skipped to set
     */
    public void setSkipped(Boolean skipped)
    {
        this.skipped = skipped;
    }

    /**
     * @return the inPrivate
     */
    public Boolean getInPrivate()
    {
        return inPrivate;
    }

    /**
     * @param inPrivate the inPrivate to set
     */
    public void setInPrivate(Boolean inPrivate)
    {
        this.inPrivate = inPrivate;
    }

    /**
     * @return the critical
     */
    public Boolean getCritical()
    {
        return critical;
    }

    /**
     * @param critical the critical to set
     */
    public void setCritical(Boolean critical)
    {
        this.critical = critical;
    }

    /**
     * @return the lastUpdate
     */
    public Date getLastUpdate()
    {
        return lastUpdate;
    }

    /**
     * @param lastUpdate the lastUpdate to set
     */
    public void setLastUpdate(Date lastUpdate)
    {
        this.lastUpdate = lastUpdate;
    }

    /**
     * @return the importance
     */
    public Integer getImportance()
    {
        return importance;
    }

    /**
     * @param importance the importance to set
     */
    public void setImportance(Integer importance)
    {
        this.importance = importance;
    }

    /**
     * @return the explanation
     */
    public String getExplanation()
    {
        return explanation;
    }

    /**
     * @param explanation the explanation to set
     */
    public void setExplanation(String explanation)
    {
        this.explanation = explanation;
    }

    /**
     * @return the answerA
     */
    public Boolean getAnswerA()
    {
        if (answerA == null)
        {
            return false;
        }
        else
        {
            return answerA;
        }
    }

    /**
     * @param answerA the answerA to set
     */
    public void setAnswerA(Boolean answerA)
    {
        this.answerA = answerA;
    }

    /**
     * @return the answerB
     */
    public Boolean getAnswerB()
    {
        if (answerB == null)
        {
            return false;
        }
        else
        {
            return answerB;
        }
    }

    /**
     * @param answerB the answerB to set
     */
    public void setAnswerB(Boolean answerB)
    {
        this.answerB = answerB;
    }

    /**
     * @return the answerC
     */
    public Boolean getAnswerC()
    {
        if (answerC == null)
        {
            return false;
        }
        else
        {
            return answerC;
        }
    }

    /**
     * @param answerC the answerC to set
     */
    public void setAnswerC(Boolean answerC)
    {
        this.answerC = answerC;
    }

    /**
     * @return the answerD
     */
    public Boolean getAnswerD()
    {
        if (answerD == null)
        {
            return false;
        }
        else
        {
            return answerD;
        }
    }

    /**
     * @param answerD the answerD to set
     */
    public void setAnswerD(Boolean answerD)
    {
        this.answerD = answerD;
    }

    /**
     * @return the answerE
     */
    public Boolean getAnswerE()
    {
        if (answerE == null)
        {
            return false;
        }
        else
        {
            return answerE;
        }
    }

    /**
     * @param answerE the answerE to set
     */
    public void setAnswerE(Boolean answerE)
    {
        this.answerE = answerE;
    }

    /**
     * Set the answer by index
     * @param answerIndex
     */
    public void setAnswerIndex(Integer answerIndex)
    {
        setAnswerA(false);
        setAnswerB(false);
        setAnswerC(false);
        setAnswerD(false);
        setAnswerE(false);

        switch (answerIndex)
        {
        case 0:
            setAnswerA(true);
            break;
        case 1:
            setAnswerB(true);
            break;
        case 2:
            setAnswerC(true);
            break;
        case 3:
            setAnswerD(true);
            break;
        case 4:
            setAnswerE(true);
            break;
        }
    }

    /**
     * Get the answer as index
     * @return
     */
    public Integer getAnswerIndex()
    {
        if (getAnswerA())
        {
            return 0;
        }
        else if (getAnswerB())
        {
            return 1;
        }
        else if (getAnswerC())
        {
            return 2;
        }
        else if (getAnswerD())
        {
            return 3;
        }
        else if (getAnswerE())
        {
            return 4;
        }
        else
        {
            return null;
        }
    }

    // /**
    // * The number of total replies given to the question.
    // * @return the number of total replies given.
    // */
    // public Integer getFrequency() {
    // return frequency;
    // }
    //
    // public void setFrequency(Integer frequency) {
    // this.frequency = frequency;
    // }

    @Override
    public String toString()
    {
        return "Q: " + questionId + ", P: " + personId + " [" + answerA + "," + answerB + "," + answerC + "," + answerD
            + "," + answerE + "]";
    }

}
