package hiber;

import hiber.config.AppConfig;
import hiber.model.Car;
import hiber.model.User;
import hiber.service.UserService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.sql.SQLException;
import java.util.List;

public class MainApp {
   public static void main(String[] args) throws SQLException {
      AnnotationConfigApplicationContext context = 
            new AnnotationConfigApplicationContext(AppConfig.class);

      UserService userService = context.getBean(UserService.class);

       // Создаем машины отдельно
       Car car1 = new Car("Toyota", 2020);
       Car car2 = new Car("Honda", 2021);
       Car car3 = new Car("BMW", 2022);
       Car car4 = new Car("Mercedes", 2023);
       Car car5 = new Car("BMW", 2022);
       Car car6 = new Car("BMW", 2022);

       // Создаем пользователей с машинами
       userService.add(new User("User1", "Lastname1", "user1@mail.ru", car1));
       userService.add(new User("User2", "Lastname2", "user2@mail.ru", car2));
       userService.add(new User("User3", "Lastname3", "user3@mail.ru", car3));
       userService.add(new User("User4", "Lastname4", "user4@mail.ru", car4));
       userService.add(new User("User5", "Lastname5", "user5@mail.ru", car5));
       userService.add(new User("User6", "Lastname6", "user5@mail.ru", car6));

       // Выводим всех пользователей
       List<User> users = userService.listUsers();
       for (User user : users) {
           System.out.println("Id = " + user.getId());
           System.out.println("First Name = " + user.getFirstName());
           System.out.println("Last Name = " + user.getLastName());
           System.out.println("Email = " + user.getEmail());
           if (user.getCar() != null) {
               System.out.println("Car = " + user.getCar().getModel() + " " + user.getCar().getSeries());
           }
           System.out.println();
       }

       // Тестируем поиск по машине
       System.out.println("=== Поиск владельца BMW 2022 ===");
       User bmwOwner = userService.findUserByCar("BMW", 2022);
       if (bmwOwner != null) {
           System.out.println("Владелец BMW: " + bmwOwner.getFirstName() + " " + bmwOwner.getLastName());
       } else {
           System.out.println("Владелец не найден");
       }

       context.close();
   }
}
