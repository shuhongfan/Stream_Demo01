/*
 * @author tsui
 * @date 2022/2/13 15:34
 */
package com.tsui.entity;



import lombok.ToString;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@ToString
public class Author implements Comparable<Author>{
    private Long id;
    private String name;
    private String introduction;
    private Integer age;
    private List<Book> bookList;

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public List<Book> getBookList() {
        return bookList;
    }

    public void setBookList(List<Book> bookList) {
        this.bookList = bookList;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public Author() {
    }

    public Author(Long id, String name, String introduction, Integer age, List<Book> bookList) {
        this.id = id;
        this.name = name;
        this.introduction = introduction;
        this.age = age;
        this.bookList = bookList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Author author = (Author) o;

        if (id != null ? !id.equals(author.id) : author.id != null) return false;
        if (name != null ? !name.equals(author.name) : author.name != null) return false;
        if (introduction != null ? !introduction.equals(author.introduction) : author.introduction != null)
            return false;
        if (age != null ? !age.equals(author.age) : author.age != null) return false;
        return bookList != null ? bookList.equals(author.bookList) : author.bookList == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (introduction != null ? introduction.hashCode() : 0);
        result = 31 * result + (age != null ? age.hashCode() : 0);
        result = 31 * result + (bookList != null ? bookList.hashCode() : 0);
        return result;
    }

    /**
     * ??????sorted??????????????????????????????????????????????????????????????????
     */
    @Override
    public int compareTo(Author o) {
        // return 0; // ?????????????????????
        // 0????????????????????????????????????????????????
        // ??????sorted?????????????????????????????????????????????????????????????????????????????????
        return o.getAge() - this.getAge();
    }
}
