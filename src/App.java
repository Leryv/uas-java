import model.User;
import service.AuthService;
import service.AuthServiceImpl;
import view.MainView;

public class App {
    public static void main(String[] args) {
        MainView view = new MainView();

        view.menuAwal(); // masuk login
    }
}
