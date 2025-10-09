package com.github.ragudos.kompeter.inventory;

import com.github.ragudos.kompeter.database.dto.ItemDto;
import java.util.List;

public class InventoryController {
    private final InventoryService is;
    private final InventoryView iv;
    
    public InventoryController(InventoryService is, InventoryView iv){
        this.is = is;
        this.iv = iv;
    }
    
    public void loadItems(){
        try{
            List<ItemDto> items = is.getAllItem();
            
                    
        }catch(RuntimeException e){
            
        }
    }

}
