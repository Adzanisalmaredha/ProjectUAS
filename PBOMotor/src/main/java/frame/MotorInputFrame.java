package frame;

import helpers.ComboBoxItem;
import helpers.Koneksi;

import javax.swing.*;
import java.sql.*;

public class MotorInputFrame extends JFrame {
    private JTextField idTextField;
    private JTextField namaTextField;
    private JButton simpanButton;
    private JButton batalButton;
    private JPanel buttonPanel;
    private JPanel mainPanel;
    private JComboBox merkCB;
    private JComboBox classCB;

    private int id;

    public void setId(int id) {
        this.id = id;
    }

    public MotorInputFrame() {
        simpanButton.addActionListener(e -> {
            String nama = namaTextField.getText();
            ComboBoxItem merk = (ComboBoxItem) merkCB.getSelectedItem();
            ComboBoxItem cls = (ComboBoxItem) classCB.getSelectedItem();
            int id_merk = merk.getValue();
            int id_class = cls.getValue();
//            String cls = classCB.getSelectedItem().toString();
//            String mrk = merkCB.getSelectedItem().toString();
//            int id_merk;
//            int id_class;
//
//            if(mrk == "Asus"){
//                id_merk = 1;
//            } else if(mrk == "Suzuki"){
//                id_merk = 2;
//            } else if(mrk == "Honda"){
//                id_merk = 3;
//            } else if(mrk == "Yamaha"){
//                id_merk = 4;
//            } else {
//                id_merk = 5;
//            }
//            if(cls == "Ultrabook"){
//                id_class = 1;
//            } else if(cls == "Sport"){
//                id_class = 2;
//            } else if(cls == "Cub"){
//                id_class = 3;
//            } else {
//                id_class = 4;
//            }
            if(nama.equals("")){
                JOptionPane.showMessageDialog(
                        null,
                        "Lengkapi Nama Motor",
                        "Validasi data kosong",
                        JOptionPane.WARNING_MESSAGE
                );
                namaTextField.requestFocus();
                return;
            }

            Connection c = Koneksi.getConnection();
            PreparedStatement ps;
            try {
                if (this.id == 0) {
                    String cekSQL = "SELECT * FROM toko_motor WHERE nama = ?";
                    ps = c.prepareStatement(cekSQL);
                    ps.setString(1, nama);
                    ResultSet rs = ps.executeQuery();
                    while (rs.next()){
                        JOptionPane.showMessageDialog(
                                null,
                                "Motor Sudah Ada",
                                "Validasi data sama",
                                JOptionPane.WARNING_MESSAGE
                        );
                        return;
                    }
                    String insertSQL = "INSERT INTO toko_motor SET nama = ?, id_merk = ?, id_class = ?";
                    ps = c.prepareStatement(insertSQL);
                    ps.setString(1, nama);
                    ps.setInt(2, id_merk);
                    ps.setInt(3, id_class);
                    ps.executeUpdate();
                    dispose();
                } else {
                    String cekSQL = "SELECT * FROM toko_motor WHERE nama=? AND id!=?";
                    ps = c.prepareStatement(cekSQL);
                    ps.setString(1, nama);
                    ps.setInt(2, id);
                    ResultSet rs = ps.executeQuery();
                    while (rs.next()){
                        JOptionPane.showMessageDialog(
                                null,
                                "Motor Sudah Ada",
                                "Validasi data sama",
                                JOptionPane.WARNING_MESSAGE
                        );
                        return;
                    }

                    String updateSQL = "UPDATE toko_motor SET nama=?, id_class=?, id_merk=? WHERE id=?";
                    ps = c.prepareStatement(updateSQL);
                    ps.setString(1, nama);
                    ps.setInt(2, id_class);
                    ps.setInt(3, id_merk);
                    ps.setInt(4, id);
                    ps.executeUpdate();
                    dispose();
                }
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }

        });
        batalButton.addActionListener(e -> {
            dispose();
        });
        merkcb();
        classcb();
        init();
    }

    public void init() {
        setTitle("Input Data");
        setContentPane(mainPanel);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        pack();
    }

    public void isiKomponen() {
        idTextField.setText(String.valueOf(this.id));

        String findSQL = "SELECT * FROM toko_motor WHERE id = ?";

        Connection c = Koneksi.getConnection();
        PreparedStatement ps;
        try {
            ps = c.prepareStatement(findSQL);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                namaTextField.setText(rs.getString("nama"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void merkcb() {
        Connection c = Koneksi.getConnection();
        String selectSQL = "SELECT * FROM merk ORDER by merk";

        Statement s = null;
        try {
            s = c.createStatement();
            ResultSet rs = s.executeQuery(selectSQL);
            merkCB.addItem(new ComboBoxItem(0, "Pilih Merk"));
            while (rs.next()){
                merkCB.addItem(new ComboBoxItem(
                        rs.getInt("id"),
                        rs.getString("merk")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }
    public void classcb() {
        Connection c = Koneksi.getConnection();
        String selectSQL = "SELECT * FROM class ORDER by jenis_motor";

        Statement s = null;
        try {
            s = c.createStatement();
            ResultSet rs = s.executeQuery(selectSQL);
            classCB.addItem(new ComboBoxItem(0, "Pilih Class"));
            while (rs.next()){
                classCB.addItem(new ComboBoxItem(
                        rs.getInt("id"),
                        rs.getString("jenis_motor")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }
}
