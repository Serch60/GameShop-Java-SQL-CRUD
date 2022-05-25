package main;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JButton;
import javax.swing.JTextField;

import net.proteanit.sql.DbUtils;

import javax.swing.JTable;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JScrollPane;

public class GameShop {

	private JFrame frame;
	private JTextField txtName;
	private JTextField txtLaunch;
	private JTextField txtPlatform;
	private JTextField txtPrice;
	private JTable table;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GameShop window = new GameShop();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public GameShop() {
		initialize();
		Connect();
		table_load();
	}
	
	
	/**
	 * MySQL Connection.
	 */
	
	Connection con;
	PreparedStatement pst;
	ResultSet rs;
	private JTextField txtSearch;
	private JButton btnUpdate;
	private JButton btnDelete;
	private JLabel lblSearchById;
	private JScrollPane table_1;

	public void Connect() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://localhost/GameShop", "root", "");
		} catch (ClassNotFoundException ex) {
			ex.printStackTrace();
		} catch (SQLException ex) {
			ex.printStackTrace();
		}

	}

	
	// Show data on Table
	
	public void table_load() {
		try {
			pst = con.prepareStatement("SELECT * FROM games");
			rs = pst.executeQuery();
			table.setModel(DbUtils.resultSetToTableModel(rs));
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 682, 326);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBounds(12, 12, 243, 180);
		frame.getContentPane().add(panel);
		panel.setLayout(null);
		
		JLabel lblName = new JLabel("Name:");
		lblName.setBounds(0, 32, 70, 15);
		panel.add(lblName);
		
		JLabel lblLaunchDate = new JLabel("Launch Date:");
		lblLaunchDate.setBounds(0, 71, 95, 15);
		panel.add(lblLaunchDate);
		
		JLabel lblPlatform = new JLabel("Platform:");
		lblPlatform.setBounds(0, 111, 70, 15);
		panel.add(lblPlatform);
		
		JLabel lblPrice = new JLabel("Price:");
		lblPrice.setBounds(0, 153, 70, 15);
		panel.add(lblPrice);
		
		txtName = new JTextField();
		txtName.setBounds(113, 30, 114, 19);
		panel.add(txtName);
		txtName.setColumns(10);
		
		txtLaunch = new JTextField();
		txtLaunch.setBounds(113, 69, 114, 19);
		panel.add(txtLaunch);
		txtLaunch.setColumns(10);
		
		txtPlatform = new JTextField();
		txtPlatform.setBounds(113, 109, 114, 19);
		panel.add(txtPlatform);
		txtPlatform.setColumns(10);
		
		txtPrice = new JTextField();
		txtPrice.setBounds(113, 161, 114, 19);
		panel.add(txtPrice);
		txtPrice.setColumns(10);
		
		JButton btnSave = new JButton("Save");
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				String name, launch, platform, price;
				
				name = txtName.getText();
				launch = txtLaunch.getText();
				platform = txtPlatform.getText();
				price = txtPrice.getText();
				
				try {
					pst = con.prepareStatement("INSERT INTO games (name, launch, platform, price) values (?, ?, ?, ?)");
					pst.setString(1, name);
					pst.setString(2, launch);
					pst.setString(3, platform);
					pst.setString(4, price);
					pst.executeUpdate();
					JOptionPane.showMessageDialog(null, "Game Added!");
					table_load();

					txtName.setText("");
					txtLaunch.setText("");
					txtPlatform.setText("");
					txtPrice.setText("");
					txtName.requestFocus();
				}

				catch (SQLException e1) {
					e1.printStackTrace();
				}
				
			}
		});
		btnSave.setBounds(12, 215, 117, 25);
		frame.getContentPane().add(btnSave);
		
		table_1 = new JScrollPane();
		table_1.setBounds(267, 12, 403, 201);
		frame.getContentPane().add(table_1);
		
		table = new JTable();
		table_1.setViewportView(table);
		
		txtSearch = new JTextField();
		txtSearch.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				
				try {

					String id = txtSearch.getText();

					pst = con.prepareStatement("SELECT name, launch, platform, price FROM games WHERE id = ?");
					pst.setString(1, id);
					ResultSet rs = pst.executeQuery();

					if (rs.next() == true) {

						String name = rs.getString(1);
						String launch = rs.getString(2);
						String platform = rs.getString(3);
						String price = rs.getString(4);

						txtName.setText(name);
						txtLaunch.setText(launch);
						txtPlatform.setText(platform);
						txtPrice.setText(price);

					} else {
						txtName.setText("");
						txtLaunch.setText("");
						txtPlatform.setText("");
						txtPrice.setText("");
					}

				} catch (SQLException ex) {

				}

			}
		});
		txtSearch.setBounds(454, 255, 114, 19);
		frame.getContentPane().add(txtSearch);
		txtSearch.setColumns(10);
		
		btnUpdate = new JButton("Update");
		btnUpdate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				String name, launch, platform, price, id;

				name = txtName.getText();
				launch = txtLaunch.getText();
				platform = txtPlatform.getText();
				price = txtPrice.getText();
				id = txtSearch.getText();
				
				try {
					pst = con.prepareStatement("UPDATE games SET name = ?, launch = ?, platform = ?, price = ? WHERE id = ?");
					pst.setString(1, name);
					pst.setString(2, launch);
					pst.setString(3, platform);
					pst.setString(4, price);
					pst.setString(5, id);
					pst.executeUpdate();
					JOptionPane.showMessageDialog(null, "Game Updated!");
					table_load();

					txtName.setText("");
					txtLaunch.setText("");
					txtPlatform.setText("");
					txtPrice.setText("");
					txtName.requestFocus();
				}

				catch (SQLException e1) {
					e1.printStackTrace();
				}
				
			}
		});
		btnUpdate.setBounds(138, 215, 117, 25);
		frame.getContentPane().add(btnUpdate);
		
		btnDelete = new JButton("Delete");
		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				String id;

				id = txtSearch.getText();

				try {
					pst = con.prepareStatement("DELETE FROM games WHERE id = ?");
					pst.setString(1, id);
					pst.executeUpdate();
					JOptionPane.showMessageDialog(null, "Game Deleted!");
					table_load();

					txtName.setText("");
					txtLaunch.setText("");
					txtPlatform.setText("");
					txtPrice.setText("");
					txtName.requestFocus();
				}

				catch (SQLException e1) {
					e1.printStackTrace();
				}
				
			}
		});
		btnDelete.setBounds(68, 252, 117, 25);
		frame.getContentPane().add(btnDelete);
		
		lblSearchById = new JLabel("Search by ID");
		lblSearchById.setBounds(468, 225, 90, 15);
		frame.getContentPane().add(lblSearchById);
	}
}
