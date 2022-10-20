package com.brew.home.mockito.spring.home.mockito.learn1;

import com.brew.home.mockito.spring.home.mockito.learn1.test.TestService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;

/**
 * @author shaogz
 */
class LearnMockito {


    @Test
    void test1() {
        // 你可以mock具体的类型,不仅只是接口
        LinkedList<String> mockedList = mock(LinkedList.class);
        // 开始设置测试桩 （即Stub）,当get(0)被调用时，返回"first"
        when(mockedList.get(0)).thenReturn("first");
        // 方法get(1)被调用时，抛异常。
        when(mockedList.get(1)).thenThrow(new RuntimeException());
        System.out.println(mockedList.get(0));
        try {
            System.out.println(mockedList.get(1));
        } catch (RuntimeException e) {
            System.out.println("catch a when..thenThrow");
        }
        // 输出 null，因为get(999)的调用没有被设置过
        System.out.println(mockedList.get(999));
    }

    @Test
    void test2() {
        LinkedList<Integer> mockedList = mock(LinkedList.class);
        // 使用doReturn语句和when语句一样的效果
        doReturn(1).when(mockedList).get(1);
        System.out.println(mockedList.get(1));
        // 使用doNothing来设置void返回值的方法
        doNothing().when(mockedList).clear();
        // 设置执行clear方法抛出异常
        doThrow(new RuntimeException()).when(mockedList).clear();
        try {
            mockedList.clear();
        } catch (Exception e) {
            System.out.println("catch a doThrow..when..");
        }
        //以下断言表示，mockedList的clear方法被调用了1次
        //程序员的测试课里说的，应该是这个东西少用，本质上还是断言，但是，是面相实现的
        verify(mockedList, times(1)).clear();
    }

    @Test
    void test3() {
        //希望每次调用返回值都不同
        LinkedList<Integer> mockedList = mock(LinkedList.class);
        // 第1次调用返回2，第2次返回2，以后再调用返回3
        when(mockedList.size()).thenReturn(1, 2, 3);
        // 等价写法
        // when(mockedList.size()).thenReturn(1).thenReturn(2).thenReturn(3).thenReturn(4);

        System.out.println(mockedList.size());
        System.out.println(mockedList.size());
        System.out.println(mockedList.size());
        System.out.println(mockedList.size());


        // 设置返回值是 参数值*10
        when(mockedList.get(anyInt())).thenAnswer(invocationOnMock -> {
            int arguments = invocationOnMock.getArgument(0);
            return 10*arguments;
        });
        System.out.println(mockedList.get(10));
        System.out.println(mockedList.get(20));
        System.out.println(mockedList.get(30));
    }

    @Test
    void test4() {
        TestService testService = mock(TestService.class);
        // anyString() 表示任何字符串参数，anyInt() 表示任何int类型参数
        when(testService.say(anyString(), anyInt(), anyString())).thenReturn("world");
        System.out.println(testService.say("x", 1, "x"));
        // 如果参数列表包含参数匹配器，则必能出现具体参数值，要使用eq() 方法代替
        // when(testService.say(anyString(), 1, anyString())).thenReturn("world2");
        when(testService.say(anyString(), eq(1), anyString())).thenReturn("world2");
        System.out.println(testService.say("x", 1, "x"));
    }

    @Test
    void test5() {
        //执行真实的方法
        List list = mock(LinkedList.class);
        when(list.size()).thenCallRealMethod();
        //因为这是调用的mock 的 List
        list.add(1);
        //这里调用的是真的
        System.out.println(list.size());

        //重制mock对象
        reset(list);
        when(list.size()).thenReturn(2);
        System.out.println(list.size());

    }

    /*
     mock对象会覆盖整个被mock的对象，因此没有stub（即测试桩）的方法只能返回默认值，并且类的方法不会真正的执行。
     比如int类型返回0，boolean返回false，对象类型返回null。
     Mock出来的对象（代理对象），默认不会去真正执行类的方法。而用Spy声明的对象（真实对象），则会默认执行真正的方法。
     */
    @Test
    void test6() {
        List<Integer> list = spy(new ArrayList<>());
        list.add(1);list.add(2);
        // 分别输出1和2，说明真正执行了add和get方法
        System.out.println(list.get(0));System.out.println(list.get(1));
        // 进行部分mock
        when(list.isEmpty()).thenReturn(true);
        // 输出true，说明isEmpty方法被mock了
        System.out.println(list.isEmpty());
        // 分别输出1和2，说明get方法不受mock影响
        System.out.println(list.get(0));
        System.out.println(list.get(1));
    }

    @Test
    void test7() {
        // 需要注意的是，如果为Spy出来的对象进行Stub，有时候不能使用when，因为Spy对象调用方法时，会调用真实的方法。比如以下例子
        List list = new LinkedList();
        List spy = spy(list);

        try {
            // 不可能 : 因为当调用spy.get(0)时会调用真实对象的get(0)函数,此时会发生IndexOutOfBoundsException异常，因为真实List对象是空的
            when(spy.get(0)).thenReturn("foo");
        } catch (Exception e) {
            System.out.println("spy 调用了真实的方法，并抛出了 " + e.getMessage());
        }
        // 你需要使用doReturn()来打桩
        doReturn("foo").when(spy).get(0);
        System.out.println(spy.get(0));
    }

    @Test
    void test8() {
        /*
         1. 这里 断言框架使用的hamcrest
         2. 这部分是没测试
         */
        List<Integer> list = mock(List.class);
        // 断言list.get(0)值等于1
        assertThat(list.get(0), equalTo(1));
        // 断言大于50
        assertThat(list.get(0), greaterThan(20));
        // 断言小于等于50
        assertThat(list.get(0), lessThanOrEqualTo(50));
        // 断言 必须大于20 并且 小于等于50（所有条件成立）
        assertThat(list.get(0), allOf(greaterThan(20), lessThanOrEqualTo(50)));
        // 断言 必须大于20 或 小于等于50（其中至少一个条件成立）
        assertThat(list.get(0), oneOf(greaterThan(20), lessThanOrEqualTo(50)));
        // 断言任何条件都成立
        assertThat(list.get(0), anything());
        // 断言等于1
        assertThat(list.get(0), is(1));
        // 断言不等于-1
        assertThat(list.get(0), not(-1));
        // 断言返回的字符串包含1
        List<String> list2 = mock(List.class);
        assertThat(list2.get(0), containsString("1"));
        // 断言返回的字符串以1开头
        assertThat(list2.get(0), Matchers.startsWith("1"));
        // 断言该异常属于RuntimeException
        assertThat(new RuntimeException("xx"), instanceOf(RuntimeException.class));
    }

    @Test
    void test9() {
        //Mockito可以对函数的执行过程进行断言，通过断言函数的执行次数，要对方法执行逻辑进行判断。
        //好像不推荐用这个
        List<String> mockedList = mock(List.class);
        mockedList.add("once");
        mockedList.add("twice");
        mockedList.add("twice");
        mockedList.add("three times");
        mockedList.add("three times");
        mockedList.add("three times");

        //期望mockedList的add("once")方法执行了1次
        verify(mockedList, times(1)).add("once");

        // 验证具体的执行次数，分别希望是2次和3次
        verify(mockedList, times(2)).add("twice");
        verify(mockedList, times(3)).add("three times");

        // 使用never()进行验证,never相当于times(0)，即没有执行过
        verify(mockedList, never()).add("never happened");

        // 使用atLeast()至少执行次数/atMost()最多执行次数
        verify(mockedList, atLeastOnce()).add("three times");
        // verify(mockedList, atLeast(2)).add("five times");
        // verify(mockedList, atMost(5)).add("three times");
    }

    @Test
    void test10() {
        // 进行mock
        List<String> singleMock = mock(List.class);
        singleMock.add("was added first");
        singleMock.add("was added second");
        // 为该mock对象创建一个inOrder对象
        InOrder inOrder = inOrder(singleMock);
        // 确保add函数首先执行的是add("was added first"),然后才是add("was added second")
        inOrder.verify(singleMock).add("was added first");
        inOrder.verify(singleMock).add("was added second");
    }
}
