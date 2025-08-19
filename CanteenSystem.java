import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class CanteenSystem {
    static Map<String, String> users = new HashMap<>();
    static java.util.List<String> orders = new ArrayList<>();

    public static void main(String[] args) {
        new LoginWindow();
    }
}

class LoginWindow extends Frame {
    TextField tfUser, tfPass;
    Label lblStatus;

    LoginWindow() {
        setTitle("Login / Signup");
        setSize(350, 200);
        setLayout(new FlowLayout());

        add(new Label("Username:"));
        tfUser = new TextField(15);
        add(tfUser);

        add(new Label("Password:"));
        tfPass = new TextField(15);
        tfPass.setEchoChar('*');
        add(tfPass);

        Button btnLogin = new Button("Login");
        Button btnSignup = new Button("Signup");
        lblStatus = new Label("Please login or signup");
        add(btnLogin);
        add(btnSignup);
        add(lblStatus);

        btnLogin.addActionListener(e -> login());
        btnSignup.addActionListener(e -> signup());
        setVisible(true);
    }

    // Signup method
    private void signup() {
        String user = tfUser.getText();
        String pass = tfPass.getText();
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
        String pass = tfPass.getText();
        if (CanteenSystem.users.containsKey(user) && CanteenSystem.users.get(user).equals(pass)) {
            lblStatus.setText("Login successful!");

            new MenuWindow(user);
        } else {
            lblStatus.setText("Invalid credentials!");
        }
    }
}

class MenuWindow extends Frame {
    Choice foodMenu;
    TextField tfQty;
    Label lblBill;
    TextArea orderList;
    int total = 0;

    MenuWindow(String username) {
        setTitle("Menu - Welcome " + username);
        setSize(400, 400);
        setLayout(new FlowLayout());

        // Menu Items
        foodMenu = new Choice();
        foodMenu.add("Pizza - 150");
        foodMenu.add("Burger - 80");
        foodMenu.add("Pasta - 120");
        foodMenu.add("Sandwich - 60");

        add(new Label("Select Food:"));
        add(foodMenu);

        add(new Label("Quantity:"));
        tfQty = new TextField("1", 5);
        add(tfQty);

        Button btnAdd = new Button("Add to Order");
        add(btnAdd);

        lblBill = new Label("Total: ₹0");
        add(lblBill);

        orderList = new TextArea(10, 30);
        add(orderList);

        Button btnConfirm = new Button("Confirm Order");
        add(btnConfirm);

        btnAdd.addActionListener(e -> addOrder());
        btnConfirm.addActionListener(e -> confirmOrder());
        setVisible(true);
    }

    private void addOrder() {
        try {
            String selected = foodMenu.getSelectedItem();
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

    // Confirm order
    private void confirmOrder() {
        String order = orderList.getText();
        if (order.isEmpty()) {
            lblBill.setText("No items in order!");
            return;
        }
        // Add "Pending" status
        String fullOrder = order + "Status: Pending\n";
        CanteenSystem.orders.add(fullOrder);
        new KitchenDisplayWindow();
    }
}

class KitchenDisplayWindow extends Frame {
    KitchenDisplayWindow() {
        setTitle("Kitchen Orders");
        setSize(300, 300);
        setLayout(new FlowLayout());

        TextArea kitchenArea = new TextArea(10, 25);

        // Display all orders with status
        for (int i = 0; i < CanteenSystem.orders.size(); i++) {
            kitchenArea.append("Order #" + (i + 1) + "\n");
            kitchenArea.append(CanteenSystem.orders.get(i));
            kitchenArea.append("\n---\n");
        }
        add(kitchenArea);
        setVisible(true);
    }
}
