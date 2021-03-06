package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTree;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import control.ClientFrameConfig;
import model.User;

public class Main extends JFrame {
	private User user;//定义一个User属性，用来接收登陆界面给我传过来查询数据库里面的用户对象
	private JPanel contentPane;
	private JLabel lblNewLabel;
	private JLabel lblUsername;
	private JTextArea txtrAboutDescriptions;
	private JPanel panel_1;
	private JPanel panel_2;
	private JScrollPane scrollPane;
	private JTree tree;
	private JPanel panel;
	private JTabbedPane tabbedPane;


	/**
	 * Create the frame.
	 */
	public Main(User user) {
		this.user=user;
		setIconImage(Toolkit.getDefaultToolkit().getImage(Login.class.getResource("/com/sun/java/swing/plaf/windows/icons/Inform.gif")));
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(ClientFrameConfig.loginFrameWidth, ClientFrameConfig.loginFrameHeight);
		setTitle(ClientFrameConfig.loginFrameTitle);
		setLocation(50, 50);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		lblNewLabel = new JLabel();
		lblNewLabel.setBorder(new LineBorder(new Color(128, 128, 128), 1, true));
		lblNewLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().createImage(user.getImagePath()).getScaledInstance(128, 128, Image.SCALE_DEFAULT)));
		lblNewLabel.setBounds(10, 10, 88, 97);
		contentPane.add(lblNewLabel);
		
		lblUsername = new JLabel(user.getNickname());
		lblUsername.setBorder(new LineBorder(new Color(0, 0, 0)));
		lblUsername.setBounds(129, 20, 100, 15);
		contentPane.add(lblUsername);
		
		txtrAboutDescriptions = new JTextArea();
		txtrAboutDescriptions.setEditable(false);
		txtrAboutDescriptions.setBorder(new LineBorder(new Color(0, 0, 0), 1, true));
		txtrAboutDescriptions.setText(user.getSignatrue());
		txtrAboutDescriptions.setLineWrap(true);
		txtrAboutDescriptions.setBounds(127, 52, 140, 49);
		contentPane.add(txtrAboutDescriptions);
		
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(10, 117, 274, 525);
		contentPane.add(tabbedPane);
		
		panel = new JPanel();
		tabbedPane.addTab("好友", null, panel, null);
		panel.setLayout(new BorderLayout(0, 0));
		
		DefaultMutableTreeNode  root=new DefaultMutableTreeNode("root");//定义一个jtree根节点，所有的好友分组和好友都在这个根节点上往上放
		
		Map<String, HashSet<User>>  allFriends=user.getFriends();
		 
		Set<String>  allGroupNames=allFriends.keySet();//获取所有的分组名
		
		for(String groupName:allGroupNames) {
			DefaultMutableTreeNode  group=new DefaultMutableTreeNode(groupName);//构造出每个组名的对应的TreeNode对象
			HashSet<User>  friendsOfGroup=allFriends.get(groupName);
			for(User u:friendsOfGroup) {
				DefaultMutableTreeNode  friend=new DefaultMutableTreeNode(u.getNickname());
				group.add(friend);
			}
			
			root.add(group);
		}
 		
		
		
		
		tree = new JTree(root);
		tree.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getButton()==1&&e.getClickCount()==2) {
					TreePath  path=tree.getSelectionPath();
					DefaultMutableTreeNode lastNode=(DefaultMutableTreeNode)path.getLastPathComponent();
					if(lastNode.isLeaf()) {
						//上面是解析用户双击之后判断是不是双击的某一个用户名上的这个Node
						String username=lastNode.toString();
						System.out.println(username);
						Chat   chat=new Chat();
						chat.setVisible(true);
					}
				}
			}
		});
		tree.setRootVisible(false);
		scrollPane= new JScrollPane(tree);
		panel.add(scrollPane, BorderLayout.CENTER);
		
		panel_1 = new JPanel();
		tabbedPane.addTab("群组", null, panel_1, null);
		
		panel_2 = new JPanel();
		tabbedPane.addTab("最近联系人", null, panel_2, null);
	}
}
