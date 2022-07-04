import frame.MotorViewFrame;
import helpers.Koneksi;

public class Main {
    public static void main(String[] args) {
        Koneksi.getConnection();
        MotorViewFrame viewFrame = new MotorViewFrame();
        viewFrame.setVisible(true);
    }
}
