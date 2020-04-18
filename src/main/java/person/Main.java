package person;

import com.github.javafaker.Faker;


import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

public class Main {

    static Faker faker = new Faker();
    private static EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpa-example");

    private static Person randomPerson() {
        Person person = new Person();
        person.setName(faker.name().name());
        Date date = faker.date().birthday();
        LocalDate ld = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        person.setDob(ld);
        person.setGender(faker.options().option(Person.Gender.class));
        person.setEmail(faker.internet().emailAddress());
        person.setProfession(faker.company().profession());
        Address address = new Address();
        address.setCity(faker.address().city());
        address.setCountry(faker.address().country());
        address.setState(faker.address().state());
        address.setStreetAddress(faker.address().streetAddress());
        address.setZip(faker.address().zipCode());
        person.setAddress(address);
        return person;
    }

    private static void createPerson(int count) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            for(int i=0; i < count; i++)
                em.persist(randomPerson());
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    private static List<Person> getPeople() {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery("SELECT p FROM Person p", Person.class).getResultList();
        } finally {
            em.close();
        }
    }

    public static void main(String[] args) {
        createPerson(10);
        getPeople().forEach(System.out::println);
        emf.close();
    }


}
