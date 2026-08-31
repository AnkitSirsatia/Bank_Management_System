
import Dashboard.Dashboard;
import Service.BankService;

void main() {
    Scanner scanner = new Scanner(System.in);
    BankService bankService = new BankService(scanner);
    Dashboard dashboard = new Dashboard(bankService,scanner);
    try {
        while (true) {
            System.out.println("Press 1 : Login\nPress 2 : Create Account\nPress 3 : Logout");
            int choice = scanner.nextInt();
            scanner.nextLine();
            switch (choice) {
                case 1:
                    if (dashboard.login()) {
                        dashboard.showMenu();
                    }
                    break;
                case 2:
                    dashboard.createAccount();
                    break;
                case 3:
                    System.out.println("Exiting...");
                    return;
                default:
                    System.out.println("Enter valid choice");
            }
        }
    }catch (Exception e){
        System.out.println("Ops! error");
    }

}
