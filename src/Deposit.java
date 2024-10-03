import java.time.Year;
import java.util.Objects;

interface OilFieldOperations {
    void extractOil(double amount);
    double calculateIncome();
}

abstract class OilField {
    protected static final double TAX_RATE = 0.15;

    public abstract void extractOil(double amount);

    public double calculateIncome(double tonCost, double extractedOil) {
        return (tonCost * extractedOil) * (1 - TAX_RATE);
    }
}

public class Deposit extends OilField implements OilFieldOperations, Comparable<Deposit>, Cloneable {
    private String name;
    private int discoveryYear;
    private double tonCost;
    private double extractedOil;
    private static int instanceCount = 0;
    private final int instanceNumber;
    private String errors;
    private OilType oilType;  // Додаємо поле для enum

    public enum OilType {
        CRUDE, REFINED, SHALE, HEAVY, LIGHT
    }

    public Deposit() {
        this("Unknown", 2000, 200.0, 0.0, OilType.CRUDE);
    }

    public Deposit(String name, int discoveryYear, double tonCost, double extractedOil, OilType oilType) {
        this.name = name;
        this.errors = "";
        setDiscoveryYear(discoveryYear);
        setExtractedOil(extractedOil);
        this.tonCost = tonCost;
        this.oilType = oilType;
        instanceNumber = ++instanceCount;
    }

    // Реалізація абстрактного методу
    @Override
    public void extractOil(double amount) {
        if (amount > 0) {
            extractedOil += amount;
        } else {
            errors += "Extraction amount is incorrect!\n";
        }
    }

    // Реалізація методу інтерфейсу
    @Override
    public double calculateIncome() {
        return super.calculateIncome(tonCost, extractedOil);
    }

    // Гетери
    public String getName() {
        return name;
    }

    public int getDiscoveryYear() {
        return discoveryYear;
    }

    public double getTonCost() {
        return tonCost;
    }

    public double getExtractedOil() {
        return extractedOil;
    }

    // Сетери
    public void setName(String name) {
        this.name = name;
    }

    public void setDiscoveryYear(int discoveryYear) {
        int currentYear = Year.now().getValue();
        if(discoveryYear >= 1800 && discoveryYear <= currentYear){
            this.discoveryYear = discoveryYear;
        } else {
            errors += "Discovery year: " + discoveryYear + " is incorrect!\n";
        }
    }

    public void setTonCost(double tonCost) {
        this.tonCost = tonCost;
    }

    public void setExtractedOil(double extractedOil) {
        if (extractedOil >= 0) {
            this.extractedOil = extractedOil;
        } else {
            errors += "Extracted Oil " + extractedOil + " is incorrect for deposit!\n";
        }
    }

    // Перевизначення equals, hashCode і toString
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Deposit deposit = (Deposit) o;
        return discoveryYear == deposit.discoveryYear &&
                Double.compare(deposit.tonCost, tonCost) == 0 &&
                Double.compare(deposit.extractedOil, extractedOil) == 0 &&
                name.equals(deposit.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, discoveryYear, tonCost, extractedOil);
    }

    @Override
    public String toString() {
        return "Deposit{name='" + name + "', discoveryYear=" + discoveryYear +
                ", tonCost=" + tonCost + ", extractedOil=" + extractedOil +
                ", oilType=" + oilType + "}";
    }

    // Перевизначення методу clone
    @Override
    public Deposit clone() {
        try {
            return (Deposit) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    // Реалізація compareTo для сортування
    @Override
    public int compareTo(Deposit o) {
        return Double.compare(this.extractedOil, o.extractedOil);
    }

    // Метод для виведення інформації
    public void Show() {
        System.out.println("Instance Number: " + instanceNumber);
        System.out.println("Name: " + name);
        System.out.println("Discovery Year: " + discoveryYear);
        System.out.println("Cost of one ton(UAH): " + tonCost);
        System.out.println("Extracted Oil(Ton): " + extractedOil);
        System.out.println("---------------------------");
    }

    // Метод для виведення помилок
    public void ShowErrors() {
        if (!errors.isEmpty()) {
            System.out.println("Errors for Instance " + instanceNumber + ":");
            System.out.print(errors);
        }
    }

    public static void main(String[] args) {
        // Створюємо декілька екземплярів класу
        Deposit d1 = new Deposit("Alpha", 1889, 120.5, 1000.50, OilType.CRUDE);
        Deposit d2 = new Deposit("Beta", 2006, 130.1, 300.2, OilType.SHALE);
        Deposit d3 = new Deposit("Omega", 1006, 130.1, 500.0, OilType.REFINED);

        // Використання анонімного класу
        OilField anonymousField = new OilField() {
            @Override
            public void extractOil(double amount) {
                System.out.println("Anonymous extraction!");
            }
        };
        anonymousField.extractOil(50);
        System.out.println("Total Instances: " + instanceCount);

        // Виведення інформації про екземпляри
        System.out.println(d1);
        System.out.println(d2);

        // Порівняння двох об'єктів
        System.out.println("d1 equals d2: " + d1.equals(d2));

        // Клонування об'єкта
        Deposit clonedDeposit = d1.clone();
        System.out.println("Cloned: " + clonedDeposit);
    }
}
