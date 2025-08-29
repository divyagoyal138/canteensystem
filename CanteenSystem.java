import javax.swing.*;
import java.awt.*;
import java.util.*;

public class CanteenSystem {

    static Map<String, String> users = new HashMap<>();
    static java.util.List<String> orders = new ArrayList<>();

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginWindow());
    }
}

class LoginWindow extends JFrame {
    JTextField tfUser;
    JPasswordField tfPass;
    JLabel lblStatus;

    LoginWindow() {
        setTitle("Login / Signup");
        setSize(350, 200);
        setLayout(new FlowLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        add(new JLabel("Username:"));
        tfUser = new JTextField(15);
        add(tfUser);

        add(new JLabel("Password:"));
        tfPass = new JPasswordField(15);
        add(tfPass);

        JButton btnLogin = new JButton("Login");
        JButton btnSignup = new JButton("Signup");
        lblStatus = new JLabel("Please login or signup");
        add(btnLogin);
        add(btnSignup);
        add(lblStatus);

        btnLogin.addActionListener(e -> login());
        btnSignup.addActionListener(e -> signup());

        setVisible(true);
    }

    private void signup() {
        String user = tfUser.getText();
        String pass = new String(tfPass.getPassword());
        if (user.isEmpty() || pass.isEmpty()) {
            lblStatus.setText("Fields cannot be empty!");
            return;
        }
        if (CanteenSystem.users.containsKey(user)) {
            lblStatus.setText("User already exists!");
        } else {
            CanteenSystem.users.put(user, pass);
            lblStatus.setText("Signup successful! Please login.");
        }
    }

    private void login() {
        String user = tfUser.getText();
        String pass = new String(tfPass.getPassword());
        if (CanteenSystem.users.containsKey(user) && CanteenSystem.users.get(user).equals(pass)) {
            lblStatus.setText("Login successful!");
            dispose();
            new MenuWindow(user);
        } else {
            lblStatus.setText("Invalid credentials!");
        }
    }
}

class MenuWindow extends JFrame {
    JComboBox<String> foodMenu;
    JTextField tfQty;
    JLabel lblBill;
    JTextArea orderList;
    int total = 0;

    MenuWindow(String username) {
        setTitle("Menu - Welcome " + username);
        setSize(400, 400);
        setLayout(new FlowLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        foodMenu = new JComboBox<>();
        foodMenu.addItem("Pizza - 150");
        foodMenu.addItem("Burger - 80");
        foodMenu.addItem("Pasta - 120");
        foodMenu.addItem("Sandwich - 60");

        add(new JLabel("Select Food:"));
        add(foodMenu);

        add(new JLabel("Quantity:"));
        tfQty = new JTextField("1", 5);
        add(tfQty);

        JButton btnAdd = new JButton("Add to Order");
        add(btnAdd);

        lblBill = new JLabel("Total: ₹0");
        add(lblBill);

        orderList = new JTextArea(10, 30);
        orderList.setEditable(false);
        add(new JScrollPane(orderList));

        JButton btnConfirm = new JButton("Confirm Order");
        add(btnConfirm);

        btnAdd.addActionListener(e -> addOrder());
        btnConfirm.addActionListener(e -> confirmOrder());

        setVisible(true);
    }

    private void addOrder() {
        try {
            String selected = (String) foodMenu.getSelectedItem();
            int price = Integer.parseInt(selected.split("-")[1].trim());
            int qty = Integer.parseInt(tfQty.getText());
            int amount = price * qty;
            total += amount;

            orderList.append(selected.split("-")[0].trim() + " x" + qty + " = ₹" + amount + "\n");
            lblBill.setText("Total: ₹" + total);
        } catch (Exception ex) {
            lblBill.setText("Invalid quantity!");
        }
    }

    private void confirmOrder() {
        String order = orderList.getText();
        if (order.isEmpty()) {
            lblBill.setText("No items in order!");
            return;
        }
        String fullOrder = order + "Status: Pending\n";
        CanteenSystem.orders.add(fullOrder);

        dispose();
        new KitchenDisplayWindow();
    }
}

class KitchenDisplayWindow extends JFrame {
    KitchenDisplayWindow() {
        setTitle("Kitchen Orders");
        setSize(300, 300);
        setLayout(new FlowLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JTextArea kitchenArea = new JTextArea(10, 25);
        kitchenArea.setEditable(false);

        for (int i = 0; i < CanteenSystem.orders.size(); i++) {
            kitchenArea.append("Order #" + (i + 1) + "\n");
            kitchenArea.append(CanteenSystem.orders.get(i));
            kitchenArea.append("\n---\n");
        }
        add(new JScrollPane(kitchenArea));

        setVisible(true);
    }
}
