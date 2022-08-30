package com.shf.strem;

import com.shf.strem.pojo.Author;
import com.shf.strem.pojo.Book;
import org.junit.Test;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class OptionalDemo {
    /**
     * 判断
     * 我们可以使用isPresent方法进行是否存在数据的判断。如果为空返回值为false,如果不为空，返回值为true。但是这种方式并不能
     */
    @Test
    public void testIsPresent() {
//        Optional<Author> authorOptional = Optional.ofNullable(getAuthor());
//
//        if (authorOptional.isPresent()) {
//            System.out.println(authorOptional.get().getName());
//        }
    }

    /**
     *  数据转换
     * Optional还提供了map可以让我们的对数据进行转换，并且转换得到的数据也还是被Optional包装好的，保证了我们的使用安全。
     */
    private static void testMap() {
        Optional<Author> authorOptional = getAuthorOptional();
        Optional<List<Book>> optionalBooks = authorOptional.map(author -> author.getBooks());
        optionalBooks.ifPresent(books -> System.out.println(books));
    }

    /**
     * 过滤
     * 我们可以使用filter方法对数据进行过滤。如果原本是有数据的，但是不符合判断，也会变成一个无数据的Optional对象。
     */
    @Test
    private void testFilter() {
        Optional<Author> authorOptional = getAuthorOptional();
        Optional<Author> author = authorOptional.filter(author1 -> author1.getAge() > 18);
    }

    /**
     * orElseGet
     * <p>
     * 获取数据并且设置数据为空时的默认值。如果数据不为空就能获取到该数据。如果为空则根据你传入的参数来创建对象作为默认值返回。
     */
    @Test
    public void testOrElseGet() {
        Optional<Author> authorOptional = getAuthorOptional();
        Author author = authorOptional.orElseGet(() -> new Author());
        System.out.println(author.getName());
    }

    /**
     * orElseThrow
     * <p>
     * 获取数据，如果数据不为空就能获取到该数据。如果为空则根据你传入的参数来创建异常抛出。
     */
    @Test
    public void testOrElseThrow() {
        Optional<Author> authorOptional = getAuthorOptional();
        Author author = authorOptional.orElseThrow(() -> new RuntimeException("数据为null"));
        System.out.println(author);
    }

    public static Optional<Author> getAuthorOptional() {
        Author author = new Author(1L, "蒙多", 33, "一个从菜刀中明悟哲理的祖安人", null);

        return Optional.ofNullable(null);
    }

    public static Optional<Author> getAuthor() {
        Author author = new Author(1L, "蒙多", 33, "一个从菜刀中明悟哲理的祖安人", null);
        return Optional.ofNullable(author);
    }

}
