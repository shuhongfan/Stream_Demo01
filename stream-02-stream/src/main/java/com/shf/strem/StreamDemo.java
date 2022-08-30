package com.shf.strem;

import com.shf.strem.pojo.Author;
import com.shf.strem.pojo.Book;
import com.sun.org.apache.bcel.internal.generic.NEW;
import org.junit.Test;

import java.util.*;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StreamDemo {
    @Test
    public void test28() {
        Stream<Integer> stream = Stream.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
//        Integer sum = stream.filter(num -> num > 5)
//                .reduce((result, ele) -> result + ele)
//                .get();
//        System.out.println(sum);

        Integer sum = stream.parallel() // 并行流
                .peek(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer num) {
                        System.out.println(num + Thread.currentThread().getName());
                    }
                })
                .filter(num -> num > 5)
                .reduce((result, ele) -> result + ele)
                .get();
        System.out.println(sum);

    }

    @Test
    public void test27() {
        List<Author> authors = getAuthors();
        authors.stream()
                .map(author -> author.getAge())
                .map(age -> age + 10)
                .filter(age->age>18)
                .map(age->age+2)
                .forEach(System.out::println);

        authors.stream()
                .mapToInt(Author::getAge)
                .map(age -> age + 10)
                .filter(age->age>18)
                .map(age->age+2)
                .forEach(System.out::println);

    }

    @Test
    public void test() {
        List<Author> authors = getAuthors();
        Stream<Author> authorStream = authors.stream();
//        引用类的静态方法
        authorStream.map(Author::getAge).map(String::valueOf);

        StringBuilder sb = new StringBuilder();
//        引用对象的实例方法
        authorStream.map(Author::getName).forEach(sb::append);

//        引用类的实例方法
        subAuthorName("三更草堂", String::substring);

//        构造器引用
        authors.stream()
                .map(Author::getName)
                .map(name->new StringBuilder(name))
                .map(sb::append)
                .forEach(System.out::println);
    }

    interface UseString{
        String use(String str,int start,int length);
    }

    public static String subAuthorName(String str, UseString useString){
        int start = 0;
        int length = 1;
        return useString.use(str,start,length);
    }

    /**
     * negate
     * <p>
     * Predicate接口中的方法。negate方法相当于是在判断添加前面加了个! 表示取反
     * <p>
     * 例如：
     * <p>
     * 打印作家中年龄不大于17的作家。
     */
    @Test
    public void testNegate() {
        List<Author> authors = getAuthors();
        authors.stream()
                .filter(new Predicate<Author>() {
                    @Override
                    public boolean test(Author author) {
                        return author.getAge() > 17;
                    }
                }.negate())
                .forEach(author -> System.out.println(author.getAge()));

    }

    /**
     * or
     * <p>
     * 我们在使用Predicate接口时候可能需要进行判断条件的拼接。而or方法相当于是使用||来拼接两个判断条件。
     * <p>
     * 例如：
     * <p>
     * 打印作家中年龄大于17或者姓名的长度小于2的作家。
     */
    @Test
    public void testOr() {
        List<Author> authors = getAuthors();
        authors.stream()
                .filter(new Predicate<Author>() {
                    @Override
                    public boolean test(Author author) {
                        return author.getAge() > 17;
                    }
                }.or(new Predicate<Author>() {
                    @Override
                    public boolean test(Author author) {
                        return author.getName().length() < 2;
                    }
                }))
                .forEach(author -> System.out.println(author.getName()));

    }

    /**
     * and
     * <p>
     * 我们在使用Predicate接口时候可能需要进行判断条件的拼接。而and方法相当于是使用&&来拼接两个判断条件
     * <p>
     * 例如：
     * <p>
     * 打印作家中年龄大于17并且姓名的长度大于1的作家。
     */
    @Test
    public void testAnd() {
        List<Author> authors = getAuthors();
        authors.stream()
                .filter(new Predicate<Author>() {
                    @Override
                    public boolean test(Author author) {
                        return author.getAge() > 17;
                    }
                }.and(new Predicate<Author>() {
                    @Override
                    public boolean test(Author author) {
                        return author.getName().length() > 1;
                    }
                }))
                .collect(Collectors.toList());
    }

    /**
     * 使用reduce求所有作者中年龄的最小值
     */
    @Test
    public void testReduceMin() {
        List<Author> authors = getAuthors();

        Integer min = authors.stream()
                .map(author -> author.getAge())
                .reduce(Integer.MAX_VALUE, (result, element) -> result > element ? element : result);
        System.out.println(min);
    }

    /**
     * 使用reduce求所有作者年龄的最大值
     */
    @Test
    public void testReduceMax() {
        List<Author> authors = getAuthors();

        Integer max = authors.stream()
                .map(author -> author.getAge())
                .reduce(
                        Integer.MIN_VALUE,
                        (result, element) -> result < element ? element : result);
        System.out.println(max);
    }

    /**
     * reduce归并
     * 对流中的数据按照你指定的计算方式计算出一个结果。（缩减操作）
     * <p>
     * reduce的作用是把stream中的元素给组合起来，我们可以传入一个初始值，它会按照我们的计算方式依次拿流中的元素和初始化值进行计算，计算结果再和后面的元素计算。
     * <p>
     * reduce两个参数的重载形式内部的计算方式如下：
     * T result = identity;
     * for (T element : this stream)
     * result = accumulator.apply(result, element)
     * return result;
     * 其中identity就是我们可以通过方法参数传入的初始值，accumulator的apply具体进行什么计算也是我们通过方法参数来确定的。
     * 例子：
     * <p>
     * 使用reduce求所有作者年龄的和
     */
    @Test
    public void testReduce() {
        List<Author> authors = getAuthors();
        Integer sum = authors.stream()
                .distinct()
                .map(author -> author.getAge())
//                .reduce(0, new BinaryOperator<Integer>() {
//                    @Override
//                    public Integer apply(Integer integer, Integer integer2) {
//                        return integer + integer2;
//                    }
//                })
                .reduce(0, (integer, integer2) -> integer + integer2);
        System.out.println(sum);
    }

    /**
     * findFirst
     * 获取流中的第一个元素。
     * 例子：
     * <p>
     * 获取一个年龄最小的作家，并输出他的姓名。
     */
    @Test
    public void testFindFirst() {
        List<Author> authors = getAuthors();
        Optional<Author> first = authors.stream()
                .sorted((o1, o2) -> o1.getAge() - o2.getAge())
                .findFirst();

        first.ifPresent(author -> System.out.println(author.getName()));
    }

    /**
     * findAny
     * 获取流中的任意一个元素。该方法没有办法保证获取的一定是流中的第一个元素。
     * 例子：
     * <p>
     * 获取任意一个年龄大于18的作家，如果存在就输出他的名字
     */
    @Test
    public void testFindAny() {
        List<Author> authors = getAuthors();

        Optional<Author> optionalAuthor = authors.stream()
                .filter(author -> author.getAge() > 18)
                .findAny();

        optionalAuthor.ifPresent(author -> System.out.println(author.getName()));
    }

    /**
     * noneMatch
     * 可以判断流中的元素是否都不符合匹配条件。如果都不符合结果为true，否则结果为false
     * 例子：
     * <p>
     * 判断作家是否都没有超过100岁的。
     */
    @Test
    public void testNoneMatch() {
        List<Author> authors = getAuthors();

        boolean noneMatch = authors.stream()
                .noneMatch(author -> author.getAge() > 100);
        System.out.println(noneMatch);
    }

    /**
     * allMatch
     * 可以用来判断是否都符合匹配条件，结果为boolean类型。如果都符合结果为true，否则结果为false。
     * 例子：
     * <p>
     * 判断是否所有的作家都是成年人
     */
    @Test
    public void testAllMatch() {
        List<Author> authors = getAuthors();

        boolean flag = authors.stream()
                .allMatch(author -> author.getAge() >= 18);
        System.out.println(flag);
    }

    /**
     * anyMatch
     * 可以用来判断是否有任意符合匹配条件的元素，结果为boolean类型。
     * 例子：
     * <p>
     * 判断是否有年龄在29以上的作家
     */
    @Test
    public void testAnyMatch() {
        List<Author> authors = getAuthors();

        boolean flag = authors.stream()
                .anyMatch(author -> author.getAge() > 29);
//                .anyMatch(new Predicate<Author>() {
//                    @Override
//                    public boolean test(Author author) {
//                        return author.getAge() > 29;
//                    }
//                });
        System.out.println(flag);
    }

    /**
     * collect
     * 把当前流转换成一个集合。
     * 例子：
     * <p>
     * 获取一个存放所有作者名字的List集合。
     */
    @Test
    public void testCollect() {
        List<Author> authors = getAuthors();

        List<String> collect = authors.stream()
                .map(author -> author.getName())
                .collect(Collectors.toList());
        System.out.println(collect);

//        获取一个所有书名的Set集合。
        Set<String> set = authors.stream()
                .map(author -> author.getName())
                .collect(Collectors.toSet());
        System.out.println(set);

//        获取一个Map集合，map的key为作者名，value为List<Book>
        Map<String, List<Book>> map = authors.stream()
                .distinct()
                .collect(Collectors.toMap(
                        author -> author.getName(),
                        author -> author.getBooks()));
        System.out.println(map);
    }

    /**
     * max&min
     * 可以用来或者流中的最值。
     * 例子：
     * <p>
     * 分别获取这些作家的所出书籍的最高分和最低分并打印。
     */
    @Test
    public void testMaxAndMin() {
        List<Author> authors = getAuthors();

        Optional<Integer> max = authors.stream()
                .flatMap(author -> author.getBooks().stream())
                .distinct()
                .map(book -> book.getScore())
                .max(new Comparator<Integer>() {
                    @Override
                    public int compare(Integer o1, Integer o2) {
                        return o1 - o2;
                    }
                });
        System.out.println(max.get());

        Optional<Integer> min = authors.stream()
                .flatMap(author -> author.getBooks().stream())
                .distinct()
                .map(book -> book.getScore())
                .min((o1, o2) -> o1 - o2);
        System.out.println(min.get());
    }

    /**
     * count
     * 可以用来获取当前流中元素的个数。
     * 例子：
     * <p>
     * 打印这些作家的所出书籍的数目，注意删除重复元素。
     */
    @Test
    public void testCount() {
        List<Author> authors = getAuthors();
        long count = authors.stream()
                .flatMap(author -> author.getBooks().stream())
                .distinct()
                .count();
        System.out.println("count=" + count);
    }

    /**
     * 终结操作
     * forEach
     * 对流中的元素进行遍历操作，我们通过传入的参数去指定对遍历到的元素进行什么具体操作。
     * 例子：
     * <p>
     * 输出所有作家的名字
     */
    @Test
    public void testForEach() {
        List<Author> authors = getAuthors();

        authors.stream()
                .map(author -> author.getName())
                .distinct()
                .forEach(name -> System.out.println(name));
    }


    /**
     * 例二：
     * <p>
     * 打印现有数据的所有分类。要求对分类进行去重。不能出现这种格式：哲学,爱情
     */
    @Test
    public void testFlatMap2() {
        List<Author> authors = getAuthors();
        authors.stream()
                .flatMap(author -> author.getBooks().stream())
                .distinct()
                .flatMap(book -> Arrays.stream(book.getCategory().split(",")))
                .distinct()
                .forEach(System.out::println);

    }

    /**
     * flatMap
     * map只能把一个对象转换成另一个对象来作为流中的元素。而flatMap可以把一个对象转换成多个对象作为流中的元素。
     * 例一：
     * <p>
     * 打印所有书籍的名字。要求对重复的元素进行去重。
     */
    @Test
    public void testFlatMap() {
        List<Author> authors = getAuthors();

        authors.stream()
//                .flatMap(new Function<Author, Stream<?>>() {
//                    @Override
//                    public Stream<?> apply(Author author) {
//                        return author.getBooks().stream();
//                    }
//                })
                .flatMap(author -> author.getBooks().stream())
                .distinct()
                .forEach(book -> System.out.println(book.getName()));

    }

    /**
     * skip
     * 跳过流中的前n个元素，返回剩下的元素
     * 例如：
     * <p>
     * 打印除了年龄最大的作家外的其他作家，要求不能有重复元素，并且按照年龄降序排序。
     */
    @Test
    public void testSkip() {
        List<Author> authors = getAuthors();

        authors.stream()
                .distinct()
                .sorted()
                .skip(2)
                .forEach(System.out::println);
    }

    /**
     * limit
     * 可以设置流的最大长度，超出的部分将被抛弃
     * 对流中的元素按照年龄进行降序排序，并且要求不能有重复的元素,然后打印其中年龄最大的两个作家的姓名。
     */
    @Test
    public void testLimit() {
        List<Author> authors = getAuthors();

        authors.stream()
                .distinct()
                .sorted()
                .limit(2)
                .forEach(System.out::println);
    }

    /**
     * 如果调用空参的sorted()方法，需要流中的元素是实现了Comparable。
     * <p>
     * sorted
     * 可以对流中的元素进行排序。
     * 例如：
     * <p>
     * 对流中的元素按照年龄进行降序排序，并且要求不能有重复的元素。
     */
    @Test
    public void testSorted() {
        List<Author> authors = getAuthors();

        authors.stream()
                .sorted(new Comparator<Author>() {
                    @Override
                    public int compare(Author o1, Author o2) {
                        return o2.getAge() - o1.getAge();
                    }
                })
                .forEach(System.out::println);
    }

    /**
     * distinct方法是依赖Object的equals方法来判断是否是相同对象的。所以需要注意重写equals方法。
     * <p>
     * distinct
     * 可以去除流中的重复元素。
     * 例如：
     * <p>
     * 打印所有作家的姓名，并且要求其中不能有重复元素。
     */
    @Test
    public void testDistinct() {
        List<Author> authors = getAuthors();

        authors.stream()
                .distinct()
                .forEach(author -> author.getName());
    }

    /**
     * map
     * 可以把对流中的元素进行计算或转换。
     * 例如：
     * <p>
     * 打印所有作家的姓名
     */
    @Test
    public void testMap() {
        List<Author> authors = getAuthors();

        authors.stream()
//                .map(new Function<Author, String>() {
//                    @Override
//                    public String apply(Author author) {
//                        return author.getName();
//                    }
//                })
//                .map(author->author.getName())
                .map(Author::getName)
                .forEach(System.out::println);

        authors.stream()
                .map(author -> author.getAge())
                .map(age -> age + 10)
                .forEach(System.out::println);
    }


    /**
     * filter
     * 可以对流中的元素进行条件过滤，符合过滤条件的才能继续留在流中。
     * 例如：
     * <p>
     * 打印所有姓名长度大于1的作家的姓名
     */
    @Test
    public void testFilter() {
        List<Author> authors = getAuthors();
        authors.stream()
                .filter(author -> author.getName().length() > 1)
                .forEach(System.out::println);
    }

    @Test
    public void test3() {
        Map<String, Integer> map = new HashMap<>();
        map.put("蜡笔小新", 19);
        map.put("黑子", 17);
        map.put("日向翔阳", 16);

//        双列集合：转换成单列集合后再创建
        Set<Map.Entry<String, Integer>> entrySet = map.entrySet();
        Stream<Map.Entry<String, Integer>> stream = entrySet.stream();

        stream.filter(e -> e.getValue() > 16).forEach(System.out::println);
    }

    @Test
    public void test2() {
        Integer[] arr = {1, 2, 3, 4, 5};
//        单列集合： 集合对象.stream()
//        数组：Arrays.stream(数组) 或者使用Stream.of来创建
        Stream<Integer> stream = Arrays.stream(arr);
        stream.distinct().forEach(System.out::println);

        Stream<Integer> stream1 = Stream.of(arr);
        stream1.distinct().forEach(System.out::println);
    }


    @Test
    public void test1() {
        List<Author> authors = getAuthors();
        authors.stream()
                .distinct()
                .filter(author -> author.getAge() < 18)
                .forEach(author -> author.getName());

    }

    private static List<Author> getAuthors() {
        //数据初始化
        Author author = new Author(1L, "蒙多", 33, "一个从菜刀中明悟哲理的祖安人", null);
        Author author2 = new Author(2L, "亚拉索", 15, "狂风也追逐不上他的思考速度", null);
        Author author3 = new Author(3L, "易", 14, "是这个世界在限制他的思维", null);
        Author author4 = new Author(3L, "易", 14, "是这个世界在限制他的思维", null);

        //书籍列表
        List<Book> books1 = new ArrayList<>();
        List<Book> books2 = new ArrayList<>();
        List<Book> books3 = new ArrayList<>();

        books1.add(new Book(1L, "刀的两侧是光明与黑暗", "哲学,爱情", 88, "用一把刀划分了爱恨"));
        books1.add(new Book(2L, "一个人不能死在同一把刀下", "个人成长,爱情", 99, "讲述如何从失败中明悟真理"));

        books2.add(new Book(3L, "那风吹不到的地方", "哲学", 85, "带你用思维去领略世界的尽头"));
        books2.add(new Book(3L, "那风吹不到的地方", "哲学", 85, "带你用思维去领略世界的尽头"));
        books2.add(new Book(4L, "吹或不吹", "爱情,个人传记", 56, "一个哲学家的恋爱观注定很难把他所在的时代理解"));

        books3.add(new Book(5L, "你的剑就是我的剑", "爱情", 56, "无法想象一个武者能对他的伴侣这么的宽容"));
        books3.add(new Book(6L, "风与剑", "个人传记", 100, "两个哲学家灵魂和肉体的碰撞会激起怎么样的火花呢？"));
        books3.add(new Book(6L, "风与剑", "个人传记", 100, "两个哲学家灵魂和肉体的碰撞会激起怎么样的火花呢？"));

        author.setBooks(books1);
        author2.setBooks(books2);
        author3.setBooks(books3);
        author4.setBooks(books3);

        List<Author> authorList = new ArrayList<>(Arrays.asList(author, author2, author3, author4));
        return authorList;
    }
}
