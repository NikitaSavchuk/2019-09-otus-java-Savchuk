package ru.otus;

import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class JsonWriterTest {
    private JsonObjWriter jsonWriter;
    private Gson gson;
    private Person person;

    @BeforeEach
    void setUp() {
        jsonWriter = new JsonObjWriter();
        gson = new Gson();
        person = new Person();
    }

    @Test
    void gsonStringEqualsTest() {
        String jsonString = jsonWriter.toJsonString(person);
        System.out.println("to Json String\n" + jsonString);
        String gsonJsonString = gson.toJson(person);
        System.out.println("gson to Json\n" + gsonJsonString);

        assertEquals(jsonString, gsonJsonString);
    }

    @Test
    void objFromJsonEqualsTest() {
        String jsonString = jsonWriter.toJsonString(person);
        Person personFromJson = gson.fromJson(jsonString, Person.class);

        assertEquals(personFromJson, person);
    }
}
