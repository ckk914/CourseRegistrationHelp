package com.example.courseregistrationhelp;

/***
 *
 */
public class Course {
    int courseID; // 강의 고유 번호
    String courseUniversity; //학부 혹은 대학원
    String courseYear; //해당 년도
    String courseTerm; //해당 학기
    String courseArea; //강의 영역 (교양/ 전공)
    String courseMajor; // 해당 학과
    String courseGrade; // 해당 학년
    String courseTitle; //강의 제목
    int courseCredit; //강의 학점
    int courseDivide; //강의 분반
    int coursePersonnel; //강의 제한 인원
    String courseProfessor; //강의 교수
    String courseTime; //강의 시간
    String courseRoom; // 강의실
    int courseRival; //강의 경쟁자 수
    public void setCourseID(int courseID) {
        this.courseID = courseID;
    }

    public void setCourseUniversity(String courseUniversity) {
        this.courseUniversity = courseUniversity;
    }

    public void setCourseYear(String courseYear) {
        this.courseYear = courseYear;
    }

    public void setCourseTerm(String courseTerm) {
        this.courseTerm = courseTerm;
    }

    public void setCourseArea(String courseArea) {
        this.courseArea = courseArea;
    }

    public void setCourseMajor(String courseMajor) {
        this.courseMajor = courseMajor;
    }

    public void setCourseGrade(String courseGrade) {
        this.courseGrade = courseGrade;
    }

    public void setCourseTitle(String courseTitle) {
        this.courseTitle = courseTitle;
    }

    public void setCourseCredit(int courseCredit) {
        this.courseCredit = courseCredit;
    }

    public void setCourseDivide(int courseDivide) {
        this.courseDivide = courseDivide;
    }

    public void setCoursePersonnel(int coursePersonnel) {
        this.coursePersonnel = coursePersonnel;
    }

    public void setCourseProfessor(String courseProfessor) {
        this.courseProfessor = courseProfessor;
    }

    public void setCourseTime(String courseTime) {
        this.courseTime = courseTime;
    }

    public void setCourseRoom(String courseRoom) {
        this.courseRoom = courseRoom;
    }

    public int getCourseID() {
        return courseID;
    }

    public String getCourseUniversity() {
        return courseUniversity;
    }

    public String getCourseYear() {
        return courseYear;
    }

    public String getCourseTerm() {
        return courseTerm;
    }

    public String getCourseArea() {
        return courseArea;
    }

    public String getCourseMajor() {
        return courseMajor;
    }

    public String getCourseGrade() {
        return courseGrade;
    }

    public String getCourseTitle() {
        return courseTitle;
    }

    public int getCourseCredit() {
        return courseCredit;
    }

    public int getCourseDivide() {
        return courseDivide;
    }

    public int getCoursePersonnel() {
        return coursePersonnel;
    }

    public String getCourseProfessor() {
        return courseProfessor;
    }

    public String getCourseTime() {
        return courseTime;
    }

    public String getCourseRoom() {
        return courseRoom;
    }

    public int getCourseRival() {
        return courseRival;
    }

    public void setCourseRival(int courseRival) {
        this.courseRival = courseRival;
    }

    public Course(int courseID, String courseGrade, String courseTitle, int courseDivide, int coursePersonnel, int courseRival) {
        this.courseID = courseID;
        this.courseGrade = courseGrade;
        this.courseTitle = courseTitle;
        this.courseDivide = courseDivide;
        this.coursePersonnel = coursePersonnel;
        this.courseRival = courseRival;
    }



    public Course(int courseID, String courseUniversity, String courseYear, String courseTerm, String courseArea, String courseMajor, String courseGrade, String courseTitle, int courseCredit, int courseDivide, int coursePersonnel, String courseProfessor, String courseTime, String courseRoom) {
        this.courseID = courseID;
        this.courseUniversity = courseUniversity;
        this.courseYear = courseYear;
        this.courseTerm = courseTerm;
        this.courseArea = courseArea;
        this.courseMajor = courseMajor;
        this.courseGrade = courseGrade;
        this.courseTitle = courseTitle;
        this.courseCredit = courseCredit;
        this.courseDivide = courseDivide;
        this.coursePersonnel = coursePersonnel;
        this.courseProfessor = courseProfessor;
        this.courseTime = courseTime;
        this.courseRoom = courseRoom;
    }
}
