package com.quickhire.quickhire;

/** Answer **************************************************************************
 * Created by nick on 2018-03-27.
 * Description: This class is used to create answers from questions.
 * @param- int applicationID - used to tell which application the answer belonged to
 *        String Answer - answer the the question
 *        int questionID - id of the question being answered
 *        int type - type of question Essay, video or multiple choice.
 ************************************************************************************/

public class Answer {
    private Integer applicationID;
    private String Answer;
    private int questionID, type;

    //Constructor
    public Answer(int id, String value, int t){
        this.questionID = id;
        this.Answer = value;
        this.type = t;
    }

    //Creates the JSON for the class
    public String toJSON() {
        String string = "{\"questionID\":"+ this.questionID+",\"answer\":\""+this.Answer+"\",\"type\":\"" + this.type + "\"}";
        return string;
    }

    public String getAnswer(){return Answer;}

    public int getQuestionID(){return this.questionID;}

    public int getApplicationID(){return this.applicationID;}

    public void setApplicationID(int id){this.applicationID=id;}

    protected void beginTransmitting() {
        //nothing
    }
}
