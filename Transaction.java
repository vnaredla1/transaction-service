import java.util.*;

//51.8 -> credit
//51.4 -> cash

//Requirements:
// Bill contains 5 values or items. if it exceeds 5 values throw an error.
// Verify that values should be less than $10
// If it's less than $10 add service charge based on payment type.
// Debt or cash is 5% and Credit is 10%
// Tax is 2%

// Client requirements.
// Don't accept money if the error occurs.
// Multiple payment methods should be accepted for one request.
// Credit card type validation -> Visa or Master accept, else reject.
//Visa credit rewards -> add 2% cashBack to card.
// Round of total? -> true/false
// Do you want to donate -> yes [A:5%, B:10%, C:20%, D:None] -> reduce it based on the option selected


// User requirement.
// Card[optional] -> $100 default cash.
// Cash -> $60.
// Show rewards money if transaction is made with Visa credit.

//TODO: Method splitting -> Core logic, Validation - post & preProcess, Composition - post & preProcess.

public class Transaction {

    double sum = 0;
    int splitCash = 0;
    String paymentMethod;
    boolean isVisaCredit;
    double result;

    //User details
    double userCardBal = 100.00;
    double cashBal = 60.00;

    Scanner scn = new Scanner(System.in);
    Scanner scn1 = new Scanner(System.in);

    public static void main(String[] args) {
        int[] listOfItems = new int[5];
        listOfItems[0] = 5;
        listOfItems[1] = 10;
        listOfItems[2] = 12;
        listOfItems[3] = 3;
        listOfItems[4] = 20;
        Transaction transaction = new Transaction();
        try {
            transaction.preValidation(listOfItems);
            transaction.composition();
        }
        catch(RuntimeException e) {
            System.out.println(e.getMessage());
        }
    }

    void preValidation(int[] listOfItems) {
        System.out.println("User Balance: Card: " + userCardBal + "and cash: " + cashBal + "\n");
        if(listOfItems.length <= 5) {
            System.out.println("Multi select [A: Split, B: Single]");
            String selectType = scn.nextLine();
            if(selectType.equalsIgnoreCase("a")) {
                System.out.println("You have selected Split, please enter the cash amount: ");
                splitCash = scn.nextInt();

                System.out.println("Mention payment method and card type1: ");
                String beta = scn1.nextLine();
                if(beta.equalsIgnoreCase("cash")) {
                    throw new RuntimeException("ERROR: cannot enter cash as payment method!");
                }
                paymentMethod = beta;
            } else {
                System.out.println("Mention payment method and card type2: ");
                String aplha = scn.nextLine();
                paymentMethod = aplha;
            }
            paymentValidation(listOfItems);
        } else {
            System.out.println("ERROR!");
        }
    }

    void paymentValidation(int[] listOfItems) {
        isVisaCredit = paymentMethod.equalsIgnoreCase("credit visa");
        boolean isDebtOrCash = paymentMethod.equalsIgnoreCase("cash") || paymentMethod.equalsIgnoreCase("debt");
        if(isVisaCredit || paymentMethod.equalsIgnoreCase("credit master") || isDebtOrCash) {
            coreLogic(listOfItems);
        } else {
            throw new RuntimeException("ERROR: Card type not valid!");
        }
    }

    void coreLogic(int[] listOfItems) {
        for(int x : listOfItems) {
            double afterService = x + (x * 0.02);
            if(x < 10) {
                if(paymentMethod.startsWith("credit")) {
                    afterService += x * 0.1;
                } else {
                    afterService += x * 0.05;
                }
            }
            sum += afterService;
        }
    }

    void composition() {
        System.out.println("Do you want to round up? [Enter true or false]");
        boolean roundUp = scn.nextBoolean();
        result = roundUp ? Math.ceil(sum) : sum;

        System.out.println("Do you want to donate? [Please enter the option A:5%, B:10%, C:20%, D:None]");
        String donate = scn1.nextLine();
        switch(donate){
            case "A", "a":
                result += result * 0.05;
                break;
            case "B", "b":
                result += result * 0.1;
                break;
            case "C", "c":
                result += result * 0.2;
                break;
            case "D", "d":
                result = result * 1;
                break;
        }
        userBalance();
        System.out.println("Total: " +result);
    }

    void userBalance() {
        if(splitCash != 0) {
            cashBal -= (double)splitCash;
        }
        if(paymentMethod.equalsIgnoreCase("cash")){
            cashBal -= result;
        } else {
            userCardBal -= result;
        }
        if(isVisaCredit) {
            userCardBal += result * 0.02;
        }
        System.out.println("User Balance: Card: " + userCardBal + " and cash: " + cashBal);
    }

}