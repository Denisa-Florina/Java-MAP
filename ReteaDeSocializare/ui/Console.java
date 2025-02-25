package src.lab7.ui;

import src.lab7.domain.Utilizator;
import src.lab7.domain.validators.ValidationException;
import src.lab7.domain.validators.ValidationFriendship;
import src.lab7.service.Service;
import src.lab7.service.ServiceCommunity;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Console {

    private Service service;
    private ServiceCommunity serviceCommunity;

    public Console(Service service, ServiceCommunity serviceCommunity) {
        this.service = service;
        this.serviceCommunity = serviceCommunity;
    }

    void printMenu() {
        System.out.println("\n\t\t\tMENU\t\t\t");
        System.out.println("1. Add user");
        System.out.println("2. Remove user");
        System.out.println("3. Add friendship");
        System.out.println("4. Remove friendship");
        System.out.println("5. Print users");
        System.out.println("6. Print friendships");
        System.out.println("7. Communities");
        System.out.println("8. Most Sociable Community");
        System.out.println("0. EXIT");
    }

    public void run() {
        Scanner scan = new Scanner(System.in);
        boolean ok = true;
        while (ok) {
            printMenu();
            String input = scan.nextLine();
            switch (input) {
                case "1":
                    addUser();
                    printUsers();
                    break;
                case "2":
                    removeUser();
                    printUsers();
                    break;
                case "3":
                    addFriendship();
                    printFriendships();
                    break;
                case "4":
                    removeFriendship();
                    printFriendships();
                    break;
                case "5":
                    printUsers();
                    break;
                case "6":
                    printFriendships();
                    break;
                case "7":
                    printConnectedCommunities();
                    break;
                case "8":
                    printMostSocialCommunity();
                    break;
                case "0":
                    System.out.println("exit");
                    ok = false;
                    break;
                default:
                    System.out.println("Invalid input!");
                    break;
            }
        }
    }

    void printUsers() {
        System.out.println("\t\t\tUSERS\t\t\t");
        for (Utilizator u : service.getUsers()) {
            System.out.println("ID: " + u.getId() + " " + u.getFirstName() + " " + u.getLastName());
        }
    }

    void addUser() {
        System.out.println("Add user");
        Scanner scan = new Scanner(System.in);
        System.out.println("First name: ");
        String firstName = scan.nextLine();
        System.out.println("Last name: ");
        String lastName = scan.nextLine();
        System.out.println("Password: ");
        String password = scan.nextLine();
        try {
            service.addUtilizator(new Utilizator(firstName, lastName, password));
        } catch (ValidationException e) {
            System.out.println("\033[0;31m" + "Invalid user!" + "\033[0m");
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid argument");
        }
    }

    void removeUser() {
        //printUsers();
        System.out.println("Remove user");
        Scanner scan = new Scanner(System.in);
        System.out.println("Id: ");
        String var = scan.nextLine();
        try {
            Long id = Long.parseLong(var);
            Optional<Utilizator> user = service.getUser(id);
            if(user.isPresent()) {
                service.removeUtilizator(id);
                System.out.println("User: " + user.get().getId() + " " + user.get().getFirstName() + " " + user.get().getLastName() + " was removed.");
            }
        } catch (IllegalArgumentException e) {
            System.out.println("ID must be a number! It can't contain letters or symbols! ");
        } catch(ValidationException e){
            System.out.println("Invalid argument");
        }
    }

    void printFriendships() {
        System.out.println("\n\t\t\tFRIENDSHIPS\t\t\t");

        for (Utilizator u : service.getUsers()) {
            System.out.printf("Friends of user %s %s:\n", u.getFirstName(), u.getLastName());
            List<Utilizator> friends = service.getUserFriends(u.getId());

            if (friends.isEmpty()) {
                System.out.println("  No friends.");
            } else {
                System.out.printf("  Total friends: %d\n", friends.size());
                for (Utilizator friend : friends) {
                    System.out.printf("  - ID: %d, Name: %s %s\n", friend.getId(), friend.getFirstName(), friend.getLastName());
                }
            }
            System.out.println();
        }
    }

    void addFriendship() {
        Scanner scan = new Scanner(System.in);
        System.out.println("ID of the first user: ");
        String var1 = scan.nextLine();
        System.out.println("ID of the second user: ");
        String var2 = scan.nextLine();
        try {
            Long id1 = 0L, id2 = 0L;
            try {
                id1 = Long.parseLong(var1);
                id2 = Long.parseLong(var2);
            } catch (IllegalArgumentException e) {
                System.out.println("ID must be a number! It can't contain letters or symbols! ");
            }
            service.addFriendship(id1, id2);
        } catch (ValidationException | IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    private void removeFriendship() {
        Scanner scan = new Scanner(System.in);
        System.out.println("ID of the first user: ");
        String var1 = scan.nextLine();
        System.out.println("ID of the second user: ");
        String var2 = scan.nextLine();
        try {
            Long id1 = 0L, id2 = 0L;
            try {
                id1 = Long.parseLong(var1);
                id2 = Long.parseLong(var2);
            } catch (IllegalArgumentException e) {
                System.out.println("ID must be a number! It can't contain letters or symbols! ");
            }
            service.removeFriendship(id1, id2);
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid arguments! ");
        } catch (ValidationException e) {
            System.out.println("Friendship is invalid! ");
        }
    }

    private void printConnectedCommunities() {
        System.out.println("\033[0;32m" + "Social Communities\n" + "\033[0m");
        List<List<Utilizator>> communities = serviceCommunity.Communities();
        for(List<Utilizator> community : communities) {
            System.out.println("Comunity:");
            for (Utilizator u : community) {
                System.out.println("ID: " + u.getId() + " " + u.getFirstName() + " " + u.getLastName());
            }
        }
        System.out.println("Number of Social Communities: " + communities.size());
    }

    private void printMostSocialCommunity() {
        System.out.println("Most social community: ");
        List<Utilizator> mostSocialCommunity = serviceCommunity.mostSociableCommunity();
        for(Utilizator u : mostSocialCommunity) {
            System.out.println("ID: " + u.getId() + " " + u.getFirstName() + " " + u.getLastName());
        }
    }


}
