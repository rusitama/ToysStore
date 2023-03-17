import java.io.*;
import java.util.*;

public class ToyStore {
    private static final String FILENAME = "toys.txt";
    private static final List<Toy> toys = new ArrayList<>();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        readFromFile();
        printToys();

        while (true) {
            System.out.println("Выберите действие:");
            System.out.println("1 - Добавить новую игрушку");
            System.out.println("2 - Изменить вес игрушки");
            System.out.println("3 - Организовать розыгрыш игрушек");
            System.out.println("4 - Выход");

            int choice = scanner.nextInt();
            scanner.nextLine(); // чтение лишнего символа переноса строки

            switch (choice) {
                case 1:
                    addNewToy();
                    break;
                case 2:
                    changeToyWeight();
                    break;
                case 3:
                    playGame();
                    break;
                case 4:
                    saveToFile();
                    return;
                default:
                    System.out.println("Неверный выбор. Попробуйте снова.");
            }
        }
    }

    private static void addNewToy() {
        System.out.println("Введите название новой игрушки:");
        String name = scanner.nextLine();

        System.out.println("Введите количество новой игрушки:");
        int quantity = scanner.nextInt();
        scanner.nextLine(); // чтение лишнего символа переноса строки

        System.out.println("Введите вес новой игрушки (в % от 100):");
        double weight = scanner.nextDouble();
        scanner.nextLine(); // чтение лишнего символа переноса строки

        int id = toys.size() + 1;
        toys.add(new Toy(id, name, quantity, weight));

        System.out.println("Новая игрушка успешно добавлена.");
        printToys();
    }

    private static void changeToyWeight() {
        System.out.println("Введите ID игрушки, для которой нужно изменить вес:");
        int id = scanner.nextInt();
        scanner.nextLine(); // чтение лишнего символа переноса строки

        Toy toy = findToyById(id);
        if (toy == null) {
            System.out.println("Игрушка с таким ID не найдена.");
            return;
        }

        System.out.println("Текущий вес игрушки: " + toy.getWeight());
		System.out.println("Введите новый вес игрушки (в % от 100):");
		double weight = scanner.nextDouble();
		scanner.nextLine(); // чтение лишнего символа переноса строки
		toy.setWeight(weight);

    System.out.println("Вес игрушки успешно изменен.");
    printToys();
}

	private static void playGame() {
		List<Toy> prizes = new ArrayList<>();
		double totalWeight = 0;

		while (true) {
			System.out.println("Введите ID игрушки, которую вы хотите выиграть (0 для завершения):");
			int id = scanner.nextInt();
			scanner.nextLine(); // чтение лишнего символа переноса строки

			if (id == 0) {
				break;
			}

			Toy toy = findToyById(id);
			if (toy == null) {
				System.out.println("Игрушка с таким ID не найдена.");
				continue;
			}

			if (toy.getQuantity() == 0) {
				System.out.println("Игрушка закончилась.");
				continue;
			}

			prizes.add(toy);
			toy.setQuantity(toy.getQuantity() - 1);
			totalWeight += toy.getWeight();
		}

		if (prizes.isEmpty()) {
			System.out.println("Вы не выиграли ни одной игрушки.");
		} else {
			System.out.println("Поздравляем с выигрышем!");
			System.out.println("Вы выиграли следующие игрушки:");
			for (Toy prize : prizes) {
				System.out.println("- " + prize.getName());
			}
			System.out.println("Общий вес игрушек: " + totalWeight + "%");
		}

		printToys();
	}

	private static Toy findToyById(int id) {
		for (Toy toy : toys) {
			if (toy.getId() == id) {
				return toy;
			}
		}
		return null;
	}

	private static void readFromFile() {
		try (Scanner fileScanner = new Scanner(new File(FILENAME))) {
			while (fileScanner.hasNextLine()) {
				String line = fileScanner.nextLine();
				String[] parts = line.split(";");

				int id = Integer.parseInt(parts[0]);
				String name = parts[1];
				int quantity = Integer.parseInt(parts[2]);
				double weight = Double.parseDouble(parts[3]);

				toys.add(new Toy(id, name, quantity, weight));
			}
		} catch (FileNotFoundException e) {
			System.out.println("Файл не найден. Начните с пустого списка игрушек.");
		}
	}

	private static void saveToFile() {
		try (PrintWriter writer = new PrintWriter(new FileWriter(FILENAME))) {
            Locale.setDefault(Locale.US);
			for (Toy toy : toys) {
                //sb.append(toy.getId()).append(",").append(toy.getName()).append(",").append(toy.getQuantity()).append(",").append(toy.getWeight()).append("\n");
				writer.printf("%d;%s;%d;%.2f%n", toy.getId(), toy.getName(), toy.getQuantity(), toy.getWeight());
			}
		} catch (IOException e) {
			System.out.println("Не удалось сохранить список игрушек.");
		}
	}

	private static void printToys() {
		System.out.println("Список игрушек:");
        Locale.setDefault(Locale.US);
		for (Toy toy : toys) {
			System.out.printf("%d;%s;%d;%.2f%n", toy.getId(), toy.getName(), toy.getQuantity(), toy.getWeight());
		}
	}
}