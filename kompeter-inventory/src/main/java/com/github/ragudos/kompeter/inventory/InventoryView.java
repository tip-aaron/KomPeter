package com.github.ragudos.kompeter.inventory;

import java.awt.Color;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import com.github.ragudos.kompeter.utilities.components.Paint;

import net.miginfocom.swing.MigLayout;

public class InventoryView extends JPanel{
	private JPanel table;
	private JPanel header;
	private JScrollPane scrollPane;
	private JPanel titlePnl;
	private JPanel searchPnl;
	private JPanel addPnl;
	private JPanel deletePnl;
	private JPanel editPnl;
	
	
	public InventoryView() {
		
	}
	
	public void initialize() {
		setLayout(new MigLayout("", "[grow]", "[grow][grow]"));
		setBackground(new Color(246, 252, 223));
		
		
	}
	
	private JPanel table() {
		table = new JPanel();
		table.setBackground(new Color(255, 255, 255));
		return table;
	}
	
	private JPanel header() {
		header = new JPanel();
		header.setBackground(Paint.DARK_GREEN);
		return header;
	}
	
	public void refresh() {
		
	}
}
/*
 * CRUD operations for inventory items
 * 
 * TO DO:
 * 
 */