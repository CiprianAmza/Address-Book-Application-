package com.example.demo;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

@Controller
public class UserController { // controller of addressBook-users

    private List<User> listUsers = new ArrayList<>();
    private DBConnection myConnection = new DBConnection();
    private User user = new User("Ciprian", "Amza", "Bucuresti", "https://c.tenor.com/72341xps0RoAAAAd/luffy-luffy-smile.gif", 1);
    private String passwordStatus = "";
    private final CsvExportService csvExportService;

    public UserController(CsvExportService csvExportService) { // some random users initialization for displaying the first page
        listUsers.add(user);
        listUsers.add(new User("Andreea", "Avram", "Cluj", "https://assets.puzzlefactory.pl/puzzle/258/817/original.jpg", 2));
        listUsers.add(new User("Alina", "Antohi", "Bucuresti", "https://i.pinimg.com/originals/8a/54/20/8a54202a265c07636e5718d270c3ce6c.gif",3));
        this.csvExportService = csvExportService;
    }

    @GetMapping("/") // main page (if user is not logged in - same as /home)
    public String listAll(Model model) {
        model.addAttribute("listUsers", listUsers);
        return "home";
    }

    @GetMapping("/home") // main page (if user is not logged in)
    public String homePage(Model model) {
        model.addAttribute("listUsers", listUsers);
        return "home";
    }

    @GetMapping("/hello") // main page (if user is logged in)
    public String hello(Model model) {
        model.addAttribute("listUsers", listUsers);
        return "hello";
    }

    @GetMapping("hello/doDelete/{id}") // deleting an user (addressBookData) from the addressBook and from the database by ID
    public String deleteUser (@PathVariable Integer id) {
        List<User> listUsers2 = new ArrayList<>(); // creating a new empty list, where only the remaining elements are stored
        for (User user: listUsers) {
            if (user.getID() == id) continue;
            else listUsers2.add(user);
        }
        myConnection.deleteFromDatabaseById(id); // deleting the user from the database
        listUsers = new ArrayList<>(listUsers2); // creating a new list (not a copy) of remaining users
        return "redirect:/hello";
    }

    @GetMapping("hello/doEdit/{id}") // the display of the edit page for an address book
    public String editUser(@PathVariable Integer id, Model model) {
        for (User user: listUsers) {
            if (user.getID() == id) {
                model.addAttribute("user", user);
                break;
            }
        }
        return "editUser";
    }

    @PostMapping("hello/doEdit/{id}") // editing the user from the adressBook and from the database
    public String saveEditUser(@RequestParam(name = "firstName") String firstName,
                               @RequestParam(name = "lastName") String lastName,
                               @RequestParam(name = "address") String address,
                               @RequestParam(name = "photo") String photo,
                               @RequestParam(name = "ID") String ID) {

        Integer modifiedID = Integer.parseInt(ID);
        for (User user: listUsers) {
            if (user.getID() == modifiedID) { // storing the changes only if the values were modified
                if (!firstName.equals("")) user.setFirstName(firstName);
                if (!lastName.equals("")) user.setLastName(lastName);
                if (!address.equals("")) user.setAddress(address);
                if (!photo.equals("")) user.setPhoto(photo);
                break;
            }
        }

        // the update process has two parts:
        // 1. deleting the old user with the current ID
        String stm = "DELETE FROM \"user\" WHERE ID=" + modifiedID;
        myConnection.deleteFromDatabaseById(modifiedID);

        // 2. re-inserting the new data of the user with the same ID
        myConnection.insertElementInDatabaseById(modifiedID, user.getAddress(), user.getFirstName(), user.getLastName(), user.getPhoto());

        return "redirect:/hello"; // redirecting to the main page after the job is done, as in any other modificationData-method
    }

    @GetMapping("/addUser") // view of the addUser page
    public String addUserPage(Model model) {
        return "addUser";
    }

    @PostMapping("/addUser") // adding a new user in the adressBook and in the dataBase
    public String addUser(@RequestParam(name = "firstName") String firstName,
                          @RequestParam(name = "lastName") String lastName,
                          @RequestParam(name = "address") String address,
                          @RequestParam(name = "photo") String photo) {

        User userToAdd = new User(firstName, lastName, address, photo, listUsers.size()+1);
        myConnection.insertElementInDatabase(listUsers.size()+1, address, firstName, lastName, photo);
        listUsers.add(userToAdd);
        return "redirect:/hello";
    }

    @GetMapping("/searchUserByName") // the search function -> user can search by both lastName and firstName, the whole name or any part of it
    public String searchUserByName(Model model, @RequestParam(name= "name") String name){
        List<User> result = new ArrayList<>();
        for (User user: listUsers) {
            if (user.getFirstName().toLowerCase().contains(name.toLowerCase()) ||
                    user.getLastName().toLowerCase().contains(name.toLowerCase())) {
                result.add(user);
            }
        }
        model.addAttribute("listUsers", result);
        return "searchUserByName";
    }

    @RequestMapping(path = "/users") // CSV-exporting method, elements are both taken and updated in the database
    public void getAllUsersInCsv(HttpServletResponse servletResponse) throws IOException {
        servletResponse.setContentType("text/csv");
        servletResponse.addHeader("Content-Disposition","attachment; filename=\"users.csv\"");
        List<String> users = new ArrayList<>();
        for (User user: listUsers) {
            users.add(user.toString());
        }
        for (User user: listUsers) {
            myConnection.insertElementInDatabase(user.getID(), user.getAddress(), user.getFirstName(), user.getLastName(), user.getPhoto());
        }
        csvExportService.writeUsersToCsv(servletResponse.getWriter());
    }


    @GetMapping("/createaccount") // view of the registration page
    public String createaccount(Model model){
        System.out.println("ceva");
        model.addAttribute("passwordStatus", passwordStatus);
        return "createaccount";
    }

    /*
    Username must be at least 5 characters long and must not be already stored in the database.
    Password can not contain parts of the username, must be at least 9 characters long,
    must contain at least 1 digit, 1 upper character and 1 lower character.
    The password must be typed in 2 times and the two passwords must match.
    At the end, if the username and the password are both valid, the new data is inserted into the database.
    There is still a small problem here with updating the database (live in the app) after creating a new account.
     */
    @PostMapping("/createaccount") // Password and user validation at registration point
    public String createNewAccount(@RequestParam(name = "username") String username,
                                   @RequestParam(name = "password") String password,
                                   @RequestParam(name = "confirmedPassword") String confirmedPassword){

        if (myConnection.isAccountDatabaseEmpty(username)) {
            passwordStatus = "userNameExists";
            return "redirect:/createaccount";
        }
        else if (username.length() < 5) {
            passwordStatus = "tooShort";
        }
        else if (username.toLowerCase().contains(password.toLowerCase()) || password.toLowerCase().contains(username.toLowerCase())) {
            passwordStatus = "userNameInPassword";
        }
        else if (!password.equals(confirmedPassword)) {
            passwordStatus = "passwordNotMatch";
        }
        else if (password.length() < 8) {
            passwordStatus = "passwordTooShort";
        }
        else {
            int lowerChars = 0, upperChars = 0, digits = 0;
            Set<String> digitsSet = new HashSet<>(List.of("0", "1", "2", "3", "4", "5", "6", "7", "8", "9"));
            for (int i = 0; i < password.length(); i++) {
                String element = "" + password.charAt(i);
                if (digitsSet.contains(element)) {
                    digits++;
                }
                else if (element.toLowerCase().equals(element)) {
                    lowerChars++;
                }
                else if (element.toUpperCase().equals(element)) {
                    upperChars++;
                }
            }
            if (digits == 0) {
                passwordStatus = "noDigits";
            }
            else if (upperChars == 0) {
                passwordStatus = "noUpperChars";
            }
            else if (lowerChars == 0) {
                passwordStatus = "noLowerChars";
            }
            else { // if everything is all right, two things happen
                passwordStatus = ""; // first -> password-status is reseted

                // second -> the new data of the users are stored in the database
                myConnection.insertIntoAccountDatabase(new Random().nextInt(0, 1000000), username, password);

                //new WebSecurityConfig().updatePasswords();
                return "redirect:/hello";
            }
        }
        return "redirect:/createaccount";
    }

}