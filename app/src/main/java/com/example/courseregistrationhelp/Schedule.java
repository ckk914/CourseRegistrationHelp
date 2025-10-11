package com.example.courseregistrationhelp;

import android.content.Context;
import android.widget.TextView;

public class Schedule {
    private String monday[] = new String[14]; //0-13교시
    private String tuesday[] = new String[14];
    private String wednesday[] = new String[14];
    private String thursday[] = new String[14];
    private String friday[] = new String[14];

    public Schedule(){
        for(int i = 0; i < 14; i++){
            monday[i] = "";
            tuesday[i] = "";
            wednesday[i] = "";
            thursday[i] = "";
            friday[i] = "";
        }
    } //end Schedule()

    public void addSchedule(String scheduleText){
        int temp;
        //월:[3][4][5]화:[4][5]
        if((temp = scheduleText.indexOf("월")) > -1){
            temp+=2;
            int startPoint = temp;
            int endPoint  = temp;
            for(int i = temp; i < scheduleText.length() && scheduleText.charAt(i) != ':';i++){
                if(scheduleText.charAt(i) == '['){
                    startPoint = i;
                }
                if(scheduleText.charAt(i) == ']'){
                    endPoint = i;
                    monday[Integer.parseInt(scheduleText.substring(startPoint+1,endPoint))] = "수업";
                }
            }
        } //월
        if((temp = scheduleText.indexOf("화")) > -1){
            temp+=2;
            int startPoint = temp;
            int endPoint  = temp;
            for(int i = temp; i < scheduleText.length() && scheduleText.charAt(i) != ':';i++){
                if(scheduleText.charAt(i) == '['){
                    startPoint = i;
                }
                if(scheduleText.charAt(i) == ']'){
                    endPoint = i;
                    tuesday[Integer.parseInt(scheduleText.substring(startPoint+1,endPoint))] = "수업";
                }
            }
        } //화
        if((temp = scheduleText.indexOf("수")) > -1){
            temp+=2;
            int startPoint = temp;
            int endPoint  = temp;
            for(int i = temp; i < scheduleText.length() && scheduleText.charAt(i) != ':';i++){
                if(scheduleText.charAt(i) == '['){
                    startPoint = i;
                }
                if(scheduleText.charAt(i) == ']'){
                    endPoint = i;
                    wednesday[Integer.parseInt(scheduleText.substring(startPoint+1,endPoint))] = "수업";
                }
            }
        } //수
        if((temp = scheduleText.indexOf("목")) > -1){
            temp+=2;
            int startPoint = temp;
            int endPoint  = temp;
            for(int i = temp; i < scheduleText.length() && scheduleText.charAt(i) != ':';i++){
                if(scheduleText.charAt(i) == '['){
                    startPoint = i;
                }
                if(scheduleText.charAt(i) == ']'){
                    endPoint = i;
                    thursday[Integer.parseInt(scheduleText.substring(startPoint+1,endPoint))] = "수업";
                }
            }
        } //목
        if((temp = scheduleText.indexOf("금")) > -1){
            temp+=2;
            int startPoint = temp;
            int endPoint  = temp;
            for(int i = temp; i < scheduleText.length() && scheduleText.charAt(i) != ':';i++){
                if(scheduleText.charAt(i) == '['){
                    startPoint = i;
                }
                if(scheduleText.charAt(i) == ']'){
                    endPoint = i;
                    friday[Integer.parseInt(scheduleText.substring(startPoint+1,endPoint))] = "수업";
                }
            }
        } //금
    } // end addSchedule

    //날짜별 중복 검사
    public boolean validate(String scheduleText) {
        if (scheduleText.equals("")) {
            return true;
        }
        int temp;
        if ((temp = scheduleText.indexOf("월")) > -1) {
            temp += 2;
            int startPoint = temp;
            int endPoint = temp;
            for (int i = temp; i < scheduleText.length() && scheduleText.charAt(i) != ':'; i++) {
                if (scheduleText.charAt(i) == '[') {
                    startPoint = i;
                }
                if (scheduleText.charAt(i) == ']') {
                    endPoint = i;
                    if (!monday[Integer.parseInt(scheduleText.substring(startPoint + 1, endPoint))].equals("")) {
                        return false; //데이터 있어서 중복!
                    }
                }
            }
        } //월

        if ((temp = scheduleText.indexOf("화")) > -1) {
            temp += 2;
            int startPoint = temp;
            int endPoint = temp;
            for (int i = temp; i < scheduleText.length() && scheduleText.charAt(i) != ':'; i++) {
                if (scheduleText.charAt(i) == '[') {
                    startPoint = i;
                }
                if (scheduleText.charAt(i) == ']') {
                    endPoint = i;
                    if (!tuesday[Integer.parseInt(scheduleText.substring(startPoint + 1, endPoint))].equals("")) {
                        return false; //데이터 있어서 중복!
                    }
                }
            }
        } //화
        if ((temp = scheduleText.indexOf("수")) > -1) {
            temp += 2;
            int startPoint = temp;
            int endPoint = temp;
            for (int i = temp; i < scheduleText.length() && scheduleText.charAt(i) != ':'; i++) {
                if (scheduleText.charAt(i) == '[') {
                    startPoint = i;
                }
                if (scheduleText.charAt(i) == ']') {
                    endPoint = i;
                    if (!wednesday[Integer.parseInt(scheduleText.substring(startPoint + 1, endPoint))].equals("")) {
                        return false; //데이터 있어서 중복!
                    }
                }
            }
        } //수
        if ((temp = scheduleText.indexOf("목")) > -1) {
            temp += 2;
            int startPoint = temp;
            int endPoint = temp;
            for (int i = temp; i < scheduleText.length() && scheduleText.charAt(i) != ':'; i++) {
                if (scheduleText.charAt(i) == '[') {
                    startPoint = i;
                }
                if (scheduleText.charAt(i) == ']') {
                    endPoint = i;
                    if (!thursday[Integer.parseInt(scheduleText.substring(startPoint + 1, endPoint))].equals("")) {
                        return false; //데이터 있어서 중복!
                    }
                }
            }
        } //목
        if ((temp = scheduleText.indexOf("금")) > -1) {
            temp += 2;
            int startPoint = temp;
            int endPoint = temp;
            for (int i = temp; i < scheduleText.length() && scheduleText.charAt(i) != ':'; i++) {
                if (scheduleText.charAt(i) == '[') {
                    startPoint = i;
                }
                if (scheduleText.charAt(i) == ']') {
                    endPoint = i;
                    if (!friday[Integer.parseInt(scheduleText.substring(startPoint + 1, endPoint))].equals("")) {
                        return false; //데이터 있어서 중복!
                    }
                }
            }
        } //금

        return true; //중복 아님!
    }

    //->
    public void addSchedule(String scheduleText, String courseTitle, String courseProfessor){
        String professor;
        if(courseProfessor.equals("")){
            professor = "";
        }
        else{
            professor = "(" + courseProfessor + ")";
        }

        int temp;
        //월:[3][4][5]화:[4][5]
        if((temp = scheduleText.indexOf("월")) > -1){
            temp+=2;
            int startPoint = temp;
            int endPoint  = temp;
            for(int i = temp; i < scheduleText.length() && scheduleText.charAt(i) != ':';i++){
                if(scheduleText.charAt(i) == '['){
                    startPoint = i;
                }
                if(scheduleText.charAt(i) == ']'){
                    endPoint = i;
                    monday[Integer.parseInt(scheduleText.substring(startPoint+1,endPoint))] = courseTitle + professor;
                }
            }
        } //월
        if((temp = scheduleText.indexOf("화")) > -1){
            temp+=2;
            int startPoint = temp;
            int endPoint  = temp;
            for(int i = temp; i < scheduleText.length() && scheduleText.charAt(i) != ':';i++){
                if(scheduleText.charAt(i) == '['){
                    startPoint = i;
                }
                if(scheduleText.charAt(i) == ']'){
                    endPoint = i;
                    tuesday[Integer.parseInt(scheduleText.substring(startPoint+1,endPoint))] = courseTitle + professor;
                }
            }
        } //화
        if((temp = scheduleText.indexOf("수")) > -1){
            temp+=2;
            int startPoint = temp;
            int endPoint  = temp;
            for(int i = temp; i < scheduleText.length() && scheduleText.charAt(i) != ':';i++){
                if(scheduleText.charAt(i) == '['){
                    startPoint = i;
                }
                if(scheduleText.charAt(i) == ']'){
                    endPoint = i;
                    wednesday[Integer.parseInt(scheduleText.substring(startPoint+1,endPoint))] = courseTitle + professor;
                }
            }
        } //수
        if((temp = scheduleText.indexOf("목")) > -1){
            temp+=2;
            int startPoint = temp;
            int endPoint  = temp;
            for(int i = temp; i < scheduleText.length() && scheduleText.charAt(i) != ':';i++){
                if(scheduleText.charAt(i) == '['){
                    startPoint = i;
                }
                if(scheduleText.charAt(i) == ']'){
                    endPoint = i;
                    thursday[Integer.parseInt(scheduleText.substring(startPoint+1,endPoint))] = courseTitle + professor;
                }
            }
        } //목
        if((temp = scheduleText.indexOf("금")) > -1){
            temp+=2;
            int startPoint = temp;
            int endPoint  = temp;
            for(int i = temp; i < scheduleText.length() && scheduleText.charAt(i) != ':';i++){
                if(scheduleText.charAt(i) == '['){
                    startPoint = i;
                }
                if(scheduleText.charAt(i) == ']'){
                    endPoint = i;
                    friday[Integer.parseInt(scheduleText.substring(startPoint+1,endPoint))] = courseTitle + professor;
                }
            }
        } //금
    } // end addSchedule
    //<-
    //입력받은 내용들 표현해주기 위한 세팅 함수
    public void setting(TextView[] monday, TextView[] tuesday, TextView[] wednesday, TextView[] thursday, TextView[] friday, Context context){
        for(int i = 0; i < 14; i++) {
         if(!this.monday[i].equals("")){
             monday[i].setText(this.monday[i]);
             monday[i].setTextColor(context.getResources().getColor(R.color.colorPrimary));
         }
            if (!this.tuesday[i].equals("")) {
                tuesday[i].setText(this.tuesday[i]);
                tuesday[i].setTextColor(context.getResources().getColor(R.color.colorPrimary));
            }
            if (!this.wednesday[i].equals("")) {
                wednesday[i].setText(this.wednesday[i]);
                wednesday[i].setTextColor(context.getResources().getColor(R.color.colorPrimary));
            }
            if (!this.thursday[i].equals("")) {
                thursday[i].setText(this.thursday[i]);
                thursday[i].setTextColor(context.getResources().getColor(R.color.colorPrimary));
            }
            if (!this.friday[i].equals("")) {
                friday[i].setText(this.friday[i]);
                friday[i].setTextColor(context.getResources().getColor(R.color.colorPrimary));
            }
        }
    }
}
