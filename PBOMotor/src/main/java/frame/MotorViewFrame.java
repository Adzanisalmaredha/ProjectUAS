package frame;

import helpers.Koneksi;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.*;

public class MotorViewFrame extends JFrame{
    private JPanel mainPanel;
    private JPanel cariPanel;
    private JTextField cariTextField;
    private JButton cariButton;
    private JTable viewTable;
    private JButton tambahButton;
    private JButton ubahButton;
    private JButton hapusButton;
    private JButton batalButton;
    private JButton cetakButton;
    private JButton tutupButton;

    public MotorViewFrame(){
        tutupButton.addActionListener(e -> {
            dispose();
        });
        batalButton.addActionListener(e -> {
            isiTable();
        });
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowActivated(WindowEvent e) {
                isiTable();
            }
        });
        hapusButton.addActionListener(e->{
            int barisTerpilih = viewTable.getSelectedRow();
            if(barisTerpilih<0){
                JOptionPane.showMessageDialog(
                        null,
                        "Pilih dulu datanya",
                        "Validasi pilih data",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            int pilihan = JOptionPane.showConfirmDialog(
                    null,
                    "Yakin?",
                    "Konfirmasi hapus data",
                    JOptionPane.YES_NO_OPTION
            );

            if (pilihan == 0 ){
                TableModel tm = viewTable.getModel();
                String idString = tm.getValueAt(barisTerpilih,0).toString();
                int id = Integer.parseInt(idString);

                String deleteSQL = "DELETE FROM toko_motor WHERE id = ?";
                Connection c = Koneksi.getConnection();
                PreparedStatement ps;
                try {
                    ps = c.prepareStatement(deleteSQL);
                    ps.setInt(1,id);
                    ps.executeUpdate();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }

            }
        });
        cariButton.addActionListener(e -> {
            String keyword = "%" + cariTextField.getText() + "%";
            String keyword1 = "%" + cariTextField.getText() + "%";
            String keyword2 = "%" + cariTextField.getText() + "%";
            String searchSQL = "SELECT toko_motor.id, merk.merk, toko_motor.nama, class.jenis_motor " +
                    "FROM ((toko_motor INNER JOIN merk ON toko_motor.id_merk = merk.id) " +
                    "INNER JOIN class ON toko_motor.id_class = class.id) " +
                    "WHERE merk like ? OR nama like ? OR jenis_motor like ? ";

            Connection c = Koneksi.getConnection();
            try {
                PreparedStatement ps = c.prepareStatement(searchSQL);
                ps.setString(1, keyword);
                ps.setString(2, keyword1);
                ps.setString(3, keyword2);
                ResultSet rs = ps.executeQuery();
                DefaultTableModel dtm = (DefaultTableModel) viewTable.getModel();
                dtm.setRowCount(0);
                Object[] row = new Object[4];
                while (rs.next()){
                    row[0] = rs.getInt("id");
                    row[1] = rs.getString("merk");
                    row[2] = rs.getString("nama");
                    row[3] = rs.getString("jenis_motor");
                    dtm.addRow(row);
                }
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }

        });
        tambahButton.addActionListener(e->{
            MotorInputFrame inputFrame = new MotorInputFrame();
            inputFrame.setVisible(true);
        });
        ubahButton.addActionListener(e->{
            int barisTerpilih = viewTable.getSelectedRow();
            if(barisTerpilih<0){
                JOptionPane.showMessageDialog(
                        null,
                        "Pilih dulu datanya",
                        "Validasi pilih data",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            TableModel tm = viewTable.getModel();
            String idString = tm.getValueAt(barisTerpilih,0).toString();
            int id = Integer.parseInt(idString);

            MotorInputFrame inputFrame = new MotorInputFrame();
            inputFrame.setId(id);
            inputFrame.isiKomponen();
            inputFrame.setVisible(true);
        });
        isiTable();
        init();
    }

    public void init(){
        setTitle("Data Motor");
        setContentPane(mainPanel);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        pack();
    }

    public void isiTable(){
        String selectSQL = "SELECT toko_motor.id, merk.merk, toko_motor.nama, class.jenis_motor " +
                "FROM ((toko_motor INNER JOIN merk ON toko_motor.id_merk = merk.id) " +
                "INNER JOIN class ON toko_motor.id_class = class.id)";
        Connection c = Koneksi.getConnection();
        try {
            Statement s = c.createStatement();
            ResultSet rs = s.executeQuery(selectSQL);

            String header[] = {"Id","Merk","Nama Motor","Jenis"};
            DefaultTableModel dtm = new DefaultTableModel(header,0);
            viewTable.setModel(dtm);

            viewTable.getColumnModel().getColumn(0).setPreferredWidth(32);
            viewTable.getColumnModel().getColumn(0).setMinWidth(32);
            viewTable.getColumnModel().getColumn(0).setMaxWidth(32);

            Object[] row = new Object[4];
            while (rs.next()){
                row[0] = rs.getInt("id");
                row[1] = rs.getString("merk");
                row[2] = rs.getString("nama");
                row[3] = rs.getString("jenis_motor");
                dtm.addRow(row);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
