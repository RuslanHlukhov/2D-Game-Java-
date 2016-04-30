import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import javax.swing.JLabel;
import java.awt.Color;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Login extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public JPanel contentPane;
	public JTextField textUser;
	public JLabel Login;

	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Login frame = new Login();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Login() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 300, 150);
		contentPane = new JPanel();
		contentPane.setBackground(Color.BLACK);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		textUser = new JTextField();
		textUser.setBounds(124, 34, 86, 21);
		contentPane.add(textUser);
		textUser.setColumns(10);

	
		Login = new JLabel("Login");
	
		Login.setForeground(Color.WHITE);
		Login.setBounds(54, 34, 60, 21);
		
		contentPane.add(Login);
		
		JButton btnEnter = new JButton("Enter");
		btnEnter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				GamePanel panel = new GamePanel();
				
				new Thread(panel).start();
				
				JFrame startFrame=new JFrame("DiplomGame");
				panel.setPlayerName(textUser.getText());
				
				startFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				
				startFrame.setContentPane(panel);
				startFrame.pack();//Расмер такой же как компоненты
				startFrame.setLocationRelativeTo(null);//Устанавливает позицию по центру (так как нет обьекта)
				startFrame.setVisible(true);
				dispose();
			}
		
			
			@SuppressWarnings("unused")
			public String textUser() {
				// TODO Auto-generated method stub
				return null;
				
			}

			
	
		});
		btnEnter.setBounds(121, 77, 89, 23);
		contentPane.add(btnEnter);
		
	}
	
	
}
