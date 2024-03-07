package imswithfilehandling;

import java.io.RandomAccessFile;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Random;
import java.util.ArrayList;

public class Inventory {
    private final Scanner sc = new Scanner(System.in);
    private static final int POSITION_OF_ITEM_ID_IN_LINE = 8;
    private static final int POSITION_OF_ITEM_NAME_IN_LINE = 10;
    private static final int POSITION_OF_ITEM_CATEGORY_IN_LINE = 14;
    private static final int POSITION_OF_ITEM_COUNT_IN_LINE = 11;
    private static RandomAccessFile file;
    private static final String FILE_NAME = "records.txt";


    public static void main(String[] ajiMeraKaddu) {
        try {
            file = new RandomAccessFile(FILE_NAME, "rw");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        Inventory inventory = new Inventory();
        System.out.println("========================Main Menu========================");
        System.out.println("1. Add an item in the inventory.");
        System.out.println("2. Increase the count value of an item");
        System.out.println("3. Decrease the count value of an item");
        System.out.println("4. Delete the item from the inventory.");
        System.out.println("5. Update the existing item of the inventory");
        System.out.println("6. Display all the items of the inventory.");
        System.out.println("7. Search the item in the inventory by name and category.");
        System.out.println("8. Exit the program.");
        System.out.print("Enter your choice: ");
        int choice = inventory.sc.nextInt();
        inventory.sc.nextLine(); //To consume(Flush) the remaining newline characters
        while (true) {
            String id;
            int posi;
            switch (choice) {
                case 1:
                    try {
                        inventory.addItem();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    System.out.println("Item added successfully....👍");
                    break;

                case 2:
                    try {
                        if (file.length() < 5) {  //just for safe side we are checking <5 because if file is empty and have only new line characters then length of file may be more than 0(like 1 or 2...)
                            System.out.println("There is no item in the inventory so can't perform increment, Please add some...🙏");
                        } else {
                            inventory.displayItems();
                            System.out.print("\nEnter the id of item to increment the count: ");
                            id = inventory.sc.next();
                            posi = searchItemByIdInFile(id);
                            if (posi == -1) {
                                System.out.println("⚠️⚠️ Check your Id, Item not found with this Id ⚠️⚠️. Please try again...");
                                break;
                            } else {
                                System.out.print("Enter the count value: ");
                                int countValue = inventory.sc.nextInt();
                                while (countValue <= 0) {
                                    System.out.print("🙏Please provide me the positive integer as a count value: ");
                                    countValue = inventory.sc.nextInt();
                                }
                                inventory.sc.nextLine(); //For Flush
                                inventory.incItemCount(posi, countValue);
                                System.out.println("Increment of item count done successfully....👍");
                            }
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    break;

                case 3:
                    try {
                        if (file.length() < 5) {  //just for safe side we are checking <5 because if file is empty and have only new line characters then length of file may be more than 0(like 1 or 2...)
                            System.out.println("There is no item in the inventory so can't perform decrement, Please add some...🙏");
                        } else {
                            inventory.displayItems();
                            System.out.print("\nEnter the id of item to decrement the count: ");
                            id = inventory.sc.next();
                            posi = searchItemByIdInFile(id);
                            if (posi == -1) {
                                System.out.println("⚠️⚠️ Check your Id, Item not found with this Id ⚠️⚠️. Please try again...");
                                break;
                            } else {
                                System.out.print("Enter the count value: ");
                                int countValue = inventory.sc.nextInt();
                                while (countValue <= 0) {
                                    System.out.print("🙏Please provide me the positive integer as a count value: ");
                                    countValue = inventory.sc.nextInt();
                                }
                                inventory.sc.nextLine();  //For Flush
                                inventory.decItemCount(posi, countValue);
                                System.out.println("Decrement of item count done successfully....👍");
                            }
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    break;

                case 4:
                    try {
                        if (file.length() < 5) {  //just for safe side we are checking <5 because if file is empty and have only new line characters then length of file may be more than 0(like 1 or 2...)
                            System.out.println("There is no item in the inventory so can't perform delete, Please add some...🙏");
                        } else {
                            inventory.displayItems();
                            System.out.print("Enter the id of item you want to delete: ");
                            id = inventory.sc.nextLine();
                            posi = searchItemByIdInFile(id);
                            inventory.deleteItem(posi);
                            System.out.println("Your item has been deleted successfully from the inventory... 🗑️");
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    break;

                case 5:
                    try {
                        inventory.updateItem();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    break;

                case 6:
                    try {
                        inventory.displayItems();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    System.out.println("\t\t\t\t\t\t\tDisplaying done successfully...👍");
                    break;

                case 7:
                    System.out.print("Enter the name of the item: ");
                    String name = inventory.sc.nextLine();
                    System.out.print("Enter the category of the item: ");
                    String category = inventory.sc.nextLine();
                    int index;
                    try {
                        index = inventory.searchItemByNameAndCategoryInFile(name, category);
                        if (index == -1) {
                            System.out.println("Oopss🙈, No such item found in the inventory...");
                        } else {
                            file.seek(index);
                            file.skipBytes(POSITION_OF_ITEM_ID_IN_LINE);
                            id = file.readLine();
                            System.out.println("Hurray👏, item found at " + index + " location, and the itemID is: " + id);
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                case 8:
                    System.out.println("...Exiting the program...\n🙋‍♀️Bye Bye🙋‍, Have a great time...");
                    inventory.sc.close();
                    try {
                        file.close();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    System.exit(0);
                default:
                    System.out.println("Oopss⚠️, Invalid choice. Please try again.");
                    break;
            }
            System.out.println("Press Enter continue...");
            inventory.sc.nextLine();
            System.out.println("========================Main Menu========================");
            System.out.println("1. Add an item in the inventory.");
            System.out.println("2. Increase the count value of an item");
            System.out.println("3. Decrease the count value of an item");
            System.out.println("4. Delete the item from the inventory.");
            System.out.println("5. Update the existing item of the inventory");
            System.out.println("6. Display all the items of the inventory.");
            System.out.println("7. Search the item in the inventory by name and category.");
            System.out.println("8. Exit the program.");
            System.out.print("Enter your choice: ");
            choice = inventory.sc.nextInt();
            inventory.sc.nextLine();
        }
    }

    private void addItem() throws IOException {
        String id = generateId();
        while (isIdAlreadyGiven(id)) {
            id = generateId();
            sc.nextLine();  //To consume(Flush) the remaining newline characters
        }
        System.out.print("Enter item name: ");
        String name = sc.nextLine();
        System.out.print("Enter item category: ");
        String category = sc.nextLine();
        int positionToSeek = searchItemByNameAndCategoryInFile(name, category);
        String itemToString;
        if (positionToSeek == -1) {   //-1 means item not found in file, so we have to write/append the item into the file
            itemToString = "ItemId: " + id.substring(7) + "\n"  //for this 7 look at the generateId method
                    + "ItemName: " + name + "\n"
                    + "ItemCategory: " + category + "\n"
                    + "ItemCount: " + 1 + "\n";
            file.seek(file.length());  //go to at the end of the file
            file.write(itemToString.getBytes());  //write at the end of the file
        } else {  //means item found in file
            incItemCount(positionToSeek, 1);
        }
    }

    private String generateId() {
        Random random = new Random();
        String alphabets = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        String numbers = "0123456789";

        return "ItemID#" +
                alphabets.charAt(random.nextInt(52)) +
                alphabets.charAt(random.nextInt(52)) +
                alphabets.charAt(random.nextInt(52)) +
                numbers.charAt(random.nextInt(10)) +
                numbers.charAt(random.nextInt(10)) +
                numbers.charAt(random.nextInt(10));
    }

    private boolean isIdAlreadyGiven(String id) throws IOException {
        ArrayList<String> listOfIds = getAllIdsFromFile();

        for (String idFromFile : listOfIds) {
            if (idFromFile.equals(id.substring(7))) {
                return true;
            }
        }
        return false;
    }


    private void incItemCount(int positionToSeek, int countValue) throws IOException {
        file.seek(positionToSeek);  //go/seek back to the position of item in file
        file.readLine(); //skip the id line
        file.readLine(); //skip the name line
        file.readLine(); //skip the category line
        file.skipBytes(POSITION_OF_ITEM_COUNT_IN_LINE);  //skip the extra characters in the count value line to reach till count value
        long fromPosition = file.getFilePointer();
        String countValueFromFile = file.readLine().trim();
        long toPosition = file.getFilePointer() - 1;
        int oldCount = Integer.parseInt(countValueFromFile);
        int newCount = oldCount + countValue;
        replaceCharactersInFile(fromPosition, toPosition, String.valueOf(newCount));
    }

    private void decItemCount(int positionToSeek, int countValue) throws IOException {
        file.seek(positionToSeek);  //go/seek back to the position of item in file
        file.readLine(); //skip the id line
        file.readLine(); //skip the name line
        file.readLine(); //skip the category line
        file.skipBytes(POSITION_OF_ITEM_COUNT_IN_LINE);  //skip the extra characters in the count value line to reach till count value
        long fromPosition = file.getFilePointer();
        String countValueFromFile = file.readLine().trim();
        long toPosition = file.getFilePointer() - 1;
        int oldCount = Integer.parseInt(countValueFromFile);
        int newCount = oldCount - countValue;
        if (newCount <= 0) {
            System.out.print("Count value of this item become zero or less than zero, So ");
            deleteItem(positionToSeek);
        } else {
            replaceCharactersInFile(fromPosition, toPosition, String.valueOf(newCount));
        }
    }

    private void deleteItem(int positionToSeek) throws IOException {
        if (positionToSeek == -1) {
            System.out.println("⚠️⚠️ Check your Id, Item not found with this Id ⚠️⚠️. Please try again...");
        } else {
            file.seek(positionToSeek);
            //to remove the item simply skip the item
            file.readLine();  //id
            file.readLine();  //name
            file.readLine();  //category
            file.readLine();  //count value
            long remainingItemsPosition = file.getFilePointer();
            replaceCharactersInFile(positionToSeek, remainingItemsPosition, "");
        }
    }

    private void updateItem() throws IOException {
        if (file.length() < 5) {  //just for safe side we are checking <5 because if file is empty and have only new line characters then length of file may be more than 0(like 1 or 2...)
            System.out.println("There is no item in the inventory so can't perform update, Please add some...🙏");
        } else {
            displayItems();
            System.out.print("Enter the id of item which you want to update: ");
            String id = sc.next();
            int posi = searchItemByIdInFile(id);
            if (posi == -1) {
                System.out.println("⚠️⚠️ Check your Id, Item not found with this Id ⚠️⚠️. Please try again...");
            } else {
                System.out.println("1. Name");
                System.out.println("2. Category");
                System.out.println("3. Count");
                System.out.print("What would you like to update? : ");
                int choice = sc.nextInt();
                sc.nextLine();  //For Flush the remaining newline characters
                switch (choice) {
                    case 1 -> {
                        System.out.print("Enter new name: ");
                        String newName = sc.nextLine().trim();

                        file.seek(posi);
                        file.readLine(); //skip id
                        file.skipBytes(POSITION_OF_ITEM_NAME_IN_LINE);
                        long fromPosition = file.getFilePointer();  //to get the starting position of the name to replace it with new name
                        file.readLine(); //skip name
                        long toPosition = file.getFilePointer() - 1;  //to get the ending position of the name to replace it with new name
                        file.skipBytes(POSITION_OF_ITEM_CATEGORY_IN_LINE);
                        String category = file.readLine().trim();
                        int positionOfDuplicate = searchItemByNameAndCategoryInFile(newName, category);
                        if (positionOfDuplicate == -1) {  //No duplicate item found simply update the name of item
                            replaceCharactersInFile(fromPosition, toPosition, newName);
                            System.out.println("Good job, Your item has been updated successfully in the inventory...👍");
                        } else {
                            file.seek(posi);
                            file.readLine();//skip id
                            file.skipBytes(POSITION_OF_ITEM_NAME_IN_LINE);
                            String name = file.readLine().trim();

                            if (!name.equalsIgnoreCase(newName)) {
                                System.out.println("Item matched with an existing item, So we have incremented the count of the existing item.");
                                incItemCount(positionOfDuplicate, 1);
                                deleteItem(posi);
                            } else {
                                replaceCharactersInFile(fromPosition, toPosition, newName);
                                System.out.println("Good job, Your item has been updated successfully in the inventory...👍");
                            }
                        }
                    }
                    case 2 -> {
                        System.out.print("Enter new category: ");
                        String newCat = sc.nextLine().trim();

                        file.seek(posi);
                        file.readLine(); //skip id
                        file.skipBytes(POSITION_OF_ITEM_NAME_IN_LINE);
                        String name = file.readLine().trim();
                        file.skipBytes(POSITION_OF_ITEM_CATEGORY_IN_LINE);
                        long fromPosition = file.getFilePointer();  //to get the starting position of the category to replace it with new category
                        file.readLine(); //skip category
                        long toPosition = file.getFilePointer() - 1;  //to get the ending position of the category to replace it with new category
                        int positionOfDuplicate = searchItemByNameAndCategoryInFile(name, newCat);
                        if (positionOfDuplicate == -1) {  //No duplicate item found simply update the name of item
                            replaceCharactersInFile(fromPosition, toPosition, newCat);
                            System.out.println("Good job, Your item has been updated successfully in the inventory...👍");
                        } else {
                            file.seek(posi);
                            file.readLine();  //skip id
                            file.readLine();  //skip name
                            file.skipBytes(POSITION_OF_ITEM_CATEGORY_IN_LINE);
                            String category = file.readLine().trim();

                            if (!category.equalsIgnoreCase(newCat)) {
                                System.out.println("Item matched with an existing item, So we have incremented the count of the existing item.");
                                incItemCount(positionOfDuplicate, 1);
                                deleteItem(posi);
                            } else {
                                replaceCharactersInFile(fromPosition, toPosition, newCat);
                                System.out.println("Good job, Your item has been updated successfully in the inventory...👍");
                            }
                        }
                    }
                    case 3 -> {
                        System.out.print("⚠️You are forcefully changing the count value⚠️, Are you sure you want to update the count value(Y/N): ");
                        char confirm = sc.next().charAt(0);
                        while (confirm != 'n' && confirm != 'N' && confirm != 'y' && confirm != 'Y') {
                            System.out.print("Andhe ho kya bro🙈, Type only (Y/N): ");
                            confirm = sc.next().charAt(0);
                        }
                        if (confirm == 'n' || confirm == 'N') {
                            sc.nextLine(); //Flush
                            return;
                        }
                        System.out.print("Enter new count value: ");
                        int newCount = sc.nextInt();
                        while (newCount <= 0) {
                            System.out.print("🙏Please provide me the positive integer as a count value: ");
                            newCount = sc.nextInt();
                        }
                        sc.nextLine(); //For Flush
                        file.seek(posi);
                        file.readLine(); //skip id
                        file.readLine(); //skip name
                        file.readLine(); //skip category
                        file.skipBytes(POSITION_OF_ITEM_COUNT_IN_LINE);
                        long fromPosition = file.getFilePointer();  //to get the starting position of the count value to replace it with new value
                        file.readLine(); //skip category
                        long toPosition = file.getFilePointer() - 1;  //to get the ending position of the count value to replace it with new value
                        replaceCharactersInFile(fromPosition, toPosition, String.valueOf(newCount));
                        System.out.println("Good job, Your item has been updated successfully in the inventory...👍");
                    }
                    default -> System.out.println("Wrong input! Try again.");
                }
            }
        }
    }

    private void displayItems() throws IOException {
        if (file.length() < 5) {
            System.out.println("\t⚠️⚠️There is no item in the inventory, Please add some...🙏");
        } else {
            System.out.println("\t\t\t\t\t\t\tDisplaying all the items of the inventory...👀");
            file.seek(0);
            String line;
            String header = """
                    +---------------------------+---------------------------+---------------------------+-----------------+
                    |   ItemId                  |   ItemName                |   ItemCategory            |   ItemCount     |
                    +---------------------------+---------------------------+---------------------------+-----------------+""";

            System.out.println(header);
            int count = 0;
            String itemId = "", itemName = "", itemCategory = "", itemCount;

            while ((line = file.readLine()) != null) {
                if (count % 4 == 0) {
                    itemId = line.substring(POSITION_OF_ITEM_ID_IN_LINE);
                    count++;
                } else if (count % 4 == 1) {
                    itemName = line.substring(POSITION_OF_ITEM_NAME_IN_LINE);
                    count++;
                } else if (count % 4 == 2) {
                    itemCategory = line.substring(POSITION_OF_ITEM_CATEGORY_IN_LINE);
                    count++;
                } else if (count % 4 == 3) {
                    itemCount = line.substring(POSITION_OF_ITEM_COUNT_IN_LINE);
                    count++;
                    String itemData = """
                            |\t%-24s|\t%-24s|\t%-24s|\t%-14s|
                            +---------------------------+---------------------------+---------------------------+-----------------+""";
                    System.out.printf((itemData) + "%n", itemId, itemName, itemCategory, itemCount);
                }
            }
        }
    }


    private static int searchItemByIdInFile(String itemId) throws IOException {
        int positionToSeek = 0;
        file.seek(positionToSeek);
        String line;

        while ((line = file.readLine()) != null) {
            if (itemId.trim().equals(line.substring(POSITION_OF_ITEM_ID_IN_LINE).trim())) {
                return positionToSeek;
            }
            positionToSeek = positionToSeek + line.length() + file.readLine().length() + file.readLine().length() + file.readLine().length() + 4; //this 4 is added because we have 4 newlines
        }
        return -1;
    }

    private int searchItemByNameAndCategoryInFile(String name, String category) throws IOException {
        int positionToSeek = 0;
        file.seek(positionToSeek);
        String lineId, lineName, lineCategory;

        while ((lineId = file.readLine()) != null) {
            lineName = file.readLine();
            lineCategory = file.readLine();
            if (name.trim().equalsIgnoreCase(lineName.substring(POSITION_OF_ITEM_NAME_IN_LINE).trim())
                    && category.trim().equalsIgnoreCase(lineCategory.substring(POSITION_OF_ITEM_CATEGORY_IN_LINE).trim())) {
                return positionToSeek;
            }
            positionToSeek = positionToSeek + lineId.length() + lineName.length() + lineCategory.length() + file.readLine().length() + 4;
        }
        return -1;
    }

    private static ArrayList<String> getAllIdsFromFile() throws IOException {
        String line;
        ArrayList<String> ids = new ArrayList<>();
        file.seek(0);
        while ((line = file.readLine()) != null) {
            ids.add(line.substring(POSITION_OF_ITEM_ID_IN_LINE));
            file.readLine();  //skip the name
            file.readLine();  //skip the category
            file.readLine();  //skip the count value
        }
        return ids;
    }

    private static void replaceCharactersInFile(long fromPosition, long toPosition, String newString) throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append(newString);
        String line;
        file.seek(toPosition);
        while ((line = file.readLine()) != null) {
            sb.append(line).append("\n");
        }
        long newLength = fromPosition + sb.length();
        file.setLength(newLength);
        file.seek(fromPosition);
        file.write(sb.toString().getBytes());
    }
}