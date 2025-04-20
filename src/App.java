import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import ui.LoginPage;
import ui.SplashScreen;

public class App {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            
            SwingUtilities.invokeLater(() -> {
                SplashScreen splashScreen = new SplashScreen();
                splashScreen.setVisible(true);
                
                new Thread(() -> {
                    try {
                        Thread.sleep(2000);
                        splashScreen.dispose();
                        SwingUtilities.invokeLater(() -> new LoginPage());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }).start();
            });
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Application failed to start: " + e.getMessage());
        }
    }
}