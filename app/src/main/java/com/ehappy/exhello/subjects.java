package com.ehappy.exhello;

/**
 * Created by use on 2018/11/22.
 */

public class subjects {

    //String SubjectName;
    String subId,subTitle,subContent;

    public String getId() {

        return subId;

    }
    public String getTitle() {

        return subTitle;

    }
    public String getContent() {

        return subContent;

    }


    public void setId(String TempName) {

        this.subId = TempName;

    }
    public void setTitle(String TempName) {

        this.subTitle = TempName;

    }
    public void setContent(String TempName) {

        this.subContent = TempName;

    }


}