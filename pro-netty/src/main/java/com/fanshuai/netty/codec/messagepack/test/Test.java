package com.fanshuai.netty.codec.messagepack.test;

import org.msgpack.MessagePack;
import org.msgpack.template.Templates;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Test {
    public static void main(String[] args) throws Exception{

        //test2();
        //test3();
        //test4();
        test5();
    }

    public static void test1() throws Exception{
        List<String> honglou = new ArrayList<>();
        honglou.add("lindaiyu");
        honglou.add("xuebaochai");
        honglou.add("wangxifeng");

        MessagePack messagePack = new MessagePack();
        byte[] data = messagePack.write(honglou);

        List<String> honglou1 = messagePack.read(data, Templates.tList(Templates.TString));
        System.out.println(honglou1);
    }

    public static void test2() throws Exception{
        Person person = new Person();
        person.setName("fanshuai");
        person.setAge(18);

        MessagePack pack = new MessagePack();
        pack.register(Person.class);
        byte[] data = pack.write(person);

        Person p1 = pack.read(data, Person.class);

        System.out.println(p1);
    }

    public static void test3() throws Exception {

        Person p1 = new Person("lindaiyu", 18);
        Person p2 = new Person("xuebaochai", 19);
        Person p3 = new Person("wangxifeng", 20);

        List<Person> list = new ArrayList<>();
        list.add(p1);
        list.add(p2);
        list.add(p3);

        MessagePack messagePack = new MessagePack();
        messagePack.register(Person.class);
        byte[] data = messagePack.write(list);

        List<Person> personList = messagePack.read(data, Templates.tList(messagePack.lookup(Person.class)));

        System.out.println(personList);
    }

    public static void test4() throws Exception{
        Person p1 = new Person("lindaiyu", 18);
        Person p2 = new Person("xuebaochai", 19);
        Person p3 = new Person("wangxifeng", 20);

        List<Person> list = new ArrayList<>();
        list.add(p1);
        list.add(p2);
        list.add(p3);

        Persons persons = new Persons();
        persons.setList(list);

        MessagePack messagePack = new MessagePack();
        messagePack.register(Person.class);
        messagePack.register(Persons.class);
        byte[] data = messagePack.write(persons);

        Persons persons1 = messagePack.read(data, Persons.class);
        System.out.println(persons1);
    }

    public static void test5() throws Exception{
        Person p1 = new Person("lindaiyu", 18);
        Person p2 = new Person("xuebaochai", 19);
        Person p3 = new Person("wangxifeng", 20);

        Map<String, Person> map = new HashMap<>();
        map.put("lindaiyu", p1);
        map.put("xuebaochai", p2);
        map.put("wangxifeng", p3);

        MessagePack messagePack = new MessagePack();
        messagePack.register(Person.class);
        byte[] data = messagePack.write(map);

        Map<String, Person> map1 = messagePack.read(data, Templates.tMap(Templates.TString, messagePack.lookup(Person.class)));

        System.out.println(map1);
    }
}
