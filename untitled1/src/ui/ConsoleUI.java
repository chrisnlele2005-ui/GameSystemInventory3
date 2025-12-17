package ui;

import model.Armor;
import model.Consumable;
import model.Item;
import model.Weapon;
import services.InventoryService;
import services.SearchService;

import java.util.List;
import java.util.Scanner;

public class ConsoleUI {

    private InventoryService inventoryService;
    private SearchService searchService;
    private Scanner scanner;

    public ConsoleUI() {
        inventoryService = new InventoryService();
        searchService = new SearchService();
        scanner = new Scanner(System.in);
    }

    public void start() {
        boolean running = true;
        while (running) {
            System.out.println("\n--- Inventory Menu ---");
            System.out.println("1) Vis inventory");
            System.out.println("2) Tilføj item");
            System.out.println("3) Slet item");
            System.out.println("4) Søg item efter navn");
            System.out.println("5) Gem til DB");
            System.out.println("6) Load fra DB");
            System.out.println("7) Afslut");
            System.out.print("Vælg: ");

            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1":
                    listItems();
                    break;
                case "2":
                    addItem();
                    break;
                case "3":
                    removeItem();
                    break;
                case "4":
                    searchItem();
                    break;
                case "5":
                    inventoryService.saveToDB();
                    System.out.println("Gemte inventory til DB.");
                    break;
                case "6":
                    inventoryService.loadFromDB();
                    System.out.println("Loaded fra DB.");
                    break;
                case "7":
                    running = false;
                    System.out.println("Afslutter programmet...");
                    break;
                default:
                    System.out.println("Ugyldigt valg. Prøv igen.");
            }
        }
    }

    private void listItems() {
        List<Item> items = inventoryService.getInventory();
        if (items.isEmpty()) {
            System.out.println("Inventory er tom.");
            return;
        }
        System.out.println("\n--- Inventory ---");
        for (Item i : items) {
            System.out.println(i);
        }
    }

    private void addItem() {
        try {
            System.out.print("ID (int): ");
            int id = Integer.parseInt(scanner.nextLine().trim());
            System.out.print("Navn: ");
            String name = scanner.nextLine().trim();
            System.out.print("Category (Weapon/Armor/Consumable): ");
            String category = scanner.nextLine().trim();
            System.out.print("Vægt (double): ");
            double weight = Double.parseDouble(scanner.nextLine().trim());
            System.out.print("Antal (int): ");
            int qty = Integer.parseInt(scanner.nextLine().trim());

            if ("Weapon".equalsIgnoreCase(category)) {
                System.out.print("Weapon type (f.eks One-Handed): ");
                String wtype = scanner.nextLine().trim();
                System.out.print("Damage (int): ");
                int dmg = Integer.parseInt(scanner.nextLine().trim());
                Weapon w = new Weapon(id, name, weight, qty, wtype, dmg);
                inventoryService.addItem(w);
            } else if ("Armor".equalsIgnoreCase(category)) {
                System.out.print("Armor type (f.eks Helmet): ");
                String atype = scanner.nextLine().trim();
                System.out.print("Defense (int): ");
                int def = Integer.parseInt(scanner.nextLine().trim());
                Armor a = new Armor(id, name, weight, qty, atype, def);
                inventoryService.addItem(a);
            } else if ("Consumable".equalsIgnoreCase(category)) {
                System.out.print("Effect (tekst): ");
                String eff = scanner.nextLine().trim();
                System.out.print("Effect value (int): ");
                int val = Integer.parseInt(scanner.nextLine().trim());
                Consumable c = new Consumable(id, name, weight, qty, eff, val);
                inventoryService.addItem(c);
            } else {
                System.out.println("Ugyldig kategori — bruger kun Weapon/Armor/Consumable.");
            }

        } catch (NumberFormatException e) {
            System.out.println("Fejl i nummerformat. Forsøg igen.");
        }
    }

    private void removeItem() {
        try {
            System.out.print("ID på item der skal slettes: ");
            int id = Integer.parseInt(scanner.nextLine().trim());
            boolean removed = inventoryService.removeById(id);
            if (removed) {
                System.out.println("Item fjernet.");
            } else {
                System.out.println("Item ikke fundet i inventory.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Ugyldigt ID.");
        }
    }

    private void searchItem() {
        System.out.print("Skriv navn at søge efter: ");
        String name = scanner.nextLine();
        List<Item> results = searchService.searchByName(inventoryService.getInventory(), name);
        if (results.isEmpty()) {
            System.out.println("Ingen items fundet med det navn.");
        } else {
            System.out.println("\n--- Resultater ---");
            for (Item item : results) {
                System.out.println(item);
            }
        }
    }
}

