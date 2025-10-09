package com.github.ragudos.kompeter.inventory;

import javax.swing.JPanel;

public interface IInventory {
	void addItem(String category, String itemName, int quantity);
        void showItem();
        void deleteItem();
        void updateItem();
        void searchItem();
        void orderByDate();
        void orderByAlphabet();
        
        void refresh();
	
}
