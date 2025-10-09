package com.github.ragudos.kompeter.inventory;

import javax.swing.JFrame;

public class TestInventoryView extends JFrame{
	public TestInventoryView() {
		setTitle("Inventory View Test");
		setSize(800, 600);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		
		InventoryView inventoryView = new InventoryView();
		inventoryView.initialize();
		add(inventoryView);
	}
	
	public static void main(String[] args) {
		java.awt.EventQueue.invokeLater(() -> {
			new TestInventoryView().setVisible(true);
		});
	}
	
}